package com.ycglj.weixin.controller;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.ycglj.manage.model.WeiXin;
import com.ycglj.manage.service.UserService;
import com.ycglj.manage.service.WeiXinService;
import com.ycglj.weixin.base.AdvancedUtil;
import com.ycglj.weixin.base.CommonUtil;
import com.ycglj.weixin.base.SNSUserInfo;
import com.ycglj.weixin.dispatcher.EventDispatcher;
import com.ycglj.weixin.dispatcher.MsgDispatcher;
import com.ycglj.weixin.util.MessageUtil;
import com.ycglj.weixin.util.SignUtil;

import com.ycglj.weixin.base.Sign;

@Controller
@RequestMapping("/wechat")
public class WechatSecurity {
    private static Logger logger = Logger.getLogger(WechatSecurity.class);

    private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
    
   private WeiXinService weixinService;
	
	@Autowired
	public void setWeiXinService(WeiXinService weiXinService) {
		this.weixinService=weiXinService;
	}
    
    /**
     * 
     * @Description: 用于接收 get 参数，返回验证参数
     * @param @param request
     * @param @param response
     * @param @param signature
     * @param @param timestamp
     * @param @param nonce
     * @param @param echostr
     * @author dapengniao
     * @date 2016 年 3 月 4 日 下午 6:20:00
     */
    @RequestMapping(value = "security", method = RequestMethod.GET)
    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "campusId", required = true) Integer campusId,
            @RequestParam(value = "signature", required = true) String signature,
            @RequestParam(value = "timestamp", required = true) String timestamp,
            @RequestParam(value = "nonce", required = true) String nonce,
            @RequestParam(value = "echostr", required = true) String echostr) {
    	String token;
    	WeiXin weixin;
    	
    	weixin=weixinService.getCampusById(campusId);   	
    	token=weixin.getToken();
    	
        try {
            if (SignUtil.checkSignature(token,signature, timestamp, nonce)) {
                PrintWriter out = response.getWriter();
                out.print(echostr);
                out.close();
            } else {
                logger.info("这里存在非法请求！");
            }
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    /*
    @RequestMapping(value = "security", method = RequestMethod.POST)
    // post 方法用于接收微信服务端消息
    public void DoPost(HttpServletRequest request,HttpServletResponse response) {
        System.out.println("这是 post 方法！");
        try{
        Map<String, String> map=MessageUtil.parseXml(request);
        System.out.println("============================="+map.get("Content"));
        }catch(Exception e){
            logger.error(e,e);
        }
    }
    */
    
    /**
     * @Description: 接收微信端消息处理并做分发
     * @param @param request
     * @param @param response   
     * @author dapengniao
     * @date 2016 年 3 月 7 日 下午 4:06:47
     */
    @RequestMapping(value = "security", method = RequestMethod.POST)
    public void DoPost(HttpServletRequest request,HttpServletResponse response) {     
            Map<String, String> map=null;
            
			try {
				map = MessageUtil.parseXml(request);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            String msgtype=map.get("MsgType");
            String userName=map.get("ToUserName");
            Integer campusId;
            //通过通过原始ID查询公众号id
            campusId=weixinService.getCampusIdByUserName(userName);
            
            WeiXin weiXin=weixinService.getCampusById(campusId);
            
            String accessToken=weiXin.getAccessToken();
            String openId = map.get("FromUserName"); // 用户 openid
            String errorCode=null;
            String appId=weiXin.getAppId();
            String appSecret=weiXin.getAppSecret();
            
            SNSUserInfo snsUserInfo;
            
          try{ 
            snsUserInfo=AdvancedUtil.getSNSUserInfo(accessToken, openId);
 			errorCode=snsUserInfo.getErrorCode();

 			System.out.println("errorCode="+errorCode);
           if(errorCode!=null){
         	  if(errorCode.equals("40001")||errorCode.equals("42001")){
         		 accessToken=AdvancedUtil.getAccessToken(appId, appSecret);
         		 Map<String, Object> paramMap=new HashMap<>();
         		 
         		 paramMap.put("accessToken", accessToken);
         		 paramMap.put("campusId", campusId);
         		paramMap.put("campusId", campusId);
        		 Date date=new Date();
        		 paramMap.put("createTime", date);
         		 System.out.println("errorcode="+errorCode+"   accessToken="+accessToken);
         		 weixinService.updateCampusById(paramMap);
         		 
         		 snsUserInfo = AdvancedUtil.getSNSUserInfo(accessToken, openId);
         	  }
           }
           
            System.out.println("usernif="+snsUserInfo);
            
            map.put("access_token", accessToken);
            map.put("nickName", snsUserInfo.getNickname());
           }catch (Exception e) {
			// TODO: handle exception
		}

          try{  
            if(MessageUtil.REQ_MESSAGE_TYPE_EVENT.equals(msgtype)){
            	response.setCharacterEncoding("utf-8");
            	String respXML =EventDispatcher.processEvent(map,userService,weixinService); //进入事件处理
            	PrintWriter out = response.getWriter();   //输出消息
                out.print(respXML);
                out.flush();
                out.close();
            }else{                
                response.setCharacterEncoding("utf-8");
               String respXML = MsgDispatcher.processMessage(map); //进入消息处理
               PrintWriter out = response.getWriter();   //输出消息
                out.print(respXML);
                out.flush();
                out.close();
            }
        }catch(Exception e){
            logger.error(e,e);
        }
    }
    
    
    @RequestMapping("/sign")
    public @ResponseBody Map<String, String>
    sign(HttpServletRequest request, HttpServletResponse response,@RequestParam Integer campusId,
    		@RequestParam String url){
    	WeiXin weiXin=weixinService.getCampusById(campusId);
    	
    	String appId=weiXin.getAppId();
    	
    	 String accessToken=weiXin.getAccessToken();
    	 String appSecret=weiXin.getAppSecret();
    	 
    	 String jsapi_ticket="";
    	  
    	  String ticketURL="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+accessToken+"&type=jsapi";
    	  JSONObject jsonObject = CommonUtil.httpsRequest(ticketURL, "GET", null);
    	  System.out.println(jsonObject);
    	  
    	  if (null != jsonObject) {
	        	try{
		        	  if(jsonObject.getString("errcode")!=null){
		        		  if(jsonObject.getString("errcode").equals("40001")||jsonObject.getString("errcode").equals("42001")){
		              		 accessToken=AdvancedUtil.getAccessToken(appId, appSecret);
		              		ticketURL="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+accessToken+"&type=jsapi";
		              	    jsonObject = CommonUtil.httpsRequest(ticketURL, "GET", null);
		              	    jsapi_ticket=jsonObject.getString("ticket");
		        	  }else{
		        		  jsapi_ticket=jsonObject.getString("ticket");
		        	  }
		        	 }
	        	}catch (Exception e) {
						// TODO: handle exception
				}
    	  }
    	  
          Map<String, String> ret = Sign.sign(jsapi_ticket, url);
          ret.put("jsapi_ticket", jsapi_ticket);
          ret.put("appId", appId);
          return ret;
    }
}
