package com.ycglj.manage.controller;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.ycglj.manage.dao.LicenseDAO;
import com.ycglj.manage.daoModel.Position;
import com.ycglj.manage.model.Users;
import com.ycglj.manage.service.UserService;
import com.ycglj.manage.tools.MyTestUtil;
import com.ycglj.sqlserver.context.Connect;



@Controller
@RequestMapping("/baiduMap")
public class BaiduMapController {

	ApplicationContext applicationContext=new Connect().get();
	
	LicenseDAO licenseDAO=(LicenseDAO) applicationContext.getBean("licensedao");
	
	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping("/getAllAssetPosition")
	public @ResponseBody Map getAllAssetPosition(){
		
		Map map=licenseDAO.getAllLicensePosition();
		
		MyTestUtil.print(map);
		
		return map;
		
	}
	
	@RequestMapping("/getAllLicensePositionJoin")
	public @ResponseBody Map getAllLicensePositionJoin(@RequestParam Integer type,String name,Long startTime,Long endTime,
			String yit,String any,HttpServletRequest request){
				
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
		
		String startDate = null,endDate = null;
		
		String[] yitStrings = null,anyStrings = null;
		
		com.ycglj.manage.model.Users users=new Users();
		
		if(type==0){
			
			HttpSession session = request.getSession();
			
			String openId=session.getAttribute("openId").toString();
			
			users=userService.getUserByOnlyOpenId(openId);
			
		}
		
		if(startTime!=null){			
			startDate=sdf.format(startTime);
		}
		
		if(endTime!=null){
			endDate=sdf.format(endTime);
		}
		
		System.out.println("startDate="+startDate+"   endDate="+endDate);
		
		if (yit != null && !yit.equals("")) {

			yitStrings = yit.split(",");
			
			MyTestUtil.print(yitStrings);

		}

		if (any != null && !any.equals("")) {
			
			anyStrings = any.split(",");
			
			MyTestUtil.print(anyStrings);
		}
		
		System.out.println("yitStrings="+yitStrings+"   anyStrings="+anyStrings);
		
				
		Map map=licenseDAO.getAllLicensePositionJoin(name, startDate, endDate, yitStrings, anyStrings,type,users);
		
		MyTestUtil.print(map);
		
		return map;
		
	}
	
