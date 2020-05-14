package com.ycglj.manage.dao;

import com.ycglj.manage.model.Area2;


public interface AreaDAO {

	public Area2 queryArea(String code, String name, Integer limit, Integer page);
}
