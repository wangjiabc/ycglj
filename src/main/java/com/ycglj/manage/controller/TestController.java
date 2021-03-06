package com.ycglj.manage.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ycglj.manage.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ycglj.manage.dao.LicenseDAO;
import com.ycglj.manage.dao.OrderDAO;
import com.ycglj.manage.daoModel.Order_User;
import com.ycglj.manage.daoModelJoin.User_Order_Join;
import com.ycglj.manage.service.SellerService;
import com.ycglj.manage.tools.MyTestUtil;
import com.ycglj.sqlserver.context.Connect;


@Controller
@RequestMapping("/test")
public class TestController {
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
	
	LicenseDAO licenseDAO=(LicenseDAO) applicationContext.getBean("licensedao");
	
	@RequestMapping("getAllUserOrder")
	public @ResponseBody Map<String, Object> getAllUserOrder(String day){
		
		Map searchMap=new HashMap<>();
		
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
		
		Map map=orderDao.getAllUser_Order_Join(10, 0, "", "", searchMap);
		
		List list=(List) map.get("data");
		
		Iterator iterator=list.iterator();
		
		if(iterator.hasNext()){
			
			User_Order_Join user_Order_Join=(User_Order_Join) iterator.next();
		
			if(user_Order_Join.getAuthentication()==3){

				Calendar cal=Calendar.getInstance();
				Date date=new Date();
				cal.setTime(date);
				
				
				Date date2=new Date();
				Calendar cal2=Calendar.getInstance();

				cal2.setTime(user_Order_Join.getSub_date());
				
				int diff=cal.get(Calendar.MONTH)-cal2.get(Calendar.MONTH);
				
				System.out.println("diff="+diff);
				
				Order_User order_User = new Order_User();

				order_User.setOverdue(1);
				order_User.setCancel(1);
				order_User.setCancel_date(new Date());

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
		
		return map;
		
	}
	
	
	@RequestMapping("/getAll")
	public @ResponseBody Map<String, Object> getAll(String search,HttpServletRequest request) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		Map searchMap=new HashMap<>();
		

		if(search!=null&&!search.equals("")){
			search="%"+search+"%";  
			searchMap.put("[Users].name like ", search);
			searchMap.put("[User_License].license like ", search);
		}
		
		Map license_Positions=licenseDAO.getAllLicense_Position(10, 0, null, null,"or", searchMap);
		
		List licenses=(List) license_Positions.get("rows");
		int total=(int) license_Positions.get("total");
		
		map.put("rows", licenses);
		map.put("total", total);
		
		Map fileBytes=licenseDAO.License_PositionImageQuery(request, licenses);
		map.put("fileBytes", fileBytes);
		
		MyTestUtil.print(fileBytes);
		
		return map;
	}
	
}
