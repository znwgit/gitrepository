package com.znw.mydemo.utils.date;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.znw.mydemo.utils.string.StringUtils;



import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.text.format.Time;

public final class DateUtil {
	@SuppressLint("SimpleDateFormat")
	public static SimpleDateFormat HHmmss = new SimpleDateFormat("HH:mm:ss");
	public static SimpleDateFormat HHmmssNoColon = new SimpleDateFormat("HHmmss");
	public static SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");

	public static SimpleDateFormat MMddYYYYHHmmss = new SimpleDateFormat("MMddyyyyHHmmss");
	public static SimpleDateFormat MMddHHmmss = new SimpleDateFormat("MMddHHmmss");
	public static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");

	public static SimpleDateFormat shortyyyyMMdd = new SimpleDateFormat("yyyyMMdd");

	public static SimpleDateFormat yyyy_MM_dde = new SimpleDateFormat("yyyy-MM-dd E");
	public static SimpleDateFormat yyyyMMddHHmm = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public static SimpleDateFormat yyyy_MM_dd_HHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat HHmm = new SimpleDateFormat("HH:mm");
	public static SimpleDateFormat yyyyMMdd_HHmmss = new SimpleDateFormat("yyyyMMdd_HHmmss");

	public static String getCurrentDateTime() {
		return yyyy_MM_dd_HHmmss.format(new Date());
	}

