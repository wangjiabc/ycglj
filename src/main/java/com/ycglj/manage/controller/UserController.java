package com.ycglj.manage.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ycglj.manage.dao.OrderDAO;
import com.ycglj.manage.dao.UserDAO;
import com.ycglj.manage.daoModel.Order_User;
import com.ycglj.manage.daoModel.Users;
import com.ycglj.manage.daoSQL.UpdateExe;
import com.ycglj.manage.service.SellerService;
import com.ycglj.sqlserver.context.Connect;

@Controller
@RequestMapping("/user")
public class UserController {

	private SellerService sellerService;
	
	public SellerService getSellerService() {
		return sellerService;
	}

	@Autowired
	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}
	
	ApplicationContext applicationContext=new Connect().get();
	
	UserDAO userDao=(UserDAO) applicationContext.getBean("userdao");
	
	OrderDAO orderDao=(OrderDAO) applicationContext.getBean("orderdao");
	
	@RequestMapping("getAllUser")
	public @ResponseBody Map<String, Object> getAllUser(@RequestParam Integer limit,@RequestParam Integer page,String sort,String order,
			String search,String authentication,HttpServletRequest request){
			
			Map searchMap=new HashMap<>();
			
			if(search!=null&&!search.trim().equals("")){
				search="%"+search+"%";  
				searchMap.put("name like ", search);
			}		
			
			if(authentication!=null&&!authentication.equals("")){
				searchMap.put("authentication = ", authentication);
			}	
			
			int offset=(page-1)*limit;
			
			return userDao.getAllUser(limit, offset, sort,order, searchMap);
				
	}
	
	@RequestMapping("updateUser")
	public @ResponseBody Integer updateUser(@RequestParam String openId,Integer authentication,
			HttpServletRequest request) {
		// TODO Auto-generated method stub

		Users users = new Users();

		String[] where = { "open_id = ", openId };

		users.setWhere(where);
		users.setAuthentication(authentication);
		users.setUp_date(new Date());

		return userDao.updateUser(users);
		
	}
	
	
	@RequestMapping("getUserDateById")
	public @ResponseBody Map<String, Object> getUserDateById(@RequestParam String openId,HttpServletRequest request) {
						
		Order_User order_User=new Order_User();

		Map searchMap=new HashMap<>();
		
		searchMap.put("open_id = ", openId);
		searchMap.put("currently = ", "1");
		
		return userDao.getAllUserData(request,1000, 0, "","", searchMap);
				
	}
	
	
	@RequestMapping("setAuthentication")
	public @ResponseBody Integer setAuthentication(@RequestParam String openId,
			@RequestParam  Integer authentication,HttpServletRequest request) {
					
		Users users=new Users();
		
		users.setAuthentication(authentication);
        users.setAuthen_date(new Date());
		
		String[] where={"open_id = ", openId};
		
		users.setWhere(where);
		
		return userDao.updateUser(users);
	
	}
	
	
}
