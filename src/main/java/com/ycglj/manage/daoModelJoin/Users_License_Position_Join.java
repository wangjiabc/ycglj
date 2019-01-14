package com.ycglj.manage.daoModelJoin;

import java.io.Serializable;
import java.util.Date;

import com.ycglj.manage.daoSQL.annotations.SQLDateTime;
import com.ycglj.manage.daoSQL.annotations.SQLDouble;
import com.ycglj.manage.daoSQL.annotations.SQLInteger;
import com.ycglj.manage.daoSQL.annotations.SQLString;

public class Users_License_Position_Join implements Serializable{

	private static final long serialVersionUID = 1L;

	@SQLString(name="name")
	private String name;

    @SQLString(name="card_type")
	private String card_type;

    @SQLString(name="sex")
	private String sex;

    @SQLString(name="id_number")
	private String id_number;

    @SQLString(name="causa")
	private String causa;

    @SQLDateTime(name="up_date")
	private Date up_date;
	
    @SQLString(name="open_id")
	private String open_id;

    @SQLString(name="phone")
	private String phone;

    @SQLString(name="license")
	private String license;

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

    @SQLDateTime(name="date")
	private Date date;

    @SQLString(name="business_state")
	private String business_state;
    
    @SQLInteger(name="weight")
	private Integer weight;
    
    @SQLInteger(name="id")
	private Integer id;

    @SQLInteger(name="is_license")
	private Integer is_license;

    @SQLString(name="check_id")
	private String check_id;

    @SQLString(name="neaten_id")
	private String neaten_id;

    @SQLString(name="province")
	private String province;

    @SQLString(name="city")
	private String city;

    @SQLString(name="district")
	private String district;

    @SQLString(name="street")
	private String street;

    @SQLString(name="street_number")
	private String street_number;

    @SQLDouble(name="lng")
	private Double lng;

    @SQLDouble(name="lat")
	private Double lat;
    
    @SQLDouble(name="wgs84_lng")
	private Double wgs84_lng;

    @SQLDouble(name="wgs84_lat")
	private Double wgs84_lat;
    
	public void setOpen_id(String open_id){
		this.open_id = open_id;
	}

	public String getOpen_id(){
		return open_id;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setLicense(String license){
		this.license = license;
	}

	public String getLicense(){
		return license;
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

	public void setDate(Date date){
		this.date = date;
	}

	public Date getDate(){
		return date;
	}

	public void setBusiness_state(String business_state){
		this.business_state = business_state;
	}

	public String getBusiness_state(){
		return business_state;
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

	public void setProvince(String province){
		this.province = province;
	}

	public String getProvince(){
		return province;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setDistrict(String district){
		this.district = district;
	}

	public String getDistrict(){
		return district;
	}

	public void setStreet(String street){
		this.street = street;
	}

	public String getStreet(){
		return street;
	}

	public void setStreet_number(String street_number){
		this.street_number = street_number;
	}

	public String getStreet_number(){
		return street_number;
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
	
	public void setWeight(Integer weight){
		this.weight = weight;
	}

	public Integer getWeight(){
		return weight;
	}
	
	public void setId(Integer id){
		this.id = id;
	}

	public Integer getId(){
		return id;
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
	
	public void setCausa(String causa){
		this.causa = causa;
	}

	public String getCausa(){
		return causa;
	}

	public void setUp_date(Date up_date){
		this.up_date = up_date;
	}

	public Date getUp_date(){
		return up_date;
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
}
