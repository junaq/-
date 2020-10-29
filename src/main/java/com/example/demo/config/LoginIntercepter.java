package com.example.demo.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.demo.model.User;
import com.example.demo.util.JwtUtil;

public class LoginIntercepter implements HandlerInterceptor {
    
    @Autowired
    private JwtUtil jwtUtil; 

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        //401
        if (StringUtils.isEmpty(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        //403
        // 注入工具类
        if (jwtUtil == null) {
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            jwtUtil = (JwtUtil) factory.getBean("jwtUtil");
        }
        User user = jwtUtil.parseJWT(token);
        if (user == null) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }
        return true;
    }
}