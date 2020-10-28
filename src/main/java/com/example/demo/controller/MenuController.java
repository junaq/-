package com.example.demo.controller;

import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.Menu;
import com.example.demo.service.MenuService;
import com.example.demo.util.DynamicSpecifications;
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
	public @ResponseBody String getMenu(ServletRequest request){
		JSONObject jsonObject =new JSONObject();
		try{
			jsonObject =JSONUtil.readJSONObject(ResourceUtils.getFile("classpath:templates/lib/web/web.json"), Charset.forName("utf8"));
			Specification<Menu>specification=DynamicSpecifications.bySearchFilter(request, Menu.class,"", new  SearchFilter("parentId", Operator.EQ, 0));
			List<Menu>menus=menuService.findByExample(specification) ;
			jsonObject.set("navs", menus);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject.toString() ;
		
	}
	

}
