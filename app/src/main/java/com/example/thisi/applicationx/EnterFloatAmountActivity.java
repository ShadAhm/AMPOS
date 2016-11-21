package com.example.thisi.applicationx;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EnterFloatAmountActivity extends Activity {
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterfloatamount);

        myDb = DatabaseHelper.getHelper(this);

        addFilterToTextBox();
    }

   private void addFilterToTextBox() {
        EditText txtFloatAmount = (EditText) findViewById(R.id.textFloatAmount);
        txtFloatAmount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10, 2)});
    }

    public void onEnterFloatAmountOK(View view) {
        SQLiteDatabase db = myDb.getReadableDatabase();

        db.beginTransaction();
        try {
            String todaysDateInString = new SimpleDateFormat("yyyyMMdd").format(new Date());
            Shift_Master latestShift = myDb.lookForLatestShiftAtDate(db, todaysDateInString);

            int newShiftNo = 1;
            if(latestShift != null)
            {
                newShiftNo = latestShift.SHIFT_NUMBER + 1; 
            }

            EditText txtFloatAmount = (EditText) findViewById(R.id.textFloatAmount);
            if (txtFloatAmount.getText() == null || txtFloatAmount.getText().toString() == null || txtFloatAmount.getText().toString().isEmpty())
                return;

            String floatAmount = txtFloatAmount.getText().toString();

            BigDecimal bd = new BigDecimal(floatAmount);

            SharedPreferences prefs = this.getSharedPreferences("com.example.thisi.applicationx", Context.MODE_PRIVATE);
            String posNo = prefs.getString("posnumber", "errorUndefined");
            String companyCode = prefs.getString("companycode", "errorUndefined");
            String outletCode = prefs.getString("outletcode", "errorUndefined");

            Shift_Master newShift = new Shift_Master();
            newShift.COMPANY_CODE = companyCode; 
            newShift.OUTLET_CODE = outletCode;
            newShift.POS_NO = posNo; 
            newShift.BUS_DATE = todaysDateInString; 
            newShift.SHIFT_NUMBER = newShiftNo; 
            newShift.SHIFT_STATUS = "O"; 
            newShift.SHIFT_START_AMT = bd; 
            newShift.SHIFT_END_AMT = new BigDecimal("0.00");

            myDb.insertShiftMaster(db, newShift);

            db.setTransactionSuccessful();

            Toast.makeText(getApplicationContext(), "Shift started", Toast.LENGTH_SHORT).show();
            
            finish(); 
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void onEnterFloatAmountCancel(View view) {
        finish(); 
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.show();
    }

}
