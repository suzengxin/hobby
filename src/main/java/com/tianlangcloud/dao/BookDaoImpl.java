package com.tianlangcloud.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mysql.jdbc.Statement;
import com.tianlangcloud.common.StaticString;
import com.tianlangcloud.model.Book;
import com.tianlangcloud.model.PageSerch;

@Repository
public class BookDaoImpl implements BookDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 根据作家名称或书籍名称查询书籍
	 * @param pageable
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getBookByValue(PageSerch pageSerch) {
		String sql = "SELECT b.id,b.book_name,a.`name` FROM book b LEFT JOIN author a ON b.author_id = a.id WHERE a.`name` LIKE '%"+pageSerch.getKeyWord()+"%' OR b.book_name LIKE '%"+pageSerch.getKeyWord()+"%' LIMIT "+pageSerch.getPageSize() * pageSerch.getPageNumber()+","+pageSerch.getPageSize();
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}
	
	/**
	 * 根据作家名称或书籍名称查询书籍总条数
	 * @param pageable
	 * @return
	 */
	public int getBookCountByValue(PageSerch pageSerch){
		String sql = "SELECT count(b.id) FROM book b LEFT JOIN author a ON b.author_id = a.id WHERE a.`name` LIKE '%"+pageSerch.getKeyWord()+"%' OR b.book_name LIKE '%"+pageSerch.getKeyWord()+"%'";
		Integer integer = jdbcTemplate.queryForObject(sql, Integer.class);
		if (integer != null) {
			return integer;
		} else {
			return 0;
		}
	};
	
	/**
	 * 获取章节列表
	 * @param value 书籍ID
	 */
	@Override
	public List<Map<String, Object>> getBookNodeNames(Integer value) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(StaticString.SELECT_BOOK_NODE_NAMES, value);
		return list;
	}
	
	/**
	 * 获取书籍详情
	 * @param value 书籍ID
	 */
	@Override
	public Map<String, Object> getBookById(Integer value) {
		Map<String, Object> map = jdbcTemplate.queryForMap(StaticString.SELECT_BOOK_BY_ID, value);
		return map;
	}

	/**
	 * 获取章节内容
	 * @param value 章节ID
	 */
	@Override
	public Map<String, Object> getBookContent(Integer value, Integer valueBID) {
		Map<String, Object> map = jdbcTemplate.queryForMap(StaticString.SELECT_BOOK_CONTENT, value, valueBID);
		return map;
	}

	/**
	 * 获取书籍列表
	 * @param search 
	 * @param pageNum 起始页数
	 * @param pageSize 每页记录数
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getBookList(String search, Integer pageNum, Integer pageSize) {
		String sql = StaticString.SELECT_BOOK_LIST;
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}

	/**
	 * 获取所有的作者
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getAuthotList() {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(StaticString.SELECT_AUTHOR_LIST);
		return list;
	}

	/**
	 * 获取所有的书籍类型
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getBookTypeList() {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(StaticString.SELECT_BOOK_TYPE_LIST);
		return list;
	}

	/**
	 * 添加书籍
	 * @param aid 作者ID
	 * @param tid 书籍类型ID
	 * @param name 书籍名称
	 * @param isfinish 是否完本
	 */
	@Override
	public int insertBook(String aid, String tid, String name, String isfinish) {
		final String authorId = aid; 
		final String typeId = tid; 
		final String bookId = name; 
		final String bookIsfinish = isfinish; 
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement pre = con.prepareStatement(StaticString.INSERT_BOOK,Statement.RETURN_GENERATED_KEYS);
				pre.setString(1, authorId);
				pre.setString(2, typeId);
				pre.setString(3, bookId);
				pre.setString(4, bookIsfinish);
				return pre;
			}
			
		}, keyHolder);
		
		int bid = keyHolder.getKey().intValue();
		return bid;
	}

	/**
	 * 书籍章节内容批量上传
	 * @param booklist 章节内容List
	 */
	@Override
	public void insertBookContent(List<Book> booklist) {
		final List<Book> books = booklist;
		jdbcTemplate.batchUpdate(StaticString.INSERT_BOOK_CONTENT, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				String bookId = books.get(i).getBookId();
				String nodeNumber = books.get(i).getNodeNumber();
				String nodeName = books.get(i).getNodeName();
				String nodeContent = books.get(i).getNodeContent();
				ps.setString(1, bookId);
				ps.setString(2, nodeNumber);
				ps.setString(3, nodeName);
				ps.setString(4, nodeContent);
			}
			
			@Override
			public int getBatchSize() {
				return books.size();
			}
		});
	}

}
