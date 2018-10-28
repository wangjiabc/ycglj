package com.ycglj.manage.daoSQL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

public class UpdateExe {

	public static Integer get(JdbcTemplate getJdbcTemplate,Object object){
		String sql="";
        Map<String,Object> map=new HashMap<>();
		try {
			map = UpdateSQL.get(object);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sql=(String) map.get("sql");
		List params=(List) map.get("pamars");

		int i=getJdbcTemplate.update(sql,params.toArray());
		
		return i;
	}
}
