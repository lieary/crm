package com.shsxt.crm.query;

import com.shsxt.crm.base.BaseQuery;

/**
 * 营销机会查询类
 */
public class SaleChanceQuery extends BaseQuery {
    private String customerName;//客户名
    private String createMan;//创建人
    private String State;//创建状态
    //客户开发计划
    private Integer assignMan;
    private Integer devResult;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public Integer getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(Integer assignMan) {
        this.assignMan = assignMan;
    }

    public Integer getDevResult() {
        return devResult;
    }

    public void setDevResult(Integer devResult) {
        this.devResult = devResult;
    }
}
