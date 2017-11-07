package com.flancer.flancer;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.flancer.flancer.beans.User;
import com.flancer.flancer.beans.User_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void register(View v) {
        EditText firstName = (EditText) findViewById(R.id.firstName);
        EditText lastName = (EditText) findViewById(R.id.lastName);
        EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);
        List<EditText> editTextList = new ArrayList<EditText>();
        editTextList.add(firstName);
        editTextList.add(lastName);
        editTextList.add(email);
        editTextList.add(password);

        if (!colorEmptyFieldRed(editTextList)) {
            registerUser(firstName, lastName, email, password);
        } else {
            Toast.makeText(getApplicationContext(), "Pleas fil in the form", Toast.LENGTH_LONG).show();
        }
    }

    private boolean colorEmptyFieldRed(List<EditText> editTextList) {
        boolean editTextFieldIsEmpty = false;
        for (EditText editText : editTextList) {
            if (TextUtils.isEmpty(editText.getText())) {
                editText.setBackgroundColor(Color.parseColor("#ff0033"));
                editText.setTextColor(Color.parseColor("#ffffff"));
                editTextFieldIsEmpty = true;
            }
        }
        return editTextFieldIsEmpty;
    }

    private void registerUser(EditText firstName, EditText lastName, EditText email, EditText password) {
        User user = new User();
        user.setFirstName(firstName.getText().toString());
        user.setLastName(lastName.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
        if (userDoesNotExist(user)) {
            user.async();
            user.save();
            Toast.makeText(getApplicationContext(), "User created.", Toast.LENGTH_LONG).show();
            finish();
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        } else {
            Toast.makeText(getApplicationContext(), "Email is already taken.", Toast.LENGTH_SHORT).show();
            email.setBackgroundColor(Color.parseColor("#ff0033"));
        }
    }

    private boolean userDoesNotExist(User user) {
        User user1 = SQLite.select().from(User.class).where(User_Table.email.is(user.getEmail())).querySingle();

        if (user1 != null) {
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
