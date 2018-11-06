package com.ycglj.manage.daoImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.ycglj.manage.tools.TransMapToString;
import com.ycglj.manage.daoModel.WeiXin_User;
import com.ycglj.manage.daoModelJoin.User_Order_Join;
import com.ycglj.manage.daoSQL.DeleteExe;
import com.ycglj.manage.dao.OrderDAO;
import com.ycglj.manage.daoModel.Order_Date;
import com.ycglj.manage.daoModel.Order_User;
import com.ycglj.manage.daoModel.Users;
import com.ycglj.manage.daoSQL.InsertExe;
import com.ycglj.manage.daoSQL.SelectExe;
import com.ycglj.manage.daoSQL.SelectJoinExe;
import com.ycglj.manage.daoSQL.UpdateExe;

public class OrderDAOImpl extends JdbcDaoSupport implements OrderDAO{

	@Override
	public int insertOrderUser(Order_User order_User) {
		// TODO Auto-generated method stub
		int i=0;
		
		String[] where={"open_id=",order_User.getOpen_id()};
		
		order_User.setLimit(1);
		order_User.setOffset(0);
		order_User.setNotIn("id");
		order_User.setWhere(where);
		
		Order_User order_User2=null;
		
		List userList=SelectExe.get(this.getJdbcTemplate(), order_User);
		
		try {
			order_User2=(Order_User) userList.get(0);
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
		}
		
		if (order_User2 != null) {

			return -2;
			
		} else {

			i = InsertExe.get(this.getJdbcTemplate(), order_User);

			if (i < 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			} else {

				String uuid = null;
				
				Date subDate = order_User.getSub_date();

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				String d = sdf.format(subDate);

				String[] subDateWhere = { "convert(varchar(11),sub_date,120 ) =", d };

				Order_Date order_Date = new Order_Date();

				order_Date.setLimit(1);
				order_Date.setOffset(0);
				order_Date.setNotIn("id");
				order_Date.setWhere(subDateWhere);

				List orderList = SelectExe.get(this.getJdbcTemplate(), order_Date);

				Order_Date order_Date2 = null;

				try {
					order_Date2 = (Order_Date) orderList.get(0);
					uuid=order_Date2.getUuid();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				if (order_Date2 != null && order_Date2.getSub_date() != null) {

					if (order_Date2.getDisagree() == 1) {

						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

						return -1;
					}

					int number = order_Date2.getOrder_number();

					if (number > 0) {
						number++;
					} else {
						number = 1;
					}

					if(number>30){
						
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						
						return -1;
					
					}else if(number>=30){
						
						order_Date.setAgree(0);
					
					}
					
					order_Date.setOrder_number(number);

					i = UpdateExe.get(this.getJdbcTemplate(), order_Date);

					if (i < 1) {
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					}

				} else {

					uuid = UUID.randomUUID().toString();

					order_Date.setUuid(uuid.toString());
					order_Date.setSub_date(order_User.getSub_date());
					order_Date.setOrder_number(1);
					order_Date.setDate(new Date());

					i = InsertExe.get(this.getJdbcTemplate(), order_Date);
					
					if (i < 1) {
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					}
					
				}
				
				String[] userWhere={" open_id = ",order_User.getOpen_id()};
				
				Order_User order_User3=new Order_User();
				
				order_User3.setOrder_date_uuid(uuid);
				order_User3.setWhere(userWhere);
				
				i=UpdateExe.get(this.getJdbcTemplate(), order_User3);
				
				if (i < 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
				
			}
		}
		
		return i;
		
	}

	@Override
	public int updateOrderUser(Order_User order_User) {
		// TODO Auto-generated method stub
		
		int i = 0;
		
		String uuid = null;
		
		String[] uuidWhere={" open_id = ", order_User.getOpen_id()};
		
		Order_User order_User2=new Order_User();
		
		order_User2.setLimit(1);
		order_User2.setOffset(0);
		order_User2.setNotIn("id");
		order_User2.setWhere(uuidWhere);
		
		List oldDateList=SelectExe.get(this.getJdbcTemplate(), order_User2);
		
		try{
			order_User2=(Order_User) oldDateList.get(0);
		}catch (Exception e) {
			// TODO: handle exception
		}
		String[] subDateWhere = { " uuid = ",order_User2.getOrder_date_uuid()};

		Order_Date order_Date = new Order_Date();

		order_Date.setLimit(1);
		order_Date.setOffset(0);
		order_Date.setNotIn("id");
		order_Date.setWhere(subDateWhere);

		List orderList = SelectExe.get(this.getJdbcTemplate(), order_Date);

		Order_Date order_Date2 = null;

		try {
			order_Date2 = (Order_Date) orderList.get(0);
			uuid=order_Date2.getUuid();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		System.out.println(" uuid2 = "+uuid);
		
		Date subDate = order_User.getSub_date();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String d = sdf.format(subDate);

		String[] orderDateWhere = { "convert(varchar(11),sub_date,120 ) =", d };
		
		System.out.println("d="+d);
		
		Order_Date order_Date3 = new Order_Date();

		order_Date3.setLimit(1);
		order_Date3.setOffset(0);
		order_Date3.setNotIn("id");
		order_Date3.setWhere(orderDateWhere);
		
		List orderList3 = SelectExe.get(this.getJdbcTemplate(), order_Date3);

		try {
			order_Date3 = (Order_Date) orderList3.get(0);
			uuid=order_Date3.getUuid();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		System.out.println(" uuid = "+order_User2.getOrder_date_uuid());
		
		System.out.println(" uuid2 = "+order_Date3.getUuid());
		
		if (order_Date2 != null && order_Date2.getSub_date() != null) {

			System.out.println("  1: "+order_Date2.getSub_date()+"   2:  "+order_User.getSub_date());
			
			if(order_Date2.getSub_date().getTime()==order_User.getSub_date().getTime()&&order_User2.getCancel()==0){
				
					return 0;
				
			}else{
				
				int number1 = order_Date2.getOrder_number();

				if (number1 > 0&&order_User2.getCancel()==0) {
					number1--;
					
					order_Date2.setWhere(subDateWhere);
					order_Date2.setOrder_number(number1);
					
					i=UpdateExe.get(this.getJdbcTemplate(), order_Date2);
					
					if (i < 1) {
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					}

					
				}
				
				String[] orderDateWhere4 = { "convert(varchar(11),sub_date,120 ) =", d };
				
				System.out.println("d="+d);
				
				Order_Date order_Date4 = new Order_Date();

				order_Date4.setLimit(1);
				order_Date4.setOffset(0);
				order_Date4.setNotIn("id");
				order_Date4.setWhere(orderDateWhere4);
				
				List orderList4 = SelectExe.get(this.getJdbcTemplate(), order_Date4);

				try {
					order_Date4= (Order_Date) orderList4.get(0);
					uuid=order_Date4.getUuid();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				if (order_Date4 != null && order_Date4.getSub_date() != null) {

					if (order_Date4.getDisagree() == 1) {

						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

						return -1;
					}

					int number2 = order_Date4.getOrder_number();

					if (number2 > 0) {
						number2++;
					} else {
						number2 = 1;
					}

					if(number2>30){
						
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						
						return -1;
					
					}else if(number2>=30){
						
						order_Date.setAgree(0);
					
					}
					
					order_Date4.setOrder_number(number2);
					order_Date4.setWhere(orderDateWhere4);

					i = UpdateExe.get(this.getJdbcTemplate(), order_Date4);

					if (i < 1) {
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					}

				} else {

					uuid = UUID.randomUUID().toString();

					order_Date.setUuid(uuid.toString());
					order_Date.setSub_date(order_User.getSub_date());
					order_Date.setOrder_number(1);
					order_Date.setDate(new Date());

					i = InsertExe.get(this.getJdbcTemplate(), order_Date);
					
					if (i < 1) {
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					}
					
				}
				
			}
		}
	
		String[] userWhere={" open_id = ",order_User.getOpen_id()};
		
		Order_User order_User4=new Order_User();
		
		order_User4.setSub_date(order_User.getSub_date());
		order_User4.setOrder_date_uuid(uuid);
		order_User4.setCancel(0);
		order_User4.setOverdue(0);
		order_User4.setWhere(userWhere);
		
		i=UpdateExe.get(this.getJdbcTemplate(), order_User4);
		
		if (i < 1) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		
		return i;
	}

	
	@Override
	public int updateOverdueNumber(Order_User order_User) {
		// TODO Auto-generated method stub
		return UpdateExe.get(this.getJdbcTemplate(), order_User);
	}

	
	@Override
	public int cancelOrder(Order_User order_User) {
		// TODO Auto-generated method stub
		int i = 0;
		
		String uuid = null;
		
		String[] uuidWhere={" open_id = ", order_User.getOpen_id()};
		
		Order_User order_User2=new Order_User();
		
		order_User2.setLimit(1);
		order_User2.setOffset(0);
		order_User2.setNotIn("id");
		order_User2.setWhere(uuidWhere);
		
		List oldDateList=SelectExe.get(this.getJdbcTemplate(), order_User2);
		
		try{
			order_User2=(Order_User) oldDateList.get(0);
			if(order_User2.getCancel()>0){
				return -1;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		String[] subDateWhere = { " uuid = ",order_User2.getOrder_date_uuid()};

		Order_Date order_Date = new Order_Date();

		order_Date.setLimit(1);
		order_Date.setOffset(0);
		order_Date.setNotIn("id");
		order_Date.setWhere(subDateWhere);

		List orderList = SelectExe.get(this.getJdbcTemplate(), order_Date);

		Order_Date order_Date2 = null;

		try {
			order_Date2 = (Order_Date) orderList.get(0);
			uuid=order_Date2.getUuid();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		System.out.println(" uuid2 = "+uuid);
		
		Date subDate = order_User.getSub_date();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String d = sdf.format(subDate);

		String[] orderDateWhere = { "convert(varchar(11),sub_date,120 ) =", d };
		
		System.out.println("d="+d);
		
		Order_Date order_Date3 = new Order_Date();

		order_Date3.setLimit(1);
		order_Date3.setOffset(0);
		order_Date3.setNotIn("id");
		order_Date3.setWhere(orderDateWhere);
		
		List orderList3 = SelectExe.get(this.getJdbcTemplate(), order_Date3);

		try {
			order_Date3 = (Order_Date) orderList3.get(0);
			uuid=order_Date3.getUuid();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
				
		if (order_Date2 != null && order_Date2.getSub_date() != null) {
			
				int number = order_Date2.getOrder_number();

				System.out.println("number="+number);
				
				if (number > 0) {
					number--;
				} else {
					
				}

				order_Date.setOrder_number(number);

				i = UpdateExe.get(this.getJdbcTemplate(), order_Date);

				if (i < 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
			
			}
		
		  i=UpdateExe.get(this.getJdbcTemplate(), order_User);
		
		  if (i < 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
		  
		 return i;
		 
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

	@Override
	public Map<String, Object> getAllUser_Order_Join(Integer limit, Integer offset, String sort, String order,
			Map<String, String> search) {
		// TODO Auto-generated method stub
		Users users=new Users();
		
		users.setLimit(limit);
		users.setOffset(offset);
		users.setNotIn("id");
		
		Order_User order_User=new Order_User();
		
		order_User.setLimit(limit);
		order_User.setOffset(offset);
		order_User.setNotIn("id");
		
		if(search!=null&&!search.isEmpty()&&!search.equals("")){
			String[] where=TransMapToString.get(search);
			users.setWhere(where);
			order_User.setWhere(where);
		}
		
		Object[] objects={users,order_User};
		
		String[] join={"open_id"};
		
		User_Order_Join user_Order_Join=new User_Order_Join();
		
		List list=SelectJoinExe.get(this.getJdbcTemplate(), objects, user_Order_Join, join);
	
		int count=(int) SelectJoinExe.getCount(this.getJdbcTemplate(), objects,join).get("");
		
		Map<String, Object> map=new HashMap<>();
		
		map.put("code", "0");
		
		map.put("data", list);
		
		map.put("count", count);
		
		return map;
		
	}


	
}
