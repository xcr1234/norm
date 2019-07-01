package norm.support.mybatis.page;

import norm.page.Page;

/**
 * MyBatis分页插件支持，只对mybatis框架有效
 */
public abstract class PageHelper {


    private static ThreadLocal<Page> pageThreadLocal = new ThreadLocal<Page>();



    public static Page getPage(){
        return pageThreadLocal.get();
    }

    public static Page setPage(Page page){
        pageThreadLocal.set(page);
        return page;
    }

    public static Page setPage(int pageNumber,int pageSize){
        Page page = new Page(pageNumber,pageSize);
        pageThreadLocal.set(page);
        return page;
    }

    public static Page setPage(int pageNumber){
        Page page = new Page(pageNumber);
        pageThreadLocal.set(page);
        return page;
    }

    public static void clearPage(){
        pageThreadLocal.remove();
    }

    private static int defaultPageSize = 10;
    private static boolean defaultEvalCount = true;
    private static boolean collectResult = false;
    public static synchronized int getDefaultPageSize() {
        return defaultPageSize;
    }

    private static Integer maxPageSize;

    public static Integer getMaxPageSize() {
        return maxPageSize;
    }

    public static void setMaxPageSize(Integer maxPageSize) {
        PageHelper.maxPageSize = maxPageSize;
    }

    public static synchronized void setDefaultPageSize(int defaultPageSize) {
        PageHelper.defaultPageSize = defaultPageSize;
    }

    public static synchronized boolean isDefaultEvalCount() {
        return defaultEvalCount;
    }

    public static synchronized void setDefaultEvalCount(boolean defaultEvalCount) {
        PageHelper.defaultEvalCount = defaultEvalCount;
    }

    public static synchronized boolean isCollectResult() {
        return collectResult;
    }

    public static synchronized void setCollectResult(boolean collectResult) {
        PageHelper.collectResult = collectResult;
    }


}
