package com.flancer.flancer;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.flancer.flancer.beans.User;
import com.flancer.flancer.database.FlancerDatabase;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
            registerUser(firstName,lastName,email,password);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean colorEmptyFieldRed(List<EditText> editTextList) {
        boolean editTextFieldIsEmpty = false;
        for (EditText editText : editTextList) {
            if (TextUtils.isEmpty(editText.getText())) {
                editText.setBackgroundColor(Color.parseColor("#ff0033"));
                editText.setTextColor(Color.parseColor("#FFFFFFFF"));
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
        user.async();
        user.save();
    }
}
