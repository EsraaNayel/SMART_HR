package com.business.smart.smart_hr.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.business.smart.smart_hr.Model.VacationModel;
import com.business.smart.smart_hr.R;

import java.util.List;

/**
 * Created by ESRAA on 2016-07-20.
 */
public class VacationAdapter   extends RecyclerView.Adapter<VacationAdapter.ViewHolder>  {
    public List<VacationModel> vacationLists;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_exception_row, parent, false);
        return new VacationAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VacationModel vacationModel = vacationLists.get(position);

        holder.tv_vacHours.setText(vacationModel.getStr_vacHours());
        holder.tv_vacType.setText(vacationModel.getStr_vacType());
        holder.tv_vacDate.setText(vacationModel.getStr_vacDate());
        holder.tv_vacCondition.setText(vacationModel.getStr_vacCondition());

    }

    @Override
    public int getItemCount() {
        return vacationLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_vacCondition,tv_vacHours, tv_vacType, tv_vacDate;

        public ViewHolder(View view) {
            super(view);

            tv_vacCondition=(TextView) view.findViewById(R.id.tv_exceptionCondition);
            tv_vacHours =(TextView) view.findViewById(R.id.tv_exceptionHours);
            tv_vacType = (TextView) view.findViewById(R.id.tv_exceptionType);
            tv_vacDate = (TextView) view.findViewById(R.id.tv_exceptionDate);
        }
    }

    public  VacationAdapter(List<VacationModel>vacationModels){
        vacationLists=vacationModels;
    }
}
