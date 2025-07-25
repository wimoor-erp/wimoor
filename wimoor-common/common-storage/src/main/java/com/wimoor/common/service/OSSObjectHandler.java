package com.wimoor.common.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;


public interface OSSObjectHandler {
	public void treatReader(InputStream inputStream, Map<String, Object> param) ;
}
