package com.jyy.interceptors;

import com.jyy.entity.PersonInfo;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class ShoploginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        Object user = httpServletRequest.getSession().getAttribute("user");
        if(user !=null){
            PersonInfo personInfo = (PersonInfo)user;
            if(personInfo != null&&personInfo.getUserId() != null
            &&personInfo.getUserId()>0&&personInfo.getEnableStatus() == 1){
                return true;
            }
        }
        PrintWriter out = httpServletResponse.getWriter();
        out.println("<html>");
        out.println("<script>");
        out.println("window.open('"+httpServletRequest.getContextPath()+
                "/local/login?usertype=2','_self')");
        out.println("</script>");
        out.println("</html>");
        return false;
    }




}
