package com.business.smart.smart_hr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.business.smart.smart_hr.Connections.SqlConnection;
import com.business.smart.smart_hr.Global.GlobalClass;
import com.daimajia.androidanimations.library.YoYo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by ESRAA on 2016-07-10.
 */
public class LoginActivity extends Activity {

    ArrayAdapter<String> UsersAdapter;
    ArrayList<String> UserName = new ArrayList<String>();
    GlobalClass globalClass ;//globalClass = (GlobalClass) getApplicationContext();
    SqlConnection sqlConnection;
    ProgressBar pb_Bar;
    ArrayAdapter adapter;
    String strUserName, strPassword, strEmployeeID;
    EditText et_Password;
    Boolean isSuccess = false;
    AutoCompleteTextView et_UserName;
    private View mTarget;
    private YoYo.YoYoString rope;
    String TheDeviceName;
    String MACAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// to make keybord hidden
//        final GlobalClass globalClass = (GlobalClass) getApplicationContext();
        globalClass = (GlobalClass) getApplicationContext();

        TextView txt_PhoneNumber = (TextView) findViewById(R.id.txt_PhoneNumber);
        TextView txt_DeviceName = (TextView) findViewById(R.id.txt_DeviceName);

        TheDeviceName = getDeviceName();
        txt_DeviceName.setText(TheDeviceName);
        sqlConnection = new SqlConnection();
        String[] usersLogin = getResources().getStringArray(R.array.LoginList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usersLogin);
//        autoComplete = (AutoCompleteTextView) findViewById(R.id.autoComplete);
        // set adapter for the auto complete fields

        et_UserName = (AutoCompleteTextView) findViewById(R.id.et_userName);

        et_UserName.setAdapter(adapter);
        // specify the minimum type of characters before drop-down list is shown
        et_UserName.setThreshold(1);


        et_Password = (EditText) findViewById(R.id.et_password);
        pb_Bar = (ProgressBar) findViewById(R.id.pb_Bar);
        pb_Bar.setVisibility(View.GONE);
//         strUserName= et_UserName.getText().toString();
//         strPassword= et_Password.getText().toString();

        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        MACAddress = info.getMacAddress();
        txt_PhoneNumber.setText(MACAddress);
    }

    public void login(View v) {
//        et_userName = (EditText) findViewById(R.id.et_userName);
        strUserName = et_UserName.getText().toString();
        strPassword = et_Password.getText().toString();
        if (TextUtils.isEmpty(strUserName)) {
            et_UserName.setError(getString(R.string.err_username_required));
            return;
        }
        if (TextUtils.isEmpty(strPassword)) {
            et_Password.setError(getString(R.string.err_username_required));
            return;
        } else {
            DoLogin doLogin = new DoLogin();
            doLogin.execute();
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
        }
//        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//        startActivity(intent);
    }

    public class DoLogin extends AsyncTask<String, String, String> {
        String z = "";
        TextView strConnection = (TextView) findViewById(R.id.txt_ConnectionStatus);
        TextView strStatus = (TextView) findViewById(R.id.txt_Wait);

        protected void onPreExecute() {
            pb_Bar.setVisibility(View.VISIBLE);
            strConnection.setText("Connecting to SMART Database");
            strStatus.setText("Please wait...");
        }

        @Override
        protected void onPostExecute(String r) {
//            openNewActivity();
            pb_Bar.setVisibility(View.GONE);
            globalClass.setStrEmployeeID(strEmployeeID);
//            strConnection.setText("Connected successfully");
//            strStatus.setText("Enter username and password");

            if (isSuccess) {
                UsersAdapter = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_list_item_1, UserName);
                et_UserName.setAdapter(UsersAdapter);
                strConnection.setText("Connected successfully");
                strStatus.setText("Enter username and password");

                et_UserName.setEnabled(true);
                et_Password.setEnabled(true);
//                ().setEnabled(true);

//                mTarget = findViewById(R.id.txt_ConnectionStatus);
//
//                rope = YoYo.with(Techniques.RubberBand).duration(3000).playOn(mTarget);// after start,just click mTarget view, rope is not init

            } else {
                strConnection.setText("Connection error! - " + z);
                strStatus.setText("Please restart");
            }
        }
        @Override
        protected String doInBackground(String... params) {

            Connection con = sqlConnection.CONN();
            try {
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query = "select UserName , Password, EmployeeID from FSB_Users ";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        if (strUserName.equals(rs.getString("UserName")) && strPassword.equals(rs.getString("Password"))) {
                            isSuccess = true;
                            strEmployeeID=rs.getString("EmployeeID");

                        } else {
                            isSuccess = false;
                        }
                    }
                    if (isSuccess == false) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                // runs on UI thread
                                Toast.makeText(LoginActivity.this, "Wrong UserName OR Password ", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                // runs on UI thread
                                Toast.makeText(LoginActivity.this, "Done!! ", Toast.LENGTH_SHORT).show();
                                z = "تم الاتصال بنجاح";
                                UserName.add(strUserName);

                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                intent.putExtra("strEmployeeID", strEmployeeID);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                    z = "Success";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return z;
        }
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


}