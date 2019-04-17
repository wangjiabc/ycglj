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

import com.alibaba.fastjson.JSONObject;
import com.ycglj.manage.dao.LicenseDAO;
import com.ycglj.manage.dao.OrderDAO;
import com.ycglj.manage.dao.UserDAO;
import com.ycglj.manage.daoModel.Check_Person;
import com.ycglj.manage.daoModel.Crimal_Case;
import com.ycglj.manage.daoModel.Crimal_Record;
import com.ycglj.manage.daoModel.FileSelfBelong;
import com.ycglj.manage.daoModel.Law_Case;
import com.ycglj.manage.daoModel.Not_License;
import com.ycglj.manage.daoModel.Position;
import com.ycglj.manage.daoModel.Temp_Change;
import com.ycglj.manage.daoModel.User_Data;
import com.ycglj.manage.daoModel.User_License;
import com.ycglj.manage.daoModel.Users;
import com.ycglj.manage.daoModel.WeiXin_User;
import com.ycglj.manage.daoModel.Weight_Log;
import com.ycglj.manage.daoModelJoin.User_Order_Join;
import com.ycglj.manage.daoModelJoin.Users_License_Join;
import com.ycglj.manage.daoModelJoin.Users_License_Position_Join;
import com.ycglj.manage.daoSQL.SelectExe;
import com.ycglj.manage.service.PhotoService;
import com.ycglj.manage.service.SellerService;
import com.ycglj.manage.service.UserService;
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
	
	@RequestMapping("/getAll")
	public @ResponseBody Map<String, Object> getAll(@RequestParam Integer position,@RequestParam Integer limit,@RequestParam Integer offset,
			@RequestParam Double lng, @RequestParam Double lat,String search,Integer weight,HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		
		String openId=session.getAttribute("openId").toString();
		
		com.ycglj.manage.model.Users users=userService.getUserByOnlyOpenId(openId);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		Map where=new HashMap<>();
		
		Integer place=users.getPlace();
		
		Integer area=users.getArea();
		
		Integer business=users.getBusiness();
		
		System.out.println("place="+place);
		
		if(area==null){
			area=1;
		}
		
		if(place==2){
			where.put("area=",String.valueOf(area));
		}
		
		/*
		if(business==null){
			business=5;
		}
		System.out.println("place="+place);
		if(place==2){
			if(business==1){
				where.put("area=",String.valueOf(area));
			}else if(business==2){
				where.put("area=",String.valueOf(area));
			}else if(business==3||business==4){
				Check_Person check_Person=new Check_Person();
				check_Person.setLimit(1);
				check_Person.setOffset(0);
				check_Person.setNotIn("id");
				String[] where2={"phone=",users.getPhone()};
				check_Person.setWhere(where2);
				List list=userDao.getAllCheck_Person(check_Person);
				String region="";
				try{
					check_Person=(Check_Person) list.get(0);
					region=check_Person.getDepartment();
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				where.put("area=",String.valueOf(area));
				if(region!=null&&!region.equals(""))
					where.put("region=",region);
			}else {
				where.put("area=",String.valueOf(area));
			}
		}else if(place==3){
			if(business==1){

			}else if(business==2){
				
			}else if(business==3){
				
			}
		}
		*/
		
		if(weight!=null&&weight==1){
			where.put("weight=", "1");
		}
		
		Map license_Positions = null;
	
		if(position==1){
			if(search!=null&&!search.trim().equals("")){
				search="%"+search+"%";  
				where.put("[Users].name like ", search);
				where.put("t2.license like ", search);
				where.put("t2.phone like ", search);
				where.put("t2.address like ", search);
			}		

			license_Positions=licenseDAO.findAllLicense_Position(limit, offset, lng, lat, "or", where);
		}else if(position==0){
			Map searchMap=new HashMap<>();
			if(search!=null&&!search.trim().equals("")){
				search="%"+search+"%";  				
				searchMap.put("[Users].name like ", search);
				searchMap.put("[User_License].license like ", search);
				searchMap.put("[User_License].phone like ", search);
				searchMap.put("[User_License].address like ", search);
			}
			if(place==2){
				license_Positions=licenseDAO.getAllLicense_Position2(limit, offset, "", "", searchMap,String.valueOf(area));
			}else{
				license_Positions=licenseDAO.getAllLicense_Position2(limit, offset, "", "", searchMap,null);
			}
		}else if(position==2){		
			if(place==2){
				license_Positions=licenseDAO.findAllLicenseNotPosition(limit, offset, search,area);
			}else{
				license_Positions=licenseDAO.findAllLicenseNotPosition(limit, offset, search,null);
			}
		}
		
		List licenses=(List) license_Positions.get("rows");
		Integer total=(Integer) license_Positions.get("total");
		
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
	
	@RequestMapping("getUserLicenseJoin")
	public @ResponseBody List getUserLicenseJoin(HttpServletRequest request){
		
		HttpSession session = request.getSession();
		
		String openId=session.getAttribute("openId").toString();
	
		User_License user_License=new User_License();
		user_License.setLimit(1000);
		user_License.setOffset(0);
		user_License.setNotIn("open_id");
		
		String[] where={"[User_License].open_id = ",openId};
		user_License.setWhere(where);
		
		List list=userDao.getUserLicenseJoin(user_License);
		
	    return list;
	}
	
	@RequestMapping("getTempUserLicenseJoin")
	public @ResponseBody List getTempUserLicenseJoin(HttpServletRequest request){
		
		HttpSession session = request.getSession();
		
		String openId=session.getAttribute("openId").toString();
		
		Map search=new HashMap<>();
		
		search.put("[Temp_User_License].open_id = ",openId);

		Map map=userDao.getAllTempLicenseJoin(10, 0, null, null, search);
		
		List list=(List) map.get("rows");
		
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
			 Map licenseMap=licenseDAO.getAllLicense_Position(1, 0, "", "","and", searchMap);

			 List<Users_License_Position_Join> users_License_Position_Joins = (List<Users_License_Position_Join>) licenseMap.get("rows");
			 
			users_License_Position_Join = users_License_Position_Joins.get(0);

			map.put("roomInfo", users_License_Position_Join);

			List fileBytes = licenseDAO.allLicenseImageByGUID(request, users_License_Position_Join);

			map.put("fileBytes", fileBytes);

			Map lawSearchMap=new HashMap<>();
			
			lawSearchMap.put("[Crimal_Record].license = ",users_License_Position_Join.getLicense());
			
			List lawCases=(List) licenseDAO.getAllCrimalRecordJoin(1000, 0, "criminal_time", "desc",lawSearchMap).get("data");
			
			map.put("lawCases", lawCases);
			
			HttpSession session = request.getSession();
			
			String openId=session.getAttribute("openId").toString();
			
			com.ycglj.manage.model.Users users=userService.getUserByOnlyOpenId(openId);
			
			map .put("place", users.getPlace());
			map.put("business", users.getBusiness());
			
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
		
		Users_License_Position_Join users_License_Position_Join2=new Users_License_Position_Join();
		
		if(users_License_Position_Join.getLng()!=null){
			
			Double lng=users_License_Position_Join.getLng();
			Double lat=users_License_Position_Join.getLat();
			
			Map pointMap=licenseDAO.findLicenseByPoint(2, 0, lng, lat, Double.valueOf(1000000), "");
			
			List list=(List) pointMap.get("rows");
			
			try{
				users_License_Position_Join2=(Users_License_Position_Join) list.get(0);
				if(users_License_Position_Join2.getLicense().equals(license))
					users_License_Position_Join2=(Users_License_Position_Join) list.get(1);
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
						
		}
		
		map.put("distanceLicense", users_License_Position_Join2);
		
		List list=licenseDAO.getAllCaseByLicense(license);
		
		Iterator iterator=list.iterator();
		
		String crimal="";
		
		while (iterator.hasNext()) {
			
			Law_Case law_Case=(Law_Case) iterator.next();
			
			crimal=crimal+","+law_Case.getCriminal_type()+law_Case.getCriminal_number()+ "条";
		
		}
		
		if(!crimal.equals("")){
			map.put("crimal", crimal.substring(1,crimal.length()));
		}else{
			map.put("crimal", "");
		}
		
		Temp_Change temp_Change=new Temp_Change();
		
		temp_Change.setLimit(1);
		temp_Change.setOffset(0);
		temp_Change.setNotIn("license");
		
		String[] where={"license=",license};
		
		temp_Change.setWhere(where);
		
		Temp_Change temp_Change2=licenseDAO.selectTempChange(temp_Change);
		
		map.put("change", temp_Change2);
		
		return map;
		
	}
	
	@RequestMapping("/getNotLicenseByID")
	public @ResponseBody Map<String, Object> getNotLicenseByID(@RequestParam String license
			,HttpServletRequest request){
		Map map=new HashMap<>();
		
		String text=licenseDAO.getNotLicense(license);
		
		map.put("text", text);
		
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
			@RequestParam String addComp,
			@RequestParam Double wgs84_lng,@RequestParam Double wgs84_lat){
		
		int upload=0;
				
		try {
			if(!classType.equals("User_License")){
				Map searchMap=new HashMap<>();
				searchMap.put("[User_License].license=", license);
	        	Map map=licenseDAO.getAllLicense_Position(1, 0, "", "","and", searchMap);
	        	List<Users_License_Position_Join> license_Position_Joins=(List<Users_License_Position_Join>) map.get("rows");
	        	Users_License_Position_Join users_License_Position_Join=license_Position_Joins.get(0);
	        	id=users_License_Position_Join.getOpen_id();
	        	
	        	System.out.println("license="+license);
	        	MyTestUtil.print(license_Position_Joins);
	        }
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

			try {
				
				JSONObject jsonObject = JSONObject.parseObject(addComp);

				String province = jsonObject.getString("province");
				String city = jsonObject.getString("city");
				String district = jsonObject.getString("district");
				String street = jsonObject.getString("street");
				String streetNumber = jsonObject.getString("streetNumber");
				position.setProvince(province);
				position.setCity(city);
				position.setDistrict(district);
				position.setStreet(streetNumber);
				position.setStreet_number(streetNumber);
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			Date date=new Date();
		    
		    position.setDate(date);
		    
			licenseDAO.updatePositionByLicense(position, isUpdate);
		}
		
		return upload;
		
	}
	
	@RequestMapping("/manualUpdatePosition")
	public @ResponseBody Integer manualUpdatePosition(
			HttpServletRequest request,ServletResponse response, 
    		@RequestParam String license,
			@RequestParam Double lng,@RequestParam Double lat,
			@RequestParam String addComp,
			@RequestParam Double wgs84_lng,@RequestParam Double wgs84_lat){

		int upload = 0;

		boolean isUpdate = true; // 有位置更新

		Position position = new Position();
		position.setLicense(license);
		position.setLat(lat);
		position.setLng(lng);
		position.setWgs84_lat(wgs84_lat);
		position.setWgs84_lng(wgs84_lng);

		try {

			JSONObject jsonObject = JSONObject.parseObject(addComp);

			String province = jsonObject.getString("province");
			String city = jsonObject.getString("city");
			String district = jsonObject.getString("district");
			String street = jsonObject.getString("street");
			String streetNumber = jsonObject.getString("streetNumber");
			position.setProvince(province);
			position.setCity(city);
			position.setDistrict(district);
			position.setStreet(streetNumber);
			position.setStreet_number(streetNumber);

		} catch (Exception e) {
			// TODO: handle exception
		}

		Date date = new Date();

		position.setDate(date);

		upload=licenseDAO.updatePositionByLicense(position, isUpdate);

		return upload;
		
	}
	
	@RequestMapping("/findPositionByLicense")
	public @ResponseBody Position findPositionByLicense(@RequestParam String license){
		
		Position position = new Position();
		position.setLimit(1);
		position.setOffset(0);
		position.setNotIn("id");
		String[] where={"license=",license};
		
		position.setWhere(where);
		position=(Position) licenseDAO.findPosition(position).get(0);
		
		return position;
	}
	
	
	@RequestMapping("/insertIntoCrimalCase")
	public @ResponseBody Integer insertIntoCrimalCase(@RequestParam String license,@RequestParam String dateTime,
			@RequestParam String ay,String remark,Double lng,Double lat,HttpServletRequest request){
		
		HttpSession session = request.getSession();
		
		String openId=session.getAttribute("openId").toString();
		
		com.ycglj.manage.model.Users users=userService.getUserByOnlyOpenId(openId);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		Map where=new HashMap<>();
		
		Integer place=users.getPlace();
		
		Integer area=users.getArea();
		
		Integer business=users.getBusiness();

		Map search=new HashMap<>();
		
		search.put("[User_License].license=", license);
		
		List list2=(List) licenseDAO.getAllLicense_Position(1, 0, null, null, "and", search).get("rows");
		
		Users_License_Position_Join users_License_Position_Join=(Users_License_Position_Join) list2.get(0);
		
		if(place==3){
			return -5;
		}
		if(place==2){
			if(business==4){
				Check_Person check_Person=new Check_Person();
				check_Person.setLimit(1);
				check_Person.setOffset(0);
				check_Person.setNotIn("id");
				String[] where2={"phone=",users.getPhone()};
				check_Person.setWhere(where2);
				List list=userDao.getAllCheck_Person(check_Person);
				String region="";
				try{
					check_Person=(Check_Person) list.get(0);
					region=check_Person.getDepartment();
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				if(region!=null&&!region.equals("")){
					if(!region.equals(users_License_Position_Join.getRegion())){
						return -5;
					}
				}else{
					return -5;
				}
					
			}else{
				return -5;
			}
		}
		
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
					content = content + ayString + "条";
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
		crimal_Record.setUp_open_id(openId);
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
	
	@RequestMapping("/getCrimalCase")
	public @ResponseBody List getCrimalCase(@RequestParam String crimal_id){
		return licenseDAO.getCrimalCase(crimal_id);
	}
	
	@RequestMapping("/updateCrimalCase")
	public @ResponseBody Integer updateCrimalCase(@RequestParam String crimal_id,@RequestParam String license,
			@RequestParam String ay,String remark,Double lng,Double lat,HttpServletRequest request){
		
		HttpSession session = request.getSession();
		
		String openId=session.getAttribute("openId").toString();
		
		com.ycglj.manage.model.Users users=userService.getUserByOnlyOpenId(openId);
		
		int place=users.getPlace();
		
		if(place<3){
			return -2;
		}
		
		UUID uuid=UUID.randomUUID();
		
		Date date=new Date();
		
		SimpleDateFormat sdf  =   new  SimpleDateFormat("yyyy-MM-dd"); 
		Date time = new Date();
		
		String[] ayStrings = ay.split(",");
		
		int i=0;
		
		Crimal_Case crimal_Case=new Crimal_Case();
		
		List<Crimal_Case> crimalCaseList=new ArrayList<>();
		
		String content="";
		
		if (ay != null && !ay.equals("")) {
			for (String ayString : ayStrings) {
				System.out.println("ayString="+ayString);
				//ayString=ayString.substring(1, ayString.length() - 1);
				
				if (i % 2 == 0) {
					crimal_Case.setCriminal_type(ayString);
					content = content + "," + ayString;
				} else if (i != 0 && i % 2 != 0) {
					content = content + ayString + "条";
					int number = Integer.parseInt(ayString);
					crimal_Case.setLicense(license);
					crimal_Case.setCrimal_id(uuid.toString());
					crimal_Case.setOpen_id(openId);
					crimal_Case.setCriminal_number(number);
					crimal_Case.setUp_date(date);
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
		crimal_Record.setUp_open_id(openId);
		crimal_Record.setUp_data(date);
		
		System.out.println("remark="+remark);
		
		if(remark!=null&&!remark.equals("")){
			crimal_Record.setRemark(remark);
			System.out.println("remark="+remark);
		}
		
		
		return licenseDAO.updateCrimalCase(crimal_id, crimalCaseList, crimal_Record,openId,lng,lat);

	}
	
	@RequestMapping("/insertNotLicenseCrimalCase")
	public @ResponseBody Integer insertNotLicenseCrimalCase(@RequestParam String name,@RequestParam String phone,
			@RequestParam String dateTime,String sex,String age,String address,String id_number,
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
					content = content + ayString + "条";
					int number = Integer.parseInt(ayString);
					crimal_Case.setLicense(phone);
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
		crimal_Record.setLicense(phone);
		crimal_Record.setCrimal_id(uuid.toString());
		crimal_Record.setCriminal_content(content);
		crimal_Record.setCriminal_time(time);
		crimal_Record.setOpen_id(openId);
		crimal_Record.setUp_open_id(openId);
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
		
		Not_License not_License=new Not_License();
		
		not_License.setName(name);
		not_License.setPhone(phone);
		not_License.setCard_type("identity");
		not_License.setId_number(id_number);
		not_License.setAddress(address);
		not_License.setLat(lat);
		not_License.setLng(lng);
		not_License.setDate(date);
		
		return licenseDAO.insertNotLicenseCrimalCase(not_License, crimalCaseList, crimal_Record);

	}
	
	@RequestMapping("/getAllCheckPerson")
	public @ResponseBody Map<String, Object> getAllCheckPerson(@RequestParam Integer limit,@RequestParam Integer page,
			String sort,String order,String search,HttpServletRequest request){
		
		if(sort!=null&&!sort.equals("")){
			
		}else{
			sort="id";
		}
		
		if(order!=null&&order.equals("")){
			
		}else{
			order="asc";
		}
		
		Map searchMap=new HashMap<>();
		
		if(search!=null&&!search.equals("")){
			searchMap.put(" name like ", "%"+search+"%");
		}
		
		int offset=(page-1)*limit;
		
		if(offset<0)
			offset=0;
		
		return licenseDAO.getAllCheckPerson(limit, offset, sort, order, searchMap);
		
		
	}
	
	@RequestMapping("/getCheckPerson")
	public @ResponseBody Check_Person getCheckPerson(HttpServletRequest request){
		
		HttpSession session = request.getSession();
		
		String openId=session.getAttribute("openId").toString();
		
		WeiXin_User weiXin_User=orderDao.selectWeiXinUser(openId);
		
		Check_Person check_Person=new Check_Person();
		
		if (weiXin_User.getPhone() != null && !weiXin_User.getPhone().equals("")) {
			
			Map search = new HashMap<>();

			search.put("phone=", weiXin_User.getPhone());

			Map map = licenseDAO.getAllCheckPerson(1, 0, "id", "asc", search);

			List list=(List) map.get("data");
			
			try{
				check_Person=(Check_Person) list.get(0);
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		return check_Person;
	}
	
	@RequestMapping("/updateWeight")
	public @ResponseBody Integer updateWeight(@RequestParam String license,
			@RequestParam Integer state,@RequestParam Double lng,
			@RequestParam Double lat,HttpServletRequest request){
		
		HttpSession session = request.getSession();
		
		String openId=session.getAttribute("openId").toString();
		
		Weight_Log weight_Log=new Weight_Log();
		
		weight_Log.setLicense(license);
		weight_Log.setState(state);
		weight_Log.setLng(lng);
		weight_Log.setLat(lat);
		weight_Log.setCampusAdmin(openId);
		weight_Log.setDate(new Date());
		
		return licenseDAO.updateWeight(weight_Log);
		
	}
	
	@RequestMapping("/deleteImgById")
	public @ResponseBody Integer deleteImgById(@RequestParam String imgName){
		
		int i=0;
		
		i=userDao.deleteFileSelfBelong(imgName);
		
		if(i<1)
			i=userDao.deleteUserData(imgName);
		
		return i;
	}
	
}
