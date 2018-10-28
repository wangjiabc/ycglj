package com.ycglj.weixin.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ycglj.manage.model.Users;
import com.ycglj.manage.model.WeiXin;
import com.ycglj.manage.service.UserService;
import com.ycglj.manage.service.WeiXinService;
import com.ycglj.weixin.base.AdvancedUtil;
import com.ycglj.weixin.base.SNSUserInfo;
import com.ycglj.weixin.base.WeixinOauth2Token;

import cn.jpush.api.report.UsersResult.User;

/**
* 类名: OAuthServlet </br>
* 描述: 授权后的回调请求处理 </br>
* 开发人员： souvc </br>
* 创建时间：  2015-11-27 </br>
* 发布版本：V1.0  </br>
 */
@Controller
@RequestMapping("/oauth")
public class OAuthServletController{
    
	private WeiXinService weixinService;
	
	private UserService userService;
	
	@Autowired
	public void setweixinService(WeiXinService weiXinService) {
		this.weixinService=weiXinService;
	}
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping("/getUserInfo")
    public @ResponseBody
    Map<String, Object> getUserInfo(HttpServletRequest request, HttpServletResponse response,
    		 @RequestParam Integer campusId) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        
        Map<String, Object> map=new HashMap<>();
        
        // 获取微信服务器返回的code
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String errorCode=null;
        
        HttpSession session = request.getSession();
        
        // 网页授权接口访问凭证
        String accessToken,appId, appSecret;
        WeiXin weiXin;

        weiXin=weixinService.getCampusById(campusId);
        
        appId=weiXin.getAppId();
        appSecret=weiXin.getAppSecret();
        
            // 获取网页授权access_token
         WeixinOauth2Token weixinOauth2Token = AdvancedUtil.getOauth2AccessToken(appId, appSecret, code);
            // 网页授权接口访问凭证
        	
            
         accessToken=weiXin.getAccessToken();
         System.out.println("accesstoke="+accessToken);   
        if(weixinOauth2Token!=null){
        	// 用户标识
            String openId = weixinOauth2Token.getOpenId();
            

        	// 获取用户信息
            SNSUserInfo snsUserInfo;
                
			snsUserInfo = AdvancedUtil.getSNSUserInfo(accessToken, openId);
      
			try {
			   errorCode=snsUserInfo.getErrorCode();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			System.out.println("errorCode="+errorCode);
          if(errorCode!=null){
        	  if(errorCode.equals("40001")||errorCode.equals("42001")){
        		 accessToken=AdvancedUtil.getAccessToken(appId, appSecret);
        		 Map<String, Object> paramMap=new HashMap<>();
        		 
        		 paramMap.put("accessToken", accessToken);
        		 paramMap.put("campusId", campusId);
        		 Date date=new Date();
       		     paramMap.put("createTime", date);
        		 System.out.println("errorcode="+errorCode+"   accessToken="+accessToken);
        		 weixinService.updateCampusById(paramMap);
        		 
        		 snsUserInfo = AdvancedUtil.getSNSUserInfo(accessToken, openId);
        		 
        		// 设置要传递的参数
        		 map.put("campusName", weiXin.getCampusName());
               //  map.put("openId", snsUserInfo.getOpenId());
                 map.put("subscribe", snsUserInfo.getSubScribe());
                 map.put("nickName", snsUserInfo.getNickname());
                 map.put("sex", snsUserInfo.getSex());
                 map.put("country", snsUserInfo.getCountry());
                 map.put("province", snsUserInfo.getProvince());
                 map.put("city", snsUserInfo.getCity());
                 map.put("headimgUrl", snsUserInfo.getHeadImgUrl());
                 map.put("subscribeTime", snsUserInfo.getSubScribeTime());
                 map.put("language", snsUserInfo.getLanguage());
                                 
         		 session.setAttribute("openId", snsUserInfo.getOpenId());
         		 session.setAttribute("campusId", campusId);
        		 
        	  }
        	  System.out.println("access_token errorCode : "+snsUserInfo.getErrorCode());
          }else{
            // 设置要传递的参数
        	  map.put("campusName", weiXin.getCampusName());
        	 // map.put("openId", snsUserInfo.getOpenId());
              map.put("subscribe", snsUserInfo.getSubScribe());
              map.put("nickName", snsUserInfo.getNickname());
              map.put("sex", snsUserInfo.getSex());
              map.put("country", snsUserInfo.getCountry());
              map.put("province", snsUserInfo.getProvince());
              map.put("city", snsUserInfo.getCity());
              map.put("headimgUrl", snsUserInfo.getHeadImgUrl());
              map.put("subscribeTime", snsUserInfo.getSubScribeTime());
              map.put("language", snsUserInfo.getLanguage());
            
    	    session.setAttribute("openId", snsUserInfo.getOpenId());
    	    session.setAttribute("campusId", campusId);
    	    
            System.out.println("subscribe="+snsUserInfo.getSubScribe()+"   subsceibeTime="+
            snsUserInfo.getSubScribeTime()+"  nickName="+snsUserInfo.getNickname()+
            "  language="+snsUserInfo.getLanguage());
      
            
          }
         
         //写入新用户
         //判断是否存在openid
         int isOpenId=userService.getOpenId(campusId, openId);          
         snsUserInfo.setCampusId(campusId); 
         
         //不存在就插入新数据
         if(isOpenId==0){       	  
        	  userService.insertUser(snsUserInfo);
          }else{       	  
            Users userinfo=userService.getUserInfoById(campusId, openId);                    
           //判断数据是否与原来数据相等，否则重新写入数据库
            if(userinfo.getSubScribe()!=(snsUserInfo.getSubScribe())||!userinfo.getNickname().equals(snsUserInfo.getNickname())||
        		  !userinfo.getHeadImgUrl().equals(snsUserInfo.getHeadImgUrl())||!userinfo.getLanguage().equals(snsUserInfo.getLanguage())||
        		   userinfo.getSubscribeTime().equals((snsUserInfo.getSubScribeTime()))||!userinfo.getSex().equals(snsUserInfo.getSex())||
        		   !userinfo.getGroupId().equals(snsUserInfo.getGroupId())||
        		   !userinfo.getRemark().equals(snsUserInfo.getRemark())){
            	 userService.upUserByOpenId(snsUserInfo);
        	     System.out.println("NOT equals!!!!");
        	   }else {
				System.out.println("OK !!!");
			}         
         }
           return map;
         }else {
        	System.out.println("error:wexinoausth=null");
			map.put("error", "weixinOauth2Token is NULL");
			return map;
		}
    }
	
