package com.ycglj.manage.daoImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.ycglj.manage.daoModel.Order_User;
import com.ycglj.manage.daoModel.PreMessage;
import com.ycglj.manage.daoModel.User_Data;
import com.ycglj.manage.daoModel.User_License;
import com.ycglj.manage.daoSQL.InsertExe;
import com.ycglj.manage.daoSQL.SelectExe;
import com.ycglj.manage.daoSQL.SelectJoinExe;
import com.ycglj.manage.daoSQL.UpdateExe;
import com.ycglj.manage.tools.TransMapToString;
import com.ycglj.manage.singleton.Singleton;
import com.ycglj.manage.tools.CopyFile;
import com.ycglj.manage.tools.MyTestUtil;
import com.ycglj.manage.dao.UserDAO;
import com.ycglj.manage.daoModel.Users;
import com.ycglj.manage.daoModelJoin.User_Order_Join;
import com.ycglj.manage.daoModelJoin.Users_License_Join;


public class UserDAOImpl extends JdbcDaoSupport implements UserDAO {

	@Override
	public Users getUser(Users users) {
		// TODO Auto-generated method stub
		Users users2=new Users();
		
		try{
			users2=(Users) SelectExe.get(this.getJdbcTemplate(), users).get(0);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		if(users2!=null){
			return users2;
		}else{
			return null;
		}
		
	}
	
	@Override
	public Integer insertPreMessage(PreMessage preMessage) {
		// TODO Auto-generated method stub
		return InsertExe.get(this.getJdbcTemplate(), preMessage);
	}

	@Override
	public Integer insertUser(Users users) {
		// TODO Auto-generated method stub
		
		String[] where={"phone=",users.getPhone()};
		
		users.setLimit(1);
		users.setOffset(0);
		users.setWhere(where);
		
		int repeat=0;
		
		try{
			repeat=(int) SelectExe.getCount(this.getJdbcTemplate(), users).get("");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		if(repeat==0){
			int i;
			
			i=InsertExe.get(this.getJdbcTemplate(), users);
			
			String openId=users.getOpen_id();
		    String phone=users.getPhone();
			
		    User_License user_License=new User_License();

		    user_License.setOpen_id(openId);
		    
		    String[] where2 = { "open_id = ", phone };
			
		    user_License.setWhere(where2);
		    
		    UpdateExe.get(this.getJdbcTemplate(), user_License);

		    User_Data user_Data=new User_Data();

		    user_Data.setOpen_id(openId);
		    
		    user_Data.setWhere(where2);
		    
		    UpdateExe.get(this.getJdbcTemplate(), user_Data);
			
		    return i;
		    
		}else{
			return -1;
		}
	}

	@Override
	public Integer insertUserLicense(User_License user_License) {
		// TODO Auto-generated method stub
		return InsertExe.get(this.getJdbcTemplate(), user_License);
	}
	
	@Override
	public Integer updateUser(Users users) {
		// TODO Auto-generated method stub
		int i;
		
		i=UpdateExe.get(this.getJdbcTemplate(), users);
		
		if(i<1){
			
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			
		}
		
		String openId=users.getOpen_id();
	    String phone=users.getPhone();
		
	    System.out.println("openId="+openId+"     phone="+phone);
	    
	    User_License user_License=new User_License();

	    user_License.setOpen_id(openId);
	    
	    String[] where = { "open_id = ", phone };
		
	    user_License.setWhere(where);
	    
	    UpdateExe.get(this.getJdbcTemplate(), user_License);

	    User_Data user_Data=new User_Data();

	    user_Data.setOpen_id(openId);
	    
	    user_Data.setWhere(where);
	    
	    UpdateExe.get(this.getJdbcTemplate(), user_Data);
	    
	    return i;

	}

	@Override
	public Map<String,Object> getAllUser(Integer limit, Integer offset, String sort,String order, Map<String, String> search) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<>();
		
		Users users=new Users();
		
		users.setLimit(limit);
		users.setOffset(offset);
		users.setNotIn("id");
		
		if(sort!=null&&!sort.equals("")){

		}else{
			sort="id";
		}
		
		if(order!=null&&order.equals("desc")){
			order="desc";
		}else{
			order="asc";
		}
		
		users.setOrder(order);
		users.setSort(sort);
		
		if(!search.equals("")&&!search.isEmpty()){
			String[] where=TransMapToString.get(search);
			users.setWhere(where);
		}
		
		List<Users> list=SelectExe.get(this.getJdbcTemplate(), users);
		
		int total=(int) SelectExe.getCount(this.getJdbcTemplate(), users).get("");
		
		map.put("code", "0");
		
		map.put("data", list);
		
		map.put("count", total);
		
		return map;
	}

	@Override
	public Map<String, Object> getAllUserJoin(Integer limit, Integer offset, String sort, String order,
			Map<String, String> search) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<>();
		
		Users users=new Users();
		
		users.setLimit(limit);
		users.setOffset(offset);
		users.setNotIn("id");

		User_License user_License=new User_License();
		
		user_License.setLimit(limit);
		user_License.setOffset(offset);
		user_License.setNotIn("id");
		
		if(sort!=null&&!sort.equals("")){

		}else{
			sort="id";
		}
		
		if(order!=null&&order.equals("desc")){
			order="desc";
		}else{
			order="asc";
		}
		
		users.setOrder(order);
		users.setSort(sort);
		
		user_License.setOrder(order);
		user_License.setSort(sort);
		
		if(!search.equals("")&&!search.isEmpty()){
			String[] where=TransMapToString.get(search);
			users.setWhere(where);
			user_License.setWhere(where);
		}
		
		Object[] objects={users,user_License};
		
		String[] join={"open_id"};
		
		Users_License_Join user_Order_Join=new Users_License_Join();
		
		List list=SelectJoinExe.get(this.getJdbcTemplate(), objects, user_Order_Join,join);
		
		int total=(int) SelectJoinExe.getCount(this.getJdbcTemplate(), objects, join).get("");
		
		map.put("code", "0");
		
		map.put("data", list);
		
		map.put("count", total);
		
		return map;
	}
	
