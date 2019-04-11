package com.ycglj.manage.controller;

import java.io.File;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ycglj.manage.dao.LicenseDAO;
import com.ycglj.manage.dao.OrderDAO;
import com.ycglj.manage.dao.UserDAO;
import com.ycglj.manage.daoModel.Check_Person;
import com.ycglj.manage.daoModel.Not_License;
import com.ycglj.manage.daoModel.Order_User;
import com.ycglj.manage.daoModel.User_Data;
import com.ycglj.manage.daoModel.User_License;
import com.ycglj.manage.daoModel.Users;
import com.ycglj.manage.daoModelJoin.Crimal_Record_Join;
import com.ycglj.manage.daoModelJoin.Users_License_Join;
import com.ycglj.manage.service.SellerService;
import com.ycglj.manage.service.UserService;
import com.ycglj.manage.service.WeiXinService;
import com.ycglj.manage.singleton.Singleton;
import com.ycglj.manage.tools.CopyFile;
import com.ycglj.manage.tools.MyTestUtil;
import com.ycglj.sqlserver.context.Connect;
import com.ycglj.weixin.controller.WechatSendMessageController;

@Controller
@RequestMapping("/user")
public class UserController {

	private SellerService sellerService;
	
	public SellerService getSellerService() {
		return sellerService;
	}

