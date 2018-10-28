package com.ycglj.manage.service;

import java.util.List;

import com.ycglj.manage.model.Photo;

public interface PhotoService {
	int insertPhtoByOpenId(Photo photo);
	
	List<Photo> getAllPhoto();
}
