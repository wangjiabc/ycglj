package com.ycglj.manage.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ycglj.manage.tools.verifycode.Captcha;
import com.ycglj.manage.tools.verifycode.SpecCaptcha;
import com.ycglj.manage.model.Sellers;
import com.ycglj.manage.service.SellerService;
import com.ycglj.manage.tools.Constants;
import com.ycglj.manage.tools.Md5;
import com.ycglj.manage.tools.MyTestUtil;
import com.ycglj.sqlserver.context.Connect;

@Controller
@RequestMapping("/seller")
public class SellerController {
	private SellerService sellerService;
	
	public SellerService getSellerService() {
		return sellerService;
	}

	@Autowired
	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}
	
	ApplicationContext applicationContext=new Connect().get();
	
	@RequestMapping(value="getYzm")
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
	
	/**
	 * 鍟嗗鐧诲綍
	 * @param campusAdmin
	 * @param password
	 * */
	@RequestMapping("/toLogin")
	public @ResponseBody Map<String, Object> toLogin(
			@RequestParam String campusAdmin, @RequestParam String password,
			@RequestParam String regtlx,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		HttpSession session = request.getSession();
		
		String verifyCode=(String) session.getAttribute("verifyCode");
		
		regtlx = regtlx.toLowerCase();
		
		System.out.println("verifyCode="+verifyCode);
		System.out.println("regtlx="+regtlx);
		System.out.println(regtlx.equals(verifyCode));
		if (!regtlx.equals(verifyCode)) {
			map.put(Constants.STATUS, "regtlxfailure");
			map.put(Constants.MESSAGE, "验证码错误");
			return map;
		}
		
		if (campusAdmin != null && password != null
				&& !campusAdmin.trim().equals("")
				&& !password.trim().equals("")) {
			/*
			Sellers sellers = sellerService.selectByCampusAdmin(campusAdmin);
			if (sellers != null) {
				if (sellers.getPassword().equals(Md5.GetMD5Code(password))) {
					map.put(Constants.STATUS, Constants.SUCCESS);
					map.put(Constants.MESSAGE, "登陆成功");
					map.put("type", sellers.getType());
					HttpSession session = request.getSession();
					session.setAttribute("type", sellers.getType());
					session.setAttribute("campusAdmin",
							sellers.getCampusAdmin());
					session.setAttribute("cityId", sellers.getCityId());
					Date date = new Date();
					sellerService.updateLastLoginTime(date, campusAdmin);
				}
				*/
			
			Sellers sellers = sellerService.selectByCampusAdmin(campusAdmin);
			
			if(sellers==null||sellers.getCampusAdmin()==null||sellers.getCampusAdmin().equals("")){
				map.put(Constants.STATUS, "notuser");
				map.put(Constants.MESSAGE, "用户不存在");
				return map;
			}
			
			if(sellers.getState()==null||sellers.getState()!=1){
				map.put(Constants.STATUS, "purview");
				map.put(Constants.MESSAGE, "没有登陆权限");
				return map;
			}
			
			if (sellers != null) {
			 if (sellers.getPassword().equals(Md5.GetMD5Code(password))) {
					map.put(Constants.STATUS, Constants.SUCCESS);
					map.put(Constants.MESSAGE, "登陆成功");
					map.put("type", sellers.getType());
					session.setAttribute("type", sellers.getType());
					session.setAttribute("campusAdmin",
							sellers.getCampusAdmin());
					session.setAttribute("cityId", sellers.getCityId());
					session.setAttribute("campusId", sellers.getCampusId());
					Date date = new Date();
					sellerService.updateLastLoginTime(date, campusAdmin);
				} else {
					map.put(Constants.STATUS, Constants.FAILURE);
					map.put(Constants.MESSAGE, "用户名或者密码错误");
				}
			} else {
				map.put(Constants.STATUS, Constants.FAILURE);
				map.put(Constants.MESSAGE, "用户名或者密码错误");
			}
		}

		return map;
	}
	
	/**
	 * 鏍规嵁鍟嗗id鏌ユ壘鍟嗗鏁版嵁
	 *
	 * */
	
	@RequestMapping("/getSellerById")
	public @ResponseBody Map<String, Object> getSellerById(String campusAdmin)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		Sellers sellers = sellerService.selectByCampusAdmin(campusAdmin);
		Sellers sellercampus=sellerService.selectByCampusId(campusAdmin);
		map.put("sellercampus", sellercampus);
		map.put("seller",sellers);		
		return map;
	}
	
	/**
	 * 妫�鏌ヨ璐﹀彿鏄惁娉ㄥ唽杩�
	 * @param campusAdmin
	 * @return
	 */
	
	@RequestMapping("/checkSellerIsExist")
	public @ResponseBody Map<String, Object> checkSellerIsExist(String campusAdmin)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		Sellers sellers = sellerService.selectByCampusAdmin(campusAdmin);
		if(sellers==null)
		{
			map.put(Constants.STATUS, Constants.SUCCESS);
			map.put(Constants.MESSAGE, "璇ョ敤鎴峰悕涓嶅瓨鍦紝鍙互娉ㄥ唽");
		}
		else
		{
			map.put(Constants.STATUS, Constants.FAILURE);
			map.put(Constants.MESSAGE, "璇ョ敤鎴峰悕宸插瓨鍦紝璇锋崲涓�涓悕瀛�");			
		}
		
		return map;	
	}
	
	
	@RequestMapping("/getCampusAdmin")
	public @ResponseBody Map getCampusAdmin(HttpServletRequest request){
		List<Sellers> sellers;
		
		HttpSession session=request.getSession();  //取得session的type变量，判断是否为公众号管理员
		String campusAdmin=session.getAttribute("campusAdmin").toString();
		String type=session.getAttribute("type").toString();
		
		int total=0;
		
		if(type.equals("0")){
          sellers=sellerService.getAllCampusAdmin();
          total=sellerService.getCampusAdminCount(campusAdmin);
		}else {
		  sellers=sellerService.getCampusAdmin(campusAdmin);
		  total=sellerService.selectCountCampusAdmin(campusAdmin);
		}
		
		Map map=new HashMap<>();
		
		map.put("code", "0");
		
		map.put("data", sellers);
		
		map.put("count", total);
		
		return map;
		
	}
	
	@RequestMapping("/updateCampusAdmin")
	public @ResponseBody Integer updateCampusAdmin(@RequestParam String campusAdmin,
			   String address,Integer area,
			   Integer state,
			HttpServletRequest request){
		Sellers sellers=new Sellers();
		
		HttpSession session=request.getSession();  //取得session的type变量，判断是否为公众号管理员
		String type=session.getAttribute("type").toString();
		
		int i=0;
		
		sellers.setCampusAdmin(campusAdmin);
				
		if(address!=null)
			sellers.setAddress(address);
		
		if (type.equals("0")) {
			
			if (area != null)
				sellers.setArea(area);
			
			if(state!=null)
				sellers.setState(state.shortValue());
			
		}
		

		if(type.equals("0")||!campusAdmin.equals("admin")){
          i=sellerService.updateSellective(sellers);
		}else {
		  return -1;
		}
		return i;
		
	}
	
	@RequestMapping("/updatePassword")
	public @ResponseBody Map updatePassword(
			@RequestParam String oldPassword,@RequestParam String password,HttpServletRequest request){
		
		HttpSession session=request.getSession();  //取得session的type变量，判断是否为公众号管理员

		String campusAdmin=session.getAttribute("campusAdmin").toString();
		
		Sellers sellersOld = sellerService.selectByCampusAdmin(campusAdmin);
				
		Sellers sellers=new Sellers();
		
		sellers.setCampusAdmin(campusAdmin);
		
		int i=0;
		
		Map map=new HashMap<>();
						
		sellers.setPassword(Md5.GetMD5Code(password));
		
		if (sellersOld.getPassword().equals(Md5.GetMD5Code(oldPassword))) {
			
			i=sellerService.updateSellective(sellers);
		
		}else{
			i=2;
		}
		
		if(i==1){
			map.put(Constants.STATUS, Constants.SUCCESS);
			map.put(Constants.MESSAGE, "");
		}
		else if(i==2){
			map.put(Constants.STATUS, "failure");
			map.put(Constants.MESSAGE, "");			
		}else{
			map.put(Constants.STATUS, "error");
			map.put(Constants.MESSAGE, "");		
		}
		
		return map;
		
	}
	
	@RequestMapping("/deleteCampusAdmin")
	public @ResponseBody Integer deleteCampusAdmin(@RequestParam String campusAdmin,
			HttpServletRequest request){

		HttpSession session=request.getSession();  //取得session的type变量，判断是否为公众号管理员
		String type=session.getAttribute("type").toString();
		
		int i=0;

		if(type.equals("0")){
          i=sellerService.deleteSellective(campusAdmin);
		}else {
		  return -1;
		}
		return i;
		
	}
	
	@RequestMapping(value="/logout")
	public  String logout(HttpServletRequest request){	
		request.getSession().removeAttribute("campusAdmin");

		return "redirect:/index.html";
	}
	
}