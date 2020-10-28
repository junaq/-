package com.example.demo.util;

 

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cn.hutool.core.util.StrUtil;



/**
 * 动态查询对象
 */
/**
 * 
 * @author LSH
 * EQ  等于
 * LIKE 像
 * GT 大于
 * LT 小于
 * GTE 大于等于
 * LTE 小于等于
 * IN  in查询
 * NOTEQ 不等于
 * BWDM  不知道
 */
public class SearchFilter {
	
	public enum Operator {
		/*
		 * EQ 等于 LIKE 像 GT 大于 LT 小于 GTE 大于等于 LTE 小于等于 IN in查询 NOTEQ 不等于 BWDM 不知道
		 */
		EQ,
		LIKE, 
		GT, 
		LT, 
		GTE, 
		LTE, 
		IN,
		NOTEQ,
		BWDM
	}

	private String fieldName;
	private Object value;
	private Operator operator;

	public SearchFilter(String fieldName, Operator operator, Object value) {
		this.fieldName = fieldName;
		this.value = value;
		this.operator = operator;
	}

	/**
	 * searchParams中key的格式为OPERATOR_FIELDNAME
	 */
	public static Map<String, SearchFilter> parse(Map<String, Object> searchParams, String className) {
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();

		for (Entry<String, Object> entry : searchParams.entrySet()) {
			// 过滤掉空值
			String key = entry.getKey();
			Object value = entry.getValue();
			if (StrUtil.isBlank((String) value)) {
				continue;
			}

			// 拆分operator与filedAttribute
			String[] names = StrUtil.split(key, "_");
			if (names.length != 2) {
				throw new IllegalArgumentException(key + " is not a valid search filter name");
			}
			String filedName = names[1];
			if(StrUtil.isNotBlank(className) &&filedName.contains(className)){
			    filedName = className+"." + filedName.split(className)[1];
			}
			Operator operator = Operator.valueOf(names[0]);

			// 创建searchFilter
			SearchFilter filter = new SearchFilter(filedName, operator, value);
			filters.put(key, filter);
		}

		return filters;
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @return the operator
	 */
	public Operator getOperator() {
		return operator;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
		result = prime * result + ((operator == null) ? 0 : operator.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
			
		}
		if (obj == null) {
			return false;
			
		}
		if (getClass() != obj.getClass()) {
			
			return false;
		}
		SearchFilter other = (SearchFilter) obj;
		if (fieldName == null) {
			if (other.fieldName != null) {
				
				return false;
			}
		} else if (!fieldName.equals(other.fieldName)) {
			
			return false;
		}
		if (operator != other.operator) {
			
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				
				return false;
			}
		} else if (!value.equals(other.value)) {
			
			return false;
		}
		return true;
	}
	
	
}