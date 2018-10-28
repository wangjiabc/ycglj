package com.ycglj.weixin.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.message.BasicNameValuePair;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ycglj.manage.dao.UserDAO;
import com.ycglj.manage.daoModel.PreMessage;
import com.ycglj.manage.mapper.MessageListMapper;
import com.ycglj.manage.mapper.UsersMapper;
import com.ycglj.manage.mapper.WeiXinMapper;
import com.ycglj.manage.model.MessageList;
import com.ycglj.manage.model.Users;
import com.ycglj.manage.model.WeiXin;
import com.ycglj.manage.service.UserService;
import com.ycglj.manage.service.WeiXinService;
import com.ycglj.manage.serviceImpl.UserServiceImpl;
import com.ycglj.manage.singleton.Singleton;
import com.ycglj.sqlserver.context.Connect;
import com.ycglj.weixin.MessageTemplate.ChatTemplateProcessor;
import com.ycglj.weixin.MessageTemplate.TemplateData;
import com.ycglj.weixin.MessageTemplate.WxTemplate;
import com.ycglj.sqlserver.context.Connect;

import common.HttpClient;

@Controller
@RequestMapping("/mobile/WechatSendMessage")
public class WechatSendMessageController {
	Integer campusId=1;
	
	private WeiXinService weixinService;

	private UserService userService;
	
	ClassPathXmlApplicationContext mysqlApplicationContext = new ClassPathXmlApplicationContext(
			"spring-mybatis2.xml");
	DefaultSqlSessionFactory defaultSqlSessionFactory = (DefaultSqlSessionFactory) mysqlApplicationContext
			.getBean("sqlSessionFactory");
	SqlSession sqlSession = defaultSqlSessionFactory.openSession();
	
	private UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
	
	private WeiXinMapper weiXinMapper = sqlSession.getMapper(WeiXinMapper.class);
		
	private MessageListMapper messageListMapper = sqlSession.getMapper(MessageListMapper.class);
	
	ApplicationContext applicationContext=new Connect().get();
	
	UserDAO userDao=(UserDAO) applicationContext.getBean("userdao");
		
	@Autowired
	public void setAccessTokenService(WeiXinService weiXinService) {
		this.weixinService=weiXinService;
	}
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping("/send")
	public @ResponseBody String template(HttpServletRequest request,HttpServletResponse response,
			@RequestParam String title,@RequestParam String reportUser,
			@RequestParam String reportContext,@RequestParam String place,@RequestParam String url) {
		String accessToken;
    	WeiXin weixin;
		
		weixin=weixinService.getCampusById(campusId);   	
		accessToken=weixin.getAccessToken();
    	
		List userlist=userService.getUserByGuidance();
		
    	ChatTemplateProcessor wechatTemplate=new ChatTemplateProcessor();
    	
    	Iterator<Users> iterator=userlist.iterator();
    	
    	String message="";
    	
    	while (iterator.hasNext()) {
		
    	Users users=iterator.next();	
    	
    	String openid=users.getOpenId();
    	
    	WxTemplate templateData=new WxTemplate();
    	System.out.println("url="+url);
    	templateData.setUrl(url);
    	templateData.setTouser(openid);
    	templateData.setTopcolor("#000000");
    	templateData.setTemplate_id("XjGvV8BcSYzVtcxOJmXD2VefRqor4QQXYfLabWckkiw");
    	Map<String,TemplateData> m = new HashMap<String,TemplateData>();
    	TemplateData first = new TemplateData();
    	first.setColor("#000000");
    	first.setValue(title);
    	m.put("first", first);
    	TemplateData keyword1 = new TemplateData();
    	keyword1.setColor("#328392");
    	keyword1.setValue(reportUser);
    	m.put("keyword1", keyword1);
    	TemplateData keyword2 = new TemplateData();
    	keyword2.setColor("#328392");
    	keyword2.setValue(reportContext);
    	m.put("keyword2", keyword2);
    	TemplateData keyword3 = new TemplateData();
    	keyword3.setColor("#328392");
    	keyword3.setValue(place);
    	m.put("keyword3", keyword3);
    	SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	Date date=new Date();
		String time=sdf.format(date);
    	TemplateData keyword4 = new TemplateData();
    	keyword4.setColor("#328392");
    	keyword4.setValue(time);
    	m.put("keyword4", keyword4);
    	TemplateData remark = new TemplateData();
    	remark.setColor("#929232");
    	remark.setValue("点击查看事件位置");
    	m.put("remark", remark);
    	templateData.setData(m);
    	
    	String s=wechatTemplate.sendTemplateMessage(accessToken, templateData);
    	
    	message=message+"  "+users.getNickname()+"  "+s;
    	
    	MessageList messageList=new MessageList();
    	
    	messageList.setCampusId(campusId);
    	messageList.setOpenId(openid);
    	messageList.setContext(reportContext);
    	messageList.setType("紧急报告");
    	messageList.setSendTime(date);
    	if(s.equals("消息发送成功")){
    		messageList.setState(1);
    	}else{
    		messageList.setState(0);
    	}
    	
    	weixinService.insertMessageList(messageList);
    	
       }
    	
      return message;
    	
	}
	
	
	
