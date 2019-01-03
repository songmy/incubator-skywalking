package org.apache.skywalking.oap.server.custommodule.storage.query;

import java.io.Serializable;

public class PageInfo implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = -164216038934318113L;
    private int count = -1;        //总记录数
    private int pageSize = 10;    //每页显示记录数
    private int pageNo = 1;        //当前页码数
    private int pageCount = -1;    //页数
    private int startIndex = 0;  //查询起始位置


    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public PageInfo() {
    }

    public PageInfo(int pageSize, int pageNo) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.startIndex = pageSize * (pageNo - 1);
    }

    public int getCount() {
        return count;
    }


    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
        if (this.pageNo < 0)
            this.pageNo = 1;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        if (this.pageSize < 0)
            this.pageSize = 10;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setCount(int count) {
        this.count = count;
        if (this.count % pageSize == 0) { // 计算页数
            this.pageCount = this.count / pageSize;
        } else {
            this.pageCount = this.count / pageSize + 1;
        }
    }

}
