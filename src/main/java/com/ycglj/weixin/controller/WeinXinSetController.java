package com.ycglj.weixin.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ycglj.manage.model.WeiXin;
import com.ycglj.manage.service.WeiXinService;



@Controller
@RequestMapping("/weixinset")
public class WeinXinSetController {

private WeiXinService weixinService;
	
	@Autowired
	public void setAccessTokenService(WeiXinService weiXinService) {
		this.weixinService=weiXinService;
	}
	
	@RequestMapping("getCampusInfo")
	public @ResponseBody Map
	getCampusInfo(HttpServletRequest request){		
		String campusAdmin;
		Short type;
        Integer  campusId,cityId,currentId;
		List<WeiXin> weixins=new ArrayList<>();
		WeiXin weixin;
		
		HttpSession session=request.getSession();  //取得session的type变量，判断是否为公众号管理员
		type=(Short) session.getAttribute("type");
		cityId=(Integer) session.getAttribute("cityId");

		//服务器端的相对地址
		String homeUrl=request.getHeader("Host")+request.getContextPath();
		
		if(type==0){
		   weixins=weixinService.getAllCampusById(cityId);
		 }
		
		Iterator<WeiXin> iterator=weixins.iterator();
		
		while (iterator.hasNext()) {
			weixin=iterator.next();
			currentId=weixin.getCampusId();
			weixin.setUrl("http://"+homeUrl+"/wechat/security.do?campusId="+currentId);
		}
		
		Map map=new HashMap<>();
		
		map.put("code", "0");
		
		map.put("data", weixins);
		
		return map;
		
	}
	
	@RequestMapping("insertIntoCampus")
	public @ResponseBody 
	Integer insetIntoCampus(HttpServletRequest request , String campusName
			, String customService,String appId ,  String appSecret
			,String token, String userName) {
		Map<String, Object> paramMap=new HashMap<>();
		Integer  cityId , flag ;
		WeiXin be=null;
		
		HttpSession session=request.getSession();  
		cityId=(Integer) session.getAttribute("cityId");
		Integer campusId=(Integer) session.getAttribute("campusId");
		
		Short type=(Short)session.getAttribute("type");
		if(type==1){
			return 0;
		}
				
		paramMap.put("campusName", campusName);
		paramMap.put("cityId", cityId);
		paramMap.put("customService", customService);
		paramMap.put("userName", userName);
		paramMap.put("appId", appId);
		paramMap.put("appSecret", appSecret);
		paramMap.put("token", token);
		paramMap.put("campusId", campusId);
		 Date date=new Date();
		 paramMap.put("createTime", date);
		if(campusId!=null){
		   be=weixinService.getByCampusIds(campusId);
		}
		System.out.println("be="+be+ "         campusid="+campusId);
		if(be==null){
		 flag=weixinService.insertIntoCampus(paramMap);
		}else{
		  paramMap.put("campusId", campusId);
		  flag=weixinService.updateCampusById(paramMap);
		}
		return flag;
	}
	
}
