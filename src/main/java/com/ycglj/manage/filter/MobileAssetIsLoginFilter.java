package com.ycglj.manage.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.ycglj.manage.dao.UserDAO;
import com.ycglj.manage.daoModelJoin.Users_License_Join;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ycglj.manage.mapper.UsersMapper;
import com.ycglj.manage.tools.MyTestUtil;
import com.ycglj.sqlserver.context.Connect;

public class MobileAssetIsLoginFilter implements Filter{
	
	ApplicationContext applicationContext=new Connect().get();
	
	private UsersMapper usersMapper;
	
	UserDAO userDao=(UserDAO) applicationContext.getBean("userdao");
	
	 public FilterConfig configAsset=null;
	    @Override  
	    public void destroy() {   
	  
	    }   
	  
	    @Override  
	    public void doFilter(ServletRequest request, ServletResponse response,   
	            FilterChain chain) throws IOException, ServletException {   
	    	System.out.println("dofilter");
	    	  HttpServletRequest hrequest = (HttpServletRequest)request;
		        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper((HttpServletResponse) response);
		        
		        String loginStrings = configAsset.getInitParameter("loginStrings");        
		        String includeStrings = configAsset.getInitParameter("includeStrings");    
		        String redirectPath = hrequest.getContextPath() + configAsset.getInitParameter("redirectPath");
		        String mobileLoginPath = hrequest.getContextPath() + configAsset.getInitParameter("mobileLoginPath");
		        String settingPath = hrequest.getContextPath() + configAsset.getInitParameter("settingPath");
		        String disabletestfilter = configAsset.getInitParameter("disabletestfilter");
		        

		        String[] loginList = loginStrings.split(";");
		        String[] includeList = includeStrings.split(";");
		        
		        if (!IsLoginFilter.isContains(hrequest.getRequestURI(), includeList)) {
		            chain.doFilter(request, response);
		            return;
		        }
		        
		        if (IsLoginFilter.isContains(hrequest.getRequestURI(), loginList)) {//
		            chain.doFilter(request, response);
		            return;
		        }
		        
		        String openId=( String ) hrequest.getSession().getAttribute("openId");
		        
		        HttpServletRequest httpRequest = (HttpServletRequest) request;
				WebApplicationContext wac = WebApplicationContextUtils    //controller浠ュ鐨勫寘鍒濆鍖栫被
						.getRequiredWebApplicationContext(httpRequest.getSession()
								.getServletContext());
		        
				usersMapper=wac.getBean(UsersMapper.class);
				
		        if (openId==null) {       
		        	MyTestUtil.print(hrequest.getSession());
		        	System.out.println("MobileAssetIsLoginFilter openid= null");
		        	wrapper.sendRedirect(mobileLoginPath);
		            return;
		        }else {

		        	Map searchMap=new HashMap<>();
		    		
		    		searchMap.put("[User_License].open_id = ", openId);
		    		
		    		List list=(List) userDao.getAllUserJoin(1, 0, "", "", searchMap).get("data");

		        	try {

		        			Users_License_Join users_License_Join= (Users_License_Join) list.get(0);
		        			
		        			if(!(users_License_Join.getName()==null)&&!users_License_Join.getName().equals("")&&
		        					!(users_License_Join.getPhone()==null)&&!users_License_Join.getPhone().equals("")
		        					&&!(users_License_Join.getLicense()==null)&&!(users_License_Join.getLicense().equals(""))){
		        				chain.doFilter(request, response);
		        			}else{		        			
		        				wrapper.sendRedirect(settingPath);
		        			}
		        			
		        		}catch (Exception e) {
							// TODO: handle exception
		        			e.printStackTrace();
		        			wrapper.sendRedirect(settingPath);
				            return;
						}
		        		
					}
	
		            return;
		        }   
	     
	  
	    @Override  
	    public void init(FilterConfig filterConfig) throws ServletException {   
	    	configAsset = filterConfig;
	    }
}
