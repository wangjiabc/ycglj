package com.ycglj.manage.daoImpl;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.druid.sql.ast.statement.SQLIfStatement.Else;
import com.google.common.collect.Lists;
import com.ycglj.manage.dao.LicenseDAO;
import com.ycglj.manage.daoModel.Check_Person;
import com.ycglj.manage.daoModel.Crimal_Case;
import com.ycglj.manage.daoModel.Crimal_Record;
import com.ycglj.manage.daoModel.Crimal_Record_UpLog;
import com.ycglj.manage.daoModel.FileSelfBelong;
import com.ycglj.manage.daoModel.Law_Case;
import com.ycglj.manage.daoModel.Not_License;
import com.ycglj.manage.daoModel.Position;
import com.ycglj.manage.daoModel.Temp_Change;
import com.ycglj.manage.daoModel.Temp_User_License;
import com.ycglj.manage.daoModel.User_License;
import com.ycglj.manage.daoModel.Users;
import com.ycglj.manage.daoModel.WeiXin_User;
import com.ycglj.manage.daoModel.Weight_Log;
import com.ycglj.manage.daoModelJoin.Crimal_Record_Join;
import com.ycglj.manage.daoModelJoin.License_Position_Join;
import com.ycglj.manage.daoModelJoin.Users_License_Position_Join;
import com.ycglj.manage.daoRowMapper.RowMappers;
import com.ycglj.manage.daoSQL.DeleteExe;
import com.ycglj.manage.daoSQL.InsertExe;
import com.ycglj.manage.daoSQL.SelectExe;
import com.ycglj.manage.daoSQL.SelectJoinExe;
import com.ycglj.manage.daoSQL.SelectJoinExe2;
import com.ycglj.manage.daoSQL.SelectSqlJoinExe;
import com.ycglj.manage.daoSQL.UpdateExe;
import com.ycglj.manage.service.UserService;
import com.ycglj.manage.singleton.Singleton;
import com.ycglj.manage.tools.CopyFile;
import com.ycglj.manage.tools.MyTestUtil;
import com.ycglj.manage.tools.TransMapToString;

import cn.jpush.api.report.UsersResult.User;

public class LicenseDAOImpl extends JdbcDaoSupport implements LicenseDAO{
	
	@Override
	public Map getAllLicensePosition() {
		// TODO Auto-generated method stub
		Position position=new Position();
		
		String[] where={"[Position].is_license = ","1"};
		
		position.setLimit(1000000);
		position.setOffset(0);
		position.setNotIn("id");
		position.setWhere(where);
		
		Map map=new HashMap<>();
		
		List list=SelectExe.get(this.getJdbcTemplate(), position);
		
		int total=(int) SelectExe.getCount(this.getJdbcTemplate(), position).get("");
		
		map.put("rows", list);
		map.put("total", total);
		
		return map;
	}

