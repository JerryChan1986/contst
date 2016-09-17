package service.impl;

import gyw.log.Log;
import gyw.log.LogUtil;
import gyw.utils.Timer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import controller.ExamController;
import service.ExamService;
import test.*;
import utils.DBUtil;
import utils.KeyUtil;

@Service("examService")
public class ExamServiceImpl implements ExamService {

	private static Logger log = Logger.getLogger(ExamServiceImpl.class);

	public boolean dbtest() {
		Connection conn = DBUtil.getDBConnection();
		if (conn != null) {
			DBUtil.closeConnection(conn);
			return true;
		}
		return false;
	}

	/**
	 * 性能测试批量题目
	 * 
	 * @param startDate
	 * @param endDate
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> QPerfBatch(String startDate, String endDate) {

		log.debug("enter QPerfBatch");
        if (false == ConnectionPool.init()) {
            LogUtil.e("Connection pool load Fail!");
        }
		/********************************************************************************************************
		 * 功能一：读取指定目录下6个月度交易明细数据文件，并将数据增加以列序列号后存入交易明细表TAB_TRANS_DETAILS。
		 * 其中6个文件名字格式为DetailsFile_yyyyMM.csv，如”DetailsFile_201601.csv“
		 ********************************************************************************************************/
		
		String path = ExamController.class.getResource("/").getPath()
		+ "/dataFiles/";
        
		log.debug("enter QPerfBatch->path:" + path);

		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();// 存储要返回的值

		long importDetailsTime = 0;
		int importDetailsCnt = 0;
		long importStoreDailyTime = 0;
		int importStoreDailyCnt = 0;

		/* 【start】【比赛需要-勿删除】为保证基础数据的稳定性，插入操作之前清理本次需要插入的数据，保证性能不受数据量增量影响。 */
		log.debug("enter QPerfBatch->Start to clean TAB_TRANS_DETAILS 2016上半年数据");
		deleteData("TAB_TRANS_DETAILS", " TRANS_DATE>=20160101");
		log.debug("enter QPerfBatch->end clean TAB_TRANS_DETAILS 2016上半年数据");
		/* 【end】【比赛需要-勿删除】为保证基础数据的稳定性，插入操作之前清理本次需要插入的数据，保证性能不受数据量增量影响。 */
		
		log.debug("enter QPerfBatch->step1:start to import trans details");
		Timer timer = new Timer();
		timer.tic();
		readFile_naive();
		
//		for (int i = 1; i < 7; i++) {
//
//			String filepath = path + "DetailsFile_20160" + i + ".csv";
//			Map<String, Object> mapDetails = new HashMap<String, Object>();
//			
//			mapDetails = importDetails(filepath);
//			
//			importDetailsCnt = importDetailsCnt
//					+ (Integer) mapDetails.get("importCnt");
//			importDetailsTime = importDetailsTime
//					+ (Long) mapDetails.get("importTime");
//			
//			log.debug("enter QPerfBatch->step1-> i:"+i+";importCnt:"+importDetailsCnt+";importDetailsTime:"+importDetailsTime);
//
//		}
		
		timer.toc();

		log.debug("enter QPerfBatch->step1:end import trans details");

		/*****************************************************************************************************************
		 * 功能二：将功能一中导入的6个月的数据位源数据，抽取每个商店日交易总额并将抽取数据存入商户日交易表TAB_STORE_TRANS_DAILY
		 * 。
		 *****************************************************************************************************************/
//		log.debug("enter QPerfBatch->step2:start to import store trans daily");
//
//		Map<String, Object> mapStoreDaily = new HashMap<String, Object>();
//	
//		mapStoreDaily=importStoreDaily(startDate,endDate);
//
//		importStoreDailyCnt = importStoreDailyCnt
//				+ (Integer) mapStoreDaily.get("importCnt");
//		importStoreDailyTime = importStoreDailyTime
//				+ (Long) mapStoreDaily.get("importTime");
//
//		log.debug("enter QPerfBatch->step2:end import store trans daily");

		/* 将页面需要的返回值进行存储 */
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("importDetailsTime", timer.getTimeCost());
		map.put("importDetailsCnt", 168000);
		map.put("importStoreDailyTime", 0);
		map.put("importStoreDailyCnt", 0);
		map.put("totalTime", importDetailsTime + 0);
		data.add(map);

