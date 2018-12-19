package com.tianlangcloud.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tianlangcloud.common.ResultResponse;
import com.tianlangcloud.service.BookService;

@Controller
public class BookController {

	@Autowired
	private BookService ser;
	
	/**
	 * 前往书籍首页
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/client/book", method=RequestMethod.GET)
	public String book (HttpServletRequest request) {
		return "/book/book";
	}
	
	/**
	 * 前往书籍章节列表页
	 * @param request
	 * @param value 书籍ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/client/book/bookNodeNames", method=RequestMethod.GET)
	public String bookNodeNames(HttpServletRequest request, Integer value) {
		if (value != null) {
			List<Map<String, Object>> bookNodeNames = null;
			Map<String, Object> book = null;
			
			try {
				Map<String, Object> map = ser.bookNodeNames(value);
				book = (Map<String, Object>) map.get("model");
				bookNodeNames = (List<Map<String, Object>>) map.get("list");
				if (bookNodeNames.size() != 0) {
					request.setAttribute("nodeMin",bookNodeNames.get(0).get("nodeNumber"));
					request.setAttribute("nodeMax",bookNodeNames.get(bookNodeNames.size() - 1).get("nodeNumber"));
				} else {
					return "redirect:/client/book";
				}
				
				request.setAttribute("book",book);
				request.setAttribute("bookNodeNames",bookNodeNames);
			} catch (Exception e) {
				e.printStackTrace();
				return "redirect:/client/node";
			}
		}
		return "/book/node";
	}
	
	/**
	 * 前往书籍章节内容页
	 * @param request
	 * @param value 章节ID
	 * @param nodeMax 章节最大ID
	 * @param nodeMin 章节最小ID
	 * @param valueBID 书籍ID
	 * @return
	 */
	@RequestMapping(value="/client/book/bookContent", method=RequestMethod.GET)
	public String bookContent(HttpServletRequest request, Integer value, Integer nodeMax, Integer nodeMin, Integer valueBID) {
		if (nodeMin <= value && value <= nodeMax) {
			Map<String, Object> bookContent = ser.bookContent(value, valueBID);
			if (bookContent == null) {
				return "redirect:/client/book/bookNodeNames?value="+valueBID;
			} else {
				request.setAttribute("bookContent", bookContent);
			}
			request.setAttribute("nodeMin", nodeMin);
			request.setAttribute("nodeMax", nodeMax);
			request.setAttribute("valueBID", valueBID);
		} else {
			return "redirect:/client/book/bookNodeNames?value="+valueBID;
		}
		return "/book/content";
	}
	
	/**
	 * 前往书籍管理页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/manage/book", method=RequestMethod.GET)
	public String book(HttpServletRequest request,String search, Integer pageNum, Integer pageSize){
		List<Map<String, Object>> list = ser.getBookList(search, pageNum, pageSize);
		request.setAttribute("books", list);
		return "/book/books";
	}
	
	/**
	 * 上传书籍前的业务处理
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/manage/book/uploadBookService", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> uploadBookService(){
		return ser.uploadBookService();
	}
	
	/**
	 * 书籍上传
	 * @param aid 作者ID
	 * @param tid 类型ID
	 * @param isfinish 是否完本
	 * @param bookName 书籍名称
	 * @param file 书籍文件
	 * @return
	 */
	@RequestMapping(value="/manage/book/uploadBook", method=RequestMethod.POST)
	@ResponseBody
	public ResultResponse uploadBook(String aid, String tid, String isfinish, 
			String bookName, MultipartFile file){
		ResultResponse result = null;
		if (!StringUtils.isNotBlank(aid)) {
			result = new ResultResponse(ResultResponse.FAIL, "书籍作者不能为空", null);
			return result;
		}
		if (!StringUtils.isNotBlank(tid)) {
			result = new ResultResponse(ResultResponse.FAIL, "书籍类型不能为空", null);
			return result;
		}
		if (!StringUtils.isNotBlank(isfinish)) {
			result = new ResultResponse(ResultResponse.FAIL, "书籍是否完结不能为空", null);
			return result;
		}
		if (!StringUtils.isNotBlank(bookName)) {
			result = new ResultResponse(ResultResponse.FAIL, "书籍名称不能为空", null);
			return result;
		}
		if (file.getSize() == 0) {
			result = new ResultResponse(ResultResponse.FAIL, "书籍文件不能为空", null);
			return result;
		}
		
		bookName = "《" + bookName + "》";
		
		Boolean start = false;
		try {
			start = ser.uploadBook(aid, tid, isfinish, bookName, file.getInputStream());
			if (start) {
				result = new ResultResponse(ResultResponse.SUCCESS, ResultResponse.REQUEST_SUCCESS, null);
			} else {
				result = new ResultResponse(ResultResponse.FAIL, ResultResponse.REQUEST_FAIL, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new ResultResponse(ResultResponse.FAIL, ResultResponse.REQUEST_FAIL, null);
		}
		
		return result;
	}
}
