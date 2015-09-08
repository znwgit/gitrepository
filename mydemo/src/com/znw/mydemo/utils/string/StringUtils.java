package com.znw.mydemo.utils.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



import android.text.TextUtils;

public class StringUtils
{

	/**
	 * 1。判断字符串是否为空 (null或者“”)
	 * 
	 * @param str
	 * @return
	 * @date 2014-2-27 下午10:26:05
	 * @author leo
	 */
	public static boolean isEmpty(String str)
	{
		return TextUtils.isEmpty(str);
	}
	
	/**
	 * 1。判断字符串是否为整数 (null或者“”)
	 * 
	 * @param str
	 * @return
	 * @date 2014-2-27 下午10:26:05
	 * @author leo
	 */
	public static boolean isInt(String str)
	{
		try
		{
			Integer.parseInt(str);
			return true;
		}
		catch(Exception e){}
		return false;
	}

	/**
	 * 1。判断字符串是否为小数 (null或者“”)
	 * 
	 * @param str
	 * @return
	 * @date 2014-2-27 下午10:26:05
	 * @author leo
	 */
	public static boolean isNumber(String str)
	{
		try
		{
			Double.parseDouble(str);
			return true;
		}
		catch(Exception e){}
		return false;
	}
	
	/**
	 * 1。判断字符串是否为小数点后最多两位小数的正的值 (null或者“”)
	 * 
	 * @param str
	 * @return
	 * @date 2014-2-27 下午10:26:05
	 * @author leo
	 */
	public static boolean isDotLess2Bit(String str)
	{
		Pattern pattern = Pattern.compile("^[0-9]{1,5}([.][0-9]{0,2}){0,1}$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	/**
	 * 2.格式化日期时间格式
	 * 
	 * @param dateFormat
	 *            2010-03-03 13:21:20
	 * @return
	 * @date 2014-2-27 下午10:24:58
	 * @author leo
	 */
	public static String getFormatDateTime(String dateFormat /*
															 * 2010-03-03
															 * 13:21:20
															 */)
	{
		String rtnDate = null;
		if (dateFormat != null && dateFormat.length() >= 10)
		{
			rtnDate = dateFormat.substring(0, 10);
		}
		return rtnDate;
	}
	
	/**
	 * 2.1 格式化日期时间格式
	 * 
	 * @param dateFormat
	 *            2010-03-03 13:21:20
	 * @return
	 * @date 2014-2-27 下午10:24:58
	 * @author leo
	 */
	public static String getFormatMD(String dateFormat /* 2010/3/3 13:21:20 */)
	{
		if(isEmpty(dateFormat))
			return "";
		StringBuffer sb = new StringBuffer();
		String tmp = dateFormat.split(" ")[0];
		String date[] = tmp.split(dateFormat.contains("/") ? "/" : "-");
		if(date != null && date.length >= 3)
		{
			sb.append(date[1]).append("-").append(date[2]);
		}
		return sb.toString();
	}


	/**
	 * 3。字符串是否是中文
	 * 
	 * @param str
	 * @return
	 * @date 2014-2-27 下午10:26:43
	 * @author leo
	 */
	public static boolean isChinese(String str)
	{
		Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]+");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * 4。验证邮箱格式是否正确
	 * 
	 * @param emailString
	 * @return
	 */
	public static boolean isEmail(String email)
	{
		if (isEmpty(email))
			return false;

		Pattern pattern = Pattern.compile("^([a-zA-Z0-9_.-])+@(([a-zA-Z0-9-])+.)+([a-zA-Z0-9]{2,4})+$");
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	/**
	 * 5.验证手机号码的格式是否正确
	 * 
	 * @param mobileString
	 * @return
	 */
	public static boolean isPhone(String phone)
	{
		if (isEmpty(phone))
			return false;

		Pattern pattern = Pattern.compile("^1[3|5|8][0-9]{9}$");
		Matcher matcher = pattern.matcher(phone);
		return matcher.matches();
	}

	/**
	 * 6.账号是否符合验证要求 
	 *   支持2-16位英文、中文、数字、下划线、点
	 *   
	 * @param account
	 * @return
	 * @date 2014-2-28 上午12:13:17
	 * @author leo
	 */
	public static boolean isAccountVerify(String username)
	{
		if (isEmpty(username))
			return false;

		Pattern pattern = Pattern.compile("^([a-zA-Z0-9_.\u4e00-\u9fa5]{2,16})+$");
		Matcher matcher = pattern.matcher(username);
		return matcher.matches();
	}

	/**
	 * 7。密码符合验证要求 
	 *   支持一部分特殊字符、英文字母、数字。不支持中文 支持6-16位
	 *   
	 * @param pwd
	 * @return
	 * @date 2014-2-28 上午12:14:49
	 * @author leo
	 */
	public static boolean isPasswordVerify(String password)
	{
		if (isEmpty(password))
			return false;

		Pattern pattern = Pattern.compile("^([a-zA-Z0-9_-`~!@#$%^&*()+\\|\\\\=,./?><\\{\\}\\[\\]]{6,16})+$");
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	/**
	 * 8。验证输入的密码和输入的确认密码是否是一致的
	 * 
	 * @param pwd
	 * @param confirmPwd
	 * @return
	 * @date 2014-2-28 上午12:16:28
	 * @author leo
	 */
	public static boolean isPwdEquals(String pwd, String confirmPwd)
	{
		return pwd.equals(confirmPwd);
	}
	/**
	 * 9。验证输入的身份证号是否合法
	 * 
	 * @param pwd
	 * @return
	 * @date 2014-2-28 上午12:16:28
	 * @author leo
	 */
	public static boolean isIDCardNo(String str)
	{
		
		if (isEmpty(str))
			return false;

		Pattern pattern = Pattern.compile("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	/**
	 * 输入正则表达式验证规则和字符串，用以判断是否符合正砸表达式
	 * @param regular
	 * @param str
	 * @return
	 * @date 2014-3-17 下午6:45:47
	 * @author leo
	 */
	public static boolean commonPatternCheck(String regular, String str)
	{
		if (isEmpty(str))
			return false;
		Pattern pattern = Pattern.compile(regular);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	public static boolean isNumeric(String str){ 
		if(str == null || "".equals(str)){
			return false;
		}
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();    
	} 
	
	/**
	 * 处理空字符串
	 * 
	 * @param str
	 * @param defaultValue
	 * @return String
	 */
	public static String doEmpty(String str, String defaultValue) {
		if (str == null || str.equalsIgnoreCase("null")
				|| str.trim().equals("") || str.trim().equals("－请选择－")) {
			str = defaultValue;
		} else if (str.startsWith("null")) {
			str = str.substring(4, str.length());
		}
		return str.trim();
	}
	

	
	/**
	 * 数组是否包含字符串
	 * 
	 * @param array
	 * *@param v
	 * @return boolean 
	 */
	public static <T> boolean contains( final T[] array, final T v ) {
	    for ( final T e : array )
	        if ( e == v || v != null && v.equals( e ) )
	            return true;

	    return false;
	}

	public static boolean isMany(String str,int mLong)
	{
		if(str.length() >= mLong){
			   return false;
		 }
		return true;
	}
	/**
	 * 截取openfile jid获取用户名 例如：znw@www.feijizhe.com
	 * @param string
	 * @return string 
	 */
	public static String getAccountId(String from){
		if(StringUtils.isEmpty(from)){
			return null;
		}
		
		if(from.contains("@")){
			String[] split = from.split("\\@");
			return split[0];
		}
		return null;
	}
}
