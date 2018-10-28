package com.ycglj.weixin.base;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSONObject;


public class AdvancedUtil {
	
	/*这个方法用于通过回调的code换取一个特殊的网页授权access_token
	 * 正确时返回的JSON数据包如下：
	 * { "access_token":"ACCESS_TOKEN",    
	     "expires_in":7200,    
	     "refresh_token":"REFRESH_TOKEN",    
	     "openid":"OPENID",    
	     "scope":"SCOPE" } 
	 */
	 public static WeixinOauth2Token getOauth2AccessToken(String appId, String appSecret, String code) {
	        WeixinOauth2Token wat = null;
	        // 拼接请求地址
	        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	        requestUrl = requestUrl.replace("APPID", appId);
	        requestUrl = requestUrl.replace("SECRET", appSecret);
	        requestUrl = requestUrl.replace("CODE", code);
	        // 获取网页授权凭证
	        
	        System.out.println("requesUrl="+requestUrl);
	        
	        
	        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
	        System.out.println("jsonObject="+jsonObject);
	        
	        if (null != jsonObject) {
	        	try{
		        	  if(jsonObject.getString("errcode")!=null){
		        		String errcode=jsonObject.getString("errcode");
		        		System.out.println("errcoid="+errcode);
                        return null;
		        	  }
		        	 }catch (Exception e) {
						// TODO: handle exception
					}
	        	
	        	
	            try {
	                wat = new WeixinOauth2Token();
	                wat.setAccessToken(jsonObject.getString("access_token"));
	                wat.setExpiresIn(jsonObject.getString("expires_in"));
	                wat.setRefreshToken(jsonObject.getString("refresh_token"));
	                wat.setOpenId(jsonObject.getString("openid"));
	                wat.setScope(jsonObject.getString("scope"));
	            } catch (Exception e) {
	                wat = null;
	                String errorCode = jsonObject.getString("errcode");
	                String errorMsg = jsonObject.getString("errmsg");
	               e.printStackTrace();
	            }
	        }
	        return wat;
	    }
	 
	 //此方法用于获取基础access_token
	  public static String getAccessToken(String appId, String appSecret){
		   String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
	        requestUrl = requestUrl.replace("APPID", appId);
	        requestUrl = requestUrl.replace("SECRET", appSecret);
	        
	        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
	        System.out.println("jsonObject="+jsonObject);
	        
	        String access_token=jsonObject.getString("access_token");
	        
	        return access_token;
	 }
	 
	 
	 
	 //这个方法的作用是可以通过access_token和openid拉取用户信息
	 @SuppressWarnings( { "deprecation", "unchecked" })
	    public static SNSUserInfo getSNSUserInfo(String accessToken, String openId){
	        SNSUserInfo snsUserInfo=new SNSUserInfo();
	        // 拼接请求地址
	    //    String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
	        String requestUrl ="https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
	        System.out.println("requesturl="+requestUrl);
	        // 通过网页授权获取用户信息
	        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
              System.out.println("json="+jsonObject);
	        if (jsonObject!=null) {
	        	try{
	        		if(jsonObject.getString("errcode")!=null){
		        		String errcode=jsonObject.getString("errcode");
		        		System.out.println("errcoid="+errcode);
                        snsUserInfo.setErrorCode(errcode);       //返回微信错误代码，以便getUserInfo方法重新生成access_token
                        return snsUserInfo;
	        	  }
	        	 }catch (Exception e) {
					// TODO: handle exception
				}
	            try {
	                // 用户的标识
	                snsUserInfo.setOpenId(jsonObject.getString("openid"));
	                //用户是否订阅该公众号标识
	                snsUserInfo.setSubScribe(Short.parseShort(jsonObject.getString("subscribe")));
	                // 昵称
	                snsUserInfo.setNickname(jsonObject.getString("nickname"));
	                // 性别（1是男性，2是女性，0是未知）
	                snsUserInfo.setSex(Short.parseShort(jsonObject.getString("sex")));
	                //用户的语言
	                snsUserInfo.setLanguage(jsonObject.getString("language"));
	                // 用户所在国家
	                snsUserInfo.setCountry(jsonObject.getString("country"));
	                // 用户所在省份
	                snsUserInfo.setProvince(jsonObject.getString("province"));
	                // 用户所在城市
	                snsUserInfo.setCity(jsonObject.getString("city"));
	                // 用户头像
	                snsUserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
	                //用户关注时间
	                String formats = "yyyy-MM-dd HH:mm:ss";
	                Long timestamp = Long.parseLong(jsonObject.getString("subscribe_time")) * 1000;
	                String dateTime = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
	                System.out.println("dateTime="+dateTime);
	                DateFormat fmt =new SimpleDateFormat(formats);
	                Date date= fmt.parse(dateTime);
	                snsUserInfo.setSubScribeTime(date);
	                System.out.println("substiem="+date);
	                //公众号运营者对粉丝的备注
	                snsUserInfo.setRemark(jsonObject.getString("remark"));
	                //用户所在的分组ID
	                snsUserInfo.setGroupId(jsonObject.getString("groupid"));
	                //只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
	                snsUserInfo.setUnionid(jsonObject.getString("unionid"));
	                // 用户特权信息
	                snsUserInfo.setPrivilegeList(jsonObject.getJSONArray("privilege"));
	            } catch (Exception e) {
	                snsUserInfo = null;
	                String errorCode = jsonObject.getString("errcode");
	                String errorMsg = jsonObject.getString("errmsg");
	               e.printStackTrace();
	            }
	        }else{
	        	System.out.println("json=null");
	        }
	        return snsUserInfo;
	    }
	 
}
