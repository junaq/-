package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import com.example.demo.util.MD5Util;
import com.example.demo.util.ResponseUtil;

@Controller
@RequestMapping("/login/")
public class LoginController {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserService userService;

	private static final String LOGIN ="/pages/login/login.html";
	private static final String REG ="/pages/login/reg.html";
	
	@GetMapping("")
	public String login(){
		return LOGIN;
	}
	
	@GetMapping("/loginOut")
	public String loginOut(HttpServletResponse response,HttpServletRequest request){
		Cookie cookie=new Cookie("token","");
		cookie.setMaxAge(0);
		cookie.setPath("/");
        response.addCookie(cookie);
        
        cookie=new Cookie("menu","");
		cookie.setMaxAge(0);
		cookie.setPath("/");
        response.addCookie(cookie);
		return LOGIN;
	}
	
	@GetMapping("/reg")
	public String reg(){

		return REG;
	}
	
	
	@PostMapping(value = "/loginUser")
	public @ResponseBody JSONObject login(@RequestBody User user,HttpServletResponse response,HttpServletRequest request) {
		try {
			// 登录判定
 		    user = userService.findByNameAndPassword(user.getName(), MD5Util.MD5Encode(user.getPassWord(), "utf-8"));
			if(user!=null) {
				
				// 生成token
				String token = jwtUtil.createJWT(user);
				Cookie cookie=new Cookie("token",token);
				cookie.setMaxAge(30 * 24 * 60 * 60);
				cookie.setPath("/");
		        response.addCookie(cookie);
				return ResponseUtil.ok("登陆成功").setToken(token).toJSONObject();
			}else {
				return ResponseUtil.error("密码错误！").toJSONObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.error("登陆失败").toJSONObject();
		}
	}
	@PostMapping(value = "/regUser")
	public @ResponseBody JSONObject regUser(@RequestBody User user) {
		try {
			user.setPassWord( MD5Util.MD5Encode(user.getPassWord(), "utf-8"));
 		    user.setUrl("./img/head.jpg");
			userService.saveOrupdate(user); 		 
			return ResponseUtil.ok("注册成功") .toJSONObject();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.error("注册失败").toJSONObject();
		}

	}
	@PostMapping(value = "/validName")
	public @ResponseBody JSONObject validName(@RequestBody User user) {
		try {
			 
			List<User>users =userService.findByName (user.getName()); 	
			
			if(users.size()>0) {	
				return ResponseUtil.error("用户名已经存在！") .toJSONObject();
			}else {
				return ResponseUtil.ok("") .toJSONObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.error("验证失败！").toJSONObject();
		}

	}
	

}
