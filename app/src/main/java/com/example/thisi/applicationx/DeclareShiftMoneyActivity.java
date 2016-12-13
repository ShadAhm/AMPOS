package com.example.thisi.applicationx;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

            if (latestOpenShift == null) {
                showMessage("Unknown open shift", "");
                return; // this shouldnt actually happen
            }

            EditText textDeclareShiftMoney = (EditText) findViewById(R.id.textDeclareShiftMoney);

            String shiftmoney = "0";
            if (textDeclareShiftMoney.getText() == null || textDeclareShiftMoney.getText().toString() == null || textDeclareShiftMoney.getText().toString().isEmpty())
                shiftmoney = "0";
            else
                shiftmoney = textDeclareShiftMoney.getText().toString();

            SharedPreferences prefs = this.getSharedPreferences("com.example.thisi.applicationx", Context.MODE_PRIVATE);
            String posNo = prefs.getString("posnumber", null);

            BigDecimal enteredAmount = new BigDecimal(shiftmoney);
            BigDecimal correctAmount = myDb.supposedEndShiftMoney(db, latestOpenShift.SHIFT_START_AMT, latestOpenShift.SHIFT_NUMBER, latestOpenShift.BUS_DATE, posNo);

            if (enteredAmount.compareTo(correctAmount) == 0) {
                endShift(db, latestOpenShift, enteredAmount);
            } else {
                giveUserOverrideOption(latestOpenShift, enteredAmount);
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private void endShift(SQLiteDatabase db, Shift_Master latestOpenShift, BigDecimal shiftEndAmount) {
        ContentValues cvals = new ContentValues();
        cvals.put("SHIFT_STATUS", "C");
        cvals.put("SHIFT_END_AMT", shiftEndAmount.doubleValue());
        db.update("shift_master", cvals, "ID = " + latestOpenShift.ID, null);

        db.setTransactionSuccessful();
        finish();
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    private void giveUserOverrideOption(final Shift_Master latestOpenShift, final BigDecimal enteredAmount) {
        AlertDialog.Builder builderLogin = new AlertDialog.Builder(this);
        View veo = LayoutInflater.from(DeclareShiftMoneyActivity.this).inflate(R.layout.activity_logindialog, null);

        final EditText tusername = (EditText) veo.findViewById(R.id.editTextUsername);
        final EditText tpassword = (EditText) veo.findViewById(R.id.editTextPassword);

        builderLogin.setView(veo);

        builderLogin.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {

                        String u = tusername.getText().toString();
                        String p = tpassword.getText().toString();

                        if (myDb.loginAdminEmployee(u, p)) {
                            SQLiteDatabase db = myDb.getReadableDatabase();

                            db.beginTransaction();
                            try {
                                endShift(db, latestOpenShift, enteredAmount);
                            } catch (SQLiteException e) {
                                e.printStackTrace();
                            } finally {
                                db.endTransaction();
                                db.close();
                            }
                        } else {
                            Toast.makeText(DeclareShiftMoneyActivity.this
                                    , "Authentication failed", Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                }
        );
        builderLogin.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                    }
                }

        );

        final AlertDialog alertLogin = builderLogin.create();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Amount incorrect");
        builder.setMessage("We have detected a different amount, please recalculate.");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                    }
                }

        );
        builder.setNegativeButton("Override",
                new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                        alertLogin.show();
                    }
                }

        );

        AlertDialog alert = builder.create();
        alert.show();
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
