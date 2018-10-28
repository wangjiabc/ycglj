package com.ycglj.manage.mapper;

import java.util.List;

import com.ycglj.manage.model.Photo;

public interface PhotoMapper {

	int insertPhtoByOpenId(Photo photo);
	
	List<Photo> getAllPhoto();
}
