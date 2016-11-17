package com.example.thisi.applicationx;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

public class DeclareShiftMoneyActivity extends Activity {
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entercustomercode);

        myDb = DatabaseHelper.getHelper(this);

        addFilterToTextBox();
    }

   private void addFilterToTextBox() {
        EditText txtPayment = (EditText) findViewById(R.id.textDeclareShiftMoney);
        txtPayment.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10, 2)});
    }

    public void onDeclareShiftMoneyOK(View view) {
        SQLiteDatabase db = mydb.getReadableDatabase();

        db.beginTransaction();
        try {
            String todaysDateInString = new SimpleDateFormat("yyyyMMdd").format(new Date());
            Shift_Master latestOpenShift = mydb.lookForOpenShiftsAtDate(db, todaysDateInString);

            if(latestOpenShift == null)
            {
                showMessage("This shouldn't happen", "This shouldn't happen");
                return; // this shouldnt actually happen
            }

            EditText textDeclareShiftMoney = (EditText) findViewById(R.id.textDeclareShiftMoney);
            if (textDeclareShiftMoney.getText() == null || textDeclareShiftMoney.getText().toString() == null || textDeclareShiftMoney.getText().toString().isEmpty())
                return;

            String floatAmount = textDeclareShiftMoney.getText().toString();

            BigDecimal bd = new BigDecimal(floatAmount);

            SharedPreferences prefs = this.getSharedPreferences("com.example.thisi.applicationx", Context.MODE_PRIVATE);
            String posNo = _sp.getString("posnumber", "errorUndefined");
            String companyCode = _sp.getString("companycode", "errorUndefined");

            Shift_Master newShift = new Shift_Master();
            newShift.COMPANY_CODE = companyCode; 
            newShift.OUTLET_CODE = "TODOFINDOUTWHERETHISCOMEFROM"; 
            newShift.POS_NO = posNo; 
            newShift.BUS_DATE = todaysDateInString; 
            newShift.SHIFT_NUMBER = newShiftNo; 
            newShift.SHIFT_STATUS = "O"; 
            newShift.SHIFT_START_AMT = bd; 
            newShift.SHIFT_END_AMT = new BigDecimal("0.00"); 

            mydb.insertShiftMaster(db, newShift); 

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
