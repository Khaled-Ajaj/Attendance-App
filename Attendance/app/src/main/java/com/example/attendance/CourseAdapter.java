package com.example.attendance;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder>
{


    ArrayList<Course> courses;
    Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

    public CourseAdapter(Context context, ArrayList<Course> courses) {
        this.courses = courses;
        this.context = context;
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder{
        TextView crsName;
        TextView crsID;
        Button deleteBtn;
        public CourseViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener)
        {
            super(itemView);
            crsName = itemView.findViewById(R.id.courseName);
            crsID = itemView.findViewById(R.id.courseID);
            deleteBtn = itemView.findViewById(R.id.courseDelete);
            itemView.setOnClickListener(v->onItemClickListener.onClick(getAdapterPosition()));

            
        }
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_design, parent, false);
        return new CourseViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        holder.crsName.setText(courses.get(position).getCourseName());
        holder.crsID.setText(courses.get(position).getCourseID());
        int currpos = position;

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent("to_delete");
                i.putExtra("position", currpos);
                LocalBroadcastManager.getInstance(context).sendBroadcast(i);

            }
        });

    }



    @Override
    public int getItemCount() {
        return courses.size();
    }


}
