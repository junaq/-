package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Menu;

public interface MenuService extends BaseSevice<Menu,Long> {

	List<Menu> findByUserId(Long id);

}
