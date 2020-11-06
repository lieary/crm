package com.shsxt.crm.controller;

import com.shsxt.crm.base.BaseController;
import com.shsxt.crm.base.ResultInfo;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import com.shsxt.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    //注入service层
    @Resource
    private UserService userService;


    /**
     * 用户登录
     * @param userName
     * @param userPwd
     * @return
     */
    @RequestMapping("login")
    @ResponseBody
    public ResultInfo userLogin(String userName,String userPwd){
        //接收用户信息返回页面
        ResultInfo resultInfo = new ResultInfo();
        //UserModel userModel;
        /*try {
            //调用service的方法
            userModel = userService.userLogin(userName,userPwd);
            //没有异常，返回成功数据
            resultInfo.setCode(200);
            resultInfo.setMsg(userName);
            resultInfo.setResult(userModel);
        }catch (ParamsException p){
            //异常处理exception的数据异常
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            return resultInfo;
        }catch (Exception e){
            resultInfo.setCode(405);
            resultInfo.setMsg("系统异常，请重新登录！");
            return resultInfo;
        }*/
        //调用service的方法
        UserModel userModel = userService.userLogin(userName,userPwd);
        //没有异常，返回成功数据
        resultInfo.setCode(200);
        //1...resultInfo.setMsg(userName);
        resultInfo.setResult(userModel);
        return resultInfo;
    }

    /**
     * 修改密码
     * @param request
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     * @return
     */
    @RequestMapping("updatePwd")
    @ResponseBody
    public ResultInfo updateUserPassWord(HttpServletRequest request,String oldPwd,String newPwd,String repeatPwd){
        //创建接收对象
        ResultInfo resultInfo = new ResultInfo();

       /* try{
            //从cookie中获取当前用户登录的id
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            //调用service层执行修改密码的操作
            userService.updateUserPwd(userId,oldPwd,newPwd,repeatPwd);
            resultInfo.setCode(200);
        }catch(ParamsException p){
            p.printStackTrace();
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            return resultInfo;
        }catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("修改密码失败！");
        }*/
        //从cookie中获取当前用户登录的id
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //调用service层执行修改密码的操作
        userService.updateUserPwd(userId,oldPwd,newPwd,repeatPwd);
        resultInfo.setCode(200);

    return resultInfo;
    }

    /**
     * 修改密码的页面
     * @return
     */

    @RequestMapping("toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }

    /**
     * 查询所有销售人员
     * @return
     */
    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String, Object>> queryAllSales(){
        return userService.queryAllSales();
    }
}
