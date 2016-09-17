package contest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Statement;

import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class FileUploadAction extends ActionSupport {

	private File file;
	private String fileFileName;
	private String fileFileContentType;

	private String message = "";
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getFileFileContentType() {
		return fileFileContentType;
	}

	public void setFileFileContentType(String fileFileContentType) {
		this.fileFileContentType = fileFileContentType;
	}

	@Override
	public String execute() throws Exception {
		
		String path = ServletActionContext.getServletContext().getRealPath("/contest/upload");
		String USER_ID = ServletActionContext.getRequest().getParameter("userid");
		
		//解决Weblogic下路径为NULL的bug
        if(path==null){
        	String absoluteClassPath = this.getClass().getClassLoader().getResource("/").getPath();
        	String relativeClassPath = "WEB-INF/classes/";
        	path = absoluteClassPath.substring(0, absoluteClassPath.length()-relativeClassPath.length())+"/contest/upload";
        }

		try {
			File f = this.getFile();
			FileInputStream inputStream = new FileInputStream(f);
			FileOutputStream outputStream = new FileOutputStream(path + "/"+ this.getFileFileName());
			byte[] buf = new byte[1024];
			int length = 0;
			while ((length = inputStream.read(buf)) != -1) {
				outputStream.write(buf, 0, length);
			}
			inputStream.close();
			outputStream.flush();
			outputStream.close();
			Connection conn = null;			  
			Statement stmt = null;
			String sql;
			try {
				Object ds_obj = utils.ApplicationContextHelper
						.getBean("dataSource");
				conn = ((javax.sql.DataSource) ds_obj).getConnection();			  
 				stmt=conn.createStatement();
 				sql="update contest_user set avatar='./upload/"+ this.getFileFileName()+"' where id = "+USER_ID;
 				int rs=stmt.executeUpdate(sql);
	 			if(rs>0) {	 				
	 				message = "文件上传成功";
	 			}
	 			else{
	 				message = "数据库更新失败";
	 			}
	 		}catch (Exception e) {
				//e.printStackTrace();
				message = "数据库出错";
			}finally{
				stmt.close();
	 			conn.close();
			}
		} catch (Exception e) {
			//e.printStackTrace();
			message = "出错了!!!!";
		}
		return SUCCESS;
	}

}
