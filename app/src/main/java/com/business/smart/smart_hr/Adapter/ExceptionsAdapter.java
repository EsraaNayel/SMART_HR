package com.business.smart.smart_hr.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.business.smart.smart_hr.Model.ExceptionsModel;
import com.business.smart.smart_hr.R;

import java.util.List;

/**
 * Created by ESRAA on 2016-07-17.
 */
public class ExceptionsAdapter  extends RecyclerView.Adapter<ExceptionsAdapter.ViewHolder>  {
    public List<ExceptionsModel> exceptionsLists;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_exception_row, parent, false);
        return new ExceptionsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ExceptionsModel exceptionsList = exceptionsLists.get(position);

        holder.tv_exceptionHours.setText(exceptionsList.getStr_excepHours());
        holder.tv_excpType.setText(exceptionsList.getStr_excepType());
        holder.tv_exceptionDate.setText(exceptionsList.getStr_excepDate());
        holder.tv_exceptionCondition.setText(exceptionsList.getStr_excepCondition());

    }

    @Override
    public int getItemCount() {
        return exceptionsLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_exceptionCondition,tv_exceptionHours, tv_excpType, tv_exceptionDate;

        public ViewHolder(View view) {
            super(view);

            tv_exceptionCondition=(TextView) view.findViewById(R.id.tv_exceptionCondition);
            tv_exceptionHours =(TextView) view.findViewById(R.id.tv_exceptionHours);
            tv_excpType = (TextView) view.findViewById(R.id.tv_exceptionType);
            tv_exceptionDate = (TextView) view.findViewById(R.id.tv_exceptionDate);
        }
    }

    public  ExceptionsAdapter(List<ExceptionsModel>exceptionsModels){
        exceptionsLists=exceptionsModels;
    }
}
