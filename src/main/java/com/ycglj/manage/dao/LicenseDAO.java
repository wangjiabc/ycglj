package com.ycglj.manage.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ycglj.manage.daoModel.Crimal_Case;
import com.ycglj.manage.daoModel.Crimal_Record;
import com.ycglj.manage.daoModel.FileSelfBelong;
import com.ycglj.manage.daoModel.Law_Case;
import com.ycglj.manage.daoModel.Position;
import com.ycglj.manage.daoModelJoin.Users_License_Position_Join;

public interface LicenseDAO {

	public Map getAllLicensePosition();
	
	public Map getAllLicensePositionJoin();
	
	public Map<String, Object> findAllLicense_Position(Integer limit, Integer offset, Double lng, Double lat,String term,Map search);
	
	public Map<String, Object> getAllLicense_Position(Integer limit, Integer offset,String sort,String order,Map search);
	
	public Map<String, Object> License_PositionImageQuery(HttpServletRequest request, List LicenseLits);
	
	public List allLicenseImageByGUID(HttpServletRequest request, Users_License_Position_Join users_License_Position_Join);
	
	public Integer updatePositionByLicense(Position position , boolean isUpdate); 
	
	public Integer insertIntoFileSelfBelong(FileSelfBelong fileSelfBelong);
	
	public Integer insertIntoCrimalCase(List<Crimal_Case> crimalCaseList,Crimal_Record crimal_Record);
	
	public List getAllCaseByLicense(String license);
}
