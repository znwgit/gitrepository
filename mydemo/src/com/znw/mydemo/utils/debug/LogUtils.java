/**
 * @系统项目名称:海米项目
 * @author leo
 * @version 1.0
 * @创建时间：2014-9-18 下午19:21:31
 * @copyright:ilindo公司-版权所有
 * @描述:
 */
package com.znw.mydemo.utils.debug;

import android.util.Log;

public class LogUtils
{
	private static final boolean isLog = true;
	 
	public static void info(String tag, Object o)
	{
		if(isLog)
			Log.i(tag, o == null ? "": o.toString());
	}
	public static void debug(String tag, Object o)
	{
		if(isLog)
			Log.d(tag, o == null ? "": o.toString());
	}
	public static void warn(String tag, Object o)
	{
		if(isLog)
			Log.w(tag, o == null ? "": o.toString());
	}
	public static void error(String tag, Object o)
	{
		if(isLog)
			Log.e(tag, o == null ? "": o.toString());
	}
	
	public static void info(Class<?> c, Object o)
	{
		if(isLog)
			Log.i(c.getSimpleName(), o == null ? "": o.toString());
	}
	public static void debug(Class<?> c, Object o)
	{
		if(isLog)
			Log.d(c.getSimpleName(), o == null ? "": o.toString());
	}
	public static void warn(Class<?> c, Object o)
	{
		if(isLog)
			Log.w(c.getSimpleName(), o == null ? "": o.toString());
	}
	public static void error(Class<?> c, Object o)
	{
		if(isLog)
			Log.e(c.getSimpleName(), o == null ? "": o.toString());
	}
}
