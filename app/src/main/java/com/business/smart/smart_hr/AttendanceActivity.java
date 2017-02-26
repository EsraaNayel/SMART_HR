package com.business.smart.smart_hr;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.business.smart.smart_hr.Adapter.AttendanceAdapter;
import com.business.smart.smart_hr.Connections.SqlConnection;
import com.business.smart.smart_hr.Global.GlobalClass;
import com.business.smart.smart_hr.Model.AttendanceModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*
  Created by ESRAA on 2016-07-21.
 */
public class AttendanceActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private List<AttendanceModel> attendList = new ArrayList<>();
    private RecyclerView recyclerView;
    AttendanceAdapter attendanceAdapter;

    Boolean isSuccess = false;
    ProgressBar pb_Bar;


    EditText etFrom, etTo;

    private DatePickerDialog mDatePickerDialog;
    int curYear, curMonth, curDay;
    EditText edtDateCaller;

    SqlConnection sqlConnection;
    GlobalClass globalClass;
    String strEmployeeID;

    String str_TimeIn, str_TimeOut, str_MachineID, str_date;
    String str_timeFrom, str_timeTo;

    Button btnFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// to make keybord hidden

//
        globalClass = (GlobalClass) getApplicationContext();
        strEmployeeID = globalClass.getStrEmployeeID();
        sqlConnection = new SqlConnection();


        attendanceAdapter = new AttendanceAdapter(attendList);
        etFrom = (EditText) findViewById(R.id.et_from);
        etTo = (EditText) findViewById(R.id.et_To);
        btnFilter = (Button) findViewById(R.id.btn_filter);


        pb_Bar = (ProgressBar) findViewById(R.id.pb_Bar);
        pb_Bar.setVisibility(View.GONE);


        recyclerView = (RecyclerView) findViewById(R.id.attendance_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AttendanceActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(attendanceAdapter);

        etFrom.setText(getCurrentDate());
        etTo.setText(getCurrentDate2());

        str_timeFrom = etFrom.getText().toString();//get txt in edit text of datefrom and put it in string
        str_timeTo = etTo.getText().toString();

        ImageButton imageButton = (ImageButton) findViewById(R.id.backbutton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//when open new activity clear the old one
                startActivity(intent);
                finish();
            }
        });

        ImageButton homeButton = (ImageButton) findViewById(R.id.homebtn);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        etFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// to make keybord hidden
                edtDateCaller = etFrom;
                etFrom.setInputType(InputType.TYPE_NULL); // to just open datepicker not keyboard

//                mDatePickerDialog = new DatePickerDialog(v.getContext(),
//                        mDateSetListener, curYear, curMonth, curDay);
//                mDatePickerDialog.setCancelable(true);
//                mDatePickerDialog.setTitle("من تاريخ");
//                mDatePickerDialog.show();

                Calendar c = Calendar.getInstance();
                c.set(Calendar.DAY_OF_MONTH, 1);//make date picker pick the day (1) in current month
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog =
                        new DatePickerDialog(AttendanceActivity.this, mDateSetListener, mYear, mMonth, mDay);
                dialog.show();


            }
        });
        etTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtDateCaller = etTo;
                etTo.setInputType(InputType.TYPE_NULL); // to just open datepicker not keyboard
//                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// to make keybord hidden
//                mDatePickerDialog = new DatePickerDialog(v.getContext(),
//                        mDateSetListener, curYear, curMonth, curDay);
//                mDatePickerDialog.setCancelable(true);
//                mDatePickerDialog.setTitle("إلى تاريخ");
//                mDatePickerDialog.show();

                Calendar c = Calendar.getInstance();
//                c.set(Calendar.DAY_OF_MONTH, 1);
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog =
                        new DatePickerDialog(AttendanceActivity.this, mDateSetListener, mYear, mMonth, mDay);
                dialog.show();


            }
        });
//        FillList fillList = new FillList();
//        fillList.execute();

//        btnFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                GetCategory getCategory = new GetCategory();
////                getCategory.execute();
//
//                Toast.makeText(AttendanceActivity.this, "Done1 ", Toast.LENGTH_SHORT).show();
//
//
//                GetCategory getCategory = new GetCategory();
//                getCategory.execute();
//            }
//        });
//
//
//        btnFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                GetCategory getCategory = new GetCategory();
//                getCategory.execute();
//            }
//        });

    }

    public String getCurrentDate() {//set Date of Day 1 in Current Month
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);

