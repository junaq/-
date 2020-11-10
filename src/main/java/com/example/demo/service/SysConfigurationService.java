package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.SysConfiguration;
import com.example.demo.model.User;
 
public interface SysConfigurationService extends BaseSevice<SysConfiguration, Long> {
	public List<String>ShowTables();
	
 
	List<Map<String, Object>> getColmunsByTableName(String tableName);


    public void saveByList(List<SysConfiguration>sysConfigurations);


	List<SysConfiguration> findByTableName(String tableName);





	public void saveCommonMenuData(JSONObject jsonDate, User user);
}
