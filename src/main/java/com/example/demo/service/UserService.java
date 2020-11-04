package com.example.demo.service;

import com.example.demo.model.User;

public interface UserService extends BaseSevice<User,Long> {

	User findByNameAndPassword(String name, String pass);

}
