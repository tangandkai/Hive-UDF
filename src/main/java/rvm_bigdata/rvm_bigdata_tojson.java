package rvm_bigdata;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
public class rvm_bigdata_tojson extends GenericUDF{

	private StringObjectInspector items;	//be used to accept the items
	
	private Logger logger = Logger.getLogger("rvm_bigdata_tojson");
	
	private JsonObject parse(String content) {
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
			return gson.fromJson(result, JsonObject.class);
		}
		catch (Exception e) {
			return null;
		}
	}
	@Override
	public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
		// TODO Auto-generated method stub
		if(arguments.length!=1) {
			throw new UDFArgumentException();
		}
		for(int i=0;i<arguments.length;i++) {
			if(arguments[i].getCategory()!=ObjectInspector.Category.PRIMITIVE) {
				throw new UDFArgumentException("only primitive type arguments are accepted,"
						+ "but "+arguments[i].getTypeName()+" is passed!");
			}
		}
		this.items = (StringObjectInspector) arguments[0];
//		this.Key   = (StringObjectInspector) arguments[1];
		return PrimitiveObjectInspectorFactory.javaStringObjectInspector;
	}

	
	@Override
	public Object evaluate(DeferredObject[] arguments) throws HiveException {
		// TODO Auto-generated method stub
		String item = items.getPrimitiveJavaObject(arguments[0].get());
		JsonObject result = parse(item);
		return result;
	}

	@Override
	public String getDisplayString(String[] children) {
		// TODO Auto-generated method stub
		return null;
	}
	
//	public static void main(String[] args) throws HiveException {
//	System.out.println("helo word");
//	String str = "{a=1.0,b=2.01,c=,d=3.0,e=4.50,f=hive,g=[3,4,5,8,9],h=2.3,gg=1.0,hh=[]}";
//	rvm_bigdata_tojson signalparse = new rvm_bigdata_tojson();
//	ObjectInspector item = PrimitiveObjectInspectorFactory.javaStringObjectInspector;
//	ObjectInspector format = signalparse.initialize(new ObjectInspector[] {item});
//	
//	System.out.println(format.toString());
//	Object res = signalparse.evaluate(new DeferredObject[]{new DeferredJavaObject(str)});  
//	System.out.println(res.getClass()+"\n"+res);
//}

}
