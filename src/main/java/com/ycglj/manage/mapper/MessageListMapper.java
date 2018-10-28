package com.ycglj.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ycglj.manage.model.MessageList;

public interface MessageListMapper {
    int deleteByPrimaryKey(Integer id);

    int insertMessageList(MessageList record);

    int insertSelective(MessageList record);

    MessageList selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessageList record);

    int updateByPrimaryKey(MessageList record);
    
    List<MessageList> getAllMessageList(@Param(value="campusId")Integer campusId,@Param(value="limit")Integer limit, @Param(value="offset")Integer offset, @Param(value="sort")String sort, @Param(value="order")String order,@Param(value="search")String search);
    
    int getAllMessageCount(@Param(value="campusId")Integer campusId,@Param(value="search")String search);
    
}