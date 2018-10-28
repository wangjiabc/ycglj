package com.ycglj.weixin.base;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ycglj.manage.model.WeiXin;
import com.ycglj.manage.service.WeiXinService;

public class AutoAccessToken {

	public static String get(WeiXinService weixinService,Integer campusId) {
		   String accessToken,appId, appSecret;
		   Date overTime,currenTime;
		   long timeDifference;
	        WeiXin weiXin;

	        weiXin=weixinService.getCampusById(campusId);
	        
	        appId=weiXin.getAppId();
	        appSecret=weiXin.getAppSecret();
	        
	        overTime=weiXin.getOverTime();
	        
	        currenTime=new Date();
      
	        timeDifference=currenTime.getTime()-overTime.getTime();
	        
	        System.out.println("timeDifference="+timeDifference);
	       	
	        if(timeDifference<7000*1000){
	        	return weiXin.getAccessToken();
	        }else{
	        	accessToken=AdvancedUtil.getAccessToken(appId, appSecret);
	        	Map<String, Object> paramMap=new HashMap<>();
       		 
       		 paramMap.put("accessToken", accessToken);
       		 paramMap.put("campusId", campusId);
      		  paramMap.put("createTime", currenTime);
       		 weixinService.updateCampusById(paramMap);
       		 return accessToken;
	       }
	        	        
	}
}
