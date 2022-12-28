package com.example.attendance;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomePage extends AppCompatActivity {

    private static final String TAG = "HomePage";
    private String dataToEmail;
    DatabaseHelper mDatabaseHelper;


    private Button addBtn;
    private Button shareBtn;
    private EditText editName, editID;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CourseAdapter courseAdapter;
    private ArrayList<Course> courses = new ArrayList<Course>();
    private String newName, newID;
    final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        mDatabaseHelper = new DatabaseHelper(this);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        courseAdapter = new CourseAdapter(this, courses);
        recyclerView.setAdapter(courseAdapter);

        populateCourses();


        addBtn = (Button) findViewById(R.id.addCourse);

        addBtn.setOnClickListener(view -> showDialog());

        courseAdapter.setOnItemClickListener(position -> switchActivity(position));


        shareBtn = findViewById(R.id.shareBtn);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEmailData();
                shareData();

            }
        });



        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-message".
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("to_delete"));


    }



    private void shareData() {
        Date currentTime = Calendar.getInstance().getTime();

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, "Attendace Data " + dateFormat.format(currentTime));
        i.putExtra(Intent.EXTRA_TEXT, dataToEmail);
        startActivity(Intent.createChooser(i, "Share Via"));

    }

    private void switchActivity(int position)
    {
        Intent intent = new Intent(this, studentsActivity.class);

        intent.putExtra("courseName", courses.get(position).getCourseName());
        intent.putExtra("courseID", courses.get(position).getCourseID());
        intent.putExtra("position", position);
        startActivity(intent);

    }

    private void showDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.addcourse_window, null);

        builder.setView(view);
        AlertDialog dlg = builder.create();
        dlg.show();


        editName = view.findViewById(R.id.addedCourseName);
        editID = view.findViewById(R.id.addedCourseID);

        Button add = view.findViewById(R.id.addCrsBtn);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = editName.getText().toString();
                String newID = editID.getText().toString();
                long entryID = mDatabaseHelper.addCourse(newName, newID);

                addCourse(newName, newID);

                dlg.dismiss();
            }
        });




    }

    private void addCourse(String name, String ID)
    {
        courses.add(new Course(name, ID));
        courseAdapter.notifyDataSetChanged();
    }








    private void populateCourses() {
        Cursor crs = mDatabaseHelper.getCourses();
        courses.clear();


        while (crs.moveToNext())
        {
            int entry = crs.getInt(crs.getColumnIndexOrThrow(DatabaseHelper.COURSE_ENTRY_ID));
            String courseName =
                    crs.getString(crs.getColumnIndexOrThrow(DatabaseHelper.COURSE_NAME_KEY));
            String courseID =
                    crs.getString(crs.getColumnIndexOrThrow(DatabaseHelper.COURSE_ID_KEY));

            courses.add(new Course(courseName, courseID));
        }

        crs.close();

    }


    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            int position = intent.getIntExtra("position", -1);
            deleteCourse(position);

        }
    };


    private void deleteCourse(int position)
    {
        mDatabaseHelper.removeCourse(courses.get(position).getCourseID());
        courses.remove(position);
        courseAdapter.notifyItemRemoved(position);

    }

    private void getEmailData()
    {
        Cursor cursor = mDatabaseHelper.getAllStatusEntries();
        String courseID, studentID, date, status;

        dataToEmail = "courseID, studentID, Date, Status:\n";

        while (cursor.moveToNext())
        {
            courseID = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COURSE_ID_KEY));
            studentID = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.STUDENT_ID_KEY));
            date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.DATE_KEY));
            status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.STATUS_KEY));
            dataToEmail += String.format("%s,%s,%s,%s\n", courseID, studentID, date, status);

        }
        cursor.close();
        Log.d("DATA :3", dataToEmail);
    }






}