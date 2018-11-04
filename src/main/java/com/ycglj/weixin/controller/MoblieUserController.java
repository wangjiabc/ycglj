package com.ycglj.weixin.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.ycglj.manage.daoModel.Users;
import com.ycglj.manage.service.SellerService;
import com.ycglj.sqlserver.context.Connect;

@Controller
@RequestMapping("/mobile/user")
public class MoblieUserController {

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
	
	
	@RequestMapping("getUserAuthen")
	public @ResponseBody Integer getUserAuthen(HttpServletRequest request){
		
		HttpSession session = request.getSession();
		
		String openId=session.getAttribute("openId").toString();
	
		Users users=new Users();
		users.setLimit(1);
		users.setOffset(0);
		users.setNotIn("id");
		
		String[] where={"open_id = ",openId};
		users.setWhere(where);
		
		users=userDao.getUser(users);
		
		if(users!=null){
			return users.getAuthentication();
		}else{
			return 0;
		}
		
	}
	
}
