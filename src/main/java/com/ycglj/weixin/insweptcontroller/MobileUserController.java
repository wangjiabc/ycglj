package com.ycglj.weixin.insweptcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.ycglj.manage.dao.UserDAO;
import com.ycglj.manage.model.Users;
import com.ycglj.manage.service.UserService;
import com.ycglj.sqlserver.context.Connect;

@Controller
@RequestMapping("/mobile/user")
public class MobileUserController {
	
	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	ApplicationContext applicationContext=new Connect().get();
	
	UserDAO userDao=(UserDAO) applicationContext.getBean("userdao");
	
	@RequestMapping(value="/getUserByPhone")
	public @ResponseBody
	Map<String, Object> getUserByphone(@RequestParam Integer limit,@RequestParam Integer offset,
			String sort,String order,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Users> userlist;
		String type,campusAdmin;
		Integer campusId=0;		
		
		/*
		 * 前端的user表与其它表不一样，必须指定查询参数，否则抛出sql异常
		 * 默认按id降序排列
		 */
		if(sort==null){
			sort="id";
			order="desc";
		}
        
		String search;
		
        userlist=userService.getUserByPhone(limit, offset, sort, order);

		JSONArray  json=JSONArray.parseArray(JSON.toJSONStringWithDateFormat(userlist,"yyyy-MM-dd"));		
		map.put("rows", json);
		return map;
	}
	
	
	@RequestMapping(value="updateAffire")
	public @ResponseBody Integer uploadFilesSpecifyPath(@RequestParam String arrays,HttpServletRequest request,HttpServletResponse response) throws Exception {  

		HttpSession session = request.getSession();
		
		String openId=session.getAttribute("openId").toString();
		
		long startTime=System.currentTimeMillis();   //获取开始时间  
		 
		 JSONArray jsonArray = JSONArray.parseArray(arrays);

		 if(jsonArray!=null&&jsonArray.size()>0){
	            //组合image名称，“;隔开”
	            int up = 0;
                System.out.println("length="+jsonArray.size());
	            try {
	            	
	            	List<String> list=new ArrayList<>();
	            	
	                for (int i = 0; i < jsonArray.size(); i++) {
	                    String uuid=jsonArray.getString(i);
	                    
	                    list.add(uuid);
	                }

	                up=userDao.updateUserDataAffirm(openId,list);
	                
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
	    
		 
		 
	        long endTime=System.currentTimeMillis(); //获取结束时间  
	        System.out.println("上传文件共使用时间："+(endTime-startTime));  
	        
	        return 0;
 
	} 
	
	
}
