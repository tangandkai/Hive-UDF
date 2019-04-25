package com.weima.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TestHttpClient {

  public String start(double la, double lon) throws UnsupportedEncodingException {
	  String.valueOf(la);
	  String.valueOf(lon);
	  String location = doGet(la, lon);
	  String address = result(location);
	  return address;
}

    private String result(String result) {
    	Map<String, String> hashMap = new HashMap<String,String>();
        result = result.replaceAll("renderReverse&&renderReverse\\(", "");
        int lastIndex = result.lastIndexOf(")");
        if (lastIndex > -1) {
            result = result.substring(0, lastIndex);
        }
//        System.out.println("result"+result);
        try {
            JSONObject jsonObject = new JSONObject(result); 
            JSONObject j_result = jsonObject.optJSONObject("result");
            String formatted_address = j_result.optString("formatted_address");
            JSONObject addressComponent = j_result.optJSONObject("addressComponent");
            if(addressComponent != null) {
            	hashMap.put("formatted_address", formatted_address);
            	hashMap.put("city_level", addressComponent.optString("city_level"));
            	hashMap.put("country", addressComponent.optString("country"));
            	hashMap.put("province", addressComponent.optString("province"));
            	hashMap.put("city", addressComponent.optString("city"));
            	hashMap.put("district", addressComponent.optString("district"));
            	hashMap.put("town", addressComponent.optString("town"));
            	hashMap.put("adcode", addressComponent.optString("adcode"));
            	hashMap.put("street", addressComponent.optString("street"));
            	hashMap.put("street_number", addressComponent.optString("street_number"));
            	hashMap.put("direction", addressComponent.optString("direction"));
            	hashMap.put("distance", addressComponent.optString("distance"));
            }
//            return province+"\t"+city;
            JSONObject j = new JSONObject(hashMap);
            return j.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String doGet(double la, double lon) throws UnsupportedEncodingException {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串

        String paramsStr = "callback=renderReverse&location="+la+","+lon+"&output=json&pois=1&ak=xxxxxx";
//        System.out.println("paramsStr="+paramsStr);
        String wholeStr = new String("/geocoder/v2/?" + paramsStr + "xxxxxxx");
        String tempStr = URLEncoder.encode(wholeStr, "UTF-8");
        //获取sn校验
        String sn = MD5(tempStr);
//        System.out.println("sn="+sn);
        try {
            // 创建远程url连接对象
            URL url = new URL("http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&" +
                    "location=" + la + "," + lon + "&output=json&pois=1&ak=xxxxxxxx&sn="+sn);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    //sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            connection.disconnect();// 关闭远程连接
        }
        return result;
    }
    
    // 对Map内所有value作utf8编码，拼接返回结果
    public static String toQueryString(Map<?, ?> data)
                    throws UnsupportedEncodingException {
            StringBuffer queryString = new StringBuffer();
            for (Entry<?, ?> pair : data.entrySet()) {
                    queryString.append(pair.getKey() + "=");
                    queryString.append(URLEncoder.encode((String) pair.getValue(),
                                    "UTF-8") + "&");
            }
            if (queryString.length() > 0) {
                    queryString.deleteCharAt(queryString.length() - 1);
            }
            return queryString.toString();
    }
    
    // 来自stackoverflow的MD5计算方法，调用了MessageDigest库函数，并把byte数组结果转换成16进制
    public static String MD5(String md5) {
            try {
                    java.security.MessageDigest md = java.security.MessageDigest
                                    .getInstance("MD5");
                    byte[] array = md.digest(md5.getBytes());
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < array.length; ++i) {
                            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                                            .substring(1, 3));
                    }
                    return sb.toString();
            } catch (java.security.NoSuchAlgorithmException e) {
            }
            return null;
    }


    
//    public static void main(String[] args) {
//    	TestHttpClient http = new TestHttpClient();
//        String result;
//		try {
//			result = http.start(39.477016, 116.655644);
//			System.out.println(result);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        
//	}
}
