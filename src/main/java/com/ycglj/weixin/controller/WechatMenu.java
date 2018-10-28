package com.ycglj.weixin.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.ycglj.manage.model.WeiXin;
import com.ycglj.manage.service.WeiXinService;
import com.ycglj.weixin.base.AdvancedUtil;
import com.ycglj.weixin.base.CommonUtil;
import com.ycglj.weixin.util.HttpUtils;

@Controller
@RequestMapping("/wechatMenu")
public class WechatMenu {

   private WeiXinService weixinService;
	
	@Autowired
	public void setAccessTokenService(WeiXinService weiXinService) {
		this.weixinService=weiXinService;
	}
	
	@RequestMapping("/query")
	public @ResponseBody JSONObject
	query(HttpServletRequest request,@RequestParam String type){  //type��Ҫ��ѯ������
        WeiXin weixin;
        String url="";
		Integer campusId=0;
		String accessToken,appId, appSecret;
		 
		Cookie[] cookies = request.getCookies();  
		if(cookies!=null){
			for(Cookie i:cookies){
				if(i.getName().equalsIgnoreCase("campusId"))
					campusId=Integer.parseInt(i.getValue());
			}
		}
		
		System.out.println("campudid="+campusId);
		
		weixin=weixinService.getCampusById(campusId);
		
		accessToken=weixin.getAccessToken();
		System.out.println("type="+type);
		if(type.equals("menu")){
		 url="https://api.weixin.qq.com/cgi-bin/"+type+"/get?access_token=";
		}else if(type.equals("get_material")){
		 url="https://api.weixin.qq.com/cgi-bin/material/"+type+"?access_token=";
		}
		
		 JSONObject jsonObject = CommonUtil.httpsRequest(url+accessToken, "GET", null);
		 System.out.println(url+accessToken);
		 System.out.println("menu json="+jsonObject);
		 try{
		 if(jsonObject.getString("errcode").equals("40001")||jsonObject.getString("errcode").equals("42001")){
			 appId=weixin.getAppId();
             appSecret=weixin.getAppSecret();
             accessToken=AdvancedUtil.getAccessToken(appId, appSecret); 
             Map<String, Object> paramMap=new HashMap<>();
             accessToken=AdvancedUtil.getAccessToken(appId, appSecret);
             paramMap.put("accessToken", accessToken);
     		 paramMap.put("campusId", campusId);
     		 Date date=new Date();
     		 paramMap.put("createTime", date);
     		 System.out.println("errorcode="+jsonObject.getString("errcode")+"   accessToken="+accessToken);
     		 weixinService.updateCampusById(paramMap);
             
             jsonObject = CommonUtil.httpsRequest(url+accessToken, "GET", null);
		 }
		 }catch (Exception e) {
			// TODO: handle exception
		}
		 System.out.println("json="+jsonObject);
		 
		 return jsonObject;
	}
	
	@RequestMapping("/set")
	public @ResponseBody JSONObject
	set(HttpServletRequest request,@RequestParam String menu){
		WeiXin weixin;
		
		Integer campusId=0;
		 String accessToken,appId, appSecret;
		 
		Cookie[] cookies = request.getCookies();  
		if(cookies!=null){
			for(Cookie i:cookies){
				if(i.getName().equalsIgnoreCase("campusId"))
					campusId=Integer.parseInt(i.getValue());
			}
		}
		
		System.out.println("campudid="+campusId);
		
		weixin=weixinService.getCampusById(campusId);
		
		accessToken=weixin.getAccessToken();
		
		//这里为请求接口的 url   +号后面的是 token
	        String url="https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+accessToken;
	        
	        JSONObject jObject;
	        
	        try{
	            String rs=HttpUtils.sendPostBuffer(url, menu);
	            System.out.println("rs="+rs);
	            JSONObject jsonObject=JSONObject.parseObject(rs);
	            jObject=JSONObject.parseObject(rs);
	            if(jsonObject.getString("errcode").equals("40001")||jsonObject.getString("errcode").equals("42001")){
	            	appId=weixin.getAppId();
	                appSecret=weixin.getAppSecret();
	                Map<String, Object> paramMap=new HashMap<>();
	                accessToken=AdvancedUtil.getAccessToken(appId, appSecret);
	                paramMap.put("accessToken", accessToken);
	        		 paramMap.put("campusId", campusId);
	        		 paramMap.put("campusId", campusId);
	         		 Date date=new Date();
	         		 paramMap.put("createTime", date);
	        		 System.out.println("errorcode="+jsonObject.getString("errcode")+"   accessToken="+accessToken);
	        		 weixinService.updateCampusById(paramMap);
	                url="https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+accessToken;
	                
	                rs=HttpUtils.sendPostBuffer(url, menu);
	                
	                jObject=JSONObject.parseObject(rs);
	                
	                return jObject;
	                
	            }
	            
	            return jObject;
	        }catch(Exception e){
	            System.out.println("请求错误");
	            jObject=JSONObject.parseObject("error");
	            return jObject;
	        }

	    }
	
}
