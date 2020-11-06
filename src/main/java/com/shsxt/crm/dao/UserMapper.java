package com.shsxt.crm.dao;

import com.shsxt.crm.base.BaseMapper;
import com.shsxt.crm.vo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface UserMapper extends BaseMapper<User,Integer> {



    //通过名字查数据
    User queryUserByName(String userName);

    //修改密码
     int updateUserPwd(@Param("userId") Integer userId, @Param("userPwd") String newPwd);

     //查询所有销售人员
    List<Map<String, Object>> queryAllSales();

}