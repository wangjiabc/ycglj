package com.ycglj.manage.tools;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TransMapToString {
	/** 
	 * 方法名称:transMapToString 
	 * 传入参数:map 
	 * 返回值:String[]
	*/  
	public static String[] get(Map<String,String> map){  
		Set  set=map.entrySet();   
		int size=map.size();
	//	System.out.print(size);
		String[] value=new String[size*2];
		Iterator iterator=set.iterator();     
		int i=0;
		while (iterator.hasNext()){       		    
		    Map.Entry  mapentry = (Map.Entry) iterator.next();       		    
		    value[i]=(String) mapentry.getKey();
		  //  System.out.println(i+"  "+mapentry.getKey());
		    value[i+1]=(String) mapentry.getValue(); 
		  //  System.out.println(i+"  "+mapentry.getValue());
		    i=i+2;
		}   
	  return value; 
	}  
}
