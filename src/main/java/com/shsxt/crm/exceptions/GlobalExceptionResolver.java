package com.shsxt.crm.exceptions;

import com.alibaba.fastjson.JSON;
import com.shsxt.crm.base.ResultInfo;
import com.shsxt.crm.exceptions.NoLoginException;
import com.shsxt.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object obj, Exception e) {
        System.out.println("今日全局异常处理");

        //判断用户是否为未登录异常
        if(e instanceof NoLoginException){
            ModelAndView mv = new ModelAndView("redirect:/index");
            return mv;
        }


        //设置默认异常处理
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("Error");
        modelAndView.addObject("code",410);
        modelAndView.addObject("msg","服务忙，请稍后再试...");


        //判断方法的返回值，返回值有两种，一种是视图，一种是json类型
        //判断返回的方法是不是HandlerMethod类型，如果是，可以拿到这个方法的注解
        if(obj instanceof HandlerMethod) {
            //强转
            HandlerMethod handlerMethod = (HandlerMethod) obj;
            //获取方法上的ResponseBody注解
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            //判断注解是否存在
            if (responseBody == null) {
                //如果为null，则说明不是json格式，返回视图
                //需要判断是否有其他异常
                if (e instanceof ParamsException) {
                    ParamsException p = (ParamsException) e;
                    modelAndView.addObject("code", p.getCode());
                    modelAndView.addObject("msg", p.getMsg());
                }
                return modelAndView;
            } else {
                //不为null则返回json格式
                 ResultInfo resultInfo = new ResultInfo();
                // resultInfo.setCode(500);
                // resultInfo.setMsg("系统异常，请重试！");
                //需要判断是否有其他异常
                if (e instanceof ParamsException) {
                    ParamsException p = (ParamsException) e;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                }

                //设置响应json的格式，防止乱码
                response.setContentType("application/json;charset=UTF-8");

                PrintWriter writer = null;
                try {
                    //得到字符输出流
                    writer = response.getWriter();
                    writer.write(JSON.toJSONString(resultInfo));
                    writer.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }finally {
                    if(writer != null){
                        writer.close();
                    }
                }

                //json格式没有视图，可以返回null
                return null;
            }
        }
        return modelAndView;
    }
}
