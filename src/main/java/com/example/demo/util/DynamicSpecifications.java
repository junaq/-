package com.example.demo.util;
 

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.jpa.domain.Specification;

import cn.hutool.core.util.StrUtil;


/**
 * @author shaohong.lu
 * @version 创建时间：2019年9月12日 上午10:10:29 类说明
 */
public class DynamicSpecifications {
//	private static final Logger logger = LoggerFactory.getLogger(DynamicSpecifications.class);
	
	// 用于存储每个线程的request请求
	private static final ThreadLocal<HttpServletRequest> LOCAL_REQUEST = new ThreadLocal<HttpServletRequest>();
	
	private static final String SHORT_DATE = "yyyy-MM-dd";
	private static final String LONG_DATE = "yyyy-MM-dd mm:HH:ss";
	private static final String TIME = "mm:HH:ss";
	
	public static void putRequest(HttpServletRequest request) {
		LOCAL_REQUEST.set(request);
	}
	
	public static HttpServletRequest getRequest() {
		return LOCAL_REQUEST.get();
	}
	
	public static void removeRequest() {
		LOCAL_REQUEST.remove();
	}
	
	public static Collection<SearchFilter> genSearchFilter(ServletRequest request, String className) {
		Map<String, Object> searchParams = ServletUtils.getParametersStartingWith(request, "search_");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams,className);
		return filters.values();
	}
	
	public static Collection<SearchFilter> genSearchFilter(Map<String, Object> searchParams, String className) {
	    Map<String, SearchFilter> filters = SearchFilter.parse(searchParams,className);
	    return filters.values();
	}
	
	public static <T> Specification<T> bySearchFilter(ServletRequest request, final Class<T> entityClazz, String entityMainClazz,final Collection<SearchFilter> searchFilters) {
		return bySearchFilter(request, entityClazz, entityMainClazz, searchFilters.toArray(new SearchFilter[]{}));
	}
	
	public static <T> Specification<T> bySearchFilter(ServletRequest request, final Class<T> entityClazz, String entityMainClazz,final SearchFilter...searchFilters) {
		Collection<SearchFilter> filters = genSearchFilter(request,entityMainClazz);
		Set<SearchFilter> set = new HashSet<SearchFilter>(filters);
		for (SearchFilter searchFilter : searchFilters) {
			set.add(searchFilter);
		}
		return bySearchFilter(entityClazz, set);
	}
	
	public static <T> Specification<T> bySearchFilter(Map<String, Object> searchParams, final Class<T> entityClazz, String entityMainClazz,final SearchFilter...searchFilters) {
	    Collection<SearchFilter> filters = genSearchFilter(searchParams,entityMainClazz);
	    Set<SearchFilter> set = new HashSet<SearchFilter>(filters);
	    for (SearchFilter searchFilter : searchFilters) {
	        set.add(searchFilter);
	    }
	    return bySearchFilter(entityClazz, set);
	}

	@SuppressWarnings("unchecked")
	public static <T> Specification<T> bySearchFilter(final Class<T> entityClazz, final Collection<SearchFilter> searchFilters) {
		final Set<SearchFilter> filterSet = new HashSet<SearchFilter>();
		ServletRequest request = getRequest();
		if (request != null) {
			// 数据权限中的filter
			Collection<SearchFilter> nestFilters = 
					(Collection<SearchFilter>)request.getAttribute("$nest_dynamic_search$");
			if (nestFilters != null && !nestFilters.isEmpty()) {
				for (SearchFilter searchFilter : nestFilters) {
					filterSet.add(searchFilter);
				}
			}
		}
		
		// 自定义
		for (SearchFilter searchFilter : searchFilters) {
			filterSet.add(searchFilter);
		}
		
		return new Specification<T>() {
			@SuppressWarnings({ "rawtypes"})
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (filterSet != null && !filterSet.isEmpty()) {
					List<Predicate> predicates = new ArrayList<Predicate>();
					for (SearchFilter filter : filterSet) {
						// nested path translate, 如Task的名为"user.name"的filedName, 转换为Task.user.name属性
						String[] names = StrUtil.split(filter.getFieldName(), ".");
						Path expression = root.get(names[0]);
						for (int i = 1; i < names.length; i++) {
							expression = expression.get(names[i]);
						}

						// 自动进行enum和date的转换。
						Class clazz = expression.getJavaType();
						//
						if (Date.class.isAssignableFrom(clazz) && !filter.getValue().getClass().equals(clazz)) {
							filter.setValue(convert2Date((String)filter.getValue(),clazz));
						} else if (Enum.class.isAssignableFrom(clazz) && !filter.getValue().getClass().equals(clazz)) {
							filter.setValue(convert2Enum(clazz, (String)filter.getValue()));
						}
						
						// logic operator
						switch (filter.getOperator()) {
						case EQ:
							predicates.add(builder.equal(expression, filter.getValue()));
							break;
						case NOTEQ:
							predicates.add(builder.notEqual(expression, filter.getValue()));
							break;
						case LIKE:
							predicates.add(builder.like(expression, "%" + filter.getValue() + "%"));
							break;
						case GT:
							predicates.add(builder.greaterThan(expression, (Comparable) filter.getValue()));
							break;
						case LT:
							predicates.add(builder.lessThan(expression, (Comparable) filter.getValue()));
							break;
						case GTE:
							predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.getValue()));
							break;
						case LTE:
							predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.getValue()));
							break;
						case IN:
							predicates.add(builder.and(expression.in((Object[])filter.getValue())));
							break;
						default:
							break;

						}
					}
					// 将所有条件用 and 联合起来
					if (predicates.size() > 0) {
						return builder.and(predicates.toArray(new Predicate[predicates.size()]));
					}/* else {
						return null;
					} */
					/*else {
						//如果没有任何查询条件，默认查询为空
						Path expressionNull = root.get("id");
						SearchFilter filter = new SearchFilter("id",Operator.EQ,0);
						predicates.add(builder.equal(expressionNull, filter.getValue()));
						return builder.and(predicates.toArray(new Predicate[predicates.size()]));
					}*/
				}

				return builder.conjunction();
			}
		};
	}
	
	private static Date convert2Date(String dateString,Class<?> clazz) {
		SimpleDateFormat sFormat = new SimpleDateFormat(SHORT_DATE);
		try {
			Date date = sFormat.parse(dateString);
			//修改  当class为timeStamp类型时
			if(clazz.equals(Timestamp.class)){
				return new Timestamp(sFormat.parse(dateString).getTime());
			}
			return date;
		} catch (ParseException e) {
			try {
				return sFormat.parse(LONG_DATE);
			} catch (ParseException e1) {
				try {
					return sFormat.parse(TIME);
				} catch (ParseException e2) {
//					logger.error("Convert time is error! The dateString is " + dateString + "." + Exceptions.getStackTraceAsString(e2));
				}
			}
		}

		return null;
	}
		
	
	private static <E extends Enum<E>> E convert2Enum(Class<E> enumClass, String enumString) {
		return EnumUtils.getEnum(enumClass, enumString);
	}
}