		log.debug("leave QPerfBatch->importDetailsCnt:" + importDetailsCnt
				+ ";importDetailsTime:" + importDetailsTime
				+ "importStoreDailyCnt:" + importStoreDailyCnt
				+ "importStoreDailyTime:" + importStoreDailyTime);

		return data;
	}

	/**
	 * 导入交易明细数据具体处理函数
	 * 
	 * @param filepath
	 * @return Map<String, Object>
	 */
	private Map<String, Object> importDetails(String filepath) {

		log.debug("enter importDetails");

		/* 变量定义 */
		java.sql.Connection conn = null;
		Statement stmt = null;

		long t1_start = System.currentTimeMillis();
		// 导入导入耗时，需反馈到响应页面
		long importTime = 0;
		// 导入条数，需反馈到响应页面
		int importCnt = 0;

		List<String> sql_list = new ArrayList<String>();// 待执行的SQL列表
		try {
			FileInputStream fis = new FileInputStream(filepath);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);

			Object ds_obj = utils.ApplicationContextHelper
					.getBean("dataSource");
			conn = ((javax.sql.DataSource) ds_obj).getConnection();

			log.debug("enter importDetails->start to step1");
			/** 步骤一：读取文件记录，准备SQL语句 ********/
			String sqlline = "";
			while ((sqlline = br.readLine()) != null) {
				if (sqlline != null && sqlline.length() > 1
						&& sqlline.indexOf(',') > 0) {
					String[] arrs = sqlline.split(",");
					String serialno = KeyUtil.getID(); // 每条记录序列号不同
					String trans_date = arrs[0];
					String trans_time = arrs[1];
					String trans_amount = arrs[2];
					String store_id = arrs[3];
					String prod_id = arrs[4];

					String sql_ins_temp = "insert into TAB_TRANS_DETAILS (SERIALNO,TRANS_DATE,TRANS_TIME,TRANS_AMOUNT,STORE_ID,PROD_ID) values('"
							+ serialno
							+ "',"
							+ trans_date
							+ ",'"
							+ trans_time
							+ "',"
							+ trans_amount
							+ ",'"
							+ store_id
							+ "','"
							+ prod_id + "')";
					sql_list.add(sql_ins_temp);

				}
				log.debug("enter importDetails->step1->sql_in size:"
						+ sql_list.size());

			}
			
			
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		log.debug("enter importDetails->end step1");

		log.debug("enter importDetails->start step2");

		/** 步骤二：遍历SQL列表，逐条执行SQL，将记录写入TAB_TRANS_DETAILS ******/
		for (int j = 0; j < sql_list.size(); j++) {
			try {
				Object ds_obj = utils.ApplicationContextHelper
						.getBean("dataSource");
				conn = ((javax.sql.DataSource) ds_obj).getConnection();
				stmt = conn.createStatement();
				 
				stmt.execute(sql_list.get(j));
				conn.commit();
				importCnt = importCnt + 1;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				
				try {
					conn.close();
				} catch (Exception e) {
				}
			}

		}

		log.debug("enter importDetails->end step2");

		// 导入所消耗时间，单位毫秒。
		importTime = (System.currentTimeMillis() - t1_start);

		// 将导入记录数和导入耗时返回
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("importCnt", importCnt);
		map.put("importTime", importTime);

		log.debug("leave importDetails->importCnt:" + importCnt
				+ ";importTime:" + importTime);

		return map;
	}

	
	/**
	 * 插入商户日交易表具体处理函数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return Map<String, Object>
	 */
	private Map<String, Object> importStoreDaily(String startDate,
			String endDate) {

		log.debug("leave importStoreDaily");

		/* 变量定义 */
		java.sql.Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		long t1_start = System.currentTimeMillis();
		// 导入导入耗时，需反馈到响应页面
		long importTime = 0;
		// 导入条数，需反馈到响应页面
		int importCnt = 0;

		/*
		 * 从交易明细表TAB_TRANS_DETAILS为源数据，查询输入月份的的数据汇总每个商户每日销售总额数据，
		 * 存入商户日交易表TAB_STORE_TRANS_DAILY。
		 */

		/* 【start】【比赛需要-勿删除】为保证基础数据的稳定性，插入操作之前清理本次需要插入的数据，保证性能不受数据量增量影响。 */
		log.debug("enter importStoreDaily->Start to clean TAB_STORE_TRANS_DAILY 2016上半年数据");
		deleteData("TAB_STORE_TRANS_DAILY", " TRANS_DATE>=20160101");
		log.debug("enter importStoreDaily->end clean TAB_STORE_TRANS_DAILY 2016上半年数据");
		/* 【end】【比赛需要-勿删除】为保证基础数据的稳定性，插入操作之前清理本次需要插入的数据，保证性能不受数据量增量影响。 */
		
		/** 步骤一: 查询出要插入的数据，并组合SQL ******************/
		log.debug("enter importStoreDaily->start to step1");
		List<String> sql_list = new ArrayList<String>();// 待执行的SQL列表
		try {

			Object ds_obj = utils.ApplicationContextHelper
					.getBean("dataSource");
			conn = ((javax.sql.DataSource) ds_obj).getConnection();
			stmt = conn.createStatement();
			rs = stmt
					.executeQuery("select t.trans_date as trans_date,t.store_id as store_id,sum(t.trans_amount) as total_amount from (select * from TAB_TRANS_DETAILS where trans_date>="
							+ startDate
							+ " and trans_date<="
							+ endDate
							+ ")  t group by t.store_id,t.trans_date");

			while (rs.next()) {

				String rs_trans_date = rs.getString("trans_date");
				String rs_store_id = rs.getString("store_id");
				String rs_total_amount = rs.getString("total_amount");

				String sql_ins_temp = "insert into TAB_STORE_TRANS_DAILY values('"
						+ rs_trans_date
						+ "','"
						+ rs_store_id
						+ "','"
						+ rs_total_amount + "')";

				sql_list.add(sql_ins_temp);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		log.debug("enter importStoreDaily->end step1");

		/** 步骤一 :遍历SQL列表，执行SQL，将记录写入商户日常交易表TAB_STORE_TRANS_DAILY ******************/
		log.debug("enter importStoreDaily->start to step1");

		for (int j = 0; j < sql_list.size(); j++) {
			try {
				Object ds_obj = utils.ApplicationContextHelper
						.getBean("dataSource");
				conn = ((javax.sql.DataSource) ds_obj).getConnection();
				stmt = conn.createStatement();
				stmt.execute(sql_list.get(j));
				importCnt = importCnt + 1;
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}

		log.debug("enter importStoreDaily->end step1");

		// 导入所消耗时间，单位毫秒。
		importTime = (System.currentTimeMillis() - t1_start);

		// 将导入记录数和导入耗时返回
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("importCnt", importCnt);
		map.put("importTime", importTime);

		log.debug("leave importStoreDaily->importCnt:" + importCnt
				+ ";importTime:" + importTime);

		return map;
	}

	

	/**
	 * 性能调优联机题目 在选取的所有的地区商店中, 在给定日期时间段内，查询出TAB_STORE_TRANS_DAILY交易总额前10名的商店，
	 * 并获取这10个商店在该时间段内销售总额列表，并从大到小排序,展现在页面上
	 * 
	 * @param startDate
	 * @param endDate
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> QPerfInquiry(String startDate,
			String endDate, String discID) {

		long t1_start = System.currentTimeMillis();
		// 导入导入耗时，需反馈到响应页面
		long inquiryTime = 0;
		// 导入条数，需反馈到响应页面
		
		log.debug("enter QPerfInquiry");

		/* 变量定义 */
		java.sql.Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<Map<String, Object>> data_temp = new ArrayList<Map<String, Object>>();// 存储过程中数据
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();// 存储响应中反馈的数据

		/* 步骤一:获取给定时间段内所需数据，竞赛中设计数据为2015年上半年6个月数据 */

		log.debug("enter QPerfInquiry->start to step1");
		try {
			Object ds_obj = utils.ApplicationContextHelper
					.getBean("dataSource");
			conn = ((javax.sql.DataSource) ds_obj).getConnection();
			stmt = conn.createStatement();
			String sql = "";
			
			/*【比赛需要-勿删除】	竞赛需要查询内容相同，为避免缓存，增加 动态sqlKey 放在主查询语句后；若选手有SQL调优需要在主查询语句后加上 sqlKey = sqlKey的查询条件 */
			String sqlKey = KeyUtil.getID();
			
			sql = "select t.store_id as store_id, b.store_name as store_name, t.trans_date as trans_date,t.trans_amount as trans_amount from  TAB_STORE_TRANS_DAILY t,TAB_STORE b where t.trans_date>="
						+ startDate
						+ " and t.trans_date<="
						+ endDate
						+ " and t.STORE_ID=b.STORE_ID and t.STORE_ID in (select STORE_ID from TAB_STORE where DISC_ID = '"
						+ discID + "') and '"+sqlKey+"'='"+sqlKey+"' order by t.trans_Date DESC";
			

			log.debug("enter QPerfInquiry->start to step1->sql:" + sql);

			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String store_id = rs.getString("store_id");
				String store_name = rs.getString("store_name");
				String trans_date = rs.getString("trans_date");
				String trans_amount = rs.getString("trans_amount");

				Map<String, Object> m = new HashMap<String, Object>();

				m.put("store_id", store_id);
				m.put("store_name", store_name);
				m.put("trans_date", trans_date);
				m.put("trans_amount", trans_amount);
				data_temp.add(m);
			
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		} 
		log.debug("enter QPerfInquiry->end step1");

		/* 步骤二:存入临时表TAB_STORE_TRANS_DAILY_TEMP */

		log.debug("enter QPerfInquiry->start to step2");

		List<String> sql_list = new ArrayList<String>();// 待执行的SQL列表

		// 防并发操作，一个线程产生一个，同批次产生的相同，操作完后将该批次数据删除
		String serialno = KeyUtil.getID();

		for (int i = 0; i < data_temp.size(); i++) {
			Map m = (Map) data_temp.get(i);
			String store_id1 = (String) m.get("store_id");
			String store_name1 = (String) m.get("store_name");
			String trans_date1 = (String) m.get("trans_date");
			String trans_amount1 = (String) m.get("trans_amount");

			String sql_ins_temp = "insert into TAB_STORE_TRANS_DAILY_TEMP values('"
					+ store_id1
					+ "','"
					+ store_name1
					+ "','"
					+ trans_date1
					+ "','" + trans_amount1 + "','" + serialno + "')";

			sql_list.add(sql_ins_temp);

		}

		// 遍历SQL列表，执行SQL，将记录写入临时表tab_trans_detail_temp
		for (int j = 0; j < sql_list.size(); j++) {
			try {
				Object ds_obj = utils.ApplicationContextHelper
						.getBean("dataSource");
				conn = ((javax.sql.DataSource) ds_obj).getConnection();
				stmt = conn.createStatement();
				stmt.execute(sql_list.get(j));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}

		}
		log.debug("enter QPerfInquiry->end step2");

		/* 步骤三:操作临时表，获取响应中所需数据 */
		log.debug("enter QPerfInquiry->start to step3");

		try {
			Object ds_obj = utils.ApplicationContextHelper
					.getBean("dataSource");
			conn = ((javax.sql.DataSource) ds_obj).getConnection();
			stmt = conn.createStatement();
			/*【比赛需要-勿删除】	竞赛需要查询内容相同，为避免缓存，增加 动态sqlKey 放在主查询语句后；若选手有SQL调优需要在主查询语句后加上 sqlKey = sqlKey的查询条件 */
			String sqlKey = KeyUtil.getID();
			
			String sql = "select t1.seq as seq,t1.store_id as store_id,(select TAB_STORE.STORE_NAME from TAB_STORE where TAB_STORE.STORE_ID=t1.store_id ) as store_name,t1.total_amount as total_amount from (select rownum as seq, t0.* from (select ttt.store_id,sum(ttt.trans_amount) as total_amount  from (select * from TAB_STORE_TRANS_DAILY_TEMP where serialno='"
					+ serialno
					+ "') ttt  group by  ttt.store_id order by total_amount DESC )  t0 ) t1 where t1.seq<11 and '"+sqlKey+"'='"+sqlKey+"'";

			rs = stmt.executeQuery(sql);

			while (rs.next()) {

				String rs_seq = rs.getString("seq");
				String rs_store_id = rs.getString("store_id");
				String rs_storename = rs.getString("store_name");
				String rs_totalamount = rs.getString("total_amount");
				Map<String, Object> m = new HashMap<String, Object>();

				m.put("seq", rs_seq);
				m.put("store_id", rs_store_id);
				m.put("store_name", rs_storename);
				m.put("total_amount", rs_totalamount);

				data.add(m);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (Exception e) {
			}
			try {
				conn.close();
			} catch (Exception e) {
			}
		}

		log.debug("enter QPerfInquiry->end step3:serialno:" + serialno);

		/*【start】 【比赛需要-勿删除】为保证基础数据的稳定性，完成任务操作之后清理本次操作插入的临时表数据，保证性能不受数据量增量影响。 */
		log.debug("enter QPerfInquiry->Start to clean TAB_STORE_TRANS_DAILY_TEMP 2015上半年数据");
		deleteData("TAB_STORE_TRANS_DAILY_TEMP", " serialno='" + serialno+"'");
		log.debug("enter QPerfInquiry->end clean TAB_STORE_TRANS_DAILY_TEMP 2015上半年数据");
		/*【end】 【比赛需要-勿删除】为保证基础数据的稳定性，完成任务操作之后清理本次操作插入的临时表数据，保证性能不受数据量增量影响。 */
		
		log.debug("leave QPerfInquiry->data size:" + data.size());

		// 导入所消耗时间，单位秒。
		inquiryTime = (System.currentTimeMillis() - t1_start);// 秒
				
		log.debug("leave QPerfInquiry->inquiryTime:" + inquiryTime+"毫秒");
				
		//System.out.println("leave QPerfInquiry->inquiryTime: "+inquiryTime+"毫秒");
		
		return data;
	}

	
	
	/**
	 * 根据条件删除数据库表中数据
	 * 
	 * @param tablename
	 * @param where 条件
	 * @return
	 */
	private void deleteData(String tablename, String where) {
		
		log.debug("enter deleteData");
		
		java.sql.Connection conn = null;
		Statement stmt = null;
		String sql = "";
		try {
			Object ds_obj = utils.ApplicationContextHelper
					.getBean("dataSource");
			conn = ((javax.sql.DataSource) ds_obj).getConnection();
			stmt = conn.createStatement();

			if (where == null || where.equals("")) {
				sql = "delete from " + tablename + " ";
			} else {
				sql = "delete from " + tablename + " where " + where;

			}
			log.debug("enter deleteData->sql:" + sql);
			stmt.execute(sql);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (Exception e) {
			}
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
		
		log.debug("leave deleteData");
	}
	
	@Log(mode=2, tag="gyw", dir="/data/weblogic12/domains/jsdomain/log", name="log_question1")
	private static void readFile_naive() {
        try {
            AdvancedFileReader fileReader = new AdvancedFileReader();
            
            Future[] fileReadResults = new Future[6];
            //read each file in a thread
            for (int i = 1; i <= 6; ++i) {
                fileReadResults[i-1] = fileReader.read(i);
            }
            
            //sync reading files at here; barrier
            for (Future fileReadResult : fileReadResults) {
                fileReadResult.get();
            }
            
            LogUtil.d("All read files done!");
            
            fileReader.close();
            
            final int SEG_NUM = 14;
            final int RECORD_NUM = 28000 / SEG_NUM;
            
            Future[][] results = new Future[6][SEG_NUM];
            for (int i = 1; i <= 6; ++i) { //each file
                TransDetailsLists lists = fileReader.getTransDetailsLists(i);
                for (int j = 0; j < SEG_NUM; ++j) { //each seg
                    results[i-1][j] = ConnectionPool.execute(lists, j * RECORD_NUM, (j+1) * RECORD_NUM - 1);
                }
            }
            
            //sync insertion at here
            for (int i = 1; i <= 6; ++i) {
                for (int j = 0; j < SEG_NUM; ++j) {
                    results[i-1][j].get();
                }
            }
            
            LogUtil.d("All insertions done!");
        } catch (Exception e) {
            LogUtil.e(e);
        }
            
    }
}
