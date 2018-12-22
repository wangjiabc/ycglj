package com.ycglj.manage.controller;

import java.io.File;
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
import com.ycglj.manage.daoModel.Order_User;
import com.ycglj.manage.daoModel.User_Data;
import com.ycglj.manage.daoModel.User_License;
import com.ycglj.manage.daoModel.Users;
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
			
			return userDao.getAllUserJoin(limit, offset, sort,order, searchMap);
				
	}
	
	
    @RequestMapping("insertUser")
	public @ResponseBody Integer insertUser(@RequestParam String license,@RequestParam String name,String idNumber,
			@RequestParam String phone,HttpServletRequest request) {
    	
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

    	userDao.insertUser(users);
    	
    	return userDao.insertUserLicense(user_License);
    	
    }
	
	
	@RequestMapping("updateUser")
	public @ResponseBody Integer updateUser(@RequestParam String openId,String name,String idNumber,
			String phone,HttpServletRequest request) {
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
		
		return userDao.updateUserPhone(users);
		
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
			i=wechatSendMessageController.sendMessage(openId, //"b1ujoxmvkW9112uTDWWy7TZ7cgd4IWI86MaPN55OLqw", 
					"1vQfPSl4pSvi5UnmmDhVtueutq2R1w7XYRMts294URg",
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
			@RequestParam Integer time,String sort,String order,
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
		
		Calendar cal = Calendar.getInstance();  
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
		
        if(time==2){
        	cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        }
        
        String startTime = null;
		
		startTime=sdf.format(cal.getTime());
        
		cal = Calendar.getInstance();  

        String endTime = null;
		
		endTime=sdf.format(cal.getTime());
		
		System.out.println("startTime="+startTime+"   endTime="+endTime);
		
		Map where=new HashMap<>();
		
		if(time==1){
			where.put("convert(varchar(11),"+Singleton.ROOMDATABASE+
					".[dbo].[PreMessage].OptDate ,120 ) = ", endTime);
		}else if(time>1){
			where.put("convert(varchar(11),"+Singleton.ROOMDATABASE+
					".[dbo].[PreMessage].OptDate ,120 ) >= ", startTime);
			where.put("convert(varchar(11),"+Singleton.ROOMDATABASE+
					".[dbo].[PreMessage].OptDate ,120 ) <= ", endTime);
		}
		
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

		map.put("code", "0");
		
		map.put("data", userlist);
		
		map.put("count", userService.getUserFullCount(campusId,search));
		
		return map;
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
	
    
    

    
}
