package com.example.demo.Dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.demo.model.User;

public interface UserDao extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

}
