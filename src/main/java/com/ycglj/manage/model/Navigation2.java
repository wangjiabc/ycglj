package com.ycglj.manage.model;

import java.util.List;

public class Navigation2 {
	/**
	 * 状态码
	 */
	private String code;
	
	private List<Navigation> data;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<Navigation> getData() {
		return data;
	}

	public void setData(List<Navigation> data) {
		this.data = data;
	}
}