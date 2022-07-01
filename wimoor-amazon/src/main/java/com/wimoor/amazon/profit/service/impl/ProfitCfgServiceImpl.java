package com.wimoor.amazon.profit.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.profit.mapper.ProfitConfigMapper;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfig;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfigCountry;
import com.wimoor.amazon.profit.service.IProfitCfgCountryService;
import com.wimoor.amazon.profit.service.IProfitCfgService;
import com.wimoor.amazon.profit.service.IProfitService;

import lombok.RequiredArgsConstructor;
 

@Service("profitCfgService")
@RequiredArgsConstructor
public class ProfitCfgServiceImpl extends ServiceImpl<ProfitConfigMapper, ProfitConfig> implements IProfitCfgService {
	@Resource
	IProfitCfgCountryService profitCfgCountryService;
	@Resource
	IProfitService profitService;
	
	public List<ProfitConfigCountry> getProfitConfigCountryList(ProfitConfig config) {
		List<ProfitConfigCountry> list = new ArrayList<ProfitConfigCountry>();
		list.add(config.getUs());
		list.add(config.getUk());
		list.add(config.getDe());
		list.add(config.getFr());
		list.add(config.getIt());
		list.add(config.getEs());
		list.add(config.getNl());
		list.add(config.getJp());
		list.add(config.getCa());
		list.add(config.getAu());
		list.add(config.getIn());
		list.add(config.getMx());
		list.add(config.getAe());
		list.add(config.getSa());
		list.add(config.getPl());
		list.add(config.getSe());
		return list;
	}
	
	@CacheEvict(value = "profitCfgCache", allEntries = true)
	public String update(ProfitConfig config) {
		String msg = null;
		int a = updateSelective(config);
		if (a > 0) {
			ProfitConfigCountry uk = config.getUk();
			uk.setFenpeiType("PAN_EU");
			uk.setVatRate(new BigDecimal("20"));
			uk.setWarehousesite("uk");
			uk.setCountry("UK");
			uk.setProfitid(config.getId());
			ProfitConfigCountry temp =config.getDe();
			if(temp.getLabelService()==null) {
				temp.setLabelService(Boolean.FALSE);
			}
			List<ProfitConfigCountry> list = getProfitConfigCountryList(config);
			int b = 0;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) == null) {
					continue;
				}
					if (isEU(list.get(i).getCountry())) {
					temp.setId(list.get(i).getId());
					temp.setCountry(list.get(i).getCountry());
					temp.setVatRate(list.get(i).getVatRate());//VAT增值税费率按国家分开设置
					b += profitCfgCountryService.update(temp);
				} else {
					b += profitCfgCountryService.update(list.get(i));
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

	@CacheEvict(value = "profitCfgCache", allEntries = true)
	public String insert(ProfitConfig config) {
		String msg = null;
		if (this.save(config)) {
			ProfitConfigCountry uk = config.getUk();
			uk.setFenpeiType("PAN_EU");
			uk.setVatRate(new BigDecimal("20"));
			uk.setWarehousesite("uk");
			uk.setCountry("UK");
			ProfitConfigCountry temp = config.getDe();
			if(temp.getLabelService()==null) {
				temp.setLabelService(Boolean.FALSE);
			}
			List<ProfitConfigCountry> list = getProfitConfigCountryList(config);
			int b = 0;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) == null) {
					continue;
				}
				if (isEU(list.get(i).getCountry())) {
					temp.setProfitid(config.getId());
					temp.setCountry(list.get(i).getCountry());
					temp.setVatRate(list.get(i).getVatRate());//VAT增值税费率按国家分开设置 
					b += profitCfgCountryService.insert(temp);
				} else {
					list.get(i).setProfitid(config.getId());
					b += profitCfgCountryService.insert(list.get(i));
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
		}
		return EUlist.contains(country);
	}

	@Cacheable(value = "profitCfgCache", key = "#id")
	public ProfitConfig findConfigAction(String id) {
		ProfitConfig profitCfg = new ProfitConfig();
		if (id == null) {
			profitCfg = getSystemProfitCfg();
		} else {
			profitCfg = selectByPKey(id);
		}
		List<ProfitConfigCountry> profitCfgCountryList = profitCfgCountryService.findByProfitId(profitCfg.getId());
		Map<String, ProfitConfigCountry> countryConfigMap = new HashMap<String, ProfitConfigCountry>();
		if (profitCfgCountryList != null && profitCfgCountryList.size() > 0) {
			for (int i = 0; i < profitCfgCountryList.size(); i++) {
				countryConfigMap.put(profitCfgCountryList.get(i).getCountry().toLowerCase(), (profitCfgCountryList.get(i)));
			}
			profitCfg.setUs(countryConfigMap.get("us"));
			profitCfg.setUk(countryConfigMap.get("uk"));
			profitCfg.setDe(countryConfigMap.get("de"));
			profitCfg.setFr(countryConfigMap.get("fr"));
			profitCfg.setIt(countryConfigMap.get("it"));
			profitCfg.setEs(countryConfigMap.get("es"));
			profitCfg.setNl(countryConfigMap.get("nl"));
			profitCfg.setJp(countryConfigMap.get("jp"));
			profitCfg.setCa(countryConfigMap.get("ca"));
			profitCfg.setAu(countryConfigMap.get("au"));
			profitCfg.setIn(countryConfigMap.get("in"));
			profitCfg.setMx(countryConfigMap.get("mx"));
			profitCfg.setSa(countryConfigMap.get("sa"));
			profitCfg.setAe(countryConfigMap.get("ae"));
			profitCfg.setPl(countryConfigMap.get("pl"));
			profitCfg.setSe(countryConfigMap.get("se"));
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

	@CacheEvict(value = "defaultProfitCfgCache", allEntries = true)
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
	
	@CacheEvict(value = "defaultProfitCfgCache", allEntries = true)
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
		return  this.baseMapper.findSystemProfitCfg();
	}
	
	@Cacheable(value = "profitCfgCache", key = "#shopId")
	public List<ProfitConfig> findProfitCfgName(String shopId) {
		List<ProfitConfig> plans =  this.baseMapper.findPlanNames(shopId);
		return plans;
	}

	@Cacheable(value = "profitCfgCache", key = "#id")
	public ProfitConfig selectByPKey(String id) {
		return  this.baseMapper.selectById(id);
	}

	public int getProfitPlanCountByShopId(String shopId) {
		long count =  this.baseMapper.selectProfitPlanCountByShopId(shopId);
		return (int) count;
	}

	@CacheEvict(value = "profitCfgCache", allEntries = true)
	public boolean save(ProfitConfig entity)  {
		entity.setId(null);
		return  this.baseMapper.insert(entity)>0?true:false;
	}

	@CacheEvict(value = "profitCfgCache", allEntries = true)
	public boolean delete(Serializable key) {
		return  this.baseMapper.deleteById(key)>0?true:false;
	}

	@CacheEvict(value = "profitCfgCache", allEntries = true)
	public int updateSelective(ProfitConfig entity)  {
		return  this.baseMapper.updateById(entity);
	}

	public ProfitConfig findSysProfitCfg() {
		return  this.baseMapper.findSystemProfitCfg();
	}

 

}
