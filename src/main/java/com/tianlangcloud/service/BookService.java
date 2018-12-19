package com.tianlangcloud.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.tianlangcloud.model.PageSerch;

public interface BookService {
	/**
	 * 根据作家名称或书籍名称查询书籍
	 * @param pageable
	 * @return
	 */
	public List<Map<String, Object>> getBookByValue(PageSerch pageSerch);

	/**
	 * 前往章节列表页的业务逻辑
	 * @param request
	 * @param value 书籍ID
	 */
	public Map<String, Object> bookNodeNames(Integer value);

	/**
	 * 前往书籍章节内容页的业务逻辑
	 * @param request
	 * @param value 章节ID
	 * @param valueBID 
	 */
	public Map<String, Object> bookContent(Integer value, Integer valueBID);
	
	/**
	 * 获取书籍列表
	 * @param pageNum 起始页数
	 * @param pageSize 每页记录数
	 * @return
	 */
	List<Map<String, Object>> getBookList(String search, Integer pageNum, Integer pageSize);

	/**
	 * 书籍上传前的业务处理
	 * @return
	 */
	Map<String, Object> uploadBookService();

	/**
	 * 书籍上传
	 * @param aid 作者ID
	 * @param tid 类型ID
	 * @param isfinish 是否完本
	 * @param bookName 书籍名称
	 * @param inputStream 书籍文件流
	 * @return
	 */
	Boolean uploadBook(String aid, String tid, String isfinish, String bookName, InputStream inputStream);
}
