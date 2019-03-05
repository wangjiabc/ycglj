package com.ycglj.weixin.insweptcontroller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ycglj.manage.dao.OrderDAO;
import com.ycglj.manage.dao.UserDAO;
import com.ycglj.manage.daoModel.Check_Person;
import com.ycglj.manage.daoModel.WeiXin_User;
import com.ycglj.manage.model.Sellers;
import com.ycglj.manage.model.Users;
import com.ycglj.manage.service.SellerService;
import com.ycglj.manage.service.UserService;
import com.ycglj.manage.tools.Constants;
import com.ycglj.manage.tools.Md5;
import com.ycglj.manage.tools.verifycode.Captcha;
import com.ycglj.manage.tools.verifycode.SpecCaptcha;
import com.ycglj.sqlserver.context.Connect;

@Controller
@RequestMapping("/mobile/register")
public class UserRegisterController {
	
	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	ApplicationContext applicationContext=new Connect().get();
	
	OrderDAO orderDao=(OrderDAO) applicationContext.getBean("orderdao");
	
	UserDAO userDao=(UserDAO) applicationContext.getBean("userdao");
	
	/*
	 * 生成验证码类
	 */
	@RequestMapping(value="getYzm",method=RequestMethod.GET)
	public void getYzm(HttpServletResponse response,HttpServletRequest request){
		HttpSession session = request.getSession();
		try {
			response.setHeader("Pragma", "No-cache");  
	        response.setHeader("Cache-Control", "no-cache");  
	        response.setDateHeader("Expires", 0);  
	        response.setContentType("image/jpeg");  
	          
	        //生成随机字串  
	        Captcha captcha = new SpecCaptcha(120,25,4); 

	        //生成图片  
	        captcha.out(response.getOutputStream());
            String verifyCode=captcha.text().toLowerCase();
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
	    
		if(repeat>=1){
			map.put("data", "该用户名已被注册，请使用其他用户名注册");
			return map;
		}else{
			map.put("data", "succeed");
			return map;
		}
	}
	
	@RequestMapping("testPassword")
	public @ResponseBody Map<String, Object>
	testPassword(@RequestParam String password){
        Map<String, Object> map=new HashMap<>();
		
		if(password.equals("")){
			map.put("data", "密码不能空");
			return map;
		}
		
		if(password.length()<6||password.length()>16){
			map.put("data", "'密码的长度应该在6-16个字符之间");
			return map;
		}
		
		map.put("data", "succeed");
		
		return map;
		
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
        boolean b = true;  

        if(str.length()!=11){
        	return false;
        }
        
        return b;
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
	
   @RequestMapping("/testEmail")
   public @ResponseBody Map<String, Object>
   testEmail(@RequestParam String email){
	   Map<String, Object> map=new HashMap<>();
	   
	   String regEx="[`~!#$%^&*()+=|{}':;',\\[\\]<>/?~！#￥%……&*（）——+|{}【】‘；：”“’。，、？]";  
		Pattern   p   =   Pattern.compile(regEx);     
	    Matcher   m   =   p.matcher(email); 
	      
	    if(m.find()){
	    	map.put("data", "Email含有非法字符");
			return map;
	    }
	    
	    String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	    
	    if(Pattern.matches(REGEX_EMAIL, email)){	    
          map.put("data", "succeed");
	    }else {
	    	map.put("data", "Email地址错误");
		}
		return map;
   }

   
   //管理人员注册
   @RequestMapping("insert")
   public @ResponseBody Integer
   insert(HttpServletRequest request,@RequestParam String name,
		   @RequestParam String headship,
		   @RequestParam String phone,@RequestParam String email,
		   @RequestParam String address,@RequestParam String regtlx){
	   
	   HttpSession session = request.getSession();
       String openId=null;
       System.out.println("name="+name+"  headship="+headship+"   phone="
    		   +phone+"   email="+email+"    address="+address+"  regtlx="+regtlx);
       try{
         openId=session.getAttribute("openId").toString();
         }catch (Exception e) {
			// TODO: handle exception
        	 e.printStackTrace();
		  }
	  
	   if(regtlx.equals("")){
		   return 2;
	   }
	   
	   regtlx=regtlx.toLowerCase();
	   
	   String verifyCode=(String) session.getAttribute("verifyCode");
	   
	   if(!regtlx.equals(verifyCode)){
		   verifyCode=null;
		   return 2;
	   }
	
	   Date upTime=new Date();
	   
	   try {		
                Users users=new Users();
                
                WeiXin_User weiXin_User=new WeiXin_User();
                
                Check_Person check_Person=new Check_Person();
                
                users.setOpenId(openId);
                
                weiXin_User.setOpen_id(openId);
                weiXin_User.setCampusAdmin(openId);
                
                if(!phone.equals("")){
                	users.setPhone(phone);
                	weiXin_User.setPhone(phone);
                	check_Person.setPhone(phone);
                }
                if(!name.equals("")){
                	users.setName(name);
                	weiXin_User.setUser_name(name);
                	check_Person.setName(name);
                }
                if(!headship.equals("")){
                	users.setHeadship(headship);
                	weiXin_User.setHeadship(headship);
                	check_Person.setDuty(headship);
                }
                if(!email.equals("")){
                	users.setEmail(email);
                	weiXin_User.setEmail(email);
                }
                if(!address.equals("")){
                	users.setAddress(address);
                	weiXin_User.setAddress(address);
                }
				users.setUpTime(upTime);
				weiXin_User.setUp_time(upTime);
				
				int testRepeat=userService.selectRepeatUserByOpenId(openId);
				
				int type;
				
				if(testRepeat==0){
					type=userService.insertUsersInfo(users);					
				}else{
					type=userService.updateUsersInfo(users);
				}
				
			
				int count=orderDao.selectCountWeiXinUser(weiXin_User);

				if(count==0){
					orderDao.insertWeiXinUser(weiXin_User);
				}else{
					orderDao.updateWeiXinUser(weiXin_User);
				}
				
				userDao.updateCheck_Person(check_Person);
				
				return type;
			
			}
		    catch (Exception e) {
		    	e.printStackTrace();

	            return 3;
		    }
   }
   
   @RequestMapping("insertUser")
   public @ResponseBody Integer
   insertUser(HttpServletRequest request,@RequestParam String name,
		   @RequestParam String headship,
		   @RequestParam String phone,@RequestParam String email,
		   @RequestParam String address,@RequestParam String regtlx){
	   
	   HttpSession session = request.getSession();
       String openId=null;
       System.out.println("name="+name+"  headship="+headship+"   phone="
    		   +phone+"   email="+email+"    address="+address+"  regtlx="+regtlx);
       try{
         openId=session.getAttribute("openId").toString();
         }catch (Exception e) {
			// TODO: handle exception
        	 e.printStackTrace();
		  }
	  
	   if(regtlx.equals("")){
		   return 2;
	   }
	   
	   regtlx=regtlx.toLowerCase();
	   
	   String verifyCode=(String) session.getAttribute("verifyCode");
	   
	   if(!regtlx.equals(verifyCode)){
		   verifyCode=null;
		   return 2;
	   }
	
	   Date upTime=new Date();
	   
	   try {		
                Users users=new Users();
                
                WeiXin_User weiXin_User=new WeiXin_User();
                
                users.setOpenId(openId);
                
                weiXin_User.setOpen_id(openId);
                weiXin_User.setCampusAdmin(openId);
                
                if(!phone.equals("")){
                	users.setPhone(phone);
                	weiXin_User.setPhone(phone);
                }
                if(!name.equals("")){
                	users.setName(name);
                	weiXin_User.setUser_name(name);
                }
                if(!headship.equals("")){
                	users.setHeadship(headship);
                	weiXin_User.setHeadship(headship);
                }
                if(!email.equals("")){
                	users.setEmail(email);
                	weiXin_User.setEmail(email);
                }
                if(!address.equals("")){
                	users.setAddress(address);
                	weiXin_User.setAddress(address);
                }
				users.setUpTime(upTime);
				weiXin_User.setUp_time(upTime);
				
				int testRepeat=userService.selectRepeatUserByOpenId(openId);
				
				int type;
				
				if(testRepeat==0){
					type=userService.insertUsersInfo(users);					
				}else{
					type=userService.updateUsersInfo(users);
				}
				
				int count=orderDao.selectCountWeiXinUser(weiXin_User);

				if(count==0){
					orderDao.insertWeiXinUser(weiXin_User);
				}else{
					orderDao.updateWeiXinUser(weiXin_User);
				}
				
				return type;
			
			}
		    catch (Exception e) {
		    	e.printStackTrace();

	            return 3;
		    }
   }
   
   @RequestMapping("/userinfoByopenId")
   public @ResponseBody Users 
   userinfoByopenId(HttpServletRequest request,@RequestParam Integer campusId){
	   HttpSession session = request.getSession();
       String openId=null;
       try{
           openId=session.getAttribute("openId").toString();
           }catch (Exception e) {
  			// TODO: handle exception
          	 e.printStackTrace();
  		  }
       
       return userService.getUserInfoById(campusId, openId);
   }
}
