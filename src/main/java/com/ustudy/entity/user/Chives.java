package com.ustudy.entity.user;

import org.ustudy.core.dao.support.entity.RecordUUIDEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name="Chives")
@Table(name="TR_Chives")
public class Chives extends RecordUUIDEntity {

    private String code;

    private String name;

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
}
