package com.example.demo.Dao;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.SysPre;

public interface SysPreDao extends JpaRepository<SysPre, Long>, JpaSpecificationExecutor<SysPre> {

	
	@Modifying
	@Transactional
	@Query("delete from SysPre where userId =?1")
	void deleteByUserId(Long id);

 

}
