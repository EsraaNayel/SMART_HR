package com.business.smart.smart_hr.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.business.smart.smart_hr.Model.AttendanceModel;
import com.business.smart.smart_hr.R;

import java.util.List;

/**
 * Created by ESRAA on 2016-07-21.
 */
public class AttendanceAdapter  extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder>  {
    public List<AttendanceModel> attendanceLists;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_row, parent, false);
        return new AttendanceAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AttendanceModel exceptionsList = attendanceLists.get(position);

        holder.tv_TimeIn.setText(exceptionsList.getStr_TimeIn());
        holder.tv_TimeOut.setText(exceptionsList.getStr_TimeOut());
        holder.tv_MachineId.setText(exceptionsList.getStr_MachineId());
        holder.tv_attendDate.setText(exceptionsList.getStr_AttendDate());

    }

    @Override
    public int getItemCount() {
        return attendanceLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_TimeIn,tv_TimeOut, tv_MachineId, tv_attendDate;

        public ViewHolder(View view) {
            super(view);

            tv_TimeIn=(TextView) view.findViewById(R.id.tv_timeIn);
            tv_TimeOut =(TextView) view.findViewById(R.id.tv_TimeOut);
            tv_MachineId = (TextView) view.findViewById(R.id.tv_MachineId);
            tv_attendDate = (TextView) view.findViewById(R.id.tv_AttendDate);
        }
    }

    public  AttendanceAdapter(List<AttendanceModel>attendanceModels){
        attendanceLists =attendanceModels;
    }
}
