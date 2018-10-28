package com.ycglj.manage.daoSQL;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.ycglj.manage.daoSQL.annotations.Constraints;
import com.ycglj.manage.daoSQL.annotations.DBTable;
import com.ycglj.manage.daoSQL.annotations.SQLDateTime;
import com.ycglj.manage.daoSQL.annotations.SQLFloat;
import com.ycglj.manage.daoSQL.annotations.SQLInteger;
import com.ycglj.manage.daoSQL.annotations.SQLString;


public class CreateSQL {
	public static String getConstraints(Constraints con)
    {
        String constraints = "";
        if(!con.allownull())
        {
        	System.out.println(con.allownull());
            constraints +=" NOT NULL";
        }
        if(con.primarykey())
        {
            constraints += " PRIMARY KEY";
        }
        return constraints;
    }
	
	public static String get(Class className) throws ClassNotFoundException{
        String name = className.getName();                                    //从控制台输入一个类名，我们输入User即可
        Class<?> cl = Class.forName(name);                         //加载类，如果该类不在默认路径底下，会报 java.lang.ClassNotFoundException
        DBTable dbTable = cl.getAnnotation(DBTable.class);         //从User类中获取DBTable注解
        if(dbTable == null){                                       //如果没有DBTable注解，则直接返回，我们写了，当然有
            return null;
        }
        String tableName = (dbTable.name().length()<1)?cl.getName():dbTable.name();//获取表的名字，如果没有在DBTable中定义，则获取类名作为Table的名字
        List<String> columnDefs = new ArrayList<String>();
        for(Field field : cl.getDeclaredFields())                  //获取声明的属性
        {
            String columnName = null;
            Annotation[] anns = field.getDeclaredAnnotations();//获取注解，一个属性可以有多个注解，所以是数组类型
            if(anns.length < 1)
            {
                continue;
            }
            if(anns[0] instanceof SQLInteger)                //判断注解类型
            {
                SQLInteger sInt = (SQLInteger)anns[0];
                columnName = (sInt.name().length()<1)?field.getName():sInt.name();//获取列名称与获取表名一样
                columnDefs.add(columnName+" INT"+getConstraints(sInt.constraints()));//使用一个方法，自己写的getConstraints(Constraints constraints)获取列定义
            }else
            if(anns[0] instanceof SQLString)
            {
                SQLString sStr = (SQLString)anns[0];
                columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                columnDefs.add(columnName + " VARCHAR("+sStr.value()+")"+getConstraints(sStr.constraints()));
            }else
            if(anns[0] instanceof SQLFloat)
            {
                SQLFloat sStr = (SQLFloat) anns[0];
                columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                columnDefs.add(columnName + " FLOAT"+getConstraints(sStr.constraints()));
            }else
            if(anns[0] instanceof SQLDateTime)
            {
                SQLDateTime sStr =  (SQLDateTime) anns[0];
                columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                columnDefs.add(columnName + " DATETIME"+getConstraints(sStr.constraints()));
            }
            
        }
        StringBuilder createCommand = new StringBuilder("CREATE TABLE "+tableName+"(");
        for(String columnDef :columnDefs)
        {
            createCommand.append("\n    "+columnDef+",");
        }
        String tableCreate = createCommand.substring(0,createCommand.length()-1)+"\n);";
        return tableCreate;
    }
}
