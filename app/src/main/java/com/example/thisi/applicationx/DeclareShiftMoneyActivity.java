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

    public void onEnterFloatAmountOK(View view) {


    }


    public void onEnterFloatAmountCancel(View view) {
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
