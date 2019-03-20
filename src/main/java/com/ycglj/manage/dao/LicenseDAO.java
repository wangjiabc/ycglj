package com.ycglj.manage.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestParam;

import com.ycglj.manage.daoModel.Crimal_Case;
import com.ycglj.manage.daoModel.Crimal_Record;
import com.ycglj.manage.daoModel.FileSelfBelong;
import com.ycglj.manage.daoModel.Law_Case;
import com.ycglj.manage.daoModel.Not_License;
import com.ycglj.manage.daoModel.Position;
import com.ycglj.manage.daoModel.Temp_Change;
import com.ycglj.manage.daoModel.Temp_User_License;
import com.ycglj.manage.daoModel.User_License;
import com.ycglj.manage.daoModel.Weight_Log;
import com.ycglj.manage.daoModelJoin.Users_License_Position_Join;

public interface LicenseDAO {

	public Map getAllLicensePosition();
	
	public Map getAllLicensePositionJoin(String name,String startDate,String endDate,String[] yitStrings,String[] anyStrings,Integer type,com.ycglj.manage.model.Users users);
	
	public Map<String, Object> findAllLicense_Position(Integer limit, Integer offset, Double lng, Double lat,String term,Map search);
	
	public Map<String, Object> getAllLicense_Position(Integer limit, Integer offset,String sort,String order,String term,Map search);
	
	public Map<String, Object> License_PositionImageQuery(HttpServletRequest request, List LicenseLits);
	
	public List allLicenseImageByGUID(HttpServletRequest request, Users_License_Position_Join users_License_Position_Join);
	
	public Integer updatePositionByLicense(Position position , boolean isUpdate); 
	
	public Integer insertIntoFileSelfBelong(FileSelfBelong fileSelfBelong);
	
	public Integer insertIntoCrimalCase(List<Crimal_Case> crimalCaseList,Crimal_Record crimal_Record);
	
	public Integer updateCrimalCase(String crimal_id,List<Crimal_Case> crimalCaseList,Crimal_Record crimal_Record,String openId,Double lng,Double lat);
	
	public Integer insertNotLicenseCrimalCase(Not_License not_License,List<Crimal_Case> crimalCaseList,Crimal_Record crimal_Record);
		
	public List getAllCaseByLicense(String license);
	
	public List findPosition(Position position);
	
	public Map getAllCrimalRecordJoin(Integer limit, Integer offset, String sort,String order,Map search);
	
	public Map findRoomInfoPositionByLatLng(Double lat, Double lng);
	
	public Map getAllCheckPerson(Integer limit, Integer offset, String sort,String order,Map search);
	
	public Integer updateWeight(Weight_Log weight_Log);
	
	public List<Crimal_Case> getCrimalCase(String crimal_id);
	
	public String getNotLicense(String phone);
	
	public Not_License getNotLicenseById(String phone);
	
	public List getBusinessStateType();
	
	public Map getBusinessStateByDate(String endTime);
	
	public List getLawCaseType();
	
	public Map getLawCaseByDate(String startTime,String endTime);
	
	public List getThreeLawCaseType();
	
	public Map getThreeLawCaseByDate(String startTime,String endTime);
	
	public Integer deleteLicense(String operate_user,User_License user_License);
	
	public Integer insertLicense(Temp_User_License temp_User_License);
	
	public Temp_User_License getTempUserLicense(Temp_User_License temp_User_License);
	
	public Integer insertTempChange(Temp_Change temp_Change);
	
	public Temp_Change selectTempChange(Temp_Change temp_Change);
	
	public Integer deleteTempChange(Temp_Change temp_Change);
	
	public Integer updateTempUserLicense(Temp_User_License temp_User_License);
}
