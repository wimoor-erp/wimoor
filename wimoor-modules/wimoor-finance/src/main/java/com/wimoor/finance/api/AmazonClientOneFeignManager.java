package com.wimoor.finance.api;

import com.wimoor.common.result.Result;
import com.wimoor.amazon.api.RemoteAmazonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class AmazonClientOneFeignManager {
	@Autowired
	private RemoteAmazonService remoteAmazonService;

    public List<Map<String,Object>> getAmazonGroup() {
		Result<List<Map<String,Object>>> result = remoteAmazonService.getAmazonGroupAction();
		if(Result.isSuccess(result)&&result.getData()!=null) {
			return result.getData();
		}else {
			return null;
		}
    }
}
