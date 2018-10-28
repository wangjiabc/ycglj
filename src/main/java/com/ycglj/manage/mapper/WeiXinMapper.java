package com.ycglj.manage.mapper;

import java.util.List;
import java.util.Map;

import com.ycglj.manage.model.WeiXin;

public interface WeiXinMapper {

	WeiXin getAccessToken(Integer campusId);
	
	List<WeiXin> getAllCampus(Integer cityId);
	
	WeiXin getCampus(Integer campusId);
	
	int insertCampus(Map<String, Object> paramMap);
	
	int updateCampus(Map<String, Object> paramMap);
	
	WeiXin getByCampusId(Integer campusId);
	
	int updateHomePageByCampusId(Map<String, Object> paramMap);
	
	Integer getCampusIdByUserName(String userName);
}
