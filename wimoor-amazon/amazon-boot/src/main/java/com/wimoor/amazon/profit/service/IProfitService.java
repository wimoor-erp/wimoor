package com.wimoor.amazon.profit.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.wimoor.amazon.finances.pojo.entity.FBAEstimatedFee;
import com.wimoor.amazon.profit.pojo.dto.ProfitQuery;
import com.wimoor.amazon.profit.pojo.entity.ProductTier;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfigCountry;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfig;
import com.wimoor.amazon.profit.pojo.entity.ReferralFee;
import com.wimoor.amazon.profit.pojo.vo.CostDetail;
import com.wimoor.amazon.profit.pojo.vo.InputDimensions;
import com.wimoor.common.mvc.BizException;

public interface IProfitService {


	public List<BigDecimal> findMarginList();

	public String findTypeById(int typeId);

	public String isMedia(int typeId);

	public List<String> findCountryList();

	public List<String> findCurrencyUnitList();

	public String determineProductTier(String country, InputDimensions inputDimension, String isMedia)
			throws BizException;

	public BigDecimal determineOutboundWeight(String country, ProductTier productTier, InputDimensions inputDimension,
			String isMedia) throws BizException;

	public BigDecimal calculateFBA(String country, String USProductTier, InputDimensions inputDimension, String isMedia,
			String type, BigDecimal outboundWeight, ProfitConfigCountry profitConfigX, String shipmentType)
			throws BizException;

	public BigDecimal calculateStorageFee(String country, ProfitConfigCountry profitConfigX,
			InputDimensions inputDimension, boolean isStandard) throws BizException;

	public BigDecimal calculateInPlaceFee(String country, ProductTier productTier, InputDimensions inputDimension,
			String invplaceFee) throws BizException;

	public BigDecimal calculateVCFee(String country, String isMedia, int typeId) throws BizException;

	public Map<String, String> strToMap(String costDetail);

	public String getCurrencyUnit(String country);

	public BigDecimal getPrepServiceFee(boolean isStandard, String country, String category);

	public BigDecimal getLabelServiceFee(boolean isStandard, String country, boolean isSmlAndLight);

	public List<String> findCategoryList();

	public Map<String, String> jsonToMap(String costDetail_);

	public CostDetail initCostDetail(String string, ProfitConfig profitCfgAll, InputDimensions inputDimension,
			String isMedia, String type, int typeId, BigDecimal cost, BigDecimal shipment, String currency,
			String categories, String shipmentType, BigDecimal declaredValue, String declaredValueCur,
			BigDecimal taxrate, BigDecimal gstrate, BigDecimal sellingGSTRate, BigDecimal referralrate,
			boolean isSmlAndLight) throws BizException;

	public CostDetail getCostDetail(ProfitQuery query, CostDetail costDetail, int typeId, BigDecimal referralrate, boolean isSmlAndLight, ProfitConfig profitcfg);

	public Map<String, String> getProductTier(ProfitConfigCountry profitConfigCountry, InputDimensions inputDimension,
			String country, String isMedia, BigDecimal cost, boolean isSmlAndLight, String type) throws BizException;

	public CostDetail getProfitByAmazonData(String country, ProfitConfig profitCfgAll, InputDimensions inputDimension,
			String isMedia, BigDecimal cost, String currency, BigDecimal price, FBAEstimatedFee fbaFee, ReferralFee ref,
			boolean isSmlAndLight, BigDecimal shipmentfee) throws BizException;

	public BigDecimal calculateClosingFee(String country, BigDecimal price);

	public String determineSmlProductTier(String country, InputDimensions inputDimension,String isMedia);

	public BigDecimal calculateSmlFBA(String country, String productTierId, BigDecimal outboundWeight,ProfitConfigCountry profitConfigX, String type);

	public BigDecimal getSLOutboundWeight(String country, ProductTier productTier, InputDimensions inputDimension);

	public CostDetail getProfitByLocalData(String country, ProfitConfig profitcfg, InputDimensions inputDimension,
			InputDimensions inputDimension_local, String isMedia, String type, int typeid, BigDecimal cost,
			String currency, BigDecimal price, String shipmentType, boolean isSmlAndLight, BigDecimal shipmentfee);

	public void clearCache();

	public boolean isEUNotUK(String country);

	public BigDecimal getFBAFormatWeight(String country, String productTierId, InputDimensions inputDimension,
			BigDecimal outboundWeight, ProfitConfigCountry profitConfigCountry);

	public void clearUnitMap();
}