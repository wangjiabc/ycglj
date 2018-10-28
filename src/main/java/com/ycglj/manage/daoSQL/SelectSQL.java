package com.ycglj.manage.daoSQL;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycglj.manage.daoSQL.annotations.DBTable;
import com.ycglj.manage.daoSQL.annotations.QualifiLimit;
import com.ycglj.manage.daoSQL.annotations.QualifiNotIn;
import com.ycglj.manage.daoSQL.annotations.QualifiOffset;
import com.ycglj.manage.daoSQL.annotations.QualifiOrder;
import com.ycglj.manage.daoSQL.annotations.QualifiSort;
import com.ycglj.manage.daoSQL.annotations.QualifiWhere;
import com.ycglj.manage.daoSQL.annotations.QualifiWhereTerm;
import com.ycglj.manage.daoSQL.annotations.SQLDateTime;
import com.ycglj.manage.daoSQL.annotations.SQLDouble;
import com.ycglj.manage.daoSQL.annotations.SQLFloat;
import com.ycglj.manage.daoSQL.annotations.SQLInteger;
import com.ycglj.manage.daoSQL.annotations.SQLLong;
import com.ycglj.manage.daoSQL.annotations.SQLString;

public class SelectSQL {
	
	public static Map<String, Object> get(Object object) throws ClassNotFoundException{
		Class className=object.getClass();
   	    String name = className.getName();                                    //从控制台输入一个类名，我们输入User即可
        Class<?> cl = Class.forName(name);                         //加载类，如果该类不在默认路径底下，会报 java.lang.ClassNotFoundException
        DBTable dbTable = cl.getAnnotation(DBTable.class);         //从User类中获取DBTable注解
        Integer limit=10;
		Integer offset=0; 
		String notIn="";
    	String sort=null;
		String order=null;
        String tableName="";
        String Term="AND";
        
        Map<String, Object> map=new HashMap<>();
        
       try{
         tableName = (dbTable.name().length()<1)?cl.getName():dbTable.name();//获取表的名字，如果没有在DBTable中定义，则获取类名作为Table的名字
        }catch (Exception e) {
			// TODO: handle exception
         tableName =name;
		 }
         List<String> columnDefs = new ArrayList<String>();
         String[] columnWhere=null;
         boolean term=false;       //判断是否有where
         List<Object> params=new ArrayList<Object>();
         
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
                columnDefs.add(columnName);//使用一个方法，自己写的getConstraints(Constraints constraints)获取列定义
            }else
            if(anns[0] instanceof SQLString)
            {
                SQLString sStr = (SQLString)anns[0];
                columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                columnDefs.add(columnName);
            }else
            if(anns[0] instanceof SQLFloat)
            {
                SQLFloat sStr = (SQLFloat) anns[0];
                columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                columnDefs.add(columnName);
            }else
            if(anns[0] instanceof SQLDouble)
            {
                 SQLDouble sStr =  (SQLDouble) anns[0];
                 columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                 columnDefs.add(columnName);
            }else
            if(anns[0] instanceof SQLLong)
            {
                 SQLLong sStr = (SQLLong) anns[0];
                 columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                 columnDefs.add(columnName);
            }else
            if(anns[0] instanceof SQLDateTime)
            {
                SQLDateTime sStr = (SQLDateTime) anns[0];
                columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                columnDefs.add(columnName);
            }else
            if(anns[0] instanceof QualifiWhere)
            {            	
                QualifiWhere sStr = (QualifiWhere) anns[0];
                columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                columnWhere = AReflectGet.getArrayMethods(object, className, field, columnName);
                System.out.println("columnWhere="+columnWhere);
                if(columnWhere!=null){
                	term=true;
                }
            }else
            if(anns[0] instanceof QualifiWhereTerm)
            {            	
                 QualifiWhereTerm sStr = (QualifiWhereTerm) anns[0];
                 columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                 String current= AReflectGet.getStringMethods(object, className, field, columnName);
                 System.out.println("columnWhere="+columnWhere);
                 if(current!=null){
                     Term=current;
                  }
             }else
            if(anns[0] instanceof QualifiLimit)
            {
            	 QualifiLimit sStr = (QualifiLimit) anns[0];
                 columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                 
                 String filedName = field.getName();  
                 limit=AReflectGet.getIntMethods(object, className, field,columnName);
                 System.out.println("limit="+limit);
             }else	
            if(anns[0] instanceof QualifiOffset)
            {
            	QualifiOffset sStr = (QualifiOffset) anns[0];
                columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                offset=AReflectGet.getIntMethods(object, className, field,columnName);                
            }else
             if(anns[0] instanceof QualifiNotIn)
             {
            	QualifiNotIn sStr = (QualifiNotIn) anns[0];
                columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                notIn=AReflectGet.getStringMethods(object, className, field,columnName);
             }else	
            if(anns[0] instanceof QualifiSort)
            {
            	QualifiSort sStr = (QualifiSort) anns[0];
                columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                 sort=AReflectGet.getStringMethods(object, className, field,columnName);
              //  System.out.println("sort="+sort);
             }else
            if(anns[0] instanceof QualifiOrder)
            {
            	QualifiOrder sStr = (QualifiOrder) anns[0];
                columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                order=AReflectGet.getStringMethods(object, className, field,columnName);
             //   System.out.println("order="+order);
             }
        }
        
        StringBuilder selectCommand = new StringBuilder("SELECT top (?)");
        
               
        for(String columnDef :columnDefs){
            selectCommand.append("\n    "+columnDef+",");
        }
        
       
        params.add(0,limit); //把limit插入到最前面
        
        String select=selectCommand.substring(0,selectCommand.length()-1)+"\n FROM \n   "+tableName;
        
        
        
        if(term){
          StringBuilder whereCommand = new StringBuilder();
          int i=1;
          for(String whereterm:columnWhere){
        	  if(i%2==0){
        		//  System.out.println("偶数");
        	   }else{
        		//  System.out.println("奇数");
        		   whereCommand.append(whereterm+"? \n  "+Term+" ");
        	   }
           i++;
          }
       /*   select=select+   //sqlserver分页需要在top也加上where条件
            		 "\n  where "+notIn+
                     " not in("+
                     " select top "+offset+" "+notIn+" FROM "+tableName+" where "+
                      whereCommand.substring(0,whereCommand.length()-7)+")";
          select=select+"\n  AND "+whereCommand.substring(0,whereCommand.length()-7);*/
          select=select+   //sqlserver分页需要在top也加上where条件
         		 "\n  where "+notIn+
                  " not in("+
                  " select top "+offset+" "+notIn+" FROM "+tableName+" where "+
                   whereCommand.substring(0,whereCommand.length()-7);
           if(sort!=null){
          	select=select+" ORDER BY "+sort;
            }
          
           if(order!=null&&sort!=null){       	
          	select=select+" "+order;        	
           }
          
          select=select+")";
          
          i=1;
          for(String whereterm:columnWhere){
        	  if(i%2==0){
        		//  System.out.println("偶数");
        		  params.add(whereterm);
        	   }else{
        		//  System.out.println("奇数");
        		 
        	   }
              i++;
          }

          select=select+"\n  AND "+whereCommand.substring(0,whereCommand.length()-7);

          i=1;
          for(String whereterm:columnWhere){
        	  if(i%2==0){
        		//  System.out.println("偶数");
        		  params.add(whereterm);
        	   }else{
        		//  System.out.println("奇数");
        		  
        	   }
             i++;
           }
        }else{
        /*	select=select+
           		 "\n  where "+notIn+
                    " not in("+
                    " select top "+offset+" "+notIn+" FROM "+tableName+")";*/
        	select=select+
              		 "\n  where "+notIn+
                       " not in("+
                       " select top "+offset+" "+notIn+" FROM "+tableName;
        	
        	 if(sort!=null){
               	select=select+" ORDER BY "+sort;
                 }
               
                if(order!=null&&sort!=null){       	
               	select=select+" "+order;        	
                }
               
             select=select+")";
        }
       
        
        if(sort!=null){
        	System.out.println("selectsql order="+sort);
        	select=select+" ORDER BY "+sort;
        }
        
        if(order!=null&&sort!=null){       	
        	select=select+" "+order;        	
        }
        
        map.put("sql", select);
        map.put("params", params);
        return map;
   }
	
	public static Map<String, Object> getCount(Object object) throws ClassNotFoundException{
		    Class className=object.getClass();
   	        String name = className.getName();                                    //从控制台输入一个类名，我们输入User即可
	        Class<?> cl = Class.forName(name);                         //加载类，如果该类不在默认路径底下，会报 java.lang.ClassNotFoundException
	        DBTable dbTable = cl.getAnnotation(DBTable.class);         //从User类中获取DBTable注解
	        String tableName="";
	        List<Object> params=new ArrayList<Object>();
	        String Term="AND";
	        
	        Map<String, Object> map=new HashMap<>();
	        
	        try{
	         tableName = (dbTable.name().length()<1)?cl.getName():dbTable.name();//获取表的名字，如果没有在DBTable中定义，则获取类名作为Table的名字
	        }catch (Exception e) {
				// TODO: handle exception
	         tableName =name;
			 }
	         List<String> columnDefs = new ArrayList<String>();
	         String[] columnWhere=null;
	         boolean term=false;
	        for(Field field : cl.getDeclaredFields())                  //获取声明的属性
	        {
	            String columnName = null;           
	            Annotation[] anns = field.getDeclaredAnnotations();//获取注解，一个属性可以有多个注解，所以是数组类型
	            if(anns.length < 1)
	            {
	                continue;
	            }else	            
	            if(anns[0] instanceof QualifiWhere)
	            {	            	
	                QualifiWhere sStr = (QualifiWhere) anns[0];
	                columnWhere = AReflectGet.getArrayMethods(object, className, field, columnName);
	                if(columnWhere!=null){
	                	term=true;
	                }
	            }else
	            if(anns[0] instanceof QualifiWhereTerm)
	            {            	
	                 QualifiWhereTerm sStr = (QualifiWhereTerm) anns[0];
	                 columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
	                 String current= AReflectGet.getStringMethods(object, className, field, columnName);
	                 System.out.println("columnWhere="+columnWhere);
	                 if(current!=null){
	                     Term=current;
	                  }
	             }
	        }
	        
	        StringBuilder selectCommand = new StringBuilder("SELECT COUNT(*)");

	        String select=selectCommand+"\n FROM \n   "+tableName;
	        
	        if(term){
	            StringBuilder whereCommand = new StringBuilder();
	            int i=1;
	            for(String whereterm:columnWhere){
	          	  if(i%2==0){
	          		//  System.out.println("偶数");
	          	   }else{
	          		//  System.out.println("奇数");
	          		   whereCommand.append(whereterm+"? \n  "+Term+" ");
	          	   }
	             i++;
	            }
	        select=select+   //sqlserver分页需要在top也加上where条件
	           		 "\n  where "+
	                     whereCommand.substring(0,whereCommand.length()-7);
	            i=1;
	            for(String whereterm:columnWhere){
	          	  if(i%2==0){
	          		//  System.out.println("偶数");
	          		  params.add(whereterm);
	          	   }else{
	          		//  System.out.println("奇数");
	          		 
	          	   }
	                i++;
	            }

	          
	          }
	        
	          map.put("sql", select);
	          map.put("params", params);
	              
	        return map;
	   }
	
}
