package com.tianlangcloud.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tianlangcloud.common.StaticString;
import com.tianlangcloud.common.Utils;
import com.tianlangcloud.model.Know;
import com.tianlangcloud.model.PageSerch;
import com.tianlangcloud.service.KnowService;

@Controller
public class KnowController {
	
	@Autowired
	private KnowService ser;
	
	/**
	 * 客户端 前往“知道”首页
	 * @return
	 */
	@RequestMapping(value = "/client/know", method = RequestMethod.GET)
	public String knowIndex (HttpServletRequest request, PageSerch pageSerch) {
		List<Know> list = new ArrayList<>();
		pageSerch = Utils.pageUtil(pageSerch, request);
		try {
			list = ser.esSelectKnowAllSortByTime(pageSerch);
			request.setAttribute("list", list);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("list", list);
			return "/know/know";
		}
		return "/know/know";
	}
	
	/**
	 * 根据id前往know详情页面
	 * @param request
	 * @param keyWord
	 * @return
	 */
	@RequestMapping(value="/client/know/content", method = RequestMethod.GET)
	public String page (HttpServletRequest request, Integer id) {
		Know know = null;
		try {
			know = ser.msSelectKnowById(id);
			request.setAttribute("know", know);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("know", know);
			return "/know/content";
		}
		return "/know/content";
	}
	
	/**
	 * 前往“知道”列表页
	 * @return
	 */
	@RequestMapping(value = "/manage/know/{type}", method = RequestMethod.GET)
	public String es_know (@PathVariable String type, HttpServletRequest request) {
		List<Know> list = new ArrayList<>();
		String title = "";
		if (StringUtils.isNotBlank(type)) {
			try {
				if (type.equals("ElasticSearch")) {
					list = ser.esSelectKnowAll();
					title = type;
				} else if (type.equals("MySql")) {
					list = ser.msSelectKnowAll();
					title = type;
				} else {
					title = StaticString.REQUEST_FORMAT_ERROE;
				}
			} catch (Exception e) {
				e.printStackTrace();
				title = StaticString.REQUEST_FORMAT_ERROE;
			}
		} else {
			title = StaticString.REQUEST_FORMAT_ERROE;
		}
		
		for (int i = 0; i < list.size(); i++) {
			String content = list.get(i).getContent();
			content = Utils.filterString(content);
			if (content.length() >= 10) {
				content = content.substring(0,10);
			}
			list.get(i).setContent(content);
		}
		request.setAttribute("title", title);
		request.setAttribute("knows", list);
		return "/know/knows";
	}
	
	/**
	 * 前往“知道”上传页
	 * @return
	 */
	@RequestMapping(value = "/manage/know/into", method = RequestMethod.GET)
	public String into_know (HttpServletRequest request, String start) {
		if (StringUtils.isNotBlank(start)) {
			request.setAttribute("start", start);
		} else {
			request.setAttribute("start", "0");
		}
		return "/know/knowsAdd";
	}
	
	/**
	 * 前往“知道”上传页
	 * @return
	 */
	@RequestMapping(value = "/manage/know", method = RequestMethod.POST)
	public String emInsertKnow (Know know) {
		String start = "2";
		if (StringUtils.isNotBlank(know.getUid())
				&& StringUtils.isNotBlank(know.getTitle())
				&& StringUtils.isNotBlank(know.getContent())
				) {
			
			try {
				ser.emInsertKnow(know);
				start = "1";
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return "redirect:/manage/know/into?start="+start;
	}
	
	/**
	 * 同步删除“知道”
	 * @return
	 */
	@RequestMapping(value = "/manage/know/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public String emDeleteKnow (@PathVariable String id) {
		if (StringUtils.isNotBlank(id) && Utils.filterNumber(id)) {
			try {
				ser.emDeleteKnowById(id);
				return StaticString.DELECT_SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
				return StaticString.DELECT_FAIL;
			}
		} else {
			return StaticString.REQUEST_PARAM_ERROE + StaticString.DELECT_FAIL;
		}
	}

}
