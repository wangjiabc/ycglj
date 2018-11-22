package com.ycglj.weixin.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.ycglj.manage.daoModel.User_License;
import com.ycglj.manage.daoModel.Users;
import com.ycglj.manage.daoModelJoin.User_Order_Join;
import com.ycglj.manage.service.SellerService;
import com.ycglj.manage.tools.MyTestUtil;
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
	
		User_License user_License=new User_License();
		user_License.setLimit(1);
		user_License.setOffset(0);
		user_License.setNotIn("open_id");
		user_License.setOrder("desc");
		user_License.setSort("authentication");
		
		String[] where={"open_id = ",openId};
		user_License.setWhere(where);
		
		user_License=userDao.getUserLicense(user_License);
		
		if(user_License!=null){
			return user_License.getAuthentication();
		}else{
			return 0;
		}
		
	}
	
	@RequestMapping("getUserLicense")
	public @ResponseBody List getUserLicense(HttpServletRequest request){
		
		HttpSession session = request.getSession();
		
		String openId=session.getAttribute("openId").toString();
	
		User_License user_License=new User_License();
		user_License.setLimit(1000);
		user_License.setOffset(0);
		user_License.setNotIn("open_id");
		
		String[] where={"open_id = ",openId};
		user_License.setWhere(where);
		
		List list=userDao.getUserLicenseById(user_License);
	
		System.out.println("openId="+openId);
		
		MyTestUtil.print(list);
		
	    return list;
	}
	
	@RequestMapping("getUserData")
	public @ResponseBody Map<String, Object> getUserData(HttpServletRequest request){
			
			Map searchMap=new HashMap<>();
			
			HttpSession session = request.getSession();
			
			String openId=session.getAttribute("openId").toString();
			
			searchMap.put("[Users].open_id = ", openId);
			
			Map map=new HashMap<>();
			
			map=userDao.getAllUserJoin(100, 0, null,null, searchMap);
			
			List list=(List) map.get("data");
			
			try{
				
				searchMap=new HashMap<>();
				
				searchMap.put("open_id = ", openId);
				searchMap.put("currently = ", "1");
				
				Map map2=userDao.getAllUserData(request,1000, 0, "","", searchMap);
				
				map.put("imgData", map2);
				
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			return map;
			
	}
	
}
