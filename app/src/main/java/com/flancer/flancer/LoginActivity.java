package com.flancer.flancer;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.flancer.flancer.beans.User;
import com.flancer.flancer.beans.User_Table;
import com.flancer.flancer.global.myApp;
import com.raizlabs.android.dbflow.sql.language.SQLite;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View v) {
        EditText editPassField = (EditText) findViewById(R.id.password);
        String passField = editPassField.getText().toString();
        EditText editLoginField = (EditText) findViewById(R.id.email);
        String loginField = editLoginField.getText().toString();
        User user = SQLite.select().from(User.class).where(User_Table.email.is(loginField)).querySingle();

        if (user != null) {
            if (passField.equals(user.getPassword())) {
                ((myApp) this.getApplication()).user = user;
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                editPassField.setBackgroundColor(Color.parseColor("#ff0033"));
                editPassField.setTextColor(Color.parseColor("#FFFFFFFF"));
            }
        } else {
            editLoginField.setBackgroundColor(Color.parseColor("#ff0033"));
            editLoginField.setTextColor(Color.parseColor("#FFFFFFFF"));
        }
    }

    public void register(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