	@RequestMapping("/getPosition")
	public @ResponseBody JSONObject getPosition() {
		JSONObject jsonObject=new JSONObject();
		Position position=new Position();
		position.setLimit(10);
		position.setOffset(0);		
		position.setSort("date");
		position.setOrder("desc");
		position.setNotIn("id");
        		
		try{
			position=(Position) licenseDAO.findPosition(position).get(0);
			jsonObject.put("lat", position.getLat());
			jsonObject.put("lng", position.getLng());
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return jsonObject;
		
	}
	
	@RequestMapping("/location")
	public @ResponseBody JSONObject baiduSwitch(HttpServletRequest request){
		JSONObject jsonObject=null;
		String requestUrl = "http://api.map.baidu.com/location/ip?ak=pQFgFpS0VnMXwCRN6cTc1jDOcBVi3XoD&coor=bd09ll";
		
		HttpGet g = new HttpGet(requestUrl);
    	RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
    	CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
    	CloseableHttpResponse r;
    	String content = null;
		try {
			r = httpClient.execute(g);
			content = EntityUtils.toString(r.getEntity());
	        r.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		jsonObject=JSONObject.parseObject(content);
  		
		return jsonObject;
		
	}
	
	@RequestMapping("getLicenseGUIDByPosition")
	public @ResponseBody Map getLicenseGUIDByPosition(@RequestParam Double lng,
			@RequestParam Double lat ,HttpServletRequest request){
		
		Map map=licenseDAO.findRoomInfoPositionByLatLng(lat, lng);
		
		return map;
        
	}
	
	
	@RequestMapping("getBusinessStatistics")
	public @ResponseBody Map getBusinessStatistics(Integer type,HttpServletRequest request){
		
        if(type==null){
        	type=0;
        }
		
		Calendar cal = Calendar.getInstance();  
		cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0); 
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		cal.roll(Calendar.DATE, -1);
		
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
		
		String endTime=null;
		
		long endTimeLong=cal.getTime().getTime();
        endTime=sdf.format(cal.getTime());
		
        List month=new ArrayList<>();

        List options=new ArrayList<>();
        
        int i=0;
        
        cal.set(cal.get(Calendar.YEAR), 0, cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));

        long currentTime=cal.getTime().getTime();
        
        while(currentTime<endTimeLong&&i<12){
        	cal.set(cal.get(Calendar.YEAR), i, cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        	cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            currentTime=cal.getTime().getTime();
        	String m=(i+1)+"月";
        	month.add(m);
        	System.out.println("i="+i);
        	System.out.println(sdf.format(currentTime)+"  "+sdf.format(endTimeLong));
        	String currentEndTime=sdf.format(currentTime);
        	Map businessMap=licenseDAO.getBusinessStateByDate(currentEndTime);
        	Map title=new HashMap<>();
        	title.put("text", currentEndTime+"业态饼状图");
        	List series=new ArrayList<>();
        	List seriesData=(List) businessMap.get("data");
        	Map data=new HashMap<>();
        	data.put("data", seriesData);
        	series.add(data);
        	Map optionMap=new HashMap<>();
        	optionMap.put("title", title);
        	optionMap.put("series", series);
        	options.add(optionMap);
        	i++;
        }
        
        Map map=new HashMap<>();
        
        Map baseOption=new HashMap<>();
        
        Map timeline=new HashMap<>();
        
        timeline.put("axisType","category");
        timeline.put("data", month);
        timeline.put("left", 0);
        timeline.put("right", 0);
        timeline.put("show", true);
        
        if(type!=1){
        	timeline.put("bottom", 60);
        }else{
        	timeline.put("autoPlay", true);
            timeline.put("playInterval", 2000);
        }
        
        List series=new ArrayList<>();
        
        Map seriesMap=new HashMap<>();
        
        seriesMap.put("name", "业态统计");
        seriesMap.put("type", "pie");
        seriesMap.put("radius", "55%");
        List center=new ArrayList<>();
        center.add("50%");
        center.add("50%");
        seriesMap.put("center", center);
        
        if(type==1){
        	Map seriesMap2=new HashMap<>();
        	seriesMap2.put("name", "业态");
        	seriesMap2.put("type", "bar");
        	series.add(seriesMap2);
        }else{
        	series.add(seriesMap);
        }
        
        Map legend=new HashMap<>();
        
        legend.put("x", "left");
        legend.put("top", 30);
        List legendData=licenseDAO.getBusinessStateType();
        legend.put("data", legendData);
        
        Map tooltip=new HashMap<>();
        
        tooltip.put("trigger", "item");
        tooltip.put("formatter", "");
        
        baseOption.put("timeline", timeline);
        baseOption.put("series", series);        
        baseOption.put("tooltip", tooltip);
        if(type==1){
        	Map grid=new HashMap<>();
        	grid.put("containLabel", true);
        	baseOption.put("grid", grid);
        	List yAxis=new ArrayList<>();
        	Map yMap=new HashMap<>();
        	yMap.put("name", "单位/条");
        	yAxis.add(yMap);
        	List xAxis=new ArrayList<>();
        	Map xMap=new HashMap<>();
        	xMap.put("type", "category");
        	xMap.put("data", legendData);
        	xAxis.add(xMap);
        	baseOption.put("yAxis", yAxis);
        	baseOption.put("xAxis", xAxis);
        }else{
        	baseOption.put("legend", legend);
        	baseOption.put("calculable", true);
        }
        
        map.put("baseOption", baseOption);
        map.put("options", options);
        
        return map;
        
	}
	
	@RequestMapping("getLawCase")
	public @ResponseBody Map getLawCase(Integer type,HttpServletRequest request){
		
        if(type==null){
        	type=0;
        }
		
		Calendar cal = Calendar.getInstance();  
		cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0); 
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		cal.roll(Calendar.DATE, -1);
		
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
		
		String endTime=null;
		
		long endTimeLong=cal.getTime().getTime();
        endTime=sdf.format(cal.getTime());
		
        List month=new ArrayList<>();

        List options=new ArrayList<>();
        
        int i=0;
        
        cal.set(cal.get(Calendar.YEAR), 0, cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));

