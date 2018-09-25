package com.dss.springboot.blog.vo;

import java.io.Serializable;

/**
 * tag的值对象类，
 * 
 * @author duan ss
 *
 */
public class TagVO implements Serializable {

	private static final long serialVersionUID = 2349368698891494101L;

	private String name;
	private Long count;

	public TagVO() {
		super();
	}

	public TagVO(String name, Long count) {
		super();
		this.name = name;
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "TagVO [name=" + name + ", count=" + count + "]";
	}

}
