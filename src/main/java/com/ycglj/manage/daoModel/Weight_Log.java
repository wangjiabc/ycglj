package com.ycglj.manage.daoModel;

import java.util.Date;

import java.io.Serializable;

import com.ycglj.manage.daoSQL.annotations.*;

@DBTable(name="[Weight_Log]")
public class Weight_Log implements Serializable{

    private static final long serialVersionUID = 1L;

    @SQLString(name="license")
	private String license;

    @SQLString(name="campusAdmin")
	private String campusAdmin;

    @SQLInteger(name="state")
	private Integer state;

    @SQLDouble(name="lng")
	private Double lng;

    @SQLDouble(name="lat")
	private Double lat;

    @SQLDateTime(name="date")
	private Date date;

	public void setLicense(String license){
		this.license = license;
	}

	public String getLicense(){
		return license;
	}

	public void setCampusAdmin(String campusAdmin){
		this.campusAdmin = campusAdmin;
	}

	public String getCampusAdmin(){
		return campusAdmin;
	}

	public void setState(Integer state){
		this.state = state;
	}

	public Integer getState(){
		return state;
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

