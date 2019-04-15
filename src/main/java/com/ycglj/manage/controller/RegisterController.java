package com.ycglj.manage.controller;

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

import com.ycglj.manage.model.Sellers;
import com.ycglj.manage.service.SellerService;
import com.ycglj.manage.tools.Constants;
import com.ycglj.manage.tools.Md5;
import com.ycglj.manage.tools.verifycode.Captcha;
import com.ycglj.manage.tools.verifycode.SpecCaptcha;
import com.ycglj.sqlserver.context.Connect;

@Controller
@RequestMapping("/register")
public class RegisterController {
	
	private SellerService sellerService;
	
	public SellerService getSellerService() {
		return sellerService;
	}

	@Autowired
	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}
		
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
	testName(@RequestParam String username){
		Map<String, Object> map=new HashMap<>();
		
		int repeat=sellerService.selectCountCampusAdmin(username);
		System.out.println("repeat="+repeat);
		if(username.equals("")){
			map.put("data", "用户名不能空");
			return map;
		}
		
		if(username.length()<4||username.length()>16){
			map.put("data", "用户名必须是4-16个字符之间（包括4、16）");
			return map;
		}
		
		String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";  
		Pattern   p   =   Pattern.compile(regEx);     
	    Matcher   m   =   p.matcher(username); 
	      
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
       /* Pattern p1 = null,p2 = null;
        Matcher m = null;
        boolean b = false;  
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
        if(str.length() >9)
        {   m = p1.matcher(str);
           b = m.matches();  
        }else{
            m = p2.matcher(str);
           b = m.matches(); 
        }  */
        return true;
     }
	
	@RequestMapping("/testPhone")
	public @ResponseBody Map<String, Object>
	testPhone(@RequestParam String telephone){
	    Map<String, Object> map=new HashMap<>();
		
		if(telephone.equals("")){
			map.put("data", "电话号码不能空");
			return map;
		}
		
		if(!isPhone(telephone)){
			map.put("data", "请输入正确的电话号码");
			return map;
		}
		
        map.put("data", "succeed");
		
		return map;
	}
	
   @RequestMapping("/testEmail")
   public @ResponseBody Map<String, Object>
   testEmail(@RequestParam String email){
	   Map<String, Object> map=new HashMap<>();
	   
	   if(email.equals("")){
			map.put("data", "门店地址不能空");
			return map;
		}
	   
	   String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";  
		Pattern   p   =   Pattern.compile(regEx);     
	    Matcher   m   =   p.matcher(email); 
	      
	    if(m.find()){
	    	map.put("data", "门店地址含有非法字符");
			return map;
	    }
	    
        map.put("data", "succeed");
		
		return map;
   }
	
   @RequestMapping("insert")
   public @ResponseBody Map<String, Object>
   insert(@RequestParam String username,@RequestParam String password,
		   String address,Integer area,
		   @RequestParam String regtlx,HttpServletRequest request){
	   Map<String, Object> map=new HashMap<>();
	   
	   HttpSession session = request.getSession();
	   
	   String verifyCode=(String) session.getAttribute("verifyCode");
	   
	   System.out.println("use="+username+"pw="+password);
	   
	   int repeat=sellerService.selectCountCampusAdmin(username);
		System.out.println("repeat="+repeat);
		if(username.equals("")){
			map.put(Constants.STATUS, Constants.FAILURE);
			map.put(Constants.MESSAGE, "用户名不能空");
			return map;
		}
		
		if(username.length()<4||username.length()>16){
			map.put(Constants.STATUS, Constants.FAILURE);
			map.put(Constants.MESSAGE, "用户名必须是4-16个字符之间（包括4、16）");
			return map;
		}
		
		String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";  
		Pattern   p   =   Pattern.compile(regEx);     
	    Matcher   m   =   p.matcher(username); 
	      
	    if(m.find()){
	    	map.put(Constants.STATUS, Constants.FAILURE);
			map.put(Constants.MESSAGE, "用户名含有非法字符");
			return map;
	    }
	    
		if(repeat>=1){
			map.put(Constants.STATUS, "repeat");
			map.put(Constants.MESSAGE, "该用户名已被注册，请使用其他用户名注册");
			return map;
		}
	   
	   if(regtlx.equals("")){
		   map.put(Constants.STATUS, "regtlxfailure");
			map.put(Constants.MESSAGE, "验证码不能为空");
		   return map;
	   }
	   
	   regtlx=regtlx.toLowerCase();
	   System.out.println("regtlx="+verifyCode);
	   if(!regtlx.equals(verifyCode)){
		   map.put(Constants.STATUS, "regtlxfailure");
			map.put(Constants.MESSAGE, "验证码错误");
		   return map;
	   }
	   
	   String campusAdmin=username;

	   Date registerTime=new Date();
	   
	   try {		
				String passwordMd5 = Md5.GetMD5Code(password);
				Sellers sellers=new Sellers();
				sellers.setCampusAdmin(campusAdmin);
				sellers.setPassword(passwordMd5);
				sellers.setType((short) 1);     //新注册类型为非管理员
				sellers.setState((short) 2);
				sellers.setArea(area);
				sellers.setAddress(address);
				sellers.setRegisterTime(registerTime);
				
				int i=sellerService.insertSellective(sellers);
				
				if(i==1){
					map.put(Constants.STATUS, Constants.SUCCESS);
					map.put(Constants.MESSAGE, "注册成功");
				}
				return map;
			
			}
		    catch (Exception e) {
		    	e.printStackTrace();
		    	map.put(Constants.STATUS, Constants.FAILURE);
				map.put(Constants.MESSAGE, "注册失败");
	            return map;
		    }
   }
   
   @RequestMapping("update")
   public @ResponseBody Map<String, Object>
   update(@RequestParam String username,@RequestParam String password,
		   Integer phone){
	   Map<String, Object> map=new HashMap<>();
   
	   String campusAdmin=username;

	   Date registerTime=new Date();
	   
	   try {		
				String passwordMd5 = Md5.GetMD5Code(password);
				Sellers sellers=new Sellers();
				sellers.setCampusAdmin(campusAdmin);
				sellers.setPassword(passwordMd5);
				sellers.setType((short) 1);     //新注册类型为非管理员
				sellers.setState((short) 2);
				sellers.setTelePhone(phone);
				
				int i=sellerService.updateSellective(sellers);
				
				if(i==1){
					map.put(Constants.STATUS, Constants.SUCCESS);
					map.put(Constants.MESSAGE, "更新成功");
				}
				 map.put("data", "succeed");
				
				return map;
			
			}
		    catch (Exception e) {
		    	e.printStackTrace();
		    	map.put(Constants.STATUS, Constants.FAILURE);
				map.put(Constants.MESSAGE, "更新失败");
	            return map;
		    }
   }
   
}
