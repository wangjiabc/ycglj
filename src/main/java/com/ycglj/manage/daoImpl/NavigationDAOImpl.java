package com.ycglj.manage.daoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import com.ycglj.manage.dao.NavigationDAO;
import com.ycglj.manage.model.Navigation2;

public class NavigationDAOImpl extends JdbcDaoSupport implements NavigationDAO {

		@Override
		public Navigation2 queryNavigation(String name) {
			String sql="select * from Navigtion where n ame like '%"+name+"%'";
			
	        List list=this.getJdbcTemplate().query(sql,new NavigationRowMapper());
	        Navigation2 rs=new Navigation2();
	        rs.setCode("0");
	        rs.setData(list);
	        
			return rs;
		}
	
	  class NavigationRowMapper implements RowMapper<Map> {
	  
		   @Override 
		   public Map mapRow(ResultSet rs, int rowNum) throws SQLException {
		   Map map = new HashMap<>();
		   map.put("name",rs.getString("name"));
		   map.put("addr",rs.getString("addr"));
	 
		   return map; 
	  }
	}
}
