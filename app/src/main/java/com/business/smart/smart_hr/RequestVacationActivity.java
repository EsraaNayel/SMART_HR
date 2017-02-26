package com.business.smart.smart_hr;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ESRAA on 2016-07-20.
 */
public class RequestVacationActivity extends Activity {

    ArrayAdapter<String> Type_adapter;
    List<String> type_list;
    Spinner spnType;
    EditText etDate;
    private DatePickerDialog mDatePickerDialog;
    int curYear, curMonth, curDay;
    EditText edtDateCaller;

    String str_Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_vacation);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// to make keybord hidden

        spnType = (Spinner) findViewById(R.id.vacSpnType);
        etDate = (EditText) findViewById(R.id.VacDate);


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


        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtDateCaller = etDate;
                etDate.setInputType(InputType.TYPE_NULL); // to just open datepicker not keyboard
                Calendar c = Calendar.getInstance();
                c.set(Calendar.DAY_OF_MONTH, 1);
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog =
                        new DatePickerDialog(RequestVacationActivity.this, mDateSetListener, mYear, mMonth, mDay);
                dialog.show();


            }
        });
        etDate.setText(getCurrentDate2());
        str_Date = etDate.getText().toString();


        ImageButton imageButton = (ImageButton) findViewById(R.id.backbutton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestVacationActivity.this, VacationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        ImageButton homeButton = (ImageButton) findViewById(R.id.homebtn);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestVacationActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }

//    public String getCurrentDate() {//set Date of Day 1 in Current Month
//        Calendar c = Calendar.getInstance();
//        c.set(Calendar.DAY_OF_MONTH, 1);
//
////        System.out.println("Current time => " + Calendar.getTime());
//
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        String formattedDate = df.format(c.getTime());
//
//        return formattedDate;
//    }
//

    public String getCurrentDate2() {
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
            }
            else
                strDate = "";

            return strDate;
        }catch (Exception ex) {
            strDate = "";
            return strDate;
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