	/**
	 * 将这种类型yyyy-MM-dd HH:mm的时间转化为long类型的
	 * 
	 * @param serverTimeString
	 * @return
	 */
	public static long getMillisecondsFromString(String serverTimeString) {
		if (TextUtils.isEmpty(serverTimeString)) {
			return 0;
		}

		long millisecond;
		try {
			millisecond = yyyyMMddHHmm.parse(serverTimeString).getTime();
			return millisecond;
		} catch (ParseException e) {
			return 0;
		}
	}
	/**
	 * 判断这种类型yyyy-MM-dd HH:mm的时间是否大于当前时间
	 * 
	 * @param serverTimeString
	 * @return
	 */
	public static boolean isMoreCurrentDate(String currentString) {
		if (TextUtils.isEmpty(currentString)) {
			return false;
		}
		
		long millisecond;
		try {
			millisecond = yyyyMMddHHmm.parse(currentString).getTime();
			if(millisecond>System.currentTimeMillis()){
				return true;
			}else {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 判断第一个时间减去第二个时间是否大于设定的值
	 * 
	 * @param firstString
	 * @param secondString
	 * @param time 秒
	 * @return
	 */
	public static boolean isMore(String firstString,String secondString,long time) {
		if (TextUtils.isEmpty(firstString)||TextUtils.isEmpty(secondString)) {
			return false;
		}
		
		long fmillisecond;
		long smillisecond;
		long temp;
		try {
			fmillisecond = yyyyMMddHHmm.parse(firstString).getTime();
			smillisecond= yyyyMMddHHmm.parse(secondString).getTime();
			temp=(smillisecond-fmillisecond)/1000 ;
			if(temp>time){
				return true;
			}else {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将毫秒数转化成yyyy-MM-dd HH:mm:ss类型的日期
	 * 
	 * @param milliseconds
	 * @return
	 */
	public static String getStringDateFromMilliseconds(long milliseconds) {
		if (milliseconds == 0) {
			return "";
		}

		String string;
		Date date = new Date(milliseconds);
		string = yyyy_MM_dd_HHmmss.format(date);
		return string;
	}

	public static String getTime(String timeStr, long time) {
		try {
			if (TextUtils.isEmpty(timeStr)) {
				return getCurrentDateTime();
			} else {
				long timeSum = yyyyMMddHHmmss.parse(timeStr).getTime() + time;
				Date date = new Date(timeSum);
				return yyyyMMddHHmmss.format(date);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return getCurrentDateTime();
		}
	}

	public static String getyyyy_MM_ddTime(Date date) {
		return yyyyMMdd.format(date);
	}
	
	public static String getStringDateFromUnix(long milliseconds) {
		
		if (milliseconds == 0) {
			return "";
		}
		SimpleDateFormat yyyyMMddHHmm = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(milliseconds * 1000);
		return yyyyMMddHHmm.format(date);
	}

	public static String getTimeyyyy_MM_dd_HH_mm_ss(String timeStr, long time) {
		try {
			if (TextUtils.isEmpty(timeStr)) {
				return yyyy_MM_dd_HHmmss.format(new Date());
			} else {
				long timeSum = yyyyMMddHHmmss.parse(timeStr).getTime() + time;
				Date date = new Date(timeSum);
				return yyyy_MM_dd_HHmmss.format(date);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return yyyy_MM_dd_HHmmss.format(new Date());
		}
	}

	/**
	 * 根据毫秒数 获取天数
	 * 
	 * @param millisSeconds
	 * @return
	 */
	public static String getStringFromMillisSeconds(long millisSeconds) {
		String string = "";
		long days = millisSeconds / 1000 / 60 / 60 / 24;
		long hours = (millisSeconds - days * 24 * 60 * 60 * 1000) / 1000 / 60 / 60;
		long mins = (millisSeconds - days * 24 * 60 * 60 * 1000 - hours * 60 * 60 * 1000) / 1000 / 60;
		long seconds = (millisSeconds - days * 24 * 60 * 60 * 1000 - hours * 60 * 60 * 1000 - mins * 60 * 1000) / 1000;
		string = days + "天" + hours + "小时" + mins + "分钟" + seconds + "秒";
		return string;
	}
	
	public static String[] getStrings(long millisSeconds) {
		String[] strings=new String[6];
		long hours = millisSeconds / 1000 / 60 / 60;
		long mins = (millisSeconds - hours * 60 * 60 * 1000) / 1000 / 60;
		long seconds = (millisSeconds - hours * 60 * 60 * 1000 - mins * 60 * 1000) / 1000;
		//string = days + "天" + hours + "小时" + mins + "分钟" + seconds + "秒";
		if(hours<99){
			strings[0]=String.valueOf(hours/10);
			strings[1]=String.valueOf(hours%10);
		}else {
			strings[0]="9";
			strings[1]="9";
		}
		strings[2]=String.valueOf(mins/10);
		strings[3]=String.valueOf(mins%10);
		strings[4]=String.valueOf(seconds/10);
		strings[5]=String.valueOf(seconds%10);
		return strings;
	}
	
	
	public static String formatTimeString2(String timeStr) {  
		long millisecond;
		try
		{
			millisecond = yyyy_MM_dd_HHmmss.parse(timeStr).getTime();
			return formatTimeString(millisecond);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	/** 
     * 显示时间格式为今天、昨天、yyyy/MM/dd hh:mm 
     *  
     * @param context 
     * @param when 
     * @return String 
     */  
    public static String formatTimeString( long when) {  
        Time then = new Time();  
        then.set(when);  
        Time now = new Time();  
        now.setToNow();  
  
        String formatStr;  
        if (then.year != now.year) {  
            formatStr = "yyyy-MM-dd";  
        } else if (then.yearDay != now.yearDay) {  
            // If it is from a different day than today, show only the date.  
            formatStr = "MM-dd";  
        } else {  
            // Otherwise, if the message is from today, show the time.  
            formatStr = "HH:MM";  
        }  
        if(then.year == now.year && then.yearDay == now.yearDay && now.hour==then.hour && now.minute==then.minute && (now.second-then.second)>0){
        	return (now.second-then.second)+"秒前";
        }else if(then.year == now.year && then.yearDay == now.yearDay && now.hour==then.hour && (now.minute-then.minute)>0){
        	return (now.minute-then.minute)+"分钟前";
        }else if(then.year == now.year && then.yearDay == now.yearDay && (now.hour-then.hour)>0){
        	return (now.hour-then.hour)+"小时前";
        }else if ((then.year == now.year) && ((now.yearDay - then.yearDay) == 1)) {  
            return "昨天";  
        } else {  
            SimpleDateFormat sdf = new SimpleDateFormat(formatStr);  
            String temp = sdf.format(when);  
            if (temp != null && temp.length() == 5 && temp.substring(0, 1).equals("0")) {  
                temp = temp.substring(1);  
            }  
            return temp;  
        }  
    }  
	public static String getTimeMMddYYYYHHmmss(String timeStr, long time) {
		try {
			if (TextUtils.isEmpty(timeStr)) {
				return MMddYYYYHHmmss.format(new Date());
			} else {
				long timeSum = yyyyMMddHHmmss.parse(timeStr).getTime() + time;
				Date date = new Date(timeSum);
				return MMddYYYYHHmmss.format(date);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return MMddYYYYHHmmss.format(new Date());
		}
	}

	public static String getTimeMMddHHmmss(String timeStr, long time) {
		try {
			if (TextUtils.isEmpty(timeStr)) {
				return MMddHHmmss.format(new Date());
			} else {
				long timeSum = yyyyMMddHHmmss.parse(timeStr).getTime() + time;
				Date date = new Date(timeSum);
				return MMddHHmmss.format(date);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return MMddHHmmss.format(new Date());
		}
	}

	public static long getTimeLong(String timeStr, long time) {
		try {
			if (TextUtils.isEmpty(timeStr)) {
				return new Date().getTime();
			} else {
				long timeSum = yyyyMMddHHmmss.parse(timeStr).getTime() + time;
				return timeSum;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date().getTime();
		}
	}

	public static String getDate(String timeStr, long time) {
		try {
			if (TextUtils.isEmpty(timeStr)) {
				Date date = new Date();
				return shortyyyyMMdd.format(date);
			} else {
				long timeSum = yyyyMMddHHmmss.parse(timeStr).getTime() + time;
				Date date = new Date(timeSum);
				return shortyyyyMMdd.format(date);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return getDate(new Date());
		}
	}

	public static String getDateyyyy_MM_dd(String timeStr, long time) {
		try {
			if (TextUtils.isEmpty(timeStr)) {
				Date date = new Date();
				return yyyyMMdd.format(date);
			} else {
				long timeSum = yyyyMMddHHmmss.parse(timeStr).getTime() + time;
				Date date = new Date(timeSum);
				return yyyyMMdd.format(date);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return getDate(new Date());
		}
	}

	public static String getOnlytime(String timeStr, long time) {
		try {
			if (TextUtils.isEmpty(timeStr)) {
				return HHmmssNoColon.format(new Date());
			} else {
				long timeSum = yyyyMMddHHmmss.parse(timeStr).getTime() + time;
				Date date = new Date(timeSum);
				return HHmmssNoColon.format(date);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return HHmmssNoColon.format(new Date());
		}
	}

	public static String getOnlytime(Date date) {
		return HHmmssNoColon.format(date);
	}

	public static String getDate(Date date) {
		return yyyyMMdd.format(date);
	}

	public static String getTime() {
		return HHmmss.format(new Date());
	}

	public static Long getCurrentMilliseconds() {
		return (new Date().getTime());
	}

	public static String formatDate(String date) {
		try {
			Date d = yyyyMMdd.parse(date);
			return yyyyMMdd.format(d);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String formatDate(SimpleDateFormat sdf, String date) {
		try {
			Date d = sdf.parse(date);
			String result = sdf.format(d);
			return result;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date addDate(Date dt, int num) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(dt);
		rightNow.add(Calendar.DATE, num);// 你要加减的日
		Date result = rightNow.getTime();
		return result;
	}

	/**
	 * 获取当前时间的字符串用来给图片命名
	 * 
	 * @return
	 */
	public static String getCurrentTimeForPicName() {
		Date date = new Date();
		String format = yyyyMMdd_HHmmss.format(date);
		return format;
	}

	/**
	 * 将10未时间戳补000，转成日期
	 */
	public static String getTenTime(int tenTime) {
		String time = String.valueOf(tenTime);
		if (!StringUtils.isEmpty(time)) {
			time = time + "000";
		}
		long parserTime = Long.parseLong(time);

		Date date = new Date();
		date.setTime(parserTime);
		return getyyyy_MM_ddTime(date);
	}

}
