package com.example.demo.Dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.demo.model.Menu;

public interface MenuDao extends JpaRepository<Menu, Long>, JpaSpecificationExecutor<Menu> {

}
