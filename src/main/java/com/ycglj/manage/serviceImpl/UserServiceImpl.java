package com.ycglj.manage.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ycglj.manage.mapper.AccessMapper;
import com.ycglj.manage.mapper.UsersMapper;
import com.ycglj.manage.model.Access;
import com.ycglj.manage.model.Users;
import com.ycglj.manage.service.UserService;
import com.ycglj.weixin.base.SNSUserInfo;


@Service("userService")
public class UserServiceImpl implements UserService {
	private UsersMapper usersMapper;         //操作用户信息
	
	private AccessMapper accessMapper;
	
	@Autowired
	public void setUsersMapper(UsersMapper usersMapper) {
		this.usersMapper = usersMapper;
	}

	
	@Autowired
	public void setAccessMapper(AccessMapper accessMapper) {
		this.accessMapper = accessMapper;
	}
	
	public List<Users> getAllFullUser(Integer campusId,Integer limit, Integer offset, String sort,
			String order,Integer place,String search) {
		return usersMapper.getAllFullUser(campusId, limit, offset, sort, order, place, search);
	}
	
	public Integer getUserCount(String campusAdmin ,Integer campusId,String search) {
		return usersMapper.getUserCount(campusAdmin,campusId,search);
	}
	
	public List<Users> getAllChartUser(Integer campusId,Integer limit, Integer offset, String sort,
			String order,String search) {
		return usersMapper.getAllChartUser(campusId,limit,offset,sort,order,search);
	}
	
	@Override
	public Integer getAllChartCount(Integer campusId, String search) {
		// TODO Auto-generated method stub
		return usersMapper.getAllChartCount(campusId, search);
	}
	


	public Integer getUserFullCount(Integer campusId,Integer place,String search) {
		return usersMapper.getUserFullCount(campusId,place,search);
	}



	@Override
	public Integer getOpenId(Integer campusId, String openId) {
		// TODO Auto-generated method stub
		return usersMapper.getOpenId(campusId, openId);
	}

	@Override
	public Integer insertUser(SNSUserInfo snsUserInfo) {
		// TODO Auto-generated method stub
		return usersMapper.insertUserInfo(snsUserInfo);
	}

	@Override
	public Users getUserInfoById(Integer campusId, String openId) {
		// TODO Auto-generated method stub
		return usersMapper.getUserByOpenId(campusId, openId);
	}

	@Override
	public Integer upUserByOpenId(SNSUserInfo snsUserInfo) {
		// TODO Auto-generated method stub
		return usersMapper.upUserByOpenId(snsUserInfo);
	}

	@Override
	public Integer upsubscribeByOpenId(Map<String, Object> paramterMap) {
		// TODO Auto-generated method stub
		return usersMapper.upsubscribeByOpenId(paramterMap);
	}

	@Override
	public int upAtionFormatter(Map<String, Object> paramterMap) {
		// TODO Auto-generated method stub
		return usersMapper.upAtionFormatter(paramterMap);
	}

	@Override
	public int upAtionTransact(Map<String, Object> paramterMap) {
		// TODO Auto-generated method stub
		return usersMapper.upAtionTransact(paramterMap);
	}
	
	@Override
	public int upAtionArea(Map<String, Object> paramterMap) {
		// TODO Auto-generated method stub
		return usersMapper.upAtionArea(paramterMap);
	}
	
	@Override
	public int upAtionBusiness(Map<String, Object> paramterMap) {
		// TODO Auto-generated method stub
		return usersMapper.upAtionBusiness(paramterMap);
	}
	
	@Override
	public int selectRepeatUser(String name) {
		// TODO Auto-generated method stub
		return usersMapper.selectRepeatUser(name);
	}

	@Override
	public int selectRepeatUserByOpenId(String openId) {
		// TODO Auto-generated method stub
		return usersMapper.selectRepeatUserByOpenId(openId);
	}

	@Override
	public int insertUsersInfo(Users users) {
		// TODO Auto-generated method stub
		return usersMapper.insertUsersInfo(users);
	}

	@Override
	public int updateUsersInfo(Users users) {
		// TODO Auto-generated method stub
		return usersMapper.updateUsersInfo(users);
	}

	@Override
	public Users getUserByOnlyOpenId(String openId) {
		// TODO Auto-generated method stub
		return usersMapper.getUserByOnlyOpenId(openId);
	}


	@Override
	public List<Users> getUserByPhone(Integer limit, Integer offset, String sort, String order) {
		// TODO Auto-generated method stub
		return usersMapper.getUserByPhone(limit, offset, sort, order);
	}

	@Override
	public List<Users> getUserByGuidance() {
		// TODO Auto-generated method stub
		return usersMapper.getUserByGuidance();
	}

	@Override
	public List<Users> getUserByTransact() {
		// TODO Auto-generated method stub
		return usersMapper.getUserByTransact();
	}

	
	@Override
	public List<Users> getWetchatAllUsers(Integer campusId,Integer place,Integer limit,Integer offset, String sort,String order) {
		// TODO Auto-generated method stub
		return usersMapper.getWetchatAllUsers(campusId, place,limit,offset,sort,order);
	}

	@Override
	public int insertAccess(Access access) {
		// TODO Auto-generated method stub
		return accessMapper.insert(access);
	}

	@Override
	public Map selectAllAccess(Integer campusId, Integer limit, Integer offset, String sort, String order,
			String search,String page) {
		// TODO Auto-generated method stub
		
		if(sort!=null&&sort.equals("accessTime")){
			sort="access_time";
		}else{
			sort="access_time";
		}
		
		if(order!=null&&order.equals("asc")){
			order="asc";
		}
		
		if(order!=null&&order.equals("desc")){
			order="desc";
		}
		
		List list=accessMapper.selectAllAccess(campusId, limit, offset, sort, order, search,page);
		
		int total=accessMapper.selectCountAccess(campusId, search,page);
	    
		Map map=new HashMap<>();
		
		map.put("rows", list);
		
		map.put("total", total);
		
		return map;
	}

	@Override
	public Users getUserByAssetCharter(String charter, String phone) {
		// TODO Auto-generated method stub
		
		return usersMapper.getUserByAssetCharter(charter, phone);
		
	}


	@Override
	public int getCountUserByOpenId(String openId) {
		// TODO Auto-generated method stub
		return usersMapper.getCountUserByOpenId(openId);
	}


}
