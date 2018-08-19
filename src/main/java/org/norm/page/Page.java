package org.norm.page;



import java.io.Serializable;

public class Page implements Serializable{


    private static final long serialVersionUID = -6798071663464647031L;

    private static final int defaultPageSize = 10;
    private static final int maxPageSize = 500;
    private static final boolean defaultEvalCount = true;


    public static int getDefaultPageSize() {
        return defaultPageSize;
    }

    public static int getMaxPageSize() {
        return maxPageSize;
    }

    public static boolean isDefaultEvalCount() {
        return defaultEvalCount;
    }

    /**
     * 页码，从1开始。
     */
    private int pageNumber;
    private int pageSize;
    private boolean evalCount;
    private Integer total;
    private Integer pageCount;

    public Page(){
        this(1,defaultPageSize);
    }

    public Page(int pageNumber) {
        this(pageNumber, defaultPageSize);
    }

    public Page(int pageNumber, int pageSize) {
        if(pageNumber <= 0){
            throw new IllegalArgumentException("illegal page number : " + pageNumber);
        }
        if(pageSize <= 0){
            throw new IllegalArgumentException("illegal page size:" + pageSize);
        }
        if(maxPageSize > 0 && pageSize > maxPageSize){
            throw new IllegalArgumentException("page size too much:" + pageSize);
        }
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.evalCount = defaultEvalCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Page page = (Page) o;

        if (pageNumber != page.pageNumber) return false;
        if (pageSize != page.pageSize) return false;
        if (evalCount != page.evalCount) return false;
        if (total != page.total) return false;
        return pageCount == page.pageCount;
    }

    @Override
    public int hashCode() {
        int result = pageNumber;
        result = 31 * result + pageSize;
        result = 31 * result + (evalCount ? 1 : 0);
        result = 31 * result + total;
        result = 31 * result + pageCount;
        return result;
    }


    @Override
    public String toString() {
        return "Page{" +
                "pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", total=" + total +
                '}';
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        if(pageNumber <= 0){
            throw new IllegalArgumentException("illegal page number:" + pageNumber);
        }
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if(pageSize <= 0){
            throw new IllegalArgumentException("illegal page size:" + pageSize);
        }
        if(maxPageSize > 0 && pageSize > maxPageSize){
            throw new IllegalArgumentException("page size too much:" + pageSize);
        }
        this.pageSize = pageSize;
    }

    public int limit() {
        return pageSize;
    }


    public int offset() {
        return (pageNumber - 1) * pageSize;
    }


    public int from() {
        return (pageNumber - 1) * pageSize;
    }


    public int to() {
        return pageNumber * pageSize;
    }

    public boolean isEvalCount() {
        return evalCount;
    }

    public void setEvalCount(boolean evalCount) {
        this.evalCount = evalCount;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

}