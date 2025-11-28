package com.wimoor.amazon.adv.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.Order.Direction;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.common.mvc.BizException;

import tk.mybatis.mapper.util.StringUtil;

public abstract class ERPBaseController<T> {
	public abstract IService<T> getServeice();

	public abstract Object getListData(HttpServletRequest request, Model model) throws BizException;
 

	public static PageList<Map<String, Object>> getPage(List<Map<String, Object>> list, PageBounds pageBounds) {
		int totalRows = list.size();
		int pageSize = pageBounds.getLimit();
		int page = pageBounds.getPage();
		int totalPages = totalRows / pageSize;
		if (totalRows % pageSize != 0) {
			totalPages = totalPages + 1;
		}
		int pageEndRow;
		int pageStartRow;
		if (totalRows > 0) {
			if (page * pageSize < totalRows) {// 判断是否为最后一页
				pageEndRow = page * pageSize;
				pageStartRow = pageEndRow - pageSize;
			} else {
				pageEndRow = totalRows;
				pageStartRow = pageSize * (totalPages - 1);
			}
			List<Order> orders = pageBounds.getOrders();
			Direction tdirects = null;
			String tfield = "";
			if (orders != null && orders.size() > 0) {
				Order order = orders.get(0);
				tdirects = order.getDirection();
				tfield = order.getProperty();
			}
			final String field = tfield;
			final Direction direct = tdirects;
			Collections.sort(list, new Comparator<Map<String, Object>>() {
				public int compare(Map<String, Object> o1, Map<String, Object> o2) {
					Object object = o1.get(field);
					Object object2 = o2.get(field);
					if (object == null && object2 == null)
						return 0;
					if (direct != null && Order.Direction.DESC == direct) {
						if (object == null) {
							return 1;
						}
						if (object2 == null) {
							return -1;
						}
						if (object instanceof BigDecimal) {
							BigDecimal obj2 = (BigDecimal) object2;
							BigDecimal obj = (BigDecimal) object;
							return obj2.compareTo((BigDecimal) obj);
						} else if (object instanceof Integer) {
							Integer obj2 = (Integer) object2;
							Integer obj = (Integer) object;
							return obj2.compareTo(obj);
						} else if (object instanceof Float) {
							Float obj2 = (Float) object2;
							Float obj = (Float) object;
							return obj2.compareTo(obj);
						} else if (object instanceof Double) {
							Double obj2 = (Double) object2;
							Double obj = (Double) object;
							return obj2.compareTo(obj);
						} else
							return object2.toString().compareTo(object.toString());
					} else {
						if (object == null) {
							return -1;
						}
						if (object2 == null) {
							return 1;
						}
						if (object instanceof BigDecimal) {
							BigDecimal obj2 = (BigDecimal) object2;
							BigDecimal obj = (BigDecimal) object;
							return obj.compareTo((BigDecimal) obj2);
						} else if (object instanceof Integer) {
							Integer obj2 = (Integer) object2;
							Integer obj = (Integer) object;
							return obj.compareTo(obj2);
						} else if (object instanceof Float) {
							Float obj2 = (Float) object2;
							Float obj = (Float) object;
							return obj.compareTo(obj2);
						} else if (object instanceof Double) {
							Double obj2 = (Double) object2;
							Double obj = (Double) object;
							return obj.compareTo(obj2);
						} else {
							return object.toString().compareTo(object2.toString());
						}
					}
				}
			});
			if (!list.isEmpty()) {
				list = list.subList(pageStartRow, pageEndRow);
			}
		}
		Paginator paginator = new Paginator(page, pageSize, totalRows);
		PageList<Map<String, Object>> result = new PageList<Map<String, Object>>(list, paginator);
		return result;
	}

	public static PageBounds getPageBounds(HttpServletRequest request) {
		String sort;
		String order;
		Integer offset = null;
		Integer limit = null;
		order = request.getParameter("order");
		sort = request.getParameter("sort");
		if (request.getParameter("offset") != null) {
			offset = Integer.parseInt(request.getParameter("offset"));
		}
		if (request.getParameter("limit") != null) {
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		int page = 1; // 页号
		Integer pageSize = 1900000000; // 每页数据条数
		PageBounds pageBounds = null;
		if (offset == null) {
			offset = 0;
		}
		if (limit != null) {
			pageSize = limit;
		}
		if (pageSize > 0) {
			page = (offset / pageSize) + 1;
		}
		if(StringUtil.isNotEmpty(sort)) {
			String[] sortarray = sort.split(",");
			String[] orderarray =null;
			if(StringUtil.isNotEmpty(sort)) {
				orderarray=order.split(",");
			}
		    List<Order> orderlist=new ArrayList<Order>();
			for(int i=0;i<sortarray.length;i++) {
				Direction orderdirect = Order.Direction.DESC;
				if (orderarray!=null&&orderarray.length>i&&StringUtil.isNotEmpty(orderarray[i])&& "asc".contains(orderarray[i])) {
					orderdirect = Order.Direction.ASC;
				} 
				Order orderObject = new Order(sortarray[i], orderdirect, "? ");
				orderlist.add(orderObject);
			}
			pageBounds = new PageBounds(page, pageSize, orderlist, true);
		}  else {
			pageBounds = new PageBounds(page, pageSize, null, true);
		}
		return pageBounds;
	}

	@ResponseBody
	@RequestMapping(value = "getColumns")
	Map<String, String> getColumns(HttpServletRequest request, Model model) {
		Map<String, String> result = getServeice().getColumnMap();
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "list")
	Object getList(HttpServletRequest request, Model model) throws BizException {
		return getListData(request, model);
	}

	@ResponseBody
	@RequestMapping(value = "findObjectById")
	Object findObjectByIdAction(HttpServletRequest request, Model model) throws BizException {
		String key = request.getParameter("id");
		T object = getServeice().selectByKey(key);
		if (object == null) {
			return "";
		}
		return object;
	}

	String searchView() {
		return "/pages/frame/blank";
	}

	@RequestMapping(value = "searchview")
	String searchViewAction(HttpServletRequest request, Model model) throws BizException {
		Enumeration<String> em = request.getParameterNames();
		String option = "";
		while (em.hasMoreElements()) {
			String name = (String) em.nextElement();
			String value = request.getParameter(name);
			option = option + name + "=" + value + "&";
		}
		if (!"".equals(option)) {
			option = option.substring(0, option.length() - 1);
			option = "?" + option;
		}
		request.setAttribute("option", option);
		return searchView();
	}

	public void beforeDelete(HttpServletRequest request, String id) throws BizException {
	};

	@ResponseBody
	@RequestMapping(value = "delete")
	Object deleteAction(HttpServletRequest request, Model model) throws BizException {
		String ids = request.getParameter("ids");
		String[] idlist = ids.split(",");
		for (int i = 0; i < idlist.length; i++) {
			String id = idlist[i];
			if (StringUtil.isNotEmpty(id)) {
				beforeDelete(request, id);
				getServeice().delete(id);
			}
		}
		return null;
	}
}
