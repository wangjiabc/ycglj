package com.ycglj.manage.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ycglj.manage.model.Sellers;

public interface SellerMapper {


	Sellers selectByCampusAdmin(String campusAdmin);

	int selectCountCampusAdmin(String campusAdmin);
	
	void updateLastLoginTime(@Param(value="date")Date date, @Param(value="campusAdmin") String campusAdmin
			);

	int insertSellective(Sellers seller);

	int updateSellective(Sellers sellers);
	
	int deleteSellective(String campusAdmin);
	
	Sellers selectByCampusId(String campusAdmin);

	List<Sellers> getCampusAdmin(String campusAdmin);

	List<Sellers> getAllCampusAdmin(); 
	
	int selectMaxCityId();
	
	int getCampusAdminCount(@Param(value="campusAdmin")String campusAdmin);
	
	int selectRepeatAdmin(@Param(value="campusAdmin") String campusAdmin);

}
