package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import com.example.demo.util.ResponseUtil;

@Controller
@RequestMapping("/login/")
public class LoginController {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserService userService;

	private static final String LOGIN ="/pages/login/login.html";
	
	@GetMapping("")
	public String login(){
		return LOGIN;
	}
	
	
	@PostMapping(value = "/loginUser")
	public @ResponseBody String login(@RequestBody User user) {
		try {
			// 登录判定
			  user = userService.findByNameAndPassword(user.getName(), user.getPassWord());
			// 生成token
			String token = jwtUtil.createJWT(user);
			return ResponseUtil.ok("登陆成功").setToken(token).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.ok("登陆失败").toString();
		}

	}
}
