package com.ycglj.manage.daoImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.ycglj.manage.daoModel.Order_User;
import com.ycglj.manage.daoModel.PreMessage;
import com.ycglj.manage.daoModel.User_Data;
import com.ycglj.manage.daoSQL.InsertExe;
import com.ycglj.manage.daoSQL.SelectExe;
import com.ycglj.manage.daoSQL.UpdateExe;
import com.ycglj.manage.tools.TransMapToString;
import com.ycglj.manage.singleton.Singleton;
import com.ycglj.manage.tools.CopyFile;
import com.ycglj.manage.dao.UserDAO;
import com.ycglj.manage.daoModel.Users;


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
			return InsertExe.get(this.getJdbcTemplate(), users);
		}else{
			return -1;
		}
	}

	@Override
	public Integer updateUser(Users users) {
		// TODO Auto-generated method stub
		return UpdateExe.get(this.getJdbcTemplate(), users);
	}

	@Override
	public Map<String,Object> getAllUser(Integer limit, Integer offset, String sort,String order, Map<String, String> search) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<>();
		
		Users users=new Users();
		
		users.setLimit(limit);
		users.setOffset(offset);
		users.setNotIn("id");
		
		if(sort!=null){

		}else{
			sort="date";
		}
		
		if(order!=null&&order.equals("asc")){
			order="asc";
		}else{
			order="desc";
		}
		
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
	public Integer insertUserData(User_Data user_Data) {
		// TODO Auto-generated method stub
		return InsertExe.get(this.getJdbcTemplate(), user_Data);
	}

	@Override
	public Integer updateUserData(User_Data user_Data) {
		// TODO Auto-generated method stub
		return UpdateExe.get(this.getJdbcTemplate(), user_Data);
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
			String[] where=TransMapToString.get(search);
			user_Data.setWhere(where);
		}
		
		List<User_Data> list=SelectExe.get(this.getJdbcTemplate(), user_Data);
		
		int total=(int) SelectExe.getCount(this.getJdbcTemplate(), user_Data).get("");
		
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
			user_Data.setWhere(where);
			int j=UpdateExe.get(this.getJdbcTemplate(), user_Data);
			if(j<1){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return 0;
			}
			i=i+j;
		}
		
		if(i<4){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return 0;
		}else{
			
			Users users=new Users();
			users.setAuthentication(3);
			
			String[] where={"open_id = ",openId};
			users.setWhere(where);
			
			i=UpdateExe.get(this.getJdbcTemplate(), users);
			
			if(i<1){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return 0;
			}
			
		}
		
		return i;
	}

    
}
