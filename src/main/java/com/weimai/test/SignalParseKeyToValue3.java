package com.weimai.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDFUtils;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
import org.apache.hadoop.io.Text;


import com.google.gson.Gson;

public class SignalParseKeyToValue3 extends GenericUDF{
	
	private StringObjectInspector items;	//be used to accept the items
	private StringObjectInspector Key;		//be used to accept the key
	
	//自动推导返回值类型
	private GenericUDFUtils.ReturnObjectInspectorResolver returnObject = new GenericUDFUtils.ReturnObjectInspectorResolver(false);

	/**
	 * change the string format to map by json
	 * @param items
	 * @return
	 */
	public Map parse(String items) {
		Gson gson = new Gson();
		Map json = new HashMap();
		if(items==null) {
			return null;
		}
		try {
			String jsonobject = items.replaceAll("=",":");
			json = gson.fromJson(jsonobject, Map.class);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return json;
		
	}
	@Override
	public Object evaluate(DeferredObject[] arg0) throws HiveException {
		String item = items.getPrimitiveJavaObject(arg0[0].get());
		String key = Key.getPrimitiveJavaObject(arg0[1].get());
		Map map = parse(item);
		if(key==null)
			return null;
		try {
			String value = map.get(key).toString();
			return value;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getDisplayString(String[] arg0) {
		return "signalparse("+arg0.toString()+")";
	}

	@Override
	public ObjectInspector initialize(ObjectInspector[] arg0) throws UDFArgumentException {
		if(arg0.length!=2) {
			throw new UDFArgumentException();
		}
		for(int i=0;i<arg0.length;i++) {
			if(arg0[i].getCategory()!=ObjectInspector.Category.PRIMITIVE) {
				throw new UDFArgumentException("only primitive type arguments are accepted,"
						+ "but "+arg0[i].getTypeName()+" is passed!");
			}
		}
		this.items = (StringObjectInspector) arg0[0];
		this.Key   = (StringObjectInspector) arg0[1];
		return PrimitiveObjectInspectorFactory.javaStringObjectInspector;  
	}
//	@Test
//	public void test() throws HiveException {
//		System.out.println("helo word");
//		String str = "{a=hadoop,b=spark,c=hdfs,d=helloword,e=mapreduce,f=hive,g=[3,4,5,8,9]}";
//		ObjectInspector item = PrimitiveObjectInspectorFactory.javaStringObjectInspector;
//		ObjectInspector key = PrimitiveObjectInspectorFactory.javaStringObjectInspector;
//		initialize(new ObjectInspector[] {item,key});
//		Object res = evaluate(new DeferredObject[]{new DeferredJavaObject(str), new DeferredJavaObject("g")});  
//		System.out.println(res);
//	} 
}
