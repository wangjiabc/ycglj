package com.ycglj.manage.daoImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.ycglj.manage.tools.TransMapToString;
import com.ycglj.manage.daoModel.WeiXin_User;
import com.ycglj.manage.daoSQL.DeleteExe;
import com.ycglj.manage.dao.OrderDAO;
import com.ycglj.manage.daoModel.Order_Date;
import com.ycglj.manage.daoModel.Order_User;
import com.ycglj.manage.daoSQL.InsertExe;
import com.ycglj.manage.daoSQL.SelectExe;
import com.ycglj.manage.daoSQL.UpdateExe;

public class OrderDAOImpl extends JdbcDaoSupport implements OrderDAO{

	@Override
	public int insertOrderUser(Order_User order_User) {
		// TODO Auto-generated method stub
		return InsertExe.get(this.getJdbcTemplate(), order_User);
	}

	@Override
	public int updateOrderUser(Order_User order_User) {
		// TODO Auto-generated method stub
		return UpdateExe.get(this.getJdbcTemplate(), order_User);
	}

	@Override
	public Map<String, Object> getAllOrderUser(Integer limit, Integer offset, String sort,String order, Map<String, String> search) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<>();
		
		Order_User order_User=new Order_User();
		
		order_User.setLimit(limit);
		order_User.setOffset(offset);
		order_User.setNotIn("id");
		
		if(!search.equals("")&&!search.isEmpty()){
			String[] where=TransMapToString.get(search);
			order_User.setWhere(where);
		}
		
		List<Order_User> list=SelectExe.get(this.getJdbcTemplate(), order_User);
		
		int total=(int) SelectExe.getCount(this.getJdbcTemplate(), order_User).get("");
		
		map.put("code", "0");
		
		map.put("data", list);
		
		map.put("count", total);
		
		return map;
	}

	@Override
	public int insertOrderDate(Order_Date order_Date) {
		// TODO Auto-generated method stub
		
		Date subDate=order_Date.getSub_date();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String subTime= sdf.format(subDate);
		
		System.out.println("subdate2="+subDate);
		
		String[] where={"convert(varchar(10),sub_date,120)=",subTime};
		
		Order_Date order_Date2=new Order_Date();
		
		order_Date2.setLimit(1);
		order_Date2.setOffset(0);
		order_Date2.setNotIn("id");
		order_Date2.setWhere(where);
		
		int repeat=(int) SelectExe.getCount(this.getJdbcTemplate(), order_Date2).get("");
		
		int i;
		
		if(repeat>0){
			order_Date.setWhere(where);
			i=UpdateExe.get(this.getJdbcTemplate(), order_Date);
		}else{
			i=InsertExe.get(this.getJdbcTemplate(), order_Date);
		}
		
		return i;
	}

	@Override
	public int updateOrderDate(Order_Date order_Date) {
		// TODO Auto-generated method stub
		return UpdateExe.get(this.getJdbcTemplate(), order_Date);
	}

	@Override
	public Map<String, Object> getAllOrderDate(Integer limit, Integer offset, String sort,String order, Map<String, String> search) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<>();
		
		Order_Date order_Date=new Order_Date();
		
		order_Date.setLimit(limit);
		order_Date.setOffset(offset);
		order_Date.setNotIn("id");
		
		if(!search.equals("")&&!search.isEmpty()){
			String[] where=TransMapToString.get(search);
			order_Date.setWhere(where);
		}
		
		List<Order_User> list=SelectExe.get(this.getJdbcTemplate(), order_Date);
		
		int total=(int) SelectExe.getCount(this.getJdbcTemplate(), order_Date).get("");
		
		map.put("code", "0");
		
		map.put("data", list);
		
		map.put("count", total);
		
		return map;
	}

	
	@Override
	public Integer insertWeiXinUser(WeiXin_User weiXin_User) {
		// TODO Auto-generated method stub
		return InsertExe.get(this.getJdbcTemplate(), weiXin_User);
	}


	@Override
	public Integer deleteWeiXinUser(WeiXin_User weiXin_User) {
		// TODO Auto-generated method stub
		
		String campusAdmin=weiXin_User.getCampusAdmin();
		
		String[] where={"[WeiXin_User].campusAdmin=",campusAdmin};
		
		weiXin_User.setWhere(where);
		
		return DeleteExe.get(this.getJdbcTemplate(), weiXin_User);
		
	}


	@Override
	public Integer updateWeiXinUser(WeiXin_User weiXin_User) {
		// TODO Auto-generated method stub
		
		String campusAdmin=weiXin_User.getCampusAdmin();
		
		String[] where={"[WeiXin_User].campusAdmin=",campusAdmin};
		
		weiXin_User.setWhere(where);
		
		return UpdateExe.get(this.getJdbcTemplate(), weiXin_User);
		
	}


	@Override
	public Integer selectCountWeiXinUser(WeiXin_User weiXin_User) {
		// TODO Auto-generated method stub
		
		String campusAdmin=weiXin_User.getCampusAdmin();
		
		String[] where={"[WeiXin_User].campusAdmin=",campusAdmin};
		
		weiXin_User.setWhere(where);
		
		int count=(int) SelectExe.getCount(this.getJdbcTemplate(), weiXin_User).get("");
		
		return count;
	}
	
}
