package com.ycglj.manage.daoRowMapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.springframework.jdbc.core.RowMapper;

import com.ycglj.manage.daoSQL.annotations.SQLDateTime;
import com.ycglj.manage.daoSQL.annotations.SQLDouble;
import com.ycglj.manage.daoSQL.annotations.SQLFloat;
import com.ycglj.manage.daoSQL.annotations.SQLInteger;
import com.ycglj.manage.daoSQL.annotations.SQLLong;
import com.ycglj.manage.daoSQL.annotations.SQLString;

/*
 * 根据class类型动态生成mapRow的返回值
 */

public class RowMappers<T> implements RowMapper<T>{

	Class<?> className;
	
	public RowMappers(Class<?> className) {
		// TODO Auto-generated constructor stub
		this.className=className;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException{
		// TODO Auto-generated method stub
        
        Object object=null;
		try {
			object = className.newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}       
   	    String name = className.getName(); 
   	    Class<?> cl=null;
        try {
			cl = Class.forName(name);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

        for(Field field : cl.getDeclaredFields())                  //获取声明的属性
        {
        	String columnName = null;           
            Annotation[] anns = field.getDeclaredAnnotations();//获取注解，一个属性可以有多个注解，所以是数组类型
            if(anns.length < 1)
            {
                continue;
            }else
            if(anns[0] instanceof SQLInteger)                //判断注解类型
            {
                SQLInteger sInt = (SQLInteger)anns[0];
                columnName = (sInt.name().length()<1)?field.getName():sInt.name();//获取列名称与获取表名一样
                setIntMethods(rs, object, cl, field, columnName);

            }else
            if(anns[0] instanceof SQLString)
            {
                SQLString sStr = (SQLString)anns[0];
                columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                setStringMethods(rs,object, cl, field, columnName);
            }else
            if(anns[0] instanceof SQLFloat)
            {
                SQLFloat sStr = (SQLFloat) anns[0];
                columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                setFloatMethods(rs, object, cl, field, columnName);
            }else
            if(anns[0] instanceof SQLDouble)
            {
                 SQLDouble sStr =  (SQLDouble) anns[0];
                 columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                 setDoubleMethods(rs, object, cl, field, columnName);
             }else
             if(anns[0] instanceof SQLLong)
             {
                  SQLLong sStr = (SQLLong) anns[0];
                  columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                  setLongMethods(rs, object, cl, field, columnName);
             }else
             if(anns[0] instanceof SQLDateTime)
             {
                SQLDateTime sStr = (SQLDateTime) anns[0];
                columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                setDateTimeMethods(rs, object, cl, field, columnName);
             }
        }
   
        
        return (T) object;
	}

	public static void setStringMethods(ResultSet rs,Object object,Class className,Field field,String columnName){
        String filedName = field.getName();  
        //获取相应字段的getXXX()方法  
        String setMethodName = "set" + filedName.substring(0, 1).toUpperCase()  
                + filedName.substring(1);
     //   System.out.println("setMethodName="+setMethodName);
       try {
       	 Method setMethod =className.getDeclaredMethod(setMethodName,String.class);
     //  	 System.out.println("setmethod="+setMethod);
       	 String aa=rs.getString(columnName);
     //  	 System.out.println("aa="+aa);
		 setMethod.invoke(object,aa);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SQLException e) {  // ResultSet的异常
			// TODO Auto-generated catch block
			e.printStackTrace();
		   }
	}
	
	public static void setIntMethods(ResultSet rs,Object object,Class className,Field field,String columnName){
        String filedName = field.getName();  
        //获取相应字段的getXXX()方法  
        String setMethodName = "set" + filedName.substring(0, 1).toUpperCase()  
                + filedName.substring(1);
     //   System.out.println("setMethodName="+setMethodName);
       try {
       	 Method setMethod =className.getDeclaredMethod(setMethodName,Integer.class);
     //  	 System.out.println("setmethod="+setMethod);
       	     int aa=rs.getInt(columnName);
     //  	 System.out.println("aa="+aa);
		     setMethod.invoke(object,aa);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SQLException e) {  // ResultSet的异常
			// TODO Auto-generated catch block
			e.printStackTrace();
		   }
	}
	
	public static void setFloatMethods(ResultSet rs,Object object,Class className,Field field,String columnName){
        String filedName = field.getName();  
        //获取相应字段的getXXX()方法  
        String setMethodName = "set" + filedName.substring(0, 1).toUpperCase()  
                + filedName.substring(1);
     //   System.out.println("setMethodName="+setMethodName);
       try {
       	 Method setMethod =className.getDeclaredMethod(setMethodName,Float.class);
     //  	 System.out.println("setmethod="+setMethod);
       	     float aa=rs.getFloat(columnName);
     //  	 System.out.println("aa="+aa);
		     setMethod.invoke(object,aa);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SQLException e) {  // ResultSet的异常
			// TODO Auto-generated catch block
			e.printStackTrace();
		   }
	}
	
	public static void setDoubleMethods(ResultSet rs,Object object,Class className,Field field,String columnName){
        String filedName = field.getName();  
        //获取相应字段的getXXX()方法  
        String setMethodName = "set" + filedName.substring(0, 1).toUpperCase()  
                + filedName.substring(1);
     //   System.out.println("setMethodName="+setMethodName);
       try {
       	 Method setMethod =className.getDeclaredMethod(setMethodName,Double.class);
     //  	 System.out.println("setmethod="+setMethod);
       	     Double aa=rs.getDouble(columnName);
     //  	 System.out.println("aa="+aa);
		     setMethod.invoke(object,aa);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SQLException e) {  // ResultSet的异常
			// TODO Auto-generated catch block
			e.printStackTrace();
		   }
	}
	
	public static void setLongMethods(ResultSet rs,Object object,Class className,Field field,String columnName){
        String filedName = field.getName();  
        //获取相应字段的getXXX()方法  
        String setMethodName = "set" + filedName.substring(0, 1).toUpperCase()  
                + filedName.substring(1);
     //   System.out.println("setMethodName="+setMethodName);
       try {
       	 Method setMethod =className.getDeclaredMethod(setMethodName,Long.class);
     //  	 System.out.println("setmethod="+setMethod);
       	     Long aa=rs.getLong(columnName);
     //  	 System.out.println("aa="+aa);
		     setMethod.invoke(object,aa);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SQLException e) {  // ResultSet的异常
			// TODO Auto-generated catch block
			e.printStackTrace();
		   }
	}
	
	public static void setDateTimeMethods(ResultSet rs,Object object,Class className,Field field,String columnName){
        String filedName = field.getName();  
        //获取相应字段的getXXX()方法  
        String setMethodName = "set" + filedName.substring(0, 1).toUpperCase()  
                + filedName.substring(1);
     //   System.out.println("setMethodName="+setMethodName);
       try {
       	 Method setMethod =className.getDeclaredMethod(setMethodName,Date.class);
     //  	 System.out.println("setmethod="+setMethod);
       	     String aa=rs.getString(columnName);       	     
       	     SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
       	    if(aa!=null){
    	      Date date = sdf.parse(aa);
    	   // System.out.println("Date="+date);
    		  setMethod.invoke(object,date);
    	     }
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SQLException e) {  // ResultSet的异常
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	    }catch (NullPointerException e) {
			// TODO: handle exception
	    	e.printStackTrace();
		}
	}
}
