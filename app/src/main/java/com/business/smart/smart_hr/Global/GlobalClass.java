package com.business.smart.smart_hr.Global;

import android.app.Application;
import android.content.Intent;

import static android.content.Intent.getIntent;

/**
 * Created by ESRAA on 2016-07-14.
 */
public class GlobalClass extends Application {
    //global class to get the main data we will need it in different ways
    //to easily get the data again.

    private String strEmployeeID;

    public String getStrEmployeeID() {
        return strEmployeeID;
    }

    public void setStrEmployeeID(String strEmployeeID) {
        this.strEmployeeID = strEmployeeID;
    }
}
