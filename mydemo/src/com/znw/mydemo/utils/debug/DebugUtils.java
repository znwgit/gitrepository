/**
 * @系统项目名称:钢铁网移动客户端开发
 * @author leo
 * @version 1.0
 * @创建时间：2014-2-28 下午2:55:18
 * @copyright:ilindo公司-版权所有
 * @描述:
 */
package com.znw.mydemo.utils.debug;

public class DebugUtils
{
	public static final boolean DEBUG = true;
	public static void print(Object msg)
	{
		if(DEBUG)
			System.out.println(msg);
	}
	
	public static void print(String tag, String msg)
	{
		if(DEBUG)
			System.out.println(tag + ":"+ msg);
	}
	
	public static void print(Class<?> c, String msg)
	{
		if(DEBUG)
			System.out.println(c.getSimpleName() + ":"+ msg);
	}
}
