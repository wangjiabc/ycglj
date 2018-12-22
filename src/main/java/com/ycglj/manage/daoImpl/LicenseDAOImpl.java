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

import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.ycglj.manage.dao.LicenseDAO;
import com.ycglj.manage.daoModel.Crimal_Case;
import com.ycglj.manage.daoModel.Crimal_Record;
import com.ycglj.manage.daoModel.FileSelfBelong;
import com.ycglj.manage.daoModel.Law_Case;
import com.ycglj.manage.daoModel.Position;
import com.ycglj.manage.daoModel.User_License;
import com.ycglj.manage.daoModel.Users;
import com.ycglj.manage.daoModel.WeiXin_User;
import com.ycglj.manage.daoModelJoin.Crimal_Record_Join;
import com.ycglj.manage.daoModelJoin.License_Position_Join;
import com.ycglj.manage.daoModelJoin.Users_License_Position_Join;
import com.ycglj.manage.daoSQL.InsertExe;
import com.ycglj.manage.daoSQL.SelectExe;
import com.ycglj.manage.daoSQL.SelectJoinExe;
import com.ycglj.manage.daoSQL.SelectJoinExe2;
import com.ycglj.manage.daoSQL.SelectSqlJoinExe;
import com.ycglj.manage.daoSQL.UpdateExe;
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
	public Map getAllLicensePositionJoin(String name,String startDate,String endDate,String[] yitStrings,String[] anyStrings) {
		// TODO Auto-generated method stub
		String sql01 = "SELECT  [User_License].phone,"
				+ "[User_License].license,[User_License].business_state,[Position].is_license,[Position].lng," 
				+"[Position].lat ,[Position].wgs84_lng,[Position].wgs84_lat, criminal_number FROM [User_License] left join ("
				+"SELECT [license],COUNT(*) as criminal_number "
				+"FROM [YC].[dbo].[Law_Case] ";
		
		String sql201 = "SELECT count(*) "
				+ " FROM [User_License] left join ("
				+"SELECT [license],COUNT(*) as criminal_number "
				+"FROM [YC].[dbo].[Law_Case] ";
		
		if(anyStrings==null||anyStrings.equals("")){
			if((startDate!=null&&!startDate.equals(""))||(endDate!=null&&!endDate.equals(""))){
				sql01=sql01+" where ";
				sql201=sql201+" where ";
			}
			
			if(startDate!=null&&!startDate.equals("")){
				startDate="'"+startDate+"'";
				sql01=sql01+"convert(varchar(10),[criminal_time],120)>"+startDate;
				sql201=sql201+"convert(varchar(10),[criminal_time],120)>"+startDate;
			}
			
			if((startDate!=null&&!startDate.equals(""))&&(endDate!=null&&!endDate.equals(""))){
				sql01=sql01+" and ";
				sql201=sql201+" and ";
			}
			
			if(endDate!=null&&!endDate.equals("")){
				endDate="'"+endDate+"'";
				sql01=sql01+"convert(varchar(10),[criminal_time],120)<"+endDate;
				sql201=sql201+"convert(varchar(10),[criminal_time],120)<"+endDate;
			}
			sql01=sql01+" group by license )";
			sql201=sql201+" group by license )";
		}else{
			StringBuilder sb = new StringBuilder();
			for(String str : anyStrings){
				
			    	sb.append("criminal_type=");
			    	sb.append("'"+str+"'");
			    	sb.append(" OR ");
			
			}
			
			String ay="("+sb.substring(0, sb.length() - 3)+")";
			
			sql01=sql01+" where ";
			sql201=sql201+" where ";
			
			if(startDate!=null&&!startDate.equals("")){
				startDate="'"+startDate+"'";
				sql01=sql01+"convert(varchar(10),[criminal_time],120)>"+startDate;
				sql201=sql201+"convert(varchar(10),[criminal_time],120)>"+startDate;
			}
			
			if((startDate!=null&&!startDate.equals(""))&&(endDate!=null&&!endDate.equals(""))){
				sql01=sql01+" and ";
				sql201=sql201+" and ";
			}
			
			if(endDate!=null&&!endDate.equals("")){
				endDate="'"+endDate+"'";
				sql01=sql01+"convert(varchar(10),[criminal_time],120)<"+endDate;
				sql201=sql201+"convert(varchar(10),[criminal_time],120)<"+endDate;
			}
			
			if((startDate!=null&&!startDate.equals(""))||(endDate!=null&&!endDate.equals(""))){
				sql01=sql01+" and ";
				sql201=sql201+" and ";
			}
			
			sql01=sql01+ay+" group by license )";
			sql201=sql201+ay+" group by license )";
		}
		
		sql01=sql01+" as t on [User_License].license = t.license ";
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
			
			String yt="("+sb.substring(0, sb.length() - 3)+")";
			
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
		
		try{
			List list=SelectSqlJoinExe.get(this.getJdbcTemplate(), sql, objects,license_Position_Join);
			int total=(int) SelectSqlJoinExe.getCount(this.getJdbcTemplate(), sql2, objects).get("");
			map.put("rows", list);
			map.put("total", total);
			//MyTestUtil.print(list);
		}catch (Exception e) {
		// TODO: handle exception
		}

		return map;
	}

	
	@Override
	public Map<String, Object> findAllLicense_Position(Integer limit, Integer offset,Double lng, Double lat,String term,Map search) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String sql0 = "SELECT TOP " + limit + " * from " 
				+"(select ROW_NUMBER() OVER (ORDER BY SQRT(("+lng+"-lng)*("+lng+"-lng)+("+lat+"-lat)*("+lat+"-lat))) AS rows ,"
				+ "[User_License].license " 
				+ " FROM [User_License] left join  [Users]"
				+ "on  [User_License].open_id = [Users].open_id "
				+ "left join [Position] "
				+ "on [User_License].license = [Position].license " 
				+ ")as t2 left join User_License on t2.license=User_License.license "
				+ " left join  [Users]"
				+ "on  [User_License].phone = [Users].phone "
				+ "left join [Position] "
				+ "on [User_License].license = [Position].license " 
				;
		
		
		String sql1="ORDER BY t2.rows";
	
		String sql;
	
		String sql2="SELECT count(*) "+				   
				" FROM [User_License] left join [Users] "
				+ "on [User_License].phone = [Users].phone "
				+ "left join [Position] "
				+ "on [User_License].license = [Position].license ";
		
		System.out.println("search="+search);
		
		if(search.equals("")||search.isEmpty()){
			sql=sql0+" where rows>"+offset+" "+sql1;
		}else{
			
			StringBuilder sb = new StringBuilder();
			
			String[] where=TransMapToString.get(search);
			
			int i=0;
			for(String str : where){
			    
			    if(i%2==0){
			    	sb.append(str);
			    }else{
			    	sb.append("'"+str+"'");
			    	sb.append(" "+term+" ");
			    }
			    i++;
			}
			String s = sb.toString();
			
			String serach=s.substring(0,s.length()-4);
			
			System.out.println("serach="+serach);
			
			sql=sql0+"  where ("+serach+" ) and rows>"+offset
					+" "+sql1;
			sql2=sql2+" WHERE ("+serach+")";
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
	public Map<String, Object> getAllLicense_Position(Integer limit, Integer offset, String sort, String order,
			Map search) {
		// TODO Auto-generated method stub
		
		User_License user_License=new User_License();

		user_License.setLimit(limit);
		user_License.setOffset(offset);
		user_License.setNotIn("license");
		
		Position position=new Position();	
		position.setLimit(limit);
		position.setOffset(offset);
		position.setNotIn("license");
		
		Users users=new Users();
		users.setLimit(limit);
		users.setOffset(offset);
		users.setNotIn("license");
		
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
		
		user_License=(User_License) SelectExe.get(this.getJdbcTemplate(), user_License).get(0);
		
		crimal_Record.setPhone(user_License.getPhone());
		
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
	public List getAllCaseByLicense(String license) {
		// TODO Auto-generated method stub
		
		Law_Case law_Case=new Law_Case();
		
		law_Case.setLimit(100);
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
	
}
