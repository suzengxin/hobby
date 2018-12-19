package com.tianlangcloud.dao;

import java.util.List;
import java.util.Map;

public interface HandlerDao {
	
	/**
	 * 获取IP白名单
	 * @return
	 */
	public List<Map<String, Object>> getIpWhiteList();
}
