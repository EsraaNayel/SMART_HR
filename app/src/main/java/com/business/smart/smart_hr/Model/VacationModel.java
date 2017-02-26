package com.business.smart.smart_hr.Model;

/**
 * Created by ESRAA on 2016-07-20.
 */
public class VacationModel {

    String str_vacCondition, str_vacHours, str_vacType,str_vacDate;

    public VacationModel(String str_vacCondition, String str_vacHours, String str_vacType, String str_vacDate) {
        this.str_vacCondition = str_vacCondition;
        this.str_vacHours = str_vacHours;
        this.str_vacType = str_vacType;
        this.str_vacDate = str_vacDate;
    }

    public String getStr_vacCondition() {
        return str_vacCondition;
    }

    public void setStr_vacCondition(String str_vacCondition) {
        this.str_vacCondition = str_vacCondition;
    }

    public String getStr_vacHours() {
        return str_vacHours;
    }

    public void setStr_vacHours(String str_vacHours) {
        this.str_vacHours = str_vacHours;
    }

    public String getStr_vacType() {
        return str_vacType;
    }

    public void setStr_vacType(String str_vacType) {
        this.str_vacType = str_vacType;
    }

    public String getStr_vacDate() {
        return str_vacDate;
    }

    public void setStr_vacDate(String str_vacDate) {
        this.str_vacDate = str_vacDate;
    }
}
