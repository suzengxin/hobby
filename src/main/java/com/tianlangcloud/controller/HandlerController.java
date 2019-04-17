package com.tianlangcloud.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tianlangcloud.common.Utils;
import com.tianlangcloud.model.Know;
import com.tianlangcloud.model.PageSerch;
import com.tianlangcloud.service.BookService;
import com.tianlangcloud.service.KnowService;

@Controller
public class HandlerController {
	
	@Autowired
	private KnowService knowService;
	@Autowired
	private BookService bookService;
	
	/**
	 * 前往客户端首页
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index (){
		return "redirect:/client/index";
	}
	@RequestMapping(value = "/client/index", method = RequestMethod.GET)
	public String clientIndex (){
		return "/index/client";
	}
	
	/**
	 * 前往后台管理首页
	 * @return
	 */
	@RequestMapping(value = "/manage/index", method = RequestMethod.GET)
	public String manageIndex (){
		return "/index/manage";
	}
	
	/**
	 * 搜索功能
	 * @param request
	 * @param type
	 * @param keyWord
	 * @return
	 */
	@RequestMapping(value="/client/search", method = RequestMethod.GET)
	public String search (HttpServletRequest request, PageSerch pageSerch) {
		String keyWord = pageSerch.getKeyWord();
		if (StringUtils.isNotBlank(keyWord)) {
			keyWord = Utils.filterString(keyWord);
			pageSerch.setKeyWord(keyWord);
		}
		if (StringUtils.isNotBlank(pageSerch.getType()) && StringUtils.isNotBlank(pageSerch.getKeyWord())) {
			request.setAttribute("type", pageSerch.getType());
			request.setAttribute("keyWord", pageSerch.getKeyWord());
			pageSerch = Utils.pageUtil(pageSerch, request);
			try {
				if (pageSerch.getType().equals("know")) {
					List<Know> list = knowService.searchKeyWord(pageSerch);
					request.setAttribute("list", list);
					return "/know/search";
				} else if (pageSerch.getType().equals("book")) {
					List<Map<String, Object>> list = bookService.getBookByValue(pageSerch);
					request.setAttribute("list", list);
					return "/book/search";
				} else {
					return "redirect:/client/index";
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "redirect:/client/index";
			} 
		}
		
		if (StringUtils.isNotBlank(pageSerch.getType())) {
			if (pageSerch.getType().equals("know")) {
				return "redirect:/client/know";
			} else if (pageSerch.getType().equals("book")) {
				return "redirect:/client/book";
			} else {
				return "redirect:/client/index";
			} 
		} else {
			return "redirect:/client/index";
		}
	}
	
	/**
	 * 修改主题
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/client/changeTheme", method=RequestMethod.GET)
	public String changeTheme(HttpServletRequest request, HttpServletResponse response){
		//获取cookies
		String theme = "white";
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("theme")) {
					theme = cookie.getValue();
				}
			} 
		}
		Cookie cookie = new Cookie("theme", theme);
		cookie.setMaxAge(2000000000);
		response.addCookie(cookie);
		return "/index/theme";
	}
	
	/**
	 * 修改主题
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/client/message", method=RequestMethod.GET)
	public String clientMessage(HttpServletRequest request, String start){
		if (StringUtils.isNotBlank(start)) {
			request.setAttribute("start", start);
		} else {
			request.setAttribute("start", "0");
		}
		return "/know/message";
	}
	
}
