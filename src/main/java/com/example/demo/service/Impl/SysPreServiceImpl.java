package com.example.demo.service.Impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.Dao.SysPreDao;
import com.example.demo.model.SysPre;
import com.example.demo.service.SysPreService;
import com.example.demo.util.Page;

@Transactional
@Service
public class SysPreServiceImpl  implements SysPreService{

	@Autowired
	private SysPreDao sysPreDao;
	
	@Override
	public List<SysPre> findByExample(Specification<SysPre> specification, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SysPre> findByExample(Specification<SysPre> specification) {
		// TODO Auto-generated method stub
		return sysPreDao.findAll(specification);
	}

	@Override
	public List<SysPre> findAll() {
		// TODO Auto-generated method stub
		return sysPreDao.findAll();
	}

	@Override
	public void saveOrupdate(SysPre model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SysPre get(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SaveCheckedLists(String[] checkedLists, Long userId) {
	     //删除全部权限
		  sysPreDao.deleteByUserId(userId);
		  for(String menuId:checkedLists) {
			  SysPre sysPre=new SysPre();
			  sysPre.setUserId(userId);
			  sysPre.setMenuId(Long.valueOf(menuId));
			  sysPreDao.save(sysPre);
		  }
		  
		
	}

}
