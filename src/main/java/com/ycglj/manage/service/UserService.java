package com.ycglj.manage.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ycglj.manage.model.Access;
import com.ycglj.manage.model.Users;
import com.ycglj.weixin.base.SNSUserInfo;


public interface UserService {
	
	List<Users> getAllFullUser(Integer campusId,Integer limit, Integer offset, String sort,String order,String search);
	
	List<Users> getAllChartUser(Integer campusId,Integer limit, Integer offset, String sort,String order,String search);
	
	Integer getAllChartCount(Integer campusId,String search);
	
	List<Users> getWetchatAllUsers(Integer campusId,Integer place,Integer limit, Integer offset, String sort,String order);
	
	List<Users> getUserByPhone(Integer limit, Integer offset, String sort, String order);
	
	List<Users> getUserByGuidance();
	
	Users getUserInfoById(Integer campusId,String openId);
	
	Integer getUserCount(String campusAdmin,Integer campusId,String search);
	
	Integer getUserFullCount(Integer campusId,String search);
	
	Integer getOpenId(Integer campusId,String openId);
	
	Integer insertUser(SNSUserInfo snsUserInfo);
	
	Integer upUserByOpenId(SNSUserInfo snsUserInfo);
	
	Integer upsubscribeByOpenId(Map<String, Object> paramterMap);
	
	int upAtionFormatter(Map<String, Object> paramterMap);
	
	int selectRepeatUser(@Param(value="name") String name);
	
	int selectRepeatUserByOpenId(@Param(value="openId") String openId);
	
    int insertUsersInfo(Users users);
    
    int updateUsersInfo(Users users);
    
    Users getUserByOnlyOpenId(String openId);
       
    int insertAccess(Access access);
    
    Map selectAllAccess(Integer campusId,Integer limit, Integer offset, String sort,String order,String search,String page);
    
    Users getUserByAssetCharter(String charter, String phone);
    
    int getCountUserByOpenId(String openId);
}