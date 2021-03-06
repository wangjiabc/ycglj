package com.ycglj.manage.model;

import java.io.Serializable;
import java.util.Date;

import com.ycglj.manage.daoSQL.annotations.SQLString;

public class Users implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private String openId;

	private float allConsume;
	
	private short subScribe;
	
	private String language;
	
	private String groupId;
	
	private String city;
	
	private String country;
	
	private String remark;
	
	private Float totalAmount;
	
	private String province;
			
	private int campusId;

    private String nickname;

    private String headImgUrl;

    private Date subscribeTime;
        
    private short sex;
    
    private String name;
    
    private Integer place;
    
    private String phone;
    
    private String headship;
    
    private String email;
    
    private String address;
    
    private String rank;
    
    private Date upTime;

   	private String department;

	private String duty;

	private String card_number;
    
	private Integer transact;
	
	private Integer area;
	
	private Integer business;
	
    public Integer getCampusId() {
		return campusId;
	}

	public void setCampusId(Integer campusId) {
		this.campusId = campusId;
	}
    
    
    public String getOpenId() {
		return openId;
	}
    
    public void setOpenId(String openId) {
		this.openId=openId;
	}

	public float getAllConsume() {
		return allConsume;
	}

	public void setAllConsume(float allConsume) {
		this.allConsume = allConsume;
	}
    
    public Integer getId() {
		return id;
	}
    
    public void setId(Integer id) {
		this.id=id;
	}
    

	public Users() {
	}

	public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }
    
    public String getImgUrl() {
    	
        return "<img src="+headImgUrl+" width='25px' height='25px'>";
        
    }

    public void setImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl == null ? null : headImgUrl.trim();
    }

	public Date getSubscribeTime() {
		return subscribeTime;
	}

	public void setSubscribeTime(Date subscribeTime) {
		this.subscribeTime = subscribeTime;
	}

	public Short getSex() {
		return sex;
	}

	public void setSex(Short sex) {
		this.sex = sex;
	}

	public short getSubScribe() {
		return subScribe;
	}

	public void setSubScribe(short subScribe) {
		this.subScribe = subScribe;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public Float getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Float totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getPlace() {
		return place;
	}

	public void setPlace(Integer place) {
		this.place = place;
	}

	public String getHeadship() {
		return headship;
	}

	public void setHeadship(String headship) {
		this.headship = headship;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getUpTime() {
		return upTime;
	}

	public void setUpTime(Date upTime) {
		this.upTime = upTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getCard_number() {
		return card_number;
	}

	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}

	public Integer getTransact() {
		return transact;
	}

	public void setTransact(int transact) {
		this.transact = transact;
	}

	public Integer getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public Integer getBusiness() {
		return business;
	}

	public void setBusiness(int business) {
		this.business = business;
	}

}