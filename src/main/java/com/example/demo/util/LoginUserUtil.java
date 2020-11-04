package com.example.demo.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.example.demo.model.User;

public class LoginUserUtil {

	public static User getLoginUser(HttpServletRequest request, HttpServletResponse response) {
		String token = "";
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("token")) {
					token = cookie.getValue();
				}
			}
		}

		BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
		JwtUtil jwtUtil = (JwtUtil) factory.getBean("jwtUtil");

		User user = jwtUtil.parseJWT(token);

		return user;

	}
}
