package com.example.demo.service.Impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.Dao.DataBaseSetDao;
import com.example.demo.Dao.MenuDao;
import com.example.demo.Dao.SysConfigurationDao;
import com.example.demo.Dao.SysLogDao;
import com.example.demo.model.Menu;
import com.example.demo.model.SysConfiguration;
import com.example.demo.model.SysLog;
import com.example.demo.model.User;
import com.example.demo.service.SysConfigurationService;
import com.example.demo.util.Page;

@Transactional
@Service
public class SysConfigurationServiceImpl implements SysConfigurationService {

	@Autowired
	private SysConfigurationDao sysConfigurationDao;

	@Autowired
	private MenuDao menuDao;

	@Autowired
	private SysLogDao sysLogDao;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private DataBaseSetDao dataBaseSetDao;

	@Override
	public List<Map<String,Object>> ShowTables() {
		// TODO Auto-generated method stub
		
		List<String> dataBaseList=dataBaseSetDao.findCheckedDataBaseName();
		return sysConfigurationDao.ShowTables(dataBaseList);
	}

	@Override
	public List<SysConfiguration> findByExample(Specification<SysConfiguration> specification, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SysConfiguration> findByExample(Specification<SysConfiguration> specification) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SysConfiguration> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveOrupdate(SysConfiguration model) {
		// TODO Auto-generated method stub

	}

	@Override
	public SysConfiguration get(Long id) {
		// TODO Auto-generated method stub
		return sysConfigurationDao.getOne(id);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<SysConfiguration> findByTableNameAndDataBase(String tableName,String dataBase) {
		// TODO Auto-generated method stub
		return sysConfigurationDao.findByTableNameAndDataBase(tableName,dataBase);
	}

	@Override
	public List<Map<String, Object>> getColmunsByTableNameAndDataBase(String tableName,String dataBase) {
		// TODO Auto-generated method stub
		return sysConfigurationDao.getColmunsByTableNameAndDataBase(tableName,dataBase);
	}

	@Override
	public void saveByList(List<SysConfiguration> sysConfigurations) {
		// TODO Auto-generated method stub
		if (sysConfigurations.size() > 0) {
			// 判断菜单是否存在
			Menu menu = menuDao.findByTitle(sysConfigurations.get(0).getTableName());

			// 不存在则重新生成
			if (menu == null) {
				menu = new Menu();
				menu.setParentId(0L);
				menu.setTitle(sysConfigurations.get(0).getTableName());
				menu.setHref("/pages/commonMenu/add.html?tableName=" + menu.getTitle()+"&&dataBase="+sysConfigurations.get(0).getDataBase());
				menu.setIcon("el-icon-star-on");
				menuDao.save(menu);
			}

		}

		sysConfigurationDao.saveAll(sysConfigurations);

	}

	@Override
	public void saveCommonMenuData(JSONObject jsonDate, User user) {
		// TODO Auto-generated method stub
		String tableName = jsonDate.getString("tableName");
		String dataBase = jsonDate.getString("dataBase");
		JSONObject jsonObject = jsonDate.getJSONObject("data");
		Set<String> keys = jsonObject.keySet();
		String sql = "";
		if (keys.size() > 0) {
			tableName = "`" + dataBase + "`."+"`" + tableName + "`";
			sql = " insert into " + tableName;
			StringBuffer colmuns = new StringBuffer();
			StringBuffer values = new StringBuffer();

			for (String key : keys) {
				Object value = jsonObject.get(key);
				if (value instanceof String) {
					if (!StringUtils.isEmpty(value.toString())) {
						if (value.toString().indexOf("T") != -1 && value.toString().indexOf(".000Z") != -1) {

							colmuns.append("`" + key + "`,");
							values.append("'" + dealDateFormat(value.toString()) + "',");
						} else {
							colmuns.append("`" + key + "`,");
							values.append("'" + (value.toString()) + "',");
						}

					}
				} else if(value!=null){
					colmuns.append("`" + key + "`,");
					values.append(value + ",");
				}
			}
			colmuns.deleteCharAt(colmuns.length() - 1);
			values.deleteCharAt(values.length() - 1);
			sql += "(" + colmuns + ")values(" + values + ")";
			System.out.println(sql);
			jdbcTemplate.batchUpdate(sql);

		}
		SysLog sysLog = new SysLog();
		sysLog.setUserName(user.getRealName());
		sysLog.setSubmitData(jsonDate.toJSONString());
		sysLog.setSubmitSql(sql);
		sysLog.setCreateTime(new Date());
		sysLogDao.save(sysLog);
	}

	public static String dealDateFormat(String oldDate) {
		Date date1 = null;
		DateFormat df2 = null;
		try {
			oldDate = oldDate.replace("Z", " UTC");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
			Date date = df.parse(oldDate);
			SimpleDateFormat df1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
			date1 = df1.parse(date.toString());
			df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return df2.format(date1);
	}

	@Override
	public List<String> ShowDataBases() {
		// TODO Auto-generated method stub
		return sysConfigurationDao.ShowDataBases();
	}
}
