package test;

import java.util.concurrent.*;
import java.util.*;
import java.util.Map.*;

import java.io.*;

import controller.ExamController;

import utils.*;
import gyw.log.*;

/** 
* Description:  <p>
* Created By: GuYiwei         <br>
*         At: 2016年9月14日  		14:37:03  <p>
* Modfied By: GuYiwei         <br>
*         At: 2016年9月14日  		14:37:03  <p>
* @author GuYiwei (Yiwei.gu09@gmail.com)
*/
public class AdvancedFileReader {
	
	//file dir path
	private static final String path =  ExamController.class.getResource("/").getPath()+ "/dataFiles/";

	//six TransDetailsLists mapping to six source files
	private static final int FILE_NUM = 6;
	private TransDetailsLists[] TransDetailsListsArray = new TransDetailsLists[FILE_NUM];
	
	//thread pool number
	private final int THREAD_NUM = 6;
	
	//thread pool
	private ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUM);
	
	public AdvancedFileReader() {
		for (int i = 0; i < FILE_NUM ; ++i) {
			TransDetailsListsArray[i] = new TransDetailsLists();
		}
	}
	
	/**
	* Read file one line by one line
	* @param fileId  [1, 2, 3, 4, 5, 6];  each fileId represents one specific file
	*/
	@Log(mode=2, tag="gyw", dir="/data/weblogic12/domains/jsdomain/log", name="log_question1")
	public Future<Integer> read(final int fileId) {
		Callable<Integer> callable = new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				String filepath = path + "DetailsFile_20160" + fileId + ".csv";
				LogUtil.d("filepath = [" + filepath + "]");
				
				try {
					FileInputStream fis = new FileInputStream(filepath);
					InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
					BufferedReader br = new BufferedReader(isr);
					
					List<String> sql_list_serialno     = new ArrayList<String>();
					List<String> sql_list_trans_date   = new ArrayList<String>();
					List<String> sql_list_trans_time   = new ArrayList<String>();
					List<String> sql_list_trans_amount = new ArrayList<String>();
					List<String> sql_list_store_id     = new ArrayList<String>();
					List<String> sql_list_prod_id      = new ArrayList<String>();
					
					String sqlline = "";
					while ((sqlline = br.readLine()) != null) {
						if (sqlline != null && sqlline.length() > 1
								&& sqlline.indexOf(',') > 0) {
							//cnt++;
							String[] arrs = sqlline.split(",");
							String serialno = KeyUtil.getID();
							String trans_date = arrs[0];
							String trans_time = arrs[1];
							String trans_amount = arrs[2];
							String store_id = arrs[3];
							String prod_id = arrs[4];
							
							sql_list_serialno.add(serialno);  
							sql_list_trans_date.add(trans_date);
							sql_list_trans_time.add(trans_time);  
							sql_list_trans_amount.add(trans_amount);
							sql_list_store_id.add(store_id);
							sql_list_prod_id.add(prod_id);	
						}
					}
					
					TransDetailsListsArray[fileId-1].serialno     = sql_list_serialno;    
					TransDetailsListsArray[fileId-1].trans_date   = sql_list_trans_date;  
					TransDetailsListsArray[fileId-1].trans_time   = sql_list_trans_time; 
					TransDetailsListsArray[fileId-1].trans_amount = sql_list_trans_amount;
					TransDetailsListsArray[fileId-1].store_id     = sql_list_store_id;    
					TransDetailsListsArray[fileId-1].prod_id      = sql_list_prod_id; 
					
					return 1;
				} catch (Exception e) {
					LogUtil.e(e);
					return 0;
				}
			}
		};
		
		return executor.submit(callable);
	}
	
	/**
	 * Shutdown thread pool
	 */
	public void close() {
		//shutdown thread pool
		executor.shutdown();
	}
	
	/**
	 * Clear all data
	 */
	public void clear() {
		//speed up gc
		for (int i = 0; i < 6; ++i) {
			TransDetailsListsArray[i] = null;
		}
		
		TransDetailsListsArray = null;
	}

	/**
	 * Return TransDetailsLists corresponding to fileId
	 * @param fileId  
	 */
	public TransDetailsLists getTransDetailsLists(int fileId) {
		return TransDetailsListsArray[fileId-1];
	}
	
	public int getRecordsNumber(int fileId) {
		return TransDetailsListsArray[fileId-1].serialno.size();
	}
}