package com.ycglj.manage.daoModel;

import java.util.Date;

import java.io.Serializable;

import com.ycglj.manage.daoSQL.annotations.*;

@DBTable(name="[PreMessage]")
public class PreMessage implements Serializable{

    private static final long serialVersionUID = 1L;

    @SQLString(name="UUID")
	private String UUID;

    @SQLString(name="Phone")
	private String Phone;

    @SQLString(name="PhoneWho")
	private String PhoneWho;

    @SQLString(name="Message")
	private String Message;

    @SQLString(name="WhenSend")
	private String WhenSend;

    @SQLString(name="OptAdd")
	private String OptAdd;

    @SQLDateTime(name="OptDate")
	private Date OptDate;

    @SQLString(name="State")
	private String State;

    @SQLString(name="UnitBelong")
	private String UnitBelong;

	public void setUUID(String UUID){
		this.UUID = UUID;
	}

	public String getUUID(){
		return UUID;
	}

	public void setPhone(String Phone){
		this.Phone = Phone;
	}

	public String getPhone(){
		return Phone;
	}

	public void setPhoneWho(String PhoneWho){
		this.PhoneWho = PhoneWho;
	}

	public String getPhoneWho(){
		return PhoneWho;
	}

	public void setMessage(String Message){
		this.Message = Message;
	}

	public String getMessage(){
		return Message;
	}

	public void setWhenSend(String WhenSend){
		this.WhenSend = WhenSend;
	}

	public String getWhenSend(){
		return WhenSend;
	}

	public void setOptAdd(String OptAdd){
		this.OptAdd = OptAdd;
	}

	public String getOptAdd(){
		return OptAdd;
	}

	public void setOptDate(Date OptDate){
		this.OptDate = OptDate;
	}

	public Date getOptDate(){
		return OptDate;
	}

	public void setState(String State){
		this.State = State;
	}

	public String getState(){
		return State;
	}

	public void setUnitBelong(String UnitBelong){
		this.UnitBelong = UnitBelong;
	}

	public String getUnitBelong(){
		return UnitBelong;
	}




/*
*数据库查询参数
*/
    @QualifiLimit(name="limit")
    private Integer limit;
    @QualifiOffset(name="offset")
    private Integer offset;
    @QualifiNotIn(name="notIn")
    private String notIn;
    @QualifiSort(name="sort")
    private String sort;
    @QualifiOrder(name="order")
    private String order;
    @QualifiWhere(name="where")
    private String[] where;
    @QualifiWhereTerm(name="whereTerm")
    private String whereTerm;


	public void setLimit(Integer limit){
		this.limit = limit;
	}

	public Integer getLimit(){
		return limit;
	}

	public void setOffset(Integer offset){
		this.offset = offset;
	}

	public Integer getOffset(){
		return offset;
	}

	public void setNotIn(String notIn){
		this.notIn = notIn;
	}

	public String getNotIn(){
		return notIn;
	}

	public void setSort(String sort){
		this.sort = sort;
	}

	public String getSort(){
		return sort;
	}

	public void setOrder(String order){
		this.order = order;
	}

	public String getOrder(){
		return order;
	}

	public void setWhere(String[] where){
		this.where = where;
	}

	public String[] getWhere(){
		return where;
	}

	public void setWhereTerm(String whereTerm){
		this.whereTerm = whereTerm;
	}

	public String getWhereTerm(){
		return whereTerm;
	}

}

