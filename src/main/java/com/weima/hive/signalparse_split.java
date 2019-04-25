package com.weima.hive;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.hadoop.hive.ql.exec.UDF;

import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@SuppressWarnings("deprecation")
public class signalparse_split extends UDF{
	private Map<String, String> map = new HashedMap();
	public Map<String, String> parse(String items) {
		if(null==items) {
			return null;
		}
		try {
			String item = items.replace("{", "").replace("}", "").trim();
			System.out.println("替换后的结果："+item);
//			map = Splitter.on(",").withKeyValueSeparator("=").split(item);
			map = Splitter.onPattern(",\\s?").withKeyValueSeparator("=").split(item);
			
			return map;
		}catch (Exception e) {
			return null;
		}	
	}
	public String parse_1(String items) {
		if(null==items) {
			return null;
		}
		try {
			String item = items.replaceAll("=", ":");
			System.out.println("替换后的结果："+item);
			Type type = new TypeToken<HashMap<String, String>>() {}.getType();
			map = new Gson().fromJson(item, type);
			return map.get("name");
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
	}
	public String evaluate(String items,String Key) {
		try {
			Map<String, String> result = parse(items);
			return result.get(Key.toString());
		}
		catch (Exception e) {
			return null;
		}
	}

	public String evaluate(String items) {
		return items;
	}
	public static void main(String[] args) {
		String items = "{author=,author_info=,  extra=,name=fee,section=}";
		String xxx = "{author=,author_info=,extra=, name=CNR经典音乐广播,  section=}";
		signalparse_split signal = new signalparse_split();
		String result = signal.evaluate(xxx,"name" );
		System.out.println(result);
		String x = signal.evaluate(items, "name");
		System.out.println(x);
	}
}