	@Override
	public Integer insertUserData(User_Data user_Data) {
		// TODO Auto-generated method stub
		
		return InsertExe.get(this.getJdbcTemplate(), user_Data);

	}

	@Override
	public Integer insertUserData2(User_Data user_Data) {
		// TODO Auto-generated method stub
		
		String openId=user_Data.getOpen_id();
		String dataType=user_Data.getData_type();
		
		User_Data user_Data2=new User_Data();
		user_Data2.setCurrently(0);
		user_Data2.setAffirm(0);
		
		if(user_Data.getData_type().equals("license")&&user_Data.getLicense()!=null&&!user_Data.getLicense().equals("")){
			String[] where={"open_id=",openId,"data_type=",dataType,"license=",user_Data.getLicense()};
			user_Data2.setWhere(where);
		}else{			
			String[] where={"open_id=",openId,"data_type=",dataType};
			user_Data2.setWhere(where);
		}
		
		UpdateExe.get(this.getJdbcTemplate(), user_Data2);
		
		return InsertExe.get(this.getJdbcTemplate(), user_Data);

	}
	
	@Override
	public Integer updateUserData(User_Data user_Data) {
		// TODO Auto-generated method stub
		return UpdateExe.get(this.getJdbcTemplate(), user_Data);
	}

	@Override
	public Integer updateUserLicense(User_License user_License) {
		// TODO Auto-generated method stub
		return UpdateExe.get(this.getJdbcTemplate(), user_License);
	}
	
