package com.ycglj.manage.dao;

import java.util.Map;

import com.ycglj.manage.daoModel.WeiXin_User;
import com.ycglj.manage.daoModel.Order_Date;
import com.ycglj.manage.daoModel.Order_User;

public interface OrderDAO {

	int insertOrderUser(Order_User order_User);
	
	int updateOrderUser(Order_User order_User);
	
	Map<String,Object> getAllOrderUser(Integer limit, Integer offset, String sort,String order,Map<String, String> search);
	
	int insertOrderDate(Order_Date order_Date);
	
	int updateOrderDate(Order_Date order_Date);
	
	Map<String,Object> getAllOrderDate(Integer limit, Integer offset, String sort,String order,Map<String, String> search);

	public Integer insertWeiXinUser(WeiXin_User weiXin_User);
	
	public Integer deleteWeiXinUser(WeiXin_User weiXin_User);
	
	public Integer updateWeiXinUser(WeiXin_User weiXin_User);
	
	public Integer selectCountWeiXinUser(WeiXin_User weiXin_User);
}
