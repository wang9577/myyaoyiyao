package com.ustudy.entity.sys;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.ustudy.core.dao.support.entity.StringIdEntity;

@SuppressWarnings("serial")
@Entity(name = "DbConfig")
@Table(name = "SYS_DB_CONFIG")
public class DbConfig extends StringIdEntity {
	
	private String code;
	
	private String name;
	
	private String val;
	
	private String memo;

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

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}
