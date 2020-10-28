package com.example.demo.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.util.Page;

public interface BaseSevice<T,S> {
	public List<T> findByExample(Specification<T> specification, Page page);
	public List<T> findByExample(Specification<T> specification);
	public List<T> findAll();
	public void saveOrupdate(T model);
	public T get(S id);
	public void delete(S id);
	
}
