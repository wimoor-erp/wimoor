package com.wimoor.amazon.inboundV2.service;

import com.wimoor.amazon.inboundV2.pojo.dto.BoxAnalysisDTO;
import com.wimoor.amazon.inboundV2.pojo.dto.BoxInfo;
import com.wimoor.common.user.UserInfo;

import java.util.List;
import java.util.Map;

public interface IShipInboundBoxAnalysisService {

    List<BoxInfo> boxAnalysis(UserInfo user, BoxAnalysisDTO dto);

}
