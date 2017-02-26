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

import com.business.smart.smart_hr.Adapter.ExceptionsAdapter;
import com.business.smart.smart_hr.Adapter.VacationAdapter;
import com.business.smart.smart_hr.Connections.SqlConnection;
import com.business.smart.smart_hr.Global.GlobalClass;
import com.business.smart.smart_hr.Model.ExceptionsModel;
import com.business.smart.smart_hr.Model.VacationModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ESRAA on 2016-07-20.
 */
public class FilterVacationsActivity extends Activity implements AdapterView.OnItemSelectedListener {

    Button btn_getAllVac;
    Spinner spnType, spnCondition;
    ArrayAdapter<String> Type_adapter;
    ArrayAdapter<String> Condition_adapter;
    List<String> type_list;
    List<String> condition_list;

    ProgressBar pb_Bar;

    EditText etFrom, etTo;
    String str_timeFrom, str_timeTo;

    private DatePickerDialog mDatePickerDialog;
    int curYear, curMonth, curDay;
    EditText edtDateCaller;


    private List<VacationModel> vacList = new ArrayList<>();
    VacationModel vacationModel;
    VacationAdapter vacationAdapter;
    private RecyclerView recyclerView;//////////


    SqlConnection sqlConnection;
    GlobalClass globalClass;
    String strEmployeeID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_filter_vacations);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// to make keybord hidden
        spnType = (Spinner) findViewById(R.id.spnVacType);
        spnCondition = (Spinner) findViewById(R.id.spnVacCondition);

        etFrom = (EditText) findViewById(R.id.et_from);
        etTo = (EditText) findViewById(R.id.et_To);

        btn_getAllVac = (Button) findViewById(R.id.btn_getAllVac);


        globalClass = (GlobalClass) getApplicationContext();
        strEmployeeID = globalClass.getStrEmployeeID();
        sqlConnection = new SqlConnection();


//        pb_Bar = (ProgressBar) findViewById(R.id.pb_Bar);
//        pb_Bar.setVisibility(View.GONE);

//        vacationAdapter = new VacationAdapter(vacList);


        type_list = new ArrayList<String>();
        type_list.add("جميع الأجازات");
        type_list.add("أجازة عارضة ");
        type_list.add("اجازة اعتيادي ");
        type_list.add("أجازة مرضي");
        type_list.add("أجازة وضع");
        type_list.add("أجازة اضافي ");
        type_list.add("بدون أجر");

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


//        recyclerView = (RecyclerView) findViewById(R.id.vac_recycler_view);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(FilterVacationsActivity.this);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(vacationAdapter);


        etFrom.setText(getCurrentDate());
        etTo.setText(getCurrentDate2());
        str_timeFrom = etFrom.getText().toString();
        str_timeTo = etTo.getText().toString();


        btn_getAllVac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterVacationsActivity.this, VacationActivity.class);
                intent.putExtra("fromDate", etFrom.getText().toString());
                intent.putExtra("toDate", etTo.getText().toString());
                startActivity(intent);
            }
        });

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
                        new DatePickerDialog(FilterVacationsActivity.this, mDateSetListener, mYear, mMonth, mDay);
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
                        new DatePickerDialog(FilterVacationsActivity.this, mDateSetListener, mYear, mMonth, mDay);
                dialog.show();


            }
        });


        ImageButton imageButton = (ImageButton) findViewById(R.id.backbutton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterVacationsActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        ImageButton homeButton = (ImageButton) findViewById(R.id.homebtn);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterVacationsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


//        ImageButton addButton = (ImageButton) findViewById(R.id.add);
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(FilterVacationsActivity.this, RequestVacationActivity.class);
//                startActivity(intent);
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

    public String getCurrentDate2() {//set Current Date
        Calendar c = Calendar.getInstance();
//        Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        Calendar.getInstance().getMaximum(Calendar.DAY_OF_MONTH);

//        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
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
//                    mDatePickerDialog.onDateChanged(view,year,monthOfYear,dayOfMonth);

                }
            };


    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // TODO Auto-generated method stub

        mDatePickerDialog.updateDate(year,monthOfYear,dayOfMonth);
//        TextView txt=(TextView)findViewById(R.id.txt);
//        txt.setText("You selected "+view.getDayOfMonth()+"/"+(view.getMonth()+1)+"/"+view.getYear());

    }


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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public class GetCategory extends AsyncTask<String, String, String> {
        String z = "";
        //        Boolean isSuccess = false;
//        Connection con = sqlConnection.CONN();

        protected void onPreExecute() {
            pb_Bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            recyclerView.setAdapter(vacationAdapter);
            pb_Bar.setVisibility(View.GONE);

        }

        //840
        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con = sqlConnection.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query = "Select * from SMRT_EmployeeVac Where EmployeeID =" + strEmployeeID + "  and  VacationID = (vacation selected from Spinner) and VacFrom >= " + str_timeFrom + " and VacTo <= " + str_timeTo;
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        vacationModel = new VacationModel(rs.getString("AttTimeFrom"), rs.getString("AttTimeTo"),
                                rs.getString("MachineName"), rs.getString("AttDate"));
                        vacList.add(vacationModel);
                    }
                    z = "Success";
                }
            } catch (Exception ex) {
//                isSuccess = false;
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
