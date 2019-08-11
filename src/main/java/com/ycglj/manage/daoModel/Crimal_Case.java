package com.ycglj.manage.daoModel;

import java.util.Date;

import java.io.Serializable;

import com.ycglj.manage.daoSQL.annotations.*;

@DBTable(name="[Crimal_Case]")
public class Crimal_Case implements Serializable{

    private static final long serialVersionUID = 1L;

    @SQLString(name="crimal_id")
	private String crimal_id;

    @SQLString(name="license")
	private String license;

    @SQLString(name="criminal_type")
	private String criminal_type;

    @SQLDouble(name="criminal_number")
	private Double criminal_number;

    @SQLString(name="open_id")
	private String open_id;

    @SQLDateTime(name="criminal_time")
	private Date criminal_time;

    @SQLDateTime(name="date")
	private Date date;

    @SQLDateTime(name="up_date")
	private Date up_date;

	public void setCrimal_id(String crimal_id){
		this.crimal_id = crimal_id;
	}

	public String getCrimal_id(){
		return crimal_id;
	}

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

	public void setOpen_id(String open_id){
		this.open_id = open_id;
	}

	public String getOpen_id(){
		return open_id;
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

