package com.example.demo.service;

import java.util.List;

import com.example.demo.model.DataBaseSet;

public interface DataBaseSetService {

	List<String>findCheckedDataBaseName();
	List<DataBaseSet>findAll();
	void saveOrUpdate(String[] dataBaseNames);
}
