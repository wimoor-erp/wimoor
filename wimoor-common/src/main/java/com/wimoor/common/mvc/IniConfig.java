package com.wimoor.common.mvc;

import java.io.IOException;
import java.util.Properties;


public class IniConfig {

	/**
	 * 同步锁
	 */
	private static final Object obj = new Object();
	
	/**
	 * 配置文件
	 */
	private static Properties prop = null;
	
	/**
	 * 配置对象单例模式
	 */
	private static IniConfig config = null;
	
	/**
	 * 配置文件名称
	 */
	private final static String FILE_NAME = "/config.properties";
	
	static{
		prop = new Properties();
		try {
			prop.load(IniConfig.class.getResourceAsStream(FILE_NAME));
		} catch (IOException e) {
		     e.printStackTrace( );
		}
		
	}
	
	/**
	 * 获取单例模式对象实例
	 * @return 唯一对象实例
	 */
	public static IniConfig getInstance(){
		if(null==config){
			synchronized (obj) {
				config = new IniConfig();
			}
		}
		return config;
	}
	
	/**
	 */
	public static String get(String key){
		return prop.getProperty(key);
	}
	public static boolean runManualTask() {
		return "true".equals(IniConfig.get("run_manual_task"));
	}
 
	public static String photoServer(){
		return IniConfig.get("photo_server");
	}
	public static String photoServerUrl(){
		return IniConfig.get("photo_server_url");
	}
}
