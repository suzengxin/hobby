package com.tianlangcloud.model;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

public class PageSerch implements Serializable{

	private static final long serialVersionUID = 1L;

	public String type;//搜索类型
	
	public String keyWord;//关键字
	
	public Integer pageNumber;//请求页
	
	public Integer pageSize;//每页记录数
	
	public HttpServletRequest request;

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public PageSerch() {
		super();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return "PageSerch [type=" + type + ", keyWord=" + keyWord + ", pageNumber=" + pageNumber + ", pageSize="
				+ pageSize + "]";
	}
	
}
