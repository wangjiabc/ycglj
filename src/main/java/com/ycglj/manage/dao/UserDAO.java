package com.ycglj.manage.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ycglj.manage.daoModel.Crimal_Case;
import com.ycglj.manage.daoModel.FileSelfBelong;
import com.ycglj.manage.daoModel.Position;
import com.ycglj.manage.daoModel.PreMessage;
import com.ycglj.manage.daoModel.User_Data;
import com.ycglj.manage.daoModel.User_License;
import com.ycglj.manage.daoModel.Users;


public interface UserDAO {

	public Users getUser(Users users);
	
	public Integer insertUser(Users users,User_License user_License);
	
    public Integer updateUser(Users users);
   
    public Integer updateUserPhone(Users users,User_License user_License);
    
    public Map<String,Object> getAllUser(Integer limit, Integer offset, String sort,String order,Map<String, String> search);

    public Map<String,Object> getAllUserJoin(Integer limit, Integer offset, String sort,String order,Map<String, String> search);
    
    public Integer insertUserLicense(User_License user_License);
    
    public Integer updateUserLicense(User_License user_License);
    
	public Integer insertUserData(User_Data user_Data);
	
	//桌面后台调用
	public Integer insertUserData2(User_Data user_Data);
	
    public Integer updateUserData(User_Data user_Data);
    
    public Integer updateUserDataAffirm(String openId,List list);
    
    public Map<String,Object> getAllUserData(HttpServletRequest request,Integer limit, Integer offset, String sort,String order,Map<String, String> search);
    
	public Map<String, Object> findAllPreMessage(Integer limit, Integer offset, String sort,
			String order,Map search);
	
	public Integer insertPreMessage(PreMessage preMessage);

	public User_License getUserLicense(User_License user_License);
	
	public List getUserLicenseById(User_License user_License);
	
	public List getTest1();
	
	public List getTest2();
	
	public Integer insertOnlyUsers(Users users);
	
	public Integer updateOnlyUsers(Users users);
	
	public Integer updateOnlyUserLicense(User_License user_License);
	
	public Integer updateOnlyLicense(User_License user_License);
	
	public Integer updateOnlyUserData(User_Data user_Data);
	
	public Integer updateOnlyPosition(Position position);
	
	public Integer updateOnlyFlieSelf(FileSelfBelong fileSelfBelong);
	
	public Integer deleteUserData(String uri);
	
	public Integer deleteFileSelfBelong(String upFileFullName);
}
