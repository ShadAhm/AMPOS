package com.example.thisi.applicationx;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by thisi on 10/24/2016.
 */

public class EnterCustomerCodeActivity extends Activity {
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entercustomercode);
        setEnterKeyListenerToProductCodeTextbox();
        myDb = DatabaseHelper.getHelper(this);
    }

    public void onCustomerCodeOK(View view) {
        searchCustomerCode();
    }

    private void searchCustomerCode() {
        EditText customerCodeTextbox = (EditText)findViewById(R.id.textCustomerCode);

        // Edited by Eddie 11/12/2016, add trim()
        String custCode = customerCodeTextbox.getText().toString().trim();

        String price_grp_code = "";
        boolean ccExist = false;

        myDb = DatabaseHelper.getHelper(this);
        SQLiteDatabase db = myDb.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();

        try {
            String selectQuery = "SELECT * FROM customer WHERE CUSTOMER_CODE = '" + custCode + "';";

            Cursor cursor = db.rawQuery(selectQuery, null);

            int rowCount = cursor.getCount();

            if (rowCount > 0) {
                cursor.moveToFirst();
                price_grp_code = cursor.getString(cursor.getColumnIndex("PRICE_GRP_CODE"));
                ccExist = true;
            }

            cursor.close();
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }

        if(!ccExist) {
            showMessage("Error", "Customer does not exist!");
        }
        else {
            finish();
            Intent intent = new Intent(this, OrderActivity.class);
            intent.putExtra("customer_code", custCode);
            intent.putExtra("price_group_code", price_grp_code);
            startActivity(intent);
        }
    }

    public void onCustomerCodeCancel(View view) {
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

    private void setEnterKeyListenerToProductCodeTextbox() {
        EditText textProductCode = (EditText) findViewById(R.id.textCustomerCode);
        textProductCode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_F5) {
                    // barcode scanner
                    searchCustomerCode();
                    return true;
                }
                return false;
            }
        });
    }


}
