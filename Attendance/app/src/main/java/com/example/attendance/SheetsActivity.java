package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SheetsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String courseID;
    private String studentID;
    private String studentName;
    private final ArrayList<String> dates = new ArrayList<String>();
    private SheetsAdapter sheetsAdapter;
    private DatabaseHelper mDatabaseHelper;
    private SimpleDateFormat dateFormatDB = new SimpleDateFormat("yyyy-MM");
    private SimpleDateFormat dateFormatShow = new SimpleDateFormat("MM/yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_months);

        mDatabaseHelper = new DatabaseHelper(this);

        Intent i = getIntent();

        courseID = i.getExtras().getString("courseID");
        studentID = i.getExtras().getString("studentID");
        studentName = i.getExtras().getString("studentName");

        // list dates
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        sheetsAdapter = new SheetsAdapter(this, dates);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(sheetsAdapter);


        populateSheets(courseID);

        sheetsAdapter.setOnItemClickListener(position -> switchActivity(position));


    }

    private void switchActivity(int position)
    {
        Intent intent = new Intent(this, StatsActivity.class);


        intent.putExtra("courseID", courseID);
        intent.putExtra("studentID", studentID);
        intent.putExtra("currMonth", dates.get(position));
        intent.putExtra("studentName", studentName);
        //intent.putExtra("position", position);
        startActivity(intent);

    }


    private void populateSheets(String courseID) {
        //saveSheet();

        Cursor cursor = mDatabaseHelper.getMonths(courseID);
        dates.clear();
        Date curr = null;
        while (cursor.moveToNext())
        {
            String month = cursor.getString(cursor.getColumnIndexOrThrow("substr("+DatabaseHelper.DATE_KEY + ",1,7)"));

            try {
                 curr = dateFormatDB.parse(month);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }

            dates.add(dateFormatShow.format(curr));
            sheetsAdapter.notifyDataSetChanged();
        }
        cursor.close();
    }
}