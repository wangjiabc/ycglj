package com.ycglj.weixin.controller;

import java.text.ParseException;
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

import org.bouncycastle.jce.provider.asymmetric.ec.Signature.ecCVCDSA;
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
import com.ycglj.manage.daoModel.Check_Person;
import com.ycglj.manage.daoModel.Position;
import com.ycglj.manage.daoModel.Temp_Change;
import com.ycglj.manage.daoModel.Temp_User_License;
import com.ycglj.manage.daoModel.Temp_Users;
import com.ycglj.manage.daoModel.User_Data;
import com.ycglj.manage.daoModel.User_License;
import com.ycglj.manage.daoModel.Users;
import com.ycglj.manage.daoModel.WeiXin_User;
import com.ycglj.manage.daoModelJoin.Users_License_Join;
import com.ycglj.manage.daoModelJoin.Users_License_Position_Join;
import com.ycglj.manage.service.PhotoService;
import com.ycglj.manage.service.SellerService;
import com.ycglj.manage.service.UserService;
import com.ycglj.manage.service.WeiXinService;
import com.ycglj.manage.tools.MyTestUtil;
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
	public @ResponseBody Integer uploadFilesSpecifyPath(@RequestParam String license,@RequestParam Integer type,@RequestParam String arrays,
							String changeName,String changeValue,String name,
							String stopStartTime,String stopEndTime,
							HttpServletRequest request,HttpServletResponse response) throws Exception {  

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
	                
	            } catch (Exception e) {
	                e.printStackTrace();
	              //  Constants.printJson(response, "上传出现异常！异常出现在：class.UploadController.insert()");
	            }
	        }else {
	         //   Constants.printJson(response, "没有检测到文件！");
	        }
	    
			
		 Users_License_Position_Join user_License2=new Users_License_Position_Join();
			
		 Map searchMap2=new HashMap<>();
		 searchMap2.put("[User_License].license = ",license);
		 
		 List lists=(List) licenseDAO.getAllLicense_Position(1, 0, null, null, "and", searchMap2).get("rows");
		 
		 try{
			 user_License2=(Users_License_Position_Join) lists.get(0);
		 }catch (Exception e) {
			// TODO: handle exception
			 e.printStackTrace();
		}
		 
		 final Users_License_Position_Join user_License3=user_License2;
		 
		 System.out.println("type="+type);
		 
		 try {

			 if(type!=6){
					User_License user_License=new User_License();
					
					String[] where={"open_id = ", openId,"license = ",license};
					
					Date license_start_date = null;
					Date license_end_date = null;
					
					System.out.println("stopStartTime="+stopStartTime);
					
					try {
						if(stopStartTime!=null&&!stopStartTime.equals("")){
							license_start_date = new Date(new Long(stopStartTime));
							user_License.setStop_start_date(license_start_date);
						}
						if(stopEndTime!=null&&!stopEndTime.equals("")){
							license_end_date=new Date(new Long(stopEndTime));
							user_License.setStop_end_data(license_end_date);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					user_License.setWhere(where);
					user_License.setAuthentication(type);
					user_License.setApplication_date(new Date());
					
					up=userDao.updateUserLicense(user_License);
			 }
			
				if (up > 0) {
					
					WechatSendMessageController wechatSendMessageController = new WechatSendMessageController();

					Map searchMap = new HashMap<>();

					Runnable r = new Runnable() {

						@Override
						public void run() {

							String name2=name;
							
							List list=null;
							
							Integer area=null;
							
							System.out.println("user_License3=");
							MyTestUtil.print(user_License3);
							if(user_License3.getArea()!=null){
								area=user_License3.getArea();
								list=userService.getUserByTransact(area);
							}else{
								list=userService.getUserByTransact();
							}
							
							String title="";
							
							if(type==4){
								title="停业";
							}else if(type==5){
								title="补办";
							}else if(type==6){
								title="恢复营业";
								
								Temp_User_License temp_User_License=new Temp_User_License();
								
								Date license_start_date = null;
								Date license_end_date = null;
								
								try {
									if(stopStartTime!=null&&!stopStartTime.equals("")){
										license_start_date = new Date(new Long(stopStartTime));
										temp_User_License.setStop_start_date(license_start_date);
									}
									if(stopEndTime!=null&&!stopEndTime.equals("")){
										license_end_date=new Date(new Long(stopEndTime));
										temp_User_License.setStop_end_data(license_end_date);
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								temp_User_License.setAuthentication(type);
								
								String[] where={"license=",license};
								
								temp_User_License.setWhere(where);
								
								licenseDAO.updateTempUserLicense(temp_User_License);
								
							}else if(type==7){
								title="变更";
								
								Temp_Change temp_Change=new Temp_Change();
								temp_Change.setLicense(license);
								temp_Change.setName(changeName);
								temp_Change.setValue(changeValue);
								
								Map searchMap=new HashMap<>();
					    		
					    		searchMap.put("[User_License].license= ", license);
					    		
					    		List userlist=(List) userDao.getAllUserJoin(1, 0, "", "", searchMap).get("data");
								
					    		try{
					    			Users_License_Join users_License_Join=(Users_License_Join) userlist.get(0);
					    			name2=users_License_Join.getName();
					    		}catch (Exception e) {
									// TODO: handle exception
					    			e.printStackTrace();
								}
					    		
								licenseDAO.insertTempChange(temp_Change);
								
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
										"http://lzgfgs.com/ycglj/mobile/asset/onlineregs/transact/index.html",
										title+"申请",name2, title, time, "已提交", "", 
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
	        
	        return up;
 
	} 
	
	@RequestMapping("getAllTransactUser")
	public @ResponseBody Map<String, Object> getAllTransactUser(@RequestParam Integer limit,@RequestParam Integer offset,String sort,String order,
			String search,String area,HttpServletRequest request){
		
		Map searchMap=new HashMap<>();
		
		searchMap.put("authentication > ", "3");
		
		if(search!=null&&!search.trim().equals("")){
			search="%"+search+"%";  
			searchMap.put("name like ", search);
		}		

		if(area!=null&&!area.trim().equals("")){
			searchMap.put("area =", area);
		}	
		
		//int offset=(page-1)*limit;
		
		Map map=userDao.getAllUserJoin(limit, offset, sort,order, searchMap);
		
		Map map2=new HashMap<>();
		
		List list=(List) map.get("data");
		
		int total=(int) map.get("count");
		
		map2.put("rows", list);
		
		map2.put("total", total);
		
		return map2;
		
	}
			
	@RequestMapping("getAllNewUser")
	public @ResponseBody Map<String, Object> getAllNewUser(@RequestParam Integer limit,@RequestParam Integer offset,String sort,String order,
			String search,String area,HttpServletRequest request){
			
			Map searchMap=new HashMap<>();
			
			searchMap.put("agree != ", "2");
			
			if(search!=null&&!search.trim().equals("")){
				search="%"+search+"%";  
				searchMap.put("name like ", search);
			}		
						
			if(area!=null&&!area.trim().equals("")){
				searchMap.put("area =", area);
			}	
			
			//int offset=(page-1)*limit;
			
			return userDao.getAllTempUserJoin(limit, offset, sort, order, searchMap);
				
	}
	
	@RequestMapping("getNewUser")
	public @ResponseBody Map<String, Object> getNewUser(@RequestParam String openId,HttpServletRequest request){
			
			Map map=new HashMap<>();
		
			Map searchMap=new HashMap<>();
			
			searchMap.put("open_id = ", openId);
			
			List list=(List) userDao.getAllTempUserJoin(1, 0, null, null, searchMap).get("rows");
			
			List<User_Data> list2;
			
			Map searchMap2=new HashMap<>();
			
			searchMap2.put("open_id = ", openId);
			searchMap2.put("currently = ", "1");
						
			List fileBytes=(List) userDao.getAllUserData(request,1000, 0, "","", searchMap2).get("fileBytes");
			
			map.put("fileBytes", fileBytes);
			
			map.put("rows", list);
			
			return map;
	}
	
	@RequestMapping("agreeNew")
	public @ResponseBody Integer agreeNew(@RequestParam String openId,@RequestParam Integer agree,
			String cause,HttpServletRequest request){
		
		Temp_Users temp_Users=new Temp_Users();
		
		temp_Users.setAgree(agree);
		temp_Users.setLimit(1);
		temp_Users.setOffset(0);
		temp_Users.setNotIn("open_id");
		
		if(cause!=null&&!cause.equals(""))
			temp_Users.setCause(cause);
		
		String[] where={"open_id=",openId};
		
		temp_Users.setWhere(where);
		
		int up=userDao.updateTempUsers(temp_Users);
		
		try {

				WechatSendMessageController wechatSendMessageController = new WechatSendMessageController();

				Temp_Users temp_Users2=userDao.getTempUsers(temp_Users);

				Runnable r = new Runnable() {

					@Override
					public void run() {

						String title = "";
						String templateID="";
						
						if(agree==1){
							title="新办受理申请通知";
							templateID="moOQnWapjZo99FItokfrzEPGjBsmElvO1bIcIWyW6XY"; // 申请待审核通知
						}else{
							title="你好"+temp_Users2.getName()+"，你的新办申请信息审核失败";
							templateID="b1ujoxmvkW9112uTDWWy7TZ7cgd4IWI86MaPN55OLqw"; //申请驳回通知
						}
						
						Date date = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
						String time = sdf.format(date);

						WechatSendMessageController wechatSendMessageController = new WechatSendMessageController();

						if(agree==1){
							wechatSendMessageController.sendMessage(openId, "moOQnWapjZo99FItokfrzEPGjBsmElvO1bIcIWyW6XY", //申请待审核通知
									//"1vQfPSl4pSvi5UnmmDhVtueutq2R1w7XYRMts294URg", 
									title,
									"http://lzgfgs.com/ycglj/mobile/asset/onlineregs/transact/index.html",
									title,temp_Users2.getName(), title, time, "已受理", "", 
									"");
						}else{
							wechatSendMessageController.sendMessage(openId, templateID,
								// "1vQfPSl4pSvi5UnmmDhVtueutq2R1w7XYRMts294URg",
								title , "http://lzgfgs.com/ycglj/mobile/asset",
								title,temp_Users2.getName(), time, "未通过", cause, "请修改后，重新提交", "");
						}
					}
				};

				Thread t = new Thread(r);
				t.start();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return up;
		
	}
			
	
	@RequestMapping(value="newTransact")
	public @ResponseBody Integer newTransact(@RequestParam String openId,@RequestParam String license,
			@RequestParam String startTime,@RequestParam String endTime,@RequestParam Integer area,
			@RequestParam String region,String business_state,
			String address,HttpServletRequest request){
		
		Temp_Users temp_Users=new Temp_Users();
		
		temp_Users.setLimit(1);
		temp_Users.setOffset(0);
		temp_Users.setNotIn("id");
		
		String[] where={"open_id=",openId};
		
		temp_Users.setWhere(where);
		
		Temp_Users temp_Users2=userDao.getTempUsers(temp_Users);
		
		Users users=new Users();
		
		users.setOpen_id(openId);
		users.setName(temp_Users2.getName());
		users.setCard_type(temp_Users2.getCard_type());
		users.setId_number(temp_Users2.getId_number());
		users.setPhone(temp_Users2.getPhone());
		users.setDate(new Date());
		
		Date license_start_date = null;
		Date license_end_date = null;
		
		try {
			if(startTime!=null&&!startTime.equals(""))
				license_start_date = new Date(new Long(startTime));
			if(endTime!=null&&!endTime.equals(""))
				license_end_date=new Date(new Long(endTime));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		User_License user_License=new User_License();
		
		user_License.setOpen_id(openId);
		user_License.setPhone(temp_Users2.getPhone());
		user_License.setLicense(license);
		user_License.setAuthentication(1);
		user_License.setAuthen_date(new Date());
		user_License.setRegion(region);
		if(temp_Users2.getAddress()!=null)
			user_License.setAddress(temp_Users2.getAddress());
		user_License.setArea(area);
		if(business_state!=null)
			user_License.setBusiness_state(business_state);
		
		if(license_start_date!=null)
			user_License.setLicense_start_date(license_start_date);
		if(license_end_date!=null)
			user_License.setLicense_end_date(license_end_date);
		
		if(business_state!=null&&!business_state.equals(""))
			user_License.setBusiness_state(business_state);
		if(address!=null&&!address.equals(""))
			user_License.setAddress(address);
		System.out.println("license_start_date="+license_start_date);
		
		
		int i=userDao.insertUser(users, user_License);
		
		if (i > 0) {
			
			userDao.deleteTempUsers(temp_Users);
			
			Position position = new Position();

			position.setLicense(license);
			position.setLng(temp_Users2.getLng());
			position.setLat(temp_Users2.getLat());
			
			licenseDAO.updatePositionByLicense(position, false);	
			
			try {

				WechatSendMessageController wechatSendMessageController = new WechatSendMessageController();


				Runnable r = new Runnable() {

					@Override
					public void run() {

						String title = "";
						String templateID="";
						

						title="审核成功通知";
						templateID="94bHvwYcW9ITl-FbQnY1BnFFrqorue-RkGdd5hND0bU"; // 预约受理通知
						
						
						Date date = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
						String time = sdf.format(date);

						WechatSendMessageController wechatSendMessageController = new WechatSendMessageController();

						wechatSendMessageController.sendMessage(openId, templateID,
								// "1vQfPSl4pSvi5UnmmDhVtueutq2R1w7XYRMts294URg",
								title , "http://lzgfgs.com/ycglj/mobile/asset",
								"尊敬的"+temp_Users2.getName(), "许可证办理成功", time, title, "", "","");

					}
				};

				Thread t = new Thread(r);
				t.start();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
			
		}

		return i;
		
	}
	
	@RequestMapping(value="transact")
	public @ResponseBody Integer transact(@RequestParam Integer type,@RequestParam String license,
			Integer agree,String startTime,String endTime,String name,String cause,HttpServletRequest request){

		HttpSession session = request.getSession();

		String openId = session.getAttribute("openId").toString();

		String transactOpenId = null;

		if (agree == null) {
			agree = 1;
		}

		int i = 0;

		if (agree == 1) {

			if (type == 4) {

				User_License user_License = new User_License();
				user_License.setLimit(1);
				user_License.setOffset(0);
				user_License.setNotIn("license");

				String[] where = { "license=", license };

				user_License.setWhere(where);

				User_License user_License2 = userDao.getUserLicense(user_License);

				transactOpenId = user_License2.getOpen_id();

				Date stop_start_date = null;
				Date stop_end_data = null;

				try {
					if (startTime != null && !startTime.equals(""))
						stop_start_date = new Date(new Long(startTime));
					if (endTime != null && !endTime.equals(""))
						stop_end_data = new Date(new Long(endTime));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (stop_start_date != null)
					user_License2.setStop_start_date(stop_start_date);
				if (stop_end_data != null)
					user_License2.setStop_end_data(stop_end_data);

				i = licenseDAO.deleteLicense(openId, user_License2);

			} else if (type == 6) {

				Temp_User_License temp_User_License = new Temp_User_License();

				temp_User_License.setLimit(1);
				temp_User_License.setOffset(0);
				temp_User_License.setNotIn("open_id");

				String[] where = { "license=", license };

				temp_User_License.setWhere(where);

				Temp_User_License temp_User_License2 = licenseDAO.getTempUserLicense(temp_User_License);

				transactOpenId = temp_User_License2.getOpen_id();

				i = licenseDAO.insertLicense(temp_User_License2);

			} else if (type == 7) {

				Temp_Change temp_Change = new Temp_Change();

				temp_Change.setLimit(1);
				temp_Change.setOffset(0);
				temp_Change.setNotIn("license");

				String[] where = { "license=", license };

				temp_Change.setWhere(where);

				Temp_Change temp_Change2 = licenseDAO.selectTempChange(temp_Change);

				String changeName = temp_Change2.getName();

				String changeValue = temp_Change2.getValue();

				int up = 0;

				User_License user_License = new User_License();

				user_License.setLimit(1);
				user_License.setOffset(0);
				user_License.setNotIn("license");

				user_License.setWhere(where);

				List list = userDao.getUserLicenseById(user_License);

				try {
					user_License = (User_License) list.get(0);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				transactOpenId = user_License.getOpen_id();

				if (changeName.equals("name")) {

					Users users = new Users();
					users.setName(changeValue);

					if (user_License.getOpen_id() != null && !user_License.getOpen_id().equals("")) {

						String[] where2 = { "open_id=", user_License.getOpen_id() };
						users.setWhere(where2);
						up = userDao.updateOnlyUsers(users);
					}

				} else if (changeName.equals("address")) {

					User_License user_License2 = new User_License();

					user_License2.setAddress(changeValue);

					user_License2.setWhere(where);

					up = userDao.updateOnlyLicense(user_License2);

				}

				if (up > 0) {

					licenseDAO.deleteTempChange(temp_Change);

					User_License user_License3 = new User_License();

					user_License3.setAuthentication(1);

					user_License3.setWhere(where);

					up = userDao.updateOnlyLicense(user_License3);

				}

				i = up;
			} else if (type == 5) {

				User_License user_License = new User_License();

				user_License.setLimit(1);
				user_License.setOffset(0);
				user_License.setNotIn("license");

				String[] where = { "license=", license };

				user_License.setWhere(where);

				List list = userDao.getUserLicenseById(user_License);

				try {
					user_License = (User_License) list.get(0);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				transactOpenId = user_License.getOpen_id();

				User_License user_License2 = new User_License();

				user_License2.setAuthentication(1);

				user_License2.setWhere(where);

				i = userDao.updateOnlyLicense(user_License2);

			} else if (type == 8) {

				User_License user_License = new User_License();

				user_License.setLimit(1);
				user_License.setOffset(0);
				user_License.setNotIn("license");

				String[] where = { "license=", license };

				user_License.setWhere(where);

				List list = userDao.getUserLicenseById(user_License);

				try {
					user_License = (User_License) list.get(0);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				transactOpenId = user_License.getOpen_id();

				i = licenseDAO.deleteUserLicense(license);

			}

		} else {

			if (type != 6) {

				User_License user_License = new User_License();

				user_License.setLimit(1);
				user_License.setOffset(0);
				user_License.setNotIn("license");

				String[] where = { "license=", license };

				user_License.setWhere(where);

				List list = userDao.getUserLicenseById(user_License);

				try {
					user_License = (User_License) list.get(0);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				transactOpenId = user_License.getOpen_id();

				User_License user_License2 = new User_License();

				user_License2.setAuthentication(1);

				user_License2.setWhere(where);

				i = userDao.updateOnlyLicense(user_License2);

			} else {

				Temp_User_License temp_User_License = new Temp_User_License();

				temp_User_License.setLimit(1);
				temp_User_License.setOffset(0);
				temp_User_License.setNotIn("open_id");

				String[] where = { "license=", license };

				temp_User_License.setWhere(where);

				temp_User_License = licenseDAO.getTempUserLicense(temp_User_License);

				transactOpenId = temp_User_License.getOpen_id();

				Temp_User_License temp_User_License2 = new Temp_User_License();

				temp_User_License2.setAuthentication(4);

				temp_User_License2.setWhere(where);

				i = licenseDAO.updateTempUserLicense(temp_User_License2);

			}
		}

		final Integer agree2 = agree;

		try {

			WechatSendMessageController wechatSendMessageController = new WechatSendMessageController();

			// List list=userService.getUserByTransact();

			final String transactOpenId2 = transactOpenId;

			Runnable r = new Runnable() {

				@Override
				public void run() {

					String title = "";

					if (type == 4) {
						title = "停业";
					} else if (type == 5) {
						title = "补办";
					} else if (type == 6) {
						title = "恢复营业";
					} else if (type == 7) {
						title = "变更";
					} else if (type == 8) {
						title = "歇业";
					}

					Date date = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
					String time = sdf.format(date);

					WechatSendMessageController wechatSendMessageController = new WechatSendMessageController();

					String result = "";

					if (agree2 == 1) {
						result = "已通过";
					} else {
						result = "已拒绝";
					}

					if (agree2 == 1) {
						wechatSendMessageController.sendMessage(transactOpenId2,
								"94bHvwYcW9ITl-FbQnY1BnFFrqorue-RkGdd5hND0bU", // 审核结果通知
								// "1vQfPSl4pSvi5UnmmDhVtueutq2R1w7XYRMts294URg",
								title + "申请", "http://lzgfgs.com/ycglj/mobile/asset",
								"尊敬的零售户" + name + ",您的" + title + "申请", result, time, "", "", "", "");
					} else {
						wechatSendMessageController.sendMessage(transactOpenId2, "b1ujoxmvkW9112uTDWWy7TZ7cgd4IWI86MaPN55OLqw",
								// "1vQfPSl4pSvi5UnmmDhVtueutq2R1w7XYRMts294URg",
								title + "申请", "http://lzgfgs.com/ycglj/mobile/asset", title + "申请", name, time, "未通过",
								cause, "请修改后，重新提交", "");
					}

				}
			};

			Thread t = new Thread(r);
			t.start();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return i;
		
	}
	
	@RequestMapping("getAllResumeUser")
	public @ResponseBody Map<String, Object> getAllResumeUser(@RequestParam Integer limit,@RequestParam Integer offset,String sort,String order,
			String search,String area,Integer type,HttpServletRequest request){
			
		Map searchMap=new HashMap<>();
		
		searchMap.put("authentication=", "6");
		
		if(search!=null&&!search.trim().equals("")){
			search="%"+search+"%";  
			searchMap.put("name like ", search);
		}		
					
		if(area!=null&&!area.trim().equals("")){
			searchMap.put("area =", area);
		}	
		
		//int offset=(page-1)*limit;
		
		return userDao.getAllTempLicenseJoin(limit, offset, sort, order, searchMap);
				
	}
	
	@RequestMapping("getResumeUser")
	public @ResponseBody Map<String, Object> getResumeUser(@RequestParam String openId,HttpServletRequest request){
			
		Map searchMap = new HashMap<>();

		searchMap.put("[Temp_User_License].open_id = ", openId);

		Map map = new HashMap<>();

		map = userDao.getAllTempLicenseJoin(1, 0, null, null, searchMap);

		try {
			List list = (List) map.get("rows");

			Users_License_Join users_License_Join = (Users_License_Join) list.get(0);
			
			Users_License_Position_Join users_License_Position_Join=new Users_License_Position_Join();
			
			users_License_Position_Join.setOpen_id(users_License_Join.getOpen_id());
			users_License_Position_Join.setLicense(users_License_Join.getLicense());
			
			List fileBytes = licenseDAO.allLicenseImageByGUID(request, users_License_Position_Join);

			map.put("fileBytes", fileBytes);
			
			Map searchMap2 = new HashMap<>();

			searchMap2.put("open_id = ", users_License_Join.getOpen_id());
			searchMap2.put("currently = ", "1");

			if (users_License_Join.getLicense() != null && !users_License_Join.getLicense().equals("")) {
				searchMap2.put("license=", users_License_Join.getLicense());
			}

			Map map2 = userDao.getAllUserData(request, 1000, 0, "", "", searchMap2);

			List fileBytes2=(List) map2.get("fileBytes");
			
			map.put("fileBytes2", fileBytes2);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return map;
		
	}
	
	@RequestMapping("/getCheckPerson")
	public @ResponseBody Check_Person getCheckPerson(@RequestParam String phone,HttpServletRequest request){
		
		Check_Person check_Person = new Check_Person();

		Map search = new HashMap<>();

		search.put("phone=", phone);

		Map map = licenseDAO.getAllCheckPerson(1, 0, "id", "asc", search);

		List list = (List) map.get("data");

		try {
			check_Person = (Check_Person) list.get(0);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return check_Person;
	}
	
	@RequestMapping("/postAdvice")
	public @ResponseBody Integer postAdvice(@RequestParam String phone,@RequestParam String advice,HttpServletRequest request){
		
		WechatSendMessageController wechatSendMessageController = new WechatSendMessageController();
		
		List list=userService.getUserByTransact();
		
		Iterator iterator=list.iterator();
		
		Date date=new Date();		
		SimpleDateFormat sdf  =   new  SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " ); 
		String time = sdf.format(date);
		
		int i=0;
		
		while (iterator.hasNext()) {
			
			com.ycglj.manage.model.Users users=(com.ycglj.manage.model.Users) iterator.next();
			
			String transactOpenId=users.getOpenId();
			
			wechatSendMessageController.sendMessage(transactOpenId, "HRSujAWukFOthuMbOITAcDPvtgimAmz3hNyltOAyEAE", //投诉建议通知
					//"1vQfPSl4pSvi5UnmmDhVtueutq2R1w7XYRMts294URg", 
					"投诉建议通知",
					"",
					"投诉人电话:"+phone,time, advice, "", "", "", 
					"");
			
			i++;
		}
		
		return i;
		
	}
	
}
