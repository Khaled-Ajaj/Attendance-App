package com.example.attendance;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder>
{


    ArrayList<Student> students;
    Context context;
    private OnItemClickListener onItemClickListener;


    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

    public StudentAdapter(Context context, ArrayList<Student> students) {
        this.students = students;
        this.context = context;
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView stdName;
        TextView stdID;
        ToggleButton stdStatus;
        public StudentViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener)
        {
            super(itemView);
            stdName = itemView.findViewById(R.id.studentName);
            stdID = itemView.findViewById(R.id.studentID);
            stdStatus = (ToggleButton) itemView.findViewById(R.id.presentButton);
            itemView.setOnCreateContextMenuListener(this);

            //itemView.setOnClickListener(v->onItemClickListener.onClick(getAdapterPosition()));
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(getAdapterPosition(), 0, 0, "DELETE");
            contextMenu.add(getAdapterPosition(), 1, 0, "MODIFY");
            contextMenu.add(getAdapterPosition(), 2, 0, "SHOW STATS");

        }
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_design, parent, false);
        return new StudentViewHolder(itemView, onItemClickListener);
    }



    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        int currpos = position;
        Student currStd = students.get(position);
        holder.stdName.setText(currStd.getName());
        holder.stdID.setText(currStd.getID());
        String currStatus = currStd.getStatus();

        if (currStatus.equals("Absent"))
        {
            holder.stdStatus.setChecked(true);
        }
        else
        {
            holder.stdStatus.setChecked(false);
        }


        holder.stdStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {



                if (isChecked)
                {
                    currStd.setStatus("Absent");

                }
                else
                {
                    currStd.setStatus("Present");
                }

                Intent i = new Intent("to_update");
                i.putExtra("position", currpos);
                i.putExtra("status", currStd.getStatus());
                LocalBroadcastManager.getInstance(context).sendBroadcast(i);
                Log.d("ABSENCE", currStd.getName() + " " + currStd.getStatus());

            }
        });


    }

    @Override
    public int getItemCount() {
        return students.size();
    }




}
