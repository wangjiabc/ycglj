package com.ycglj.manage.face;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;

import com.ycglj.manage.dao.UserDAO;
import com.ycglj.manage.daoModel.User_Data;
import com.ycglj.manage.singleton.Singleton;
import com.ycglj.sqlserver.context.Connect;

public class RegisterCompare {

	ApplicationContext applicationContext=new Connect().get();
	
	UserDAO userDao=(UserDAO) applicationContext.getBean("userdao");
	
	public Long match(String openId,HttpServletRequest request) {
		
		String imgPath=request.getSession().getServletContext().getRealPath(Singleton.filePath);
		
		String pathRoot = System.getProperty("user.home");
		
		String filePath=pathRoot+Singleton.filePath;
		
		User_Data user_Data=userDao.getUserDataByTime(openId, "self");
		
		User_Data user_Data2=userDao.getUserDataByTime(openId, "identity");
		
		String img1=filePath+"\\"+user_Data.getUri();
		
		String img2=filePath+"\\"+user_Data2.getUri();
		
		JSONObject jsonObject=ComparePhoto.match(img1, img2);

		long score=0;
		
		try {
			System.out.print(jsonObject.toString(2));
			JSONObject result=jsonObject.getJSONObject("result");
			score=result.getLong("score");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return score;
		
	}
	
}
