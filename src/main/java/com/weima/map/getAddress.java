package com.weima.map;

import org.apache.hadoop.hive.ql.exec.UDF;

public class getAddress extends UDF{
	
	public String parse(double la, double lon) {
		try {
			String address = new TestHttpClient().start(la, lon);
			return address;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public String evaluate(String la,String lon) {
		try {
			double laa = Double.parseDouble(la);
			double lonn = Double.parseDouble(lon);
			String result = parse(laa,lonn);
			return result;
		}
		catch (Exception e) {
			return null;
		}
	}
	public static void test() {
		System.out.println("just a test");
	}
//	public static void main(String[] args) {
//		test();
//		getAddress address = new getAddress();
//		String result = address.evaluate("31.066652","121.736326");
//		System.out.println("result="+result);
//		System.out.println(result.getClass());
//	}
}
