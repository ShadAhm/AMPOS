package com.example.thisi.applicationx.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.thisi.applicationx.model.Customer;
import com.example.thisi.applicationx.model.Detail;
import com.example.thisi.applicationx.model.Employee;
import com.example.thisi.applicationx.model.Header;
import com.example.thisi.applicationx.model.POS_Control;
import com.example.thisi.applicationx.model.Payment;
import com.example.thisi.applicationx.model.Price_Group;
import com.example.thisi.applicationx.model.Product_Master;
import com.example.thisi.applicationx.model.Shift_Master;
import com.example.thisi.applicationx.model.Suspend;

import java.math.BigDecimal;

public class MainActivity 
extends AppCompatActivity {
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewtransactions);
        myDb = DatabaseHelper.getHelper(this);

        // addTransactionsToView();
    }

    // private void addTransactionsToView() {
    //     TableLayout tableLayout = (TableLayout) findViewById(R.id.tableTransactions);
    //     // Add header row
    //     // TableRow rowHeader = createHeaderRow();
    //     // tableLayout.addView(rowHeader);

    //     SQLiteDatabase db = dataHelper.getReadableDatabase();
    //     // Start the transaction.
    //     db.beginTransaction();

    //     try {
    //         String selectQuery = "SELECT * FROM header " +
    //                 "WHERE customer_code = '" + customer_code + "' " +
    //                 "AND trans_date = '" + todaysDateInString + "' ORDER BY TRANS_TIME DESC;";
                    
    //         Cursor cursor = db.rawQuery(selectQuery, null);

    //         int rowCount = cursor.getCount();

    //         if (rowCount > 0) {
    //             int i = 0;

    //             while (cursor.moveToNext()) {
    //                 i++;

    //                 // Read columns data
    //                 int outlet_id = i;
    //                 String customerCode = cursor.getString(cursor.getColumnIndex("CUSTOMER_CODE"));
    //                 String transTime = cursor.getString(cursor.getColumnIndex("TRANS_TIME"));
    //                 String rcpNo = cursor.getString(cursor.getColumnIndex("RCP_NO"));
                    
    //                 // dara rows
    //                 TableRow row = new TableRow(this);

    //                 if (i % 2 == 0) {
    //                     row.setBackgroundColor(Color.parseColor(evenRowColor));
    //                 } else {
    //                     row.setBackgroundColor(Color.parseColor(oddRowColor));
    //                 }

    //                 row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
    //                         TableLayout.LayoutParams.WRAP_CONTENT));

    //                 if(rmColumnValue == null)
    //                     rmColumnValue = "0.0";

    //                 BigDecimal bigDecimalRMColumnValue = new BigDecimal(rmColumnValue);

    //                 DecimalFormat df = new DecimalFormat("0.00");
    //                 df.setMaximumFractionDigits(2);
    //                 String stringRmColumnValue = df.format(bigDecimalRMColumnValue);

    //                 String[] colText = {outlet_id + "", nameColumnValue, stringRmColumnValue, " "};

    //                 int j = 0;
    //                 for (String text : colText) {
    //                     TextView tv = new TextView(this);
    //                     tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
    //                             TableRow.LayoutParams.WRAP_CONTENT));

    //                     if (j == 2) { // RM column align right
    //                         tv.setGravity(Gravity.RIGHT);
    //                     } else {
    //                         tv.setGravity(Gravity.LEFT);
    //                     }

    //                     tv.setTextSize(11);
    //                     tv.setPadding(5, 5, 5, 5);
    //                     tv.setText(text);
    //                     row.addView(tv);

    //                     j++;
    //                 }
    //                 tableLayout.addView(row);
    //             }

    //             cursor.close();

    //             latest_row_after_suspend_inserts = i;
    //         }

    //         db.setTransactionSuccessful();


    //     } catch (SQLiteException e) {
    //         e.printStackTrace();
    //     } finally {
    //         db.endTransaction();
    //         db.close();
    //     }
    // }
}
