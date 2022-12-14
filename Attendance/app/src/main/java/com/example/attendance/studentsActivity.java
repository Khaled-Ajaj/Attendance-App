package com.example.attendance;

import androidx.annotation.NonNull;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;

public class studentsActivity extends AppCompatActivity {

    private String courseName;
    private String courseID;
    private TextView editName, editID;
    private Intent goToSheets;
    StudentAdapter studentAdapter;
    CalendarClass cal = new CalendarClass();
    private int position;
    private Button apply;
    private Button addBtn;
    private Button dateBtn;
    private Button saveBtn;
    private Button searchBtn;
    private Button clearBtn;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private final ArrayList<Student> students = new ArrayList<Student>();
    private DatabaseHelper mDatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        mDatabaseHelper = new DatabaseHelper(this);

        // draw course name at top of screen
        Intent i = getIntent();

        courseName = i.getStringExtra("courseName");
        courseID = i.getStringExtra("courseID");
        //position = i.getIntExtra("position", -1);





        TextView courseText = findViewById(R.id.courseName);
        TextView idText = findViewById(R.id.courseID);
        dateBtn = findViewById(R.id.changeDateBtn);


        courseText.setText(courseName);
        idText.setText(courseID);
        dateBtn.setText("DATE: " + cal.getDate());

        // list students
        recyclerView = findViewById(R.id.studentList);
        layoutManager = new LinearLayoutManager(this);
        studentAdapter= new StudentAdapter(this, students);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(studentAdapter);

        populateStudents();


        addBtn = findViewById(R.id.addStudentBtn);

        addBtn.setOnClickListener(view -> showAddDialog());


        dateBtn.setOnClickListener(view -> changeDate());

        saveBtn = findViewById(R.id.saveSheetBtn);

        saveBtn.setOnClickListener(view -> saveSheet());


        searchBtn = findViewById(R.id.searchBtn);

        searchBtn.setOnClickListener(view -> showSearchDialog());

        clearBtn = findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(view -> saveSheet());
        clearBtn.setOnClickListener(view -> populateStudents());

        
        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-message".
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("to_update"));





    }

    private void saveSheet() {
        Student curr = null;
        for (int i = 0; i < students.size(); i++)
        {
            curr = students.get(i);
            mDatabaseHelper.updateStatus(Integer.parseInt(curr.getID()), courseID, cal.getDateRaw(), curr.getStatus());

        }
    }

    private void changeDate() {

        saveSheet();
        cal.show(getSupportFragmentManager(), "");
        cal.setOnCalendarClickListener(this::onCalendarOK);

        
    }

    private void onCalendarOK(int year, int month, int day) {

        cal.setDate(year, month, day);


        dateBtn.setText("DATE: " + cal.getDate());


        Student curr = null;
        for (int i = 0; i < students.size(); i++)
        {
            curr = students.get(i);
            mDatabaseHelper.addStatus(Integer.parseInt(curr.getID()), courseID, cal.getDateRaw(), "Present");

        }
        populateStudents();

        studentAdapter.notifyDataSetChanged();


    }


    private void showAddDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.addstudent_window, null);

        builder.setView(view);
        AlertDialog dlg = builder.create();
        dlg.show();


        editName = view.findViewById(R.id.addedStdName);
        editID = view.findViewById(R.id.addedStdID);

        Button add = view.findViewById(R.id.addStdBtn);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = editName.getText().toString();
                String newID = editID.getText().toString();
                mDatabaseHelper.addStudent(courseID, Integer.parseInt(newID), newName);
                mDatabaseHelper.addStatus(Integer.parseInt(newID), courseID, cal.getDateRaw(), "Present");
                students.add(new Student(newName, newID));
                studentAdapter.notifyDataSetChanged();
                dlg.dismiss();
            }
        });




    }

    private void populateStudents()
    {
        //saveSheet();
        Cursor cursor = mDatabaseHelper.getStudents(courseID);
        students.clear();

        while (cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.STUDENT_ID_KEY));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.STUDENT_NAME_KEY));

            students.add(new Student(name, String.valueOf(id)));
            students.get(students.size()-1).setStatus(mDatabaseHelper.getStatus(id, courseID, cal.getDateRaw()));
            studentAdapter.notifyDataSetChanged();
        }
        cursor.close();
    }

    private void populateSearchResults(String searchText)
    {
        saveSheet();
        Cursor cursor = mDatabaseHelper.getSearchStudents(courseID, searchText);
        students.clear();

        while (cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.STUDENT_ID_KEY));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.STUDENT_NAME_KEY));

            students.add(new Student(name, String.valueOf(id)));
            students.get(students.size()-1).setStatus(mDatabaseHelper.getStatus(id, courseID, cal.getDateRaw()));
            studentAdapter.notifyDataSetChanged();
        }
        cursor.close();
    }



    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int curr = item.getItemId();

        if (curr == 1)
        {
            // edit student info
            showEditDialog(item.getGroupId());
        }
        else if (curr == 0)
        {
            deleteStudent(item.getGroupId());
        }
        else if (curr == 2)
        {
            // show stats
            goToSheets = new Intent(studentsActivity.this, SheetsActivity.class);

            goToSheets.putExtra("courseID", courseID);
            goToSheets.putExtra("studentID", students.get(item.getGroupId()).getID());
            goToSheets.putExtra("studentName", students.get(item.getGroupId()).getName());
            startActivity(goToSheets);
        }
        return super.onContextItemSelected(item);
    }

    private void deleteStudent(int position)
    {
        mDatabaseHelper.removeStudent(students.get(position).getID());
        students.remove(position);
        studentAdapter.notifyItemRemoved(position);
        populateStudents();

    }

    private void showEditDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.editstudent_window, null);

        builder.setView(view);
        AlertDialog dlg = builder.create();
        dlg.show();


        editName = view.findViewById(R.id.editedStdName);
        editID = view.findViewById(R.id.editedStdID);

        editName.setText(students.get(position).getName());
        editID.setText(students.get(position).getID());

        apply = view.findViewById(R.id.applyStdEdits);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = editName.getText().toString();
                String newID = editID.getText().toString();
                mDatabaseHelper.updateStudent(Integer.parseInt(students.get(position).getID()), newName, Integer.parseInt(newID));
                students.get(position).setName(newName);
                students.get(position).setID(newID);
                studentAdapter.notifyDataSetChanged();

                dlg.dismiss();
            }
        });
    }

    private void showSearchDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.search_student_window, null);

        builder.setView(view);
        AlertDialog dlg = builder.create();
        dlg.show();


        EditText searchBar = view.findViewById(R.id.searchBar);


        Button searchButton = view.findViewById(R.id.searchStdBtn);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newSearch = searchBar.getText().toString();
                populateSearchResults(newSearch);
                studentAdapter.notifyDataSetChanged();
                dlg.dismiss();
            }
        });




    }

    @Override
    public void onBackPressed() {
        saveSheet();
        super.onBackPressed();
    }


    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

            String status = intent.getStringExtra("status");
            int pos = intent.getIntExtra("position", -1);
            studentAdapter.notifyDataSetChanged();
            //populateStudents();
            students.get(pos).setStatus(status);
            Log.d("UPDATING", students.get(pos).getName() + " to " + students.get(pos).getStatus());

        }
    };
}