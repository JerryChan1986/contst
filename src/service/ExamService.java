package service;

import java.util.List;
import java.util.Map;


public interface ExamService {
	/**
	 * 数据库测试
	 *
	 * @return
	 */
	public boolean dbtest();
	
	/**
	 * questionPerfJob结果
	 * @return
	 */
	public List<Map<String, Object>> QPerfBatch(String startDate,String endDate);
	
	/**
	 * questionPerfInqury结果
	 * @return
	 */
	public List<Map<String, Object>> QPerfInquiry(String startDate,String endDate,String discID);
	
	
	
}