        long currentTime=cal.getTime().getTime();
        
        while(currentTime<endTimeLong&&i<12){
        	cal.set(cal.get(Calendar.YEAR), i, cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        	cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            currentTime=cal.getTime().getTime();
        	String m=(i+1)+"月";
        	month.add(m);
        	System.out.println("i="+i);
        	System.out.println(sdf.format(currentTime)+"  "+sdf.format(endTimeLong));
        	String currentStartTime=sdf.format(currentTime);
        	cal.set(cal.get(Calendar.YEAR), i+1, cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        	cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            long currentETime=cal.getTime().getTime();
            String currentEndTime=sdf.format(currentETime);
        	Map businessMap=licenseDAO.getLawCaseByDate(currentStartTime, currentEndTime);
        	Map title=new HashMap<>();
        	title.put("text", currentStartTime+"案由饼状图");
        	List series=new ArrayList<>();
        	List seriesData=(List) businessMap.get("data");
        	Map data=new HashMap<>();
        	data.put("data", seriesData);
        	series.add(data);
        	Map optionMap=new HashMap<>();
        	optionMap.put("title", title);
        	optionMap.put("series", series);
        	options.add(optionMap);
        	i++;
        }
        
        Map map=new HashMap<>();
        
        Map baseOption=new HashMap<>();
        
        Map timeline=new HashMap<>();
        
        timeline.put("axisType","category");
        timeline.put("data", month);
        timeline.put("left", 0);
        timeline.put("right", 0);
        timeline.put("show", true);

        if(type!=1){
        	timeline.put("bottom", 60);
        }else{
            timeline.put("autoPlay", true);
            timeline.put("playInterval", 2000);
        }
        
        List series=new ArrayList<>();
        
        Map seriesMap=new HashMap<>();
        
        seriesMap.put("name", "案由统计");
        seriesMap.put("type", "pie");
        seriesMap.put("radius", "55%");
        List center=new ArrayList<>();
        center.add("50%");
        center.add("50%");
        seriesMap.put("center", center);
        
        if(type==1){
        	Map seriesMap2=new HashMap<>();
        	seriesMap2.put("name", "案由");
        	seriesMap2.put("type", "bar");
        	series.add(seriesMap2);
        }else{
        	series.add(seriesMap);
        }
        
        Map legend=new HashMap<>();
        
        legend.put("x", "left");
        legend.put("top", 30);
        List legendData=licenseDAO.getLawCaseType();
        legend.put("data", legendData);
        
        Map tooltip=new HashMap<>();
        
        tooltip.put("trigger", "item");
        tooltip.put("formatter", "");
        
        baseOption.put("timeline", timeline);
        baseOption.put("series", series);        
        baseOption.put("tooltip", tooltip);
        if(type==1){
        	Map grid=new HashMap<>();
        	grid.put("containLabel", true);
        	baseOption.put("grid", grid);
        	List yAxis=new ArrayList<>();
        	Map yMap=new HashMap<>();
        	yMap.put("name", "单位/条");
        	yAxis.add(yMap);
        	List xAxis=new ArrayList<>();
        	Map xMap=new HashMap<>();
        	xMap.put("type", "category");
        	xMap.put("data", legendData);
        	xAxis.add(xMap);
        	baseOption.put("yAxis", yAxis);
        	baseOption.put("xAxis", xAxis);
        }else{
        	baseOption.put("legend", legend);
        	baseOption.put("calculable", true);
        }
        
        map.put("baseOption", baseOption);
        map.put("options", options);
        
        return map;
        
	}

	
	@RequestMapping("getThreeLawCase")
	public @ResponseBody Map getThreeLawCase(Integer type,HttpServletRequest request){
		
        if(type==null){
        	type=0;
        }
		
		Calendar cal = Calendar.getInstance();  
		cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0); 
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		cal.roll(Calendar.DATE, -1);
		
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
		
		String endTime=null;
		
		long endTimeLong=cal.getTime().getTime();
        endTime=sdf.format(cal.getTime());
		
        List month=new ArrayList<>();

        List options=new ArrayList<>();
        
        int i=0;
        
        cal.set(cal.get(Calendar.YEAR), 0, cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));

