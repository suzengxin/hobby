package com.tianlangcloud.model;

public class Book {

	public String authorId;
	
	public String typeid;
	
	public String bookId;
	
	public String nodeNumber;
	
	public String nodeName;
	
	public String nodeContent;

	public Book() {
		super();
	}

	public Book(String authorId, String typeid, String bookId, String nodeNumber, String nodeName, String nodeContent) {
		super();
		this.authorId = authorId;
		this.typeid = typeid;
		this.bookId = bookId;
		this.nodeNumber = nodeNumber;
		this.nodeName = nodeName;
		this.nodeContent = nodeContent;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getNodeNumber() {
		return nodeNumber;
	}

	public void setNodeNumber(String nodeNumber) {
		this.nodeNumber = nodeNumber;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeContent() {
		return nodeContent;
	}

	public void setNodeContent(String nodeContent) {
		this.nodeContent = nodeContent;
	}

	@Override
	public String toString() {
		return "Book [authorId=" + authorId + ", typeid=" + typeid + ", bookId=" + bookId + ", nodeNumber=" + nodeNumber
				+ ", nodeName=" + nodeName + ", nodeContent=" + nodeContent + "]";
	}
}
