package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.model.Menu;
import com.example.demo.model.SysPre;
import com.example.demo.model.User;
import com.example.demo.service.MenuService;
import com.example.demo.service.SysPreService;
import com.example.demo.service.UserService;
import com.example.demo.util.DynamicSpecifications;
import com.example.demo.util.ResponseUtil;
import com.example.demo.util.SearchFilter;
import com.example.demo.util.SearchFilter.Operator;

import cn.hutool.http.server.HttpServerResponse;

@Controller
@RequestMapping("/sysPre")
public class SysPreController {

	@Autowired 
	private UserService userService;
	
	@Autowired 
	private MenuService menuService;
	
	@Autowired
	private SysPreService sysPreService;
	
	
	@PostMapping("/getList")
	public @ResponseBody String getList(HttpServletRequest request, HttpServerResponse response) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		Specification<User>specification=DynamicSpecifications.bySearchFilter(request, User.class,"", new  SearchFilter("name", Operator.NOTEQ, "admin"));
		List<User>list=userService.findByExample(specification) ;
	 
	 
		map.put("data", list);
 
 
		String json = JSON.toJSONString(map,SerializerFeature.DisableCircularReferenceDetect);
		return json;

	}
	@PostMapping("/getMenu")
	public @ResponseBody String getMenu(HttpServletRequest request, HttpServerResponse response) {

		Map<String, Object> map = new HashMap<String, Object>();
		
	 
		List<Menu>list=menuService.findAll() ;
	 
	 
		map.put("data", list);
 
 
		String json = JSON.toJSONString(map,SerializerFeature.DisableCircularReferenceDetect);
		return json;

	}
	
	@PostMapping("/getCheckedLists/{id}")
	public @ResponseBody String getCheckedLists(@PathVariable Long id,  HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> map = new HashMap<String, Object>();
		 
		Specification<SysPre>specification=DynamicSpecifications.bySearchFilter(request, SysPre.class,"", new  SearchFilter("userId", Operator.EQ, id));
		List<SysPre>list=sysPreService.findByExample(specification) ;
	    List<String> menuIds=new ArrayList<String>();
	    list.stream().forEach(syspre->{
	    	menuIds.add(syspre.getMenuId().toString());
	    });
		map.put("data", menuIds);
 
 
		String json = JSON.toJSONString(map,SerializerFeature.DisableCircularReferenceDetect);
		return json;

	}
	@PostMapping("/SaveCheckedLists/{userId}")
	public @ResponseBody JSONObject SaveCheckedLists(@PathVariable Long userId ,@RequestBody String[] checkedLists,HttpServletResponse response,HttpServletRequest request) {
		try {
			     
                sysPreService.SaveCheckedLists(checkedLists,userId);
				return ResponseUtil.ok("设置成功") .toJSONObject();
		 
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.error("设置失败").toJSONObject();
		}
	}
	
	
}
