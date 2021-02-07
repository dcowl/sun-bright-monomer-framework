package org.sun.bright.framework.page;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

public final class PageInfo<T> implements Serializable {

    private static final long serialVersionUID = -3948758643046103961L;

    /**
     * 当前页码
     */
    private int curPage;
    /**
     * 一页显示的记录数
     */
    private int pageSize;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 记录总数
     */
    private int totalNum;
    /**
     * 开始页
     */
    private int beginPage = 1;
    /**
     * 结束页
     */
    private int endPage = 1;
    /**
     * 起始行数
     */
    private int startIndex;
    /**
     * 结束行数
     */
    private int lastIndex;
    /**
     * 结果集存放List
     */
    private List<T> recList;


    public PageInfo() {
    }

    public PageInfo(int curPage, int pageSize, int totalNum) {
        this.curPage = curPage;
        this.pageSize = pageSize;
        this.totalNum = totalNum;
        calPageParam();
    }

    /**
     * 计算结束时候的索引
     */
    public void calLastIndex() {
        if (totalNum < pageSize) {
            this.lastIndex = totalNum;
        } else if ((totalNum % pageSize == 0) || (totalNum % pageSize != 0 && curPage < getTotalPage())) {
            this.lastIndex = curPage * pageSize;
        } else if (totalNum % pageSize != 0 && curPage == getTotalPage()) { //最后一页
            this.lastIndex = totalNum;
        }
    }

    public void calPageParam() {
        calTotalPages();
        calStartIndex();
        calLastIndex();
    }

    /**
     * 提取分页对象信息
     */
    public void convert(Page<T> page) {
        this.setRecList(page.getContent());
        this.setCurPage(page.getPageable().getPageNumber() + 1);
        this.setPageSize(page.getPageable().getPageSize());
        this.setTotalPage(page.getTotalPages());
        this.setTotalNum((int) page.getTotalElements());
    }

    /**
     * 计算总页数
     */
    public void calTotalPages() {
        if (totalNum % pageSize == 0) {
            this.setTotalPage(totalNum / pageSize);
        } else {
            this.setTotalPage((totalNum / pageSize) + 1);
        }
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public List<T> getRecList() {
        return recList;
    }

    public void setRecList(List<T> recList) {
        this.recList = recList;
    }

    public void calStartIndex() {
        this.startIndex = (curPage - 1) * pageSize;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public int getBeginPage() {
        return beginPage;
    }

    public void setBeginPage(int beginPage) {
        this.beginPage = beginPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }
}
