package com.ycglj.manage.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ycglj.manage.dao.NavigationDAO;
import com.ycglj.manage.model.Navigation2;
import com.ycglj.sqlserver.context.Connect;


@Controller
@RequestMapping("/baiduMap/Navication")
public class NavigationController {
	
     ApplicationContext applicationContext=new Connect().get();
    
     NavigationDAO navigationDAO=(NavigationDAO) applicationContext.getBean("navigationDAO");
     
	@RequestMapping("navigation")
	public @ResponseBody Navigation2 queryNavigation(String name, HttpServletRequest request,
			HttpServletResponse response){
		
		return navigationDAO.queryNavigation(name);
		
	}	
}
