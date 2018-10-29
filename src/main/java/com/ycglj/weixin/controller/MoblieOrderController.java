package com.ycglj.weixin.controller;

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
import com.ycglj.manage.daoModel.Order_Date;
import com.ycglj.manage.daoModel.Order_User;
import com.ycglj.manage.service.SellerService;
import com.ycglj.sqlserver.context.Connect;

@Controller
@RequestMapping("/mobile/order")
public class MoblieOrderController {

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
	
	@RequestMapping("getOrderDate")
	public @ResponseBody Map getOrderDate(@RequestParam String time,HttpServletRequest request){
		
		System.out.println("time="+time);
		
		Date date = null;
		DateFormat fmt =new SimpleDateFormat("yyyy-MM");
		
		try {
			date = fmt.parse(time);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		SimpleDateFormat sdfyyyy = new SimpleDateFormat("yyyy");
		
		SimpleDateFormat sdfmm = new SimpleDateFormat("MM");
		
		String year=sdfyyyy.format(date);
		
		String month=sdfmm.format(date);
		
		Calendar cal = Calendar.getInstance();  
		cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, 0, 0, 0, 0);  

		String startTime = null;
		
		String endTime=null;
		
		startTime=sdf.format(cal.getTime());
		
		cal.set(Integer.parseInt(year), Integer.parseInt(month), 0, 0, 0, 0);
        
        endTime=sdf.format(cal.getTime());

        Map searchMap=new HashMap<>();
        
        searchMap.put("convert(varchar(11),sub_date,120 ) >", startTime);
		searchMap.put("convert(varchar(11),sub_date,120 ) <=", endTime);
		
		System.out.println("startTime="+startTime);
		System.out.println("endTime="+endTime);
		
		Map map=orderDao.getAllOrderDate(1000, 0, null, null, searchMap);
		
		List dataList=(List) map.get("data");
		
		Iterator<Order_Date> iterator=dataList.iterator();
		
		List agree=new ArrayList<>();
		List disagree=new ArrayList<>();
		List order=new ArrayList<>();
		List sub_date=new ArrayList<>();
		List full=new ArrayList<>();
		
		while (iterator.hasNext()) {
			
			Order_Date order_Date=iterator.next();
			
			Date sDate=order_Date.getSub_date();
			
			String subDate=sdf.format(sDate);
			
			if(order_Date.getAgree()>0){
				agree.add(subDate);
			}
			if(order_Date.getDisagree()>0){
				disagree.add(subDate);
			}
			if(order_Date.getOrder_number()>0){
				Map orderMap=new HashMap<>();
				orderMap.put("datetime", subDate);
				orderMap.put("order_number", order_Date.getOrder_number());
				order.add(orderMap);
				if(order_Date.getOrder_number()>=30){
					full.add(subDate);
				}
			}
			
		}
		
		Map map2=new HashMap<>();
		
		map2.put("agree",agree);
		map2.put("disagree",disagree);
		map2.put("order",order);
		map2.put("sub_date",sub_date);
		map2.put("full",full);
						
		return map2;
		
	}
	
	
	@RequestMapping("insertOrder")
	public @ResponseBody Integer insertOrder(@RequestParam String time,HttpServletRequest request){
		
		Date subDate = null;
		DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			subDate = fmt.parse(time);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		HttpSession session = request.getSession();
		
		String openId=session.getAttribute("openId").toString();
		
		Order_User order_User=new Order_User();
		
		order_User.setOpen_id(openId);
		order_User.setSub_date(subDate);
		order_User.setCancel(0);
		order_User.setDate(new Date());
		
		return orderDao.insertOrderUser(order_User);
	}
	
	
	@RequestMapping("updateOrder")
	public @ResponseBody Integer updateOrder(@RequestParam String time,
			Integer cancel,HttpServletRequest request){
		
		Date subDate = null;
		DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			subDate = fmt.parse(time);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		HttpSession session = request.getSession();
		
		String openId=session.getAttribute("openId").toString();
		
		Order_User order_User=new Order_User();
		order_User.setOpen_id(openId);
		order_User.setSub_date(subDate);
		order_User.setDate(new Date());
		
		if(cancel!=null&&cancel==1){
			order_User.setCancel(cancel);
		}
		
		String[] where={"open_id=","0"};
		
		order_User.setWhere(where);
		
		return orderDao.updateOrderUser(order_User);
	}
	
}
