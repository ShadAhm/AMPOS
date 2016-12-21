package com.example.thisi.applicationx.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.thisi.applicationx.data.DatabaseHelper;
import com.example.thisi.applicationx.service.DownloadDataService;
import com.example.thisi.applicationx.util.IWsdl2CodeEvents;
import com.example.thisi.applicationx.R;
import com.example.thisi.applicationx.model.Employee;

import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;

public class MainActivity 
extends AppCompatActivity {
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewtransactions);
        myDb = DatabaseHelper.getHelper(this);
    }
}
