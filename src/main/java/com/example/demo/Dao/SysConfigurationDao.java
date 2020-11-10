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

	
	
	@Query(value = "Show tables" ,nativeQuery=true)
	public List<String>ShowTables();
	
	@Query(value = "select column_name,cast(data_type as CHAR(45) ) as data_type  from information_schema.columns where table_name=?1" ,nativeQuery=true)
	public List<Map<String, Object>> getColmunsByTableName(String tableName);

	public List<SysConfiguration> findByTableName(String tableName);
	
	@Transactional
	@Modifying
	@Query(value="insert into ?1 (?2) values(?3)",nativeQuery = true)
	public void addData(String tableName,String keys,String values);
 	
}
