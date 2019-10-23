package com.example.demo_app.adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.demo_app.R;
import com.example.demo_app.models.Leave;

import java.util.List;

public class listAdapter extends RecyclerView.Adapter<listAdapter.MyViewHolder> {
    private List<Leave> list;
    private listListener listListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_username, txt_leave_reason, txt_from_date, txt_to_date;
        Button approve, reject;
        listListener listListener;

        public MyViewHolder(View view, listListener listListener) {
            super(view);
            txt_username = view.findViewById(R.id.username);
            txt_leave_reason = view.findViewById(R.id.leave_reason);
            txt_from_date = view.findViewById(R.id.from_date);
            txt_to_date = view.findViewById(R.id.to_date);
            approve = view.findViewById(R.id.approve);
            reject = view.findViewById(R.id.reject);

            approve.setOnClickListener(this);
            reject.setOnClickListener(this);

            this.listListener = listListener;
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onClick(View v) {
            Leave leave = list.get(getAdapterPosition());
            switch (v.getId()) {
                case R.id.approve:
                    listListener.onApproveClickListener(leave.getUid());
                    break;
                case R.id.reject:
                    listListener.onRejectClickListener(leave.getUid());
                    break;
                default:
                    break;
            }
        }
    }

    public interface listListener {
        void onApproveClickListener(String uid);
        void onRejectClickListener(String uid);
    }

    public listAdapter(List<Leave> list, listListener listListener) {
        this.list = list;
        this.listListener = listListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hod_list_item, parent, false);

        return new MyViewHolder(itemView, listListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Leave leave = list.get(position);

        holder.txt_username.setText(leave.getUsername());
        holder.txt_leave_reason.setText(leave.getLeaveReason());
        holder.txt_from_date.setText(leave.getFromDate());
        holder.txt_to_date.setText(leave.getToDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}