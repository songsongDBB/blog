package com.dss.springboot.blog.vo;

/**
 * 返回给页面的对象，json数据格式
 * 
 * @author duan ss
 */
public class Response {

	private boolean success;
	private String message;
	private Object body;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public Response(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}

	public Response(boolean success, String message, Object body) {
		super();
		this.success = success;
		this.message = message;
		this.body = body;
	}

	public Response() {
		super();
	}

}
