package com.example.demo_app.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo_app.R;
import com.example.demo_app.models.Leave;

import java.text.DateFormatSymbols;
import java.util.List;
import java.util.Random;

public class leaveAdapter extends RecyclerView.Adapter<leaveAdapter.MyViewHolder> {
    private List<Leave> leaveList;
    leaveListener leaveListener;
    String uid;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView leave_title, from_date, to_date, status;
        CardView leaveItem;
        leaveListener leaveListener;

        public MyViewHolder(View view, leaveListener leaveListener) {
            super(view);
            leaveItem = view.findViewById(R.id.leaveItem);
            leave_title = view.findViewById(R.id.leave_title);
            from_date = view.findViewById(R.id.from_date);
            to_date = view.findViewById(R.id.to_date);
            status = view.findViewById(R.id.status);

            this.leaveListener = leaveListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Leave leave = leaveList.get(getAdapterPosition());
            leaveListener.onLeaveClickListener(leave.getLeaveReason());
        }
    }

    public interface leaveListener {
        void onLeaveClickListener(String leave_reason);
    }

    public leaveAdapter(List<Leave> appointmentList) {
        this.leaveList = appointmentList;
    }

    public int getRandomColor() {
        Random rnd = new Random();
        //return Color.argb(255, rnd.nextInt(243), rnd.nextInt(156), rnd.nextInt(255));
        return Color.argb(255, 255, 255, 255);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leave_list, parent, false);

        return new MyViewHolder(itemView, leaveListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Leave leave = leaveList.get(position);

        holder.leave_title.setText(leave.getLeaveReason());
        holder.from_date.setText(leave.getFromDate());
        holder.to_date.setText(leave.getToDate());
        holder.status.setText(leave.getApproved());

        /*if(position == 0) {
            holder.appointmentItem.setCardBackgroundColor(getRandomColor());
        }*/
    }

    @Override
    public int getItemCount() {
        return leaveList.size();
    }

}