package com.wimoor.erp.material.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.material.mapper.MaterialMapper;
import com.wimoor.erp.material.mapper.MaterialSupplierMapper;
import com.wimoor.erp.material.mapper.MaterialSupplierStepwiseMapper;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.pojo.entity.MaterialSupplier;
import com.wimoor.erp.material.pojo.entity.MaterialSupplierStepwise;
import com.wimoor.erp.material.pojo.entity.StepWisePrice;
import com.wimoor.erp.material.pojo.vo.MaterialSupplierVO;
import com.wimoor.erp.material.service.IMaterialSupplierService;
import com.wimoor.erp.material.service.IStepWisePriceService;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class MaterialSupplierServiceImpl extends  ServiceImpl<MaterialSupplierMapper,MaterialSupplier> implements IMaterialSupplierService {
	
    final MaterialMapper materialMapper;
    final MaterialSupplierStepwiseMapper materialSupplierStepwiseMapper;
	final IStepWisePriceService stepWisePriceService;
	 
	public MaterialSupplier getDefaultSupplier(String materialid) {
		QueryWrapper<MaterialSupplier> queryWrapper=new QueryWrapper<MaterialSupplier>();
  		queryWrapper.eq("materialid",materialid);
  		queryWrapper.eq("isdefault",true);
  		return this.baseMapper.selectOne(queryWrapper);
	}
	public void handleDefaultSspplier(String supplierid,Material material,String oldsupid,Integer moq,UserInfo user) {
		boolean isnew=true;
		List<MaterialSupplier> otherlist = this.selectSupplierByMaterialId(material.getId());
		if(otherlist!=null && otherlist.size()>0) {
			for(MaterialSupplier item:otherlist) {
				if(supplierid.equals(item.getSupplierid())) {
					isnew=false;
					if(item.getIsdefault()==false) {
						//默认的在备选里面存在  就更新成默认的
						item.setMOQ(moq);
						item.setIsdefault(true);
						this.baseMapper.updateById(item);
					}
				}else{
					//其它的设置成不是默认
					item.setIsdefault(false);
					this.baseMapper.updateById(item);
				}
			}
		}
		if(isnew) {
			//新增当前的supplierid为默认
			MaterialSupplier entity=new MaterialSupplier();
			entity.setMaterialid(material.getId());
			entity.setSupplierid(supplierid);
			entity.setMOQ(moq);
			entity.setOperator(user.getId());
			entity.setOpttime(new Date());
			entity.setCreatedate(new Date());
			entity.setCreater(user.getId());
			entity.setIsdefault(true);
			this.baseMapper.insert(entity);
		}
	}
	
	public void updatePriceOfDefaultSupplier(UserInfo user,List<MaterialSupplierStepwise> prices) {
		//默认供应商id  
		String supplierid=null;
		    Map<String,List<MaterialSupplierStepwise>> materialmap=new HashMap<String,List<MaterialSupplierStepwise>>();
		    for(MaterialSupplierStepwise item:prices) {
		    	if(StrUtil.isNotEmpty(item.getSupplierid())) {
		    		supplierid=item.getSupplierid();
		    	}
		    	List<MaterialSupplierStepwise> list=materialmap.get(item.getMaterialid());
		    	if(list==null) {
		    		list=new LinkedList<MaterialSupplierStepwise>();
		    	}
		    	list.add(item);
		    	materialmap.put(item.getMaterialid(),list);
		    }
		    for(Entry<String, List<MaterialSupplierStepwise>> priceentry:materialmap.entrySet()) {
		    	String materialid=priceentry.getKey();
		    	List<MaterialSupplierStepwise> list = priceentry.getValue();
		    	MaterialSupplier supplier = this.getDefaultSupplier(materialid);
		    	Material material = materialMapper.selectById(materialid);
		   	    Integer moq=0;
		   	    BigDecimal maxprice = new BigDecimal("0");
		    	String supid=null;
		    	if(supplier!=null) {
		    		supid=supplier.getSupplierid();
		    	}else {
		    		supid=material.getSupplier();
		    	}
		    	List<MaterialSupplierStepwise> pricelist = list;
		        if(supid!=null) {
			    	QueryWrapper<MaterialSupplierStepwise> queryWrapper2=new QueryWrapper<MaterialSupplierStepwise>();
			  		queryWrapper2.eq("materialid",materialid);
			  		queryWrapper2.eq("supplierid", supid);
			  		materialSupplierStepwiseMapper.delete(queryWrapper2);
		        }
		        material.setSupplier(supplierid);
		        	
				    stepWisePriceService.deleteByMaterial(materialid);
					if(pricelist!=null && pricelist.size()>0) {
						for(int j=0;j<pricelist.size();j++) {
							MaterialSupplierStepwise step = pricelist.get(j);
							int amount= step.getAmount();
							BigDecimal price=step.getPrice();
							if (price.toString().split("\\.").length > 1 && price.toString().split("\\.")[1].length() > 2) {
								throw new ERPBizException("阶梯采购采购价不能超过两位小数！");
							}
							if(j==0 && (moq==null || moq<=0)) {
								moq=amount;
							}
							MaterialSupplierStepwise stepWisePrice=new MaterialSupplierStepwise();
							stepWisePrice.setAmount(amount);
							stepWisePrice.setPrice(price);
							stepWisePrice.setMaterialid(material.getId());
							stepWisePrice.setOperator(user.getId());
							stepWisePrice.setOpttime(new Date());
							stepWisePrice.setSupplierid(supplierid);
							materialSupplierStepwiseMapper.insert(stepWisePrice);
						}
					}
				  material.setMOQ(moq);
				  if(material.getPrice()==null||material.getPrice().compareTo(maxprice)<0) {
						material.setPrice(maxprice);
				   }
				  this.handleDefaultSspplier(supplierid,material,supid,moq,user);	
			     materialMapper.updateById(material);
		    }
	  		
  		}
	
  //供应商列表
  	public void saveOrUpdateSupplier(List<MaterialSupplierVO> supplierList,UserInfo user,String id,Material material) {
  		//一进来先删除当前的列表 以最新的supplierlist为准
  		QueryWrapper<MaterialSupplier> queryWrapper=new QueryWrapper<MaterialSupplier>();
  		queryWrapper.eq("materialid",id);
  		this.baseMapper.delete(queryWrapper);
  	 
  		QueryWrapper<MaterialSupplierStepwise> queryWrapper2=new QueryWrapper<MaterialSupplierStepwise>();
  		queryWrapper2.eq("materialid",id);
  		materialSupplierStepwiseMapper.delete(queryWrapper2);
  		stepWisePriceService.deleteByMaterial(id);
  	 
  			if(supplierList!=null && supplierList.size()>0) {
  				for(int i=0;i<supplierList.size();i++) {
  					MaterialSupplierVO item = supplierList.get(i);
  					MaterialSupplier materialSupplier=new MaterialSupplier();
  					String supid = item.getSupplierid();
  					boolean isdefault=item.getIsdefault();
  					String procode = item.getProductCode();
  					BigDecimal costother= item.getOtherCost();
  					String purchaseurl = item.getPurchaseUrl();
  					BigDecimal badRate= item.getBadrate();
  					String remark=item.getRemark();
  					Integer moq=item.getMOQ();
  					List<MaterialSupplierStepwise> pricelist = item.getStepList();
  					BigDecimal maxprice = new BigDecimal("0");
  					if(pricelist!=null && pricelist.size()>0) {
  						for(int j=0;j<pricelist.size();j++) {
  							MaterialSupplierStepwise step = pricelist.get(j);
  							int amount= step.getAmount();
  							if(j==0 && (moq==null || moq<=0)) {
  								moq=amount;
  							}
  							BigDecimal price=step.getPrice();
  							if (price.toString().split("\\.").length > 1 && price.toString().split("\\.")[1].length() > 2) {
  								throw new ERPBizException("阶梯采购采购价不能超过两位小数！");
  							}
  							if (maxprice.compareTo(price) < 0) {
  								maxprice = price;
  							}
  							MaterialSupplierStepwise stepwise=new MaterialSupplierStepwise();
  							stepwise.setAmount(amount);
  							stepwise.setMaterialid(id);
  							stepwise.setOperator(user.getId());
  							stepwise.setOpttime(new Date());
  							stepwise.setPrice(price);
  							stepwise.setSupplierid(supid);
  							materialSupplierStepwiseMapper.insert(stepwise);
  						}
  					}
  					if(isdefault==true) {
  						if(badRate!=null && badRate.compareTo(BigDecimal.ZERO)>0) {
  							material.setBadrate(badRate.floatValue());
  						}
  						material.setSupplier(supid);
  						material.setOtherCost(costother);
  						material.setMOQ(moq);
  						if(material.getPrice()==null) {
  							material.setPrice(maxprice);
  						}
  						material.setProductCode(procode);
  						material.setPurchaseUrl(purchaseurl);
  						material.setDeliveryCycle(item.getDeliverycycle());
  						if(material.getId()!=null) {
  							materialMapper.updateById(material);
  						}
  						if(pricelist!=null && pricelist.size()>0) {
  							for(int j=0;j<pricelist.size();j++) {
  								MaterialSupplierStepwise step = pricelist.get(j);
  								int amount= step.getAmount();
  								BigDecimal price=step.getPrice();
  								if (price.toString().split("\\.").length > 1 && price.toString().split("\\.")[1].length() > 2) {
  									throw new ERPBizException("阶梯采购采购价不能超过两位小数！");
  								}
  								StepWisePrice stepWisePrice = new StepWisePrice();
  								stepWisePrice.setAmount(amount);
  								stepWisePrice.setPrice(price);
  								stepWisePrice.setMaterial(id);
  								stepWisePrice.setOperator(user.getId());
  								stepWisePriceService.save(stepWisePrice);
  							}
  						}
  						
  					}
  					materialSupplier.setRemark(remark);
  					materialSupplier.setCreatedate(new Date());
  					materialSupplier.setCreater(user.getId());
  					materialSupplier.setIsdefault(isdefault);
  					materialSupplier.setMaterialid(id);
  					materialSupplier.setSupplierid(supid);
  					materialSupplier.setOperator(user.getId());
  					materialSupplier.setOpttime(new Date());
  					materialSupplier.setOthercost(costother);
  					materialSupplier.setProductcode(procode);
  					materialSupplier.setPurchaseurl(purchaseurl);
  					materialSupplier.setDeliverycycle(item.getDeliverycycle());
  					if(badRate!=null && badRate.compareTo(BigDecimal.ZERO)>0) {
  						materialSupplier.setBadrate(badRate.floatValue());
  					}
  					materialSupplier.setMOQ(moq);
  					this.baseMapper.insert(materialSupplier);
  				}
  				
  			}else {
  				material.setSupplier(null);
  				material.setOtherCost(null);
  				material.setMOQ(0);
  				material.setProductCode(null);
  				material.setPurchaseUrl(null);
  				if(material.getId()!=null) {
  					materialMapper.updateById(material);
  				}
  			}
  		}
	public List<MaterialSupplierVO> selectSupplierByMainmid(String id) {
		List<MaterialSupplierVO> supplierList=this.baseMapper.selectSupplierByMainId(id);
		if(supplierList!=null && supplierList.size()>0) {
			for(MaterialSupplierVO item:supplierList) {
				String supid = item.getSupplierid();
				if(GeneralUtil.isNotEmpty(supid)) {
					List<MaterialSupplierStepwise> stepList=materialSupplierStepwiseMapper.selectSupplierByMainId(id,supid);
					if(stepList!=null && stepList.size()>0) {
						item.setStepList(stepList);
					}
				}
			}
			return supplierList;
		}else {
			return null;
		}
	}
	
	
    public List<Map<String, Object>> selectSupplerOtherById(String id) {
		List<Map<String, Object>> supplierList=this.baseMapper.selectSupplerOtherById(id);
		if(supplierList!=null && supplierList.size()>0) {
			for(Map<String, Object> item:supplierList) {
				String supid = item.get("supplierid").toString();
				if(GeneralUtil.isNotEmpty(supid)) {
					List<MaterialSupplierStepwise> stepList=materialSupplierStepwiseMapper.selectSupplierByMainId(id,supid);
					if(stepList!=null && stepList.size()>0) {
						item.put("stepList", stepList);
					}
				}
			}
			return supplierList;
		}else {
			return null;
		}
	}

	public List<MaterialSupplier> selectSupplierByMaterialId(String id) {
		QueryWrapper<MaterialSupplier> queryWrapper=new QueryWrapper<MaterialSupplier>();
		queryWrapper.eq("materialid",id);
		return  this.baseMapper.selectList(queryWrapper);
	}
	
	public MaterialSupplier selectSupplier(String materialid,String supplierid) {
		QueryWrapper<MaterialSupplier> queryWrapper=new QueryWrapper<MaterialSupplier>();
		queryWrapper.eq("materialid",materialid);
		queryWrapper.eq("supplierid",supplierid);
		return  this.baseMapper.selectOne(queryWrapper);
	}
	
	public int deleteMaterialSupplierStepwise(String materialid,String supplierid) {
		QueryWrapper<MaterialSupplierStepwise> queryWrapper=new QueryWrapper<MaterialSupplierStepwise>();
		queryWrapper.eq("materialid",materialid);
		queryWrapper.eq("supplierid",supplierid);
		return materialSupplierStepwiseMapper.delete(queryWrapper);
	}

	public int saveMaterialSupplierStepwise(MaterialSupplierStepwise mss) {
		// TODO Auto-generated method stub
	 	return materialSupplierStepwiseMapper.insert(mss);
	}
	
	@Override
	public Map<String, Object> getPriceBySupplier(String supplierid, String materialid, Integer amount) {
		if(amount!=null && StrUtil.isNotEmpty(materialid) && StrUtil.isNotEmpty(supplierid)) {
			Map<String, Object> map = new HashMap<String, Object>();
			Material material = materialMapper.selectById(materialid);
			QueryWrapper<MaterialSupplierStepwise> queryWrapper=new QueryWrapper<MaterialSupplierStepwise>();
			queryWrapper.eq("materialid", materialid);
			queryWrapper.eq("supplierid", supplierid);
			List<MaterialSupplierStepwise> stepwiseprice = materialSupplierStepwiseMapper.selectList(queryWrapper);
			BigDecimal itemprice = new BigDecimal("0");
			Integer needamount = 0;
			if (stepwiseprice != null && stepwiseprice.size() > 0) {
				itemprice = stepwiseprice.get(0).getPrice();
				needamount = stepwiseprice.get(0).getAmount();
				for (int step = 0; step < stepwiseprice.size(); step++) {
					if (stepwiseprice.get(step).getAmount() < amount + 1) {
						if (needamount < stepwiseprice.get(step).getAmount()) {
							needamount = stepwiseprice.get(step).getAmount();
							itemprice = stepwiseprice.get(step).getPrice();
						}
					}
				}
			}
			map.put("itemprice", itemprice);
			map.put("needamount", needamount);
			if(material.getPrice()!=null) {
				map.put("price", material.getPrice());
			}
			return map;
		}else {
			return null;
		}
	}

}
