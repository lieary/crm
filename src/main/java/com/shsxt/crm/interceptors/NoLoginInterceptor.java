package com.shsxt.crm.interceptors;

import com.shsxt.crm.exceptions.NoLoginException;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoLoginInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private UserService userService;

    /**
     * 如何判断用户是否登录
     *  1.获取coolie对象，找到ID
     *  2.判断ID是否为空，不为空看是否与数据库中的ID是否相等，相等不拦截，不相等，拦截抛出异常
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从cookie中获取ID
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //判断ID是否为空，不为空看是否与数据库中的ID是否相等，相等不拦截，不相等，拦截抛出异常
        if((userId == null) || userService.selectByPrimaryKey(userId) == null){

            throw new NoLoginException();
        }
        return true;
    }
}
