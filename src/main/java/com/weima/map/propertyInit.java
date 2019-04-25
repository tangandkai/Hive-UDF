package com.weima.map;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class propertyInit {
//	private String path = "C:\\software\\eclipse\\EclipseProjects\\hive\\src\\main\\java\\com\\weima\\map\\jdbc.properties";
	private String path = "jdbc.properties";
	private static propertyInit propertyinit = null;
	private Properties properties;
	private propertyInit() {}
	
	/**
	 * *
	 * @return
	 */
	public static propertyInit getInstance() {
		if(null==propertyinit) {
			return new propertyInit();
		}
		return propertyinit;
	}
	
	/**
	 * *获取配置属性
	 * @param path
	 * @return
	 */
	public Properties getPropertirs() {
		properties = new Properties();
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(path);
			try {
				properties.load(fs);
				return properties;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		finally {
			try {
				fs.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		propertyInit Property = new propertyInit();
		Properties property = Property.getPropertirs();
		System.out.println(Property.getPropertirs());
		String user = property.getProperty("user");
		String driver = property.getProperty("driver");
		String password = property.getProperty("password"); 
		String url = property.getProperty("url");
		System.out.println("user="+user+"\n"+"driver"+driver+"\n"+"password"+password+"\n"+"url="+url);
	}
}
