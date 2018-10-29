package com.ycglj.manage.daoModel;

import java.util.Date;

import java.io.Serializable;

import com.ycglj.manage.daoSQL.annotations.*;

@DBTable(name="[Order_User]")
public class Order_User implements Serializable{

    private static final long serialVersionUID = 1L;

    @SQLInteger(name="id")
	private Integer id;

    @SQLString(name="open_id")
	private String open_id;

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

