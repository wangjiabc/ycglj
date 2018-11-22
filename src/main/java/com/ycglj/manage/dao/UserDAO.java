package com.ycglj.manage.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ycglj.manage.daoModel.PreMessage;
import com.ycglj.manage.daoModel.User_Data;
import com.ycglj.manage.daoModel.User_License;
import com.ycglj.manage.daoModel.Users;


public interface UserDAO {

	public Users getUser(Users users);
	
	public Integer insertUser(Users users);
	
    public Integer updateUser(Users users);
   
    public Map<String,Object> getAllUser(Integer limit, Integer offset, String sort,String order,Map<String, String> search);

    public Map<String,Object> getAllUserJoin(Integer limit, Integer offset, String sort,String order,Map<String, String> search);
    
    public Integer updateUserLicense(User_License user_License);
    
	public Integer insertUserData(User_Data user_Data);
	
    public Integer updateUserData(User_Data user_Data);
    
    public Integer updateUserDataAffirm(String openId,List list);
    
    public Map<String,Object> getAllUserData(HttpServletRequest request,Integer limit, Integer offset, String sort,String order,Map<String, String> search);
    
	public Map<String, Object> findAllPreMessage(Integer limit, Integer offset, String sort,
			String order,Map search);
	
	public Integer insertPreMessage(PreMessage preMessage);
    
	public User_License getUserLicense(User_License user_License);
	
	public List getUserLicenseById(User_License user_License);
}
