package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.SysConfiguration;
import com.example.demo.model.Tables;
import com.example.demo.model.User;
import com.example.demo.service.DataBaseSetService;
import com.example.demo.service.SysConfigurationService;
import com.example.demo.util.LoginUserUtil;
import com.example.demo.util.ResponseUtil;

import cn.hutool.http.server.HttpServerResponse;

@Controller
@RequestMapping("/SysConfiguration")
public class SysConfigurationController {

	@Autowired
	private SysConfigurationService sysConfigurationService;

	@Autowired
	private DataBaseSetService dataBaseSetService;
	
	@PostMapping("/getList")
	public @ResponseBody String getList(HttpServletRequest request, HttpServerResponse response) {

		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = sysConfigurationService.ShowTables();
		List<Tables> tables = new ArrayList<Tables>();
		list.stream().forEach(a -> {
			Tables table = new Tables();
			table.setTableName(a.get("tableName").toString());
			table.setDataBase(a.get("data_Base").toString());
			tables.add(table);
		});
		map.put("data", tables);
        
		String json = JSON.toJSONString(map);
		return json;

	}
	@PostMapping("/getDataBase")
	public @ResponseBody String getDataBase(HttpServletRequest request, HttpServerResponse response) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		List<String> list = sysConfigurationService.ShowDataBases();
 
		List<String>dataBaseSets=dataBaseSetService.findCheckedDataBaseName();
		list.remove("information_schema");
		list.remove("performance_schema");
		list.remove("mysql");
		
		map.put("data", list);
		map.put("CheckData", dataBaseSets);
        
		String json = JSON.toJSONString(map);
		return json;

	}

	

	@PostMapping("/getTable/{tableName}/{dataBase}")
	public @ResponseBody String getList(@PathVariable String tableName,@PathVariable String dataBase, HttpServletRequest request,
			HttpServerResponse response) {

		Map<String, Object> map = new HashMap<String, Object>();
		// 获取配置信息
		List<SysConfiguration> sysConfigurations = sysConfigurationService.findByTableNameAndDataBase(tableName,dataBase);

		List<SysConfiguration> tableData = new ArrayList<SysConfiguration>();
		List<Map<String, Object>> list = sysConfigurationService.getColmunsByTableNameAndDataBase(tableName,dataBase);
		tableData.addAll(sysConfigurations);
		list.stream().forEach(a -> {
			boolean flag = true;
			for (SysConfiguration sysConfiguration : sysConfigurations) {
				if (sysConfiguration.getName().equals(a.get("column_name"))) {
					flag = false;
				}
			}
			if (flag) {
				SysConfiguration sysConfiguration = new SysConfiguration();
				sysConfiguration.setName(a.get("column_name").toString());
				sysConfiguration.setEgName(a.get("column_name").toString());
				sysConfiguration.setTableName(tableName);
				sysConfiguration.setDataBase(dataBase);
				sysConfiguration.setPriority(0);
				String dataType = a.get("data_type").toString();
				if ("int".equals(dataType) || "bigInt".equals(dataType)) {
					sysConfiguration.setType("string");
				} else if ("double".equals(dataType) || "float".equals(dataType)) {
					sysConfiguration.setType("string");
				} else if ("date".equals(dataType) || "datetime".equals(dataType) || "".equals(dataType)) {
					sysConfiguration.setType("date");
				} else {
					sysConfiguration.setType("string");
				}
				sysConfiguration.setRequired("false");
				tableData.add(sysConfiguration);

			}

		});
		map.put("tableData", tableData);

		String json = JSON.toJSONString(map);
		return json;

	}

	@PostMapping(value = "/saveData")
	public @ResponseBody JSONObject saveData(@RequestBody List<SysConfiguration> sysConfigurations,
			HttpServletResponse response, HttpServletRequest request) {
		try {
			sysConfigurationService.saveByList(sysConfigurations);
			return ResponseUtil.ok("保存成功!").toJSONObject();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.error("保存失败!").toJSONObject();
		}
	}

	@PostMapping(value = "/saveCommonMenuData")
	public @ResponseBody JSONObject saveCommonMenuData(@RequestBody JSONObject jsonDate,
			HttpServletResponse response, HttpServletRequest request) {
		try {
			User user=LoginUserUtil.getLoginUser(request, response); 
			sysConfigurationService.saveCommonMenuData(jsonDate,user);
			return ResponseUtil.ok("保存成功!").toJSONObject();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.error("保存失败!").toJSONObject();
		}
	}
	
	
	
	@PostMapping("/CommonMenuAdd/{tableName}/{dataBase}")
	public @ResponseBody String CommonMenuAdd(@PathVariable String tableName,@PathVariable String dataBase,  HttpServletRequest request,
			HttpServerResponse response) {

		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
		Map<String, Object> descriptorsMap = new LinkedHashMap<String, Object>();

		List<SysConfiguration> sysConfigurations = sysConfigurationService.findByTableNameAndDataBase(tableName,dataBase);
		for (SysConfiguration sysConfiguration : sysConfigurations) {
			Map<String, Object> sysConfigurationMap = new HashMap<String, Object>();
			if ("enum".equals(sysConfiguration.getType())) {
				sysConfigurationMap.put("type",sysConfiguration.getType());
				sysConfigurationMap.put("label",sysConfiguration.getEgName());
				if(sysConfiguration.getSource()!=null) {		
					sysConfigurationMap.put("enum", sysConfiguration.getSource().split(";"));
				}
				sysConfigurationMap.put("required","true".equals(sysConfiguration.getRequired())?true:false);

			} else {
				sysConfigurationMap.put("type",sysConfiguration.getType());
				sysConfigurationMap.put("label",sysConfiguration.getEgName());
				sysConfigurationMap.put("required","true".equals(sysConfiguration.getRequired())?true:false);
		 
			}
			dataMap.put(sysConfiguration.getName(), null);
		    
			
			descriptorsMap.put(sysConfiguration.getName(), sysConfigurationMap);
		}

		map.put("data", dataMap);

		map.put("descriptors", descriptorsMap);
		String json = JSON.toJSONString(map);
		return json;

	}
	@PostMapping("/SaveCheckedLists")
	public @ResponseBody JSONObject SaveCheckedLists( @RequestBody String[] checkedLists,HttpServletResponse response,HttpServletRequest request) {
		try {
			     
			    dataBaseSetService.saveOrUpdate (checkedLists);
				return ResponseUtil.ok("设置成功") .toJSONObject();
		 
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.error("设置失败").toJSONObject();
		}
	}
	
	
}
