package testfromalibaba.designforsort;

import java.util.Date;

public class MobileNumber<T> {
    private String number;
    private Date createTime;
    private Integer callOutTimes;
    private Integer callInTimes;
    //- other property....



    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCallOutTimes() {
        return callOutTimes;
    }

    public void setCallOutTimes(Integer callOutTimes) {
        this.callOutTimes = callOutTimes;
    }

    public Integer getCallInTimes() {
        return callInTimes;
    }

    public void setCallInTimes(Integer callInTimes) {
        this.callInTimes = callInTimes;
    }
}