	@Autowired
	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}
	
	private WeiXinService weiXinService;
	
	@Autowired
	public void setWeiXinService(WeiXinService weiXinService) {
		this.weiXinService = weiXinService;
	}
	
	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	ApplicationContext applicationContext=new Connect().get();
	
	UserDAO userDao=(UserDAO) applicationContext.getBean("userdao");
	
	OrderDAO orderDao=(OrderDAO) applicationContext.getBean("orderdao");
	
	LicenseDAO licenseDAO=(LicenseDAO) applicationContext.getBean("licensedao");
	
	@RequestMapping("getAllUser")
	public @ResponseBody Map<String, Object> getAllUser(@RequestParam Integer limit,@RequestParam Integer page,String sort,String order,
			String search,String authentication,String region,HttpServletRequest request){
			
			Map searchMap=new HashMap<>();
			
			if(search!=null&&!search.trim().equals("")){
				search="%"+search+"%";  
				searchMap.put("name like ", search);
			}		
			
			if(authentication!=null&&!authentication.equals("")&&!authentication.equals("undefined")){
				searchMap.put("authentication = ", authentication);
			}	
			
			if(region!=null&&!region.trim().equals("")){
				searchMap.put("region =", region);
			}	
			
			int offset=(page-1)*limit;
			
			Map map=new HashMap<>();
			
			if(authentication==null||(!authentication.equals("11")&&!authentication.equals("8")&&!authentication.equals("6")&&!authentication.equals("4"))){
				map=userDao.getAllUserJoin(limit, offset, sort,order, searchMap);
			}else if(authentication.equals("11")){
				Map map2=userDao.getAllTempUserJoin(limit, offset, sort, order, new HashMap<>());
				map.put("code", "0");
				map.put("data", map2.get("rows"));
				map.put("count", map2.get("total"));
			}else{
				Map map2=userDao.getAllTempLicenseJoin(limit, offset, sort, order, searchMap);
				map.put("code", "0");
				map.put("data", map2.get("rows"));
				map.put("count", map2.get("total"));
			}
			
			return map;
	}
	
	
    @RequestMapping("insertUser")
	public @ResponseBody Integer insertUser(@RequestParam String license,@RequestParam String name,String idNumber,
			@RequestParam String phone,String address,String region,String business_status,String license_end_time,HttpServletRequest request) {
    	
    	Users users = new Users();
    	
    	users.setName(name);
    	users.setOpen_id(phone);
    	users.setPhone(phone);

    	if(idNumber!=null&&!idNumber.equals("")){
    		users.setId_number(idNumber);
    	}
    	
    	User_License user_License=new User_License();
    	
    	user_License.setLicense(license);
    	user_License.setOpen_id(phone);
    	user_License.setPhone(phone);
    	user_License.setAddress(address);
    	user_License.setRegion(region);
    	
    	//SimpleDateFormat sdf  =   new  SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " ); 
    	DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
    	
    	if(business_status!=null&&!business_status.equals("")){
    		user_License.setBusiness_state(business_status);
    		user_License.setBusiness_date(new Date());
    	}
    	
    	if(license_end_time!=null&&!license_end_time.equals("")&&license_end_time.equals("undefined")){
    		try {
				user_License.setLicense_end_date(fmt.parse(license_end_time));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	return userDao.insertUser(users, user_License);
    	
    }
	
	
	@RequestMapping("updateUser")
	public @ResponseBody Integer updateUser(@RequestParam String openId,String name,String idNumber,
			String phone,String license,String address,String region,String business_status,String license_end_time,HttpServletRequest request) {
		// TODO Auto-generated method stub

		System.out.println("openId="+openId);
		
		Users users = new Users();

		users.setOpen_id(openId);
		
		String[] where = { "open_id = ", openId };
		
		users.setWhere(where);
		
		if(name!=null&&!name.equals("")){
			users.setName(name);
		}
		
		if(idNumber!=null&&!idNumber.equals("")){
			users.setId_number(idNumber);
		}
		
		if(phone!=null&&!phone.equals("")){
			users.setPhone(phone);
		}
		
		users.setUp_date(new Date());
		
		System.out.println("license="+license);
		
		User_License user_License=new User_License();
		
		if(license!=null&&!license.equals("")){
			user_License.setLicense(license);
		}
		
		if(address!=null&&!address.equals(""))
			user_License.setAddress(address);
		
		if(region!=null&&!region.equals(""))
			user_License.setRegion(region);
		
		if(business_status!=null&&!business_status.equals(""))
			user_License.setBusiness_state(business_status);
		
		if(license_end_time!=null&&!license_end_time.equals("")){
			DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
			try {
				user_License.setLicense_end_date(fmt.parse(license_end_time));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return userDao.updateUserPhone(users,user_License);
		
	}
	
	
	@RequestMapping("updateUserAuth")
	public @ResponseBody Integer updateUserAuth(@RequestParam String openId,Integer authentication,
			@RequestParam String license,HttpServletRequest request) {
		// TODO Auto-generated method stub

		User_License user_License=new User_License();
		
		String[] where={"open_id = ", openId,"license = ",license};
		
		user_License.setWhere(where);
		user_License.setAuthentication(authentication);

		return userDao.updateUserLicense(user_License);
		
	}
	
	@RequestMapping("getUserDateById")
	public @ResponseBody Map<String, Object> getUserDateById(@RequestParam String openId,@RequestParam String license,HttpServletRequest request) {
		
		List<User_Data> list;
		
		Map searchMap=new HashMap<>();
		
		searchMap.put("open_id = ", openId);
		searchMap.put("currently = ", "1");
		
		if(license!=null&&!license.equals("")){
			searchMap.put("license=", license);
		}
		
		return userDao.getAllUserData(request,1000, 0, "","", searchMap);
				
	}
	
	
	@RequestMapping("setAuthentication")
	public @ResponseBody Integer setAuthentication(@RequestParam String openId,
			@RequestParam String license,@RequestParam  Integer authentication,HttpServletRequest request) {
					
		User_License user_License=new User_License();
		
		String[] where={"open_id = ", openId,"license = ",license};
		
		user_License.setWhere(where);
		user_License.setAuthentication(authentication);

		return userDao.updateUserLicense(user_License);
		
	}
	
	@RequestMapping("sendMessage")
	public @ResponseBody Integer sendMessage(@RequestParam String openId,
			@RequestParam String license,@RequestParam String content,
			@RequestParam  Integer result,HttpServletRequest request) {

		WechatSendMessageController wechatSendMessageController=new WechatSendMessageController();
		
		Map search=new HashMap<>();
		
		search.put("[Users].open_id = ",openId);
		
		Map map=userDao.getAllUserJoin(10, 0 , "", "", search);
		
		List list=(List) map.get("data");
		
		String userName = "";
		
		System.out.println("content="+content);
		
		Date date=new Date();		
		SimpleDateFormat sdf  =   new  SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " ); 
		String time = sdf.format(date);
		
		try {
			
			Users_License_Join users_License_Join = (Users_License_Join) list.get(0);

			userName = users_License_Join.getName();
			
			time=sdf.format(users_License_Join.getDate());
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		int i=0;
	
		if(result==1){
			i=wechatSendMessageController.sendMessage(openId, "94bHvwYcW9ITl-FbQnY1BnFFrqorue-RkGdd5hND0bU", "审核通知",
					"http://lzgfgs.com/ycglj/mobile/asset/",
					"尊敬的零售户"+userName+"您的许可证"+license+"审核信息如下:", "审核通过", time, "",
					"", "", "");
		}else{
			i=wechatSendMessageController.sendMessage(openId, "b1ujoxmvkW9112uTDWWy7TZ7cgd4IWI86MaPN55OLqw", 
					//"1vQfPSl4pSvi5UnmmDhVtueutq2R1w7XYRMts294URg",
					"申请驳回通知",
					"http://lzgfgs.com/ycglj/mobile/asset/",
					"许可证审核通知:", userName, time, "未通过",
					content, "", "请修改后，重新提交");
		}

		return i;
		
	}
	
	
	@RequestMapping("sendPhoneMessage")
	public @ResponseBody Integer sendPhoneMessage(@RequestParam String openId,
			@RequestParam String license,@RequestParam String content,
			@RequestParam  Integer result,HttpServletRequest request) {

		WechatSendMessageController wechatSendMessageController=new WechatSendMessageController();
		
		Map search=new HashMap<>();
		
		search.put("[Users].open_id = ",openId);
		
		Map map=userDao.getAllUserJoin(10, 0 , "", "", search);
		
		List list=(List) map.get("data");
		
		String userName = "";
		
		String phone="";

		System.out.println("content="+content);
		
		Date date=new Date();		
		SimpleDateFormat sdf  =   new  SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " ); 
		String time = sdf.format(date);
		
		try {
			
			Users_License_Join users_License_Join = (Users_License_Join) list.get(0);

			userName = users_License_Join.getName();
			
			phone=users_License_Join.getPhone();
			
			time=sdf.format(users_License_Join.getDate());
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		int i=0;
		
		
		if (phone != null && !phone.equals("")) {

			if (result == 1) {

				i = wechatSendMessageController.sendPhoneMessage(phone,
						"尊敬的零售户" + userName + ",您的许可证" + license + "审核通过", userName, openId);

			} else {

				i = wechatSendMessageController.sendPhoneMessage(phone,
						"尊敬的零售户" + userName + ",您的许可证" + license + "审核信息如下:"+content, userName, openId);
			}
			
		}
		
		return i;
		
	}
	
	@RequestMapping("/getAllMessageList")
	public @ResponseBody Map<String, Object> getAllMessageList(Integer limit,Integer page,String sort,String order,
			String search,HttpServletRequest request){
		
		Integer campusId=1;
		
		if(sort!=null&&sort.equals("sendTime")){
			sort="send_time";
		}else{
			sort="send_time";
		}
		
		if(order!=null&&order.equals("asc")){
			order="asc";
		}else if(order!=null&&order.equals("desc")){
			order="desc";
		}else{
			order="desc";
		}
		
		if(search!=null&&!search.equals("")){
			search="%"+search+"%";
		}
		
		int offset=0;
		
		if(page!=null)
			offset=(page-1)*limit;
		
		List list=weiXinService.getAllMessageList(campusId, limit, offset, sort, order, search);
		
		int count=weiXinService.getAllMessageCount(campusId, search);
		
		Map map=new HashMap<>();
		
		map.put("code", "0");
		
		map.put("data", list);
		
		map.put("count", count);
		
		return map;
	}
	
	@RequestMapping("/getPreMessage")
	public @ResponseBody Map getPreMessage(@RequestParam Integer limit,@RequestParam Integer page,
			String sort,String order,
			String search){
		
		if(sort!=null&&!sort.equals("")){
		}else{
			sort="OptDate";
		}
		
		System.out.println("order="+order);
		
		if(order!=null&&order.equals("asc")){
			order="asc";
		}else if(order!=null&&order.equals("desc")){
			order="desc";
		}else{
			order="desc";
		}
		
		Map where=new HashMap<>();
				
		if(search!=null&&!search.equals("")){
			where.put(Singleton.ROOMDATABASE+
					".[dbo].[PreMessage].PhoneWho like ","%"+search+"%");
		}
		
		int offset=0;
		
		if(page!=null)
			offset=(page-1)*limit;
		
		return userDao.findAllPreMessage(limit, offset, sort, order, where);
	}
	
	@RequestMapping(value="/getAllWeixinUser")
	public @ResponseBody
	Map<String, Object> getAllWeixinUser(@RequestParam Integer limit,@RequestParam Integer page,String sort,String order,
			Integer place,String search,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<com.ycglj.manage.model.Users> userlist;
		
		Integer campusId=1;

		if(sort!=null&&sort.equals("subscribeTime")){
			sort="subscribe_time";
		}
		
		if(sort!=null&&sort.equals("totalAmount")){
			sort="total_amount";
		}
		
		if(sort!=null&&sort.equals("defaultAddress")){
			sort="default_address";
		}
		
		/*
		 * 前端的user表与其它表不一样，必须指定查询参数，否则抛出sql异常
		 * 默认按id降序排列
		 */
		if(sort==null){
			sort="id";
			order="desc";
		}
		
		if(search!=null&&!search.trim().equals("")){
			search="%"+search+"%";
		}		
					
		HttpSession session=request.getSession();  //取得session的type变量，判断是否为公众号管理员

		int offset=(page-1)*limit;
		
        userlist=userService.getAllFullUser(campusId,limit,offset,sort,order,place,search);

        Iterator<com.ycglj.manage.model.Users> iterator=userlist.iterator();
        
        int i=0;
        
        while (iterator.hasNext()) {
        	
        	com.ycglj.manage.model.Users users=iterator.next();
        	
        	MyTestUtil.print(users);
        	
        	if (users.getPlace()>0) {
				
        		Map serarchMap=new HashMap<>();
        		
        		serarchMap.put("phone=", users.getPhone());
        		
        		List<Check_Person> list=(List) licenseDAO.getAllCheckPerson(1, 0, "id", "", serarchMap).get("data");
        		
        		try{        			
        			Check_Person check_Person=list.get(0);
        			if(check_Person!=null){
        				users.setName(check_Person.getName());
        				users.setDepartment(check_Person.getDepartment());
        				users.setDuty(check_Person.getDuty());
        				users.setCard_number(check_Person.getCard_number());
        				userlist.set(i, users);
        			}
        			
        		}catch (Exception e) {
					// TODO: handle exception
        			e.printStackTrace();
				}
        		
			}
			
        	i++;
        	
		}
        
		map.put("code", "0");
		
		map.put("data", userlist);
		
		map.put("count", userService.getUserFullCount(campusId,place,search));
		
		return map;
	}
	
	@RequestMapping("/getAllCheckPerson")
	public @ResponseBody Map<String, Object> getAllCheckPerson(@RequestParam Integer limit,
			Integer page,String sort,String order,String search,HttpServletRequest request){
		
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
		
		return licenseDAO.getAllCheckPerson(limit, offset, sort, order, searchMap);
	
	}
	
	
	@RequestMapping(value="/upAtionFormatter")
	public @ResponseBody Integer upAtionFormatter(HttpServletRequest request,@RequestParam String openId,
			@RequestParam Integer place){
		Map<String, Object> map = new HashMap<String, Object>();
		
		Integer campusId=1;
		
		map.put("openId", openId);
		map.put("place", place);
		map.put("campusId", campusId);
		
		return userService.upAtionFormatter(map);
	}
	
		
	@RequestMapping(value="/upAtionTransact")
	public @ResponseBody Integer upAtionTransact(HttpServletRequest request,@RequestParam String openId,
			@RequestParam Integer transact){
		Map<String, Object> map = new HashMap<String, Object>();
		
		Integer campusId=1;
		
		map.put("openId", openId);
		map.put("transact", transact);
		map.put("campusId", campusId);
		
		return userService.upAtionTransact(map);
	}
	
	@RequestMapping(value="/upAtionArea")
	public @ResponseBody Integer upAtionArea(HttpServletRequest request,@RequestParam String openId,
			@RequestParam Integer area,@RequestParam String region){
		Map<String, Object> map = new HashMap<String, Object>();
		
		Integer campusId=1;
		
		map.put("openId", openId);
		map.put("area", area);
		map.put("campusId", campusId);
		
		int i=0;
		
		i=userService.upAtionArea(map);
		
		com.ycglj.manage.model.Users users=userService.getUserByOnlyOpenId(openId);
		MyTestUtil.print(users);
		System.out.println("phone="+users.getPhone());
		Check_Person check_Person=new Check_Person();
		
		check_Person.setDepartment(region);
		
		check_Person.setPhone(users.getPhone());
		
		userDao.updateCheck_Person(check_Person);
		
		return i;
	}
	
	@RequestMapping(value="/upAtionBusiness")
	public @ResponseBody Integer upAtionBusiness(HttpServletRequest request,@RequestParam String openId,
			@RequestParam Integer business){
		Map<String, Object> map = new HashMap<String, Object>();
		
		Integer campusId=1;
		
		map.put("openId", openId);
		map.put("business", business);
		map.put("campusId", campusId);
		
		return userService.upAtionBusiness(map);
	}
	
	@RequestMapping(value="/getAllCrimalRecord")
	public @ResponseBody
	Map<String, Object> getAllCrimalRecordJoin(@RequestParam Integer limit,@RequestParam Integer page,String sort,String order,
			String search,HttpServletRequest request) {

		Map<String, Object> map;


		if(sort!=null&&!sort.equals("")){

		}else{
			sort="criminal_time";
		}
				
		if(order!=null&&!order.equals("")){

		}else{
			order="desc";
		}
		
		Map searchMap=new HashMap<>();
		
		if(search!=null&&!search.trim().equals("")){
			searchMap.put("[Crimal_Record].license like ","%"+search+"%");
			searchMap.put("[Crimal_Record].phone like ","%"+search+"%");
			searchMap.put("[Users].name like ","%"+search+"%");
		}		
					
		HttpSession session=request.getSession();  //取得session的type变量，判断是否为公众号管理员

		int offset=(page-1)*limit;
		
        map=licenseDAO.getAllCrimalRecordJoin(limit, offset, sort, order,searchMap);

        List list=(List) map.get("data");
        
        Iterator iterator=list.iterator();
        
        int i=0;
        
        while (iterator.hasNext()) {
        	Crimal_Record_Join crimal_Record_Join=(Crimal_Record_Join) iterator.next();
        	MyTestUtil.print(crimal_Record_Join);
        	if(crimal_Record_Join.getName()==null||crimal_Record_Join.getName().equals("")){
        		Not_License not_License=licenseDAO.getNotLicenseById(crimal_Record_Join.getLicense());
				if (not_License != null) {
					crimal_Record_Join.setName(not_License.getName());
					crimal_Record_Join.setPhone(not_License.getPhone());
					crimal_Record_Join.setAddress(not_License.getAddress());
					crimal_Record_Join.setLicense("无许可证");
					list.set(i, crimal_Record_Join);
				}
        	}
        	i++;
        	System.out.println("i="+i);
		}
        
        map.put("data", list);
        
        if(map==null){
        	map = new HashMap<String, Object>();
        }else{
        	map.put("code", "0");
        }
		return map;
	}
	
	@RequestMapping(value="inputImage")
	public @ResponseBody Integer uploadFilesSpecifyPath(@RequestParam("file") MultipartFile[] file,@RequestParam String openId,@RequestParam String data_type,String license,HttpServletRequest request,HttpServletResponse response) throws Exception {  
		 long startTime=System.currentTimeMillis();   //获取开始时间  
	      /*  if(!file.isEmpty()){  
	            try {  
	                //定义输出流 将文件保存在D盘    file.getOriginalFilename()为获得文件的名字   
	                FileOutputStream os = new FileOutputStream("C:\\Users\\WangJing\\Desktop\\bb\\2\\"+file.getOriginalFilename());  
	                InputStream in = file.getInputStream();  
	                int b = 0;  
	                while((b=in.read())!=-1){ //读取文件   
	                    os.write(b);  
	                }  
	                os.flush(); //关闭流   
	                in.close();  
	                os.close();  
	                  
	            } catch (FileNotFoundException e) {  
	                e.printStackTrace();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }  */
		 
		 
		 if(file!=null&&file.length>0){
	            //组合image名称，“;隔开”
	            List<String> fileName =new ArrayList<String>();
	            String upimage = null;
                System.out.println("length="+file.length);
	            try {
	                for (int i = 0; i < file.length; i++) {
	                    if (!file[i].isEmpty()) {

	                        //上传文件，随机名称，";"分号隔开
	                       // fileName.add(FileUtil.uploadImage(request, "/upload/"+"/", file[i], true));
	                    	upimage=uploadImage(request, "/upload/", file[i],openId,data_type,license,true);
	                    	fileName.add(upimage);
	                    }
	                }

	                //上传成功
	                if(fileName!=null&&fileName.size()>0&&!upimage.equals("error")){
	                	System.out.println(fileName);
	                    System.out.println("上传成功！");
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
	    
		 
		 
	        long endTime=System.currentTimeMillis(); //获取结束时间  
	        System.out.println("上传文件共使用时间："+(endTime-startTime));  
	        
	        return 0;
 
	} 
	
	
	/**
     * 上传图片
     *  原名称
     * @param request 请求
     * @param path_deposit 存放位置(路径)
     * @param file 文件
     * @param isRandomName 是否随机名称
     * @return 完整文件路径
     */
    public  String uploadImage(HttpServletRequest request,String path_deposit,MultipartFile file,String openId,String data_type,String license,boolean isRandomName) {
        
    	System.out.println("license="+license);
    	
    	//获得物理路径webapp所在路径  
        String pathRoot = System.getProperty("user.home");
       //存放图片文件的路径
        String path="/mobile/photo/";  
        String filePath=pathRoot+path;  
    	
        //判断是否存在目录
        File savePath = new File(pathRoot+Singleton.filePath);//创建新文件  
        System.out.println("filePath="+Singleton.filePath);
        if (!savePath.exists()) {   
            savePath.mkdir();   
        }  
        
    	//上传
        try {
            String[] typeImg={"gif","png","jpg","jpeg","ico"};

            if(file!=null){
                String origName=file.getOriginalFilename();// 文件原名称
                System.out.println("上传的文件原名称:"+origName);
                // 判断文件类型
                String type=origName.indexOf(".")!=-1?origName.substring(origName.lastIndexOf(".")+1, origName.length()):null;
                    boolean booIsType=false;
                    for (int i = 0; i < typeImg.length; i++) {
                        if (typeImg[i].equals(type.toLowerCase())) {
                            booIsType=true;
                        }
                    }
                    //类型正确
                    if (booIsType) {
                    	//组合名称
                        String fileSrc=""; 
                        //是否随机名称
                        String uuid=UUID.randomUUID().toString();
                        if(isRandomName){
                            origName=uuid+origName.substring(origName.lastIndexOf("."));
                        }
                        //判断是否存在目录
                        File targetFile=new File(savePath,origName);

                        if(!targetFile.exists()){
                            targetFile.mkdirs();//创建目录
                        }
                        //上传
                        try{
                        	file.transferTo(targetFile);

                             User_Data user_Data=new User_Data();
                             user_Data.setOpen_id(openId);
                             user_Data.setUuid(uuid.toString());
                             user_Data.setData_type(data_type);
                             user_Data.setType(type);
                             user_Data.setCurrently(1);
                             user_Data.setAffirm(1);
                             user_Data.setDate(new Date());
                             user_Data.setUri(origName);
                             if(license!=null&&!license.equals(""))
                            	 user_Data.setLicense(license);
                             userDao.insertUserData2(user_Data);

                        }catch (Exception e) {
							// TODO: handle exception
                        	e.printStackTrace();
						}
                        //完整路径
                        fileSrc=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+path_deposit+origName;
                        
                        CopyFile.set(Singleton.ROOMINFOIMGPATH2, savePath+"\\"+origName, origName);
                        
                        System.out.println("图片上传成功:"+path+origName);                       
                        System.out.println("图片上传成功2:"+filePath+origName);
                        System.out.println("图片上传成功:"+fileSrc);
                        return fileSrc;
                    }
                }
            return "error";
        }catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
	
    
    @RequestMapping("/updateCheckPerson")
	public @ResponseBody Integer updateCheckPerson(@RequestParam String phone,
			String name,String unit,String department,String duty,String card_number,
			HttpServletRequest request){
		
		   Check_Person check_Person=new Check_Person();
		   
		   check_Person.setPhone(phone);
		   check_Person.setName(name);
		   check_Person.setUnit(unit);
		   check_Person.setDepartment(department);
		   check_Person.setDuty(duty);
		   check_Person.setCard_number(card_number);
		   
		   return userDao.updateCheck_Person(check_Person);
	
	}

    
}
