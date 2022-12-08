package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class addCourseScreen extends AppCompatActivity {

    private Button addBtn;
    private EditText crsName, crsID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_course_screen);
//        addBtn = (Button) findViewById(R.id.addCrsBtn);
//        crsName = (EditText) findViewById(R.id.edtCourseName);
//        crsID = (EditText) findViewById(R.id.edtCourseID);
//
//        addBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String newName = crsName.getText().toString();
//                crsName.setText("");
//                crsID.setText("");
//
//                Intent intent = new Intent(addCourseScreen.this, HomePage.class);
//                intent.putExtra("name", newName);
//
//                startActivity(intent);
//            }
//        });
    }


}