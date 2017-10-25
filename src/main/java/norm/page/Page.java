package norm.page;


import norm.support.mybatis.page.PageHelper;

import java.io.Serializable;

public class Page implements Serializable{


    private static final long serialVersionUID = -6798071663464647031L;




    /**
     * 页码，从1开始。
     */
    private int pageNumber;
    private int pageSize;
    private boolean evalCount;
    private boolean collectResult;
    private Integer total;
    private Integer pageCount;
    private transient Object result;

    public Page(){
        this(1);
    }

    public Page(int pageNumber) {
        this(pageNumber, PageHelper.getDefaultPageSize());
    }

    public Page(int pageNumber, int pageSize) {
        if(pageNumber <= 0){
            throw new IllegalArgumentException("illegal page number : " + pageNumber);
        }
        if(pageSize <= 0){
            throw new IllegalArgumentException("illegal page size:" + pageSize);
        }
        if(PageHelper.getMaxPageSize() != null && pageSize > PageHelper.getMaxPageSize()){
            throw new IllegalArgumentException("page size too much:" + pageSize);
        }
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.evalCount = PageHelper.isDefaultEvalCount();
        this.collectResult = PageHelper.isCollectResult();
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
                ", evalCount=" + evalCount +
                ", total=" + total +
                ", pageCount=" + pageCount +
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
        if(PageHelper.getMaxPageSize() != null && pageSize > PageHelper.getMaxPageSize()){
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

    public boolean isCollectResult() {
        return collectResult;
    }

    public void setCollectResult(boolean collectResult) {
        this.collectResult = collectResult;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

}