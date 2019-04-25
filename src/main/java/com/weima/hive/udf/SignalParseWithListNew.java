package com.weima.hive.udf;

import java.util.Map;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDFUtils;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
import com.google.gson.Gson;


public class SignalParseWithListNew extends GenericUDF{
	
	private StringObjectInspector items;	//be used to accept the items
	private StringObjectInspector Key;		//be used to accept the key

	//自动推导返回值类型
	private GenericUDFUtils.ReturnObjectInspectorResolver returnObject = new GenericUDFUtils.ReturnObjectInspectorResolver(false);

	/**
	 * change the string format to map by json
	 * @param items
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> parse(String content) {
		Gson gson = new Gson();
		if(content == null) {
			return null;
		}
		try {
			String jsonobject = content.replaceAll("=",":");
			System.out.println(jsonobject);
			String pattern = "\\w{1,64}:,";
			String result = jsonobject.replaceAll(pattern, "");
			System.out.println("result:"+result);
			return gson.fromJson(result, Map.class);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public String evaluate(DeferredObject[] arg0) throws HiveException {
		String item = items.getPrimitiveJavaObject(arg0[0].get());
		String key = Key.getPrimitiveJavaObject(arg0[1].get());
		Map<String, Object> map = parse(item);
		if(key==null)
			return null;
		try {
			return map.get(key)+"";
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
//		return returnObject.get();
		return PrimitiveObjectInspectorFactory.javaStringObjectInspector;
	}

//	public static void main(String[] args) throws HiveException {
//		System.out.println("helo word");
//		String str = "{a=hadoop,b=spark,c=hdfs,d=helloword,e=mapreduce,f=hive,g=[3,4,5,8,9]}";
//		SignalParseWithListNew signalparse = new SignalParseWithListNew();
//		ObjectInspector item = PrimitiveObjectInspectorFactory.javaStringObjectInspector;
//		ObjectInspector key = PrimitiveObjectInspectorFactory.javaStringObjectInspector;
//		ObjectInspector format = signalparse.initialize(new ObjectInspector[] {item,key});
//		System.out.println(format.toString());
//		Object res = signalparse.evaluate(new DeferredObject[]{new DeferredJavaObject(str), new DeferredJavaObject("g")});  
//		System.out.println(res.getClass()+"\t"+res);
//	}
}
