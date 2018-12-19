package com.tianlangcloud.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mysql.jdbc.Statement;
import com.tianlangcloud.common.StaticString;
import com.tianlangcloud.model.Know;

@Repository
public class MSKnowDaoImpl implements MSKnowDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 查询所有知道
	 * @return
	 */
	@Override
	public List<Map<String, Object>> selectKnowAll() {
		return jdbcTemplate.queryForList(StaticString.SELECT_KNOW);
	}

	/**
	 * 添加知道
	 * @param know
	 * @return
	 */
	@Override
	public int insertKnow(final Know know) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement pre = con.prepareStatement(StaticString.INSERT_KNOW,Statement.RETURN_GENERATED_KEYS);
				pre.setString(1, know.getUid());
				pre.setString(2, know.getTitle());
				pre.setString(3, know.getContent());
				return pre;
			}
			
		}, keyHolder);
		
		int kid = keyHolder.getKey().intValue();
		return kid;
	}
	
	/**
	 * 根据知道ID查询详情
	 * @param id
	 * @return
	 */
	@Override
	public Map<String, Object> selectKnowById(int id) {
		return jdbcTemplate.queryForMap(StaticString.SELECT_KNOW_BY_ID, id);
	}

	/**
	 * 根据ID删除知道
	 * @param know
	 * @return
	 */
	@Override
	public void deleteKnowById(String id) {
		jdbcTemplate.update(StaticString.DELETE_KNOW_BY_ID, id);
	}
}
