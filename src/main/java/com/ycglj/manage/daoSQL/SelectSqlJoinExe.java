package com.ycglj.manage.daoSQL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ycglj.manage.daoRowMapper.RowMappersJoin;

public class SelectSqlJoinExe {
	public static List get(JdbcTemplate getJdbcTemplate,String sql,Object[] objects,
			Object object){
	        Map<String,Object> map=new HashMap<>();
	        int amount=objects.length;
	        Class<?>[] classeNames=new Class<?>[amount];
	        
	        int index=0;
	        for(Object object1:objects){
	         classeNames[index]=object1.getClass();
	         System.out.println(index+"__classeNames="+object1.toString());
	         index++;
	        }
	        
			List list=getJdbcTemplate.query(sql,new RowMappersJoin(classeNames,object.getClass()));
	        return list;
	}
	
	public static Map getCount(JdbcTemplate getJdbcTemplate,String sql,Object[] objects) {

		
		 Map amount=getJdbcTemplate.queryForMap(sql);
		 
		 return amount;
	}
}
