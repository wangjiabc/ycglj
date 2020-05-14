package com.ycglj.manage.controller;

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
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ycglj.manage.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ycglj.manage.dao.OrderDAO;
import com.ycglj.manage.daoModel.Order_Date;
import com.ycglj.manage.service.SellerService;
import com.ycglj.sqlserver.context.Connect;

@Controller
@RequestMapping("/order")
public class OrderController {

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
	
	@RequestMapping("getAllOrderDate")
	public @ResponseBody Map<String, Object> getAllOrderDate(@RequestParam Integer limit,@RequestParam Integer page,String sort,String order,
			String search,Integer agree,Integer disagree, String day,HttpServletRequest request){
			
			Map searchMap=new HashMap<>();
			
			if(search!=null&&!search.trim().equals("")){
				search="%"+search+"%";  
				searchMap.put("name like ", search);
			}		
			
			if(agree!=null&&agree==1){
				searchMap.put(" agree = ", "1");
			}
			
			if(disagree!=null&&disagree==1){
				searchMap.put(" disagree = ", "1");
			}
			
			if(day!=null&&!day.equals("")){
				Date date = null;
				
				DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
				
				try {
					date = fmt.parse(day);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
				String d=sdf.format(date);
		        
		        searchMap.put("convert(varchar(11),sub_date,120 ) =", d);

			}
			
			int offset=(page-1)*limit;
			
			return orderDao.getAllOrderDate(limit, offset, sort,order, searchMap);
				
	}
	
	@RequestMapping("getAllOrderUser")
	public @ResponseBody Map<String, Object> getAllOrderUser(@RequestParam Integer limit,@RequestParam Integer page,String sort,String order,
			String search, String day,String time,String overdue,HttpServletRequest request){
			
			Map searchMap=new HashMap<>();
			
			if(search!=null&&!search.trim().equals("")){
				search="%"+search+"%";  
				searchMap.put("name like ", search);
			}		
			
			if(day!=null&&!day.equals("")){
				Date date = null;
				
				DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
				
				try {
					date = fmt.parse(day);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
				String d=sdf.format(date);
		        
		        searchMap.put("convert(varchar(11),sub_date,120 ) =", d);

			}
			
			if(time!=null&&!time.equals("")){
				Date date = null;
				
				DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
				
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
		        
		        searchMap.put("convert(varchar(11),sub_date,120 ) >", startTime);
				searchMap.put("convert(varchar(11),sub_date,120 ) <=", endTime);
			}
			
			if(overdue!=null&&!overdue.equals("")){
				
				searchMap.put(" overdue = ", overdue);
				
			}
			
			int offset=(page-1)*limit;
			
			searchMap.put("[cancel] =", "0");
			
			return orderDao.getAllOrderUser(limit, offset, sort,order, searchMap);
				
	}
	
	@RequestMapping("insertOrderDate")
	public @ResponseBody Integer insertOrderDate(@RequestParam String subTime,
			Integer agree,Integer disagree, HttpServletRequest request){
		
		HttpSession session = request.getSession();
		String operation = null;

		try {
			operation = session.getAttribute("campusAdmin").toString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		Date subDate = null;
		DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
		
		Date date=new Date();
		
		try {
			subDate = fmt.parse(subTime);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return 0;
		}
		
		
		UUID uuid=UUID.randomUUID();
		
		Order_Date order_Date=new Order_Date();
		
		order_Date.setUuid(uuid.toString());
		order_Date.setSub_date(subDate);
		order_Date.setOperation(operation);
		order_Date.setDate(date);

		if(agree!=null){
			order_Date.setAgree(1);
			order_Date.setDisagree(0);
		}else if(disagree!=null){			
			order_Date.setAgree(0);
			order_Date.setDisagree(1);
		}
		
		return orderDao.insertOrderDate(order_Date);
		
	}
	
	
	@RequestMapping("getOrderDate")
	public @ResponseBody Map getOrderDate(@RequestParam String time,HttpServletRequest request){
		
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
	
}
