package com.example.demo.Dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.demo.model.SysLog;

public interface SysLogDao extends JpaRepository<SysLog, Long>, JpaSpecificationExecutor<SysLog> {

 

}
