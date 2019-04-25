package com.weima.hive.udf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@SuppressWarnings("deprecation")
public class SignalParseWithList extends UDF{
	private Map<String,String> map = new HashMap<String,String>();
	private Logger logger = Logger.getLogger("rvm_bigdata_json");
	/**
	 * 
	 * @param content
	 * @return
	 */ 
	@SuppressWarnings("unchecked")
	private Map<String,Object> parse(String content) {
		Gson gson = new Gson();
		if(content == null) {
			return null;
		}
		try {
			String jsonobject = content.replaceAll("=",":");
			System.out.println(jsonobject);
			String pattern = "\\w{1,64}:,";
	
			String result = jsonobject.replaceAll(pattern, "ok");
			System.out.println("result:"+result);
			return gson.fromJson(result, Map.class);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	private JsonObject parseJson(String content) {
		Gson gson = new Gson();
		String pattern = "\\w{1,64}:,";
		String patternN = "\\.0,";
		if(content == null) {
			return null;
		}
		try {
			String jsonobject = content.replaceAll("=",":");
			logger.info("替换等号的结果"+jsonobject); 
			String value = jsonobject.replaceAll(pattern, "");
			logger.info("清洗无效数据结果："+value);
			String result = value.replaceAll(patternN, ",");
			logger.info("result="+result);
			JsonObject json = gson.fromJson(result, JsonObject.class);
			logger.info("json结果是="+json); 
			logger.info(json.get("kk")+"");
			return json;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public String evaluate(String content) {
		JsonObject result = parseJson(content);
		return result.toString();
	}
	
	/**
	    将items字段内容转换为map，通过传入key获取对应的值
	 * @param items
	 * @param Key
	 * @return
	 */
	public String evaluate(String items,String Key) {
		try {
			Map<String, Object> result = parse(items);
			return result.get(Key)+"";
		}
		catch (Exception e) {
			return null;
		}
	}
	/**
	 * 返回items字段对应的内容
	 * @param items
	 * @return
	 */
	
	public static void main(String[] args) {
		SignalParseWithList signal = new SignalParseWithList();
		String i = "{frequency=-1.0, info={BCM_AutoLampStatus={diff_time=[5.0, 9.0], value=[1.0, 0.0]}, BCM_BrakeLampSta={diff_time=[2.0, 5.0, 7.0, 11.0, 16.0, 23.0, 29.0], value=[0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0]}, BCM_BrakePadelSta={diff_time=[2.0, 5.0, 7.0, 11.0, 16.0, 23.0, 29.0], value=[0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0]}, BCM_Drv_wdw_Opend={diff_time=[26.0], value=[1.0]}, BCM_HazardLampSts={diff_time=[7.0], value=[1.0]}, BCM_LowBeamSta={diff_time=[9.0], value=[0.0]}, BCM_TurnLampSta_Left={diff_time=[7.0, 9.0, 10.0, 13.0, 14.0, 16.0, 19.0, 20.0, 22.0, 25.0, 26.0, 29.0], value=[1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0]}, BCM_TurnLampSta_Right={diff_time=[0.0, 2.0, 4.0, 7.0, 9.0, 10.0, 13.0, 14.0, 16.0, 19.0, 20.0, 22.0, 25.0, 26.0, 29.0], value=[0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0]}, BCM_WiperCtrl_Front={diff_time=[22.0, 23.0], value=[3.0, 0.0]}, ESC_BrakePedalSwSta={diff_time=[2.0, 5.0, 7.0, 11.0, 16.0, 23.0, 29.0], value=[0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0]}, SCM_TurnLightLeverSta={diff_time=[4.0], value=[0.0]}, VCU_VacmPumpSta={diff_time=[7.0, 10.0], value=[1.0, 0.0]}}, startTime=-1.0}";
		String items = "{a=hadoop,b=spark,c=ok, cc=, cd=[3,4,5,6], d=helloword, x=,e=mapreduce,f=,g=[3,4,5,8,null,9,null,99],h=ok，v=[]}";
		String item = "{a=hadoop, b=spark,c=ok, d=helloword, e=mapreduce,f=null,g=[3,4,5,8,null,9,null,99],h=ok}";
		String f = signal.evaluate(i,"info");
		System.out.println("info="+f);
	}
}
