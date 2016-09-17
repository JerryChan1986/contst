package utils.PagerPlugin;

import javax.servlet.http.HttpServletRequest;

import utils.PagerPlugin.Page;

public class PageUtil {
	
	/**
	 * 构造分页组件
	 * @param request
	 * @return
	 */
    public static Page getPage(HttpServletRequest request){
    	String pageNum=request.getParameter("pageNum");//当前页号
        String pageSize=request.getParameter("pageSize");//当前页记录数
        if(pageNum==null){
        	if(pageSize==null){
        		return new Page();//默认
        	}else{
        		int pageSize_int=Integer.parseInt(pageSize);
        		return new Page(0,pageSize_int);
        	}
        }else{
        	int pageNum_int=Integer.parseInt(pageNum);
        	if(pageSize==null){
        		return new Page(pageNum_int,20);//默认
        	}else{
        		int pageSize_int=Integer.parseInt(pageSize);
        		return new Page(pageNum_int,pageSize_int);
        	}
        }
    }
    
    /**
     * 生成分页html。
     * @param page 分页结果集
     * @return
     */
    public static String generatePageFooterHtml(Page page){
    	/**
    	 * <div class="row">
				       <div class="col-sm-8" style="padding-top:5px;">
				           	当前显示：1-20，共6条记录。 每页显示
				           	<select  id="page" style="">
							     <option value="20">20条</option>
							     <option value="50">50条</option>
							     <option value="100">100条</option>
							     <option value="all">全部数据</option>
							 </select>
					   </div>
				       <div class="col-sm-4">
					       	<div style="float:right">
						       	<ul class="pager" style="margin:0px;">
								  <li><a href="#">上一页</a></li>
								  <li class="disabled"><a href="#">下一页</a></li>
								</ul>
							</div>
				       </div>
			</div>
    	 */
    	StringBuffer sb=new StringBuffer();
    	sb.append("<div class='row'>");
	    	sb.append("<div class='col-sm-8' style='padding-top:5px;'>");
		    	sb.append("当前显示："+(page.getStartRow()+1)+"-"+page.getEndRow()+"，共"+page.getTotal()+"条记录。 每页显示");
		    	sb.append("<select  id='pageSize' name='pageSize' style=''>");

		    	if(page.getPageSize()==20){
		    		sb.append("<option value='20' selected >20条</option>");
		    	}else{
		    		sb.append("<option value='20' >20条</option>");
		    	}
		    	if(page.getPageSize()==50){
		    		sb.append("<option value='50' selected >50条</option>");
		    	}else{
		    		sb.append("<option value='50' >50条</option>");
		    	}
		    	
		    	if(page.getPageSize()==100){
		    		sb.append("<option value='100' selected >100条</option>");
		    	}else{
		    		sb.append("<option value='100' >100条</option>");
		    	}
		    	
		    	sb.append("</select>");
		    sb.append("</div>");
	    	sb.append("<div class='col-sm-4'>");
	    		sb.append("<div style='float:right'>");
	    			sb.append("<ul class='pager' style='margin:0px;'>");
	    			    if(page.getPages()==1||page.getPageNum()==0){
	    			    	sb.append("<li class='disabled'><a href='javascript:void(0);' id='prePage'>上一页</a></li>");
	    			    }else{
	    			    	sb.append("<li><a href='javascript:void(0);' id='prePage'>上一页</a></li>");
	    			    }
	    			    if(page.getPages()==1||page.getPageNum()==(page.getPages()-1)){
	    			    	sb.append("<li class='disabled'><a href='javascript:void(0);' id='nextPage'>下一页</a></li>");
	    			    }else{
	    			    	sb.append("<li><a href='javascript:void(0);' id='nextPage'>下一页</a></li>");
	    			    }
	    			sb.append("</ul>");
	    		sb.append("</div>");
	    	sb.append("</div>");
    	sb.append("</div>");
    	
    	return sb.toString();
    }
}  
