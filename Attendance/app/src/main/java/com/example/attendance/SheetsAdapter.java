package com.example.attendance;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SheetsAdapter extends RecyclerView.Adapter<SheetsAdapter.SheetsViewHolder> {

    private ArrayList<String> dates;
    private Context context;
    private OnItemClickListener onItemClickListener;



    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SheetsAdapter(Context context, ArrayList<String> dates) {
        this.dates = dates;
        this.context = context;
    }

    public static class SheetsViewHolder extends RecyclerView.ViewHolder {

        TextView dateView;

        public SheetsViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            dateView = itemView.findViewById(R.id.sheetDate);
            itemView.setOnClickListener(v->onItemClickListener.onClick(getAdapterPosition()));


            //itemView.setOnClickListener(v->onItemClickListener.onClick(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public SheetsAdapter.SheetsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sheet_design, parent, false);
        return new SheetsViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SheetsViewHolder holder, int position) {

        String currDate = dates.get(position);


        holder.dateView.setText(currDate);



        //Log.d("PP", Integer.toString(dates.size()));

    }

    @Override
    public int getItemCount() {
        return dates.size();
    }
}



