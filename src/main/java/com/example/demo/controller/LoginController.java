package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
//import javax.ws.rs.core.Response;
import com.example.demo.model.User;
import com.example.demo.util.JwtUtil;

@Controller
@RequestMapping("/login")
public class LoginController {

	// @Autowired
	// private JwtUtil jwtUtil;

	// @PostMapping(value = "/login" )
	// public Response login(@RequestParam(value = "name") String name,
	// @RequestParam(value = "pass") String pass) {
	// // 登录判定
	// User user = userService.getLoginUserService(name, pass);
	// // 生成token
	// String token = jwtUtil.createJWT(user);
	// return Response.ok().data(token);
	// }
}
