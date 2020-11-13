package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.SysConfiguration;
import com.example.demo.model.User;
 
public interface SysConfigurationService extends BaseSevice<SysConfiguration, Long> {
	public List<Map<String, Object>> ShowTables();
	
 
	List<Map<String, Object>> getColmunsByTableNameAndDataBase(String tableName,String dataBase);


    public void saveByList(List<SysConfiguration>sysConfigurations);


	List<SysConfiguration> findByTableNameAndDataBase(String tableName,String dataBase);





	public void saveCommonMenuData(JSONObject jsonDate, User user);


	public List<String> ShowDataBases();
}
