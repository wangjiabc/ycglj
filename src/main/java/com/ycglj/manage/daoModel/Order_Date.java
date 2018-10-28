package com.ycglj.manage.daoModel;

import java.util.Date;

import java.io.Serializable;

import com.ycglj.manage.daoSQL.annotations.*;

@DBTable(name="[Order_Date]")
public class Order_Date implements Serializable{

    private static final long serialVersionUID = 1L;

    @SQLInteger(name="id")
	private Integer id;

    @SQLString(name="uuid")
	private String uuid;

    @SQLDateTime(name="sub_date")
	private Date sub_date;

    @SQLInteger(name="agree")
	private Integer agree;

    @SQLInteger(name="disagree")
	private Integer disagree;

    @SQLString(name="operation")
	private String operation;

    @SQLInteger(name="order_number")
	private Integer order_number;

    @SQLDateTime(name="up_date")
	private Date up_date;

    @SQLDateTime(name="date")
	private Date date;

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getId(){
		return id;
	}

	public void setUuid(String uuid){
		this.uuid = uuid;
	}

	public String getUuid(){
		return uuid;
	}

	public void setSub_date(Date sub_date){
		this.sub_date = sub_date;
	}

	public Date getSub_date(){
		return sub_date;
	}

	public void setAgree(Integer agree){
		this.agree = agree;
	}

	public Integer getAgree(){
		return agree;
	}

	public void setDisagree(Integer disagree){
		this.disagree = disagree;
	}

	public Integer getDisagree(){
		return disagree;
	}

	public void setOperation(String operation){
		this.operation = operation;
	}

	public String getOperation(){
		return operation;
	}

	public void setOrder_number(Integer order_number){
		this.order_number = order_number;
	}

	public Integer getOrder_number(){
		return order_number;
	}

	public void setUp_date(Date up_date){
		this.up_date = up_date;
	}

	public Date getUp_date(){
		return up_date;
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

