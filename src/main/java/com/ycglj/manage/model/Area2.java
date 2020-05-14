package com.ycglj.manage.model;

import java.util.List;

public class Area2 {
	/**
	 * 状态码
	 */
	private String code;
	
	/**
	 * 查询数据
	 */
    private List<Area> data;
    
    /**
            *总数
     */
    private int count;
    
    public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
	public List<Area> getData() {
		return data;
	}
	public void setData(List<Area> data) {
		this.data = data;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
    
}