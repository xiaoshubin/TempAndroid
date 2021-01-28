package com.smallcake.temp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Date: 2020/9/9
 * author: SmallCake
 */
public class WorkListResponse implements Serializable {

    /**
     * banners : []
     * items : [{"id":2,"userId":2,"typeId":7,"address":"湖北省武汉市汉阳区建龙大道社区挨着轻轨站","startDate":"2020-09-12","endDate":"2020-09-15","day":3,"price":320,"settleType":1,"advanceType":1,"num":3,"desc":"诚意招工，要求踏实肯干，有意者直接报名","isSafe":1,"status":1,"isFav":false,"isJoin":false,"publisher":"柱子","division":"武汉事业部","typeTitle":"杠精工"},{"id":1,"userId":3,"typeId":2,"address":"湖北省武汉市汉阳区七里晴川小区","startDate":"2020-09-03","endDate":"2020-09-03","day":1,"price":300,"settleType":1,"advanceType":1,"num":1,"desc":"诚意招工，要求踏实肯干，有意者直接报名","isSafe":1,"status":1,"isFav":false,"isJoin":false,"publisher":"和尚","division":"武汉事业部","typeTitle":"电工"}]
     * page : 1
     * totalPage : 1
     */

    private int page;
    private int totalPage;

    @Override
    public String toString() {
        return "WorkListResponse{" +
                "page=" + page +
                ", totalPage=" + totalPage +
                '}';
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }


}
