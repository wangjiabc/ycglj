package com.ycglj.manage.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ycglj.manage.daoModel.*;


public interface UserDAO {

	public Users getUser(Users users);
	
	public WeiXin_User getWeiXinUser(WeiXin_User weiXin_User);
	
	public Integer insertUser(Users users, User_License user_License);
	
    public Integer updateUser(Users users);
   
    public Integer updateUserPhone(Users users, User_License user_License, String user_license);
    
    public Map<String,Object> getAllUser(Integer limit, Integer offset, String sort, String order, Map<String, String> search);

    public Map<String,Object> getAllUserJoin(Integer limit, Integer offset, String sort, String order, Map<String, String> search);
    
    public Integer insertUserLicense(User_License user_License);
    
    public Integer updateUserLicense(User_License user_License);
    
	public Integer insertUserData(User_Data user_Data);
	
	//桌面后台调用
	public Integer insertUserData2(User_Data user_Data);
	
    public Integer updateUserData(User_Data user_Data);
    
    public Integer updateUserDataAffirm(String openId, List list, Integer authen_type);
    
    public Map<String,Object> getAllUserData(HttpServletRequest request, Integer limit, Integer offset, String sort, String order, Map<String, String> search);
    
	public Map<String, Object> findAllPreMessage(Integer limit, Integer offset, String sort,
                                                 String order, Map search);
	
	public Integer insertPreMessage(PreMessage preMessage);

	public User_License getUserLicense(User_License user_License);
	
	public List getUserLicenseJoin(User_License user_License); 
	
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
	
	public List getAllCheck_Person(Check_Person check_Person);

	public Integer insertTempUser(Temp_Users temp_Users);
	
	public Integer updateCheck_Person(Check_Person check_Person);
	
	public Integer insertCheck_Person(Check_Person check_Person);
	
	public Map<String,Object> getAllTempUserJoin(Integer limit, Integer offset, String sort, String order, Map<String, String> search);
    
	public Temp_Users getTempUsers(Temp_Users temp_Users);
	
	public Integer deleteTempUsers(Temp_Users temp_Users); 
	
	public Map<String,Object> getAllTempLicenseJoin(Integer limit, Integer offset, String sort, String order, Map<String, String> search);
    
	public Integer updateTempUsers(Temp_Users temp_Users);
	
	public User_Data getUserDataByTime(String openId, String data_type);
	
	public Map<String,Object> getAllTempUser(Integer limit, Integer offset, String sort, String order, Map<String, String> search, String openId,Integer area);

	Integer updateTempUserLicense(Temp_User_License temp_user_license);
}
