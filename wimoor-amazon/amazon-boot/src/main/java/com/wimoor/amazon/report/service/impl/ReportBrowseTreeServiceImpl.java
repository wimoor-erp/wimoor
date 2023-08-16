package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.amazon.spapi.api.ReportsApi;
import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.reports.CreateReportResponse;
import com.amazon.spapi.model.reports.CreateReportSpecification;
import com.amazon.spapi.model.reports.ReportOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.report.mapper.AmzBrowseNodeMapper;
import com.wimoor.amazon.report.pojo.entity.AmzBrowseNode;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.util.XmlOper;

import cn.hutool.core.util.StrUtil;
 
@Service("reportBrowseTreeService")
public class ReportBrowseTreeServiceImpl extends ReportServiceImpl {

	@Resource
	private IMarketplaceService marketplaceService;
	
	@Resource
	private AmzBrowseNodeMapper amzBrowseNodeMapper;

 
	public void   requestReport(AmazonAuthority amazonAuthority,Calendar cstart,Calendar cend,Boolean ignore) {
		  ReportsApi api = apiBuildService.getReportsApi(amazonAuthority);
		  List<Marketplace> marketlist = marketplaceService.findbyauth(amazonAuthority.getId());
		  if(cstart!=null) {
			  for(Marketplace market:marketlist) {
				  amazonAuthority.setMarketPlace(market);
				  CreateReportSpecification body=new CreateReportSpecification();
				  body.setReportType(myReportType());
				  List<String> list=new ArrayList<String>();
				  list.add(market.getMarketplaceid());
				  amazonAuthority.setMarketPlace(market);
				  body.setMarketplaceIds(list);
				  ReportOptions options=new ReportOptions();
				  options.put("RootNodesOnly", "true");
				  body.setReportOptions(options);
				  try {
						  ApiCallback<CreateReportResponse> response = new ApiCallbackReportCreate(this,amazonAuthority,market,cstart.getTime(),cend.getTime());
					      api.createReportAsync(body,response);
					  } catch (ApiException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				    }
			  }
		  }else {
			  for (Marketplace marketplace : marketlist) {
					QueryWrapper<AmzBrowseNode> query = new QueryWrapper<AmzBrowseNode>();
					query.eq("level", 1);
					query.eq("country", marketplace.getMarket());
					List<AmzBrowseNode> nodelist = amzBrowseNodeMapper.selectList(query);
					if (nodelist.size() > 0) {
						this.requestReportByNodeId(amazonAuthority, nodelist);
					}
				}
		  }
		
}

	public void requestReportByNodeId(AmazonAuthority amazonAuthority, List<AmzBrowseNode> nodelist) {
		 ReportsApi api = apiBuildService.getReportsApi(amazonAuthority);
		 for(AmzBrowseNode node: nodelist){
			  CreateReportSpecification body=new CreateReportSpecification();
			  body.setReportType(myReportType());
			  List<String> list=new ArrayList<String>();
			  list.add(amazonAuthority.getMarketPlace().getMarketplaceid());
			  Marketplace market = amazonAuthority.getMarketPlace();
			  body.setMarketplaceIds(list);
			  ReportOptions options=new ReportOptions();
			  options.put("BrowseNodeId",node.getId().toString());
			  options.put("MarketplaceId",amazonAuthority.getMarketPlace().getMarketplaceid());
			  body.setReportOptions(options);
			  try {
					  ApiCallback<CreateReportResponse> response = new ApiCallbackReportCreate(this,amazonAuthority,market,null,null);
				      api.createReportAsync(body,response);
				  } catch (ApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			    }
		}
	 
	}
	
