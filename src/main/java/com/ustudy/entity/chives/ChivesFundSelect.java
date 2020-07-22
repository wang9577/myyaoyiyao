package com.ustudy.entity.chives;

import com.ustudy.entity.user.Chives;
import org.ustudy.core.dao.support.entity.RecordUUIDEntity;

import javax.persistence.*;

@Entity(name="ChivesFundSelect")
@Table(name="TR_Chives_FSELECT")
public class ChivesFundSelect extends RecordUUIDEntity {

    @ManyToOne(cascade= {CascadeType.REFRESH},fetch= FetchType.LAZY)
    @JoinColumn(name="c_ID")
    private Chives chives;


    private String fundCode;

    private String fundName;

    private Long selectTime;

    private Long dayTime;

    public Chives getChives() {
        return chives;
    }

    public void setChives(Chives chives) {
        this.chives = chives;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public Long getSelectTime() {
        return selectTime;
    }

    public void setSelectTime(Long selectTime) {
        this.selectTime = selectTime;
    }

    public Long getDayTime() {
        return dayTime;
    }

    public void setDayTime(Long dayTime) {
        this.dayTime = dayTime;
    }
}
