package com.example.demo.controller;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
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

import cn.hutool.core.io.resource.ClassPathResource;
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
				User user=LoginUserUtil.getLoginUser(request, response); 
				
				ClassPathResource resource = new ClassPathResource("classpath:templates/lib/web/web.json");
				InputStream inputStream=resource.getStream();
				
				File newFile =File.createTempFile("web", ".json");
				FileUtils.copyInputStreamToFile(inputStream, newFile);
				
				jsonObject =JSONUtil.readJSONObject(newFile, Charset.forName("utf8"));
				Specification<Menu>specification=DynamicSpecifications.bySearchFilter(request, Menu.class,"", new  SearchFilter("parentId", Operator.EQ, 0));
				List<Menu>menus= new ArrayList<Menu>();
				
				if("admin".equals( user.getName())) {
					
					menus=menuService.findByExample(specification) ;
				}else {
					menus=menuService.findByUserId(user.getId()) ;
				}
				
				
				
				jsonObject.set("name",user.getRealName());
				jsonObject.set("url",user.getUrl());
				jsonObject.set("navs", menus);
				menuList=jsonObject.toString();
			 
		 
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return menuList;
		
	}
	

}
