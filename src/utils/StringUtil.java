
package utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 字符串操作工具类
 * 
 * @author bing
 */
public class StringUtil {

  private static final int TWO_BYTE_START = 0x100;

  /**
   * 替换字符串中的下划线并根据isFirstUpperCase参数确定下划线后首字母是否大写 例如： abc_dec 替换后为 abcDec.
   * 
   * @param src the src
   * @param isFirstUpperCase the is first upper case
   * @return the string
   */
  public static String replaceUnderline(String src, boolean isFirstUpperCase) {
    if (src == null || src.length() == 0) { return src; }

    String[] res = src.split("_");
    if (res.length == 1) { return src.toLowerCase(); }

    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < res.length; i++) {
      String temp = res[i];

      if (temp == null || temp.trim().length() == 0) {
        continue;
      }

      temp = temp.trim().toLowerCase();

      if (i > 0) {
        temp = temp.substring(0, 1).toUpperCase() + temp.substring(1);
      }

      buffer.append(temp);
    }// end for
    return buffer.toString();
  }

  /**
   * 将字符串根据分割符分割为字符串数组.
   * 
   * @param s the s
   * @param delimiters the delimiters
   * @param trimTokens the trim tokens
   * @param ignoreEmptyTokens the ignore empty tokens
   * @return the string[]
   */
  public static String[] split(String s, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
    String src = s;
    if (src == null) { return null; }

    if (trimTokens) {
      src = src.trim();
      src = src.replaceAll("\n", "");
      src = src.replaceAll("\r", "");
      src = src.replaceAll("\t", "");
    }

    String[] str = src.split(delimiters);
    if (ignoreEmptyTokens) {
      for (int i = 0; i < str.length; i++) {
        str[i] = str[i].trim();
      }
    }

    return str;
  }

  /**
   * 将一个数组中的内容连接成一个字符串，各个数组中的元素使用参数delim分隔.
   * 
   * @param arr the arr
   * @param delim the delim
   * @return the string
   */
  public static String arrayToString(String[] arr, String delim) {
    if (arr == null) {
      return "null";
    } else {
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < arr.length; i++) {
        if (i > 0) {
          sb.append(delim);
        }
        sb.append(arr[i]);
      }
      return sb.toString();
    }
  }

  /**
   * 将一个数组中的内容连接成一个字符串，各个数组中的元素使用参数delim分隔,
   * 为了便于sql查询，每个字符串前后用fg包装.
   * 
   * @param arr the arr
   * @param delim the delim
   * @param fg the fg
   * @return the string
   */
  public static String arrayToString(String[] arr, String delim, String fg) {
    if (arr == null) {
      return "null";
    } else {
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < arr.length; i++) {
        if (i > 0) {
          sb.append(delim);
        }
        sb.append(fg + arr[i] + fg);
      }
      return sb.toString();
    }
  }

  public static String arrayToString(Object[] arr, String delim, String fg) {
	    if (arr == null) {
	      return "null";
	    } else {
	      StringBuffer sb = new StringBuffer();
	      for (int i = 0; i < arr.length; i++) {
	        if (i > 0) {
	          sb.append(delim);
	        }
	        sb.append(fg + arr[i].toString() + fg);
	      }
	      return sb.toString();
	    }
	  }
  
  /**
   * 将map全部value内容拼装成字符串形式，如'1','2','3'
   * @param arr 
   * @param delim 分隔符，如 ,
   * @param fg    字符分割 如 '
   * @param desc  排序，默认asc
   * @return
   */
  public static String mapValueToString(Map arr, String delim, String fg, Boolean desc) {
	    if (arr == null) {
	      return "";
	    } else {
	      StringBuffer sb = new StringBuffer();
	      Object[] t =arr.values().toArray();
	      if(desc){//倒序
	    	  int len=t.length;
	    	  Object[] t_new=new Object[len];
	    	  for(int i=0;i<len;i++){
	    		  t_new[i]=t[len-i-1];
	    	  }
	    	  return arrayToString(t_new,delim,fg);
	      }else{
	    	  return arrayToString(t,delim,fg);
	      } 
	    }
  }
  
  /**
   * 数组倒排
   * @param old
   * @return
   */
  public static Object[] revertObject(Object[] old) {
	    if (old == null) {
	      return null;
	    } else {
	      int len=old.length;
	      Object[] t_new=new Object[len];
	      for(int i=0;i<len;i++){
	    	 t_new[i]=old[len-i-1];
	      }
	      return t_new;	  
	    }
}
  
  /**
   * 将map全部key内容拼装成字符串形式，如'1','2','3'
   * @param arr 
   * @param delim 分隔符，如 ,
   * @param fg    字符分割 如 '
   * @return
   */
  public static String mapKeyToString(Map arr, String delim, String fg) {
	    if (arr == null) {
	      return "";
	    } else {
	      StringBuffer sb = new StringBuffer();
	      return arrayToString((String[])arr.keySet().toArray(),delim,fg);
	    }
  }
  
  public static String[] mapKeyToStringArr(Map arr) {
	  if (arr == null) {
	      return new String[0];
	   } else {
		  Object[] r=arr.keySet().toArray();
	      String[] t=new String[r.length];
	      for(int i=0;i<t.length;i++){
	    	  t[i]=r[i].toString();
	      }
		return t;
	   }
  }
  
  
  public static String[] mapValueToStringArr(Map arr) {
	  if (arr == null) {
	      return new String[0];
	   } else {
		  Object[] r=arr.values().toArray();
	      String[] t=new String[r.length];
	      for(int i=0;i<t.length;i++){
	    	  t[i]=r[i].toString();
	      }
		return t;
	   }
  }
  
  
  /**
   * 本地字符集转换成unicode。.
   * 
   * @param s the s
   * @return java.lang.String
   */
  public static String native2unicode(String s) {
    if (s == null || s.length() == 0) { return null; }
    byte[] buffer = new byte[s.length()];

    for (int i = 0; i < s.length(); i++) {
      if (s.charAt(i) >= TWO_BYTE_START) { return s; }
      buffer[i] = (byte) s.charAt(i);
    }
    return new String(buffer);
  }

  /**
   * unicode转为本地字符集.
   * 
   * @param s Unicode编码的字符串
   * @return String
   */
  public static String unicode2native(String s) {
    if (s == null || s.length() == 0) { return null; }
    char[] buffer = new char[s.length() * 2];
    char c;
    int j = 0;
    for (int i = 0; i < s.length(); i++) {
      if (s.charAt(i) >= TWO_BYTE_START) {
        c = s.charAt(i);
        byte[] buf = ("" + c).getBytes();
        buffer[j++] = (char) buf[0];
        buffer[j++] = (char) buf[1];
      } else {
        buffer[j++] = s.charAt(i);
      }
    }
    return new String(buffer, 0, j);
  }

  /**
   * 对字符串中制定的字符进行替换.
   * 
   * @param inString the in string
   * @param oldPattern the old pattern
   * @param newPattern the new pattern
   * @return the string
   */
  public static String replace(String inString, String oldPattern, String newPattern) {
    if (inString == null) { return null; }
    if (oldPattern == null || newPattern == null) { return inString; }

    StringBuffer sbuf = new StringBuffer();
    // output StringBuffer we'll build up
    int pos = 0; // Our position in the old string
    int index = inString.indexOf(oldPattern);
    // the index of an occurrence we've found, or -1
    int patLen = oldPattern.length();
    while (index >= 0) {
      sbuf.append(inString.substring(pos, index));
      sbuf.append(newPattern);
      pos = index + patLen;
      index = inString.indexOf(oldPattern, pos);
    }
    sbuf.append(inString.substring(pos));

    // remember to append any characters to the right of a match
    return sbuf.toString();
  }

  /**
   * 替换输入字符串中的HTML字符< 和 >.
   * 
   * @param old the old
   * @return the string
   */
  public static String replaceHtml(String old) {
    String rt = replace(old, "<", "&lt;");
    rt = replace(rt, ">", "&gt;");
    rt = replace(rt, "'", "&quot;");
    rt = replace(rt, "'", "&quot;");
    rt = replace(rt, "\"", "&quot;");
    return rt;
  }

  /**
   * 转换至 databse pattern.
   * 
   * @param old the old
   * @return the string
   */
  public static String toDatabsePattern(String old) {
    StringBuffer sb = new StringBuffer();
    if (old != null && old.length() > 0) {

      if (old.indexOf("_") >= 0) { return old.toUpperCase(); }

      if (Character.isUpperCase(old.charAt(0))) { return old.toUpperCase(); }

      for (int i = 0; i < old.length(); i++) {
        char s = old.charAt(i);
        if (Character.isUpperCase(s)) {
          sb.append("_");
        }
        sb.append(Character.toUpperCase(s));

      }
    } else {
      return old;
    }
    return sb.toString();
  }

  /**
   * 为空判断。.
   * 
   * @param s the s
   * @return java.lang.String
   */
  public static String notEmpt(String s) {
    if (s == null || s.length() == 0 || s.equals("null")) {
      return "";
    } else {
      return s;
    }
  }

  /**
   * 为空判断。.
   * 
   * @param s the s
   * @return java.lang.String
   */
  public static String notEmpt(Object s) {
    if (s == null || s.toString().length() == 0 || s.toString().equals("null")) {
      return "";
    } else {
      return s.toString();
    }
  }

  public static String notEmpt1(Object s,String nullValue) {
	    if (s == null || s.toString().length() == 0 || s.toString().equals("null")) {
	      return nullValue;
	    } else {
	      return s.toString();
	    }
  }
  
  /**
   * 为空判断。.
   * 
   * @param s the s
   * @return java.lang.String
   */
  public static boolean isEmpt(Object s) {
    if (s == null || s.toString().length() == 0 || s.toString().equals("null")) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 为空判断。.
   * 
   * @param s the s
   * @param dis the dis
   * @return java.lang.String
   */
  public static String notEmpt(Object s, String dis) {
    if (s == null || s.toString().length() == 0 || s.toString().equals("null")) {
      return dis;
    } else {
      return s.toString();
    }
  }

  /**
   * 为空判断。
   * s:判断对象
   * dis:为空显示值
   * dis2:不为空显示值.
   * 
   * @param s the s
   * @param dis the dis
   * @param dis2 the dis2
   * @return java.lang.String
   */
  public static String notEmpt(Object s, String dis, String dis2) {
    if (s == null || s.toString().length() == 0 || s.toString().equals("null")) {
      return dis;
    } else {
      return dis2;
    }
  }

  /**
   * 转换数字.
   * 
   * @param s the s
   * @return java.lang.String
   */
  public static Integer notEmptInt(Object s) {
    if (s == null || s.toString().replace(" ", "").length() == 0 || s.toString().equals("null")) {
      return 0;
    } else {
      return Integer.valueOf(s.toString().replace(" ", ""));
    }
  }

  /**
   * 日期.
   * 
   * @param s the s
   * @param format the format
   * @return java.lang.String
   */
  public static String dataToStr(Object s, String format) {
    if (s == null || s.toString().replace(" ", "").length() == 0 || s.toString().equals("null")) {
      return "";
    } else {
      return new java.text.SimpleDateFormat(format).format(s);
    }
  }

  /**
   * Sql_inj.
   * 
   * @param str the str
   * @return true, if successful
   */
  public static boolean sql_inj(String str) {
    // String inj_str =
    // "'|and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+|,";
    String inj_str = "'@@@->@@@exec";

    String inj_stra[] = inj_str.split("@@@");
    for (int i = 0; i < inj_stra.length; i++) {
      if (str.indexOf(inj_stra[i]) >= 0) { return true; }
    }
    return false;
  }

  /**
   * 获得 string sub.
   * 
   * @param str the str
   * @param lengthCN the length cn
   * @return the string sub
   */
  public static String getStringSub(String str, int lengthCN) {
    if (str == null || str.length() == 0) { return ""; }
    if (str.getBytes().length > lengthCN * 2 && str.length() > lengthCN) {
      return str.substring(0, 11) + "..";
    } else {
      return str;
    }
  }
  
  /**
   * 获取数字型字符串小数点前n位<br>
   * @param str  数字型字符串
   * @param lengthCN 小数点位数
   * @return
   */
  public static String getNumberStringSub(String str, int lengthCN) {
	    if (str == null || str.length() == 0) { return ""; }
	    
	    String[] s=str.split("\\.");
	    if(s.length==1){
	    	return str;
	    }else{
	    	if(s[1].length()<=lengthCN){
	    		return str;
	    	}else{
	    		return s[0]+"."+s[1].substring(0, lengthCN);
	    	}
	    	
	    }
	    
  }

  /**
   * main 方法.
   * 
   * @param args the arguments
   */
  public static void main(String args[]) {
    // sql_inj("admin");
    String[] s = "'A001','A002','A003','A004'".split(",");
    System.out.println(arrayToString(s, ","));
    
    HashMap map=new HashMap();
    map.put("1", "a1");
    map.put("2", "a2");
    System.out.println(mapValueToString(map, ",","'",true));
    
    System.out.println(getNumberStringSub("0.3315851801725596",2));
    
    
  }

  /**
   * JAVA判断字符串数组中是否包含某字符串元素.
   * 
   * @param substring 某字符串
   * @param source 源字符串数组
   * @return 包含则返回true，否则返回false
   */
  public static boolean isIn(String substring, String[] source) {
    if (source == null || source.length == 0) { return false; }
    for (int i = 0; i < source.length; i++) {
      String aSource = source[i];
      if (aSource.equals(substring)) { return true; }
    }
    return false;
  }
  
  public static String echoString(String str) {
	 if(str==null){
		 return "";
	 }
	 if(str.equals("null")){
		 return "";
	 }
	 return str.trim();
  }

  public static String echoString(Object obj) {
		 if(obj==null){
			 return "";
		 }
		 if(obj.equals("null")){
			 return "";
		 }
		 return obj.toString();
	  }

public static boolean isNotNull(String str) {
	if(str==null){
		return false;
	}
	if(str.equals("null")){
		return false;
	}
	return true;
}

}
