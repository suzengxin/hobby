package com.tianlangcloud.service;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tianlangcloud.common.Utils;
import com.tianlangcloud.dao.BookDao;
import com.tianlangcloud.model.Book;
import com.tianlangcloud.model.PageSerch;

@Service
public class BookServiceImpl implements BookService{

	@Autowired
	BookDao dao;
	
	/**
	 * 根据作家名称或书籍名称查询书籍
	 * @param pageable
	 * @return
	 */
	public List<Map<String, Object>> getBookByValue(PageSerch pageSerch){
		List<Map<String, Object>> list = dao.getBookByValue(pageSerch);
		for (Map<String, Object> map : list) {
			String bookName = map.get("book_name").toString();
			String authorName = map.get("name").toString();
			bookName = Utils.highLightKeyWords(pageSerch.getKeyWord(), bookName);
			authorName = Utils.highLightKeyWords(pageSerch.getKeyWord(), authorName);
			map.put("book_name", bookName);
			map.put("name", authorName);
		}
		
		//封装到作用域
		int count = dao.getBookCountByValue(pageSerch);
		int total = count%pageSerch.getPageSize() == 0 ? (count/pageSerch.getPageSize()) : (count/pageSerch.getPageSize()+1);
		pageSerch.getRequest().setAttribute("total", total);
		pageSerch.getRequest().setAttribute("current", pageSerch.getPageNumber() + 1);
		pageSerch.getRequest().setAttribute("count", count);
		return list;
	}
	
	/**
	 * 前往章节列表页的业务逻辑
	 * @param request
	 * @param value 书籍ID
	 */
	@Override
	public Map<String, Object> bookNodeNames(Integer value) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<Map<String, Object>> bookNodeNames = dao.getBookNodeNames(value);
			Map<String, Object> book = dao.getBookById(value);
			map.put("model", book);
			map.put("list", bookNodeNames);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return map;
		}
	}

	/**
	 * 前往书籍章节内容页的业务逻辑
	 * @param request
	 * @param value 章节ID
	 */
	@Override
	public Map<String, Object> bookContent(Integer value, Integer valueBID) {
		try {
			Map<String, Object> bookContentMap = dao.getBookContent(value, valueBID);
			return bookContentMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取书籍列表
	 * @param pageNum 起始页数
	 * @param pageSize 每页记录数
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getBookList(String search, Integer pageNum, Integer pageSize) {
		return dao.getBookList(search, pageNum, pageSize);
	}

	/**
	 * 书籍上传前的业务处理
	 * @return
	 */
	@Override
	public Map<String, Object> uploadBookService() {
		try {
			Map<String, Object> map = new HashMap<>();
			List<Map<String, Object>> authotList = dao.getAuthotList();
			List<Map<String, Object>> bookTypeList = dao.getBookTypeList();
			map.put("authotList", authotList);
			map.put("bookTypeList", bookTypeList);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 书籍上传
	 * @param aid 作者ID
	 * @param tid 类型ID
	 * @param isfinish 是否完本
	 * @param bookName 书籍名称
	 * @param inputStream 书籍文件流
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean uploadBook(String aid, String tid, String isfinish, 
			String bookName, InputStream inputStream) {
		try{
			int bid = dao.insertBook(aid, tid, bookName, isfinish);
			
			Map<String, List<String>> map = Utils.BookUploadParseText(inputStream);
			List<Book> resultList = new ArrayList<>();//返回Book结果集
			
			List<String> nodeNames = map.get("nodeNames");//章节列表
			List<String> contents = map.get("contents");//全文列表
			
			SimpleDateFormat format = new SimpleDateFormat("hh:dd:ss");
			String s = format.format(new Date());//第一次解析结束时间
			//解析内容集合，提取出每章内容，转化为Book对象存入集合  //resultList
			int temp = 0;
			for (int i = 0; i < nodeNames.size(); i++) {
				//取出章节名称
				String key = nodeNames.get(i);
				//下一章节名称
				String end = null;
				
				//如果是最后一个章节，放入一个基本不重复的字符串
				if (i == nodeNames.size() - 1) {
					end = UUID.randomUUID().toString();
				} else {
					end = nodeNames.get(i+1);
				}
				
				boolean start = false;
				//本章节内容
				String contentStr = "";
				Book b = new Book();//书籍对象
				//循环所有行
				//行中包含章节名称的情况下，打开集合控制器
				//直到行中包含下一章节名称的情况下，关闭控制器
				//控制器判断过后再打开，下一个循环就会存入新的集合中
				for (int j = temp; j < contents.size(); j++) {
					String content = contents.get(j);
					if (content.equals(end)) {
						start = false;
						temp = j;
						break;
					}
					
					if (start) {
						String str = content.replaceAll("\\s*", "");
						if (!str.equals("")) {
							contentStr += "<p class=\"content\">" + str + "</p></br>";
						}
					}
					
					if (content.equals(key)) {
						start = true;
					}
				}
				b.setBookId(String.valueOf(bid));
				b.setNodeNumber(String.valueOf(i + 1));
				b.setNodeName(key);
				b.setNodeContent(contentStr);
				resultList.add(b);
				//System.out.println(key);
			}
			dao.insertBookContent(resultList);
			String e = format.format(new Date());//第二次解析结束时间
			System.out.println("解析开始时间："+s);
			System.out.println("解析结束时间："+e);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
}
