package utils.PagerPlugin;

import java.util.List;

public class Page {
    /**
     * 当前页号，默认0，分页前需设置此值
     */
	private int pageNum;  
	/**
     * 当前页显示记录数，默认20，分页前需设置此值
     */
    private int pageSize;  
    private int startRow;  
    private int endRow; 
    /**
     * 记录总数，用于界面展示
     */
    private long total;  
    /**
     * 总页数，用于界面展示
     */
    private int pages; 
    /**
     * 分页结果集，用于界面展示
     */
    private List result;  

    public Page() {  
    	this.pageNum = 0;  
        this.pageSize = 20;  
        this.startRow = 0;  
        this.endRow = 20;  
    }
    
    public Page(int pageNum, int pageSize) {  
        this.pageNum = pageNum;  
        this.pageSize = pageSize;  
        this.startRow = pageNum > 0 ? (pageNum ) * pageSize : 0;  
        this.endRow = this.startRow+pageSize;  
    }  
    /**
     * 获取分页后的结果集，用于界面展示
     * @return
     */
    public List getResult() {  
        return result;  
    }  

    public void setResult(List result) {  
        this.result = result;  
    }  
    /**
     * 获取总页数，用于界面展示
     * @return
     */
    public int getPages() {  
        return pages;  
    }  

    public void setPages(int pages) {  
        this.pages = pages;  
    }  

    public int getEndRow() {  
        return endRow;  
    }  

    public void setEndRow(int endRow) {  
        this.endRow = endRow;  
    }  

    public int getPageNum() {  
        return pageNum;  
    }  

    public void setPageNum(int pageNum) {  
        this.pageNum = pageNum;  
    }  

    public int getPageSize() {  
        return pageSize;  
    }  

    public void setPageSize(int pageSize) {  
        this.pageSize = pageSize;  
    }  

    public int getStartRow() {  
        return startRow;  
    }  

    public void setStartRow(int startRow) {  
        this.startRow = startRow;  
    }  

    /**
     * 获取总记录数，用于界面展示
     * @return
     */
    public long getTotal() {  
        return total;  
    }  

    public void setTotal(long total) {  
        this.total = total;  
    }  

    @Override  
    public String toString() {  
        return "Page{" +  
                "pageNum=" + pageNum +  
                ", pageSize=" + pageSize +  
                ", startRow=" + startRow +  
                ", endRow=" + endRow +  
                ", total=" + total +  
                ", pages=" + pages +  
                '}';  
    }  
}  
