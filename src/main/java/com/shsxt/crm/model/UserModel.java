package com.shsxt.crm.model;
/*
* 在service层调用这个封装类，用来返回一些必要的信息给前端，不必返回整个User对象
* 返回封装返回用户对象
*
* */

public class UserModel {
   // private Integer userId; //用户名称
    private String userIdStr;
    private String userName; //用户姓名
    private String trueName;//真实用户姓名

    public String getUserIdStr() {
        return userIdStr;
    }

    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }
}
