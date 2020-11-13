package com.example.demo.service;

import java.util.List;

import com.example.demo.model.User;

public interface UserService extends BaseSevice<User,Long> {

	User findByNameAndPassword(String name, String pass);

	List<User> findByName(String name);

	void updateUser(User user);

}