        long currentTime=cal.getTime().getTime();
        
        while(currentTime<endTimeLong&&i<12){
        	cal.set(cal.get(Calendar.YEAR), i, cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        	cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            currentTime=cal.getTime().getTime();
        	String m=(i+1)+"月";
        	month.add(m);
        	System.out.println("i="+i);
        	System.out.println(sdf.format(currentTime)+"  "+sdf.format(endTimeLong));
        	String currentStartTime=sdf.format(currentTime);
        	cal.set(cal.get(Calendar.YEAR), i+1, cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        	cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            long currentETime=cal.getTime().getTime();
            String currentEndTime=sdf.format(currentETime);
        	Map businessMap=licenseDAO.getThreeLawCaseByDate(currentStartTime, currentEndTime);
        	Map title=new HashMap<>();
        	title.put("text", currentStartTime+"案由饼状图");
        	List series=new ArrayList<>();
        	List seriesData=(List) businessMap.get("data");
        	Map data=new HashMap<>();
        	data.put("data", seriesData);
        	series.add(data);
        	Map optionMap=new HashMap<>();
        	optionMap.put("title", title);
        	optionMap.put("series", series);
        	options.add(optionMap);
        	i++;
        }
        
        Map map=new HashMap<>();
        
        Map baseOption=new HashMap<>();
        
        Map timeline=new HashMap<>();
        
        timeline.put("axisType","category");
        timeline.put("data", month);
        timeline.put("left", 0);
        timeline.put("right", 0);
        timeline.put("show", true);

        if(type!=1){
        	timeline.put("bottom", 60);
        }else{
            timeline.put("autoPlay", true);
            timeline.put("playInterval", 2000);
        }
        
        List series=new ArrayList<>();
        
        Map seriesMap=new HashMap<>();
        
        seriesMap.put("name", "案由统计");
        seriesMap.put("type", "pie");
        seriesMap.put("radius", "55%");
        List center=new ArrayList<>();
        center.add("50%");
        center.add("50%");
        seriesMap.put("center", center);
        
        if(type==1){
        	Map seriesMap2=new HashMap<>();
        	seriesMap2.put("name", "案由");
        	seriesMap2.put("type", "bar");
        	series.add(seriesMap2);
        }else{
        	series.add(seriesMap);
        }
        
        Map legend=new HashMap<>();
        
        legend.put("x", "left");
        legend.put("top", 30);
        List legendData=licenseDAO.getThreeLawCaseType();
        legend.put("data", legendData);
        
        Map tooltip=new HashMap<>();
        
        tooltip.put("trigger", "item");
        tooltip.put("formatter", "");
        
        baseOption.put("timeline", timeline);
        baseOption.put("series", series);        
        baseOption.put("tooltip", tooltip);
        if(type==1){
        	Map grid=new HashMap<>();
        	grid.put("containLabel", true);
        	baseOption.put("grid", grid);
        	List yAxis=new ArrayList<>();
        	Map yMap=new HashMap<>();
        	yMap.put("name", "单位/条");
        	yAxis.add(yMap);
        	List xAxis=new ArrayList<>();
        	Map xMap=new HashMap<>();
        	xMap.put("type", "category");
        	xMap.put("data", legendData);
        	xAxis.add(xMap);
        	baseOption.put("yAxis", yAxis);
        	baseOption.put("xAxis", xAxis);
        }else{
        	baseOption.put("legend", legend);
        	baseOption.put("calculable", true);
        }
        
        map.put("baseOption", baseOption);
        map.put("options", options);
        
        return map;
        
	}
	
}
