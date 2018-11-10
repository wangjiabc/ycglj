package com.ycglj.weixin.insweptcontroller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.activemq.filter.function.makeListFunction;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ycglj.manage.dao.OrderDAO;
import com.ycglj.manage.dao.UserDAO;
import com.ycglj.manage.daoModel.PreMessage;
import com.ycglj.manage.daoModel.WeiXin_User;
import com.ycglj.manage.model.Sellers;
import com.ycglj.manage.model.Users;

import com.ycglj.manage.service.SellerService;
import com.ycglj.manage.service.UserService;
import com.ycglj.manage.singleton.Singleton;
import com.ycglj.manage.tools.Constants;
import com.ycglj.manage.tools.IdcardUtil;
import com.ycglj.manage.tools.Md5;
import com.ycglj.manage.tools.MyTestUtil;
import com.ycglj.manage.tools.verifycode.Captcha;
import com.ycglj.manage.tools.verifycode.SpecCaptcha;
import com.ycglj.sqlserver.context.Connect;
import com.ycglj.weixin.controller.WechatSendMessageController;

import common.HttpClient;

@Controller
@RequestMapping("/mobile/assetRegister")
public class AssetUserRegisterController {

	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	ApplicationContext applicationContext=new Connect().get();
	
	UserDAO userDao=(UserDAO) applicationContext.getBean("userdao");
	
	OrderDAO orderDao=(OrderDAO) applicationContext.getBean("orderdao");
	