//        System.out.println("Current time => " + Calendar.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    public String getCurrentDate2() {// Set Current Date
        Calendar c = Calendar.getInstance();
//        Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        Calendar.getInstance().getMaximum(Calendar.DAY_OF_MONTH);

//        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public class FillList extends AsyncTask<String, String, String> {
        String z = "";

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String r) {
//            tv_empCode.setText(str_empCode);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                AttendanceModel model = new AttendanceModel("زمن الحضور ", "زمن الانصراف", "كود البصمة", " التاريخ");
                attendList.add(model);

                model = new AttendanceModel("زمن الحضور ", "زمن الانصراف", "كود البصمة", " التاريخ");
                attendList.add(model);
                model = new AttendanceModel("زمن الحضور ", "زمن الانصراف", "كود البصمة", " التاريخ");
                attendList.add(model);
                model = new AttendanceModel("زمن الحضور ", "زمن الانصراف", "كود البصمة", " التاريخ");
                attendList.add(model);
                model = new AttendanceModel("زمن الحضور ", "زمن الانصراف", "كود البصمة", " التاريخ");
                attendList.add(model);
                model = new AttendanceModel("زمن الحضور ", "زمن الانصراف", "كود البصمة", " التاريخ");
                attendList.add(model);
                model = new AttendanceModel("زمن الحضور ", "زمن الانصراف", "كود البصمة", " التاريخ");
                attendList.add(model);
                model = new AttendanceModel("زمن الحضور ", "زمن الانصراف", "كود البصمة", " التاريخ");
                attendList.add(model);
                model = new AttendanceModel("زمن الحضور ", "زمن الانصراف", "كود البصمة", " التاريخ");
                attendList.add(model);

            } catch (Exception ex) {
                z = "Error retrieving data from table";
            }
            return z;
        }
    }

    // define a variable mDataSetListener
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    curYear = year;
                    curMonth = monthOfYear;
                    curDay = dayOfMonth;
                    updateDisplay();
                }
            };

    private void updateDisplay() {
        StringBuilder myStr = new StringBuilder()
                .append(curYear).append("-")
                .append(curMonth + 1).append("-")
                .append(curDay).append(" ");
        edtDateCaller.setText(ValidateDate(myStr.toString()));
    }

    public String ValidateDate(String strDate) {
        try {
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            Calendar SetMyDate = Calendar.getInstance();

            if (!strDate.isEmpty()) {
                SetMyDate.setTime(formatDate.parse(strDate));
                strDate = formatDate.format(SetMyDate.getTime());
            } else
                strDate = "";

            return strDate;
        } catch (Exception ex) {
            strDate = "";
            return strDate;
        }

    }

    public void getData(View v) {
        GetCategory getCategory = new GetCategory();
        getCategory.execute();
    }

    public class GetCategory extends AsyncTask<String, String, String> {
        String z = "";


        TextView strConnection = (TextView) findViewById(R.id.txt_ConnectionStatus);

//        Connection con = sqlConnection.CONN();

        protected void onPreExecute() {
            pb_Bar.setVisibility(View.VISIBLE);
            strConnection.setText("Please wait ..... ");
            strConnection.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            recyclerView.setAdapter(attendanceAdapter);
            pb_Bar.setVisibility(View.GONE);
            strConnection.setVisibility(View.GONE);


            if (isSuccess == true) {
                strConnection.setText("Please wait ..... ");
                strConnection.setVisibility(View.GONE);
            } else {
                strConnection.setText("Check the internet connection!");
                Toast.makeText(AttendanceActivity.this, "Check the internet connection! ", Toast.LENGTH_SHORT).show();
            }
        }

        //840
        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con = sqlConnection.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query = "Select * , (Select MachineName from SMRT_Machines Where MachineID = SMRT_EmployeeAtt.MachineID) as MachineName" +
                            " from SMRT_EmployeeAtt Where (AttDate >=" + "'" + str_timeFrom + "'" + " and AttDate <=" + "'" + str_timeTo + "'" + "and EmployeeID = " + strEmployeeID + ")";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        AttendanceModel attendanceModel = new AttendanceModel(rs.getString("AttTimeFrom"), rs.getString("AttTimeTo"),
                                rs.getString("MachineName"), rs.getString("AttDate"));
                        attendList.add(attendanceModel);
                        isSuccess = true;
                    }
                    z = "Success";
                }
            } catch (Exception ex) {
//                isSuccess = false;
                z = "Exceptions";
                isSuccess = false;
            }

            return z;
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
