package com.ycglj.manage.daoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import com.ycglj.manage.dao.AreaDAO;
import com.ycglj.manage.model.Area2;

public class AreaDAOImpl extends JdbcDaoSupport implements AreaDAO {

    public Area2 queryArea(String code,String name,Integer limit,Integer page) {
    	//返回List数据
        String sql1="SELECT top "+limit+" * FROM (SELECT row_number ( ) over (ORDER BY code ASC) AS rownumber,* FROM Area)temp_row WHERE rownumber > ("+page+"-1)*10";
        //sql1判断
        if(code!=null && name==null) {
        	sql1="SELECT top "+limit+" * FROM (SELECT row_number ( ) over (ORDER BY code ASC) AS rownumber,* FROM Area)temp_row WHERE code like '%"+code+"%' and rownumber > ("+page+"-1)*10";
        }else if(code==null && name!=null) {
        	sql1="SELECT top "+limit+" * FROM (SELECT row_number ( ) over (ORDER BY code ASC) AS rownumber,* FROM Area)temp_row WHERE name like '%"+name+"%' and rownumber > ("+page+"-1)*10";
        }else if(code!=null && name!=null) {
        	sql1="SELECT top "+limit+" * FROM (SELECT row_number ( ) over (ORDER BY code ASC) AS rownumber,* FROM Area)temp_row WHERE code like '%"+code+"%' and name like '%"+name+"%' and rownumber > ("+page+"-1)*10";
        }
        List list=this.getJdbcTemplate().query(sql1,new AreaRowMapper());
        //返回总数量
        String sql2="select count(0) from Area";
        //sql2判断
        if(code!=null && name==null) {
        	sql2=sql2+" where code LIKE '%"+code+"%'";
        }else if(code==null && name!=null) {
        	sql2=sql2+" where name LIKE '%"+name+"%'";
        }else if(code!=null && name!=null) {
        	sql2=sql2+" where name LIKE '%"+name+"%' and code LIKE '%"+code+"%'";
        }
        
        int count=this.getJdbcTemplate().queryForInt(sql2);
        
        //返回数据
        Area2 ar=new Area2();
        ar.setCode("0");
        ar.setData(list);
        ar.setCount(count);
        
        return ar;
    }
	
	  class AreaRowMapper implements RowMapper<Map> {
	  
	  @Override public Map mapRow(ResultSet rs, int rowNum) throws SQLException {
	  Map map = new HashMap<>();
	  map.put("code",rs.getString("code"));
	  map.put("name",rs.getString("name"));
	 
	  return map; 
	  }
	}
}
