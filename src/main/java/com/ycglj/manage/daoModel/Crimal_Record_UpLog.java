package com.ycglj.manage.daoModel;

import java.util.Date;

import java.io.Serializable;

import com.ycglj.manage.daoSQL.annotations.*;

@DBTable(name="[Crimal_Record_UpLog]")
public class Crimal_Record_UpLog implements Serializable{

    private static final long serialVersionUID = 1L;

    @SQLInteger(name="id")
	private Integer id;

    @SQLString(name="crimal_id")
	private String crimal_id;

    @SQLString(name="old_criminal_content")
	private String old_criminal_content;

    @SQLString(name="criminal_content")
	private String criminal_content;

    @SQLString(name="old_remark")
	private String old_remark;

    @SQLString(name="remark")
	private String remark;

    @SQLString(name="open_id")
	private String open_id;

    @SQLString(name="last_up_open_id")
	private String last_up_open_id;

    @SQLString(name="up_open_id")
	private String up_open_id;

    @SQLDateTime(name="criminal_time")
	private Date criminal_time;

    @SQLDateTime(name="last_up_time")
	private Date last_up_time;

    @SQLDateTime(name="up_time")
	private Date up_time;

    @SQLDouble(name="lng")
	private Double lng;

    @SQLDouble(name="lat")
	private Double lat;

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getId(){
		return id;
	}

	public void setCrimal_id(String crimal_id){
		this.crimal_id = crimal_id;
	}

	public String getCrimal_id(){
		return crimal_id;
	}

	public void setOld_criminal_content(String old_criminal_content){
		this.old_criminal_content = old_criminal_content;
	}

	public String getOld_criminal_content(){
		return old_criminal_content;
	}

	public void setCriminal_content(String criminal_content){
		this.criminal_content = criminal_content;
	}

	public String getCriminal_content(){
		return criminal_content;
	}

	public void setOld_remark(String old_remark){
		this.old_remark = old_remark;
	}

	public String getOld_remark(){
		return old_remark;
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

	public void setLast_up_open_id(String last_up_open_id){
		this.last_up_open_id = last_up_open_id;
	}

	public String getLast_up_open_id(){
		return last_up_open_id;
	}

	public void setUp_open_id(String up_open_id){
		this.up_open_id = up_open_id;
	}

	public String getUp_open_id(){
		return up_open_id;
	}

	public void setCriminal_time(Date criminal_time){
		this.criminal_time = criminal_time;
	}

	public Date getCriminal_time(){
		return criminal_time;
	}

	public void setLast_up_time(Date last_up_time){
		this.last_up_time = last_up_time;
	}

	public Date getLast_up_time(){
		return last_up_time;
	}

	public void setUp_time(Date up_time){
		this.up_time = up_time;
	}

	public Date getUp_time(){
		return up_time;
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

