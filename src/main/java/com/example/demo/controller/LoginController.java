package com.example.demo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import com.example.demo.util.LoginUserUtil;
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
				cookie.setMaxAge(1 * 24 * 60 * 60);
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
 		    user.setUrl("/login/openImg/"+user.getName()+".jpg");
			userService.saveOrupdate(user); 		 
			return ResponseUtil.ok("注册成功") .toJSONObject();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.error("注册失败").toJSONObject();
		}
	}
	@PostMapping(value = "/uploadImg")
	public @ResponseBody JSONObject uploadImg(@RequestParam("file") MultipartFile file,HttpServletResponse response,HttpServletRequest request) {
		try {
			ApplicationHome h = new ApplicationHome(getClass());
	        File jarF = h.getSource();
	        User user=LoginUserUtil.getLoginUser(request, response); 
	        System.out.println(jarF.getParentFile().toString()) ; 
	        File dest = new File(jarF.getParentFile().toString()+"/img/"+user.getName()+"tmp.jpg");
	        file.transferTo(dest);
	        
	        return ResponseUtil.ok("上传成功").setToken(user.getName()+"tmp.jpg") .toJSONObject();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.error("上传失败").toJSONObject();
		}
	}
	@PostMapping(value = "/updateUser")
	public @ResponseBody JSONObject updateUser(@RequestBody User user,HttpServletResponse response,HttpServletRequest request) {
		try {
 
			user.setPassWord( MD5Util.MD5Encode(user.getPassWord(), "utf-8"));
			userService.updateUser(user);
			ApplicationHome h = new ApplicationHome(getClass());
	        File jarF = h.getSource();
	        
	        File dest = new File(jarF.getParentFile().toString()+"/img/"+user.getName()+"tmp.jpg");
	        dest.renameTo(new File(jarF.getParentFile().toString()+"/img/"+user.getName()+".jpg"));
	        return ResponseUtil.ok("修改成功") .toJSONObject();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.error("修改失败").toJSONObject();
		}
	}
	
	
	
	@GetMapping(value = "/openImg/{fileName}")
	public void oppenImg( @PathVariable String fileName,HttpServletResponse response,HttpServletRequest request) {
		try {
			ApplicationHome h = new ApplicationHome(getClass());
	        File jarF = h.getSource();
	       
	        System.out.println(jarF.getParentFile().toString()) ; 
	        
	        //根据文件信息中文件名字 和 文件存储路径获取文件输入流
	        String realpath = jarF.getParentFile().toString()+"/img/"+fileName;
	        //获取文件输入流
	       File file= new File(realpath );
	        if(!file.exists()) {
	        	 realpath = jarF.getParentFile().toString()+"/img/head.jpg";
	        	file=new File(realpath);
	        }
	        FileInputStream is = new FileInputStream(file);
	        //附件下载
	        response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
	        //获取响应输出流
	        ServletOutputStream os = response.getOutputStream();
	        //文件拷贝
	        IOUtils.copy(is, os);
	        os.close();
	        is.close();
	        
	         
		} catch (Exception e) {
			e.printStackTrace();
			 
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
