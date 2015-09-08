package com.znw.mydemo.net.httpclient.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.znw.mydemo.app.task.UserJio.UserEntity;
import com.znw.mydemo.application.SoftApplication;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class NetImpl extends NetAdapter {

	// appkey，secret用移动提供的值？az2ak8ribm和2c4fad71b887d178745694a3eb59fa98
	/**
	 * 初始化公共post请求
	 * 
	 * @param addurl
	 *            请求地址
	 * @param map
	 *            请求参数集合
	 * @return jsonString
	 */
	private String commonNetPost(String addurl, Map<String, String> map) {
		StringBuffer url = getBaseUri();// 初始化基础url
		url.append(addurl);// 添加请求url
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();// 初始化请求参数集合
		// commonNameValuePair(nameValuePairs);// 添加公共请求参数
		if (!map.isEmpty()) {// map集合不为空
			for (Object obj : map.keySet()) {// 遍历集合
				String key = (String) obj; // 获取键
				String value = (String) map.get(key); // 获取值
				nameValuePairs.add(new BasicNameValuePair(key, value));// 添加其他参数
			}
		}
		// nameValuePairs.add(new BasicNameValuePair("sign",
		// getSign(nameValuePairs)));// 添加签名参数
		// DebugUtils.println("nameValuePairs.sort() = " + nameValuePairs);
		return netPost(new HttpUrl(url.toString()), nameValuePairs);// 发射
	}

	// appkey，secret用移动提供的值？az2ak8ribm和2c4fad71b887d178745694a3eb59fa98
	/**
	 * 初始化公共post请求
	 * 
	 * @param addurl
	 *            自定义请求地址
	 * @param map
	 *            请求参数集合
	 * @return jsonString
	 */
	private String commonNetPostByMyUrl(String myurl, Map<String, String> map) {
		String url = myurl;// 初始化基础url
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();// 初始化请求参数集合
		// commonNameValuePair(nameValuePairs);// 添加公共请求参数
		if (!map.isEmpty()) {// map集合不为空
			for (Object obj : map.keySet()) {// 遍历集合
				String key = (String) obj; // 获取键
				String value = (String) map.get(key); // 获取值
				nameValuePairs.add(new BasicNameValuePair(key, value));// 添加其他参数
			}
		}
		// nameValuePairs.add(new BasicNameValuePair("sign",
		// getSign(nameValuePairs)));// 添加签名参数
		// DebugUtils.println("nameValuePairs.sort() = " + nameValuePairs);
		return netPost(new HttpUrl(url), nameValuePairs);// 发射
	}

	/**
	 * 上传文件的post请求
	 * 
	 * @param addurl
	 *            请求地址
	 * @param file
	 *            上传的文件
	 * @return jsonString
	 */
	private String commonNetPostFile(String addurl, Map<String, String> map,
			String paramName, File file) {
		StringBuffer url = getBaseUri();// 初始化基础url
		url.append(addurl);// 添加请求url
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();// 初始化请求参数集合
		// commonNameValuePair(nameValuePairs);// 添加公共请求参数
		if (!map.isEmpty()) {// map集合不为空
			for (Object obj : map.keySet()) {// 遍历集合
				String key = (String) obj; // 获取键
				String value = (String) map.get(key); // 获取值
				nameValuePairs.add(new BasicNameValuePair(key, value));// 添加其他参数
			}
		}
		// nameValuePairs.add(new BasicNameValuePair("sign",
		// getSign(nameValuePairs)));// 添加签名参数
		return netPostFile(new HttpUrl(url.toString()), nameValuePairs,
				paramName, file);// 发射
	}

	/** 添加公共请求参数 */
	private void commonNameValuePair(List<NameValuePair> nameValuePairs) {
		nameValuePairs.add(new BasicNameValuePair("imei", "imei"));
		nameValuePairs.add(new BasicNameValuePair("imsi", "imsi"));
		nameValuePairs.add(new BasicNameValuePair("os_version",
				android.os.Build.VERSION.RELEASE));
		nameValuePairs.add(new BasicNameValuePair("platform", "android"));
		try {
			PackageInfo packageInfo = SoftApplication.softApplication
					.getPackageManager()
					.getPackageInfo(
							SoftApplication.softApplication.getPackageName(), 0);
			String versionName = packageInfo.versionName;
			nameValuePairs.add(new BasicNameValuePair("app_version",
					versionName));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		nameValuePairs.add(new BasicNameValuePair("request_time", String
				.valueOf(System.currentTimeMillis())));// 请求时间
		nameValuePairs.add(new BasicNameValuePair("tokenKey",
				"F957BCAA4DCA44F1BF1CEEB9CDFAD2FC5A4"));
		nameValuePairs.add(new BasicNameValuePair("client_type", "t"));

	}

	@Override
	public String getUserInfo(String userName) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userName", userName);
		// map.put("pageNo", pageNo);
		// map.put("pageSize", pageSize);
		return commonNetPost(USER_INFO, map);
	}

}
