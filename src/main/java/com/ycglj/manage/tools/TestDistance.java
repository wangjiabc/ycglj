package com.ycglj.manage.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestDistance {

	public static void main(String[] args) {
		String aString="10000";
		
		System.out.println(get(aString));
	}
	
	public static double get(String str) { 
		
        Pattern p1 = null;
        Matcher m = null;
        boolean b = false;  

        double i=0;

        	 
        	 p1 = Pattern.compile("^-?[0-9]+$");
	         m = p1.matcher(str);
	         
	         b = m.matches(); 
        	 
	         
	         if(b){
	        	 i=Double.parseDouble(str);
	         }
	         
        	 return i;

     }
	
}
