package com.example.demo.controller;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.Menu;
import com.example.demo.model.User;
import com.example.demo.service.MenuService;
import com.example.demo.util.DynamicSpecifications;
import com.example.demo.util.LoginUserUtil;
import com.example.demo.util.SearchFilter;
import com.example.demo.util.SearchFilter.Operator;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

 
 
 

 

 

@Controller
@RequestMapping("/menu")
public class MenuController {
	@Autowired
	private MenuService menuService;
 
	
	
	@GetMapping("/getMenu")
	public @ResponseBody String getMenu(HttpServletRequest request,HttpServletResponse response){
		JSONObject jsonObject =new JSONObject();
		String menuList=null;
		try{
	        Cookie[] cookies =  request.getCookies();
	        if(cookies != null){
	            for(Cookie cookie : cookies){
	                if(cookie.getName().equals("menu")){
	                	menuList=cookie.getValue();
	                }
	            }
	        }
	      

 

	        
			if( StringUtils.isEmpty(menuList)) {
				
				User user=LoginUserUtil.getLoginUser(request, response); 
				jsonObject =JSONUtil.readJSONObject(ResourceUtils.getFile("classpath:templates/lib/web/web.json"), Charset.forName("utf8"));
				Specification<Menu>specification=DynamicSpecifications.bySearchFilter(request, Menu.class,"", new  SearchFilter("parentId", Operator.EQ, 0));
				List<Menu>menus=menuService.findByExample(specification) ;
				jsonObject.set("name",user.getRealName());
				jsonObject.set("url",user.getUrl());
				jsonObject.set("navs", menus);
				menuList=jsonObject.toString();
				String encodeCookie = URLEncoder.encode(menuList, "utf-8");
				Cookie cookie=new Cookie("menu",encodeCookie);
				cookie.setMaxAge(30 * 24 * 60 * 60);
				cookie.setPath("/");
				response.addCookie(cookie);
			}else {
				menuList=URLDecoder.decode(menuList , "utf-8");
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return menuList;
		
	}
	

}
