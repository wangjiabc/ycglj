package com.ycglj.manage.daoModelJoin;

import java.io.Serializable;
import java.util.Date;

import com.ycglj.manage.daoSQL.annotations.SQLDateTime;
import com.ycglj.manage.daoSQL.annotations.SQLDouble;
import com.ycglj.manage.daoSQL.annotations.SQLInteger;
import com.ycglj.manage.daoSQL.annotations.SQLString;

public class License_Position_Join implements Serializable{

	private static final long serialVersionUID = 1L;

    @SQLString(name="license")
	private String license;

    @SQLString(name="region")
	private String region;

    @SQLDateTime(name="date")
	private Date date;

    @SQLInteger(name="id")
	private Integer id;

    @SQLInteger(name="is_license")
	private Integer is_license;

    @SQLString(name="check_id")
	private String check_id;

    @SQLString(name="neaten_id")
	private String neaten_id;

    @SQLDouble(name="lng")
	private Double lng;

    @SQLDouble(name="lat")
	private Double lat;

    @SQLDouble(name="wgs84_lng")
	private Double wgs84_lng;

    @SQLDouble(name="wgs84_lat")
	private Double wgs84_lat;
    
    @SQLString(name="business_state")
	private String business_state;
    
    @SQLDouble(name="criminal_number")
	private Double criminal_number;
    
	public void setLicense(String license){
		this.license = license;
	}

	public String getLicense(){
		return license;
	}

	public void setRegion(String region){
		this.region = region;
	}

	public String getRegion(){
		return region;
	}

	public void setDate(Date date){
		this.date = date;
	}

	public Date getDate(){
		return date;
	}

	public void setIs_license(Integer is_license){
		this.is_license = is_license;
	}

	public Integer getIs_license(){
		return is_license;
	}

	public void setCheck_id(String check_id){
		this.check_id = check_id;
	}

	public String getCheck_id(){
		return check_id;
	}

	public void setNeaten_id(String neaten_id){
		this.neaten_id = neaten_id;
	}

	public String getNeaten_id(){
		return neaten_id;
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
	
	public void setId(Integer id){
		this.id = id;
	}

	public Integer getId(){
		return id;
	}

	public void setWgs84_lng(Double wgs84_lng){
		this.wgs84_lng = wgs84_lng;
	}

	public Double getWgs84_lng(){
		return wgs84_lng;
	}

	public void setWgs84_lat(Double wgs84_lat){
		this.wgs84_lat = wgs84_lat;
	}

	public Double getWgs84_lat(){
		return wgs84_lat;
	}
	
	public void setBusiness_state(String business_state){
		this.business_state = business_state;
	}

	public String getBusiness_state(){
		return business_state;
	}

	public void setCriminal_number(Double criminal_number){
		this.criminal_number = criminal_number;
	}

	public Double getCriminal_number(){
		return criminal_number;
	}

}
