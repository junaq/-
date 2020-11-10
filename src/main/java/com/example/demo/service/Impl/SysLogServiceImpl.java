package com.example.demo.service.Impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.Dao.SysLogDao;
import com.example.demo.model.SysLog;
import com.example.demo.service.SysLogService;
import com.example.demo.util.Page;


@Transactional
@Service
public class SysLogServiceImpl implements  SysLogService {

	@Autowired 
	private SysLogDao sysLogDao;
	
	@Override
	public List<SysLog> findByExample(Specification<SysLog> specification, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SysLog> findByExample(Specification<SysLog> specification) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SysLog> findAll() {
		// TODO Auto-generated method stub
		return sysLogDao.findAll();
	}

	@Override
	public void saveOrupdate(SysLog model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SysLog get(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

}