	/*
	 * 生成验证码类
	 */
	@RequestMapping(value="getYzm",method=RequestMethod.GET)
	public void getYzm(HttpServletResponse response,HttpServletRequest request){
		String verifyCode;
		try {
			response.setHeader("Pragma", "No-cache");  
	        response.setHeader("Cache-Control", "no-cache");  
	        response.setDateHeader("Expires", 0);  
	        response.setContentType("image/jpeg");  
	          
	        //生成随机字串  
	        Captcha captcha = new SpecCaptcha(120,25,4); 

	        //生成图片  
	        captcha.out(response.getOutputStream());
            verifyCode=captcha.text().toLowerCase();
            HttpSession session = request.getSession();
            session.setAttribute("verifyCode", verifyCode);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@RequestMapping("testName")
	public @ResponseBody Map<String, Object>
	testName(@RequestParam String name){
		Map<String, Object> map=new HashMap<>();
		
		int repeat=userService.selectRepeatUser(name);
		
		if(name.equals("")){
			map.put("data", "用户名不能空");
			return map;
		}
		
		
		String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";  
		Pattern   p   =   Pattern.compile(regEx);     
	    Matcher   m   =   p.matcher(name); 
	      
	    if(m.find()){
	    	map.put("data", "用户名含有非法字符");
			return map;
	    }
	    
			map.put("data", "succeed");
			return map;
		
	}
	
	@RequestMapping("testIDNo")
	public @ResponseBody Map<String, Object>
	testIDNo(@RequestParam String IDNo){
		Map<String, Object> map=new HashMap<>();
		
		if(IDNo.equals("")){
			map.put("data", "身份证号码不能空");
			return map;
		}
		
		
		//if(IdcardUtil.isIdcard(IDNo)) {	 
		if(true){
			map.put("data", "succeed");
			return map;
		}else {
			map.put("data", "false");
			return map;
		}
		
	}

	/**
     * 电话号码验证
     * 
     * @param  str
     * @return 验证通过返回true
     */
	 public static boolean isPhone(String str) { 
        Pattern p1 = null,p2 = null;
        Matcher m = null;
        if(str.length()!=11){
        	return false;
        }
      //  return b;
         return true;
     }
	
	@RequestMapping("/testPhone")
	public @ResponseBody Map<String, Object>
	testPhone(@RequestParam String telephone){
	    Map<String, Object> map=new HashMap<>();
		
		if(telephone.equals("")){
			map.put("data", "手机号码不能空");
			return map;
		}
		
		if(!isPhone(telephone)){
			map.put("data", "请输入正确的手机号码");
			return map;
		}
		
        map.put("data", "succeed");
		
		return map;
	}
	
	

	//生成短信验证码
	@RequestMapping(value="getValidate",method=RequestMethod.GET)
	public @ResponseBody Integer getValidate(@RequestParam String phone,
			@RequestParam String name,HttpServletResponse response,HttpServletRequest request){
			
		PreMessage preMessage=new PreMessage();
		
		HttpSession session = request.getSession();
		
		String openId=session.getAttribute("openId").toString();
		
		HttpClient httpClient = new HttpClient();

		String requestUrl="http://utf8.api.smschinese.cn";
		
		Map searchMap=new HashMap<>();
		
		searchMap.put("Charter like ", "%"+name.trim()+"%");
		

		try{

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 2;
		}
		
		String vcode = "";
        for (int i = 0; i < 6; i++) {
            vcode = vcode + (int)(Math.random() * 9);
        }
		
		String Message="您正在进行手机验证,验证码是 : "+vcode+" , 5分钟内有效";
		
		System.out.println("phone="+phone);
		System.out.println("Message="+Message);
		
		WechatSendMessageController wechatSendMessageController=new WechatSendMessageController();

		int i = wechatSendMessageController.sendPhoneMessage(phone, Message,name,openId);
		
		System.out.println("i="+i);
		
		if (i > 0) {
			LinkedHashMap<String, Map<String, Object>> linkMap=Singleton.getInstance().getRegisterMap();
			Map<String, Object> map=new HashMap<>();
			map.put("vcode", vcode);
			map.put("startTime", new Date());
			linkMap.put(phone, map);
		}
		
		return i;
	}
	
   @RequestMapping("insert")
   public @ResponseBody Integer
   insert(HttpServletRequest request,@RequestParam String name,
		   @RequestParam String phone,
		   @RequestParam String regtlx){
	   
	   HttpSession session = request.getSession();
       String openId=null;

       try{
         openId=session.getAttribute("openId").toString();
         }catch (Exception e) {
			// TODO: handle exception
        	 e.printStackTrace();
		  }
	  
	   if(regtlx.equals("")){
		   return 2;
	   }
	   
	   
		try {

			regtlx = regtlx.toLowerCase();
			LinkedHashMap<String, Map<String, Object>> linkMap = Singleton.getInstance().getRegisterMap();
			Map<String, Object> map = linkMap.get(phone);
			
			if(map==null||map.isEmpty()){
				return 3;
			}
			
			String verifyCode = (String) map.get("vcode");

			System.out.println("regtlx=" + regtlx + "      verifyCode=" + verifyCode);

			if (!regtlx.equals(verifyCode)) {
				verifyCode = null;
				return 2;
			}

			Date upTime = new Date();

			Users users = new Users();

			com.ycglj.manage.daoModel.Users users2=new com.ycglj.manage.daoModel.Users();
			
			WeiXin_User weiXin_User = new WeiXin_User();

			users.setOpenId(openId);
			users2.setOpen_id(openId);

			weiXin_User.setOpen_id(openId);
			weiXin_User.setCampusAdmin(openId);

			if (!phone.equals("")) {
				users.setPhone(phone);
				users2.setPhone(phone);
				weiXin_User.setPhone(phone);
			}
			if (!name.equals("")) {
				users.setName(name);
				users2.setName(name);
				weiXin_User.setUser_name(name);
			}

			users.setUpTime(upTime);			
			weiXin_User.setUp_time(upTime);

			int testRepeat = userService.selectRepeatUserByOpenId(openId);

			int type;

			if (testRepeat == 0) {
				type = userService.insertUsersInfo(users);
				users2.setDate(upTime);
				
				com.ycglj.manage.daoModel.Users users3=new com.ycglj.manage.daoModel.Users();
				String[] where={"phone = ",phone};
				users3.setWhere(where);
				users3.setLimit(1);
				users3.setOffset(0);
				users3.setUp_date(upTime);
				
				com.ycglj.manage.daoModel.Users users4=null;
				
				users4=userDao.getUser(users3);
				
				System.out.println("users4=");
				
				MyTestUtil.print(users4);
				
				if(users4!=null&&users4.getPhone()!=null&&!users4.getPhone().equals("")
						&&users4.getOpen_id()!=null&&!users4.getOpen_id().equals("")){
				
					users3.setOpen_id(openId);
					userDao.updateUser(users3);
					
				}else{
				
					userDao.insertUser(users2);
				
				}
			} else {
				type = userService.updateUsersInfo(users);
				String[] where={"open_id = ",openId};
				users2.setWhere(where);
				users2.setUp_date(upTime);
				userDao.updateUser(users2);
			}

			int count = orderDao.selectCountWeiXinUser(weiXin_User);

			if (count == 0) {
				orderDao.insertWeiXinUser(weiXin_User);
			} else {
				orderDao.updateWeiXinUser(weiXin_User);
			}
			
			
			return type;

		} catch (Exception e) {
			e.printStackTrace();

			return 3;
		}
   }
   
   @RequestMapping("/userByopenId")
   public @ResponseBody Users
   userAssetByopenId(HttpServletRequest request,@RequestParam Integer campusId){
	   HttpSession session = request.getSession();
       String openId=null;
       try{
           openId=session.getAttribute("openId").toString();
           }catch (Exception e) {
  			// TODO: handle exception
          	 e.printStackTrace();
  		  }
       
       return userService.getUserByOnlyOpenId(openId);
   }
}