	@RequestMapping("test")
	public 
	@ResponseBody boolean test(HttpServletRequest request,@RequestParam Integer campusId){
        HttpSession session = request.getSession();
        String openId=null;
        
        try{
          openId=session.getAttribute("openId").toString();
          }catch (Exception e) {
			// TODO: handle exception
		  }
        
        System.out.println("session="+session.getId());
        if(openId!=null){
          System.out.println("openId=="+session.getAttribute("openId").toString());
          /*
           * 查询该openId是否属于此公众号
           */
          if(userService.getOpenId(campusId, openId)==1){
             return true;
          }else{
        	  return false;
          }
        }else{
        	System.out.println("test openId==NULL ! ");
          return false;
        }
	}
	
	@RequestMapping("getUserInfoByOpenId")
	public @ResponseBody 
	Map<String, Object> getUserInfoByOpenId(HttpServletRequest request,@RequestParam Integer campusId){
		HttpSession session = request.getSession();
		String openId=session.getAttribute("openId").toString();
		Users snsUserInfo=userService.getUserInfoById(campusId, openId); 
		WeiXin weiXin=weixinService.getCampusById(campusId);
		
		Map<String, Object> map=new HashMap<>();
		
		map.put("campusName", weiXin.getCampusName());
	  // map.put("openId", snsUserInfo.getOpenId());
        map.put("subscribe", snsUserInfo.getSubScribe());
        map.put("nickName", snsUserInfo.getNickname());
        map.put("sex", snsUserInfo.getSex());
        map.put("country", snsUserInfo.getCountry());
        map.put("province", snsUserInfo.getProvince());
        map.put("city", snsUserInfo.getCity());
        map.put("headimgUrl", snsUserInfo.getHeadImgUrl());
        map.put("subscribeTime", snsUserInfo.getSubscribeTime());
        map.put("language", snsUserInfo.getLanguage());
        
        System.out.println("nickName="+snsUserInfo.getNickname());
        System.out.println("headimgUrl="+snsUserInfo.getImgUrl());
        
        return map;
	}
	
	/*
	 * 这个类是购买后回调到个人主页，与上面的类的区别是不需要传入campusId参数
	 */
	@RequestMapping("getUserInfoByNull")
	public @ResponseBody 
	Map<String, Object> getUserInfo(HttpServletRequest request){
		HttpSession session = request.getSession();
		String openId=session.getAttribute("openId").toString();
		Integer campusId;
		
		campusId=Integer.parseInt(session.getAttribute("campusId").toString());
		
		Users snsUserInfo=userService.getUserInfoById(campusId, openId); 
		WeiXin weiXin=weixinService.getCampusById(campusId);
		
		Map<String, Object> map=new HashMap<>();
		
		map.put("campusName", weiXin.getCampusName());
	  // map.put("openId", snsUserInfo.getOpenId());
        map.put("subscribe", snsUserInfo.getSubScribe());
        map.put("nickName", snsUserInfo.getNickname());
        map.put("sex", snsUserInfo.getSex());
        map.put("country", snsUserInfo.getCountry());
        map.put("province", snsUserInfo.getProvince());
        map.put("city", snsUserInfo.getCity());
        map.put("headimgUrl", snsUserInfo.getHeadImgUrl());
        map.put("subscribeTime", snsUserInfo.getSubscribeTime());
        map.put("language", snsUserInfo.getLanguage());
        
        System.out.println("nickName="+snsUserInfo.getNickname());
        System.out.println("headimgUrl="+snsUserInfo.getImgUrl());
        
        return map;
	}
	
}
