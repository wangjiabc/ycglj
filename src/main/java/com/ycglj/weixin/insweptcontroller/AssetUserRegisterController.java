package com.ycglj.weixin.insweptcontroller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ycglj.manage.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.ycglj.manage.dao.OrderDAO;
import com.ycglj.manage.daoModel.PreMessage;
import com.ycglj.manage.daoModel.Temp_Users;
import com.ycglj.manage.daoModel.WeiXin_User;
import com.ycglj.manage.face.RegisterCompare;
import com.ycglj.manage.model.Users;

import com.ycglj.manage.service.UserService;
import com.ycglj.manage.singleton.Singleton;
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
	 * 鐢熸垚楠岃瘉鐮佺被
	 */
	@RequestMapping(value="getYzm",method=RequestMethod.GET)
	public void getYzm(HttpServletResponse response,HttpServletRequest request){
		String verifyCode;
		try {
			response.setHeader("Pragma", "No-cache");  
	        response.setHeader("Cache-Control", "no-cache");  
	        response.setDateHeader("Expires", 0);  
	        response.setContentType("image/jpeg");  
	          
	        //鐢熸垚闅忔満瀛椾覆  
	        Captcha captcha = new SpecCaptcha(120,25,4); 

	        //鐢熸垚鍥剧墖  
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
			map.put("data", "鐢ㄦ埛鍚嶄笉鑳界┖");
			return map;
		}
		
		
		String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~锛丂#锟�%鈥︹��&*锛堬級鈥斺��+|{}銆愩�戔�橈紱锛氣�濃�溾�欍�傦紝銆侊紵]";  
		Pattern   p   =   Pattern.compile(regEx);     
	    Matcher   m   =   p.matcher(name); 
	      
	    if(m.find()){
	    	map.put("data", "鐢ㄦ埛鍚嶅惈鏈夐潪娉曞瓧绗�");
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
			map.put("data", "韬唤璇佸彿鐮佷笉鑳界┖");
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
     * 鐢佃瘽鍙风爜楠岃瘉
     * 
     * @param  str
     * @return 楠岃瘉閫氳繃杩斿洖true
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
			map.put("data", "鎵嬫満鍙风爜涓嶈兘绌�");
			return map;
		}
		
		if(!isPhone(telephone)){
			map.put("data", "璇疯緭鍏ユ纭殑鎵嬫満鍙风爜");
			return map;
		}
		
        map.put("data", "succeed");
		
		return map;
	}
	
	

	//鐢熸垚鐭俊楠岃瘉鐮�
	@RequestMapping(value="getValidate",method=RequestMethod.GET)
	public @ResponseBody Integer getValidate(@RequestParam String phone,
			@RequestParam String name,HttpServletResponse response,HttpServletRequest request){
			
		PreMessage preMessage=new PreMessage();
		
		HttpSession session = request.getSession();
		
		String openId=session.getAttribute("openId").toString();
		
		HttpClient httpClient = new HttpClient();

		String requestUrl="http://utf8.api.smschinese.cn";
		
		Map searchMap=new HashMap<>();
		
		searchMap.put("name like ", "%"+name.trim()+"%");
		searchMap.put("phone = ", phone);

		try{

			int count=(int) userDao.getAllUser(1, 0, null, null, searchMap).get("count");
			if(count<1){
				return 2;
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 2;
		}
		
		String vcode = "";
        for (int i = 0; i < 6; i++) {
            vcode = vcode + (int)(Math.random() * 9);
        }
		
		String Message="鎮ㄦ鍦ㄨ繘琛屾墜鏈洪獙璇�,楠岃瘉鐮佹槸 : "+vcode+" , 5鍒嗛挓鍐呮湁鏁�";
		
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

   //宸插瓨鍦ㄧ殑闆跺敭鎴峰井淇″彿娉ㄥ唽
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

			users2.setDate(upTime);

			com.ycglj.manage.daoModel.Users users3 = new com.ycglj.manage.daoModel.Users();
			String[] where = { "phone = ", phone };
			users3.setPhone(phone);
			users3.setWhere(where);
			users3.setLimit(1);
			users3.setOffset(0);
			users3.setUp_date(upTime);

			com.ycglj.manage.daoModel.Users users4 = null;

			users4 = userDao.getUser(users3);

			System.out.println("users4=");

			MyTestUtil.print(users4);

			if (users4 != null && users4.getPhone() != null && !users4.getPhone().equals("")
					&& users4.getOpen_id() != null && !users4.getOpen_id().equals("")) {

				users3.setOpen_id(openId);
				userDao.updateUser(users3);

			} else {

				userDao.insertOnlyUsers(users2);

			}


			int count = orderDao.selectCountWeiXinUser(weiXin_User);

			if (count == 0) {
				orderDao.insertWeiXinUser(weiXin_User);
			} else {
				orderDao.updateWeiXinUser(weiXin_User);
			}
			
			
			return 1;

		} catch (Exception e) {
			e.printStackTrace();

			return 3;
		}
   }
   
   //鏂伴浂鍞埛娉ㄥ唽
   @RequestMapping("insertNew")
   public @ResponseBody Integer
   insertNew(HttpServletRequest request,@RequestParam String name,
		   @RequestParam String id_number,@RequestParam String phone,
		   @RequestParam String address,
		   @RequestParam String arrays,Integer area,String business_state,
		   Double lng,Double lat,
		   String regtlx){
	   
	   HttpSession session = request.getSession();
       String openId=null;

       try{
         openId=session.getAttribute("openId").toString();
         }catch (Exception e) {
			// TODO: handle exception
        	 e.printStackTrace();
		  }
	  
 /*       long face=new RegisterCompare().match(openId,request);
//
        if(face<70){
        	return 4;
        }
   */     
		JSONArray jsonArray = JSONArray.parseArray(arrays);

		if (jsonArray != null && jsonArray.size() > 0) {
			// 缁勫悎image鍚嶇О锛屸��;闅斿紑鈥�
			int up = 0;
			System.out.println("length=" + jsonArray.size());
			try {

				List<String> list = new ArrayList<>();

				for (int i = 0; i < jsonArray.size(); i++) {
					String uuid = jsonArray.getString(i);

					list.add(uuid);
				}

				up = userDao.updateUserDataAffirm(openId, list,3);

				// 涓婁紶鎴愬姛
				if (up > 0) {

				} else {
					return 3;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return 3;
			}
		} else {
			return 3;
		}
       
       /*
	   if(regtlx.equals("")){
		   return 2;
	   }
	   */
	   
		try {

			/*
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
			*/
						
			Date upTime = new Date();

			Users users = new Users();
			
			Temp_Users temp_Users=new Temp_Users();
			
			WeiXin_User weiXin_User = new WeiXin_User();

			users.setOpenId(openId);
			temp_Users.setOpen_id(openId);

			weiXin_User.setOpen_id(openId);
			weiXin_User.setCampusAdmin(openId);

			if (!phone.equals("")) {
				users.setPhone(phone);
				temp_Users.setPhone(phone);
				weiXin_User.setPhone(phone);
			}
			if (!name.equals("")) {
				users.setName(name);
				temp_Users.setName(name);
				weiXin_User.setUser_name(name);
			}

			if(!address.equals("")){
				temp_Users.setAddress(address);
			}
			
			if(lng!=null){
				temp_Users.setLng(lng);
			}
			
			if(lat!=null){
				temp_Users.setLat(lat);
			}
			if(area!=null){
				temp_Users.setArea(area);
			}
			if(business_state!=null&&!business_state.equals("")){
				temp_Users.setBusiness_state(business_state);
			}
			if(id_number!=null){
				temp_Users.setId_number(id_number);
			}
			users.setUpTime(upTime);			
			weiXin_User.setUp_time(upTime);

			temp_Users.setDate(upTime);
			temp_Users.setAgree(0);
			
			int up=userDao.insertTempUser(temp_Users);
			
			int count = orderDao.selectCountWeiXinUser(weiXin_User);

			if (count == 0) {
				orderDao.insertWeiXinUser(weiXin_User);
			} else {
				orderDao.updateWeiXinUser(weiXin_User);
			}
			
			try {


					WechatSendMessageController wechatSendMessageController = new WechatSendMessageController();

					List list=userService.getUserByTransact(area);					
					
					Runnable r = new Runnable() {

						@Override
						public void run() {
														
							Date date=new Date();		
							SimpleDateFormat sdf  =   new  SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " ); 
							String time = sdf.format(date);
							
							WechatSendMessageController wechatSendMessageController = new WechatSendMessageController();

							Iterator iterator=list.iterator();
													
							while (iterator.hasNext()) {
								
								Users users=(Users) iterator.next();
								
								String transactOpenId=users.getOpenId();
								wechatSendMessageController.sendMessage(transactOpenId, "moOQnWapjZo99FItokfrzEPGjBsmElvO1bIcIWyW6XY", //鐢宠寰呭鏍搁�氱煡
										//"1vQfPSl4pSvi5UnmmDhVtueutq2R1w7XYRMts294URg", 
										"鏂板姙鐢宠",
										"http://lzgfgs.com/ycglj/mobile/asset/onlineregs/transact/index.html",
										"鏂板姙鐢宠",name, "鏂板姙鐑熻崏涓撳崠闆跺敭璁稿彲璇�", time, "宸叉彁浜�", "", 
										"");

							}
							
						}
					};

					Thread t = new Thread(r);
					t.start();


			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			return 1;

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
