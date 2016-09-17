package controller;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import service.ExamService;



/**
 *  考试题目逻辑
 * @author Administrator
 *
 */
@Controller
@RequestMapping ("/exam")
public class ExamController
{
	private ExamService examService;
	
	@Autowired
	public void setExamService(ExamService examService) {
		this.examService = examService;
	}
	
	  /**
     * 试题QPerfBatch 性能调优批量题目
     * @param request
     * @param response
     * @return
     */
	@RequestMapping ("/QPerfBatch")
	public ModelAndView QPerfBatch(HttpServletRequest request,HttpServletResponse response)
	{   
		
		String startDate=request.getParameter("startDate");
		String endDate=request.getParameter("endDate");
		
		ModelAndView map = new ModelAndView();
		map.addObject("startDate", startDate);
		map.addObject("endDate", endDate);
		map.addObject("data_batch", examService.QPerfBatch(startDate,endDate));
		map.setViewName("QPerfBatch");//跳转至QPerfBatch.jsp
		return map ;
	}
	
	 /**
     * 试题QPerfInquiry 性能调优联机交易
     * @param request
     * @param response
     * @return
     */
	@RequestMapping ("/QPerfInquiry")
	public ModelAndView QPerfInquiry(HttpServletRequest request,HttpServletResponse response)
	{   
		
		String startMonth=request.getParameter("startMonth");
		String endMonth=request.getParameter("endMonth");
		String discID=request.getParameter("discID");
		
		ModelAndView map = new ModelAndView();
		map.addObject("startMonth", startMonth);
		map.addObject("endMonth", endMonth);
		map.addObject("discID", discID);
		map.addObject("data_inquiry", examService.QPerfInquiry(startMonth,endMonth,discID));
		map.setViewName("QPerfInquiry");//跳转至QPerfInquiry.jsp
		return map ;
	}
	
	
	
}
