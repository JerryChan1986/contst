package utils.PagerPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.parameter.DefaultParameterHandler;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;

import utils.PagerPlugin.Page;

@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class}),  
@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})})  
public class PageHelper implements Interceptor {

	   public static final ThreadLocal<Page> localPage = new ThreadLocal<Page>();  
	   
	    /** 
	     * 开始分页 
	     * @param pageNum 当前页数
	     * @param pageSize 每页记录数
	     */  
	    public static void startPage(int pageNum, int pageSize) {  
	        localPage.set(new Page(pageNum, pageSize));  
	    }  
	  
	    /** 
	     * 结束分页并返回结果，该方法必须被调用，否则localPage会一直保存下去，直到下一次startPage 
	     * @return 
	     */  
	    public static Page endPage() {  
	        Page page = localPage.get();  
	        localPage.remove();  
	        return page;  
	    }
	
	public Object intercept(Invocation invocation) throws Throwable {  
        if (localPage.get() == null) {  
            return invocation.proceed();  
        }  
        if (invocation.getTarget() instanceof StatementHandler) {  
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();  
            MetaObject metaStatementHandler = MetaObject.forObject(statementHandler);   
            while (metaStatementHandler.hasGetter("h")) {  
                Object object = metaStatementHandler.getValue("h");  
                metaStatementHandler = MetaObject.forObject(object);  
            }  
            while (metaStatementHandler.hasGetter("target")) {  
                Object object = metaStatementHandler.getValue("target");  
                metaStatementHandler = MetaObject.forObject(object);  
            }  
            MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");  
            //分页信息if (localPage.get() != null) {  
            Page page = localPage.get();  
            BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");  
            // 分页参数作为参数对象parameterObject的一个属性  
            String sql = boundSql.getSql();  
            // 重写sql  
            String pageSql = buildPageSql(sql, page);  
            //重写分页sql  
            metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);  
            Connection connection = (Connection) invocation.getArgs()[0];  
            // 重设分页参数里的总页数等  
            setPageParameter(sql, connection, mappedStatement, boundSql, page);  
            // 将执行权交给下一个拦截器  
            return invocation.proceed();  
        } else if (invocation.getTarget() instanceof ResultSetHandler) {  
            Object result = invocation.proceed();  
            Page page = localPage.get();  
            page.setResult((List) result);  
            return result;  
        }  
        return null;  
	}

	public Object plugin(Object target) {
		if (target instanceof StatementHandler || target instanceof ResultSetHandler) {  
            return Plugin.wrap(target, this);  
        } else {  
            return target;  
        }  
	}

	public void setProperties(Properties arg0) {
	}

	
	private String buildPageSql(String sql, Page page) {  
        StringBuilder pageSql = new StringBuilder(200);  
        pageSql.append(sql);  
        pageSql.append(" limit "+page.getStartRow()+","+(page.getEndRow()-page.getStartRow()));  
        //System.out.println(pageSql);
        return pageSql.toString();  
    }  
	
	private void setPageParameter(String sql, Connection connection, MappedStatement mappedStatement,  
            BoundSql boundSql, Page page) {  
		// 记录总记录数  
		String countSql = "select count(0) from (" + sql + ") pageCount";  
		PreparedStatement countStmt = null;  
		ResultSet rs = null;  
		try {  
			countStmt = connection.prepareStatement(countSql);  
			BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,  
					boundSql.getParameterMappings(), boundSql.getParameterObject());  
			setParameters(countStmt, mappedStatement, countBS, boundSql.getParameterObject());  
			rs = countStmt.executeQuery();  
			int totalCount = 0;  
			if (rs.next()) {  
				totalCount = rs.getInt(1);  
			}  
			page.setTotal(totalCount);  
			int totalPage = totalCount / page.getPageSize() + ((totalCount % page.getPageSize() == 0) ? 0 : 1);  
			page.setPages(totalPage);  
		} catch (Exception e) {  
			e.printStackTrace();
		} finally {  
			try {  
					rs.close();  
				} catch (Exception e) {  
					e.printStackTrace();
				}  
			try {  
					countStmt.close();  
				} catch (Exception e) {  
				   e.printStackTrace();
				}  
		}  
	}  
	
	private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,  
	        Object parameterObject) throws Exception {  
		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);  
		parameterHandler.setParameters(ps);  
	}  
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
