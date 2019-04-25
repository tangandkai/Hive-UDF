package com.weimai.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {
	
	private static Logger logger = Logger.getLogger("test");
	public static void main(String[] args) throws Exception {
//		Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
//		Connection conn = DriverManager.getConnection("jdbc:phoenix:10.40.150.17:2181");
//		PreparedStatement stamt = conn.prepareStatement("select * from RVM_BIGDATA limit 10");
//		ResultSet result = stamt.executeQuery();
//		while(result.next()) {
//			System.out.println("tbox_time="+result.getString("tbox_time"));
//		}
		
		String res = null;
		String items = "{d=9,c=[559.0,45.90,4.0,9.0],e=9.00,f=jje}";
		logger.info("items="+items);
		String pa = ":,";
		String item = "{d:9,c:[559.0,45.90,4.0,9.0],e:,f=jje}";
		String xx = item.replaceAll(pa, ":null,");
		logger.info("pipwei"+xx);
		String pattern = "\\.0\\]";
		String pattern_1 = "\\.0,";
		String pattern_2 = "=";
		Matcher m = Pattern.compile(pattern_2).matcher(items);
		if(m.find()) {
			logger.info("匹配到等号");
			logger.info(items.replaceAll(pattern, "]").replaceAll(pattern_1, ","));
		}
		else
			logger.info("没有匹配到等号");
		
		System.out.println((int)Math.ceil(3.1));
//		logger.info(items.replaceAll(pattern, ""));
//		logger.info(items.replaceAll(pattern, "]").replaceAll(pattern_1, ","));
		
		String vin = "dewfed";
		String sql = String.format(
                "select * from RVM_BIGDATA_ENV where \"%s\"=\'%s\' and \"%s\">=%d and \"%s\"<=%d order by \"%s\" %s limit %d offset %d","vin",vin,"tbox_time",12323,"tbox_time",31232,"vin","desc",32,1);
		logger.info("sql="+sql);
	}
	
}
