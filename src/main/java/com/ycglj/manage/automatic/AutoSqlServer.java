package com.ycglj.manage.automatic;

import java.io.InputStream;  
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;


import java.io.FileWriter;  
import java.io.PrintWriter;
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.ResultSetMetaData;  

class DBUtils {  
    private static Properties prop = new Properties();  
    static{  
        ClassLoader loader = DBUtils.class.getClassLoader();  
        InputStream in = loader.getResourceAsStream("jdbc.properties");  
        try {  
            prop.load(in);  
            Class.forName(prop.getProperty("driverClassName"));  
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }  
      
    public static Connection getConnection(String url) throws Exception{  
        return DriverManager.getConnection(url,prop.getProperty("username"),prop.getProperty("password"));  
    }  
      
    public static void close(Connection conn){  
        if(conn != null){  
            try {  
                conn.close();  
            } catch (SQLException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }  
    }  
       
}  
  
public class AutoSqlServer { 
	//包名
	private String packageName="package com.ycglj.manage.daoModel;\n\n";
	
    private String[] colNames;//列名数组  
    private String[] colType;//列名类型数组  
    private int[] colSize;//列名大小数组  
    private boolean f_util_date = false;//是否需要导入java.util.*  
    private boolean f_Clob = false;//是否需要导入java.sql.*  
    private boolean f_Blob = false;//是否需要导入java.sql.* 
    
    public AutoSqlServer(){}  
    public AutoSqlServer(String url,String dataBase,String tabName,String filePath){  
        Connection conn = null;  
        String sql = "select top 1 * from " + tabName;  
        System.out.println("sql="+sql);
        try {  
            conn = DBUtils.getConnection(url+dataBase);  
            PreparedStatement prep = conn.prepareStatement(sql);  
            ResultSetMetaData rsmd = prep.getMetaData(); 
            System.out.println("rsmd="+rsmd);
            int size = rsmd.getColumnCount();//共有多少列  
            colNames = new String[size];  
            colType = new String[size];  
            colSize = new int[size];  
            for(int i=0;i<rsmd.getColumnCount();i++){            	            	
                colNames[i] = rsmd.getColumnName(i+1);  
                colType[i] = rsmd.getColumnTypeName(i+1); 
                if(colType[i].equalsIgnoreCase("datetime")){  
                    f_util_date = true;  
                }
                if(colType[i].equalsIgnoreCase("text")){  
                    f_Clob = true;  
                }
                if(colType[i].equalsIgnoreCase("image")){  
                    f_Blob = true;  
                }
                colSize[i] = rsmd.getColumnDisplaySize(i+1);  
            }  
            String content = parse(dataBase,tabName, colNames, colType, colSize);  
            FileWriter fw = new FileWriter(filePath+initCap(tabName)+".java");  
            PrintWriter pw = new PrintWriter(fw);  
            pw.println(content);  
            pw.flush();  
            pw.close();  
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }finally{  
            DBUtils.close(conn);  
        }  
    }  
      
    /* 
     * 解析处理，生成java实体类主体代码 
     */  
    private String parse(String dataBase,String tabName,String[] colNames,String[] colType,int[] colSize){  
        StringBuffer sb = new StringBuffer();  
        if(packageName!=null&&!packageName.equals("")){
        	sb.append(packageName);
        }
        if(f_util_date){  
            sb.append("import java.util.Date;\r\n");  
            sb.append("\n");
        } 
        if(f_Clob){  
            sb.append("import java.sql.Clob;\r\n");  
            sb.append("\n");
        } 
        if(f_Blob){  
            sb.append("import java.sql.Blob;\r\n");  
            sb.append("\n");
        } 
        
        sb.append("import java.io.Serializable;\n\n");
        sb.append("import com.ycglj.manage.daoSQL.annotations.*;\n\n");
        
        String prefix="[";
        sb.append("@DBTable(name=\""+prefix+tabName+"]\")\n");
        sb.append("public class "+initCap(tabName)+" implements Serializable{\r\n"); 
        sb.append("\n");
        sb.append("    private static final long serialVersionUID = 1L;\n\n");
        processAllAttrs(sb);  
        processAllMethod(sb);  
        processBasicTerm(sb);  //不添加自己的查询函数
        sb.append("}\r\n");  
        return sb.toString();  
    }  
    
    
    /* 
     * 生成所有的方法 
     */  
    private void processAllMethod(StringBuffer sb){  
        for(int i=0;i<colNames.length;i++){  
            sb.append("\tpublic void set"+initCap(colNames[i])+"("+sqlType2JavaType(colType[i])+" "+colNames[i]+"){\r\n");  
            sb.append("\t\tthis."+colNames[i]+" = "+colNames[i]+";\r\n");  
            sb.append("\t}\r\n");  
            sb.append("\n");
              
            sb.append("\tpublic "+sqlType2JavaType(colType[i])+" get"+initCap(colNames[i])+"(){\r\n");  
            sb.append("\t\treturn "+colNames[i]+";\r\n");  
            sb.append("\t}\r\n"); 
            sb.append("\n");
        }  
    }  
      
    /* 
     * 解析输出属性 
     *  
     * @return 
     */  
    private void processAllAttrs(StringBuffer sb){  
        for(int i=0;i<colNames.length;i++){  
        	String varType=sqlType2JavaType(colType[i]);
        	/*
        	 * 添加动态生成SQL的注释
        	 */
        	if(varType!=null){
        	  if(varType.equals("String")){
        		sb.append("    @SQLString(name=\""+colNames[i]+"\")\n");
        	  }else
        	  if(varType.equals("Float")){
          		sb.append("    @SQLFloat(name=\""+colNames[i]+"\")\n");
          	  }else
          	  if(varType.equals("Double")){
              		sb.append("    @SQLDouble(name=\""+colNames[i]+"\")\n");
              }else
        	  if(varType.equals("Boolean")){
          		sb.append("    @SQLBoolean(name=\""+colNames[i]+"\")\n");
          	  }else
        	  if(varType.equals("Byte")){
          		sb.append("    @SQLByte(name=\""+colNames[i]+"\")\n");
          	  }else
        	  if(varType.equals("Date")){
            		sb.append("    @SQLDateTime(name=\""+colNames[i]+"\")\n");
              }else
        	  if(varType.equals("Integer")){
            		sb.append("    @SQLInteger(name=\""+colNames[i]+"\")\n");
              }else
        	  if(varType.equals("Long")){
          		sb.append("    @SQLLong(name=\""+colNames[i]+"\")\n");
          	  }else
        	  if(varType.equals("Short")){
            		sb.append("    @SQLShort(name=\""+colNames[i]+"\")\n");
              }else
              if(varType.equals("Clob")){
              		sb.append("    @SQLClob(name=\""+colNames[i]+"\")\n");
              }else
        	  if(varType.equals("Blob")){
            		sb.append("    @SQLBlob(name=\""+colNames[i]+"\")\n");
             }
        	}
            sb.append("\tprivate "+varType+" "+colNames[i]+";\r\n");  
            sb.append("\n");
        }  
    }  
      
    /* 
     * 把输入字符串的首字母变成大写 
     *  
     * @param str 
     * @return 
     *  
     */  
    private String initCap(String str){  
        char[] ch = str.toCharArray();  
        if(ch[0] >= 'a' && ch[0] <= 'z'){  
            ch[0] = (char) (ch[0] - 32);  
        }  
        return new String(ch);  
    }  
      
    private String sqlType2JavaType(String sqlType) {    
        if (sqlType.equalsIgnoreCase("bit")) {  
            return "Boolean";  
        } else if (sqlType.equalsIgnoreCase("tinyint")) {  
            return "Byte";  
        } else if (sqlType.equalsIgnoreCase("smallint")) {  
            return "Short";  
        } else if (sqlType.equalsIgnoreCase("int")) {  
            return "Integer";  
        } else if (sqlType.equalsIgnoreCase("bigint")) {  
            return "Long";  
        } else if (sqlType.equalsIgnoreCase("float")) {  
            return "Float";  
        } else if (sqlType.equalsIgnoreCase("decimal")  
                || sqlType.equalsIgnoreCase("numeric")  
                || sqlType.equalsIgnoreCase("double")  
                || sqlType.equalsIgnoreCase("real")) {  
            return "Double";  
        } else if (sqlType.equalsIgnoreCase("money")  
                || sqlType.equalsIgnoreCase("smallmoney")) {  
            return "Double";  
        } else if (sqlType.equalsIgnoreCase("varchar")  
                || sqlType.equalsIgnoreCase("char")  
                || sqlType.equalsIgnoreCase("nvarchar")  
                || sqlType.equalsIgnoreCase("nchar")) {  
            return "String";  
        } else if (sqlType.equalsIgnoreCase("datetime")  
                || sqlType.equalsIgnoreCase("date")) {  
            return "Date";  
        }  
  
        else if (sqlType.equalsIgnoreCase("image")) {  
            return "Blob";  
        } else if (sqlType.equalsIgnoreCase("text")) {  
            return "Clob";  
        }  
        return null;  
    }    
      
    /* 
     * 读取数据库中的表名 
     * @return表名的String数组 
     *  
     */  
    public static List<String> getTabNames(String url,String dataBase){  
        Connection conn = null;  
        String sql = "SELECT Name FROM "+dataBase+"..SysObjects Where XType='U' ORDER BY Name";  
        ArrayList<String> tabNames = null;  
          
        try {  
            conn = DBUtils.getConnection(url);  
            PreparedStatement  prep = conn.prepareStatement(sql);  
            ResultSet rs = prep.executeQuery();   
            tabNames = new ArrayList<String>();  
            int i=0;  
            while(rs.next()){  
                tabNames.add(rs.getString("NAME"));  
                i++;  
            }  
              
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }finally{  
            DBUtils.close(conn);  
        }  
        return tabNames;  
    }  
      
    
    private void processBasicTerm(StringBuffer sb){ //添加basicTerm
    	sb.append("\n\n\n/*\n*数据库查询参数\n*/\n");
    	sb.append("    @QualifiLimit(name=\"limit\")\n");
    	sb.append("    private Integer limit;\n");
    	sb.append("    @QualifiOffset(name=\"offset\")\n");
    	sb.append("    private Integer offset;\n");
    	sb.append("    @QualifiNotIn(name=\"notIn\")\n");
    	sb.append("    private String notIn;\n");
    	sb.append("    @QualifiSort(name=\"sort\")\n");
    	sb.append("    private String sort;\n");
    	sb.append("    @QualifiOrder(name=\"order\")\n");
    	sb.append("    private String order;\n");
    	sb.append("    @QualifiWhere(name=\"where\")\n");
    	sb.append("    private String[] where;\n");
    	sb.append("    @QualifiWhereTerm(name=\"whereTerm\")\n");
    	sb.append("    private String whereTerm;\n");
    	sb.append("\n\n");
    	
    	String[] term={"limit","offset","notIn","sort","order","where","whereTerm"};
    	String[] termType={"Integer","Integer","String","String","String","String[]","String"};
    	 for(int i=0;i<term.length;i++){  
             sb.append("\tpublic void set"+initCap(term[i])+"("+termType[i]+" "+term[i]+"){\r\n");  
             sb.append("\t\tthis."+term[i]+" = "+term[i]+";\r\n");  
             sb.append("\t}\r\n");  
             sb.append("\n");
               
             sb.append("\tpublic "+termType[i]+" get"+initCap(term[i])+"(){\r\n");  
             sb.append("\t\treturn "+term[i]+";\r\n");  
             sb.append("\t}\r\n"); 
             sb.append("\n");
         }  
    }
    
    public static void main(String[] args){
        String url="jdbc:jtds:sqlserver://127.0.0.1:1433/";
    	String dataBase="YC";
    	String filePath="C:\\Users\\WangJing\\Desktop\\pasoft\\YC\\";
    	List<String> tabNames = getTabNames(url,dataBase);  
        Iterator<String> iterator=tabNames.iterator();
        int i=0;
        while (iterator.hasNext()) {
        	String tabName=iterator.next();
        	new AutoSqlServer(url,dataBase,tabName,filePath);  
        	System.out.println(i+"   "+tabName);
        	i++;
		}       
    }  
}  


