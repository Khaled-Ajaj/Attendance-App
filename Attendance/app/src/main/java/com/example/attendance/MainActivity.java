package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {



    private EditText user;
    private EditText pass;
    private Button login;

    private String correctUser = "Admin";
    private String correctPass = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        login = findViewById(R.id.loginButton);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputUser = user.getText().toString();
                String inputPass = pass.getText().toString();

                if (isValidLogin(inputUser, inputPass))
                {
                    Intent intent = new Intent(MainActivity.this, HomePage.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private Boolean isValidLogin(String username, String password)
    {
        return username.equals(correctUser) && password.equals(correctPass);
    }
}