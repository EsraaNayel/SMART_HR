package com.business.smart.smart_hr;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.business.smart.smart_hr.Adapter.ExceptionsAdapter;
import com.business.smart.smart_hr.Connections.SqlConnection;
import com.business.smart.smart_hr.Global.GlobalClass;
import com.business.smart.smart_hr.Model.AttendanceModel;
import com.business.smart.smart_hr.Model.ExceptionsModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ESRAA on 2016-07-19.
 */
public class FilterExceptionsActivity extends Activity implements AdapterView.OnItemSelectedListener {

    Button btn_getAll;
    Spinner spnType, spnCondition;
    ArrayAdapter<String> Type_adapter;
    ArrayAdapter<String> Condition_adapter;
    List<String> type_list;
    List<String> condition_list;

    Boolean isSuccess = false;

    ProgressBar pb_Bar;

    EditText etFrom, etTo;
    String str_timeFrom, str_timeTo;

    private DatePickerDialog mDatePickerDialog;
    int curYear, curMonth, curDay;
    EditText edtDateCaller;

    private List<ExceptionsModel> excepList = new ArrayList<>();//////
    ExceptionsModel exModel;////////
    private RecyclerView recyclerView;//////////
    ExceptionsAdapter exceptionsAdapter;//////////////

    FloatingActionButton FabButton;

    SqlConnection sqlConnection;
    GlobalClass globalClass;
    String strEmployeeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.exception);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// to make keybord hidden

        spnType = (Spinner) findViewById(R.id.spnType);
        spnCondition = (Spinner) findViewById(R.id.spnCondition);

        etFrom = (EditText) findViewById(R.id.et_from);
        etTo = (EditText) findViewById(R.id.et_To);
        btn_getAll = (Button) findViewById(R.id.btn_getAll);


        globalClass = (GlobalClass) getApplicationContext();
        strEmployeeID = globalClass.getStrEmployeeID();
        sqlConnection = new SqlConnection();

        pb_Bar = (ProgressBar) findViewById(R.id.pb_Bar);
        pb_Bar.setVisibility(View.GONE);

        exceptionsAdapter = new ExceptionsAdapter(excepList);///////
//        FabButton = (FloatingActionButton) findViewById(R.id.FabButton);////////

        recyclerView = (RecyclerView) findViewById(R.id.item_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(FilterExceptionsActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(exceptionsAdapter);



        type_list = new ArrayList<String>();
        type_list.add("جميع الأذون");
        type_list.add("أذون التأخير");
        type_list.add("أذون المغادرة قبل الميعاد");
        type_list.add("أذون بعدد الساعات");

        Type_adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinner_item, type_list);
        Type_adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spnType.setAdapter(Type_adapter);

        condition_list = new ArrayList<String>();
        condition_list.add("تم الموافقة ");
        condition_list.add("تحت المراجعة");
        Condition_adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinner_item, condition_list);
        Condition_adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spnCondition.setAdapter(Condition_adapter);

//        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
//        spinner.setAdapter(spinnerArrayAdapter);

        btn_getAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetCategory getCategory = new GetCategory();
                getCategory.execute();
            }
        });

        ImageButton imageButton = (ImageButton) findViewById(R.id.backbutton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterExceptionsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        ImageButton homeButton = (ImageButton) findViewById(R.id.homebtn);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterExceptionsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        ImageButton addButton = (ImageButton) findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterExceptionsActivity.this, RequestExceptionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        etFrom.setText(getCurrentDate());
        etTo.setText(getCurrentDate2());


        str_timeFrom = etFrom.getText().toString();
        str_timeTo = etTo.getText().toString();

        etFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtDateCaller = etFrom;
                etFrom.setInputType(InputType.TYPE_NULL); // to just open datepicker not keyboard
//                mDatePickerDialog = new DatePickerDialog(v.getContext(),
//                        mDateSetListener, curYear, curMonth, curDay);
//                mDatePickerDialog.setCancelable(true);
//                mDatePickerDialog.setTitle("من تاريخ");
//                mDatePickerDialog.show();

                Calendar c = Calendar.getInstance();
                c.set(Calendar.DAY_OF_MONTH, 1);
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog =
                        new DatePickerDialog(FilterExceptionsActivity.this, mDateSetListener, mYear, mMonth, mDay);
                dialog.show();


            }
        });
        etTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtDateCaller = etTo;
                etTo.setInputType(InputType.TYPE_NULL); // to just open datepicker not keyboard
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
                        new DatePickerDialog(FilterExceptionsActivity.this, mDateSetListener, mYear, mMonth, mDay);
                dialog.show();


            }
        });
