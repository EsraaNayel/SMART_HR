package com.business.smart.smart_hr.Model;

/**
 * Created by ESRAA on 2016-07-13.
 */
public class ExceptionsModel {
    String str_excepCondition, str_excepHours, str_excepType,str_excepDate;


    public ExceptionsModel(String str_excepCondition, String str_excepHours, String str_excepType, String str_excepDate) {
        this.str_excepCondition = str_excepCondition;
        this.str_excepHours = str_excepHours;
        this.str_excepType = str_excepType;
        this.str_excepDate = str_excepDate;
    }




    public ExceptionsModel(String str_excepHours, String str_excepType, String str_excepDate) {
        this.str_excepHours = str_excepHours;
        this.str_excepType = str_excepType;
        this.str_excepDate = str_excepDate;
    }

    public String getStr_excepHours() {
        return str_excepHours;
    }

    public void setStr_excepHours(String str_excepHours) {
        this.str_excepHours = str_excepHours;
    }

    public String getStr_excepType() {
        return str_excepType;
    }

    public void setStr_excepType(String str_excepType) {
        this.str_excepType = str_excepType;
    }

    public String getStr_excepDate() {
        return str_excepDate;
    }

    public void setStr_excepDate(String str_excepDate) {
        this.str_excepDate = str_excepDate;
    }

    public String getStr_excepCondition() {
        return str_excepCondition;
    }

    public void setStr_excepCondition(String str_excepCondition) {
        this.str_excepCondition = str_excepCondition;
    }

}
