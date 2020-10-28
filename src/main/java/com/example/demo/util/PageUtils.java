package com.example.demo.util;

 

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

 
public class PageUtils {

	/**
	 * 生成spring data JPA 对象 描述
	 * 
	 * @param page
	 * @return
	 */
	public static Pageable createPageable(Page page) {
		String[] fields={};
		if (StringUtils.hasText(page.getOrderField())) {
			if(page.getOrderField() != null && page.getOrderField().contains(",")) {
				fields = page.getOrderField().split(",");
			}
			// 忽略大小写
			if (page.getOrderDirection().equalsIgnoreCase(Page.ORDER_DIRECTION_ASC)) {
				if(fields.length == 2 ) {
					return new PageRequest(page.getPlainPageNum() - 1, page.getNumPerPage(), 
							Sort.Direction.ASC, fields[0],fields[1]);
				} else {
					return new PageRequest(page.getPlainPageNum() - 1, page.getNumPerPage(), 
							Sort.Direction.ASC, page.getOrderField());
				}
				
			} else {
				if(fields.length == 2 ) {
					return new PageRequest(page.getPlainPageNum() - 1, page.getNumPerPage(), 
							Sort.Direction.DESC, fields[0],fields[1]);
				} else {
					return new PageRequest(page.getPlainPageNum() - 1, page.getNumPerPage(), 
							Sort.Direction.DESC, page.getOrderField());
				}
			}
		}

		return new PageRequest(page.getPlainPageNum() - 1, page.getNumPerPage());
	}

	/**
	 * 将springDataPage的属性注入page描述
	 * 
	 * @param page
	 * @param springDataPage
	 */
	@Deprecated
	public static void injectPageProperties(Page page,
			org.springframework.data.domain.Page<?> springDataPage) {
		// 暂时只注入总记录数量
		page.setTotalCount(springDataPage.getTotalElements());
	}
}