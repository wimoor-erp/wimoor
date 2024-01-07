package com.wimoor.amazon.profit.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.api.AdminClientOneFeignManager;

import com.wimoor.amazon.profit.mapper.IndividualFeeMapper;
import com.wimoor.amazon.profit.mapper.InplaceFeeMapper;
import com.wimoor.amazon.profit.mapper.ManualProcessingFeeMapper;
import com.wimoor.amazon.profit.mapper.ProfitConfigMapper;
import com.wimoor.amazon.profit.pojo.entity.InplaceFee;
import com.wimoor.amazon.profit.pojo.entity.ManualProcessingFee;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfig;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfigCountry;
import com.wimoor.amazon.profit.service.IProfitCfgCountryService;
import com.wimoor.amazon.profit.service.IProfitCfgService;
import com.wimoor.amazon.profit.service.IProfitService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.util.UUIDUtil;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

@Service("profitCfgService")
@RequiredArgsConstructor
public class ProfitCfgServiceImpl extends ServiceImpl<ProfitConfigMapper, ProfitConfig> implements IProfitCfgService {
	@Resource
	IProfitCfgCountryService profitCfgCountryService;
	@Resource
	@Lazy
	IProfitService profitService;
	final AdminClientOneFeignManager adminClientOneFeign;
	final IndividualFeeMapper individualFeeMapper;
	final InplaceFeeMapper inplaceFeeMapper;
	final ManualProcessingFeeMapper manualProcessingFeeMapper;
	
	@Caching(evict={@CacheEvict(value = "defaultProfitCfgCache", allEntries = true),
            @CacheEvict(value = "profitCfgCache", allEntries = true)})
	public String update(ProfitConfig config) {
		String msg = null;
		int a = updateSelective(config);
		if (a > 0) {
			ProfitConfigCountry uk = config.getProfitConfigCountry("UK");
			uk.setFenpeiType("PAN_EU");
			uk.setVatRate(new BigDecimal("20"));
			uk.setWarehousesite("uk");
			uk.setCountry("UK");
			uk.setProfitid(config.getId());
			ProfitConfigCountry temp =config.getProfitConfigCountry("DE");
			if(temp.getLabelService()==null) {
				temp.setLabelService(Boolean.FALSE);
			}
			  Collection<ProfitConfigCountry> list = config.getCountryMap().values();
			int b = 0;
			for (ProfitConfigCountry one:list) {
				one.setProfitid(config.getId());
				LambdaQueryWrapper<ProfitConfigCountry> query=new LambdaQueryWrapper<ProfitConfigCountry>();
				query.eq(ProfitConfigCountry::getProfitid,config.getId());
				query.eq(ProfitConfigCountry::getCountry,one.getCountry());
				ProfitConfigCountry oldone =null;
				List<ProfitConfigCountry> oldlist = profitCfgCountryService.list(query);
				if(oldlist!=null&&oldlist.size()>0) {
					oldone=oldlist.get(0);
				}
				if(oldone!=null) {
					one.setId(oldone.getId());
				}
			   if (isEU(one.getCountry())) {
					temp.setId(one.getId());
					temp.setCountry(one.getCountry());
					temp.setVatRate(one.getVatRate());//VAT增值税费率按国家分开设置
					if(oldone!=null) {
						b += profitCfgCountryService.update(temp);
					}else {
						b += profitCfgCountryService.insert(temp);
					}
				} else {
					if(oldone!=null) {
						b += profitCfgCountryService.update(one);
					}else {
						b += profitCfgCountryService.insert(one);
					}
				}
			}
			if (b >0) {
				msg = "更新成功！";
			} else {
				msg = "更新失败！";
			}
		} else {
			msg = "更新失败！";
		}
		profitService.clearCache();
		return msg;
	}

	@Caching(evict={@CacheEvict(value = "defaultProfitCfgCache", allEntries = true),
            @CacheEvict(value = "profitCfgCache", allEntries = true)})
	public String insert(ProfitConfig config) {
		String msg = null;
		if (this.save(config)) {
			ProfitConfigCountry temp = config.getProfitConfigCountry("DE");
			if(temp.getLabelService()==null) {
				temp.setLabelService(Boolean.FALSE);
			}
			Collection<ProfitConfigCountry> list = config.getCountryMap().values();
			ProfitConfigCountry uk = config.getProfitConfigCountry("UK");
			uk.setFenpeiType("PAN_EU");
			uk.setVatRate(new BigDecimal("20"));
			uk.setWarehousesite("uk");
			uk.setCountry("UK");
			uk.setProfitid(config.getId());
			int b = 0;
			for (ProfitConfigCountry one:list) {
				if (isEU(one.getCountry())) {
					temp.setId(UUIDUtil.getUUIDshort());
					temp.setProfitid(config.getId());
					temp.setCountry(one.getCountry());
					temp.setVatRate(one.getVatRate());//VAT增值税费率按国家分开设置 
					b += profitCfgCountryService.insert(temp);
				} else {
					one.setProfitid(config.getId());
					b += profitCfgCountryService.insert(one);
				}
			}
			if (b == list.size()) {
				msg = "添加成功！";
			} else {
				msg = "添加失败！";
			}
		} else {
			msg = "添加失败！";
		}
		profitService.clearCache();
		return msg;
	}

	public static List<String> EUlist = null;
	public boolean isEU(String country) {
		if(EUlist==null){
			EUlist = new ArrayList<String>();
			EUlist.add("FR");
			EUlist.add("IT");
			EUlist.add("ES");
			EUlist.add("NL");
			EUlist.add("PL");
			EUlist.add("DE");
			EUlist.add("SE");
			EUlist.add("BE");
		}
		return EUlist.contains(country);
	}

