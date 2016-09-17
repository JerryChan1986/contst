
package utils;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * 日期操作工具类.
 * 
 * @author bing
 * 
 */
public class DateUtil {

  /**
   * 默认日期格式：yyyy.
   */
  public static final String DEFAULT_YEAR               = "yyyy";
  /**
   * 默认日期格式：yyyyMM.
   */
  public static final String DEFAULT_DATE               = "yyyyMM";

  /**
   * 默认日期格式：yyyy-MM-dd.
   */
  public static final String DEFAULT_DATE_PATTERN       = "yyyy-MM-dd";

  /**
   * 默认时间格式：yyyy-MM-dd HH:mm:ss.
   */
  public static final String DEFAULT_DATETIME_PATTERN   = "yyyy-MM-dd HH:mm:ss";

  /**
   * 默认时间戳格式，到毫秒 yyyy-MM-dd HH:mm:ss SSS.
   */
  public static final String DEFAULT_DATEDETAIL_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";

  /**
   * 1天折算成毫秒数.
   */
  public static final long   MILLIS_A_DAY               = 24 * 3600 * 1000;

  private static HashMap     parsers                    = new HashMap();

  /**
   * 获得 date parser.
   * 
   * @param pattern the pattern
   * @return the date parser
   */
  private static SimpleDateFormat getDateParser(String pattern) {
    Object parser = parsers.get(pattern);
    if (parser == null) {
      parser = new SimpleDateFormat(pattern);
      parsers.put(pattern, parser);
    }
    return (SimpleDateFormat) parser;
  }

  /**
   * 取得当前系统日期.
   * 
   * @return the java.util. date
   */
  public static java.util.Date currentDate() {
    return new java.util.Date();
  }

  /**
   * 取得系统当前日期，返回默认日期格式的字符串。.
   * 
   * @param strFormat the str format
   * @return the string
   */
  public static String nowDate(String strFormat) {
    java.util.Date date = new java.util.Date();
    return getDateParser(strFormat).format(date);
  }

  /**
   * 取得当前系统时间戳.
   * 
   * @return the timestamp
   */
  public static Timestamp currentTimestamp() {
    return new Timestamp(new Date().getTime());
  }

  /**
   * 将日期字符串转换为java.util.Date对象.
   * 
   * @param dateString the date string
   * @param pattern 日期格式
   * @return the java.util. date
   * @throws Exception the exception
   */
  public static java.util.Date toDate(String dateString, String pattern) throws Exception {
    return getDateParser(pattern).parse(dateString);
  }

  /**
   * 将日期字符串转换为java.util.Date对象，使用默认日期格式.
   * 
   * @param dateString the date string
   * @return the java.util. date
   * @throws Exception the exception
   */
  public static java.util.Date toDate(String dateString) throws Exception {
    return getDateParser(DEFAULT_DATE_PATTERN).parse(dateString);
  }

  /**
   * 将时间字符串转换为java.util.Date对象.
   * 
   * @param dateString the date string
   * @return the java.util. date
   * @throws Exception the exception
   */
  public static java.util.Date toDateTime(String dateString) throws Exception {
    return getDateParser(DEFAULT_DATETIME_PATTERN).parse(dateString);
  }

  /**
   * 将java.util.Date对象转换为字符串.
   * 
   * @param date the date
   * @param pattern the pattern
   * @return the string
   */
  public static String toDateString(java.util.Date date, String pattern) {
    return getDateParser(pattern).format(date);
  }

  /**
   * 将java.util.Date对象转换为字符串，使用默认日期格式.
   * 
   * @param date the date
   * @return the string
   */
  public static String toDateString(java.util.Date date) {
    return getDateParser(DEFAULT_DATE_PATTERN).format(date);
  }

  /**
   * 将java.util.Date对象转换为时间字符串，使用默认日期格式.
   * 
   * @param date the date
   * @return the string
   */
  public static String toDateTimeString(java.util.Date date) {
    return getDateParser(DEFAULT_DATETIME_PATTERN).format(date);
  }

  /**
   * 日期相减.
   * 
   * @param date the date
   * @param day the day
   * @return the date
   */
  public static Date diffDate(java.util.Date date, int day) {
    java.util.Calendar c = java.util.Calendar.getInstance();
    c.setTimeInMillis(getMillis(date) - day * MILLIS_A_DAY);
    return c.getTime();
  }

  /**
   * 返回毫秒.
   * 
   * @param date 日期
   * @return 返回毫秒
   * @author doumingjun create 2007-04-07
   */
  public static long getMillis(java.util.Date date) {
    java.util.Calendar c = java.util.Calendar.getInstance();
    c.setTime(date);
    return c.getTimeInMillis();
  }

  /**
   * 日期相加.
   * 
   * @param date 日期
   * @param day 天数
   * @return 返回相加后的日期
   * @author doumingjun create 2007-04-07
   */
  public static java.util.Date addDate(java.util.Date date, int day) {
    java.util.Calendar c = java.util.Calendar.getInstance();

    c.setTimeInMillis(getMillis(date) + day * MILLIS_A_DAY);
    return c.getTime();
  }

  /**
   * 获得 before time str.
   * 
   * @param currentTimeStr the current time str
   * @return the before time str
   * @Title: getBeforeTimeStr
   * @Description: 根据传入的当前时间毫秒数返回前15分钟的时间字符串
   * @see com.baosight.eidp.util.DateUtil.getBeforeTimeStr
   * @创建人:bing
   * @修改人:
   * @修改原因:
   * @修改时间:
   * @版本:
   */
  public static String getBeforeTimeStr(long currentTimeStr) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date;
    String beforeTimeStr = "";
    try {
      date = new Date(currentTimeStr - 900000);
      beforeTimeStr = dateFormat.format(date);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return beforeTimeStr;
  }
  
  public static String getTimeStr(String longStr) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date date;
	    String beforeTimeStr = "";
	    try {
	      date = new Date(Long.parseLong(longStr));
	      beforeTimeStr = dateFormat.format(date);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return beforeTimeStr;
	  }

  /**
   * main 方法.
   * 
   * @param agrs the arguments
   */
  public static void main(String[] agrs) {
    /*System.out.println(DateUtil.nowDate(DEFAULT_DATETIME_PATTERN));
    System.out.println(DateUtil.nowDate("yyyy"));
    System.out.println(DateUtil.nowDate("MM"));*/
    System.out.println(DateUtil.getTimeStr("1411293804203"));
    
    /*String time_temp_db = "2014-12-11 10:00:00";
	try {
		String time_temp = DateUtil.toDateString(DateUtil.toDate(time_temp_db, DateUtil.DEFAULT_DATETIME_PATTERN),"yyyyMMddHHmmss");
		System.out.println(time_temp);
	} catch (Exception e) {
		
		e.printStackTrace();
	}*/
    
    long currentTime=Long.parseLong("1418448608827");
    long news=System.currentTimeMillis();//Long.parseLong("1418448093843");
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
    
    if(news>=currentTime){
    	System.out.println("ok"+news);
    }else{
    	System.out.println("error"+news);
    }
    
    try {
    	Date date_current=new Date(currentTime);
        Date date_news=new Date(news);
        boolean flag = date_news.after(date_current);
        if(flag){
        	System.out.println("ok(requestTime:"+dateFormat.format(date_current)+";date_news:"+dateFormat.format(date_news)+")");
        }else{
        	System.out.println("error(requestTime:"+dateFormat.format(date_current)+";date_news:"+dateFormat.format(date_news)+")");
        }
        
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
}
