package com.ycglj.manage.daoSQL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ycglj.manage.daoRowMapper.RowMappers;

public class SelectExe {

	public static List get(JdbcTemplate getJdbcTemplate,Object object){
		   String sql="";
	        Map<String,Object> map=new HashMap<>();
			try {
				map = SelectSQL.get(object);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			sql=(String) map.get("sql");
			List params=(List) map.get("params");
			
			List list= getJdbcTemplate.query(sql,params.toArray(),new RowMappers(object.getClass()));
	        return list;
	}
	
	public static Map getCount(JdbcTemplate getJdbcTemplate,Object object) {
		String sql="";
        Map<String,Object> map=new HashMap<>();
		try {
			map = SelectSQL.getCount(object);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sql=(String) map.get("sql");
		List params=(List) map.get("params");
		
		 Map amount=getJdbcTemplate.queryForMap(sql,params.toArray());
		 
		 return amount;
	}
	
}
