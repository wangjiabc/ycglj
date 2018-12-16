package com.ycglj.manage.daoModel;

import java.util.Date;

import java.io.Serializable;

import com.ycglj.manage.daoSQL.annotations.*;

@DBTable(name="[FileSelfBelong]")
public class FileSelfBelong implements Serializable{

    private static final long serialVersionUID = 1L;

    @SQLString(name="license")
	private String license;

    @SQLString(name="UpFileFullName")
	private String UpFileFullName;

    @SQLString(name="FileType")
	private String FileType;

    @SQLString(name="FileBelong")
	private String FileBelong;

    @SQLInteger(name="FileIndex")
	private Integer FileIndex;

    @SQLString(name="ViewFileName")
	private String ViewFileName;

    @SQLDateTime(name="date")
	private Date date;

	public void setLicense(String license){
		this.license = license;
	}

	public String getLicense(){
		return license;
	}

	public void setUpFileFullName(String UpFileFullName){
		this.UpFileFullName = UpFileFullName;
	}

	public String getUpFileFullName(){
		return UpFileFullName;
	}

	public void setFileType(String FileType){
		this.FileType = FileType;
	}

	public String getFileType(){
		return FileType;
	}

	public void setFileBelong(String FileBelong){
		this.FileBelong = FileBelong;
	}

	public String getFileBelong(){
		return FileBelong;
	}

	public void setFileIndex(Integer FileIndex){
		this.FileIndex = FileIndex;
	}

	public Integer getFileIndex(){
		return FileIndex;
	}

	public void setViewFileName(String ViewFileName){
		this.ViewFileName = ViewFileName;
	}

	public String getViewFileName(){
		return ViewFileName;
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

