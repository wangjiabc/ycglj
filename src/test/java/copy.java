import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.ycglj.manage.dao.LicenseDAO;
import com.ycglj.manage.dao.OrderDAO;
import com.ycglj.manage.dao.UserDAO;
import com.ycglj.manage.daoModel.FileSelfBelong;
import com.ycglj.manage.daoModel.Position;
import com.ycglj.manage.daoModel.User_Data;
import com.ycglj.manage.daoModel.User_License;
import com.ycglj.manage.daoModel.Users;
import com.ycglj.manage.tools.MyTestUtil;
import com.ycglj.sqlserver.context.Connect;

import aaa.java.Test1;
import aaa.java.Test2;

public class copy {

	static ApplicationContext applicationContext=new Connect().get();
	
	static UserDAO userDao=(UserDAO) applicationContext.getBean("userdao");
	
	OrderDAO orderDao=(OrderDAO) applicationContext.getBean("orderdao");
	
	static LicenseDAO licenseDAO=(LicenseDAO) applicationContext.getBean("licensedao");
	
	public static void main(String[] args) {
		
		List list=userDao.getTest1();
		
		Iterator iterator=list.iterator();
		
		int i=0;
		
		int u=0;
		
		while (iterator.hasNext()) {
			Test1 test1=(Test1) iterator.next();
		
			//MyTestUtil.print(test1);
			
			
					Users users3 = new Users();
					users3.setName(test1.getName());
					users3.setOpen_id(test1.getPhone());
					users3.setPhone(test1.getPhone());
					users3.setCard_type("identity");
					users3.setId_number(test1.getId_number());
					users3.setDate(new Date());
					users3.setUp_date(new Date());

					// if(u<10)
					//userDao.insertUser(users3);

					u++;
				}
		
		
		
		List list2=userDao.getTest2();
		
		Iterator iterator2=list2.iterator();
		
		int n=0;
		

		
		while (iterator2.hasNext()) {
			
			Test2 test2=(Test2) iterator2.next();

				User_License user_License3=new User_License();
				user_License3.setOpen_id(test2.getPhone());
				user_License3.setPhone(test2.getPhone());
				user_License3.setLicense(test2.getLicense());
				user_License3.setAddress(test2.getAddress());
				user_License3.setLicense_end_date(test2.getLicense_end_date());
				user_License3.setRegion(test2.getRegion());
				user_License3.setBusiness_state(test2.getBusiness_state());
				user_License3.setDate(new Date());
				user_License3.setBusiness_date(new Date());
			
				//if(in<10)
				n=n+userDao.insertUserLicense(user_License3);

		}
		//MyTestUtil.print(list2);
		
		System.out.println("i="+i+"  u="+u+"  n="+n);
		
	}
	
}
