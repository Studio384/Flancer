package com.flancer.flancer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void login(){
        EditText editPassField = (EditText)findViewById(R.id.password);
        String passField = editPassField.getText().toString();
        EditText editLoginField = (EditText)findViewById(R.id.email);
        String loginField = editLoginField.getText().toString();

        if(passField.equals("test") && loginField.equals("test")) {
            Intent intent = new Intent(this, JobListActivity.class);
            startActivity(intent);
        }
    }
    public void creatCompany(){

    }
    public void creatUser(){

    }
}
