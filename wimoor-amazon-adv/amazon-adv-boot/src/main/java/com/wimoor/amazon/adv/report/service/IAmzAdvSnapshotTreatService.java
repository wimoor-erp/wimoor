package com.wimoor.amazon.adv.report.service;

 
import com.alibaba.fastjson.JSONArray;
import com.wimoor.amazon.adv.report.pojo.AmzAdvSnapshot;

public interface IAmzAdvSnapshotTreatService {
	 
	void treatAdvCampaigns(AmzAdvSnapshot record, JSONArray a);
	void treatAdvAdgroups(AmzAdvSnapshot record, JSONArray a);
	void treatAdvCampKeywordsNegativa(AmzAdvSnapshot record, JSONArray a);
	void treatAdvKeywords(AmzAdvSnapshot record, JSONArray a);
	void treatAdvKeywordsNegativa(AmzAdvSnapshot record, JSONArray a);
	void treatAdvProductads(AmzAdvSnapshot record, JSONArray a);
	void treatAdvProductTarget(AmzAdvSnapshot record, JSONArray a);
	void treatAdvProductTargetNegativa(AmzAdvSnapshot record, JSONArray a);

	void treatAdvCampaignsSD(AmzAdvSnapshot record, JSONArray a);
	void treatAdvAdgroupsSD(AmzAdvSnapshot record, JSONArray a);
	void treatAdvProductadsSD(AmzAdvSnapshot record, JSONArray a);
	void treatAdvProductTargetSD(AmzAdvSnapshot record, JSONArray a);
	void treatAdvProductTargetNegativaSD(AmzAdvSnapshot record, JSONArray a);
	
	void treatAdvKeywordsHsa(AmzAdvSnapshot record, JSONArray a);
	void treatAdvProductadsHsa(AmzAdvSnapshot record, JSONArray a);
	void treatAdvKeywordsNegativaHsa(AmzAdvSnapshot record, JSONArray a);
	void treatAdvCampKeywordsNegativaHsa(AmzAdvSnapshot record, JSONArray a);
	void treatAdvProductTargetHsa(AmzAdvSnapshot record, JSONArray a);
	void treatAdvProductTargetNegativaHsa(AmzAdvSnapshot record, JSONArray a);
	void treatAdvAdgroupsHsa(AmzAdvSnapshot record, JSONArray a);


	
}
