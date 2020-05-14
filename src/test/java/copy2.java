import java.util.Iterator;
import java.util.List;

import com.ycglj.manage.dao.UserDAO;
import com.ycglj.manage.daoModel.User_License;
import org.springframework.context.ApplicationContext;

import com.ycglj.manage.dao.LicenseDAO;
import com.ycglj.manage.dao.OrderDAO;
import com.ycglj.sqlserver.context.Connect;

import aaa.java.Test2;

public class copy2 {

	static ApplicationContext applicationContext=new Connect().get();
	
	static UserDAO userDao=(UserDAO) applicationContext.getBean("userdao");
	
	OrderDAO orderDao=(OrderDAO) applicationContext.getBean("orderdao");
	
	static LicenseDAO licenseDAO=(LicenseDAO) applicationContext.getBean("licensedao");
	
	public static void main(String[] args) {
		
		List list2=userDao.getTest2();
		
		Iterator iterator2=list2.iterator();
		
		
		int i=0;
		
		int n=0;
		
		
		while (iterator2.hasNext()) {
			
			Test2 test2=(Test2) iterator2.next();
		
			User_License user_License=new User_License();
			
			user_License.setLimit(1);
			user_License.setOffset(0);
			user_License.setNotIn("license");
			
			
			String phone=test2.getPhone();
			
			if(phone!="13518391110"&&phone!="13541495558"&&phone!="13548374834"
					&&phone!="13982731799"&&phone!="13989133270"&&phone!="15228471788"
					&&phone!="18281121489"&&phone!="18721085123"
					&&phone!="18980251014"&&phone!="18982461703"){
			
				String[] where={"phone=",test2.getPhone()};
			
				user_License.setWhere(where);
			}else{
				String[] where={"phone=",test2.getPhone(),"address like ","%"+test2.getAddress()+"%"};
				
				user_License.setWhere(where);
			}
			
			
			User_License user_License2=userDao.getUserLicense(user_License);
			
			String[] where2={"license=",user_License2.getLicense()};
			
			if(user_License2.getLicense()!=null){
				User_License user_License3=new User_License();
				user_License3.setLicense(test2.getLicense());
				user_License3.setLicense_end_date(test2.getLicense_end_date());
				user_License3.setRegion(test2.getRegion());
				user_License3.setBusiness_state(test2.getBusiness_state());
								
				user_License3.setWhere(where2);
				
				n=n+userDao.updateOnlyUserLicense(user_License3);
			
			}
			
			i++;
		}
		
		//MyTestUtil.print(list2);
		
		System.out.println("i="+i+"  n="+n);
		
	}
	
}