	@Cacheable(value = "profitCfgCache", key = "#id")
	public ProfitConfig findConfigAction(String id) {
		ProfitConfig profitCfg = new ProfitConfig();
		if (StrUtil.isBlank(id)) {
			profitCfg = getSystemProfitCfg();
		} else {
			profitCfg = selectByPKey(id);
		}
		if(profitCfg.getCountryList()==null) {
			List<ProfitConfigCountry> profitCfgCountryList = profitCfgCountryService.findByProfitId(profitCfg.getId());
			profitCfg.setCountryList(profitCfgCountryList);
		}
		return profitCfg;
	}

	@Cacheable(value = "defaultProfitCfgCache", key = "#shopId")
	public String findDefaultPlanId(String shopId) {
		String id = null;
		ProfitConfig profitCfg = this.baseMapper.findDefaultPlan(shopId);
		if (profitCfg != null) {
			id = profitCfg.getId();
		}
		return id;
	}

	
	@Caching(evict={@CacheEvict(value = "defaultProfitCfgCache", allEntries = true),
                    @CacheEvict(value = "profitCfgCache", allEntries = true)})
	public String setDefaultPlan(String id) {
		String msg;
		int result = this.baseMapper.setDefaultPlanById(id);
		if (result > 0) {
			msg = "操作成功！";
		} else {
			msg = "操作失败！";
		}
		return msg;
	}
	
	@Caching(evict={@CacheEvict(value = "defaultProfitCfgCache", allEntries = true),
                    @CacheEvict(value = "profitCfgCache", allEntries = true)})
	public int setNotDefault(String shopId) {
		return  this.baseMapper.updateAllByShopId(shopId);
	}
	
	@Cacheable(value = "defaultProfitCfgCache", key = "#groupid")
	public String findDefaultPlanIdByGroup(String groupid) {
		String id = null;
		ProfitConfig profitCfg =  this.baseMapper.findDefaultPlanIdByGroup(groupid);
		if (profitCfg != null) {
			id = profitCfg.getId();
		}
		return id;
	}

	@Cacheable(value = "profitCfgCache")
	public ProfitConfig getSystemProfitCfg() {
		ProfitConfig cfg=  this.baseMapper.findSystemProfitCfg();
		List<ProfitConfigCountry> profitCfgCountryList = profitCfgCountryService.findByProfitId(cfg.getId());
		cfg.setCountryList(profitCfgCountryList);
		return  cfg;
	}
	
	@Cacheable(value = "profitCfgCache", key = "#shopId")
	public List<ProfitConfig> findProfitCfgName(String shopId) {
		List<ProfitConfig> plans =  this.baseMapper.findPlanNames(shopId);
		for(ProfitConfig cfg:plans) {
			List<ProfitConfigCountry> profitCfgCountryList = profitCfgCountryService.findByProfitId(cfg.getId());
			cfg.setCountryList(profitCfgCountryList);
			if(cfg.getOperator()!=null) {
				Result<UserInfo> result = adminClientOneFeign.getUserByUserId(cfg.getOperator().toString());
				if(Result.isSuccess(result)&&result.getData()!=null) {
					cfg.setOperatorName(result.getData().getUserinfo().get("name").toString());
				}
			}
		
		}
		return plans;
	}

	@Cacheable(value = "profitCfgCache", key = "#id")
	public ProfitConfig selectByPKey(String id) {
		ProfitConfig cfg  =this.baseMapper.selectById(id);
		List<ProfitConfigCountry> profitCfgCountryList = profitCfgCountryService.findByProfitId(cfg.getId());
		cfg.setCountryList(profitCfgCountryList);
		return  cfg;
	}

	public int getProfitPlanCountByShopId(String shopId) {
		long count =  this.baseMapper.selectProfitPlanCountByShopId(shopId);
		return (int) count;
	}

	@Caching(evict={@CacheEvict(value = "defaultProfitCfgCache", allEntries = true),
            @CacheEvict(value = "profitCfgCache", allEntries = true)})
	public boolean save(ProfitConfig entity)  {
		entity.setId(null);
		return  this.baseMapper.insert(entity)>0?true:false;
	}

	@Caching(evict={@CacheEvict(value = "defaultProfitCfgCache", allEntries = true),
            @CacheEvict(value = "profitCfgCache", allEntries = true)})
	public boolean delete(Serializable key) {
		return  this.baseMapper.deleteById(key)>0?true:false;
	}

	@Caching(evict={@CacheEvict(value = "defaultProfitCfgCache", allEntries = true),
            @CacheEvict(value = "profitCfgCache", allEntries = true)})
	public int updateSelective(ProfitConfig entity)  {
		return  this.baseMapper.updateById(entity);
	}

	public ProfitConfig findSysProfitCfg() {
		ProfitConfig cfg= this.baseMapper.findSystemProfitCfg();
		List<ProfitConfigCountry> profitCfgCountryList = profitCfgCountryService.findByProfitId(cfg.getId());
		cfg.setCountryList(profitCfgCountryList);
		return  cfg;
	}

	public List<InplaceFee> findInplacefee(String country) {
		List<InplaceFee> inplacefees = inplaceFeeMapper.findByCountry(country);
		return inplacefees;
	}

	public List<ManualProcessingFee> findManualProcessingFee() {
		List<ManualProcessingFee> manualProcessingFees = manualProcessingFeeMapper.findByCountry("US");
		return manualProcessingFees;
	}

	@Override
	@Caching(evict={@CacheEvict(value = "defaultProfitCfgCache", allEntries = true),
            @CacheEvict(value = "profitCfgCache", allEntries = true)})
	public boolean updateById(ProfitConfig entity) {
		// TODO Auto-generated method stub
		return this.updateById(entity);
	}
	
	

}
