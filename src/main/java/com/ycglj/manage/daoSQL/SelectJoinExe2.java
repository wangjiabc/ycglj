package com.ycglj.manage.daoSQL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ycglj.manage.daoRowMapper.RowMappersJoin;

public class SelectJoinExe2 {
	public static List get(JdbcTemplate getJdbcTemplate,Object[] objects,
			Object object,String[][] joinParames){
		   String sql="";
	        Map<String,Object> map=new HashMap<>();
	        int amount=objects.length;
	        Class<?>[] classeNames=new Class<?>[amount];
	        
	        int index=0;
	        for(Object object1:objects){
	         classeNames[index]=object1.getClass();
	         System.out.println(index+"__classeNames="+object1.toString());
	         index++;
	        }
	        
	        try {
				map=new SelectSQLJoin2().get(objects,joinParames);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			sql=(String) map.get("sql");
			List params=(List) map.get("params");
			System.out.println("params="+params);
			List list=getJdbcTemplate.query(sql,params.toArray(), new RowMappersJoin(classeNames,object.getClass()));
	        return list;
	}
	
	public static Map getCount(JdbcTemplate getJdbcTemplate,Object[] objects,
			String[][] joinParames) {
		String sql="";
     Map<String,Object> map=new HashMap<>();
		try {
			map = SelectSQLJoin2.getCount(objects,joinParames);
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
