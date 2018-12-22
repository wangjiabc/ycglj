package com.ycglj.manage.daoModelJoin;

import java.io.Serializable;
import java.util.Date;

import com.ycglj.manage.daoSQL.annotations.SQLDateTime;
import com.ycglj.manage.daoSQL.annotations.SQLDouble;
import com.ycglj.manage.daoSQL.annotations.SQLInteger;
import com.ycglj.manage.daoSQL.annotations.SQLString;

public class Crimal_Record_Join implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@SQLInteger(name="id")
	private Integer id;
	
	@SQLString(name = "crimal_id")
	private String crimal_id;

	@SQLString(name = "license")
	private String license;

	@SQLDateTime(name = "criminal_time")
	private Date criminal_time;

	@SQLString(name = "criminal_content")
	private String criminal_content;

	@SQLString(name = "remark")
	private String remark;

	@SQLString(name = "open_id")
	private String open_id;

	@SQLDouble(name = "lng")
	private Double lng;

	@SQLDouble(name = "lat")
	private Double lat;

	@SQLDateTime(name = "date")
	private Date date;

    @SQLString(name="phone")
	private String phone;

    @SQLString(name="address")
	private String address;

    @SQLInteger(name="authentication")
	private Integer authentication;

    @SQLDateTime(name="authen_date")
	private Date authen_date;

    @SQLDateTime(name="license_start_date")
	private Date license_start_date;

    @SQLDateTime(name="license_end_date")
	private Date license_end_date;

    @SQLString(name="region")
	private String region;


    @SQLString(name="business_state")
	private String business_state;

    @SQLString(name="name")
	private String name;
    
    @SQLString(name="user_name")
	private String user_name;
    
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

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}


	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setAuthentication(Integer authentication){
		this.authentication = authentication;
	}

	public Integer getAuthentication(){
		return authentication;
	}

	public void setAuthen_date(Date authen_date){
		this.authen_date = authen_date;
	}

	public Date getAuthen_date(){
		return authen_date;
	}

	public void setLicense_start_date(Date license_start_date){
		this.license_start_date = license_start_date;
	}

	public Date getLicense_start_date(){
		return license_start_date;
	}

	public void setLicense_end_date(Date license_end_date){
		this.license_end_date = license_end_date;
	}

	public Date getLicense_end_date(){
		return license_end_date;
	}

	public void setRegion(String region){
		this.region = region;
	}

	public String getRegion(){
		return region;
	}


	public void setBusiness_state(String business_state){
		this.business_state = business_state;
	}

	public String getBusiness_state(){
		return business_state;
	}
	
	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}
	
	public void setUser_name(String user_name){
		this.user_name = user_name;
	}

	public String getUser_name(){
		return user_name;
	}
}
