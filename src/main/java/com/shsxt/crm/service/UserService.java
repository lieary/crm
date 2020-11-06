package com.shsxt.crm.service;

import com.shsxt.crm.base.BaseService;
import com.shsxt.crm.dao.UserMapper;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.utils.Md5Util;
import com.shsxt.crm.utils.UserIDBase64;
import com.shsxt.crm.vo.SaleChance;
import com.shsxt.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service
public class UserService extends BaseService<User,Integer> {
    //注入dao层
    @Resource
    private UserMapper userMapper;

    /**
     * 用户登录
     * @param userName
     * @param userPwd
     * @return
     */
    public UserModel userLogin(String userName, String userPwd) {


        //调用AssertUtil工具类 判断用户名或密码是否为空，如果为空，则抛出异常
        AssertUtil.isTrue(StringUtils.isBlank(userName) || StringUtils.isBlank(userPwd),"用户名或密码不能为空！");

        //调用dao层方法，通过用户名查询用户对象
        User user = userMapper.queryUserByName(userName);

        //判断用户名是否存在
        AssertUtil.isTrue(user == null,"用户对象不存在！");

        //如果存在，则对用户密码进行加密比较,不相等，则抛出异常
        AssertUtil.isTrue(!Md5Util.encode(userPwd).equals(user.getUserPwd()),"用户密码不正确！");

        //构建用户对象的model模型
        return buildUserInfo(user);
    }

    /*private void checkLoginPwd(String userPwd, String upwd) {
        // 数据库中的密码是经过加密的，将前台传递的密码先加密，再与数据库中的密码作⽐较
        userPwd = Md5Util.encode(userPwd);
        // ⽐较密码
        AssertUtil.isTrue(!userPwd.equals(upwd), "⽤户密码不正确！");
    }


    private void checkLoginParams(String userName, String userPwd) {
        // 判断姓名
        AssertUtil.isTrue(StringUtils.isBlank(userName), "⽤户姓名不能为空！");
        // 判断密码
        AssertUtil.isTrue(StringUtils.isBlank(userPwd), "⽤户密码不能为空！");
    }*/

    /**
     * 构建用户对象模型
     * @param user
     * @return
     */
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();

        //设置用户信息
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());

        return userModel;
    }


    /**
     * 修改用户对象密码
     * @param userId
     * @param oldPwd
     * @param newPwd
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserPwd(Integer userId, String oldPwd, String newPwd, String repeatPwd) {
        //判断用户ID是否为空
        AssertUtil.isTrue(userId == null,"用户ID不存在！");
        //调用dao层查询用户对象
        User user = userMapper.selectByPrimaryKey(userId);
        //判断用户对象是否为空
        AssertUtil.isTrue(user == null,"用户对象不存在！");
        //判断用户旧密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd),"用户原始密码不能为空！");
        //判断用户旧密码是否正确
        AssertUtil.isTrue(!Md5Util.encode(oldPwd).equals(user.getUserPwd()),"用户旧密码不正确！");
        //判断新密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"用户新密码不能为空！");
        //判断新密码和旧密码是否一致
        AssertUtil.isTrue(oldPwd.equals(newPwd),"用户新旧密码不能相同！");
        //判断判断重复密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(repeatPwd),"重复密码不能为空！");
        //判断重复密码是否与新密码一致
        AssertUtil.isTrue(!newPwd.equals(repeatPwd),"新密码与确认密码不一致！");
        //执行操作
        AssertUtil.isTrue(userMapper.updateUserPwd(userId, Md5Util.encode(newPwd)) < 1,"修改密码失败！");
    }


    /**
     * 查询所有销售人员
     * @return
     */
    public List<Map<String , Object>> queryAllSales(){

        return userMapper.queryAllSales();
    }
}
