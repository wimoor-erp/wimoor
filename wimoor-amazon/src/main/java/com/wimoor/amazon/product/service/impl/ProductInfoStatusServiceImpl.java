package com.wimoor.amazon.product.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.product.mapper.ProductInfoStatusDefineMapper;
import com.wimoor.amazon.product.pojo.entity.ProductInfoStatusDefine;
import com.wimoor.amazon.product.service.IProductInfoStatusService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;


@Service
public class ProductInfoStatusServiceImpl extends ServiceImpl<ProductInfoStatusDefineMapper, ProductInfoStatusDefine> implements IProductInfoStatusService{
	
	
	@Override
	public List<ProductInfoStatusDefine> getProStatusByShop(String shopid) {
		List<ProductInfoStatusDefine> list = this.baseMapper.getProStatusByShop(shopid);
		for(ProductInfoStatusDefine item:list) {
			if(item.getColor()!=null) {
				item.setColor(GeneralUtil.getColorType(item.getColor()).toString());
			}
		}
		return list;
	}

	@Override
	public int updateProductInfoStatus(boolean isupdate, UserInfo user, String id, String name, String remark,
			String color) {
		if("删除".equals(name) || "停售".equals(name) ||"清仓".equals(name) ) {
			throw new BizException("该店铺下已有该状态名称！请勿重复保存！");
		}
		if(isupdate) {
			//做更新
			int key = Integer.parseInt(id);
			ProductInfoStatusDefine record = this.baseMapper.selectById(key);
			if(record!=null) {
				if(StrUtil.isNotEmpty(name)) {
					record.setName(name);
				} 
				record.setIssystem(false);
				record.setOperator(user.getId());
				record.setShopid(user.getCompanyid());
				record.setOpttime(new Date());
				record.setRemark(remark);
				record.setColor(color);
				return this.baseMapper.updateById(record);
			}else {
				return 0;
			}
		}else {
			//先拿name和已有的做匹配
			QueryWrapper<ProductInfoStatusDefine> queryWrapper=new QueryWrapper<ProductInfoStatusDefine>();
			queryWrapper.eq("shopid", user.getCompanyid());
			queryWrapper.eq("name", name);
			List<ProductInfoStatusDefine> list = this.list(queryWrapper);
			if(list!=null &&list.size()>0) {
				throw new BizException("该店铺下已有该状态名称！请勿重复保存！");
			}
			//做插入
			ProductInfoStatusDefine record =new ProductInfoStatusDefine();
			if(StrUtil.isNotEmpty(name)) {
				record.setName(name);
			}else {
				throw new BizException("请输入正确的名称！");
			}
			record.setIssystem(false);
			record.setOperator(user.getId());
			record.setShopid(user.getCompanyid());
			record.setOpttime(new Date());
			record.setRemark(remark);
			record.setColor(color);
			record.setCreatetime(new Date());
			record.setCreator(user.getId());
			return this.baseMapper.insert(record);
		}
	}
	
	

	@Override
	public int deleteProductInfoStatus(String id, String shopid) {
		int count=this.baseMapper.selectStatusTotalCount(shopid,id);
		if(count>0) {
			throw new BizException("该状态下存在产品，请先置空后再操作！");
		}else {
			return this.baseMapper.deleteById(id);
		}
	}

}
