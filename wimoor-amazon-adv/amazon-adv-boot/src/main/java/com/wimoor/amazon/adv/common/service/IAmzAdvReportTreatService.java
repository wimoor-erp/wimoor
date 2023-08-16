package com.wimoor.amazon.adv.common.service;

 
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONReader;
import com.wimoor.amazon.adv.common.pojo.AmzAdvRequest;
import com.wimoor.amazon.adv.common.pojo.AmzAdvSnapshot;

public interface IAmzAdvReportTreatService {
	
	void treatProductAdvertReport( AmzAdvRequest request,JSONReader a);
	
	void treatProductSDAdvertReport( AmzAdvRequest request,JSONReader a);
	
	void treatCampaignsAdvertReport(AmzAdvRequest request,JSONReader a);
	
	void treatCampaignsSDAdvertReport(AmzAdvRequest request,JSONReader a);
	
	void treatAdroupsAdvertReport(AmzAdvRequest request,JSONReader a);
	
	void treatAdroupsSDAdvertReport(AmzAdvRequest request,JSONReader a);
	
	void treatKeywordsAdvertReport(AmzAdvRequest request,JSONReader a);
	
	void treatAsinsAdvertReport( AmzAdvRequest request,JSONReader a);
	
	void treatKeywordsQuryAdvertReport( AmzAdvRequest request,JSONReader a);
	
	void treatKeywordsHsaQuryAdvertReport( AmzAdvRequest request,JSONReader jsonReader);
	
	void treatCampaignsPlaceAdvertReport(AmzAdvRequest request,JSONReader a);

	void treatCampaignsPlaceHsaAdvertReport(AmzAdvRequest request,JSONReader a);
	
	void treatCampaignsHsaAdvertReport(AmzAdvRequest request,JSONReader a);
	
	void treatKeywordsHsaAdvertReport(AmzAdvRequest request,JSONReader a);
	
	void treatProductTargetHsaAdvertReport( AmzAdvRequest request,JSONReader a);
	
	void treatProductTargetAdvertReport( AmzAdvRequest request,JSONReader a);
	
	void treatTargetQueryAdvertReport( AmzAdvRequest request,JSONReader a);
	
	void treatAdvAdgroups(AmzAdvSnapshot record, JSONArray a);
	
	void treatAdvCampaigns(AmzAdvSnapshot record, JSONArray a);

	void treatAdvCampKeywordsNegativa(AmzAdvSnapshot record, JSONArray a);
	
	void treatAdvKeywords(AmzAdvSnapshot record, JSONArray a);

	void treatAdvKeywordsNegativa(AmzAdvSnapshot record, JSONArray a);

	void treatAdvProductads(AmzAdvSnapshot record, JSONArray a);
	
	void treatAdvCampaignsHsa(AmzAdvSnapshot record, JSONArray a);
	
	void treatAdvProductTarget(AmzAdvSnapshot record, JSONArray a);
	
	void treatAdvProductTargetNegativa(AmzAdvSnapshot record, JSONArray a);

	void treatAdvCampaignsSD(AmzAdvSnapshot record, JSONArray a);

	void treatAdvAdgroupsSD(AmzAdvSnapshot record, JSONArray a);

	void treatAdvProductadsSD(AmzAdvSnapshot record, JSONArray a);

	void treatAdvProductTargetSD(AmzAdvSnapshot record, JSONArray a);

	void treatAdvProductTargetNegativaSD(AmzAdvSnapshot record, JSONArray a);

	void treatAdvProductTargetSDAdvertReport(AmzAdvRequest request, JSONReader jsonReader);

	void treatAdvAsinSDAdvertReport(AmzAdvRequest request, JSONReader jsonReader);

	void treatCampaignsT00001SDAdvertReport(AmzAdvRequest request, JSONReader jsonReader);
	
}
