package com.ycglj.weixin.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ycglj.manage.dao.LicenseDAO;
import com.ycglj.manage.dao.OrderDAO;
import com.ycglj.manage.dao.UserDAO;
import com.ycglj.manage.daoModel.Crimal_Case;
import com.ycglj.manage.daoModel.Crimal_Record;
import com.ycglj.manage.daoModel.Law_Case;
import com.ycglj.manage.daoModel.Position;
import com.ycglj.manage.daoModel.User_Data;
import com.ycglj.manage.daoModel.User_License;
import com.ycglj.manage.daoModel.Users;
import com.ycglj.manage.daoModelJoin.User_Order_Join;
import com.ycglj.manage.daoModelJoin.Users_License_Position_Join;
import com.ycglj.manage.service.PhotoService;
import com.ycglj.manage.service.SellerService;
import com.ycglj.manage.service.WeiXinService;
import com.ycglj.manage.tools.MyTestUtil;
import com.ycglj.sqlserver.context.Connect;
import com.ycglj.weixin.insweptcontroller.FileUploadController;

@Controller
@RequestMapping("/mobile/user")
public class MoblieUserController {

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
	
	private PhotoService photoService;
	
	@Autowired
	public void setPhotoService(PhotoService photoService) {
		this.photoService = photoService;
	}
	
	ApplicationContext applicationContext=new Connect().get();
	
	UserDAO userDao=(UserDAO) applicationContext.getBean("userdao");
	
	OrderDAO orderDao=(OrderDAO) applicationContext.getBean("orderdao");
	
	LicenseDAO licenseDAO=(LicenseDAO) applicationContext.getBean("licensedao");
	
	@RequestMapping("/getAll")
	public @ResponseBody Map<String, Object> getAll(@RequestParam Integer limit,@RequestParam Integer offset,
			@RequestParam Double lng, @RequestParam Double lat,String search,HttpServletRequest request) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String term="OR";
		
		Map where=new HashMap<>();
		
		if(search!=null&&!search.trim().equals("")){
			search="%"+search+"%";  
			where.put("[Users].name like ", search);
			where.put("[User_License].license like ", search);
			where.put("[User_License].phone like ", search);
			where.put("[User_License].address like ", search);
		}		


		Map license_Positions=licenseDAO.findAllLicense_Position(limit, offset, lng, lat, term, where);
		
		List licenses=(List) license_Positions.get("rows");
		int total=(int) license_Positions.get("total");
		
		map.put("rows", licenses);
		map.put("total", total);
		
		Map fileBytes=licenseDAO.License_PositionImageQuery(request, licenses);
		map.put("fileBytes", fileBytes);
		
		MyTestUtil.print(fileBytes);
		