	@Override
	public Map getAllLicensePositionJoin(String name,String startDate,String endDate,String[] yitStrings,String[] anyStrings
			,Integer type,com.ycglj.manage.model.Users users) {
		// TODO Auto-generated method stub
		String sql01 = "SELECT  [User_License].phone,"
				+ "[User_License].license,[User_License].business_state,[User_License].weight,"
				+ "[Position].is_license,[Position].lng," 
				+"[Position].lat ,[Position].wgs84_lng,[Position].wgs84_lat,"
				+ "criminal_number,fake_number,case_count,not_channel_number "
				+ " FROM [User_License] left join ("
				+"SELECT [license],count(license) as case_count "
				+"FROM [YC].[dbo].[Crimal_Record] group by license) as t0 "
				+"on [User_License].license = t0.license "
				+ "left join ("
				+"SELECT [license],sum(criminal_number) as criminal_number "
				+"FROM [YC].[dbo].[Law_Case] ";
		
		String sql201 = "SELECT count(*) "
				+ " FROM [User_License] left join ("
				+"SELECT [license],sum(criminal_number) as criminal_number "
				+"FROM [YC].[dbo].[Law_Case] ";
		
		int notDeal=0;
		
		int notTransit=0;
		
		int notBatch=0;
		
		if(anyStrings==null||anyStrings.equals("")){
			if((startDate!=null&&!startDate.equals(""))||(endDate!=null&&!endDate.equals(""))){
				sql01=sql01+" where ";
				sql201=sql201+" where ";
			}
			
			if(startDate!=null&&!startDate.equals("")){
				startDate="'"+startDate+"'";
				sql01=sql01+"convert(varchar(10),[criminal_time],120)>="+startDate;
				sql201=sql201+"convert(varchar(10),[criminal_time],120)>="+startDate;
			}
			
			if((startDate!=null&&!startDate.equals(""))&&(endDate!=null&&!endDate.equals(""))){
				sql01=sql01+" and ";
				sql201=sql201+" and ";
			}
			
			if(endDate!=null&&!endDate.equals("")){
				endDate="'"+endDate+"'";
				sql01=sql01+"convert(varchar(10),[criminal_time],120)<="+endDate;
				sql201=sql201+"convert(varchar(10),[criminal_time],120)<="+endDate;
			}
			sql01=sql01+" group by license )";
			sql201=sql201+" group by license )";
		}else{
			StringBuilder sb = new StringBuilder();
			for(String str : anyStrings){
				
			    	sb.append("criminal_type=");
			    	sb.append("'"+str+"'");
			    	sb.append(" OR ");
			
			    	if(str.equals("无证经营")){
			    		notDeal=1;
			    	}else if(str.equals("无证运输")){
			    		notTransit=1;
			    	}else if(str.equals("无证批发")){
			    		notBatch=1;
			    	}
			}
			
			String ay="("+sb.substring(0, sb.length() - 3)+")";
			
			sql01=sql01+" where ";
			sql201=sql201+" where ";
			
			if(startDate!=null&&!startDate.equals("")){
				startDate="'"+startDate+"'";
				sql01=sql01+"convert(varchar(10),[criminal_time],120)>="+startDate;
				sql201=sql201+"convert(varchar(10),[criminal_time],120)>="+startDate;
			}
			
			if((startDate!=null&&!startDate.equals(""))&&(endDate!=null&&!endDate.equals(""))){
				sql01=sql01+" and ";
				sql201=sql201+" and ";
			}
			
			if(endDate!=null&&!endDate.equals("")){
				endDate="'"+endDate+"'";
				sql01=sql01+"convert(varchar(10),[criminal_time],120)<="+endDate;
				sql201=sql201+"convert(varchar(10),[criminal_time],120)<="+endDate;
			}
			
			if((startDate!=null&&!startDate.equals(""))||(endDate!=null&&!endDate.equals(""))){
				sql01=sql01+" and ";
				sql201=sql201+" and ";
			}
			
			sql01=sql01+ay+" group by license )";
			sql201=sql201+ay+" group by license )";
		}
		
		sql01=sql01+" as t on [User_License].license = t.license "
				+ " left join (SELECT [license],sum(criminal_number) as fake_number FROM [YC].[dbo].[Law_Case] where criminal_type='假冒' and criminal_number>=50 group by license) as t2 "
				+ " on [User_License].license = t2.license "
				+ " left join (SELECT [license],sum(criminal_number) as not_channel_number FROM [YC].[dbo].[Law_Case] where criminal_type='非渠道' and criminal_number >=5000  group by license) as t3 "
				+ " on [User_License].license = t3.license ";
		sql201=sql201+" as t on [User_License].license = t.license ";
		
		String sql02 = " left join  [Position]"
				+ "on [User_License].license = [Position].license "
				+ "left join [Users] on [User_License].phone=[Users].phone";
		
		String sql202 = " left join  [Position]"
				+ "on [User_License].license = [Position].license "
				+ "left join [Users] on [User_License].phone=[Users].phone";
		
		String sql=sql01+sql02;
		String sql2=sql201+sql202;
		

		sql=sql+" where ([Position].lng is not null and [Position].lat is not null) ";
		sql2=sql2+" where ([Position].lng is not null and [Position].lat is not null) ";
		
		
		if((name!=null&&!name.equals(""))){
			name="'%"+name+"%'";
			sql=sql+" and ([Users].name  like "+name+" or [Users].phone  like "+name+")";
			sql2=sql2+" and ([Users].name  like "+name+" or [Users].phone  like "+name+")";
		}
		
		if((name!=null&&!name.equals(""))&&(yitStrings!=null&&!yitStrings.equals(""))){
			sql=sql+" and ";
			sql2=sql2+" and ";
		}
		
		if(yitStrings!=null&&!yitStrings.equals("")){
			StringBuilder sb = new StringBuilder();
			for(String str : yitStrings){
					
			    	sb.append("business_state=");
			    	sb.append("'"+str+"'");
			    	sb.append(" OR ");
			
			}
			
			String yt=" and ("+sb.substring(0, sb.length() - 3)+")";
			
			sql=sql+yt;
			sql2=sql2+yt;
		}
		
		if(anyStrings!=null&&!anyStrings.equals("")){

			sql=sql+" and criminal_number > 0";
			sql2=sql2+" and criminal_number > 0";
		}
		
		System.out.println("sql="+sql);
		System.out.println("sql2="+sql2);

		User_License user_License=new User_License();

		Position position=new Position();	
		
		Object[] objects={user_License,position};
	
		Map map=new HashMap<>();
	
		License_Position_Join license_Position_Join=new License_Position_Join();
		
		String lawCaseSql;
		
		if ((anyStrings==null||anyStrings.equals(""))&&(yitStrings==null||yitStrings.equals(""))) {
			lawCaseSql = "(SELECT license FROM [YC].[dbo].[Law_Case] where criminal_type='无证运输' or criminal_type='无证经营' or criminal_type='无证批发' group by license)";
		}else if ((anyStrings==null||anyStrings.equals(""))&&(yitStrings!=null||!yitStrings.equals(""))) {
			lawCaseSql = "(SELECT license FROM [YC].[dbo].[Law_Case] where criminal_type='' group by license)";
		} else {
			lawCaseSql = "(SELECT license FROM [YC].[dbo].[Law_Case] where ";
			if (notDeal == 1) {
				lawCaseSql = lawCaseSql+"criminal_type='无证运输' or ";
			} else if (notTransit == 1) {
				lawCaseSql = lawCaseSql+"criminal_type='无证经营' or ";
			} else if (notBatch == 1) {
				lawCaseSql = lawCaseSql+"criminal_type='无证批发' or";
			}
			if(notDeal == 1||notTransit == 1||notBatch == 1){
				lawCaseSql=lawCaseSql.substring(0, lawCaseSql.length() - 3)+"group by license)";
			}else{
				lawCaseSql="(SELECT license FROM [YC].[dbo].[Law_Case] group by license)";
			}
		}
		
		String notLicense="SELECT * FROM [Not_License] left join "+lawCaseSql+
				"as t on [Not_License].phone=t.license where t.license is not null";
		
		String notLicenseCount="SELECT count(*) FROM [Not_License] left join "+lawCaseSql+
				"as t on [Not_License].phone=t.license where t.license is not null";
		
		Not_License not_License=new Not_License();
		
		allPositionNotLicense allPositionNotLicense=new allPositionNotLicense();

		try{
			//List list=SelectSqlJoinExe.get(this.getJdbcTemplate(), sql, objects,license_Position_Join);
			
							
				Integer place=users.getPlace();
				
				Integer area=users.getArea();
				
				Integer business=users.getBusiness();
				
				if(area==null){
					area=1;
				}
				
				if(business==null){
					business=5;
				}
				
				if(place==2){
					if(business==1){
						sql=sql+" and area="+area;
						sql2=sql2+" and area="+area;
					}else if(business==2){
						sql=sql+" and area="+area;
						sql2=sql2+" and area="+area;
					}else if(business==3||business==4){
						Check_Person check_Person=new Check_Person();
						check_Person.setLimit(1);
						check_Person.setOffset(0);
						check_Person.setNotIn("id");
						String[] where={"phone=",users.getPhone()};
						check_Person.setWhere(where);
						List list=SelectExe.get(this.getJdbcTemplate(), check_Person);
						String region="";
						try{
							check_Person=(Check_Person) list.get(0);
							region=check_Person.getDepartment();
						}catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						sql=sql+" and area="+area+" and region='"+region+"'";
						sql2=sql2+" and area="+area+" and region='"+region+"'";
					}else if(business==5){
						sql=sql+" and area="+area;
						sql2=sql2+" and area="+area;
					}
				}else if(place==3){
					if(business==1){

					}else if(business==2){
						
					}else if(business==3){
						
					}
				}

			
			List list=this.getJdbcTemplate().query(sql,new allPositionCriminal());
			int total=(int) SelectSqlJoinExe.getCount(this.getJdbcTemplate(), sql2, objects).get("");
			
			List notLicenseList=this.getJdbcTemplate().query(notLicense, allPositionNotLicense);	
			int notLicenseTotal=(int) this.getJdbcTemplate().queryForMap(notLicenseCount).get("");
			
			list.addAll(notLicenseList);
			total=total+notLicenseTotal;
			
			map.put("rows", list);
			map.put("total", total);
			//System.out.println("list=");
			//MyTestUtil.print(list);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return map;
	}

	class allPositionCriminal implements RowMapper<Map> {

		@Override
		public Map mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			
			Double lng=rs.getDouble("lng");
			
			Double lat=rs.getDouble("lat");
			
			Integer case_count=rs.getInt("case_count");
			
			Integer fake_number=rs.getInt("fake_number");
			
			Integer not_channel_number=rs.getInt("not_channel_number");
			
			Integer weight=rs.getInt("weight");
			
			Map map=new HashMap<>();
			
			map.put("lng", lng);
			
			map.put("lat", lat);
			
			map.put("case_count", case_count);
			
			map.put("fake_number", fake_number);
			
			map.put("not_channel_number", not_channel_number);
			
			map.put("weight", weight);
			
			return map;
		}
		
	}
	
	class allPositionNotLicense implements RowMapper<Map> {

		@Override
		public Map mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			
			Double lng=rs.getDouble("lng");
			
			Double lat=rs.getDouble("lat");
			
			Map map=new HashMap<>();
			
			map.put("lng", lng);
			
			map.put("lat", lat);
			
			map.put("case_count", -1);
			
			map.put("fake_number", 0);
			
			map.put("not_channel_number", 0);
			
			map.put("weight", 0);
			
