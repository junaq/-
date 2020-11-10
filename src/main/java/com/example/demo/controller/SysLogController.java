package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.example.demo.model.SysLog;
import com.example.demo.service.SysLogService;

import cn.hutool.http.server.HttpServerResponse;

@Controller
@RequestMapping("/sysLog")
public class SysLogController {

	@Autowired 
	private SysLogService sysLogService;
	
	@PostMapping("/getList")
	public @ResponseBody String getList(HttpServletRequest request, HttpServerResponse response) {

		Map<String, Object> map = new HashMap<String, Object>();
		List<SysLog> list = sysLogService.findAll();
	 
		map.put("data", list);
 
		map.put("colmuns", SysLog.class.getDeclaredFields());
		String json = JSON.toJSONString(map);
		return json;

	}
	
	
}
