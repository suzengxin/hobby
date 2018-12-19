package com.tianlangcloud.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tianlangcloud.common.StaticString;

@Repository
public class HandlerDaoImpl implements HandlerDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 获取IP白名单
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getIpWhiteList() {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(StaticString.SELECT_IP_WHITE_ROLL);
		return list;
	}
	
}