			return map;
		}
		
	}
	
	@Override
	public Map<String, Object> findAllLicense_Position(Integer limit, Integer offset,Double lng, Double lat,String term,Map search) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String sql0 = "SELECT TOP " + limit + " * from " 
				+"(select ROW_NUMBER() OVER (ORDER BY SQRT(("+lng+"-lng)*("+lng+"-lng)+("+lat+"-lat)*("+lat+"-lat))) AS rows ,"
				+ "[User_License].license ,[User_License].region,[User_License].area, [User_License].open_id,[User_License].phone,[User_License].address,[User_License].weight " 
				+ " FROM [User_License] left join  [Users]"
				+ "on  [User_License].open_id = [Users].open_id "
				+ "left join [Position] "
				+ "on [User_License].license = [Position].license " 
				+ "where (Position.lat is not null and Position.lng is not null) "
				+ ")as t2 "
				+ " left join  [Users]"
				+ "on  t2.open_id = [Users].open_id "
				+ "left join [Position] "
				+ "on t2.license = [Position].license " 
				;
		
		
		String sql1="ORDER BY t2.rows";
	
		String sql;
	
		String sql2="SELECT count(*) "+				   
				" FROM [User_License] left join [Users] "
				+ "on [User_License].phone = [Users].phone "
				+ "left join [Position] "
				+ "on [User_License].license = [Position].license "
				+"where (Position.lat is not null and Position.lng is not null)";
		
		System.out.println("search="+search);
		
		if(search.equals("")||search.isEmpty()){
			sql=sql0+" where (Position.lat is not null and Position.lng is not null) and rows>"+offset+" "+sql1;
		}else{
			
			StringBuilder sb = new StringBuilder();
			
			String[] where=TransMapToString.get(search);
			
			int i=0;
			boolean areaTerm=false; 
			boolean regionTerm=false;
			boolean weightTerm=false;
			String areawhere=null;
			String regionwhere=null;
			String weightwhere=null;
			
			for(String str : where){
				
				System.out.println("str="+str);

				String REGEX = "area=";
				Pattern pattern=Pattern.compile(REGEX);
				Matcher matcher=pattern.matcher(str);
				if(matcher.find()){
					areawhere="("+str+" ";
					areaTerm=true;
					continue;
				}
				
				if(areaTerm){
					areawhere=areawhere+str+")";
			    	areaTerm=false;
			    	continue;
				}
				
				
				String REGEX1 = "region=";
				Pattern pattern1=Pattern.compile(REGEX1);
				Matcher matcher1=pattern1.matcher(str);				
				if(matcher1.find()){
					regionwhere="("+str+" ";
					regionTerm=true;
					continue;
				}
				System.out.println("regionwhere="+regionwhere);
				if(regionTerm){
					regionwhere=regionwhere+"'"+str+"')";
			    	regionTerm=false;
			    	continue;
				}
				
				String REGEX2 = "weight=";
				Pattern pattern2=Pattern.compile(REGEX2);
				Matcher matcher2=pattern2.matcher(str);				
				if(matcher2.find()){
					weightwhere="("+str+" ";
					weightTerm=true;
					continue;
				}
				System.out.println("weightwhere="+weightwhere);
				if(weightTerm){
					weightwhere=weightwhere+"'"+str+"')";
			    	weightTerm=false;
			    	continue;
				}
				
			    if(i%2==0){
			    	sb.append(str);
			    }else{
			    	sb.append("'"+str+"'");
			    	sb.append(" "+term+" ");
			    }
			    i++;
			}
			String s = sb.toString();
			
			String serach = null;
			
			if(s!=null&&!s.equals("")){
				serach="("+s.substring(0,s.length()-4)+")";
			}
			
			if(serach!=null){
				if((areawhere!=null&&regionwhere!=null)){
					serach=serach+" and ("+areawhere+" and "+regionwhere+")";
				}else if(areawhere!=null&&regionwhere==null){
					serach=serach+" and ("+areawhere+")";
				}else if(areawhere==null&&regionwhere!=null){
					serach=serach+" and ("+regionwhere+")";
				}
			}else{
				if((areawhere!=null&&regionwhere!=null)){
					serach=" ("+areawhere+" and "+regionwhere+")";
				}else if(areawhere!=null&&regionwhere==null){
					serach="("+areawhere+")";
				}else if(areawhere==null&&regionwhere!=null){
					serach="("+regionwhere+")";
				}
			}
			
			if(weightwhere!=null){
				if(serach==null||serach.equals("")){
					serach=weightwhere;
				}else{
					serach=serach+" and "+weightwhere;
				}
			}
			
			System.out.println("serach="+serach);
			
			sql=sql0+"  where ("+serach+" ) and (Position.lat is not null and Position.lng is not null) and rows>"+offset
					+" "+sql1;
			sql2="SELECT count(*) from " 
					+"(select ROW_NUMBER() OVER (ORDER BY SQRT(("+lng+"-lng)*("+lng+"-lng)+("+lat+"-lat)*("+lat+"-lat))) AS rows ,"
					+ "[User_License].license ,[User_License].region,[User_License].area,[User_License].open_id,[User_License].phone,[User_License].address,[User_License].weight " 
					+ " FROM [User_License] left join  [Users]"
					+ "on  [User_License].open_id = [Users].open_id "
					+ "left join [Position] "
					+ "on [User_License].license = [Position].license " 
					+ ")as t2 "
					+ " left join  [Users]"
					+ "on  t2.open_id = [Users].open_id "
					+ "left join [Position] "
					+ "on t2.license = [Position].license " 
					+ "where ("+serach+" ) and (Position.lat is not null and Position.lng is not null)";
		}
	
		System.out.println("sql="+sql);
	
		User_License user_License=new User_License();

		Position position=new Position();	
		
		Users users=new Users();
		
		Object[] objects={user_License,position,users};
	
		Map map=new HashMap<>();
	
		Users_License_Position_Join users_License_Position_Join=new Users_License_Position_Join();
		
		try{
			List list=SelectSqlJoinExe.get(this.getJdbcTemplate(), sql, objects,users_License_Position_Join);
			int total=(int) SelectSqlJoinExe.getCount(this.getJdbcTemplate(), sql2, objects).get("");
			map.put("rows", list);
			map.put("total", total);
			MyTestUtil.print(list);
		}catch (Exception e) {
		// TODO: handle exception
		}

	return map;
	}

	@Override
	public Map<String, Object> findAllLicenseNotPosition(Integer limit, Integer offset, String search,Integer area) {
		// TODO Auto-generated method stub
		
		String sql01 = "SELECT top " + limit + " * FROM "
				+ "[User_License] left join [Position] on [User_License].license=[Position].license left join [Users] on [User_License].open_id=[Users].open_id "
				+ "where ";
		
		 String sql02= "[Position].lng is null ";
		 
		 String sql03= "and [User_License].license not in( select top " + offset
				+ " [User_License].license FROM [User_License] left join [Position] on [User_License].license=[Position].license left join [Users] on [User_License].open_id=[Users].open_id where  [Position].lng is null "
				+ " ";
		
		
		String sql1="SELECT count(*) "+
				"FROM [User_License] left join [Position] on [User_License].license=[Position].license left join [Users] on [User_License].open_id=[Users].open_id "+
				"where [Position].lng is null";
		
		String sql;
		
		if(search.equals("")){
			if(area==null){
				sql=sql01+sql02+sql03+")";
			}else{
				sql=sql01+sql02+" and  area="+area+sql03+" and area="+area+")";
				sql1=sql1+" and area="+area;
			}
		}else{
			if(area==null){
				sql=sql01+sql02+" and [Users].name like '%"+search+"%' "+sql03+"AND [Users].name like '%"+search+ "%' )";
				sql1=sql1+" and [Users].name like '%"+search+"%'";
			}else{
				sql=sql01+sql02+" and area="+area+" AND [Users].name like '%"+search+"%' "+sql03+" and area="+area+" AND [Users].name like '%"+search+"%')";
				sql1=sql1+" and area="+area+" AND [Users].name like '%"+search+"%'";
			}
		}

		
		Users_License_Position_Join users_License_Position_Join=new Users_License_Position_Join();
		
		User_License user_License=new User_License();

		Position position=new Position();	
		
		Users users=new Users();
		
		Object[] objects={user_License,position,users};
		
		Map map=new HashMap<>();
		
		try{
			List list=SelectSqlJoinExe.get(this.getJdbcTemplate(), sql, objects,users_License_Position_Join);
			
			int total=(int) SelectSqlJoinExe.getCount(this.getJdbcTemplate(), sql1, objects).get("");
			
			map.put("rows", list);
			
			map.put("total", total);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return map;
	}
	
	@Override
	public Map<String, Object> getAllLicense_Position(Integer limit, Integer offset, String sort, String order,
			String term,Map search) {
		// TODO Auto-generated method stub
		
		if(term.equals("or")||term.equals("OR")){
			term="OR";
		}else{
			term="AND";
		}
		
		User_License user_License=new User_License();

		user_License.setLimit(limit);
		user_License.setOffset(offset);
		user_License.setNotIn("license");
		user_License.setWhereTerm(term);
		
		Position position=new Position();	
		position.setLimit(limit);
		position.setOffset(offset);
		position.setNotIn("license");
		position.setWhereTerm(term);
		
		Users users=new Users();
		users.setLimit(limit);
		users.setOffset(offset);
		users.setNotIn("license");
		users.setWhereTerm(term);
		
		if(search!=null&&!search.isEmpty()&&!search.equals("")){
			String[] where=TransMapToString.get(search);
			users.setWhere(where);
			user_License.setWhere(where);
			position.setWhere(where);
		}
		
		Object[] objects={user_License,position,users};
		
		String[] join={"license","open_id"};
		
		Map map=new HashMap<>();
		
		Users_License_Position_Join users_License_Position_Join=new Users_License_Position_Join();
		
		List list=SelectJoinExe.get(this.getJdbcTemplate(), objects, users_License_Position_Join, join);
		
		int total=(int) SelectJoinExe.getCount(this.getJdbcTemplate(), objects, join).get("");
		
		map.put("rows", list);
		map.put("total", total);
		MyTestUtil.print(list);
		
		return map;
	}
	
	@Override
	public Map<String, Object> getAllLicense_Position2(Integer limit, Integer offset, String sort, String order,
			Map search,String area) {
		// TODO Auto-generated method stub

		String sql0 = "SELECT TOP " + limit + " * from "
				+ "[User_License] left join [Position] on [User_License].license=[Position].license left join [Users] on [User_License].open_id=[Users].open_id ";
				
		String sql = null;

		StringBuilder whereCommand = new StringBuilder();

		List params = new ArrayList<>();
		List paramsCount = new ArrayList<>();

		if (!search.isEmpty()) {
			String[] where = TransMapToString.get(search);

			List wheres = new ArrayList<String[]>();

			String[] columnWhere;

			wheres.add(where);

			Iterator<String[]> iterator = wheres.iterator();
			System.out.println("wheres=" + wheres);

			while (iterator.hasNext()) {
				columnWhere = iterator.next();
				int k = 1;
				for (String whereterm : columnWhere) {
						if (k % 2 == 0) {
							// System.out.println("鍋舵暟");
							// whereCommand.append(whereterm+"\n AND ");
						} else {
							// System.out.println("濂囨暟");
							// whereCommand.append("\n "+whereterm);
							whereCommand.append(whereterm + "? \n  OR ");
						}
					k++;
					System.out.println("whereCommand=" + whereCommand);
				}
			}

			iterator = wheres.iterator();
			while (iterator.hasNext()) {
				columnWhere = iterator.next();
				int k = 1;
				for (String whereterm : columnWhere) {
					if (!whereterm.equals("rea=")) {
						if (k % 2 == 0) {
							// System.out.println("鍋舵暟");
							params.add(whereterm);
						} else {
							// System.out.println("濂囨暟");
							// whereCommand.append("\n "+whereterm);
						}
					}
					k++;
				}
			}

			iterator = wheres.iterator();
			while (iterator.hasNext()) {
				columnWhere = iterator.next();
				int k = 1;
				for (String whereterm : columnWhere) {
					if (k % 2 == 0) {
						// System.out.println("鍋舵暟");
						params.add(whereterm);
						paramsCount.add(whereterm);
					} else {
						// System.out.println("濂囨暟");
						// whereCommand.append("\n "+whereterm);
					}
					k++;
				}
			}

		}

		if (!search.isEmpty()) {
			String  whereCom;
			if(area!=null&&!area.equals("")){
				whereCom="(" + whereCommand.substring(0, whereCommand.length() - 7) +") and area="+area;
			}else{
				whereCom="(" + whereCommand.substring(0, whereCommand.length() - 7) +")";
			}
			sql = sql0 + // sqlserver鍒嗛〉闇�瑕佸湪top涔熷姞涓妛here鏉′欢
					"\n  where "+whereCom+" and "+ 
					" [User_License].license not in("+
					" select top "+offset+" [User_License].license FROM [User_License] left join [Position] on [User_License].license=[Position].license left join [Users] on [User_License].open_id=[Users].open_id where "
					+ whereCom +")";
		}else{
			String  whereCom;
			if(area!=null&&!area.equals("")){
				whereCom=" area= "+area+" and ";
			}else{
				whereCom="";
			}
			
			String  whereCom2;
			if(area!=null&&!area.equals("")){
				whereCom2=" where area= "+area;
			}else{
				whereCom2="";
			}
			
			sql=sql0+" where "+whereCom+" [User_License].license not in( select top "+offset+" [User_License].license FROM [User_License] left join [Position] on [User_License].license=[Position].license left join [Users] on [User_License].open_id=[Users].open_id "+whereCom2+" ORDER BY [User_License].license)ORDER BY [User_License].license";
		}

		System.out.println("sql="+sql);
		
		List list = this.getJdbcTemplate().query(sql, params.toArray(),
				new RowMappers(Users_License_Position_Join.class));

		Map map=new HashMap<String, Object>();
		
		map.put("rows", list);
		MyTestUtil.print(list);

		String countSql = "SELECT count(*) from "
				+ "[User_License] left join [Position] on [User_License].license=[Position].license left join [Users] on [User_License].open_id=[Users].open_id ";

		if (!search.isEmpty()) {
			countSql = countSql + // sqlserver鍒嗛〉闇�瑕佸湪top涔熷姞涓妛here鏉′欢
					"\n  where (" + whereCommand.substring(0, whereCommand.length() - 7)+")";
			if(area!=null&&!area.equals("")){
				countSql = countSql + // sqlserver鍒嗛〉闇�瑕佸湪top涔熷姞涓妛here鏉′欢
						"\n  and area="+area;
			}
		}else{
			if(area!=null&&!area.equals("")){
				countSql = countSql + // sqlserver鍒嗛〉闇�瑕佸湪top涔熷姞涓妛here鏉′欢
						"\n  where area="+area;
			}

		}

		Map amount = this.getJdbcTemplate().queryForMap(countSql, paramsCount.toArray());

		map.put("total", amount.get(""));
		
		return map;
	}
	
	@Override
	public Map<String, Object> License_PositionImageQuery(HttpServletRequest request, List LicenseLits) {
		// TODO Auto-generated method stub
		String pathRoot = System.getProperty("user.home");
		
		String filePath=pathRoot+Singleton.filePath;
        
		String imgPath=request.getSession().getServletContext().getRealPath(Singleton.filePath);
		
		Map fileBytes=new HashMap<>();
		
		Iterator<Users_License_Position_Join> iterator=LicenseLits.iterator();
	
		while(iterator.hasNext()){			
		
			Users_License_Position_Join users_License_Position_Join = iterator.next();
		
			String license=users_License_Position_Join.getLicense();
		
			String sql="SELECT top 1 "+    
					"[license] "+
					",[UpFileFullName] "+
					",[FileType] "+
					",[FileBelong] "+
					",[FileIndex] "+
					",[ViewFileName] "+
					",[date] "+
				"FROM "+
				"[dbo].[FileSelfBelong] "+  
				"where license='"+license+"'";
		
			List fileSelfBelongs=this.getJdbcTemplate().query(sql,new fileSelfBelongRowMapper());
		
			try{
				FileSelfBelong fileSelfBelong=(FileSelfBelong) fileSelfBelongs.get(0);
							
				//String fileByte=Base64Test.getImageStr(filePath+"\\"+hidden_Data_Join.getURI());
				
				String oldFile=Singleton.ROOMINFOIMGPATH2+"\\"+fileSelfBelong.getUpFileFullName();
				
				System.out.println("oldFile="+oldFile);
				
				System.out.println("imgPath="+imgPath);
				
				CopyFile.set(imgPath, oldFile, fileSelfBelong.getUpFileFullName());
				
				fileBytes.put(license, Singleton.filePath+"\\compressFile\\"+fileSelfBelong.getUpFileFullName());
		
			}catch (Exception e) {
			// TODO: handle exception
				e.printStackTrace();
			}
		}

		return fileBytes;
	}
	
	class fileSelfBelongRowMapper implements RowMapper<FileSelfBelong>{

		@Override
		public FileSelfBelong mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			FileSelfBelong fileSelfBelong=new FileSelfBelong();
			fileSelfBelong.setLicense(rs.getString("license"));
			fileSelfBelong.setUpFileFullName(rs.getString("UpFileFullName"));
			fileSelfBelong.setFileType(rs.getString("FileType"));
			fileSelfBelong.setFileBelong(rs.getString("FileBelong"));
			fileSelfBelong.setFileIndex(rs.getInt("FileIndex"));
			fileSelfBelong.setViewFileName(rs.getString("ViewFileName"));
			fileSelfBelong.setDate(rs.getDate("date"));
			return fileSelfBelong;
		}
		
	}

	@Override
	public List allLicenseImageByGUID(HttpServletRequest request,
			Users_License_Position_Join users_License_Position_Join) {
		// TODO Auto-generated method stub
		String pathRoot = System.getProperty("user.home");
		
		String filePath=pathRoot+Singleton.filePath;
        
		String imgPath=request.getSession().getServletContext().getRealPath(Singleton.filePath);
		
		List fileBytes=new ArrayList<>();
				
		String license=users_License_Position_Join.getLicense();
	
		String sql="SELECT "+    
				"[license] "+
				",[UpFileFullName] "+
				",[FileType] "+
				",[FileBelong] "+
				",[FileIndex] "+
				",[ViewFileName] "+
				",[date] "+
			"FROM "+
			"[dbo].[FileSelfBelong] "+  
			"where license='"+license+"'";
		
		List fileSelfBelongs=this.getJdbcTemplate().query(sql,new fileSelfBelongRowMapper());
	
		System.out.println("fileSelfBelongs=");
		
		MyTestUtil.print(fileSelfBelongs);
		
		Iterator<FileSelfBelong> iterator=fileSelfBelongs.iterator();
		
		while(iterator.hasNext()){			

			FileSelfBelong fileSelfBelong=iterator.next();
			
			try{
			
				//String fileByte=Base64Test.getImageStr(filePath+"\\"+hidden_Data_Join.getURI());
				
				String oldFile=Singleton.ROOMINFOIMGPATH2+"\\"+fileSelfBelong.getUpFileFullName();
				
				System.out.println("imgPath="+imgPath);
				System.out.println("oldfile="+oldFile);
				
				CopyFile.set(imgPath, oldFile, fileSelfBelong.getUpFileFullName());
				
				Map<String,String> map=new HashMap<>();
				
				map.put("uri", Singleton.filePath+"\\"+fileSelfBelong.getUpFileFullName());
				map.put("compressUri", Singleton.filePath+"\\compressFile\\"+fileSelfBelong.getUpFileFullName());
				map.put("fileBelong", fileSelfBelong.getFileBelong());

				fileBytes.add(map);
		
			}catch (Exception e) {
			// TODO: handle exception
				e.printStackTrace();
			}
		
		}
		
		return fileBytes;
	}

	@Override
	public Integer updatePositionByLicense(Position position, boolean isUpdate) {
		// TODO Auto-generated method stub
		int i = 0;
		String[] where={"[Position].license=",position.getLicense()};
		position.setWhere(where);
		int count=(int) SelectExe.getCount(this.getJdbcTemplate(), position).get("");
		if(count==0){
			position.setIs_license(1);
			i=InsertExe.get(this.getJdbcTemplate(), position);			
		}else if(isUpdate){
			i=UpdateExe.get(this.getJdbcTemplate(), position);
		}
		
		return i;
	}

	@Override
	public Integer insertIntoFileSelfBelong(FileSelfBelong fileSelfBelong) {
		// TODO Auto-generated method stub
		return InsertExe.get(this.getJdbcTemplate(), fileSelfBelong);
	}

	@Override
	public Integer insertIntoCrimalCase(List<Crimal_Case> crimalCaseList, Crimal_Record crimal_Record) {
		// TODO Auto-generated method stub
		int i;
		
		Iterator<Crimal_Case> iterator=crimalCaseList.iterator();
		
		while (iterator.hasNext()) {

			Crimal_Case crimal_Case=iterator.next();
			
			i=InsertExe.get(this.getJdbcTemplate(), crimal_Case);
			
			if (i < 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
			
		}

		User_License user_License=new User_License();
		user_License.setLimit(1);
		user_License.setOffset(0);
		user_License.setNotIn("license");
		String[] where1={"license=",crimal_Record.getLicense()};
		user_License.setWhere(where1);
		
		try{
			user_License=(User_License) SelectExe.get(this.getJdbcTemplate(), user_License).get(0);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		if(user_License.getPhone()!=null&&!user_License.getPhone().contentEquals("")){
			crimal_Record.setPhone(user_License.getPhone());
		}else{
			crimal_Record.setPhone(crimal_Record.getLicense());
		}
		
		i=InsertExe.get(this.getJdbcTemplate(), crimal_Record);
		
		if (i < 1) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		
		iterator=crimalCaseList.iterator();
		
		while (iterator.hasNext()) {
			
			Crimal_Case crimal_Case=iterator.next();
			
			int case_number=crimal_Case.getCriminal_number();
			String license=crimal_Case.getLicense();
			String criminal_type=crimal_Case.getCriminal_type();
			Date criminal_time=crimal_Case.getCriminal_time();
			Date date=crimal_Case.getDate();
			
			Law_Case law_Case = new Law_Case();
			law_Case.setLimit(1);
			law_Case.setOffset(0);
			law_Case.setNotIn("license");

			String[] where = { "[license]=", crimal_Case.getLicense(), "[criminal_type]=",
					crimal_Case.getCriminal_type() };

			law_Case.setWhere(where);

			int count = (int) SelectExe.getCount(this.getJdbcTemplate(), law_Case).get("");

			if (count > 0) {

				try {

					List list = SelectExe.get(this.getJdbcTemplate(), law_Case);

					law_Case = (Law_Case) list.get(0);

					Law_Case law_Case2=new Law_Case();
					
					law_Case2.setCriminal_number(law_Case.getCriminal_number()+case_number);
					law_Case2.setDate(date);
					
					law_Case2.setWhere(where);
					
					i=UpdateExe.get(this.getJdbcTemplate(), law_Case2);
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					
				}

			}else{
				
				law_Case.setLicense(license);
				law_Case.setCriminal_type(criminal_type);
				law_Case.setCriminal_number(case_number);
				law_Case.setCriminal_time(criminal_time);
				law_Case.setDate(date);
				
				i=InsertExe.get(this.getJdbcTemplate(), law_Case);
				
			}
			
			if (i < 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
			
		}
		
		return i;
	}

	@Override
	public Integer updateCrimalCase(String crimal_id,List<Crimal_Case> crimalCaseList, Crimal_Record crimal_Record,String openId,Double lng,Double lat) {
		// TODO Auto-generated method stub
		int i = 0;
		
		Iterator<Crimal_Case> iterator=crimalCaseList.iterator();
		
		iterator=crimalCaseList.iterator();
		
		while (iterator.hasNext()) {
			
			Crimal_Case crimal_Case=iterator.next();
			
			String[] where1={"crimal_id=",crimal_id,"criminal_type=",crimal_Case.getCriminal_type()};
			
			Crimal_Case old_crimal_Case=new Crimal_Case();
			old_crimal_Case.setLimit(1);
			old_crimal_Case.setOffset(0);
			old_crimal_Case.setNotIn("crimal_id");
			
			old_crimal_Case.setWhere(where1);
			
			List oldList=SelectExe.get(this.getJdbcTemplate(), old_crimal_Case);
			
			old_crimal_Case=(Crimal_Case) oldList.get(0);
			
			int case_number=crimal_Case.getCriminal_number();
			String license=crimal_Case.getLicense();
			String criminal_type=crimal_Case.getCriminal_type();
			Date criminal_time=crimal_Case.getCriminal_time();
			Date date=crimal_Case.getDate();
			
			Law_Case law_Case = new Law_Case();
			law_Case.setLimit(1);
			law_Case.setOffset(0);
			law_Case.setNotIn("license");

			String[] where = { "[license]=", crimal_Case.getLicense(), "[criminal_type]=",
					crimal_Case.getCriminal_type() };

			law_Case.setWhere(where);

			int count = (int) SelectExe.getCount(this.getJdbcTemplate(), law_Case).get("");

			if (count > 0) {

				try {

					List list = SelectExe.get(this.getJdbcTemplate(), law_Case);

					law_Case = (Law_Case) list.get(0);

					Law_Case law_Case2=new Law_Case();
					
					int current_number=law_Case.getCriminal_number()+(crimal_Case.getCriminal_number()-old_crimal_Case.getCriminal_number());
					
					law_Case2.setCriminal_number(current_number);
					law_Case2.setDate(date);
					
					law_Case2.setWhere(where);
					
					i=UpdateExe.get(this.getJdbcTemplate(), law_Case2);
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					
				}

			}else{
				
				law_Case.setLicense(license);
				law_Case.setCriminal_type(criminal_type);
				law_Case.setCriminal_number(case_number);
				law_Case.setCriminal_time(criminal_time);
				law_Case.setDate(date);
				
				i=InsertExe.get(this.getJdbcTemplate(), law_Case);
				
			}
			
			if (i < 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
			
		}


		iterator=crimalCaseList.iterator();
		
		int criminal_number=0;
		
		while (iterator.hasNext()) {

			Crimal_Case crimal_Case=iterator.next();

			String[] where0={"crimal_id=",crimal_id,"criminal_type=",crimal_Case.getCriminal_type()};
			
			crimal_Case.setWhere(where0);
			
			if(crimal_Case.getCriminal_number()>0){
				i=UpdateExe.get(this.getJdbcTemplate(), crimal_Case);
			}else{
				i=DeleteExe.get(this.getJdbcTemplate(), crimal_Case);
			}
			
			criminal_number=criminal_number+crimal_Case.getCriminal_number();
			
			if (i < 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
			
		}

		Crimal_Record crimal_Record2=new Crimal_Record();
		crimal_Record2.setLimit(1);
		crimal_Record2.setOffset(0);
		crimal_Record2.setNotIn("crimal_id");
		String[] where3={"crimal_id = ",crimal_id};
		
		crimal_Record2.setWhere(where3);
		
		List oldcrimalList=SelectExe.get(this.getJdbcTemplate(), crimal_Record2);
		
		crimal_Record2=(Crimal_Record) oldcrimalList.get(0);
		
		User_License user_License=new User_License();
		user_License.setLimit(1);
		user_License.setOffset(0);
		user_License.setNotIn("license");
		String[] where1={"license=",crimal_Record.getLicense()};
		user_License.setWhere(where1);
		
		user_License=(User_License) SelectExe.get(this.getJdbcTemplate(), user_License).get(0);
		
		crimal_Record.setPhone(user_License.getPhone());
		
		String[] where0={"crimal_id=",crimal_id};
		
		crimal_Record.setWhere(where0);
		
		if(criminal_number>0){
			i=UpdateExe.get(this.getJdbcTemplate(), crimal_Record);
		}else{
			i=DeleteExe.get(this.getJdbcTemplate(), crimal_Record);
		}
		
		if (i < 1) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		Crimal_Record_UpLog crimal_Record_UpLog=new Crimal_Record_UpLog();
		crimal_Record_UpLog.setCrimal_id(crimal_id);
		crimal_Record_UpLog.setOld_criminal_content(crimal_Record2.getCriminal_content());
		crimal_Record_UpLog.setCriminal_content(crimal_Record.getCriminal_content());
		crimal_Record_UpLog.setOld_remark(crimal_Record2.getRemark());
		crimal_Record_UpLog.setRemark(crimal_Record.getRemark());
		crimal_Record_UpLog.setOpen_id(crimal_Record2.getOpen_id());
		crimal_Record_UpLog.setLast_up_open_id(crimal_Record2.getUp_open_id());
		crimal_Record_UpLog.setUp_open_id(openId);
		crimal_Record_UpLog.setCriminal_time(crimal_Record.getCriminal_time());
		crimal_Record_UpLog.setLast_up_time(crimal_Record2.getUp_data());
		crimal_Record_UpLog.setUp_time(new Date());
		crimal_Record_UpLog.setLat(lat);
		crimal_Record_UpLog.setLng(lng);
		
		i=InsertExe.get(this.getJdbcTemplate(), crimal_Record_UpLog);
		
		if (i < 1) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return i;
	}
	
	@Override
	public Integer insertNotLicenseCrimalCase(Not_License not_License, List<Crimal_Case> crimalCaseList,
			Crimal_Record crimal_Record) {
		// TODO Auto-generated method stub
		int i;
		
		i=InsertExe.get(this.getJdbcTemplate(), not_License);
		
		if (i < 1) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		i=insertIntoCrimalCase(crimalCaseList, crimal_Record);
		
		if (i < 1) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return i;
	}
	
	@Override
	public List getAllCaseByLicense(String license) {
		// TODO Auto-generated method stub
		
		Law_Case law_Case=new Law_Case();
		
		law_Case.setLimit(1000);
		law_Case.setOffset(0);
		law_Case.setNotIn("license");
		
		String[] where={"license=",license};
		
		law_Case.setWhere(where);
		
		List list=SelectExe.get(this.getJdbcTemplate(), law_Case);
		
		return list;
	}

	@Override
	public List findPosition(Position position) {
		// TODO Auto-generated method stub
		return SelectExe.get(this.getJdbcTemplate(), position);
	}

	@Override
	public Map getAllCrimalRecordJoin(Integer limit, Integer offset, String sort,String order, Map search) {
		// TODO Auto-generated method stub
		
		Crimal_Record crimal_Record=new Crimal_Record();
		crimal_Record.setLimit(limit);
		crimal_Record.setOffset(offset);
		crimal_Record.setNotIn("id");
		crimal_Record.setSort(sort);
		crimal_Record.setOrder(order);
		
		User_License user_License=new User_License();
		user_License.setLimit(limit);
		user_License.setOffset(offset);
		user_License.setNotIn("id");
		user_License.setSort(sort);
		user_License.setOrder(order);
		
		Users users=new Users();
		users.setLimit(limit);
		users.setOffset(offset);
		users.setNotIn("id");
		users.setSort(sort);
		users.setOrder(order);
		
		WeiXin_User weiXin_User=new WeiXin_User();
		weiXin_User.setLimit(limit);
		weiXin_User.setOffset(offset);
		weiXin_User.setNotIn("id");
		weiXin_User.setSort(sort);
		weiXin_User.setOrder(order);
		
		
		if(search!=null&&!search.isEmpty()&&!search.equals("")){
			String[] where=TransMapToString.get(search);
			users.setWhere(where);
			users.setWhereTerm("or");
			user_License.setWhere(where);
			user_License.setWhereTerm("or");
			weiXin_User.setWhere(where);
			weiXin_User.setWhereTerm("or");
		}
		
		Object[] objects={crimal_Record,user_License,users,weiXin_User};
		
		String[][] join={{"license","license"},{"phone","phone"},{"open_id","open_id"}};
		
		Map map=new HashMap<>();
	
		Crimal_Record_Join crimal_Record_Join=new Crimal_Record_Join();
		
		try{
			List list=SelectJoinExe2.get(this.getJdbcTemplate(), objects, crimal_Record_Join, join);
			int total=(int) SelectJoinExe2.getCount(this.getJdbcTemplate(), objects, join).get("");
			map.put("data", list);
			map.put("count", total);
			//MyTestUtil.print(list);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		
		return map;
	}

	@Override
	public Map findRoomInfoPositionByLatLng(Double lat, Double lng) {
		// TODO Auto-generated method stub
		Position position = new Position();
		position.setLimit(1);
		position.setOffset(0);
		position.setNotIn("id");

		String[] where={"lng=",String.valueOf(lng),"lat=",String.valueOf(lat)};
		
		position.setWhere(where);
		
		List list=SelectExe.get(this.getJdbcTemplate(), position);
		
		String license = null;
		
		Map map=new HashMap<>();
		
		try {
			position = (Position) list.get(0);
			license = position.getLicense();
			map.put("license", license);
			map.put("type", 1);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Not_License not_License=new Not_License();
			not_License.setLimit(1);
			not_License.setOffset(0);
			not_License.setNotIn("phone");
			not_License.setWhere(where);
			List list2=SelectExe.get(this.getJdbcTemplate(), not_License);
			not_License=(Not_License) list2.get(0);
			license=not_License.getPhone();
			map.put("license", license);
			map.put("type", 0);
		}
		
		return map;
	}

	@Override
	public Map getAllCheckPerson(Integer limit, Integer offset, String sort, String order, Map search) {
		// TODO Auto-generated method stub
		
		Check_Person check_Person=new Check_Person();
		check_Person.setLimit(limit);
		check_Person.setOffset(offset);
		check_Person.setSort(sort);
		check_Person.setOrder(order);
		check_Person.setNotIn("id");
		
		if(search!=null&&!search.isEmpty()&&!search.equals("")){
			String[] where=TransMapToString.get(search);
			check_Person.setWhere(where);
		}
		
		List list=SelectExe.get(this.getJdbcTemplate(),check_Person);
		
		int total=(int) SelectExe.getCount(this.getJdbcTemplate(),check_Person).get("");
		
		Map map=new HashMap<>();
		
		map.put("code", "0");
		map.put("data", list);
		map.put("count", total);
		
		return map;
	}

	@Override
	public Integer updateWeight(Weight_Log weight_Log) {
		// TODO Auto-generated method stub
		User_License user_License=new User_License();
		
		String[] where={"[license]=",weight_Log.getLicense()};
		
		user_License.setWeight(weight_Log.getState());
		user_License.setWhere(where);
		
		int i=0;
		
		i=UpdateExe.get(this.getJdbcTemplate(), user_License);
		
		if(i<1){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		i=InsertExe.get(this.getJdbcTemplate(), weight_Log);
		
		if(i<1){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return i;
	}

	@Override
	public List<Crimal_Case> getCrimalCase(String crimal_id) {
		// TODO Auto-generated method stub
		Crimal_Case crimal_Case=new Crimal_Case();
		crimal_Case.setLimit(100);
		crimal_Case.setOffset(0);
		crimal_Case.setNotIn("crimal_id");
		
		String[] where={"crimal_id=",crimal_id};
		
		crimal_Case.setWhere(where);
		
		return SelectExe.get(this.getJdbcTemplate(), crimal_Case);
	}

	@Override
	public String getNotLicense(String phone) {
		// TODO Auto-generated method stub
		Not_License not_License=new Not_License();
		not_License.setLimit(1);
		not_License.setOffset(0);
		not_License.setNotIn("phone");
		
		String[] where={"phone=",phone};
		
		not_License.setWhere(where);
		
		List list=SelectExe.get(this.getJdbcTemplate(), not_License);
		
		not_License=(Not_License) list.get(0);
		
		String text;
		
		text=not_License.getName();
		
		Crimal_Record crimal_Record=new Crimal_Record();
		crimal_Record.setLimit(100);
		crimal_Record.setOffset(0);
		crimal_Record.setNotIn("id");
		
		crimal_Record.setWhere(where);
		
		List list2=SelectExe.get(this.getJdbcTemplate(), crimal_Record);
		
		Iterator iterator=list2.iterator();
		
		while(iterator.hasNext()){
			Crimal_Record crimal_Record2=(Crimal_Record) iterator.next();
			text=text+","+crimal_Record2.getCriminal_content();
		}
		
		return text;
	}

	@Override
	public Not_License getNotLicenseById(String phone) {
		// TODO Auto-generated method stub
		Not_License not_License=new Not_License();
		not_License.setLimit(1);
		not_License.setOffset(0);
		not_License.setNotIn("phone");
		
		String[] where={"phone=",phone};
		
		not_License.setWhere(where);
		
		List list=SelectExe.get(this.getJdbcTemplate(), not_License);
		
		try{
			not_License=(Not_License) list.get(0);
			return not_License;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public List getBusinessStateType() {
		// TODO Auto-generated method stub
		String sql="SELECT business_state,COUNT(*) as value "+
					"from  User_License group by business_state ";
		
		List list=this.getJdbcTemplate().query(sql, new businessStateType());
		
		Iterator iterator=list.iterator();
		
		List list2=new ArrayList<>();
		
		while(iterator.hasNext()){
			Map map=(Map) iterator.next();
			String businessState=(String) map.get("name");
			list2.add(businessState);
		}
		
		return list2;
	}
	
	@Override
	public Map getBusinessStateByDate(String endTime) {
		// TODO Auto-generated method stub
		String sql="SELECT business_state,COUNT(*) as value "+
				"from  User_License where convert(varchar(11),business_date,120)<='"+endTime+"'"+
				" or business_date is null group by business_state ";
	
		List list=this.getJdbcTemplate().query(sql, new businessStateType());
		
		String sqlCount="SELECT count(*)"+
				"from  User_License where convert(varchar(11),business_date,120)<='"+endTime+"'"+
				" or business_date is null ";
		
		int total=(int) this.getJdbcTemplate().queryForMap(sqlCount).get("");
		
		Map map=new HashMap<>();
		
		map.put("data", list);
		map.put("total", total);
		
		return map;
	}
	
	class businessStateType implements RowMapper<Map> {

		@Override
		public Map mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			
			Integer value=rs.getInt("value");
			
			String name=rs.getString("business_state");
			
			Map map=new HashMap<>();
			
			map.put("value", value);
			
			if(name==null||name.equals("")){
				name="无";
			}
			
			map.put("name", name);
			
			return map;
		}
		
	}

	@Override
	public List getLawCaseType() {
		// TODO Auto-generated method stub
		String sql = "SELECT criminal_type,COUNT(*) as value " + "from  Crimal_Case group by criminal_type ";

		List list = this.getJdbcTemplate().query(sql, new LawCaseType());

		Iterator iterator = list.iterator();

		List list2 = new ArrayList<>();

		while (iterator.hasNext()) {
			Map map = (Map) iterator.next();
			String LawCaseType = (String) map.get("name");
			list2.add(LawCaseType);
		}

		return list2;
	}

	@Override
	public Map getLawCaseByDate(String startTime, String endTime) {
		// TODO Auto-generated method stub
		String sql="SELECT criminal_type,sum(criminal_number) as value "+
				"from  Crimal_Case where convert(varchar(11),criminal_time,120)>='"+startTime+"'"+
				" and convert(varchar(11),criminal_time,120)<='"+endTime+"'"+
				"group by criminal_type ";
	
		List list=this.getJdbcTemplate().query(sql, new LawCaseType());
		
		String sqlCount="SELECT count(*)"+
				"from  Crimal_Case where convert(varchar(11),criminal_time,120)>='"+startTime+"'"+
				" and convert(varchar(11),criminal_time,120)<='"+endTime+"'";
		
		int total=(int) this.getJdbcTemplate().queryForMap(sqlCount).get("");
		
		Map map=new HashMap<>();
		
		map.put("data", list);
		map.put("total", total);
		
		return map;
	}

	@Override
	public List getThreeLawCaseType() {
		// TODO Auto-generated method stub
		List list=new ArrayList<>();
		
		list.add("非渠道");
		list.add("假冒");
		list.add("走私");
		
		return list;
	}

	@Override
	public Map getThreeLawCaseByDate(String startTime, String endTime) {
		// TODO Auto-generated method stub
		String sql="SELECT criminal_type,sum(criminal_number) as value "+
				"from  Crimal_Case where convert(varchar(11),criminal_time,120)>='"+startTime+"'"+
				" and convert(varchar(11),criminal_time,120)<='"+endTime+"'"+
				" and (criminal_type='非渠道' or criminal_type='假冒' or criminal_type='走私')"+
				"group by criminal_type ";
	
		List list=this.getJdbcTemplate().query(sql, new LawCaseType());
		
		String sqlCount="SELECT count(*)"+
				"from  Crimal_Case where convert(varchar(11),criminal_time,120)>='"+startTime+"'"+
				" and convert(varchar(11),criminal_time,120)<='"+endTime+"'"+
				" and (criminal_type='非渠道' or criminal_type='假冒' or criminal_type='走私')";
		
		int total=(int) this.getJdbcTemplate().queryForMap(sqlCount).get("");
		
		Map map=new HashMap<>();
		
		map.put("data", list);
		map.put("total", total);
		
		return map;
	}
	
	class LawCaseType implements RowMapper<Map> {

		@Override
		public Map mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			
			Integer value=rs.getInt("value");
			
			String name=rs.getString("criminal_type");
			
			Map map=new HashMap<>();
			
			map.put("value", value);
						
			map.put("name", name);
			
			return map;
		}
		
	}

	@Override
	public Integer deleteLicense(String operate_user,User_License user_License) {
		// TODO Auto-generated method stub
		int i;
		
		Temp_User_License temp_User_License=new Temp_User_License();
		
		temp_User_License.setOpen_id(user_License.getOpen_id());
		temp_User_License.setPhone(user_License.getPhone());
		temp_User_License.setLicense(user_License.getLicense());
		temp_User_License.setAddress(user_License.getAddress());
		temp_User_License.setAuthentication(user_License.getAuthentication());
		temp_User_License.setAuthen_date(user_License.getAuthen_date());
		temp_User_License.setLicense_start_date(user_License.getLicense_start_date());
		temp_User_License.setLicense_end_date(user_License.getLicense_end_date());
		temp_User_License.setBusiness_state(user_License.getBusiness_state());
		temp_User_License.setBusiness_date(user_License.getBusiness_date());
		temp_User_License.setWeight(user_License.getWeight());
		temp_User_License.setArea(user_License.getArea());
		temp_User_License.setOperate_user(operate_user);
		temp_User_License.setOperate_date(new Date());
		temp_User_License.setDate(new Date());
		
		i=InsertExe.get(this.getJdbcTemplate(), temp_User_License);
		
		if (i < 1) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		String[] where={"license=",user_License.getLicense()};
		
		user_License.setWhere(where);
		
		i=DeleteExe.get(this.getJdbcTemplate(), user_License);
		
		if (i < 1) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return i;
	}

	@Override
	public Integer insertLicense(Temp_User_License temp_User_License) {
		// TODO Auto-generated method stub
		int i;
		
		User_License user_License=new User_License();
		
		user_License.setOpen_id(temp_User_License.getOpen_id());
		user_License.setPhone(temp_User_License.getPhone());
		user_License.setLicense(temp_User_License.getLicense());
		user_License.setAddress(temp_User_License.getAddress());
		user_License.setAuthentication(temp_User_License.getAuthentication());
		user_License.setAuthen_date(temp_User_License.getAuthen_date());
		user_License.setLicense_start_date(temp_User_License.getLicense_start_date());
		user_License.setLicense_end_date(temp_User_License.getLicense_end_date());
		user_License.setBusiness_state(temp_User_License.getBusiness_state());
		user_License.setBusiness_date(temp_User_License.getBusiness_date());
		user_License.setWeight(temp_User_License.getWeight());
		user_License.setArea(temp_User_License.getArea());
		user_License.setDate(new Date());
		user_License.setAuthentication(1);
		
		i=InsertExe.get(this.getJdbcTemplate(), user_License);
		
		if (i < 1) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		String[] where={"license=",temp_User_License.getLicense()};
		MyTestUtil.print(temp_User_License);
		MyTestUtil.print(where);
		
		temp_User_License.setWhere(where);
		
		i=DeleteExe.get(this.getJdbcTemplate(), temp_User_License);
		
		if (i < 1) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return i;
	}

	@Override
	public Temp_User_License getTempUserLicense(Temp_User_License temp_User_License) {
		// TODO Auto-generated method stub
		
		List list=SelectExe.get(this.getJdbcTemplate(), temp_User_License);
		
		Temp_User_License temp_User_License2=new Temp_User_License();
		
		try{
			temp_User_License2=(Temp_User_License) list.get(0);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return temp_User_License2;
	}

	@Override
	public Integer insertTempChange(Temp_Change temp_Change) {
		// TODO Auto-generated method stub
		return InsertExe.get(this.getJdbcTemplate(), temp_Change);
	}

	@Override
	public Temp_Change selectTempChange(Temp_Change temp_Change) {
		// TODO Auto-generated method stub
		List list=SelectExe.get(this.getJdbcTemplate(), temp_Change);
		
		Temp_Change temp_Change2=new Temp_Change();
		
		try{
			temp_Change2=(Temp_Change) list.get(0);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return temp_Change2;
	}

	@Override
	public Integer deleteTempChange(Temp_Change temp_Change) {
		// TODO Auto-generated method stub
		return DeleteExe.get(this.getJdbcTemplate(), temp_Change);
	}

	@Override
	public Integer updateTempUserLicense(Temp_User_License temp_User_License) {
		// TODO Auto-generated method stub
		return UpdateExe.get(this.getJdbcTemplate(), temp_User_License);
	}

	@Override
	public Integer updateCheckPerson(Check_Person check_Person) {
		// TODO Auto-generated method stub
		return UpdateExe.get(this.getJdbcTemplate(), check_Person);
	}

	@Override
	public Integer deleteUserLicense(String license) {
		// TODO Auto-generated method stub
		
		int i=0;
		
		User_License user_License=new User_License();
		
		String[] where={"license=",license};
		
		user_License.setWhere(where);
		
		i=DeleteExe.get(this.getJdbcTemplate(), user_License);
		
		if(i>0){
			
			Position position=new Position();
			
			position.setWhere(where);
			
			DeleteExe.get(this.getJdbcTemplate(), position);
		}
		
		return i;
	}

	@Override
	public Map findLicenseByPoint(int limit, int offset, Double lng, Double lat, Double distance, String search) {
		// TODO Auto-generated method stub
				String sql0="SELECT TOP "+limit+" * "+			   
							"FROM [User_License] left join  [Position]"+
							"on [User_License].license = [Position].license "+
							"left join  [Users]"+
							"on [User_License].open_id = [Users].open_id "+
							"WHERE ";

				String sql1="geography::STGeomFromText('POINT(' + cast(cast([lng] as decimal(20,12)) as varchar(50)) + ' '"+  
							"+ cast(cast([lat] as decimal(20,12)) as varchar(50)) +')', 4326).STDistance(  "+
							"geography::STGeomFromText('POINT("+lng+"  "+lat+")', 4326))<"+distance+" ";
				
				String sql10=" ORDER BY   "+
							"SQRT(("+lng+"-lng)*("+lng+"-lng)+("+lat+"-lat)*("+lat+"-lat))  ";		
				
				String sql01="AND "+
						"[User_License].license not in( select top "+offset+" [User_License].license "+
						"FROM [User_License] left join  [Position]"+
						"on [User_License].license = [Position].license "+
						"left join  [Users]"+
						"on [User_License].open_id = [Users].open_id "+
						"WHERE ";
				
				String sql02="geography::STGeomFromText('POINT(' + cast(cast([lng] as decimal(20,12)) as varchar(50)) + ' '"+  
						"+ cast(cast([lat] as decimal(20,12)) as varchar(50)) +')', 4326).STDistance(  "+
						"geography::STGeomFromText('POINT("+lng+"  "+lat+")', 4326))<"+distance+" ";
				
				String sql;
				
				String sql2="SELECT COUNT(*) "+
						"FROM [User_License] left join  [Position]"+
						"on [User_License].license = [Position].license "+
						"left join  [Users]"+
						"on [User_License].open_id = [Users].open_id "+
						"WHERE ";
				
				if(search==null||search.equals("")){
					sql=sql0+sql1+sql01+sql1+sql10+")"+sql10;
					sql2=sql2+sql02;
				}else{
					sql=sql0+"([User_License].Address like '%"+search+"%' or [Users].name like '%"+search+"%')"+" AND "+sql1;
					sql2=sql2+"([User_License].Address like '%"+search+"%' or [Users].name like '%"+search+"%')"+" AND "+sql02;
				}
				
				System.out.println("sql="+sql);
				
				User_License user_License=new User_License();
				
				Position position=new Position();		
				
				Users users=new Users();
				
				Object[] objects={user_License,position,users};
				
				Map map=new HashMap<>();
				
				Users_License_Position_Join users_License_Position_Join=new Users_License_Position_Join();
				
				try{
					List list=SelectSqlJoinExe.get(this.getJdbcTemplate(), sql, objects,users_License_Position_Join);
					int total=(int) SelectSqlJoinExe.getCount(this.getJdbcTemplate(), sql2, objects).get("");
					map.put("rows", list);
					map.put("total", total);
				}catch (Exception e) {
					// TODO: handle exception
				}

				return map;
	}


	
}
