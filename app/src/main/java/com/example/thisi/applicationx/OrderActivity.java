package com.example.thisi.applicationx;

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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class OrderActivity extends Activity {
    private static String customer_code;
    private static String price_group_code;
    private static int latest_row_after_suspend_inserts = 0;
    private static String default_price_field;

    private static String posNo;
    private static String companyCode;

    final String headerRowColor = "#00688B";
    final String evenRowColor = "#E0FFFF";
    final String oddRowColor = "#00EEEE";

    CrossableProducts crossableProducts; 

    DatabaseHelper dataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        setEnterKeyListenerToProductCodeTextbox();

        Bundle b = getIntent().getExtras();
        initializeVariables(b); 

        addSuspendProductsToView();

        String newProductsCodes = b.getString("new_products_codes");
        if(newProductsCodes != null && newProductsCodes != "") {
            crossableProducts.addSemicolonDelimitedToList(newProductsCodes); 
            addNewProductMastersToView();
        }

        ninePLUs();
    }

    private void initializeVariables(Bundle b) {
        this.crossableProducts = new CrossableProducts(); 

        this.customer_code = b.getString("customer_code");
        this.price_group_code = b.getString("price_group_code");

        SharedPreferences prefs = this.getSharedPreferences("com.example.thisi.applicationx", Context.MODE_PRIVATE);
        default_price_field = prefs.getString("defaultprice", "PRICE_01");
        posNo = prefs.getString("posnumber", null);    
        companyCode = prefs.getString("companycode", null);

        latest_row_after_suspend_inserts = 0; 

        dataHelper = DatabaseHelper.getHelper(this);

        if(posNo == null || posNo.isEmpty()) {
            onOrderErrorCantContinue(errorCantContinueType.POS);
        }
        else if (companyCode == null || companyCode.isEmpty()) {
            onOrderErrorCantContinue(errorCantContinueType.COMCODE);
        }
        else if (default_price_field == null || default_price_field.isEmpty()) {
            onOrderErrorCantContinue(errorCantContinueType.DEFAULTPRICE); 
        }
    }

    private void onOrderErrorCantContinue(errorCantContinueType errType) {
        Button buttonPayment = (Button) findViewById(R.id.buttonPayment);
        buttonPayment.setEnabled(false);

        String whatsNotGood = null; 

        switch(errType) {
            case POS : whatsNotGood = "POS number"; 
                break; 
            case COMCODE : whatsNotGood = "Company Code";
                break;
            case DEFAULTPRICE : whatsNotGood = "Default Price";
                break;
        }

        showMessage("Payment Disabled", "Proceed to Payment has been disabled due to incomplete settings. Go to Settings then select a " + whatsNotGood); 
    }

    private enum errorCantContinueType {
        POS, 
        COMCODE,
        DEFAULTPRICE
    }

    private void addSuspendProductsToView() {
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tablelayout);
        // Add header row
        TableRow rowHeader = createHeaderRow();
        tableLayout.addView(rowHeader);

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();

        try {
            String selectQuery = "SELECT suspend.prod_name, price_group.price, customer.customer_code, product_master." + default_price_field + " FROM suspend " +
                    "LEFT JOIN price_group ON suspend.price_grp_code = price_group.price_grp_code " +
                    "AND suspend.prod_code = price_group.prod_code " +
                    "LEFT JOIN customer ON suspend.customer_code = customer.customer_code " +
                    "JOIN product_master ON suspend.prod_code = product_master.prod_code " +
                    "WHERE customer.customer_code = '" + customer_code + "';";

            Cursor cursor = db.rawQuery(selectQuery, null);

            int rowCount = cursor.getCount();

            if (rowCount > 0) {
                int i = 0;

                while (cursor.moveToNext()) {
                    i++;

                    // Read columns data
                    int outlet_id = i; 
                    String nameColumnValue = cursor.getString(cursor.getColumnIndex("PROD_NAME"));
                    String rmColumnValue = cursor.getString(cursor.getColumnIndex("PRICE"));

                    if (rmColumnValue == null || rmColumnValue.isEmpty())
                        rmColumnValue = cursor.getString(cursor.getColumnIndex(default_price_field));

                    // dara rows
                    TableRow row = new TableRow(this);

                    if (i % 2 == 0) {
                        row.setBackgroundColor(Color.parseColor(evenRowColor));
                    } else {
                        row.setBackgroundColor(Color.parseColor(oddRowColor));
                    }
                    row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    if(rmColumnValue == null)
                        rmColumnValue = "0.0";

                    BigDecimal bigDecimalRMColumnValue = new BigDecimal(rmColumnValue);

                    DecimalFormat df = new DecimalFormat("0.00");
                    df.setMaximumFractionDigits(2);
                    String stringRmColumnValue = df.format(bigDecimalRMColumnValue);

                    String[] colText = {outlet_id + "", nameColumnValue, stringRmColumnValue, " "};

                    int j = 0;
                    for (String text : colText) {
                        TextView tv = new TextView(this);
                        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));

                        if (j == 2) { // RM column align right
                            tv.setGravity(Gravity.RIGHT);
                        } else {
                            tv.setGravity(Gravity.LEFT);
                        }

                        tv.setTextSize(11);
                        tv.setPadding(5, 5, 5, 5);
                        tv.setText(text);
                        row.addView(tv);

                        j++;
                    }
                    tableLayout.addView(row);
                }

                cursor.close();

                latest_row_after_suspend_inserts = i;
            }

            db.setTransactionSuccessful();


        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private void addNewProductMastersToView() {
        for(String prodCode: crossableProducts.productCodes) {
            searchAndAddProductCode(prodCode); 
        }
    }

    private void setEnterKeyListenerToProductCodeTextbox() {
        EditText textProductCode = (EditText) findViewById(R.id.textProductCode);
        textProductCode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    // enter key pressed
                    searchProductCodeFromTextbox(false);
                    return true;
                } else if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_F5) {
                    // barcode scanner
                    searchProductCodeFromTextbox(true);
                    return true;
                }
                return false;
            }
        });
    }

    private void searchProductCodeFromTextbox(boolean isBarcodeReader) {
        EditText textProductCode = (EditText) findViewById(R.id.textProductCode);
        String productcode = textProductCode.getText().toString();

        textProductCode.setText(null);

        if (isBarcodeReader) {
            productcode = productcode.replaceAll("\\n", "");
            productcode = productcode.replaceAll("\\r", "");
        }

        searchAndAddProductCode(productcode);
    }

    private void searchAndAddProductCode(String productcode) {
        if(productcode == null || productcode == "") {
            return;
        }

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();

        try {
            String selectQuery = "SELECT * FROM product_master WHERE prod_code = '" + productcode + "';";

            Cursor cursor = db.rawQuery(selectQuery, null);
            int rowCount = cursor.getCount();
            cursor.close();

            if (rowCount > 0) {
                addProductMasterToView(productcode, db);
            } else {
                showMessage("Error", "Product \"" + productcode + "\" does not exist");
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private void addProductMasterToView(final String productCode, SQLiteDatabase db) {
        final TableLayout tableLayout = (TableLayout) findViewById(R.id.tablelayout);

        if(crossableProducts == null) {
            crossableProducts = new CrossableProducts();
        }

        crossableProducts.addToList(productCode);

        String selectFromPriceGroup = "SELECT product_master.prod_name, price_group.price, customer.customer_code, product_master." + default_price_field + " FROM product_master " +
                "LEFT JOIN price_group ON price_group.prod_code = product_master.prod_code " +
                "LEFT JOIN customer ON customer.price_grp_code = price_group.price_grp_code " +
                "WHERE product_master.prod_code = '" + productCode + "'";

        Cursor cursor1 = db.rawQuery(selectFromPriceGroup, null);
        int cursor1RowCount = cursor1.getCount();

        // dara rows
        TableRow row = new TableRow(this);

        if (cursor1RowCount > 0) {
            cursor1.moveToFirst();
            latest_row_after_suspend_inserts++;
            String nameColumnValue = cursor1.getString(cursor1.getColumnIndex("PROD_NAME"));
            String rmColumnValue = cursor1.getString(cursor1.getColumnIndex("PRICE"));

            if (rmColumnValue == null || rmColumnValue.isEmpty())
                rmColumnValue = cursor1.getString(cursor1.getColumnIndex(default_price_field));

            cursor1.close();

            if (latest_row_after_suspend_inserts % 2 == 0) {
                row.setBackgroundColor(Color.parseColor(evenRowColor));
            } else {
                row.setBackgroundColor(Color.parseColor(oddRowColor));
            }

            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            if(rmColumnValue == null)
                rmColumnValue = "0.0";

            BigDecimal bigDecimalRMColumnValue = new BigDecimal(rmColumnValue);

            DecimalFormat df = new DecimalFormat("0.00");
            df.setMaximumFractionDigits(2);
            String stringRmColumnValue = df.format(bigDecimalRMColumnValue);

            String[] colText = {latest_row_after_suspend_inserts + "", nameColumnValue, stringRmColumnValue, "X"};

            int i = 0;
            for (String text : colText) {
                TextView tv = new TextView(this);
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                if (i == 2) { // RM column align right
                    tv.setGravity(Gravity.RIGHT);
                } else if (i == 3) // X column align centre
                {
                    tv.setGravity(Gravity.CENTER);
                } else {
                    tv.setGravity(Gravity.LEFT);
                }

                tv.setTextSize(11);
                tv.setPadding(5, 5, 5, 5);
                tv.setText(text);

                row.addView(tv);

                if (text == "X") {
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeProductFromView(productCode);
                        }
                    });
                }

                i++;
            }
        }
        tableLayout.addView(row);
    }

    private void removeProductFromView(String productCode) {
        newProductsCodes = newProductsCodes.replace(productCode + ";", "");

        reDrawTable();
    }

    private void reDrawTable() {
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tablelayout);

        tableLayout.removeAllViews();

        addSuspendProductsToView();
        if(newProductsCodes != null && newProductsCodes != "") {
            addNewProductMastersToView();
        }
    }

    private TableRow createHeaderRow() {
        TableRow rowHeader = new TableRow(this);
        rowHeader.setBackgroundColor(Color.parseColor(headerRowColor));

        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        String[] headerText = {"#", "Name", "RM", ""};

        int i = 0;
        for (String c : headerText) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            if (i == 2) { // RM column align right
                tv.setGravity(Gravity.RIGHT);
            } else {
                tv.setGravity(Gravity.LEFT);
            }

            tv.setTextSize(13);
            tv.setPadding(5, 5, 5, 5);
            tv.setTextColor(Color.WHITE);
            tv.setText(c);
            rowHeader.addView(tv);

            i++;
        }
        return rowHeader;
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.show();
    }

    public void onCancelOrder(View view) {
        cancelOrder();
    }

    private void cancelOrder() {
        finish();

        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    public void onPaymentClick(View view) {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("customer_code", customer_code);
        intent.putExtra("newproductscodes", crossableProducts.toSemicolonDelimited());
        intent.putExtra("price_grp_code", price_group_code);

        finish();
        startActivity(intent);
    }

    private void ninePLUs() {
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();

        try {
            String selectQuery = "SELECT * FROM product_master LIMIT 9;";

            Cursor cursor = db.rawQuery(selectQuery, null);

            int rowCount = cursor.getCount();

            if (rowCount > 0) {
                int i = 0;

                while (cursor.moveToNext()) {
                    final String nameColumnValue = cursor.getString(cursor.getColumnIndex("PROD_CODE"));

                    String buttonID = "buttonPLU" + i;
                    int resID = getResources().getIdentifier(buttonID, "id", getPackageName());

                    Button btn = (Button)findViewById(resID);

                    String btnText = "";
                    if(nameColumnValue.length() > 6) {
                        btnText = nameColumnValue.substring(0, 6) + "..";
                    }
                    else {
                        btnText = nameColumnValue;
                    }

                    btn.setText(btnText);

                    btn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            EditText eok = (EditText)findViewById(R.id.textProductCode);
                            eok.setText(nameColumnValue);
                            searchProductCodeFromTextbox(false);
                        }
                    });

                    ++i;
                }
            }

            cursor.close();
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private class CrossableProducts {
        public ArrayList<String> productCodes;

        public CrossableProducts() {
            productCodes = new ArrayList<String>();
        }

        public void addToList(String productCode) {
            productCodes.add(productCode);
        }

        public void removeOneFromList(String productCode) {
            for (Iterator<String> iterator = this.productCodes.iterator(); iterator.hasNext();) {
                String currentProdCode = iterator.next();
                if (currentProdCode == productCode) {
                    iterator.remove();
                    return;
                }
            }
        }

        public void addSemicolonDelimitedToList(String productCodes) {
            String[] newProductsCodesArray = newProductsCodes.split(";");

            for (int i = 0; i < newProductsCodesArray.length; i++) {
                productCodes.add(newProductsCodesArray[i]);
            }
        }

        public String toSemicolonDelimited() {
            return "";
        }
    }
}
