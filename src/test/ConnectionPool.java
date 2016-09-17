package test;

import java.util.concurrent.*;
import java.util.*;
import java.util.Map.*;

import java.sql.*; 

import utils.DBUtil;

import gyw.log.*;

/** 
* Description:Provide a connection pool with fixed connection number. <br>
*             Each thread in the threadpool will use a thread local connection<p>
* Created By: GuYiwei         <br>
*         At: 2016年9月13日  		20:01:23  <p>
* Modfied By: GuYiwei         <br>
*         At: 2016年9月13日  		20:01:23  <p>
* @author GuYiwei (Yiwei.gu09@gmail.com)
*/
@Log(mode=2, tag="gyw", dir="/data/weblogic12/domains/jsdomain/log", name="log_question1")
public class ConnectionPool {
	
	private static final int CONNECTION_NUM = 2;
	
	//thread pool
	private static ExecutorService executor = Executors.newFixedThreadPool(CONNECTION_NUM);
	
	//hold connections to database
	private static Map<Long, Connection> connectionsMap= new ConcurrentHashMap<Long, Connection>();
	
	public static boolean init() {
	    LogUtil.d("init");
		Future[] results = new Future[CONNECTION_NUM];
		for (int i = 0; i < CONNECTION_NUM; ++i) {
		    LogUtil.d("i = " + i);
			results[i] = getConnectionForThread();
		}
		
		int successCnt = 0;
		try {
			for (Future result : results) {
			    int resultVal = (int)result.get();
			    LogUtil.d("resultVal = " + resultVal);
				if (resultVal == 1) {
					successCnt++;
				}
			}
		} catch (Exception e) {
		    LogUtil.e(e);
			return false;
		}
		
		LogUtil.d("successCnt = " + successCnt);
		if (successCnt != CONNECTION_NUM) {
			return false;
		}
		return true;
	}
	
	/**
	 * Get database connection for one thread in threadpool
	 */
	@Log(mode=2, tag="gyw", dir="/data/weblogic12/domains/jsdomain/log", name="log_question1")
	private static Future<Integer> getConnectionForThread() {
		Callable<Integer> callable = new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				try {
					LogUtil.d("getConnectionForThread");
					Object ds_obj = utils.ApplicationContextHelper
		                    .getBean("dataSource");
				    Connection conn = ((javax.sql.DataSource) ds_obj).getConnection();
				    
				    if (conn == null) {
				        LogUtil.e("conn fetch is null");
			            return -2;
			        }
					conn.setAutoCommit(false); 
					connectionsMap.put(Thread.currentThread().getId(), conn);
					return 1;
				} catch (Exception e) {
					LogUtil.e(e);
					return -1;
				}
			}
		};
		return executor.submit(callable);
	}
	
	/**
	* Execute insert table transDetails with records interval [beg, end]
	* insertion to database will be executed in batch mode
	*/
	public static Future<Integer> execute(final TransDetailsLists transDetailsLists, final int beg, final int end) {
		Callable<Integer> callable = new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				LogUtil.d("execute; beg = " + beg + "end = " + end);
				Long threadId = Thread.currentThread().getId();
				
				Connection conn = connectionsMap.get(threadId);
				
				if (conn == null) {
                    LogUtil.e("conn get is null");
                    return 0;
                }
				
				LogUtil.d("conn is not null");
				
				
				PreparedStatement stmt = conn.prepareStatement("insert into TAB_TRANS_DETAILS values(?, ?, ?, ?, ?, ?)");
				
				for (int j = beg; j <= end; ++j) {
					try {
						//LogUtil.d("j = [" + j + "]");
						stmt.setString(1, transDetailsLists.serialno.get(j));
						stmt.setString(2, transDetailsLists.trans_date.get(j));
						stmt.setString(3, transDetailsLists.trans_time.get(j));
						stmt.setString(4, transDetailsLists.trans_amount.get(j));
						stmt.setString(5, transDetailsLists.store_id.get(j));
						stmt.setString(6, transDetailsLists.prod_id.get(j));
						stmt.addBatch();

					} catch (Exception e) {
						LogUtil.e(e);
						return 0;
					}
				}
				
				//submit batch sql
				stmt.executeBatch();
				conn.commit();
				return 1;
			}
		};
		return executor.submit(callable);
	}
	
	/**
	 * Close database connections and shutdown thread pool
	 */
	public static void close() {
		//shutdown thread pool
		executor.shutdown();
		
		for (Entry<Long, Connection> entry : connectionsMap.entrySet()) {
			//close database connection
		    DBUtil.closeConnection(entry.getValue());
		}
		
		//gc connectionsMap
		connectionsMap = null;
	}
}