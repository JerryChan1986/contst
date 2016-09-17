
package utils;


/**
 * 唯一值工具类
 * 
 * @author bing
 */
public class KeyUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		for(int i=0;i<100;i++){
			System.out.println(KeyUtil.getID());
		}
		

	}
    /**
     * 获取唯一值
     * @return
     */
	public static String getID(){
		return java.util.UUID.randomUUID().toString();
	}
	
}
