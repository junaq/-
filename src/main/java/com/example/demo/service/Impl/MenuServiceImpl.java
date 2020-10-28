package com.example.demo.service.Impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.Dao.MenuDao;
import com.example.demo.model.Menu;
import com.example.demo.service.MenuService;
import com.example.demo.util.Page;
import com.example.demo.util.PageUtils;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {
	@Autowired
	private MenuDao menuDao;

	@Override
	public List<Menu> findByExample(Specification<Menu> specification, Page page) {
		org.springframework.data.domain.Page<Menu> springDataPage = menuDao.findAll(specification,
				PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<Menu> findByExample(Specification<Menu> specification) {
		// TODO Auto-generated method stub
		return menuDao.findAll(specification);
	}

	@Override
	public void saveOrupdate(Menu t) {
		menuDao.save(t);

	}

	@Override
	public Menu get(Long id) {
		// TODO Auto-generated method stub
		return menuDao.getOne(id);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		menuDao.deleteById(id);
	}

	@Override
	public List<Menu> findAll() {
		// TODO Auto-generated method stub
		return menuDao.findAll();
	}

}
