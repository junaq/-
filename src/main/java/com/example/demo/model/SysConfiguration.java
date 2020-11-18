package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "sys_Configuration")
@Data
public class SysConfiguration {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String tableName;
	private String dataBase;
	private String name;
	private String egName;
	private String type;
	private String source;
	private int priority;
	private String required;
}
