package com.business.smart.smart_hr.Model;

/**
 * Created by ESRAA on 2016-07-21.
 */
public class AttendanceModel {

    String str_TimeIn, str_TimeOut, str_MachineId,str_AttendDate;

    public AttendanceModel(String str_TimeIn, String str_TimeOut, String str_MachineId, String str_AttendDate) {
        this.str_TimeIn = str_TimeIn;
        this.str_TimeOut = str_TimeOut;
        this.str_MachineId = str_MachineId;
        this.str_AttendDate = str_AttendDate;
    }

    public String getStr_TimeIn() {
        return str_TimeIn;
    }

    public void setStr_TimeIn(String str_TimeIn) {
        this.str_TimeIn = str_TimeIn;
    }

    public String getStr_TimeOut() {
        return str_TimeOut;
    }

    public void setStr_TimeOut(String str_TimeOut) {
        this.str_TimeOut = str_TimeOut;
    }

    public String getStr_MachineId() {
        return str_MachineId;
    }

    public void setStr_MachineId(String str_MachineId) {
        this.str_MachineId = str_MachineId;
    }

    public String getStr_AttendDate() {
        return str_AttendDate;
    }

    public void setStr_AttendDate(String str_AttendDate) {
        this.str_AttendDate = str_AttendDate;
    }
}
