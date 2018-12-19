package com.tianlangcloud.dao;

import java.util.List;
import java.util.Map;

import com.tianlangcloud.model.Book;
import com.tianlangcloud.model.PageSerch;

public interface BookDao {
	
	/**
	 * 根据作家名称或书籍名称查询书籍
	 * @param pageable
	 * @return
	 */
	public List<Map<String, Object>> getBookByValue(PageSerch pageSerch);
	
	/**
	 * 根据作家名称或书籍名称查询书籍总条数
	 * @param pageable
	 * @return
	 */
	public int getBookCountByValue(PageSerch pageSerch);
	
	/**
	 * 获取章节列表
	 * @param value 书籍ID
	 */
	public List<Map<String, Object>> getBookNodeNames(Integer value);
	
	/**
	 * 获取书籍详情
	 * @param value 书籍ID
	 */
	public Map<String, Object> getBookById(Integer value);
	
	/**
	 * 获取章节内容
	 * @param value 章节ID
	 * @param valueBID 
	 */
	public Map<String, Object> getBookContent(Integer value, Integer valueBID);
	
	/**
	 * 获取书籍列表
	 * @param search 
	 * @param pageNum 起始页数
	 * @param pageSize 每页记录数
	 * @return
	 */
	public List<Map<String, Object>> getBookList(String search, Integer pageNum, Integer pageSize);
	
	/**
	 * 获取所有的作者
	 * @return
	 */
	public List<Map<String, Object>> getAuthotList();
	
	/**
	 * 获取所有的书籍类型
	 * @return
	 */
	public List<Map<String, Object>> getBookTypeList();
	
	/**
	 * 添加书籍
	 * @param aid 作者ID
	 * @param tid 书籍类型ID
	 * @param name 书籍名称
	 * @param isfinish 是否完本
	 */
	public int insertBook(String aid, String tid, String name, String isfinish);

	/**
	 * 书籍章节内容批量上传
	 * @param booklist 章节内容List
	 */
	public void insertBookContent(List<Book> booklist);
}
