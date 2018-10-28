package com.ycglj.manage.automatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class test {
 public static void main(String[] args) {
	 String url="jdbc:jtds:sqlserver://127.0.0.1:1433/";
  	 String dataBase="test";
	 Connection conn = null;  
     String sql = "SELECT Name FROM "+dataBase+"..SysObjects Where XType='U' ORDER BY Name";  
     ArrayList<String> tabNames = null;  
	  try {  
          conn = DBUtils.getConnection(url+dataBase);  
          PreparedStatement  prep = conn.prepareStatement(sql);  
          ResultSet rs = prep.executeQuery();   
          tabNames = new ArrayList<String>();  
          int i=0;  
          while(rs.next()){  
              tabNames.add(rs.getString("NAME"));  
              System.out.println(rs.getString("NAME"));
              i++;  
          }  
            
      } catch (Exception e) {  
          // TODO Auto-generated catch block  
          e.printStackTrace();  
      }finally{  
          DBUtils.close(conn);  
      }  
}
}
