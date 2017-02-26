package com.business.smart.smart_hr;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.business.smart.smart_hr.Adapter.ExceptionsAdapter;
import com.business.smart.smart_hr.Model.ExceptionsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ESRAA on 2016-07-17.
 */
public class ExceptionsActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private List<ExceptionsModel> excepList = new ArrayList<>();
    ExceptionsModel exModel;
    private RecyclerView recyclerView;
    ExceptionsAdapter exceptionsAdapter;

    FloatingActionButton FabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception);

        exceptionsAdapter = new ExceptionsAdapter(excepList);
        FabButton = (FloatingActionButton) findViewById(R.id.FabButton);


        recyclerView = (RecyclerView) findViewById(R.id.item_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ExceptionsActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(exceptionsAdapter);

        FabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExceptionsActivity.this, RequestExceptionActivity.class);
                startActivity(intent);
                finish();
            }
        });


        ImageButton imageButton = (ImageButton) findViewById(R.id.backbutton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExceptionsActivity.this, FilterExceptionsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        ImageButton homeButton = (ImageButton) findViewById(R.id.homebtn);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExceptionsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        FillList fillList = new FillList();
        fillList.execute();
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
        protected void onPreExecute() {}
        @Override
        protected void onPostExecute(String r) {}
        @Override
        protected String doInBackground(String... params) {
            try {
                ExceptionsModel model = new ExceptionsModel("الحالة", "عدد الساعات", "نوع الإذن", " التاريخ");
                excepList.add(model);

                model = new ExceptionsModel("الحالة", "عدد الساعات", "نوع الإذن", " التاريخ");
                excepList.add(model);
                model = new ExceptionsModel("الحالة", "عدد الساعات", "نوع الإذن", " التاريخ");
                excepList.add(model);
                model = new ExceptionsModel("الحالة", "عدد الساعات", "نوع الإذن", " التاريخ");
                excepList.add(model);
                model = new ExceptionsModel("الحالة", "عدد الساعات", "نوع الإذن", " التاريخ");
                excepList.add(model);
                model = new ExceptionsModel("الحالة", "عدد الساعات", "نوع الإذن", " التاريخ");
                excepList.add(model);
                model = new ExceptionsModel("الحالة", "عدد الساعات", "نوع الإذن", " التاريخ");
                excepList.add(model);
                model = new ExceptionsModel("الحالة", "عدد الساعات", "نوع الإذن", " التاريخ");
                excepList.add(model);
                model = new ExceptionsModel("الحالة", "عدد الساعات", "نوع الإذن", " التاريخ");
                excepList.add(model);

            } catch (Exception ex) {
                z = "Error retrieving data from table";
            }
            return z;
        }
    }
}
