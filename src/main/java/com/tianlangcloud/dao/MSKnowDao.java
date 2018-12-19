package com.tianlangcloud.dao;

import java.util.List;
import java.util.Map;

import com.tianlangcloud.model.Know;

public interface MSKnowDao {
	
	/**
	 * 查询所有知道
	 * @return
	 */
	public List<Map<String, Object>> selectKnowAll ();
	
	/**
	 * 添加知道
	 * @param know
	 * @return
	 */
	public int insertKnow(Know know);
	
	/**
	 * 根据知道ID查询详情
	 * @param id
	 * @return
	 */
	Map<String, Object> selectKnowById(int id);

	/**
	 * 根据ID删除知道
	 * @param know
	 * @return
	 */
	public void deleteKnowById(String id);
}
