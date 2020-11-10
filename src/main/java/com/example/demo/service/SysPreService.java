package com.example.demo.service;

import com.example.demo.model.SysPre;

public interface SysPreService extends BaseSevice<SysPre, Long> {

	void SaveCheckedLists(String[] checkedLists, Long userId);

}
