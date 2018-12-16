package com.ycglj.manage.daoModel;

import java.util.Date;

import java.io.Serializable;

import com.ycglj.manage.daoSQL.annotations.*;

@DBTable(name="[Crimal_Record]")
public class Crimal_Record implements Serializable{

    private static final long serialVersionUID = 1L;

    @SQLString(name="crimal_id")
	private String crimal_id;

    @SQLString(name="license")
	private String license;

    @SQLDateTime(name="criminal_time")
	private Date criminal_time;

    @SQLString(name="criminal_content")
	private String criminal_content;

    @SQLString(name="remark")
	private String remark;

    @SQLString(name="open_id")
	private String open_id;

    @SQLDouble(name="lng")
	private Double lng;

    @SQLDouble(name="lat")
	private Double lat;

    @SQLDateTime(name="date")
	private Date date;

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

	public void setCriminal_time(Date criminal_time){
		this.criminal_time = criminal_time;
	}

	public Date getCriminal_time(){
		return criminal_time;
	}

	public void setCriminal_content(String criminal_content){
		this.criminal_content = criminal_content;
	}

	public String getCriminal_content(){
		return criminal_content;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getRemark(){
		return remark;
	}

	public void setOpen_id(String open_id){
		this.open_id = open_id;
	}

	public String getOpen_id(){
		return open_id;
	}

	public void setLng(Double lng){
		this.lng = lng;
	}

	public Double getLng(){
		return lng;
	}

	public void setLat(Double lat){
		this.lat = lat;
	}

	public Double getLat(){
		return lat;
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

