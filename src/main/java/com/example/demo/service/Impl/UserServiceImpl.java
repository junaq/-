package com.example.demo.service.Impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.Dao.UserDao;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.util.Page;
import com.example.demo.util.PageUtils;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
	
	@Override
	public List<User> findByExample(Specification<User> specification, Page page) {
		org.springframework.data.domain.Page<User> springDataPage = userDao.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<User> findByExample(Specification<User> specification) {
		// TODO Auto-generated method stub
		return  userDao.findAll(specification);
	}

	@Override
	public void saveOrupdate(User t) {
		userDao.save(t);
		
	}

	@Override
	public User get(Long id) {
		//TODO Auto-generated method stub
		return userDao.getOne(id);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		userDao.deleteById(id);
	}

	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return userDao.findAll();
	}

	@Override
	public User findByNameAndPassword(String name, String pass) {
		// TODO Auto-generated method stub
		return userDao.findByNameAndPassword(name,pass);
	}

	@Override
	public List<User> findByName(String name) {
		// TODO Auto-generated method stub
		return userDao.findByName(name);
	}

}
