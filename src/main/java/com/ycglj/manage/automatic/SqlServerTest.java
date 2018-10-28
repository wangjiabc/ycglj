package com.ycglj.manage.automatic;

import java.sql.Connection;  
import java.sql.DatabaseMetaData;  
import java.sql.DriverManager;  
import java.sql.ResultSet;  
import java.sql.ResultSetMetaData;  
import java.sql.SQLException;  
import java.sql.Statement;  
import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;

import com.ycglj.manage.tools.MyTestUtil;  
/** 
 * 还在改进中。。。。。下面将要对比sqlserver2008和oracle类型的转换 
 * 从sqlserver取出来的列类型变换成oracle的，然后在oracle创建表 
 * 接着在拼接创建表结构的时候拼接insert语句，类型转换想做成xml格式。 
 * 以后方便不同数据库的类型转换。 
 * @author HJ 
 *2010-7-31 0:53 
 */  
public class SqlServerTest {  
  
    private static String JDBCURL= "jdbc:jtds:sqlserver://localhost:1433;DatabaseName=TTT";  
    private static String USER = "sa";  
    private static String PWD = "123";  
    private static Connection con = null;  
    private static Map<String,String> type= new HashMap<String, String>();  
      
    static{  
        try {  
            if(type != null){  
                type.put("int", "number");  
                type.put("text", "blob");  
                type.put("nvarchar", "varchar2");  
            }  
            Class.forName("net.sourceforge.jtds.jdbc.Driver");  
        } catch (ClassNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }  
      
    public static void main(String[] args) {  
        SqlServerTest t  = new SqlServerTest();  
        List<String> names = t.getAllTables("mssql", "");  
        for (int i = 0; i < names.size(); i++) {  
            System.out.println(names.get(i));  
          //  System.out.println(t.getTableStruct(names.get(i),"mssql"));  
        }  
          
    }  
      
      
    /** 
     * 获取连接 
     * @return 
     */  
    public static Connection getConnection(){  
          
        if(con == null){  
            try {  
                con = DriverManager.getConnection(JDBCURL,USER,PWD);  
            } catch (SQLException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }  
        return con;  
    }  
    /** 
     * 获取某种数据库中中用户的所有表 
     * @param type 
     * @param database 
     * @return 
     */  
    public List<String> getAllTables(String type,String database){  
          
        List<String> list = null;  
        if("mssql".equals(type)){  
            //查询所有用户表  
            StringBuffer sql = new StringBuffer("Select NAME FROM ")  
                               .append(database)  
                               .append("..SysObjects Where XType='U' orDER BY Name");  
            Statement sta = null;  
            ResultSet rs = null;  
            try {  
                sta = SqlServerTest.getConnection().createStatement();  
                rs = sta.executeQuery(sql.toString());  
                MyTestUtil.print(rs);
                while(rs.next()){  
                    if(list == null){  
                        list = new ArrayList<String>();  
                    }  
                    list.add(rs.getString("NAME"));  
                }  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }finally{  
                try {  
                    if(rs != null)  
                        rs.close();  
                    if(sta != null)  
                        sta.close();  
                } catch (SQLException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return list;  
    }  
    /** 
     * 获取表结构 
     * @param tableName 表名  
     * @param dataType 数据库类型 
     * @return 
     */  
    public String getTableStruct(String tableName,String dataType){  
          
        StringBuilder sb = null;  
        if("mssql".equals(dataType)){  
              
            sb = new StringBuilder("drop table if exists ")  
                              .append(tableName).append(";")  
                              .append("\n")  
                              .append("create table ")  
                              .append(tableName)  
                              .append("(")  
                              .append("\n\t");  
            String sql = "select * from "+tableName;  
            Statement sta = null;  
            ResultSet rs = null;  
            ResultSet key = null;  
            ResultSetMetaData ma = null;  
            DatabaseMetaData meta = null;  
            try {  
                meta =SqlServerTest.getConnection().getMetaData();//取得数据库连接元数据  
                //判断表中是否有主键列  
                key = meta.getPrimaryKeys(null, null, tableName);  
                String keyColumn = null;  
                if(key.next()){  
                    keyColumn = key.getString(4);  
                }  
                sta = SqlServerTest.getConnection().createStatement();  
                  
                rs = sta.executeQuery(sql.toString());  
                ma = rs.getMetaData();  
                  
                int countColumn = ma.getColumnCount();  
                  
                for(int i = 1;i<=countColumn; i++){  
                       
                    sb.append(ma.getColumnName(i)).append("\t").append(  
                               ma.getColumnTypeName(i));  
                     //判断长度是否为0  
                     if(!"0".equals(ma.getColumnDisplaySize(i))){  
                         sb.append("(").append(  
                                   ma.getColumnDisplaySize(i)).append(")");  
                     }  
                     //System.out.println(keyColumn);  
                        //判断是否为主键  
                     if(ma.getColumnName(i).equals(keyColumn)){  
                         sb.append("  primary key");  
                     }  
                      
                      // 判断字段是否能为空  
                     if(ma.isNullable(i) == ResultSetMetaData.columnNoNulls) {  
                         sb.append(" ").append("not null");  
                     }  
                     // 最后一列去掉逗号  
                     if (i != countColumn) {  
                         sb.append(", \n\t");  
                     } else {  
                         sb.append("\n");  
                     }  
                }  
                sb.append(")");  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }finally{  
                try {  
                    if(rs != null)  
                        rs.close();  
                    if(sta != null)  
                        sta.close();  
                } catch (SQLException e) {  
                    e.printStackTrace();  
                }  
            }    
              
        }  
        return sb.toString();  
    }  
}  