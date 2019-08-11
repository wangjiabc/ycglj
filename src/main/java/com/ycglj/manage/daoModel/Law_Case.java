package com.ycglj.manage.daoModel;

import java.util.Date;

import java.io.Serializable;

import com.ycglj.manage.daoSQL.annotations.*;

@DBTable(name="[Law_Case]")
public class Law_Case implements Serializable{

    private static final long serialVersionUID = 1L;

    @SQLString(name="license")
	private String license;

    @SQLString(name="criminal_type")
	private String criminal_type;

    @SQLDouble(name="criminal_number")
	private Double criminal_number;

    @SQLDateTime(name="criminal_time")
	private Date criminal_time;

    @SQLDateTime(name="date")
	private Date date;

	public void setLicense(String license){
		this.license = license;
	}

	public String getLicense(){
		return license;
	}

	public void setCriminal_type(String criminal_type){
		this.criminal_type = criminal_type;
	}

	public String getCriminal_type(){
		return criminal_type;
	}

	public void setCriminal_number(Double criminal_number){
		this.criminal_number = criminal_number;
	}

	public Double getCriminal_number(){
		return criminal_number;
	}

	public void setCriminal_time(Date criminal_time){
		this.criminal_time = criminal_time;
	}

	public Date getCriminal_time(){
		return criminal_time;
	}

	public void setDate(Date date){
		this.date = date;
	}

	public Date getDate(){
		return date;
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

