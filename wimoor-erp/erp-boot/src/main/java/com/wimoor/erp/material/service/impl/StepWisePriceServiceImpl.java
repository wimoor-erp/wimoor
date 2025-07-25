package com.wimoor.erp.material.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.material.mapper.StepWisePriceMapper;
import com.wimoor.erp.material.pojo.entity.StepWisePrice;
import com.wimoor.erp.material.service.IStepWisePriceService;

@Service("stepWisePriceService")
public class StepWisePriceServiceImpl extends  ServiceImpl<StepWisePriceMapper,StepWisePrice> implements IStepWisePriceService {

	 

	public void deleteIsNull() {
		this.baseMapper.deleteIsNull();
	}

	public List<StepWisePrice> selectByMaterial(String material) {
		return 		this.baseMapper.selectByMaterial(material);
	}

	public Map<String, Object> getMaterialPriceByAmount(String material, Integer planamount) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<StepWisePrice> stepwiseprice = 		this.baseMapper.selectByMaterial(material);
		BigDecimal itemprice = new BigDecimal("0");
		Integer needamount = 0;
		if (stepwiseprice != null && stepwiseprice.size() > 0) {
			itemprice = stepwiseprice.get(0).getPrice();
			needamount = stepwiseprice.get(0).getAmount();
			for (int step = 0; step < stepwiseprice.size(); step++) {
				if (stepwiseprice.get(step).getAmount() < planamount + 1) {
					if (needamount < stepwiseprice.get(step).getAmount()) {
						needamount = stepwiseprice.get(step).getAmount();
						itemprice = stepwiseprice.get(step).getPrice();
					}
				}
			}
		}
		map.put("itemprice", itemprice);
		map.put("needamount", needamount);
		return map;
	}

	public void deleteByMaterial(String material) {
		this.baseMapper.deleteByMaterial(material);
	}

	public Map<String, Object> selectAssByMaterial(String materialid) {
		return 		this.baseMapper.selectAssByMaterial(materialid);
	}

}
