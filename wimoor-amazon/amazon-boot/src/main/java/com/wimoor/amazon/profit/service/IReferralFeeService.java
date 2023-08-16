package com.wimoor.amazon.profit.service;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.profit.pojo.entity.ReferralFee;
 
public interface IReferralFeeService extends IService<ReferralFee> {

	public ReferralFee getReferralFeeByType(int typeId);

	public ReferralFee getReferralFeeByTypeCountry(int typeId, String country);

	public List<ReferralFee> findAllType();

	public ReferralFee findByPgroup(String group, String country);

	public ReferralFee findCommonOther(String country);
 
	
}
