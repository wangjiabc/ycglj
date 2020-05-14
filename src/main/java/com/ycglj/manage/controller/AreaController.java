package com.ycglj.manage.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ycglj.manage.dao.AreaDAO;
import com.ycglj.manage.model.Area2;
import com.ycglj.sqlserver.context.Connect;


@Controller
@RequestMapping("/baiduMap/Area")
public class AreaController {
	
     ApplicationContext applicationContext=new Connect().get();
    
     AreaDAO areaDAO=(AreaDAO) applicationContext.getBean("areadao");
     
	@RequestMapping("area")
	public @ResponseBody Area2 queryArea(String code,String name,Integer limit,Integer page, HttpServletRequest request,
			HttpServletResponse response){
		
		return areaDAO.queryArea(code, name, limit, page);
		
	}	
}
