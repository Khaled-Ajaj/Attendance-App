package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StatsActivity extends AppCompatActivity {

    private DatabaseHelper mDatabaseHelper;
    private String courseID;
    private String studentID;
    private String studentName;
    private String currMonth;
    private int currMonthInt;
    private String percentageString;
    private Date currMonthDBFormat;
    private TextView nameView;
    private TextView studentIDView;
    private TextView courseIDView;
    private TextView dateView;
    private TextView presentView;
    private TextView absentView;
    private TextView NAView;
    private TextView percentageView;


    private int[] studentStats = {0,0};
    private SimpleDateFormat dateFormatDB = new SimpleDateFormat("yyyy-MM");
    private SimpleDateFormat dateFormatShow = new SimpleDateFormat("MM/yyyy");
    private final int[] numDays = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        mDatabaseHelper = new DatabaseHelper(this);

        Intent i = getIntent();
        courseID = i.getExtras().getString("courseID");
        studentID = i.getExtras().getString("studentID");
        currMonth = i.getExtras().getString("currMonth");
        studentName = i.getExtras().getString("studentName");


        currMonthInt = Integer.parseInt(currMonth.substring(0,2));
        Log.d("currmonth", String.valueOf(currMonthInt));


        studentStats = getPresenceNums();

        nameView = findViewById(R.id.studentName);
        nameView.setText(studentName);

        studentIDView = findViewById(R.id.studentID);
        studentIDView.setText(studentID);

        courseIDView = findViewById(R.id.courseID);
        courseIDView.setText(courseID);

        dateView = findViewById(R.id.dateText);
        dateView.setText(currMonth);

        presentView = findViewById(R.id.presentText);
        presentView.setText("Number of Present Days: " + studentStats[0]);

        absentView = findViewById(R.id.absentText);
        absentView.setText("Number of Absent Days: " + studentStats[1]);

        NAView = findViewById(R.id.noRecordText);
        NAView.setText("Number of No Record Days: " + (numDays[currMonthInt] - (studentStats[0]+studentStats[1])));

        percentageView = findViewById(R.id.absencePercentage);
        percentageString = String.format("%.2f", ((float)studentStats[1]/(float)numDays[currMonthInt])*100);
        percentageView.setText("Absence Percentage: " + percentageString + "%");












    }

    private int[] getPresenceNums()
    {
        int[] presentAbsentData = {0,0};

        try {
            currMonthDBFormat = dateFormatShow.parse(currMonth);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }



        Cursor cursor = mDatabaseHelper.getStudentStats(courseID, studentID, dateFormatDB.format(currMonthDBFormat));

        while (cursor.moveToNext())
        {
            Boolean isPresent = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.STATUS_KEY)).equals("Present");
            if (isPresent)
            {
                presentAbsentData[0] += 1;
            }
            else
            {
                presentAbsentData[1] += 1;
            }
        }
        cursor.close();
        return presentAbsentData;
    }


}