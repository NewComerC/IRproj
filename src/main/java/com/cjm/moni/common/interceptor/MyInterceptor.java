package com.cjm.moni.common.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyInterceptor implements HandlerInterceptor {
    // @Override
    // public boolean preHandle(HttpServletRequest request, HttpServletResponse
    // response, Object handler)
    // throws Exception {
    // // TODO Auto-generated method stub
    // return false;
    // }
    //
    // @Override
    // public void postHandle(HttpServletRequest request, HttpServletResponse
    // response, Object handler,
    // ModelAndView modelAndView) throws Exception {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void afterCompletion(HttpServletRequest request,
    // HttpServletResponse response, Object handler, Exception ex)
    // throws Exception {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public boolean preHandle(HttpServletRequest request, HttpServletResponse
    // response, Object handler)
    // throws Exception {
    // System.out.println(">>>MyInterceptor1>>>>>>>在请求处理之前进行调用（Controller方法调用之前）");
    //
    //
    // response.setHeader("Access-Control-Allow-Origin","http://192.168.1.78:9000");
    // response.setHeader("Access-Control-Allow-Credentials", "true");
    // response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT,
    // DELETE");
    // response.setHeader("Access-Control-Max-Age", "3600");
    // response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache,
    // X-Requested-With, If-Modified-Since, Pragma, Last-Modified,
    // Cache-Control, Expires, Content-Type");
    // return true;// 只有返回true才会继续向下执行，返回false取消当前请求
    // }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)
            throws Exception {
        return true;// 只有返回true 才会继续请求, 否则取消请求
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // System.out.println(">>>MyInterceptor1>>>>>>>请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // System.out.println(">>>MyInterceptor1>>>>>>>在整个请求结束之后被调用");
    }
}
