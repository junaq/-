package com.example.demo.Dao;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.SysConfiguration;
 
public interface SysConfigurationDao extends JpaRepository<SysConfiguration, Long>, JpaSpecificationExecutor<SysConfiguration>{

	
	
	@Query(value = "SELECT table_name as tableName, table_schema as data_Base  FROM 	information_schema.TABLES WHERE table_schema IN ?1 AND TABLE_type = 'BASE TABLE'" ,nativeQuery=true)
	public List<Map<String, Object>> ShowTables(List<String> dataBaseList);
	
	@Query(value = "select column_name,cast(data_type as CHAR(45) ) as data_type  from information_schema.columns where table_name=?1 and table_schema=?2" ,nativeQuery=true)
	public List<Map<String, Object>> getColmunsByTableNameAndDataBase(String tableName, String dataBase);

	 
	@Query("from  SysConfiguration where tableName=?1 and dataBase=?2 order  by priority desc") 
	public List<SysConfiguration> findByTableNameAndDataBase(String tableName, String dataBase);
	
	@Transactional
	@Modifying
	@Query(value="insert into ?1 (?2) values(?3)",nativeQuery = true)
	public void addData(String tableName,String keys,String values);

	
	@Query(value = "Show DATABASEs" ,nativeQuery=true)
	public List<String> ShowDataBases();
 	
}
