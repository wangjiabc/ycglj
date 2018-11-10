package com.ycglj.manage.mytask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.ycglj.manage.dao.OrderDAO;
import com.ycglj.manage.dao.UserDAO;
import com.ycglj.manage.daoModel.Order_User;
import com.ycglj.manage.daoModelJoin.User_Order_Join;
import com.ycglj.manage.service.SellerService;
import com.ycglj.sqlserver.context.Connect;

import common.HttpClient;


@Component("taskJob")  
public class TaskJob {  
	
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
	
    public void job1() {  
    	
    	System.out.println("taskjob!");
    	
    	Map searchMap=new HashMap<>();
		
    	Calendar cal=Calendar.getInstance();
    	
    	cal.add(Calendar.DATE,-1);
    	
    	Date date=cal.getTime();
			
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String d = sdf.format(date);

		searchMap.put("convert(varchar(11),sub_date,120 ) =", d);
		
		Map map=orderDao.getAllUser_Order_Join(10, 0, "", "", searchMap);
		
		List list=(List) map.get("data");
		
		Iterator iterator=list.iterator();
		
		if(iterator.hasNext()){
			
			User_Order_Join user_Order_Join=(User_Order_Join) iterator.next();
		
			if(user_Order_Join.getAuthentication()==3){
			
				Order_User order_User = new Order_User();

				order_User.setOverdue(1);
				order_User.setCancel(1);
				order_User.setCancel_date(date);

				if (user_Order_Join.getOverdue_number() == null || user_Order_Join.getOverdue_number() < 1) {
					order_User.setOverdue_number(1);
				} else {

					int overdueNumber = user_Order_Join.getOverdue_number() + 1;

					order_User.setOverdue_number(overdueNumber);
				}

				String[] where = { "open_id = ", user_Order_Join.getOpen_id() };

				order_User.setWhere(where);

				orderDao.updateOverdueNumber(order_User);
			
			}
			
		}
    	    
    }  
    
    
}  