	public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br) {
		StringBuffer log = new StringBuffer();
		InputStream is = null;
		String country = amazonAuthority.getMarketPlace().getMarket();
		try {
			SAXReader reader = new SAXReader();
			org.dom4j.Document doc = reader.read(br);
			if (doc == null) {
				return null;
			}
			org.dom4j.Element root = doc.getRootElement();
			Iterator<Element> it = root.elementIterator();
			List<Element> nodelist = new ArrayList<Element>();
			String query = null;
			// 遍历迭代器，获取根节点中的信息
			while (it.hasNext()) {
				Element node = it.next();
				String name = node.getName();
				if (name.equals("query")) {
					query = node.getStringValue();
					if (StrUtil.isEmpty(query)) {
						return null;
					}
					log.append("query="+query);
				} else if (name.equals("Node")) {
					nodelist.add(node);
				}
			}
			try {
				if ("RootsOnlyRequest".equals(query)) {
					getRootBrowseNodeList(nodelist, country);
				} else {
					String log2 = getAmzBrowseNodeList(nodelist, country);
					if(log2!=null && log2.length()>0){
						log.append(log2);
					}
				}
			} catch (Exception e) {
				log.append(",AmzAdvBrowseNode更新失败。" + e.getMessage());
				e.printStackTrace();
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return log.toString();
	}


	public List<AmzBrowseNode> getRootBrowseNodeListByDoc(Document doc, String country) {
		List<AmzBrowseNode> list = new ArrayList<AmzBrowseNode>();
		NodeList nl = doc.getElementsByTagName("Node");
		if (nl == null || nl.getLength() == 0) {
			return null;
		}
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			Node browseNode = XmlOper.getNodeByName(node, "browseNodeId");
			String browseNodeId = browseNode.getTextContent();
			if (browseNodeId != null) {
				Node browseNodeStoreContextNameNode = XmlOper.getNodeByName(node, "browseNodeStoreContextName");
				String browseNodeStoreContextName = browseNodeStoreContextNameNode.getTextContent();
				AmzBrowseNode nodeentity = new AmzBrowseNode();
				nodeentity.setId(new BigInteger(browseNodeId));
				nodeentity.setName(browseNodeStoreContextName);
				nodeentity.setCountry(country);
				nodeentity.setLevel(1);
				nodeentity.setRefreshtime(new Date());
				list.add(nodeentity);
			}
		}

		if (list == null || list.size() == 0) {
			return null;
		} else {
			QueryWrapper<AmzBrowseNode> query = new QueryWrapper<AmzBrowseNode>();
			query.eq("level", 1);
			query.eq("country", country);
			amzBrowseNodeMapper.delete(query);
			amzBrowseNodeMapper.insertBatch(list);
		}
		return list;
	}

	public void getAmzBrowseNodeListByDoc(Document doc, String country) {
		NodeList nl = doc.getElementsByTagName("Node");
		if (nl == null) {
			return;
		}
		for (int i = 0; i < nl.getLength(); i++) {
			AmzBrowseNode nodeParent = null;
			Node node = nl.item(i);
			Node browseNode = XmlOper.getNodeByName(node, "browseNodeId");
			String browseNodeId = browseNode.getTextContent();
			Node browseNodeStoreContextNameNode = XmlOper.getNodeByName(node, "browseNodeStoreContextName");
			String browseNodeStoreContextName = browseNodeStoreContextNameNode.getTextContent();
			if (browseNodeId != null) {
				nodeParent = amzBrowseNodeMapper.selectById(browseNodeId);
				if (nodeParent == null) {
					nodeParent = new AmzBrowseNode();
					nodeParent.setId(new BigInteger(browseNodeId));
					nodeParent.setName(browseNodeStoreContextName);
					nodeParent.setCountry(country);
					nodeParent.setLevel(1);
					nodeParent.setRefreshtime(new Date());
					amzBrowseNodeMapper.insert(nodeParent);
				} else if (StrUtil.isEmpty(nodeParent.getName())
						&& StrUtil.isNotEmpty(browseNodeStoreContextName)) {
					nodeParent.setName(browseNodeStoreContextName);
					nodeParent.setRefreshtime(new Date());
					amzBrowseNodeMapper.updateById(nodeParent);
				}
			}
			Node hasChildrenNode = XmlOper.getNodeByName(node, "hasChildren");
			String hasChildren = hasChildrenNode.getTextContent();
			if (hasChildren != null && hasChildren.equals("true")) {
				Node childNodes = XmlOper.getNodeByName(node, "childNodes");
				if (childNodes != null && childNodes.getChildNodes() != null) {
					NodeList ids = childNodes.getChildNodes();
					for (int j = 0; j < ids.getLength(); j++) {
						Node idnode = ids.item(i);
						String id = idnode.getTextContent();
						AmzBrowseNode nodechild = amzBrowseNodeMapper.selectById(id);
						if (nodechild == null) {
							nodechild = new AmzBrowseNode();
							nodechild.setId(new BigInteger(id));
							nodechild.setCountry(country);
							nodechild.setParentid(nodeParent.getId());
							nodechild.setLevel(nodeParent.getLevel() + 1);
							nodechild.setRefreshtime(new Date());
							amzBrowseNodeMapper.insert(nodechild);
						} else {
							nodechild.setCountry(country);
							nodechild.setParentid(nodeParent.getId());
							nodechild.setLevel(nodeParent.getLevel() + 1);
							nodechild.setRefreshtime(new Date());
							amzBrowseNodeMapper.updateById(nodechild);
							updateNodeChlidLevel(nodechild);
						}
					}
				}
			}

		}
	}

	public void updateNodeChlidLevel(AmzBrowseNode nodechild) {
		List<AmzBrowseNode> chlidlist = amzBrowseNodeMapper.selcetByParentId(nodechild.getId());
		if (chlidlist != null && chlidlist.size() > 0) {
			for (AmzBrowseNode ch : chlidlist) {
				ch.setParentid(nodechild.getId());
				ch.setLevel(nodechild.getLevel() + 1);
				ch.setRefreshtime(new Date());
				amzBrowseNodeMapper.updateById(ch);
				updateNodeChlidLevel(ch);
			}
		}
	}

	public List<AmzBrowseNode> getRootBrowseNodeList(List<Element> nl, String country) {
		if (nl == null || nl.size() == 0) {
			return null;
		}
		List<AmzBrowseNode> list = new ArrayList<AmzBrowseNode>();
		Set<String> idset = new HashSet<String>();
		for (int i = 0; i < nl.size(); i++) {
			Element node = nl.get(i);
			String browseNodeId = null;
			String browseNodeStoreContextName = null;
			String browsePathByName = null;
			Iterator<Element> it = node.elementIterator();
			while (it.hasNext()) {
				Element child = it.next();
				if ("browseNodeId".equals(child.getName())) {
					browseNodeId = child.getStringValue();
				}
				if ("browseNodeStoreContextName".equals(child.getName())) {
					browseNodeStoreContextName = child.getStringValue();
				}
				if ("browsePathByName".equals(child.getName())) {
					browsePathByName = child.getStringValue();
				}
			}
			if (StrUtil.isEmpty(browseNodeStoreContextName)) {
				browseNodeStoreContextName = browsePathByName;
			}
			if (StrUtil.isNotEmpty(browseNodeId)) {
				if(idset.contains(browseNodeId)){
					continue;
				} else{
					idset.add(browseNodeId);
				}
				AmzBrowseNode nodeentity = new AmzBrowseNode();
				nodeentity.setId(new BigInteger(browseNodeId));
				nodeentity.setName(browseNodeStoreContextName);
				nodeentity.setCountry(country);
				nodeentity.setLevel(1);
				nodeentity.setRefreshtime(new Date());
				list.add(nodeentity);
			}
		}
		if (list == null || list.size() == 0) {
			return null;
		} else {
			QueryWrapper<AmzBrowseNode> query = new QueryWrapper<AmzBrowseNode>();
			query.eq("level", 1);
			query.eq("country", country);
			amzBrowseNodeMapper.delete(query);
			amzBrowseNodeMapper.insertBatch(list);
		}
		return list;
	}

	public String getAmzBrowseNodeList(List<Element> nl, String country) {
		StringBuffer log = new StringBuffer();
		if (nl == null || nl.size() == 0) {
			return null;
		}
		for (int i = 0; i < nl.size(); i++) {
			Element node = nl.get(i);
			AmzBrowseNode nodeParent = null;
			String browseNodeId = null;
			String browseNodeStoreContextName = null;
			String browsePathByName = null;
			String hasChildren = null;
			Element childNodes = null;
			Iterator<Element> it = node.elementIterator();
			while (it.hasNext()) {
				Element child = it.next();
				if ("browseNodeId".equals(child.getName())) {
					browseNodeId = child.getStringValue();
				}
				if ("browseNodeStoreContextName".equals(child.getName())) {
					browseNodeStoreContextName = child.getStringValue();
				}
				if ("browsePathByName".equals(child.getName())) {
					browsePathByName = child.getStringValue();
				}
				if ("hasChildren".equals(child.getName())) {
					hasChildren = child.getStringValue();
				}
				if ("childNodes".equals(child.getName())) {
					childNodes = child;
				}
			}
			if (StrUtil.isEmpty(browseNodeStoreContextName)) {
				browseNodeStoreContextName = browsePathByName;
			}
			if (StrUtil.isNotEmpty(browseNodeId)) {
				nodeParent = amzBrowseNodeMapper.selectById(browseNodeId);
				if (nodeParent == null) {
					nodeParent = new AmzBrowseNode();
					nodeParent.setId(new BigInteger(browseNodeId));
					nodeParent.setName(browseNodeStoreContextName);
					nodeParent.setCountry(country);
					nodeParent.setLevel(1);
					nodeParent.setRefreshtime(new Date());
					try {
						amzBrowseNodeMapper.insert(nodeParent);
					} catch (Exception e) {
						e.printStackTrace();
						log.append("。"+browseNodeId+",insert失败，"+e.getMessage());
					}
				} else if (StrUtil.isNotEmpty(browseNodeStoreContextName) && 
						(StrUtil.isEmpty(nodeParent.getName()) || !nodeParent.getName().equals(browseNodeStoreContextName))){
					nodeParent.setName(browseNodeStoreContextName);
					nodeParent.setRefreshtime(new Date());
					try {
						amzBrowseNodeMapper.updateById(nodeParent);
					} catch (Exception e) {
						e.printStackTrace();
						log.append("。"+browseNodeId+",update失败，"+e.getMessage());
					}
				}
				if (hasChildren != null && hasChildren.equals("true")) {
					if (childNodes != null) {
						Iterator<Element> childit = childNodes.elementIterator();
						while (childit.hasNext()) {
							Element idnode = childit.next();
							String id = idnode.getStringValue();
							AmzBrowseNode nodechild = amzBrowseNodeMapper.selectById(id);
							if (nodechild == null) {
								nodechild = new AmzBrowseNode();
								nodechild.setId(new BigInteger(id));
								nodechild.setCountry(country);
								nodechild.setParentid(nodeParent.getId());
								nodechild.setLevel(nodeParent.getLevel() + 1);
								nodechild.setRefreshtime(new Date());
								amzBrowseNodeMapper.insert(nodechild);
							} else {
								nodechild.setCountry(country);
								nodechild.setParentid(nodeParent.getId());
								nodechild.setLevel(nodeParent.getLevel() + 1);
								nodechild.setRefreshtime(new Date());
								amzBrowseNodeMapper.updateById(nodechild);
								updateNodeChlidLevel(nodechild);
							}
						}
					}
				}
			}
		}
		return log.toString();
	}

	@Override
	public String myReportType() {
		return ReportType.BrowseTreeReport;
	}

}