	public void sendMessage(@RequestParam Integer place,@RequestParam String Template_Id,
			@RequestParam String Send_Type,@RequestParam String url,
			@RequestParam String first_data,@RequestParam String keyword1_data,
			@RequestParam String keyword2_data,@RequestParam String keyword3_data,
			@RequestParam String keyword4_data,@RequestParam String remark_data){

		Integer campusId=1;
		
		String accessToken;
    	WeiXin weixin;

	try{
		
		ClassPathXmlApplicationContext applicationContext=new ClassPathXmlApplicationContext("spring-mybatis2.xml");		
		DefaultSqlSessionFactory defaultSqlSessionFactory= (DefaultSqlSessionFactory) applicationContext.getBean("sqlSessionFactory");				
		SqlSession sqlSession=defaultSqlSessionFactory.openSession();
			
		UsersMapper usersMapper=sqlSession.getMapper(UsersMapper.class);
			
		MessageListMapper messageListMapper=sqlSession.getMapper(MessageListMapper.class);
			
		List<Users> list=usersMapper.getUserByPlace(place);
			
	  if(list!=null){
		
		 Iterator<Users> iterator=list.iterator();
			
	    while (iterator.hasNext()) {
			
		    Users users=iterator.next();

			if(users==null)
				return ;
			
			String openId=users.getOpenId();
						
			WeiXinMapper weiXinMapper=sqlSession.getMapper(WeiXinMapper.class);
			
			weixin=weiXinMapper.getCampus(campusId);   	
			accessToken=weixin.getAccessToken();
		
			WxTemplate templateData=new WxTemplate();
			templateData.setUrl(url);
	    	templateData.setTouser(openId);
	    	templateData.setTopcolor("#000000");
	    	templateData.setTemplate_id(Template_Id);
	    	Map<String,TemplateData> m = new HashMap<String,TemplateData>();
	    	TemplateData first = new TemplateData();
	    	first.setColor("#000000");
	    	first.setValue(first_data);
	    	m.put("first", first);
	        
	    	TemplateData keyword1 = new TemplateData();
	    	keyword1.setColor("#328392");
	    	keyword1.setValue(keyword1_data);
	    	m.put("keyword1", keyword1);
			TemplateData keyword2 = new TemplateData();
			keyword2.setColor("#328392");
			keyword2.setValue(keyword2_data);
			m.put("keyword2", keyword2);
			TemplateData keyword3 = new TemplateData();
			keyword3.setColor("#328392");
			keyword3.setValue(keyword3_data);
			m.put("keyword3", keyword3);
			
			if(keyword4_data!=null&&!keyword4_data.equals("")){
				TemplateData keyword4 = new TemplateData();
				keyword4.setColor("#328392");
				keyword4.setValue(keyword4_data);
				m.put("keyword4", keyword4);
			}
			
			if(remark_data!=null&&remark_data.equals("")){
				TemplateData remark = new TemplateData();
				remark.setColor("#929232");
				remark.setValue(remark_data);
				m.put("remark", remark);
			}
	    	templateData.setData(m);
	    	
	    	ChatTemplateProcessor wechatTemplate=new ChatTemplateProcessor();
	    	
	    	String s=wechatTemplate.sendTemplateMessage(accessToken, templateData);
	    	
	    	MessageList messageList=new MessageList();
	    	
	    	messageList.setCampusId(campusId);
	    	messageList.setOpenId(openId);
	    	messageList.setContext(keyword1_data+","+keyword2_data+
	    			","+keyword3_data+","+keyword4_data);
	    	messageList.setType(Send_Type);
	    	messageList.setSendTime(new Date());
	    	if(s.equals("消息发送成功")){
	    		messageList.setState(1);
	    	}else{
	    		messageList.setState(0);
	    	}
	    	
	    	messageListMapper.insertMessageList(messageList);
		  }
		}
	   }catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public Integer sendPhoneMessage(String phone,String Message,
			String charter,String openId){
		
		Users users = usersMapper.getUserByOnlyOpenId(openId);

		PreMessage preMessage = new PreMessage();

		preMessage.setOptAdd(users.getName());
		preMessage.setMessage(Message);

		HttpClient httpClient = new HttpClient();

		String requestUrl="http://utf8.api.smschinese.cn";

		List<BasicNameValuePair> reqParam = new ArrayList<BasicNameValuePair>();
		reqParam.add(new BasicNameValuePair("Uid", Singleton.UID));
		reqParam.add(new BasicNameValuePair("Key", Singleton.KEY));
		reqParam.add(new BasicNameValuePair("smsMob", phone));
		reqParam.add(new BasicNameValuePair("smsText", Message));
		String r = httpClient.doGet(requestUrl, reqParam);

		String uuid = UUID.randomUUID().toString();

		preMessage.setUUID(uuid);
		preMessage.setPhone(phone);
		preMessage.setOptDate(new Date());
		
		if(charter!=null&&!charter.equals(""))
			preMessage.setPhoneWho(charter);
		
		int i=Integer.parseInt(r);
		
		if(i>0){
			  preMessage.setState("发送成功");
		  }else{
			  preMessage.setState("发送失败");
		  }
		
		userDao.insertPreMessage(preMessage);

		return i;
	}
	
}
