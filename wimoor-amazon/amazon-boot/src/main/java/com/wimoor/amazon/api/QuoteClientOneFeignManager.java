package com.wimoor.amazon.api;

import com.wimoor.common.result.Result;
import com.wimoor.quote.api.QuoteClientOneFeign;
import com.wimoor.quote.api.dto.QuoteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public class QuoteClientOneFeignManager {
	@Autowired
	QuoteClientOneFeign quoteClientOneFeign;
	public Boolean saveAction(QuoteDTO dto){
		return Result.isSuccess(quoteClientOneFeign.saveAction(dto));
	}


}