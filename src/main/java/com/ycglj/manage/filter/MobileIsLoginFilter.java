package com.ycglj.manage.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ycglj.manage.mapper.UsersMapper;
import com.ycglj.manage.model.Users;
import com.ycglj.manage.service.UserService;

public class MobileIsLoginFilter implements Filter{
	private UsersMapper usersMapper;
	
	 public FilterConfig config3=null;
	    @Override  
	    public void destroy() {   
	  
	    }   
	  
	    @Override  
	    public void doFilter(ServletRequest request, ServletResponse response,   
	            FilterChain chain) throws IOException, ServletException {   
	    	System.out.println("dofilter");
	    	  HttpServletRequest hrequest = (HttpServletRequest)request;
		        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper((HttpServletResponse) response);
		        
		        String loginStrings = config3.getInitParameter("loginStrings");        
		        String includeStrings = config3.getInitParameter("includeStrings");    
		        String redirectPath = hrequest.getContextPath() + config3.getInitParameter("redirectPath");
		        String mobileLoginPath = hrequest.getContextPath() + config3.getInitParameter("mobileLoginPath");
		        String disabletestfilter = config3.getInitParameter("disabletestfilter");
		        

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
				WebApplicationContext wac = WebApplicationContextUtils    //controller以外的包初始化类
						.getRequiredWebApplicationContext(httpRequest.getSession()
								.getServletContext());
		        
				usersMapper=wac.getBean(UsersMapper.class);
				
		        if (openId==null) {
		        //	HttpSession session = hrequest.getSession();
		        //	  session.invalidate();       
		        	System.out.println("mobileIsLoginFilter openid= null");
		        	wrapper.sendRedirect(mobileLoginPath);
		            return;
		        }else {
		        	System.out.println("mobileIsLoginFilter openid="+openId);
		        	Users users=usersMapper.getUserByOnlyOpenId(openId);
		        	System.out.println("mobileIsLoginFilter openId ="+openId);
		           if(users.getPlace()==2||users.getPlace()>4){  //通过place判断用户的访问权限，数字越大权限越高
		             chain.doFilter(request, response);
		            }else{
		        	 wrapper.sendRedirect(redirectPath);
		        	}
		            return;
		        }   
	    }   
	  
	    @Override  
	    public void init(FilterConfig filterConfig) throws ServletException {   
	    	config3 = filterConfig;
	    }
}
