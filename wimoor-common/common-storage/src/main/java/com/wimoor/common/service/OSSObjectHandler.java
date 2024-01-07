package com.wimoor.common.service;

import java.io.BufferedReader;
import java.util.Map;


public interface OSSObjectHandler {
	public void treatReader(BufferedReader reader, Map<String, Object> param);
}
