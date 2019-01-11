package com.tianlangcloud.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.tianlangcloud.common.Utils;
import com.tianlangcloud.dao.HandlerDao;

public class LoginInterceptor extends HandlerInterceptorAdapter{

	@Autowired
	private HandlerDao dao;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//获取请求的IP地址
		String requestIP = Utils.getIpAddress(request);
		System.out.println(requestIP);
		
		//获取请求路径
		String url = "http://" + request.getServerName()//服务器地址  
                    + ":"   
                    + request.getServerPort()           //端口号  
                    + request.getContextPath()      	//项目名称  
                    + request.getServletPath()      	//请求页面或其他地址  
                    + "?" + (request.getQueryString()); //参数 
		//请求白名单
		List<String> whiteList = new ArrayList<>();
		whiteList.add("http://127.0.0.1:80/?null");//登录页
		whiteList.add("http://www.tianlangcloud.com:80/?null");//登录页
		whiteList.add("http://127.0.0.1:443/?null");//登录页
		whiteList.add("http://www.tianlangcloud.com:443/?null");//登录页
		
		//在请求白名单中放行
		for (String w : whiteList) {
			if (url.equals(w)) {
				return true;
			}
		}
		
		//IP白名单
		List<Map<String, Object>> ipWhiteList = dao.getIpWhiteList();
		
		Boolean start = false;
		//比对请求的地址是否在白名单内
		for (Map<String, Object> map : ipWhiteList) {
			String IP = map.get("ip").toString();
			if (IP.equals(requestIP)) {
				start = true;
			}
		}
		
		//没在白名单的IP地址，所有的非客户端请求都返回到首页
		if (start) {
			return true;
		} else {
			response.sendRedirect("/");
			return false;
		}
		
	}
	
}
