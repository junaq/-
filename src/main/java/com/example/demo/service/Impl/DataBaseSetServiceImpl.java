package com.example.demo.service.Impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Dao.DataBaseSetDao;
import com.example.demo.model.DataBaseSet;
import com.example.demo.service.DataBaseSetService;

@Service
@Transactional
public class DataBaseSetServiceImpl implements DataBaseSetService {

	@Autowired
	private DataBaseSetDao dataBaseSetDao;
	
	@Override
	public List<String> findCheckedDataBaseName() {
		// TODO Auto-generated method stub
		return dataBaseSetDao.findCheckedDataBaseName();
	}

	@Override
	public void saveOrUpdate(String[] dataBaseNames) {
		dataBaseSetDao.deleteAll();
		List<DataBaseSet>dataBaseSets=new ArrayList<DataBaseSet>();
		for(String a :dataBaseNames) {
			DataBaseSet dataBaseSet=new DataBaseSet();
			dataBaseSet.setDataBaseName(a);
			dataBaseSets.add(dataBaseSet);
		}
		
 
		if(dataBaseSets.size()>0) {
			dataBaseSetDao.saveAll(dataBaseSets);
		}
	}

	@Override
	public List<DataBaseSet> findAll() {
		// TODO Auto-generated method stub
		return dataBaseSetDao.findAll();
	}
	
	

}