	@Override
	public Map<String,Object> getAllUserData(HttpServletRequest request,Integer limit, Integer offset, String sort, String order,Map<String, String> search) {
		// TODO Auto-generated method stub
		
		String pathRoot = System.getProperty("user.home");
		
		String filePath=pathRoot+Singleton.filePath;
        
		String imgPath=request.getSession().getServletContext().getRealPath(Singleton.filePath);
		
		Map<String,Object> map=new HashMap<>();
		
		User_Data user_Data=new User_Data();
		
		user_Data.setLimit(limit);
		user_Data.setOffset(offset);
		user_Data.setNotIn("open_id");
		
		if(!search.equals("")&&!search.isEmpty()){
			Set set=search.entrySet();   
			int size=search.size();
			String[] where=new String[size*2+2];
			Iterator iterator=set.iterator();     
			int i=0;
			while (iterator.hasNext()){ 
			    Map.Entry  mapentry = (Map.Entry) iterator.next();
			    System.out.println("key="+i+" "+mapentry.getKey());
			    //去掉license条件
				if (!mapentry.getKey().equals("license=")) {
					where[i] = (String) mapentry.getKey();
					// System.out.println(i+" "+mapentry.getKey());
					where[i + 1] = (String) mapentry.getValue();
					// System.out.println(i+" "+mapentry.getValue());
					i = i + 2;
					MyTestUtil.print(where);
				}
			}   
			
			where[i]="data_type != ";
			where[i+1]="license";
			where[i+2]="data_type != ";
			where[i+3]="business";
			
			System.out.println("where1=");
			
			MyTestUtil.print(where);
			
			user_Data.setWhere(where);
		}
		
		List<User_Data> list=SelectExe.get(this.getJdbcTemplate(), user_Data);
		
		int total=(int) SelectExe.getCount(this.getJdbcTemplate(), user_Data).get("");
		
		
		
		if(!search.equals("")&&!search.isEmpty()){
			Set set=search.entrySet();   
			int size=search.size();
			String[] where=new String[size*2+2];
			Iterator iterator=set.iterator();     
			int i=0;
			while (iterator.hasNext()){ 
			    Map.Entry  mapentry = (Map.Entry) iterator.next();
			    System.out.println("key="+i+" "+mapentry.getKey());
					where[i] = (String) mapentry.getKey();
					// System.out.println(i+" "+mapentry.getKey());
					where[i + 1] = (String) mapentry.getValue();
					// System.out.println(i+" "+mapentry.getValue());
					i = i + 2;
					MyTestUtil.print(where);
			}   
			
			where[i]="data_type = ";
			where[i+1]="license";
			
			System.out.println("where2=");
			
			MyTestUtil.print(where);
			
			user_Data.setWhere(where);
		}
		
		try {
			
			List<User_Data> list2 = SelectExe.get(this.getJdbcTemplate(), user_Data);

			list.addAll(list2);
		
			total=total+(int) SelectExe.getCount(this.getJdbcTemplate(), user_Data).get("");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		if(!search.equals("")&&!search.isEmpty()){
			Set set=search.entrySet();   
			int size=search.size();
			String[] where=new String[size*2+2];
			Iterator iterator=set.iterator();     
			int i=0;
			while (iterator.hasNext()){ 
			    Map.Entry  mapentry = (Map.Entry) iterator.next();
			    System.out.println("key="+i+" "+mapentry.getKey());
					where[i] = (String) mapentry.getKey();
					// System.out.println(i+" "+mapentry.getKey());
					where[i + 1] = (String) mapentry.getValue();
					// System.out.println(i+" "+mapentry.getValue());
					i = i + 2;
					MyTestUtil.print(where);
			}   
			
			where[i]="data_type = ";
			where[i+1]="business";
			
			System.out.println("where3=");
			
			MyTestUtil.print(where);
			
			user_Data.setWhere(where);
		}
		
		try {
			
			List<User_Data> list3 = SelectExe.get(this.getJdbcTemplate(), user_Data);

			list.addAll(list3);
		
			total=total+(int) SelectExe.getCount(this.getJdbcTemplate(), user_Data).get("");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		List fileBytes=new ArrayList<>();
		
		Iterator<User_Data> iterator=list.iterator();
		
		while (iterator.hasNext()) {
			
			User_Data user_data=iterator.next();
			
			try{			
				String oldFile=filePath+"\\"+user_data.getUri();
			
				CopyFile.set(imgPath, oldFile, user_data.getUri());
			
				Map<String,String> map2=new HashMap<>();
				
				map2.put("dataType", user_data.getData_type());
				map2.put("uri", Singleton.filePath+"\\"+user_data.getUri());
				map2.put("compressUri", Singleton.filePath+"\\compressFile\\"+user_data.getUri());
				map2.put("date", user_data.getDate().toString());
				
				fileBytes.add(map2);
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
		
		map.put("data", list);
		
		map.put("count", total);
		
		map.put("fileBytes", fileBytes);
		
		return map;
	}
	
    @Override
	public Map<String, Object> findAllPreMessage(Integer limit, Integer offset, String sort, String order, Map search) {
		// TODO Auto-generated method stub
		
		PreMessage preMessage=new PreMessage();
		
		preMessage.setLimit(limit);
		preMessage.setOffset(offset);
		preMessage.setSort(sort);
		preMessage.setOrder(order);
		preMessage.setNotIn("UUID");
		
		if(!search.isEmpty()){
		    String[] where=TransMapToString.get(search);
		    preMessage.setWhere(where);
		}
		
		List list=SelectExe.get(this.getJdbcTemplate(), preMessage);
		
		int total=(int) SelectExe.getCount(this.getJdbcTemplate(), preMessage).get("");
		
		Map map=new HashMap<>();
		
		map.put("data", list);
		
		map.put("count", total);
		
		return map;
	}

	@Override
	public Integer updateUserDataAffirm(String openId , List list) {
		// TODO Auto-generated method stub
		
		Iterator<String> iterator=list.iterator();
		
		int i=0;
		
		User_Data user_Data=new User_Data();
		
		user_Data.setCurrently(0);

		String[] where0={"open_id = ",openId,"affirm >= ","0"};

		user_Data.setWhere(where0);
		
		int k=UpdateExe.get(this.getJdbcTemplate(), user_Data);
		
		if(k<1){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return 0;
		}
		
		user_Data.setCurrently(1);
		
		user_Data.setAffirm(1);
		
		user_Data.setUp_date(new Date());
		
		while (iterator.hasNext()) {
			String[] where={"uuid=",iterator.next()};
			System.out.println("uuid=");
			MyTestUtil.print(where);
			user_Data.setWhere(where);
			int j=UpdateExe.get(this.getJdbcTemplate(), user_Data);
			if(j<1){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return 0;
			}
			
			user_Data.setLimit(1);
			user_Data.setOffset(0);
			user_Data.setNotIn("open_id");
			
			try{
				
				User_Data user_Data2 = (User_Data) SelectExe.get(this.getJdbcTemplate(), user_Data).get(0);

				String[] where2 = { "open_id=", user_Data2.getOpen_id(),"license=", user_Data2.getLicense()};

				User_License user_License = new User_License();
				user_License.setAuthentication(3);
				user_License.setWhere(where2);
				user_License.setDate(new Date());
				
				System.out.println("user_data2");
				
				MyTestUtil.print(user_Data2);
				
				if (user_Data2 != null){
					UpdateExe.get(this.getJdbcTemplate(), user_License);
					
					Users users=new Users();
					
					users.setDate(new Date());
					
					String[] where3= {"open_id = ",openId};
					
					users.setWhere(where3);
					
					UpdateExe.get(this.getJdbcTemplate(), users);
				}
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			i=i+j;
		}
		
		if(i<4){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return 0;
		}
		
		return i;
	}

	@Override
	public User_License getUserLicense(User_License user_License) {
		// TODO Auto-generated method stub
		User_License user_License2=new User_License();
		
		try{
			user_License2=(User_License) SelectExe.get(this.getJdbcTemplate(), user_License).get(0);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		if(user_License2!=null){
			return user_License2;
		}else{
			return null;
		}
	}

	@Override
	public List getUserLicenseById(User_License user_License) {
		// TODO Auto-generated method stub
		return SelectExe.get(this.getJdbcTemplate(), user_License);
	}


    
}
