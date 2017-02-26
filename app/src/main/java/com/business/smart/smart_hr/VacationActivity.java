package com.business.smart.smart_hr;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.business.smart.smart_hr.Adapter.ExceptionsAdapter;
import com.business.smart.smart_hr.Adapter.VacationAdapter;
import com.business.smart.smart_hr.Connections.SqlConnection;
import com.business.smart.smart_hr.Global.GlobalClass;
import com.business.smart.smart_hr.Model.AttendanceModel;
import com.business.smart.smart_hr.Model.ExceptionsModel;
import com.business.smart.smart_hr.Model.VacationModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ESRAA on 2016-07-20.
 */
public class VacationActivity extends Activity implements AdapterView.OnItemSelectedListener {
    private List<VacationModel> vacList = new ArrayList<>();
    VacationModel vacationModel;
    private RecyclerView recyclerView;
    VacationAdapter vacationAdapter;
    FloatingActionButton FabButton;

    SqlConnection sqlConnection;
    GlobalClass globalClass;
    String strEmployeeID;

    Boolean isSuccess = false;
    ProgressBar pb_Bar;
    TextView strConnection;

    String str_timeFrom , str_timeTo;

    SwipeRefreshLayout swipeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacations);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// to make keybord hidden

        globalClass = (GlobalClass) getApplicationContext();
        strEmployeeID = globalClass.getStrEmployeeID();
        sqlConnection = new SqlConnection();

        pb_Bar = (ProgressBar) findViewById(R.id.pb_Bar);
        pb_Bar.setVisibility(View.GONE);

        Intent intent = getIntent();

        str_timeFrom = intent.getStringExtra("fromDate");
        str_timeTo = intent.getStringExtra("toDate");


        vacationAdapter = new VacationAdapter(vacList);
        FabButton = (FloatingActionButton) findViewById(R.id.FabButton);

        recyclerView = (RecyclerView) findViewById(R.id.vacation_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(VacationActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(vacationAdapter);
        strConnection = (TextView) findViewById(R.id.txt_ConnectionStatus);




//        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        FabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VacationActivity.this, RequestVacationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageButton imageButton = (ImageButton) findViewById(R.id.backbutton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VacationActivity.this, FilterVacationsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        ImageButton homeButton = (ImageButton) findViewById(R.id.homebtn);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VacationActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        GetCategory getCategory = new GetCategory();
        getCategory.execute();

//        FillList fillList = new FillList();
//        fillList.execute();
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
//                VacationModel vacModel = new VacationModel("الفترة من ","إلى ","عدد الأيام","التاريخ");
//                vacList.add(vacModel);
//
//                vacModel = new VacationModel("الفترة من ","إلى ","عدد الأيام","التاريخ");
//                vacList.add(vacModel);
//                vacModel = new VacationModel("الفترة من ","إلى ","عدد الأيام","التاريخ");
//                vacList.add(vacModel);
//                vacModel = new VacationModel("الفترة من ","إلى ","عدد الأيام","التاريخ");
//                vacList.add(vacModel);
//                vacModel = new VacationModel("الفترة من ","إلى ","عدد الأيام","التاريخ");
//                vacList.add(vacModel);
//                vacModel = new VacationModel("الفترة من ","إلى ","عدد الأيام","التاريخ");
//                vacList.add(vacModel);
//                vacModel = new VacationModel("الفترة من ","إلى ","عدد الأيام","التاريخ");
//                vacList.add(vacModel);
//                vacModel = new VacationModel("الفترة من ","إلى ","عدد الأيام","التاريخ");
//                vacList.add(vacModel);
//                vacModel = new VacationModel("الفترة من ","إلى ","عدد الأيام","التاريخ");
//                vacList.add(vacModel);
//
//            } catch (Exception ex) {
//                z = "Error retrieving data from table";
//            }
//            return z;
//        }
//    }

    public class GetCategory extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;
//        Connection con = sqlConnection.CONN();

        protected void onPreExecute() {
            pb_Bar.setVisibility(View.VISIBLE);
            strConnection.setText("Please wait ..... ");
            strConnection.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            recyclerView.setAdapter(vacationAdapter);
            pb_Bar.setVisibility(View.GONE);

            pb_Bar.setVisibility(View.GONE);
            strConnection.setVisibility(View.GONE);


            if (isSuccess==true) {
                strConnection.setText("Please wait ..... ");
                strConnection.setVisibility(View.GONE);
            } else {
                strConnection.setText("Check the internet connection!" );
                Toast.makeText(VacationActivity.this, "Check the internet connection! ", Toast.LENGTH_SHORT).show();
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
                    int vacationId=1;
                    String query = "Select * from SMRT_EmployeeVac Where EmployeeID ="+strEmployeeID +"  and  VacationID = "+vacationId +" and VacFrom >= " +"'"+ str_timeFrom+"'"+" and VacTo <= "+"'"+str_timeTo+"'" ;
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                         vacationModel = new VacationModel(rs.getString("VacFrom"), rs.getString("VacTo"),
                                rs.getString("VaDays"), rs.getString("VacTo"));
                        vacList.add(vacationModel);
                    }
                    z = "Success";
                    isSuccess=true;
                }
            } catch (Exception ex) {
//                isSuccess = false;
                z = "Exceptions";
                isSuccess=false;
            }
            return z;
        }
    }

//    public void showSwipeProgress() {
//        swipeLayout.setRefreshing(true);
//    }
//
////    @Override public void setContentView(int layoutResID) {
////        View v = getLayoutInflater().inflate(layoutResID, swipeLayout, false);
////        setContentView(v);
////    }
////
////    @Override public void setContentView(View view) {
////        setContentView(view, view.getLayoutParams());
////    }
//
//    @Override public void setContentView(View view, ViewGroup.LayoutParams params) {
//        swipeLayout.addView(view, params);
//        initSwipeOptions();
//    }
//
//    private void initSwipeOptions() {
//        swipeLayout.setOnRefreshListener(VacationActivity.this);
//        setAppearance();
//        disableSwipe();
//    }
//
//    private void setAppearance() {
//        swipeLayout.setColorScheme(getResources().getColor(android.R.color.holo_blue_bright),
//               getResources().getColor( android.R.color.holo_green_light),
//                getResources().getColor(android.R.color.holo_orange_light),
//                getResources().getColor(android.R.color.holo_red_light));
//    }
//
//    /**
//     * It shows the SwipeRefreshLayout progress
//     */
//    public void hideSwipeProgress() {
//        swipeLayout.setRefreshing(false);
//    }
//
//    /**
//     * Enables swipe gesture
//     */
//    public void enableSwipe() {
//        swipeLayout.setEnabled(true);
//    }
//
//    /**
//     * Disables swipe gesture. It prevents manual gestures but keeps the option tu show
//     * refreshing programatically.
//     */
//    public void disableSwipe() {
//        swipeLayout.setEnabled(false);
//    }
//
//    /**
//     * It must be overriden by parent classes if manual swipe is enabled.
//     */
//    @Override public void onRefresh() {
//        // Empty implementation
//    }

    @Override
    public void onBackPressed() {
        finish();
    }
}