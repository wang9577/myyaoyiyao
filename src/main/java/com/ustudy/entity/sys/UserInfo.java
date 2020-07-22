package com.ustudy.entity.sys;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.ustudy.core.annos.views.SearchDisplayField;
import org.ustudy.core.annos.views.SearchField;
import org.ustudy.core.annos.views.SearchModel;
import org.ustudy.core.dao.support.bean.ListModel;
import org.ustudy.core.dao.support.entity.StringIdEntity;
/**
 * 登录用户信息
 * @author Hector.Tong
 * 2017年12月30日 下午7:57:32
 */
import org.ustudy.core.enums.views.ComType;

import com.ustudy.entity.commons.PathFile;
@SuppressWarnings("serial")
@Entity(name="UserInfo")
@Table(name="SYS_USERS_INFO",indexes= {@Index(columnList="code")})
@SearchModel(fields={@SearchField(name="code$like",type=ComType.INPUT,label="编号")
,@SearchField(name="name$like",type=ComType.INPUT,label="姓名")}
,displayFields={@SearchDisplayField(label="编号",name="code")
,@SearchDisplayField(label="姓名",name="name")
,@SearchDisplayField(label="备注",name="memo")})
public class UserInfo extends StringIdEntity implements ListModel{
	
	@ManyToOne(cascade={CascadeType.REFRESH},fetch=FetchType.LAZY)
	@JoinColumn(name="USER_ID")
	private User user;
	
	@Column(length=100)
	private String code;
	
	private String name;
	
	private String userType;
	
	private String gender;
	
	@ManyToOne(cascade={CascadeType.REFRESH},fetch=FetchType.LAZY)
	@JoinColumn(name="PHOTO_ID")
	private PathFile photo;
	
	
	private String birthday;
	
	private String position;
	
	private String phone;
	
	private String mobile;
	
	private String email;
	
	private String fax;
	
	private String qq;
	
	private String wechat;

	@Column
	@Lob
	@Basic(fetch=FetchType.LAZY)
	private String signature;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public PathFile getPhoto() {
		return photo;
	}

	public void setPhoto(PathFile photo) {
		this.photo = photo;
	}


	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

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

	public String getValue() {
		return this.getId();
	}

	public String getLabel() {
		return this.getName();
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}


}
