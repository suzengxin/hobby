package com.tianlangcloud.service;

import java.util.List;

import com.tianlangcloud.model.Know;
import com.tianlangcloud.model.PageSerch;

public interface KnowService {
	
	/**
	 * 根据关键字进行全文查询
	 * @param keyWord
	 * @return
	 */
	public List<Know> searchKeyWord(PageSerch pageSerch);
	
	/**
	 * 根据ID查询mysql数据库中的知道详情
	 * @param id
	 * @return
	 */
	public Know msSelectKnowById(Integer id);
	
	/**
	 * 查询ES库中所有的“知道”根据修改时间排序
	 * @param pageSerch 
	 * @return
	 */
	List<Know> esSelectKnowAllSortByTime(PageSerch pageSerch);
	
	/**
	 * 查询ES库中所有的“知道”
	 * @return
	 */
	public List<Know> esSelectKnowAll ();
	
	/**
	 * 查询MS库中所有的“知道”
	 * @return
	 */
	public List<Know> msSelectKnowAll ();
	
	/**
	 * 同步添加知道
	 * @param know
	 * @return
	 */
	public void emInsertKnow(Know know);
	
	/**
	 * 根据ID同步删除知道
	 * @param know
	 * @return
	 */
	public void emDeleteKnowById(String id);
	
}
