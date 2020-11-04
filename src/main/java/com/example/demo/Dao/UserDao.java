package com.example.demo.Dao;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.User;

public interface UserDao extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	
	@Query("from User WHERE name=?1 and passWord =?2")
	User findByNameAndPassword(String name, String pass);
 
	List<User> findByName(String name);

}
