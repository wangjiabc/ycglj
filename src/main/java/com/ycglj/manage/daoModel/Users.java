package com.ycglj.manage.daoModel;

import java.util.Date;

import java.io.Serializable;

import com.ycglj.manage.daoSQL.annotations.*;

@DBTable(name="[Users]")
public class Users implements Serializable{

    private static final long serialVersionUID = 1L;

    @SQLInteger(name="id")
	private Integer id;

    @SQLString(name="open_id")
	private String open_id;

    @SQLString(name="name")
	private String name;

    @SQLString(name="card_type")
	private String card_type;

    @SQLString(name="sex")
	private String sex;

    @SQLString(name="id_number")
	private String id_number;

    @SQLString(name="phone")
	private String phone;

    @SQLString(name="authentication")
	private String authentication;

    @SQLDateTime(name="authen_date")
	private Date authen_date;

    @SQLDateTime(name="date")
	private Date date;

    @SQLDateTime(name="up_date")
	private Date up_date;

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getId(){
		return id;
	}

	public void setOpen_id(String open_id){
		this.open_id = open_id;
	}

	public String getOpen_id(){
		return open_id;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setCard_type(String card_type){
		this.card_type = card_type;
	}

	public String getCard_type(){
		return card_type;
	}

	public void setSex(String sex){
		this.sex = sex;
	}

	public String getSex(){
		return sex;
	}

	public void setId_number(String id_number){
		this.id_number = id_number;
	}

	public String getId_number(){
		return id_number;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setAuthentication(String authentication){
		this.authentication = authentication;
	}

	public String getAuthentication(){
		return authentication;
	}

	public void setAuthen_date(Date authen_date){
		this.authen_date = authen_date;
	}

	public Date getAuthen_date(){
		return authen_date;
	}

	public void setDate(Date date){
		this.date = date;
	}

	public Date getDate(){
		return date;
	}

	public void setUp_date(Date up_date){
		this.up_date = up_date;
	}

	public Date getUp_date(){
		return up_date;
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

