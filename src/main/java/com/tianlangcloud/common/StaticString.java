package com.tianlangcloud.common;

public class StaticString {
	
	/** 其他静态字符串  **/
	public static final int PAGENUMBER = 0;
	
	public static final int PAGESIZE = 10;
	
	public static String INSERT_SUCCESS = "添加成功！";
	
	public static String INSERT_FAIL = "添加失败！";
	
	public static String DELECT_SUCCESS = "删除成功！";
	
	public static String DELECT_FAIL = "删除失败！";
	
	public static String UPDATE_SUCCESS = "修改成功！";
	
	public static String UPDATE_FAIL = "修改失败";

	public static String REQUEST_FORMAT_ERROE = "请求格式不正确！";
	
	public static String REQUEST_PARAM_ERROE = "请求参数不正确！";
	
	/** SQL 语句  **/

	/**
	 *	添加SQL 
	 */
	//添加知道
	public static String INSERT_KNOW = "INSERT INTO know (uid, title, content) VALUES (?, ?, ?);";
	//添加书籍
	public static String INSERT_BOOK = "INSERT INTO book (author_id, type_id, book_name, isfinish) VALUES (?,?,?,?)";
	//添加章节内容
	public static String INSERT_BOOK_CONTENT = "INSERT INTO book_content (bookId, nodeNumber, nodeName, nodeContent) VALUES (?,?,?,?)";
	
	/**
	 *	删除SQL
	 **/
	public static String DELETE_KNOW_BY_ID = "DELETE FROM know WHERE id = ?;";
	
	/**
	 *	修改SQL
	 **/
	
	/**
	 *	查询SQL 
	 **/
	//获取IP白名单
	public static String SELECT_IP_WHITE_ROLL = "SELECT ip FROM ip_white_roll";
	//查询所有知道
	public static String SELECT_KNOW = "SELECT id, uid, title, content, create_time as createTime, update_time as updateTime FROM know;";
	//根据ID查询知道
	public static String SELECT_KNOW_BY_ID = "SELECT * FROM know WHERE id = ?;";
	//获取章节列表//参数:书籍ID
	public static String SELECT_BOOK_NODE_NAMES = "SELECT nodeNumber,nodeName FROM book_content WHERE bookId = ?";
	//获取章节内容//参数:章节ID
	public static String SELECT_BOOK_CONTENT = "SELECT nodeNumber,nodeName,nodeContent FROM book_content WHERE nodeNumber = ? AND bookId = ?";
	//获取账号内容//参数:username
	public static String SELECT_USER_BY_USERNAME = "SELECT u.password FROM user AS u WHERE u.username = ?";
	//获取所有书籍
	public static String SELECT_BOOK_LIST = "SELECT b.id AS bid,a.id AS aid,t.id AS tid,b.book_name,a.name,t.type_name,b.isfinish FROM book b LEFT JOIN author a ON a.id = b.author_id LEFT JOIN book_type t ON t.id = b.type_id";
	//获取所有作者
	public static String SELECT_AUTHOR_LIST = "SELECT a.id AS aid,a.name FROM author a";
	//获取所有书籍类型
	public static String SELECT_BOOK_TYPE_LIST = "SELECT t.id AS tid,t.type_name AS typeName FROM book_type t";
	//根据书籍ID获取书籍详情
	public static String SELECT_BOOK_BY_ID = "SELECT * FROM book WHERE id = ?";
	//根据作家名称或书籍名称查询书籍
	public static String SELECT_BOOK_BY_KEYWORD = "SELECT b.id,b.book_name,a.`name` FROM book b LEFT JOIN author a ON b.author_id = a.id WHERE a.`name` LIKE '%?%' OR b.book_name LIKE '%?%' LIMIT ?,?";
}
