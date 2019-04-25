package com.weima.hive.udf;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.hive.ql.exec.UDF;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class rvm_bigdata_json extends UDF{

	private Logger logger = Logger.getLogger("rvm_bigdata_json");
	private String parse(String content) {
		Gson gson = new Gson();
		String pattern = "\\w{1,64}:,";
		String patternN = "\\.0,";
		String patternN_1 = "\\.0\\]";
		String patternN_2 = "\\.0}";
		String patternN_3 = "=";
		if(content == null) {
			return null;
		}
		try {
			Matcher m = Pattern.compile(patternN_3).matcher(content);
			if(m.find()) {
				logger.info("匹配到等号");
				String jsonobject = content.replaceAll("=",":"); 
				logger.info("替换等号的结果"+jsonobject); 
				String value = jsonobject.replaceAll(pattern, "");
				logger.info("清洗无效数据结果："+value);
				String result = value.replaceAll(patternN, ",").replaceAll(patternN_1, "]").replaceAll(patternN_2, "}");
				logger.info("result="+result);
				String json = gson.fromJson(result, JsonObject.class).toString();
				logger.info("json结果是="+json); 
//				logger.info(json.get("kk")+"");
				return json;
			}
			else
				return content;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String evaluate(String content) {
		String result = parse(content);
		return result;
	}
	
	public String evaluate(String content,String key) {
		return content;
	}
	
//	public static void main(String[] args) {
//	System.out.println("helo word");
//	String str = "{a=1.0,b=2.01,c=,d=3.0,e=4.50,f=hive,g=[3.0,4,5.09,8,9.0],h=2.3,gg=1.0,hh=[],cd=9.0}";
//	String str1 = "{a:1.0,b:2.01,c:,d:3.0,e:4.50,f:hive,g:[3.0,4,5.09,8,9.0],h:2.3,gg:1.0,hh:[],cd=9.0}";
//	rvm_bigdata_json rvm = new rvm_bigdata_json();
//	Object res = rvm.evaluate(str);
//	System.out.println(res);
//}

}
