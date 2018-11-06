package com.ycglj.manage.daoModelJoin;

import java.util.Date;

import com.ycglj.manage.daoSQL.annotations.SQLDateTime;
import com.ycglj.manage.daoSQL.annotations.SQLInteger;
import com.ycglj.manage.daoSQL.annotations.SQLString;

public class User_Order_Join {
	
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

    @SQLInteger(name="authentication")
	private Integer authentication;

    @SQLString(name="causa")
	private String causa;

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

	public void setAuthentication(Integer authentication){
		this.authentication = authentication;
	}

	public Integer getAuthentication(){
		return authentication;
	}

	public void setCausa(String causa){
		this.causa = causa;
	}

	public String getCausa(){
		return causa;
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


	    @SQLDateTime(name="sub_date")
		private Date sub_date;

	    @SQLInteger(name="cancel")
		private Integer cancel;

	    @SQLInteger(name="overdue")
		private Integer overdue;

	    @SQLInteger(name="overdue_number")
		private Integer overdue_number;

	    @SQLString(name="order_date_uuid")
		private String order_date_uuid;

	    @SQLDateTime(name="cancel_date")
		private Date cancel_date;

		public void setSub_date(Date sub_date){
			this.sub_date = sub_date;
		}

		public Date getSub_date(){
			return sub_date;
		}

		public void setCancel(Integer cancel){
			this.cancel = cancel;
		}

		public Integer getCancel(){
			return cancel;
		}

		public void setOverdue(Integer overdue){
			this.overdue = overdue;
		}

		public Integer getOverdue(){
			return overdue;
		}

		public void setOverdue_number(Integer overdue_number){
			this.overdue_number = overdue_number;
		}

		public Integer getOverdue_number(){
			return overdue_number;
		}

		public void setOrder_date_uuid(String order_date_uuid){
			this.order_date_uuid = order_date_uuid;
		}

		public String getOrder_date_uuid(){
			return order_date_uuid;
		}

		public void setCancel_date(Date cancel_date){
			this.cancel_date = cancel_date;
		}

		public Date getCancel_date(){
			return cancel_date;
		}


	
}
