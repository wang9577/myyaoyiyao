package com.ustudy.entity.chives;

import com.ustudy.entity.user.Chives;
import org.ustudy.core.dao.support.entity.RecordUUIDEntity;

import javax.persistence.*;

@Entity(name="ChivesFund")
@Table(name="TR_Chives_FUND")
public class ChivesFund extends RecordUUIDEntity {

    @ManyToOne(cascade= {CascadeType.REFRESH},fetch= FetchType.LAZY)
    @JoinColumn(name="c_ID")
    private Chives chives;


    private String fundCode;

    private String fundName;

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
}
