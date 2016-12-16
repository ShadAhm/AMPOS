package com.example.thisi.applicationx.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.thisi.applicationx.data.DatabaseHelper;
import com.example.thisi.applicationx.R;

public class LoginDialogActivity 
extends AppCompatActivity {
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logindialog);

        myDb = DatabaseHelper.getHelper(this);
    }
}
