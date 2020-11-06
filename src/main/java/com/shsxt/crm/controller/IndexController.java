package com.shsxt.crm.controller;

import com.shsxt.crm.base.BaseController;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import com.shsxt.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController extends BaseController {

    @Resource
    private UserService userService;

    //系统登录界面
    @RequestMapping("index")
    public String index(){
        return "index";
    }
    //系统主页面
    @RequestMapping("main")
    public String main(HttpServletRequest request){
        //获取cookie中用户的id
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //通过用户id查询用户对象
        User user = userService.selectByPrimaryKey(userId);
        //将用户对象设置到作用域中
        request.setAttribute("user",user);

        return "main";
    }
    //登录欢迎页面
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }



}