//        FillList fillList = new FillList();
//        fillList.execute();
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

    public String getCurrentDate() {//set Date of Day 1 in Current Month
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);

//        System.out.println("Current time => " + Calendar.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    public String getCurrentDate2() {//set Current Date
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

//    public class FillList extends AsyncTask<String, String, String> {
//        String z = "";
//
//        @Override
//        protected void onPreExecute() {
//        }
//
//        @Override
//        protected void onPostExecute(String r) {
//        }
//
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                ExceptionsModel model = new ExceptionsModel("الحالة", "عدد الساعات", "نوع الإذن", " التاريخ");
//                excepList.add(model);
//
//                model = new ExceptionsModel("الحالة", "عدد الساعات", "نوع الإذن", " التاريخ");
//                excepList.add(model);
//                model = new ExceptionsModel("الحالة", "عدد الساعات", "نوع الإذن", " التاريخ");
//                excepList.add(model);
//                model = new ExceptionsModel("الحالة", "عدد الساعات", "نوع الإذن", " التاريخ");
//                excepList.add(model);
//                model = new ExceptionsModel("الحالة", "عدد الساعات", "نوع الإذن", " التاريخ");
//                excepList.add(model);
//                model = new ExceptionsModel("الحالة", "عدد الساعات", "نوع الإذن", " التاريخ");
//                excepList.add(model);
//                model = new ExceptionsModel("الحالة", "عدد الساعات", "نوع الإذن", " التاريخ");
//                excepList.add(model);
//                model = new ExceptionsModel("الحالة", "عدد الساعات", "نوع الإذن", " التاريخ");
//                excepList.add(model);
//                model = new ExceptionsModel("الحالة", "عدد الساعات", "نوع الإذن", " التاريخ");
//                excepList.add(model);
//
//            } catch (Exception ex) {
//                z = "Error retrieving data from table";
//            }
//            return z;
//        }
//    }


    public class GetCategory extends AsyncTask<String, String, String> {
        String z = "";
        //        Boolean isSuccess = false;
//        Connection con = sqlConnection.CONN();



        TextView strConnection = (TextView) findViewById(R.id.txt_ConnectionStatus);


        protected void onPreExecute() {
            pb_Bar.setVisibility(View.VISIBLE);
            strConnection.setText("Please wait ..... ");
            strConnection.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String r) {
            pb_Bar.setVisibility(View.GONE);
            recyclerView.setAdapter(exceptionsAdapter);
            strConnection.setVisibility(View.GONE);

            if (isSuccess==true) {
                strConnection.setText("Please wait ..... ");
                strConnection.setVisibility(View.GONE);

            } else {
//                strConnection.setVisibility(View.VISIBLE);
                strConnection.setText("Check the internet connection!" );
                Toast.makeText(FilterExceptionsActivity.this, "Check the internet connection! ", Toast.LENGTH_SHORT).show();
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
                    String query = "select *,\n" +
                            "(case PermissionIndex when '0' then 'تأخير في الحضور' when '1' then 'مغادرة قبل الميعاد' when '2' then 'أذن بعدد ساعات' end )as PermType\n" +
                            "from SMRT_EmployeePerm where EmployeeID=" + strEmployeeID ;
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        ExceptionsModel exceptionsModel = new ExceptionsModel(rs.getString("PermissionIndex"), rs.getString("PermHours"),
                                rs.getString("PermType"), rs.getString("PermDaysTo"));
                        excepList.add(exceptionsModel);

//                        isSuccess=true;
                    }
                    z = "Success";
                    isSuccess=true;
                }
            } catch (Exception ex) {
                isSuccess = false;
                z = "Exceptions";
            }
            return z;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
