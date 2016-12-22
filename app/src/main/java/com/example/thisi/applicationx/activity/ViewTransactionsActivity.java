package com.example.thisi.applicationx.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.graphics.Color;
import android.widget.TextView;
import android.view.Gravity;
import android.database.Cursor;

import com.example.thisi.applicationx.data.DatabaseHelper;
import com.example.thisi.applicationx.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;

    final String headerRowColor = "#00688B";
    final String evenRowColor = "#E0FFFF";
    final String oddRowColor = "#00EEEE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewtransactions);

        initializeVariables();
        addTransactionsToView();
    }

    private void initializeVariables() {
        SharedPreferences prefs = this.getSharedPreferences("com.example.thisi.applicationx", Context.MODE_PRIVATE);
        dataHelper = DatabaseHelper.getHelper(this);
    }

    private void addTransactionsToView() {
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableTransactions);
        // Add header row
        // TableRow rowHeader = createHeaderRow();
        // tableLayout.addView(rowHeader);

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();

        try {
            String todaysDateInString = new SimpleDateFormat("yyyyMMdd").format(new Date());

            String selectQuery = "SELECT * FROM header " +
                    "WHERE trans_date = '" + todaysDateInString + // get today's transactions only
                    "' ORDER BY TRANS_TIME DESC;";
                    
            Cursor cursor = db.rawQuery(selectQuery, null);

            int rowCount = cursor.getCount();

            if (rowCount > 0) {
                int i = 0;

                while (cursor.moveToNext()) {
                    i++;

                    // Read columns data
                    int outlet_id = i;
                    String customerCode = cursor.getString(cursor.getColumnIndex("CUSTOMER_CODE"));
                    String transTime = cursor.getString(cursor.getColumnIndex("TRANS_TIME"));
                    String rcpNo = cursor.getString(cursor.getColumnIndex("RCP_NO"));
                    
                    // dara rows
                    TableRow row = new TableRow(this);

                    if (i % 2 == 0) {
                        row.setBackgroundColor(Color.parseColor(evenRowColor));
                    } else {
                        row.setBackgroundColor(Color.parseColor(oddRowColor));
                    }

                    row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    String[] colText = { transTime, customerCode, rcpNo, "Reprint"};

                    for (String text : colText) {
                        TextView tv = new TextView(this);
                        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));

                        tv.setTextSize(11);
                        tv.setPadding(5, 5, 5, 5);
                        tv.setText(text);
                        row.addView(tv);
                    }
                    tableLayout.addView(row);
                }
                cursor.close();


            }
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }
}