		return map;
	}
	
	@RequestMapping("getUserAuthen")
	public @ResponseBody Integer getUserAuthen(HttpServletRequest request){
		
		HttpSession session = request.getSession();
		
		String openId=session.getAttribute("openId").toString();
	
		User_License user_License=new User_License();
		user_License.setLimit(1);
		user_License.setOffset(0);
		user_License.setNotIn("open_id");
		user_License.setOrder("desc");
		user_License.setSort("authentication");
		
		String[] where={"open_id = ",openId};
		user_License.setWhere(where);
		
		user_License=userDao.getUserLicense(user_License);
		
		if(user_License!=null){
			return user_License.getAuthentication();
		}else{
			return 0;
		}
		
	}
	
	@RequestMapping("getUserLicense")
	public @ResponseBody List getUserLicense(HttpServletRequest request){
		
		HttpSession session = request.getSession();
		
		String openId=session.getAttribute("openId").toString();
	
		User_License user_License=new User_License();
		user_License.setLimit(1000);
		user_License.setOffset(0);
		user_License.setNotIn("open_id");
		
		String[] where={"open_id = ",openId};
		user_License.setWhere(where);
		
		List list=userDao.getUserLicenseById(user_License);
	
		System.out.println("openId="+openId);
		
		MyTestUtil.print(list);
		
	    return list;
	}
	
	@RequestMapping("getUserData")
	public @ResponseBody Map<String, Object> getUserData(HttpServletRequest request){
			
			Map searchMap=new HashMap<>();
			
			HttpSession session = request.getSession();
			
			String openId=session.getAttribute("openId").toString();
			
			searchMap.put("[Users].open_id = ", openId);
			
			Map map=new HashMap<>();
			
			map=userDao.getAllUserJoin(100, 0, null,null, searchMap);
			
			List list=(List) map.get("data");
			
			try{
				
				searchMap=new HashMap<>();
				
				searchMap.put("open_id = ", openId);
				searchMap.put("currently = ", "1");
				
				Map map2=userDao.getAllUserData(request,1000, 0, "","", searchMap);
				
				map.put("imgData", map2);
				
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			return map;
			
	}
	
	@RequestMapping("/getRoomInfoByGUID")
	public @ResponseBody Map<String, Object> getRoomInfoByGUID(@RequestParam String license
			,HttpServletRequest request){
		Map searchMap=new HashMap<>();
		searchMap.put("[User_License].license = ", license);
		
		String term="AND";
		
		Map map=new HashMap<>();
		
		Users_License_Position_Join users_License_Position_Join = new Users_License_Position_Join();
		
		try {
			List<Users_License_Position_Join> users_License_Position_Joins = (List<Users_License_Position_Join>) licenseDAO
					.findAllLicense_Position(1, 0, null, null, term, searchMap).get("rows");

			users_License_Position_Join = users_License_Position_Joins.get(0);

			map.put("roomInfo", users_License_Position_Join);

			List fileBytes = licenseDAO.allLicenseImageByGUID(request, users_License_Position_Join);

			map.put("fileBytes", fileBytes);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		try {
			
			Map searchMap2 = new HashMap<>();

			searchMap2.put("open_id = ", users_License_Position_Join.getOpen_id());
			searchMap2.put("currently = ", "1");

			if (license != null && !license.equals("")) {
				searchMap2.put("license=", license);
			}

			Map map2 = userDao.getAllUserData(request, 1000, 0, "", "", searchMap2);

			List fileBytes2=(List) map2.get("fileBytes");
			
			map.put("fileBytes2", fileBytes2);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		List list=licenseDAO.getAllCaseByLicense(license);
		
		Iterator iterator=list.iterator();
		
		String crimal="";
		
		while (iterator.hasNext()) {
			
			Law_Case law_Case=(Law_Case) iterator.next();
			
			crimal=crimal+","+law_Case.getCriminal_type()+law_Case.getCriminal_number()+ "件";
		
		}
		
		if(!crimal.equals("")){
			map.put("crimal", crimal.substring(1,crimal.length()));
		}else{
			map.put("crimal", "");
		}
		
		return map;
		
	}
	
	@RequestMapping("/updatePositionByRoomInfo")
	public @ResponseBody Integer updatePositionByRoomInfo(
			HttpServletRequest request,ServletResponse response, 
			@RequestParam String imagename,@RequestParam String serverId,  
    		@RequestParam Integer campusId,@RequestParam String id,
    		@RequestParam String classType,
    		@RequestParam String license,
			@RequestParam Double lng,@RequestParam Double lat,
			@RequestParam Double wgs84_lng,@RequestParam Double wgs84_lat){
		
		int upload=0;
				
		try {
			upload=FileUploadController.fildUpload2(request, response, classType, serverId, campusId, id, classType, weixinService, license, photoService);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(upload==1){
			
			boolean isUpdate=false;   //如果有位置就不更新
			
			Position position=new Position();		
			position.setLicense(license);
			position.setLat(lat);
			position.setLng(lng);
			position.setWgs84_lat(wgs84_lat);
			position.setWgs84_lng(wgs84_lng);

			Date date=new Date();
		    
		    position.setDate(date);
		    
			licenseDAO.updatePositionByLicense(position, isUpdate);
		}
		
		return upload;
		
	}
	
	@RequestMapping("/insertIntoCrimalCase")
	public @ResponseBody Integer insertIntoCrimalCase(@RequestParam String license,@RequestParam String dateTime,
			@RequestParam String ay,String remark,Double lng,Double lat,HttpServletRequest request){
		
		HttpSession session = request.getSession();
		
		String openId=session.getAttribute("openId").toString();
		
		UUID uuid=UUID.randomUUID();
		
		Date date=new Date();
		
		SimpleDateFormat sdf  =   new  SimpleDateFormat("yyyy-MM-dd"); 
		Date time = null;
		try {
			 time = sdf.parse(dateTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ay=ay.substring(1,ay.length()-1);
		
		String[] ayStrings = ay.split(",");
		
		int i=0;
		
		Crimal_Case crimal_Case=new Crimal_Case();
		
		List<Crimal_Case> crimalCaseList=new ArrayList<>();
		
		String content="";
		
		if (ay != null && !ay.equals("")) {
			for (String ayString : ayStrings) {
				
				ayString=ayString.substring(1, ayString.length() - 1);
				
				if (i % 2 == 0) {
					crimal_Case.setCriminal_type(ayString);
					content = content + "," + ayString;
				} else if (i != 0 && i % 2 != 0) {
					content = content + ayString + "件";
					int number = Integer.parseInt(ayString);
					crimal_Case.setLicense(license);
					crimal_Case.setCrimal_id(uuid.toString());
					crimal_Case.setOpen_id(openId);
					crimal_Case.setCriminal_number(number);
					crimal_Case.setCriminal_time(time);
					crimal_Case.setDate(date);
					crimalCaseList.add(crimal_Case);
					crimal_Case = new Crimal_Case();
				}

				i++;
			}
		}else{
			return -1;
		}
		
		content=content.substring(1,content.length());
		
		Crimal_Record crimal_Record=new Crimal_Record();
		crimal_Record.setLicense(license);
		crimal_Record.setCrimal_id(uuid.toString());
		crimal_Record.setCriminal_content(content);
		crimal_Record.setCriminal_time(time);
		crimal_Record.setOpen_id(openId);
		crimal_Record.setDate(date);
		
		System.out.println("remark="+remark);
		
		if(remark!=null&&!remark.equals("")){
			crimal_Record.setRemark(remark);
			System.out.println("remark="+remark);
		}
		
		if(lat!=null&&lng!=null){
			crimal_Record.setLat(lat);
			crimal_Record.setLng(lng);
		}
		
		return licenseDAO.insertIntoCrimalCase(crimalCaseList, crimal_Record);

	}
	
}
