package com.tianlangcloud.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "es-know", type = "know", 
			shards = 2, replicas = 1, refreshInterval = "-1")
public class Know {

	@Id
	public String id;
	public String uid;
	public String title;
	public String content;
	public String createTime;
	public String updateTime;
	
	public Know() {
		super();
	}
	public Know(String id, String uid, String title, String content, String createTime, String updateTime) {
		super();
		this.id = id;
		this.uid = uid;
		this.title = title;
		this.content = content;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	@Override
	public String toString() {
		return "Know [id=" + id + ", uid=" + uid + ", title=" + title + ", content=" + content + ", createTime="
				+ createTime + ", updateTime=" + updateTime + "]";
	}
	
}
