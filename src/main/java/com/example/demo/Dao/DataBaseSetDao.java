package com.example.demo.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.DataBaseSet;

public interface DataBaseSetDao extends JpaRepository<DataBaseSet, Long>, JpaSpecificationExecutor<DataBaseSet> {

	@Query("select dataBaseName from DataBaseSet")
    List<String>findCheckedDataBaseName();
	
}
