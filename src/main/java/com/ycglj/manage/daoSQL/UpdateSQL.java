package com.ycglj.manage.daoSQL;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycglj.manage.daoSQL.annotations.DBTable;
import com.ycglj.manage.daoSQL.annotations.QualifiWhere;
import com.ycglj.manage.daoSQL.annotations.QualifiWhereTerm;
import com.ycglj.manage.daoSQL.annotations.SQLDateTime;
import com.ycglj.manage.daoSQL.annotations.SQLDouble;
import com.ycglj.manage.daoSQL.annotations.SQLFloat;
import com.ycglj.manage.daoSQL.annotations.SQLInteger;
import com.ycglj.manage.daoSQL.annotations.SQLLong;
import com.ycglj.manage.daoSQL.annotations.SQLString;

public class UpdateSQL {

	public static Map<String, Object> get(Object object) throws ClassNotFoundException{
		Class className=object.getClass();
   	    String name = className.getName(); 
        Class<?> cl = Class.forName(name);                         //加载类，如果该类不在默认路径底下，会报 java.lang.ClassNotFoundException
        DBTable dbTable = cl.getAnnotation(DBTable.class);
        String tableName="";
        String[] columnWhere=null;
        String Term="AND";
        
        Map<String, Object> map=new HashMap<>();
        
        try{
            tableName = (dbTable.name().length()<1)?cl.getName():dbTable.name();//获取表的名字，如果没有在DBTable中定义，则获取类名作为Table的名字
           }catch (Exception e) {
   			// TODO: handle exception
            tableName =name;
   		 }
        
        List<String> columnDefs = new ArrayList<String>();
  
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
                    Integer iResult=AReflectGet.getIntMethods(object, className, field, columnName);
                    if(iResult!=null){
                     columnDefs.add(columnName);//使用一个方法，自己写的getConstraints(Constraints constraints)获取列定义
                     params.add(iResult);
                    }
                }else
                if(anns[0] instanceof SQLString)
                {
                    SQLString sStr = (SQLString)anns[0];
                    columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                    String sResult=AReflectGet.getStringMethods(object, className, field, columnName);
                    if(sResult!=null){
                     columnDefs.add(columnName);
                     params.add(sResult);
                    }
                }else
                if(anns[0] instanceof SQLFloat)
                {
                    SQLFloat sStr = (SQLFloat) anns[0];
                    columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                    Float fResult=AReflectGet.getFloatMethods(object, className, field, columnName);
                    if(fResult!=null){
                      columnDefs.add(columnName);
                      params.add(fResult);
                    }
                }else
                if(anns[0] instanceof SQLDouble)
                {
                     SQLDouble sStr = (SQLDouble) anns[0];
                     columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                     Double fResult=AReflectGet.getDoubleMethods(object, className, field, columnName);
                     if(fResult!=null){
                        columnDefs.add(columnName);
                        params.add(fResult);
                      }
                 }else
                 if(anns[0] instanceof SQLLong)
                 {
                       SQLLong sStr = (SQLLong) anns[0];
                       columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                       Long fResult=AReflectGet.getLongMethods(object, className, field, columnName);
                       if(fResult!=null){
                          columnDefs.add(columnName);
                           params.add(fResult);
                         }
                 }else
                if(anns[0] instanceof SQLDateTime)
                {
                    SQLDateTime sStr = (SQLDateTime) anns[0];
                    columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                    Date dResult=AReflectGet.getDateMethods(object, className, field, columnName);
                    if(dResult!=null){
                      columnDefs.add(columnName);
                      params.add(dResult);
                    }
                }else
                    if(anns[0] instanceof QualifiWhere)
                    {            	
                        QualifiWhere sStr = (QualifiWhere) anns[0];
                        columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                        columnWhere = AReflectGet.getArrayMethods(object, className, field, columnName);
                    }else
                        if(anns[0] instanceof QualifiWhereTerm)
                        {            	
                             QualifiWhereTerm sStr = (QualifiWhereTerm) anns[0];
                             columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                             String current= AReflectGet.getStringMethods(object, className, field, columnName);
                             if(current!=null){
                                 Term=current;
                              }
                         }
         }  
        
        StringBuilder insertCommand = new StringBuilder("UPDATE "+tableName+" SET ");
	
        for(String columnDef :columnDefs){
            insertCommand.append("\n    "+columnDef+"= ? ,");
        }
        
        String insert=insertCommand.substring(0,insertCommand.length()-1);

 
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
        
        insert=insert+" WHERE "+whereCommand.substring(0,whereCommand.length()-7);
            
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
            
        map.put("sql", insert);
        map.put("pamars", params);
        
        return map;  
	
	}
}
