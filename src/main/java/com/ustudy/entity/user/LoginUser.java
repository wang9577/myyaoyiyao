package com.ustudy.entity.user;

import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.BooleanUtils;
import org.ustudy.core.dao.support.bean.anno.PathFile;
import org.ustudy.core.dao.support.entity.RecordUUIDEntity;

@SuppressWarnings("serial")

@MappedSuperclass
public class LoginUser extends RecordUUIDEntity {
	
	/**
	 * 学号或者工号，与登陆账户一致
	 */
	private String code;
	
	@PathFile
	private String photo;
	
	/**
	 * 姓名，正式姓名
	 */
	private String name;
	
	/**
	 * 性别
	 */
	private String gender;
	
	/**
	 * 手机
	 */
	private String mobile;
	
	/**
	 * 电子邮件
	 */
	private String email;

	/**
	 * 状态
	 */
	private Boolean status=true;
	
	private Integer loginCount = 0;
	
	private Long lastLoginTime;
	


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Integer getLoginCount() {
		if(loginCount == null) {
			return 0;
		}else {
			return loginCount;
		}
		
	}

	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}

	public Long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public void addLoginCount() {
		if(this.loginCount==null) {
			this.loginCount=0;
		}
		this.loginCount++;
		this.lastLoginTime=System.currentTimeMillis();
	}

	public Boolean getStatus() {
		return BooleanUtils.isTrue(status);
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
}
