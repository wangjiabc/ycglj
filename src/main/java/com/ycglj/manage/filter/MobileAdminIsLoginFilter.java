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
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ycglj.manage.dao.UserDAO;
import com.ycglj.manage.daoModel.WeiXin_User;
import com.ycglj.manage.mapper.UsersMapper;
import com.ycglj.manage.model.Users;
import com.ycglj.sqlserver.context.Connect;   
  
public class MobileAdminIsLoginFilter implements Filter {   
	private UsersMapper usersMapper;
	
	ApplicationContext applicationContext=new Connect().get();
	
	UserDAO userDao=(UserDAO) applicationContext.getBean("userdao");
	
    public FilterConfig config2=null;
    @Override  
    public void destroy() {   
  
    }   
  
    @Override  
    public void doFilter(ServletRequest request, ServletResponse response,   
            FilterChain chain) throws IOException, ServletException {   
    	  HttpServletRequest hrequest = (HttpServletRequest)request;
	        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper((HttpServletResponse) response);
	        
	        String loginStrings = config2.getInitParameter("loginStrings");        
	        String includeStrings = config2.getInitParameter("includeStrings");    
	        String redirectPath = hrequest.getContextPath() + config2.getInitParameter("redirectPath");
	        String mobileLoginPath = hrequest.getContextPath() + config2.getInitParameter("mobileLoginPath");
	        String disabletestfilter = config2.getInitParameter("disabletestfilter");
	        String settingPath = hrequest.getContextPath() + "/mobile/1/userSetting.html";

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
	        //	session.invalidate();         //如果微信登录后就清除session再登陆管理员页面
	        	wrapper.sendRedirect(mobileLoginPath);
	            return;
	        }else {
	        	System.out.println("mobileAdminIsLoginFilter openid="+openId);
	        	Users users=usersMapper.getUserByOnlyOpenId(openId);
	        	System.out.println("mobileAdminIsLoginFilter openId ="+openId);
	        	
	        	Map searchMap=new HashMap<>();
	    		
	        	WeiXin_User weiXin_User=new WeiXin_User();
	        	
	        	weiXin_User.setLimit(1);
	        	weiXin_User.setOffset(0);
	        	weiXin_User.setNotIn("open_id");
	        	
	    		String[] where={"open_id = ", openId};
	    		
	    		weiXin_User.setWhere(where);
	    		
	    		WeiXin_User weiXin_User2=userDao.getWeiXinUser(weiXin_User);

	    		System.out.println("settingPath="+settingPath);
	    		
	        	try {

	        			if(!(weiXin_User2.getPhone()==null)&&!weiXin_User2.getPhone().equals("")&&
	        					!(weiXin_User2.getHeadship()==null)&&!weiXin_User2.getHeadship().equals("")){
	        		           if(users.getPlace()>0){  //通过place判断用户的访问权限，数字越大权限越高
	        			             chain.doFilter(request, response);
	        			            }else{
	        			        	 wrapper.sendRedirect(redirectPath);
	        			        	}
	        			}else{		        			
	        				wrapper.sendRedirect(settingPath);
	        			}
	        			
	        		}catch (Exception e) {
						// TODO: handle exception
	        			e.printStackTrace();
	        			wrapper.sendRedirect(settingPath);
			            return;
					}
	        	
	            return;
	        }   
    }   
  
    @Override  
    public void init(FilterConfig filterConfig) throws ServletException {   
    	config2 = filterConfig;
    }
}   