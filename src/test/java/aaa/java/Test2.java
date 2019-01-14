package aaa.java;

import java.util.Date;

import java.io.Serializable;

import com.ycglj.manage.daoSQL.annotations.*;

@DBTable(name="[test2]")
public class Test2 implements Serializable{

    private static final long serialVersionUID = 1L;

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

