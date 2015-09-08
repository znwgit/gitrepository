package com.znw.mydemo.parse;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ParseUtil {

	
	public static JSONObject parseObject(String result) {
		return JSON.parseObject(result);
	}

	
	public static <T> T parseObject(String result, Class<T> clazz) {
		return JSON.parseObject(result, clazz);
	}
	
	/*{
	    "code": 0, 
	    "data": {
	        "avatar": "http://localhost:8080/Beijing_Daily/images/LEN/avatar/1429163457016487.jpg", 
	        "blog": "", 
	        "cname": "刘德华", 
	    }, 
	    "message": "请求成功"
	}*/
	public static <T> T parseObjectSelf(String result, String data,Class<T> clazz) {
		return  (T) JSON.parseObject(parseObject(result).getJSONObject(data).toString(), clazz);
	}

	
	public static JSONArray parseArray(String result) {
		return JSON.parseArray(result);
	}

	
	public static <T> List<T> parseArray(String result, Class<T> clazz) {
		return JSON.parseArray(result, clazz);
	}

	
	public static String toJSONString(Object object) {
		return JSON.toJSONString(object);
	}

	
	public static String toJSONString(Object object, boolean prettyFormat) {
		return JSON.toJSONString(object, prettyFormat);
	}

	
	public static Object toJSON(Object javaObject) {
		return JSON.toJSON(javaObject);
	}

}
