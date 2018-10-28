package com.ycglj.manage.serviceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ycglj.manage.mapper.MessageListMapper;
import com.ycglj.manage.mapper.WeiXinMapper;
import com.ycglj.manage.model.MessageList;
import com.ycglj.manage.model.WeiXin;
import com.ycglj.manage.service.WeiXinService;


@Service("weixinService")
public class WeiXinServiceImpl implements WeiXinService{

	private WeiXinMapper weixinMapper;
	
	private MessageListMapper messageListMapper;
	
	@Autowired
	public void setWeiXinMapper(WeiXinMapper weixinMapper) {
		this.weixinMapper=weixinMapper;
	}
	
	public WeiXinMapper getAccessTokenMapper() {
		return weixinMapper;
	}
	
	@Autowired
	public void setMessageListMapper(MessageListMapper messageListMapper) {
		this.messageListMapper = messageListMapper;
	}
	
	@Override
	public WeiXin getAccessTokenById(Integer campusId) {
		// TODO Auto-generated method stub
		return weixinMapper.getAccessToken(campusId);
	}

	@Override
	public List<WeiXin> getAllCampusById(Integer cityId) {
		// TODO Auto-generated method stub
		return weixinMapper.getAllCampus(cityId);
	}

	@Override
	public Integer insertIntoCampus(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return weixinMapper.insertCampus(paramMap);
	}

	@Override
	public WeiXin getByCampusIds(Integer campusId) {
		// TODO Auto-generated method stub
		return weixinMapper.getByCampusId(campusId);
	}

	@Override
	public Integer updateCampusById(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return weixinMapper.updateCampus(paramMap);
	}

	@Override
	public WeiXin getCampusById(Integer campusId) {
		// TODO Auto-generated method stub
		return weixinMapper.getCampus(campusId);
	}

	@Override
	public Integer updateHomePageByCampusId(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return weixinMapper.updateHomePageByCampusId(paramMap);
	}

	@Override
	public Integer getCampusIdByUserName(String userName) {
		// TODO Auto-generated method stub
		return weixinMapper.getCampusIdByUserName(userName);
	}

	@Override
	public Integer insertMessageList(MessageList messageList) {
		// TODO Auto-generated method stub
		return messageListMapper.insertMessageList(messageList);
	}

	@Override
	public List<MessageList> getAllMessageList(Integer campusId, Integer limit, Integer offset, String sort,
			String order, String search) {
		// TODO Auto-generated method stub
		return messageListMapper.getAllMessageList(campusId, limit, offset, sort, order, search);
	}

	@Override
	public Integer getAllMessageCount(Integer campusId, String search) {
		// TODO Auto-generated method stub
		return messageListMapper.getAllMessageCount(campusId, search);
	}


}
