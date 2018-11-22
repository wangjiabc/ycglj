package com.ycglj.manage.daoModelJoin;

import java.util.Date;

import com.ycglj.manage.daoSQL.annotations.SQLDateTime;
import com.ycglj.manage.daoSQL.annotations.SQLInteger;
import com.ycglj.manage.daoSQL.annotations.SQLString;

public class Users_License_Join {

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

	    @SQLString(name="causa")
		private String causa;

	    @SQLDateTime(name="up_date")
		private Date up_date;

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

		public void setCausa(String causa){
			this.causa = causa;
		}

		public String getCausa(){
			return causa;
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


		
}
