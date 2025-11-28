package com.wimoor.common.service;

import java.io.InputStream;
import java.util.Map;


public interface ObjectHandler {
	public void treatReader(InputStream inputStream, Map<String, Object> param) ;
}
