package com.example.demo.Dao;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.Menu;

public interface MenuDao extends JpaRepository<Menu, Long>, JpaSpecificationExecutor<Menu> {

	Menu findByTitle(String tableName);

	
	@Query("select a from Menu a ,SysPre b where a.id =b.menuId and b.userId=?1")
	List<Menu> findByUserId(Long id);

}
