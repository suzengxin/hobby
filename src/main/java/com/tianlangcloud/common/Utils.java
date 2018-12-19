package com.tianlangcloud.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.tianlangcloud.model.PageSerch;

public class Utils {

	/** 
	 * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址, 
	 *  
	 * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？ 
	 * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。 
	 *  
	 * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130, 
	 * 192.168.1.100 
	 *  
	 * 用户真实IP为： 192.168.1.110 
	 *  
	 * @param request 
	 * @return 
	 **/  
	public static String getIpAddress(HttpServletRequest request) {  
		String ip = request.getHeader("x-forwarded-for");  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getHeader("Proxy-Client-IP");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getHeader("WL-Proxy-Client-IP");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getHeader("HTTP_CLIENT_IP");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getRemoteAddr();  
		}  
		return ip;  
	} 
	
	/**
	 * map value : uri, url, ip, params, host, port!
	 * uri : 返回请求行中的资源名称
	 * url : 获得客户端发送请求的完整url
	 * ip : 返回发出请求的IP地址
	 * params : 返回请求行中的参数部分
	 * host : 返回发出请求的客户机的主机名
	 * port : 返回发出请求的客户机的端口号。
	 * @param request
	 */
	public static Map<String, String> getUrlAddress(HttpServletRequest request) {
	    String uri = request.getRequestURI();//返回请求行中的资源名称
	    String url = request.getRequestURL().toString();//获得客户端发送请求的完整url
	    String ip = request.getRemoteAddr();//返回发出请求的IP地址
	    String params = request.getQueryString();//返回请求行中的参数部分
	    String host = request.getRemoteHost();//返回发出请求的客户机的主机名
	    String port = String.valueOf(request.getRemotePort());//返回发出请求的客户机的端口号。
		Map<String, String> map = new HashMap<>();
		map.put("uri", uri);
		map.put("url", url);
		map.put("ip", ip);
		map.put("params", params);
		map.put("host", host);
		map.put("port", port);
	    return map;
	}
	
	/**
	 * 清空字符串中所有的html格式,空格,回车,换行符,制表符
	 * @param string
	 * @return
	 */
	public static String filterString (String string) {
		string = string.replaceAll("</?[^>]+>", ""); //剔出<html>的标签
		string = string.replaceAll("\\s*", "");//剔出所有空格
		string = string.replaceAll("\\s*|\t|\r|\n", "");//去除字符串中的空格,回车,换行符,制表符
		return string;
	}
	
	/**
	 * 判断字符串中是否是纯数字
	 * 是返回true
	 * 否返回false
	 * @param string
	 * @return
	 */
	public static Boolean filterNumber (String string) {
		boolean b = string.matches("^[0-9]+$");
		return b;
	}
	
	/**
	 * 高亮字符串中的关键字
	 * @param KeyWords 关键字
	 * @param content 需要高亮的字符串
	 * @return
	 */
	public static String highLightKeyWords (String keyWords, String content) {
		//将字符串放入StringBuffer中
		StringBuffer contentBuffer = new StringBuffer(content);
		
		//循环关键字
		for (int i = 0; i < keyWords.length(); i++) {
			char c = keyWords.charAt(i);
			
			if (String.valueOf(c).equals("/")
					|| String.valueOf(c).equals("<")
					|| String.valueOf(c).equals(">")) {
				continue;
			} else {
				for (int n = 0; n < contentBuffer.length(); n++) {
					char c1 = contentBuffer.charAt(n);
					Boolean start = true;
					if (n != 0 && n != contentBuffer.length() - 1) {
						if (String.valueOf(c).equals("e")) {
							char c_prev = contentBuffer.charAt(n - 1);
							char c_next = contentBuffer.charAt(n + 1);
							if (String.valueOf(c_prev).equals("<")
									|| String.valueOf(c_prev).equals("/")) {
								if (String.valueOf(c_next).equals("m")) {
									start = false;
								}
							}
						}
						if (String.valueOf(c).equals("m")) {
							char c_prev = contentBuffer.charAt(n - 1);
							char c_next = contentBuffer.charAt(n + 1);
							if (String.valueOf(c_prev).equals("e")
									&& String.valueOf(c_next).equals(">")) {
								start = false;
							}
						}
					}
					
					if (start) {
						if (String.valueOf(c).equals(String.valueOf(c1))) {
							contentBuffer.insert(n, "<em>");
							contentBuffer.insert(n + 5, "</em>");
							n += 10;
						}
					}
				}
			}
			
		}
		
		return contentBuffer.toString();
	}
	
	/**
	 * 分页处理
	 * @param pageSerch
	 * @param request
	 * @return
	 */
	public static PageSerch pageUtil(PageSerch pageSerch, HttpServletRequest request){
		if (pageSerch.getPageNumber() == null) {
			pageSerch.setPageNumber(StaticString.PAGENUMBER);
		}
		if (pageSerch.getPageNumber() != null) {
			if (pageSerch.getPageNumber() == 0) {
				pageSerch.setPageNumber(StaticString.PAGENUMBER);
			}
			if (pageSerch.getPageNumber() > 0) {
				pageSerch.setPageNumber(pageSerch.getPageNumber() - 1);
			} else {
				pageSerch.setPageNumber(StaticString.PAGENUMBER);
			}
		}
		pageSerch.setPageSize(StaticString.PAGESIZE);
		pageSerch.setRequest(request);
		return pageSerch;
	}
	
	/**
	 * 解析文本流
	 * 转化为章节List和内容List
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, List<String>> BookUploadParseText (InputStream inputStream) throws Exception{
		List<String> nodeNames = new ArrayList<>();//章节集合
		List<String> contents = new ArrayList<>();//每一行内容集合
		Map<String, List<String>> map = new HashMap<>();
		//获取章节名称
		String encoding = "UTF-8";
		InputStreamReader is = new InputStreamReader(inputStream, encoding);
		BufferedReader br = new BufferedReader(is);
		
		//解析流，将章节名称放入章节集合    //nodeNames
		//将文件流以行的格式存入集合  //contents
		String line = null;
		while ((line = br.readLine()) != null) {
			contents.add(line);
			if (line.indexOf("第") == 0 && line.indexOf("章") != -1) {
				nodeNames.add(line);
			}
		}
		map.put("nodeNames", nodeNames);
		map.put("contents", contents);
		return map;
	}
	
}
