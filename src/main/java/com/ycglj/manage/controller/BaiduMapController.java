package com.ycglj.manage.controller;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
	
}
