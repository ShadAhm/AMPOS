package com.example.thisi.applicationx;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DeclareShiftMoneyActivity extends Activity {
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declareshiftmoney);

        myDb = DatabaseHelper.getHelper(this);

        addFilterToTextBox();
    }

   private void addFilterToTextBox() {
        EditText txtPayment = (EditText) findViewById(R.id.textDeclareShiftMoney);
        txtPayment.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10, 2)});
    }

    public void onDeclareShiftMoneyOK(View view) {
        SQLiteDatabase db = myDb.getReadableDatabase();

        db.beginTransaction();
        try {
            String todaysDateInString = new SimpleDateFormat("yyyyMMdd").format(new Date());
            Shift_Master latestOpenShift = myDb.lookForOpenShiftsAtDate(db, todaysDateInString);

            if(latestOpenShift == null)
            {
                showMessage("This shouldn't happen", "This shouldn't happen");
                return; // this shouldnt actually happen
            }

            EditText textDeclareShiftMoney = (EditText) findViewById(R.id.textDeclareShiftMoney);
            if (textDeclareShiftMoney.getText() == null || textDeclareShiftMoney.getText().toString() == null || textDeclareShiftMoney.getText().toString().isEmpty())
                return;

            SharedPreferences prefs = this.getSharedPreferences("com.example.thisi.applicationx", Context.MODE_PRIVATE);
            String posNo = prefs.getString("posnumber", null);

            String floatAmount = textDeclareShiftMoney.getText().toString();
            BigDecimal enteredAmount = new BigDecimal(floatAmount);
            BigDecimal correctAmount = myDb.supposedEndShiftMoney(db, latestOpenShift.SHIFT_START_AMT, latestOpenShift.SHIFT_NUMBER, latestOpenShift.BUS_DATE, posNo);

            if(enteredAmount.compareTo(correctAmount) == 0) {
                // TODO : update shift set to close 

                ContentValues cvals = new ContentValues();
                cvals.put("SHIFT_STATUS", "C");
                cvals.put("SHIFT_END_AMT", enteredAmount.doubleValue());
                db.update("shift_master", cvals, "ID = " + latestOpenShift.ID, null);

                db.setTransactionSuccessful();
                finish(); 
                Intent intent = new Intent(this, MainMenuActivity.class);
                startActivity(intent);
            }
            else {
                showMessage("Amount incorrect", "We have detected a different amount, please recalculate.");
            }

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void onDeclareShiftMoneyCancel(View view) {
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
