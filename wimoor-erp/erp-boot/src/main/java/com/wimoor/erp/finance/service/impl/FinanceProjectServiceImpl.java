package com.wimoor.erp.finance.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.finance.mapper.FinJournalAccountMapper;
import com.wimoor.erp.finance.mapper.FinanceProjectMapper;
import com.wimoor.erp.finance.pojo.entity.FinJournalAccount;
import com.wimoor.erp.finance.pojo.entity.FinanceProject;
import com.wimoor.erp.finance.service.IFinanceProjectService;

import lombok.RequiredArgsConstructor;
@Service("financeProjectService")
@RequiredArgsConstructor
public class FinanceProjectServiceImpl  extends ServiceImpl<FinanceProjectMapper,FinanceProject> implements IFinanceProjectService {
   final FinJournalAccountMapper finJournalAccountMapper;
	public List<FinanceProject> findProject(String shopid) {
		List<FinanceProject> list = this.baseMapper.findProject(shopid);
		if (list.size() > 0 && list != null) {
			return list;
		} else {
			return null;
		}
	}
	

	public Map<String, Object> saveProject(String name, UserInfo user) {
		Map<String, Object> map = new HashMap<String, Object>();
		if ("商品运费".equals(name) || "采购商品".equals(name)) {
			throw new BizException("添加失败!该公司下已有该项目!");
		}
		QueryWrapper<FinanceProject> queryWrapper=new QueryWrapper<FinanceProject>();
		queryWrapper.eq("shopid", user.getCompanyid());
		queryWrapper.eq("name", name);
		List<FinanceProject> list =  this.baseMapper.selectList(queryWrapper);
		if (list.size() > 0 && list != null) {
			throw new BizException("添加失败!该公司下已有该项目!");
		} else {
			FinanceProject project = new FinanceProject();
			Date nowdate = new Date();
			project.setCreatedate(nowdate);
			project.setCreator(user.getId());
			project.setIssys(false);
			project.setName(name);
			project.setOperator(user.getId());
			project.setOpttime(nowdate);
			project.setShopid(user.getCompanyid());
			int result =  this.baseMapper.insert(project);
			if (result > 0) {
				map.put("msg", "添加成功!");
			} else {
				map.put("msg", "添加失败!");
			}
			map.put("id", project.getId());
		}
		return map;
	}

	public Map<String, Object> updateProject(String id, String name, UserInfo user) {
		Map<String, Object> map = new HashMap<String, Object>();
		if ("商品运费".equals(name) || "采购商品".equals(name)) {
			throw new BizException("添加失败!该公司下已有该项目!");
		}
		if (GeneralUtil.isNotEmpty(id)) {
			QueryWrapper<FinanceProject> queryWrapper=new QueryWrapper<FinanceProject>();
			queryWrapper.eq("shopid", user.getCompanyid());
			queryWrapper.eq("name", name);
			List<FinanceProject> list =  this.baseMapper.selectList(queryWrapper);
			if (list.size() > 0 && list != null) {
				if (list.get(0).getId().equals(id)) {
					FinanceProject oldpro =  this.baseMapper.selectById(id);
					oldpro.setOperator(user.getId());
					oldpro.setOpttime(new Date());
					oldpro.setName(name);
					int result =  this.baseMapper.updateById(oldpro);
					if (result > 0) {
						map.put("msg", "更新成功!");
					} else {
						map.put("msg", "更新失败!");
					}
				} else {
					throw new BizException( "更新失败!该公司下已有该项目!");
				}
			} else {
				FinanceProject oldpro =  this.baseMapper.selectById(id);
				oldpro.setOperator(user.getId());
				oldpro.setOpttime(new Date());
				oldpro.setName(name);
				int result =  this.baseMapper.updateById(oldpro);
				if (result > 0) {
					map.put("msg", "更新成功!");
				} else {
					map.put("msg", "更新失败!");
				}
			}
		} else {
			map.put("msg", "更新失败!");
		}
		return map;
	}

	public Map<String, Object> delProject(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (GeneralUtil.isNotEmpty(id)) {
			FinanceProject finpro =  this.baseMapper.selectById(id);
			QueryWrapper<FinJournalAccount> queryWrapper=new QueryWrapper<FinJournalAccount>();
			queryWrapper.eq("projectid", finpro.getId());
			List<FinJournalAccount> oldlist =  finJournalAccountMapper.selectList(queryWrapper);
			if (oldlist != null && oldlist.size() > 0) {
				throw new BizException("删除失败!该项目已存在记录！");
			} else {
				int result =  this.baseMapper.deleteById(finpro.getId());
				if (result > 0) {
					map.put("msg", "删除成功!");
					map.put("isok", true);
				} else {
					map.put("msg", "删除失败!");
					map.put("isok", false);
				}
			}
		} else {
			map.put("msg", "删除失败!");
			map.put("isok", false);
		}
		return map;
	}


}
