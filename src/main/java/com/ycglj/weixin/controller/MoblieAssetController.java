package com.ycglj.weixin.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.ycglj.manage.dao.LicenseDAO;
import com.ycglj.manage.dao.OrderDAO;
import com.ycglj.manage.dao.UserDAO;
import com.ycglj.manage.daoModel.Users;
import com.ycglj.manage.service.PhotoService;
import com.ycglj.manage.service.SellerService;
import com.ycglj.manage.service.UserService;
import com.ycglj.manage.service.WeiXinService;
import com.ycglj.sqlserver.context.Connect;

@Controller
@RequestMapping("/mobile/asset")
public class MoblieAssetController {

private SellerService sellerService;
	
	public SellerService getSellerService() {
		return sellerService;
	}

	@Autowired
	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}
	
	private WeiXinService weixinService;
	
	@Autowired
	public void setweixinService(WeiXinService weiXinService) {
		this.weixinService=weiXinService;
	}
	
	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	private PhotoService photoService;
	
	@Autowired
	public void setPhotoService(PhotoService photoService) {
		this.photoService = photoService;
	}
	
	ApplicationContext applicationContext=new Connect().get();
	
	UserDAO userDao=(UserDAO) applicationContext.getBean("userdao");
	
	OrderDAO orderDao=(OrderDAO) applicationContext.getBean("orderdao");
	
	LicenseDAO licenseDAO=(LicenseDAO) applicationContext.getBean("licensedao");
	
	@RequestMapping(value="updateAffire")
	public @ResponseBody Integer uploadFilesSpecifyPath(@RequestParam Integer type,@RequestParam String arrays,HttpServletRequest request,HttpServletResponse response) throws Exception {  

		HttpSession session = request.getSession();
		
		String openId=session.getAttribute("openId").toString();
		
		long startTime=System.currentTimeMillis();   //获取开始时间  
		 
		 JSONArray jsonArray = JSONArray.parseArray(arrays);

		 int up = 0;
		 
		 if(jsonArray!=null&&jsonArray.size()>0){
	            //组合image名称，“;隔开”
	            
                System.out.println("length="+jsonArray.size());
	            try {
	            	
	            	List<String> list=new ArrayList<>();
	            	
	                for (int i = 0; i < jsonArray.size(); i++) {
	                    String uuid=jsonArray.getString(i);
	                    
	                    list.add(uuid);
	                }

	                up=userDao.updateUserDataAffirm(openId,list,type);
	                
	                //上传成功
	                if(up>0){
	                    return 1;
	                 //   Constants.printJson(response, fileName);;
	                }else {
	                  //  Constants.printJson(response, "上传失败！文件格式错误！");
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	              //  Constants.printJson(response, "上传出现异常！异常出现在：class.UploadController.insert()");
	            }
	        }else {
	         //   Constants.printJson(response, "没有检测到文件！");
	        }
	    
		 
		 try {

				if (up > 0) {

					WechatSendMessageController wechatSendMessageController = new WechatSendMessageController();

					Map searchMap = new HashMap<>();

					searchMap.put("open_id = ", openId);

					List list2 = (List) userDao.getAllUser(1, 0, "", "", searchMap).get("data");

					Users users = (Users) list2.get(0);

					users.getPhone();
					
					List list=userService.getUserByTransact();					
					
					Runnable r = new Runnable() {

						@Override
						public void run() {

							String title="";
							
							if(type==4){
								title="停业";
							}else if(type==5){
								title="补办";
							}else if(type==6){
								title="恢复营业";
							}else if(type==7){
								title="变更";
							}else if(type==8){
								title="歇业";
							}
							
							Date date=new Date();		
							SimpleDateFormat sdf  =   new  SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " ); 
							String time = sdf.format(date);
							
							WechatSendMessageController wechatSendMessageController = new WechatSendMessageController();

							Iterator iterator=list.iterator();
													
							while (iterator.hasNext()) {
								
								com.ycglj.manage.model.Users users=(com.ycglj.manage.model.Users) iterator.next();
								
								String transactOpenId=users.getOpenId();
								
								wechatSendMessageController.sendMessage(transactOpenId, "moOQnWapjZo99FItokfrzEPGjBsmElvO1bIcIWyW6XY", //申请待审核通知
										//"1vQfPSl4pSvi5UnmmDhVtueutq2R1w7XYRMts294URg", 
										title+"申请",
										"http://lzgfgs.com/ycglj/mobile/asset/",
										users.getName(), title, time, "已提交申请", "", "",
										"");

							}
							
						}
					};

					Thread t = new Thread(r);
					t.start();

				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	        
	        return 0;
 
	} 
	
	
	
	
}
