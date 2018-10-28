package com.ycglj.manage.dao;

import java.util.List;
import java.util.Map;

import com.ycglj.manage.daoModel.PreMessage;
import com.ycglj.manage.daoModel.User_Data;
import com.ycglj.manage.daoModel.Users;


public interface UserDAO {

	public Integer insertUser(Users users);
	
    public Integer updateUser(Users users);
   
    public Map<String,Object> getAllUser(Integer limit, Integer offset, String sort,String order,Map<String, String> search);

	public Integer insertUserData(User_Data user_Data);
	
    public Integer updateUserData(User_Data user_Data);
    
    public Integer updateUserDataAffirm(List list);
    
    public Map<String,Object> getAllUserData(Integer limit, Integer offset, String sort,Map<String, String> search);
    
	public Map<String, Object> findAllPreMessage(Integer limit, Integer offset, String sort,
			String order,Map search);
	
	public Integer insertPreMessage(PreMessage preMessage);
    
}
