package contest;  
  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.InputStream;  
import java.io.UnsupportedEncodingException;

import org.apache.struts2.ServletActionContext;  
  

public class FileDownloadAction {  
    private String fileName;
    private String basePath;
    public String execute(){  
        return "succ";  
    }  
      
    public InputStream getInputStream() throws FileNotFoundException {
    	
    	basePath = ServletActionContext.getServletContext().getRealPath("/");
		//解决Weblogic下路径为NULL的bug
        if(basePath==null){
        	String absoluteClassPath = this.getClass().getClassLoader().getResource("/").getPath();
        	String relativeClassPath = "WEB-INF/classes/";
        	basePath = absoluteClassPath.substring(0, absoluteClassPath.length()-relativeClassPath.length());
        }
        //安全堵漏目录contest
        basePath +="/contest/";
        InputStream f;
		try {
			f = new FileInputStream(new File(basePath, fileName));
		} catch (FileNotFoundException e) {
			fileName = "/images/banner_sm.png";
			f = new FileInputStream(new File(basePath, fileName));
		}
    	return  f; 

    }  
  
    public String getFileName() throws UnsupportedEncodingException {  
        return new String(fileName.getBytes(), "ISO-8859-1");  
    }  
  
    public void setFileName(String fileName) {  
        this.fileName = fileName;  
    }  
}  