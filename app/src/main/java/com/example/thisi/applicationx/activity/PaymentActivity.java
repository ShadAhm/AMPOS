package com.example.thisi.applicationx.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thisi.applicationx.printutil.ApplicationContext;
import com.example.thisi.applicationx.data.DatabaseHelper;
import com.example.thisi.applicationx.util.DecimalDigitsInputFilter;
import com.example.thisi.applicationx.printutil.DeviceControl;
import com.example.thisi.applicationx.model.POS_Control;
import com.example.thisi.applicationx.model.Price_Group;
import com.example.thisi.applicationx.model.Product_Master;
import com.example.thisi.applicationx.R;
import com.example.thisi.applicationx.model.Shift_Master;
import com.example.thisi.applicationx.model.Suspend;
import com.example.thisi.applicationx.model.Customer;
import com.example.thisi.applicationx.model.Detail;
import com.example.thisi.applicationx.printutil.preDefiniation;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by thisi on 10/28/2016.
 */

public class PaymentActivity extends Activity {
    private static String customer_code;
    private static String newProductsCodes;
    private static String default_price_field;
    private static String price_grp_code;
    private static BigDecimal total;
    private static BigDecimal changeAmount;
    private static Boolean isPaymentComplete;
    private static ArrayList<PaymentMade> paymentsMade;

    private static String thisRcpId;

    // from settings :
    private static String companyCode;
    private static String posNo;
    private static String outletCode;
    private static String empCode;
    private static String empName;

    // printer stuff
    public ApplicationContext context;
    public int state;
    public boolean mBconnect = false;
    DatabaseHelper dataHelper;
    private DeviceControl DevCtrl;
    private boolean isTT43 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Bundle b = getIntent().getExtras();
        initializeVariables(b);

        addFilterToTextBox();

        setEnterKeyListenerToPaymentTextbox();

        calculateTotal();
    }

    private void initializeVariables(Bundle b) {
        context = (ApplicationContext) getApplicationContext();

        customer_code = b.getString("customer_code");
        newProductsCodes = b.getString("newproductscodes");
        price_grp_code = b.getString("price_grp_code");

        total = new BigDecimal("0.0");

        paymentsMade = new ArrayList<PaymentMade>();

        thisRcpId = null;

        isPaymentComplete = false;

        SharedPreferences prefs = this.getSharedPreferences("com.example.thisi.applicationx", Context.MODE_PRIVATE);
        default_price_field = prefs.getString("defaultprice", "PRICE_01");
        posNo = prefs.getString("posnumber", null);
        companyCode = prefs.getString("companycode", null);
        outletCode = prefs.getString("outletcode", null);
        empCode = prefs.getString("empcode", "EMPNULL");
        empName = prefs.getString("empname", "EMPLOYEENULL");
    }

    private void setEnterKeyListenerToPaymentTextbox() {
        final EditText editTextPayment = (EditText) findViewById(R.id.editTextPayment);
        editTextPayment.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    // enter key pressed
                    onConfirm(null);

                    return true;
                }
                return false;
            }
        });
    }

    private void addFilterToTextBox() {
        EditText txtPayment = (EditText) findViewById(R.id.editTextPayment);
        txtPayment.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10, 2)});
    }

    private void calculateTotal() {
        dataHelper = DatabaseHelper.getHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();

        try {
            String selectQuery = "SELECT suspend.prod_name, suspend.total_price, customer.customer_code, product_master." + default_price_field + " FROM suspend " +
                    "LEFT JOIN price_group ON suspend.price_grp_code = price_group.price_grp_code " +
                    "AND suspend.prod_code = price_group.prod_code " +
                    "LEFT JOIN customer ON suspend.customer_code = customer.customer_code " +
                    "JOIN product_master ON suspend.prod_code = product_master.prod_code " +
                    "WHERE customer.customer_code = '" + customer_code + "';";

            Cursor cursor = db.rawQuery(selectQuery, null);

            int rowCount = cursor.getCount();

            if (rowCount > 0) {
                while (cursor.moveToNext()) {

                    String rmColumnValue = cursor.getString(cursor.getColumnIndex("TOTAL_PRICE"));

                    if (rmColumnValue == null || rmColumnValue.isEmpty())
                        rmColumnValue = cursor.getString(cursor.getColumnIndex(default_price_field));

                    BigDecimal priceInRM = new BigDecimal(rmColumnValue);

                    total = total.add(priceInRM);
                }
            }

            if (newProductsCodes != null && !newProductsCodes.isEmpty()) {
                String[] newProductsCodesArray = newProductsCodes.split(";");

                for (String newProdCode : newProductsCodesArray) {
                    String selectFromPriceGroup =
                            "SELECT product_master.prod_name, price_group.price, customer.customer_code, product_master." + default_price_field + " FROM product_master   " +
                                    "LEFT JOIN customer ON customer.price_grp_code = price_group.price_grp_code   " +
                                    "LEFT JOIN price_group ON price_group.prod_code = product_master.prod_code   " +
                                    "WHERE product_master.prod_code = '" + newProdCode + "' " +
                                    "AND customer.customer_code = '" + customer_code + "' ";

                    Cursor cursor1 = db.rawQuery(selectFromPriceGroup, null);
                    int cursor1RowCount = cursor1.getCount();

                    if (cursor1RowCount > 0) {
                        cursor1.moveToFirst();

                        String rmColumnValue = cursor1.getString(cursor1.getColumnIndex("PRICE"));

                        if (rmColumnValue == null || rmColumnValue.isEmpty())
                            rmColumnValue = cursor1.getString(cursor1.getColumnIndex(default_price_field));

                        BigDecimal priceInRM = new BigDecimal(rmColumnValue);

                        total = total.add(priceInRM);
                    }
                }
            }

            db.setTransactionSuccessful();

            DecimalFormat df = new DecimalFormat("0.00");
            df.setMaximumFractionDigits(2);
            String stringTotalPrice = df.format(total);

            TextView textViewTotal = (TextView) findViewById(R.id.textViewTotal);

            textViewTotal.setText(stringTotalPrice);

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void onBackClick(View view) {
        cancelOrder();
    }

    private void cancelOrder() {
        finish();
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra("customer_code", customer_code);
        intent.putExtra("new_products_codes", newProductsCodes);

        newProductsCodes = "";

        startActivity(intent);
    }

    public void onHoldOrder(View view) {
        holdOrderNew();
    }

    private void holdOrderNew() {
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        try {
            if (newProductsCodes != null && !newProductsCodes.isEmpty()) {
                String[] newProductsCodesArray = newProductsCodes.split(";");

                String todaysDateInString = new SimpleDateFormat("yyyyMMdd").format(new Date());
                String todaysTimeInString = new SimpleDateFormat("HHmm").format(new Date());

                Shift_Master currentShift = dataHelper.lookForOpenShiftsAtDate(db, todaysDateInString);
                Customer customer = dataHelper.getCustomerByCustomerCode(db, customer_code);

                for (int i = 0; i < newProductsCodesArray.length; i++) {
                    Product_Master productMastar = dataHelper.getProductByProductCode(db, newProductsCodesArray[i]);
                    Price_Group priceGrp = dataHelper.getPriceGrpByProductAndCustomerCode(db, this.customer_code, newProductsCodesArray[i]);

                    BigDecimal priice = new BigDecimal("0.0");
                    if(priceGrp == null) {
                        switch (default_price_field) {
                            case "PRICE_01":  priice = productMastar.PRICE_01;
                                break;
                            case "PRICE_02":  priice = productMastar.PRICE_02;
                                break;
                            case "PRICE_03":  priice = productMastar.PRICE_03;
                                break;
                            case "PRICE_04":  priice = productMastar.PRICE_04;
                                break;
                            case "PRICE_05":  priice = productMastar.PRICE_05;
                                break;
                            case "PRICE_06":  priice = productMastar.PRICE_06;
                                break;
                            case "PRICE_07":  priice = productMastar.PRICE_07;
                                break;
                            case "PRICE_08":  priice = productMastar.PRICE_08;
                                break;
                            case "PRICE_09":  priice = productMastar.PRICE_09;
                                break;
                            case "PRICE_10":  priice = productMastar.PRICE_10;
                                break;
                        }
                    }
                    else {
                        if(priceGrp.PRICE != null) {
                            priice = priceGrp.PRICE;
                        }
                    }

                    BigDecimal tax = new BigDecimal("0.0");
                    if(productMastar.TAX_01) {
                        if(productMastar.TAXCODE.equalsIgnoreCase("SR")) {
                            BigDecimal taxRate = new BigDecimal("0.06");
                            tax = priice.multiply(taxRate).setScale(2, RoundingMode.CEILING);
                        }
                        else if (productMastar.TAXCODE.equalsIgnoreCase("ZRL")) {
                            // 0
                        }
                    }

                    Suspend sus = new Suspend();
                    sus.COMPANY_CODE = this.companyCode;
                    sus.OUTLET_CODE = this.outletCode;
                    sus.POS_NO = this.posNo;
                    sus.SHIFT_NO = Integer.toString(currentShift.SHIFT_NUMBER);
                    sus.RCP_NO = null;
                    sus.TRANS_TYPE = "S";
                    sus.BUS_DATE = todaysDateInString;
                    sus.TRANS_DATE = todaysDateInString;
                    sus.TRANS_TIME = todaysTimeInString;
                    sus.ROW_NUMBER = i + 1;
                    sus.PROD_CODE = productMastar.PROD_CODE;
                    sus.BARCODE = productMastar.BARCODE;
                    sus.PROD_NAME = productMastar.PROD_NAME;
                    sus.PROD_SHORT_NAME = productMastar.PRODUCT_SHORT_NAME;
                    sus.PROD_TYPE_CODE = productMastar.PROD_TYPE_CODE;
                    sus.USAGE_UOM = productMastar.USAGE_UOM;
                    sus.UOM_CONV = BigDecimal.ZERO;
                    sus.QUANTITY = new BigDecimal("1");
                    sus.PRICE_LVL_CODE = null;
                    sus.UNIT_PRICE = priice; // we recalculate this next time
                    sus.TOTAL_PRICE = priice;
                    sus.BOM_PARENT = null;
                    sus.TAX_01 = tax;
                    sus.TAX_02 = BigDecimal.ZERO;
                    sus.TAX_03 = BigDecimal.ZERO;
                    sus.TAX_04 = BigDecimal.ZERO;
                    sus.TAX_05 = BigDecimal.ZERO;
                    sus.ALLOW_DISC = productMastar.ALLOW_DISC;
                    sus.MULTIPLE_DISC = productMastar.MULTIPLE_DISC;
                    sus.DISCOUNT_CODE = null;
                    sus.ITEM_DISCOUNT_AMOUNT = BigDecimal.ZERO;
                    sus.TOTAL_DICOUNT_CODE = null;
                    sus.TOTAL_DISCOUNT_AMOUNT = BigDecimal.ZERO;
                    sus.TICKET_SURCHARGE = BigDecimal.ZERO;
                    sus.STAFF_DISCOUNT_CODE = productMastar.STAFF_DISCOUNT_CODE;
                    sus.STAFF_DISCOUNT = BigDecimal.ZERO;
                    sus.PRICE_GRP_CODE = customer.PRICE_GRP_CODE;
                    sus.SUSPEND_NUMBER = null;
                    sus.IsRECALL = false;
                    sus.IS_UPSALES = false;
                    sus.UPSALES_CONV = BigDecimal.ZERO;
                    sus.IS_MULTIPLEUOM = false;
                    sus.RECALL_BY = null;
                    sus.APPROVE_BY = null;
                    sus.MODIFIED_DATE = todaysDateInString;
                    sus.MODIFIED_ID = null;
                    sus.CUSTOMER_CODE = this.customer_code;
                    sus.TAXCODE = productMastar.TAXCODE;
                    sus.COST = productMastar.COST;
                    sus.PRICE_GRP_CODE = null;
                    sus.TABLE_NO = null;
                    sus.PROMOSOURCECODE = null;
                    sus.PROMOCHANGEPRICE = BigDecimal.ZERO;

                    dataHelper.insertSuspend(db, sus);
                }

                db.beginTransaction();
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }

        String successMessage = "Order for customer " + customer_code + " put on hold";

        Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();

        finish();
        Intent intent = new Intent(this, EnterCustomerCodeActivity.class);

        newProductsCodes = null;
        price_grp_code = null;
        customer_code = null;

        startActivity(intent);
    }

    public void onConfirm(View view) {
        if (!isPaymentComplete) {
            RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroupPaymentMethod);

            int radioButtonID = rg.getCheckedRadioButtonId();
            View radioButton = rg.findViewById(radioButtonID);
            int idx = rg.indexOfChild(radioButton); // 0 - cash, 1 - credit card

            EditText editTextPayment = (EditText) findViewById(R.id.editTextPayment);

            if (editTextPayment.getText().toString() == "" || editTextPayment.getText().toString() == null || editTextPayment.getText().toString().isEmpty())
                return;

            String amountPaid = editTextPayment.getText().toString();

            BigDecimal bd = new BigDecimal(amountPaid);

            PaymentMade pm = new PaymentMade(idx, bd);

            if (paymentsMade == null) {
                paymentsMade = new ArrayList<PaymentMade>();
            }

            paymentsMade.add(pm);

            TextView textViewAmtPaid = (TextView) findViewById(R.id.textViewPaidAmt);

            BigDecimal cumPayment = getCumulativeTotalPaymentsMade();

            DecimalFormat df = new DecimalFormat("0.00");
            df.setMaximumFractionDigits(2);
            String stringAmtPaid = df.format(cumPayment);

            textViewAmtPaid.setText(stringAmtPaid);
            textViewAmtPaid.setBackgroundColor(Color.parseColor("#99ff99"));

            if (cumPayment.compareTo(total) >= 0) { // payments made more than total
                editTextPayment.setEnabled(false);

                Button btnHoldOrder = (Button) findViewById(R.id.buttonHoldOrder);
                btnHoldOrder.setEnabled(false);

                Button buttonBack = (Button) findViewById(R.id.buttonBack);
                buttonBack.setEnabled(false);

                for (int i = 0; i < rg.getChildCount(); i++) {
                    rg.getChildAt(i).setEnabled(false);
                    rg.getChildAt(i).setSelected(false);
                }

                changeAmount = cumPayment.subtract(total);

                TextView textViewChange = (TextView) findViewById(R.id.textViewChange);
                textViewChange.setText(df.format(changeAmount));

                textViewChange.setBackgroundColor(Color.parseColor("#99ff99"));

                isPaymentComplete = true;
            }

            editTextPayment.setText(null);
        } else {
            insertPaymentsIntoDb();

            Toast.makeText(this, "Payment complete", Toast.LENGTH_SHORT).show();

            Button btnHoldOrder = (Button) findViewById(R.id.buttonHoldOrder);
            btnHoldOrder.setVisibility(View.GONE);

            Button btnPrintReceipt = (Button)findViewById(R.id.buttonPrintReceipt);
            btnPrintReceipt.setVisibility(View.VISIBLE);

            LinearLayout lap = (LinearLayout)findViewById(R.id.linearLayoutConfirmAndCancel);
            lap.setVisibility(View.GONE);

            Button btnContinue = (Button)findViewById(R.id.buttonContinue);
            btnContinue.setVisibility(View.VISIBLE);
        }
    }

    public void onPrintReceipt(View view) {
        printReceipt();
    }

    public void onContinue(View view) {
        finish();
        Intent intent = new Intent(this, EnterCustomerCodeActivity.class);
        startActivity(intent);
    }

    private void insertPaymentsIntoDb() {
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            String todaysDateInString = new SimpleDateFormat("yyyyMMdd").format(new Date());
            Shift_Master currentShift = dataHelper.lookForOpenShiftsAtDate(db, todaysDateInString);

            String rcp_id = insertHeader(db, currentShift.SHIFT_NUMBER);

            if(!rcp_id.equalsIgnoreCase("noposcontrol")) {
                insertDetailFromSuspend(db, rcp_id);

                int rowNumStart = dataHelper.getLatestRownumberFromDetail(db, rcp_id);

                insertDetailFromProducts(db, rcp_id, currentShift.SHIFT_NUMBER, rowNumStart);
                insertPayment(db, rcp_id, currentShift.SHIFT_NUMBER);
                updateHeader(db, rcp_id);
                db.delete("suspend", "customer_code == '" + customer_code + "'", null);
            }

            db.setTransactionSuccessful();

            this.thisRcpId = rcp_id;
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            db.close();
        }
    }

    private void updateHeader(SQLiteDatabase db, String rcp_id) {
        Cursor res = db.rawQuery("SELECT TOTAL(TOTAL_PRICE) as TOTALPRICE, TOTAL(TAX_01) as TOTALTAX FROM DETAIL WHERE RCP_NO = '" + rcp_id + "';", null);

        String totPrice = "0.01";
        String totTax = "0.01";
        if (res.getCount() > 0) {
            res.moveToFirst();

            totPrice = res.getString(res.getColumnIndex("TOTALPRICE"));
            totTax = res.getString(res.getColumnIndex("TOTALTAX"));

            res.close();
        }

        String ssql0 = "UPDATE HEADER SET SALES_AMOUNT = " + totPrice + " WHERE RCP_NO = '" + rcp_id + "'";
        String ssql1 = "UPDATE HEADER SET TOTAL_TAX = " + totTax + " WHERE RCP_NO = '" + rcp_id + "'";

        db.execSQL(ssql0);
        db.execSQL(ssql1);
    }

    private void insertDetailFromProducts(SQLiteDatabase db, String rcp_id, int shift_number, int rowNumStart) {
        if (newProductsCodes != null && !newProductsCodes.isEmpty()) {
            String[] newProductsCodesArray = newProductsCodes.split(";");

            String todaysDateInString = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String todaysTimeInString = new SimpleDateFormat("HHmm").format(new Date());

            for (int i = 0; i < newProductsCodesArray.length; i++) {
                int rowNum = rowNumStart + i + 1;

                Product_Master productMastar = dataHelper.getProductByProductCode(db, newProductsCodesArray[i]);
                Price_Group priceGrp = dataHelper.getPriceGrpByProductAndCustomerCode(db, this.customer_code, newProductsCodesArray[i]);

                BigDecimal priice = new BigDecimal("0.0");
                if(priceGrp == null) {
                    switch (default_price_field) {
                        case "PRICE_01":  priice = productMastar.PRICE_01;
                            break;
                        case "PRICE_02":  priice = productMastar.PRICE_02;
                            break;
                        case "PRICE_03":  priice = productMastar.PRICE_03;
                            break;
                        case "PRICE_04":  priice = productMastar.PRICE_04;
                            break;
                        case "PRICE_05":  priice = productMastar.PRICE_05;
                            break;
                        case "PRICE_06":  priice = productMastar.PRICE_06;
                            break;
                        case "PRICE_07":  priice = productMastar.PRICE_07;
                            break;
                        case "PRICE_08":  priice = productMastar.PRICE_08;
                            break;
                        case "PRICE_09":  priice = productMastar.PRICE_09;
                            break;
                        case "PRICE_10":  priice = productMastar.PRICE_10;
                            break;
                    }
                }
                else {
                    if(priceGrp.PRICE != null) {
                        priice = priceGrp.PRICE;
                    }
                }

                BigDecimal tax = new BigDecimal("0.0");
                if(productMastar.TAX_01) {
                    if(productMastar.TAXCODE.equalsIgnoreCase("SR")) {
                        BigDecimal taxRate = new BigDecimal("0.06");
                        tax = priice.multiply(taxRate).setScale(2, RoundingMode.CEILING);
                    }
                    else if (productMastar.TAXCODE.equalsIgnoreCase("ZRL")) {
                        // 0
                    }
                }

                Detail detl = new Detail();
                detl.COMPANY_CODE = companyCode;
                detl.OUTLET_CODE = outletCode;
                detl.EMP_CODE = empCode;
                detl.POS_NO = posNo;
                detl.SHIFT_NO = Integer.toString(shift_number);
                detl.RCP_NO = rcp_id;
                detl.TRANS_TYPE = "S";
                detl.BUS_DATE = todaysDateInString;
                detl.TRANS_DATE = todaysDateInString;
                detl.TRANS_TIME = todaysTimeInString;
                detl.ROW_NUMBER = rowNum;
                detl.PROD_CODE = productMastar.PROD_CODE;
                detl.PROD_NAME = productMastar.PROD_NAME;
                detl.PROD_TYPE_CODE = productMastar.PROD_TYPE_CODE;
                detl.USAGE_UOM = productMastar.USAGE_UOM;
                detl.QUANTITY = BigDecimal.ONE;
                detl.UOM_CONV = BigDecimal.ONE;
                detl.PRICE_LVL_CODE = null;
                detl.UNIT_PRICE = priice;
                detl.TOTAL_PRICE = priice;
                detl.TAX_01 = tax;
                detl.TAX_02 = BigDecimal.ZERO;
                detl.TAX_03 = BigDecimal.ZERO;
                detl.TAX_04 = BigDecimal.ZERO;
                detl.TAX_05 = BigDecimal.ZERO;
                detl.DISCOUNT_CODE = null;
                detl.ITEM_DISCOUNT_AMOUNT = BigDecimal.ZERO;
                detl.TOTAL_DISCOUNT_CODE = null;
                detl.TOTAL_DISCOUNT_AMOUNT = BigDecimal.ZERO;
                detl.TICKET_SURCHARGE = BigDecimal.ZERO;
                detl.STAFF_DISCOUNT_CODE = productMastar.STAFF_DISCOUNT_CODE;
                detl.STAFF_DISCOUNT = BigDecimal.ZERO;
                detl.BARCODE = productMastar.BARCODE;
                detl.TAXCODE = productMastar.TAXCODE;
                detl.COST = productMastar.COST;
                detl.ToSAP = false;

                dataHelper.insertDetail(db, detl);
            }
        }
    }

    private void insertPayment(SQLiteDatabase db, String rcp_id, int shiftNo) {
        String ssql = "";

        String todaysDateInString = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String todaysTimeInString = new SimpleDateFormat("HHmm").format(new Date());

        for (int i = 0; i < paymentsMade.size(); i++) {

            String paymentCode = "CASH";
            String paymentName = "Cash";

            switch (paymentsMade.get(i).getPaymentType()) {
                case 0:
                    paymentCode = "CASH";
                    paymentName = "Cash";
                    break;
                case 1:
                    // Edited by Eddie 11/12/2016, change credit card to credit payment
                    paymentCode = "CREDIT";
                    paymentName = "Account";
                    break;
                default:
                    paymentCode = "CASH";
                    paymentName = "Cash";
                    break;
            }

            String changeAmountInsertString = "0.00";
            int lastItemIndex = paymentsMade.size() - 1;
            if(i == lastItemIndex) {
                changeAmountInsertString = changeAmount.toString();
            }

            String rowNumber = String.valueOf(i + 1);
            ssql = "INSERT INTO payment ( " +
                    "COMPANY_CODE, " +
                    "OUTLET_CODE, " +
                    "EMP_CODE, " +
                    "POS_NO, " +
                    "SHIFT_NO, " +
                    "RCP_NO, " +
                    "TRANS_TYPE, " +
                    "BUS_DATE, " +
                    "TRANS_DATE, " +
                    "TRANS_TIME, " +
                    "ROW_NUMBER, " +
                    "PAYMENT_CODE, " +
                    "PAYMENT_NAME, " +
                    "PAYMENT_TYPE, " +
                    "FOREX_CODE, " +
                    "FOREX_AMOUNT, " +
                    "CARD_NO, " +
                    "CARD_TYPE, " +
                    "BANK_CODE, " +
                    "PAYMENT_AMOUNT, " +
                    "CHANGE_AMOUNT, " +
                    "TENDER_AMOUNT, " +
                    "PAYMT_REMARK, " +
                    "DRAWER_DECLARE_ID, " +
                    "MODIFIED_DATE, " +
                    "MODIFIED_ID, " +
                    "ToSAP, " +
                    "IsNewInDevice " +
                    ") " +
                    "VALUES " +
                    "('" + companyCode + "', " +
                    "'" + outletCode + "', " +
                    "'" + empCode + "', " +
                    "'" + posNo + "', " +
                    "'" + Integer.toString(shiftNo) + "', " +
                    "'" + rcp_id + "', " +
                    "'S', " +
                    "'" + todaysDateInString + "', " + // bus date
                    "'" + todaysDateInString + "', " + // trans dates
                    "'" + todaysTimeInString + "', " + // trans time
                    rowNumber + ", " +
                    "'" + paymentCode + "', " +
                    "'" + paymentName + "', " +
                    "'" + paymentCode + "', " +
                    "'RM', " +
                    "0, " +
                    "null, " +
                    "null, " +
                    "null, " +
                    "" + paymentsMade.get(i).getPrice().toString() + ", " +
                    "" + changeAmountInsertString + ", " +
                    "" + total + ", " + // Edited by Eddie 11/12/2016, change the tender to total, initial was same as payment amount
                    "'', " +
                    "null, " +
                    "'" + todaysDateInString + "', " + // bus date
                    "'" + empCode + "', " + // Edited by Eddie 11/12/2016, make the field become employee code instead of null
                    "0, " +
                    "1); ";

            db.execSQL(ssql);
        }
    }

    private void insertDetailFromSuspend(SQLiteDatabase db, String rcp_id) {
        String ssql = "INSERT INTO detail ( " +
                "COMPANY_CODE, " +
                "OUTLET_CODE, " +
                "EMP_CODE, " +
                "POS_NO, " +
                "SHIFT_NO, " +
                "RCP_NO, " +
                "TRANS_TYPE, " +
                "BUS_DATE, " +
                "TRANS_DATE, " +
                "TRANS_TIME, " +
                "ROW_NUMBER, " +
                "PROD_CODE, " +
                "PROD_NAME, " +
                "PROD_TYPE_CODE, " +
                "USAGE_UOM, " +
                "QUANTITY, " +
                "UOM_CONV, " +
                "PRICE_LVL_CODE, " +
                "UNIT_PRICE, " +
                "TOTAL_PRICE, " +
                "TAX_01, " +
                "TAX_02, " +
                "TAX_03, " +
                "TAX_04, " +
                "TAX_05, " +
                "DISCOUNT_CODE, " +
                "ITEM_DISCOUNT_AMOUNT, " +
                "TOTAL_DISCOUNT_CODE, " +
                "TOTAL_DISCOUNT_AMOUNT, " +
                "TICKET_SURCHARGE, " +
                "STAFF_DISCOUNT_CODE, " +
                "STAFF_DISCOUNT, " +
                "BARCODE, " +
                "TAXCODE, " +
                "COST, " +
                "ToSAP, " +
                "IsNewInDevice " +
                ") " +
                "SELECT  " +
                "suspend.COMPANY_CODE, " +
                "suspend.OUTLET_CODE, " +
                "'" + empCode + "', " +
                "suspend.POS_NO, " +
                "suspend.SHIFT_NO, " +
                "'" + rcp_id + "', " +
                "suspend.TRANS_TYPE, " +
                "suspend.BUS_DATE, " +
                "suspend.TRANS_DATE, " +
                "suspend.TRANS_TIME, " +
                "suspend.ROW_NUMBER, " +
                "suspend.PROD_CODE, " +
                "suspend.PROD_NAME, " +
                "suspend.PROD_TYPE_CODE, " +
                "suspend.USAGE_UOM, " +
                "suspend.QUANTITY, " +
                "suspend.UOM_CONV, " +
                "null, " +
                "suspend.unit_price, " +
                "suspend.total_price, " +
                "suspend.TAX_01, " +
                "suspend.TAX_02, " +
                "suspend.TAX_03, " +
                "suspend.TAX_04, " +
                "suspend.TAX_05, " +
                "suspend.DISCOUNT_CODE, " +
                "suspend.ITEM_DISCOUNT_AMOUNT, " +
                "suspend.TOTAL_DICOUNT_CODE, " +
                "suspend.TOTAL_DISCOUNT_AMOUNT, " +
                "suspend.TICKET_SURCHARGE, " +
                "suspend.STAFF_DISCOUNT_CODE, " +
                "suspend.STAFF_DISCOUNT, " +
                "suspend.BARCODE, " +
                "suspend.TAXCODE, " +
                "suspend.COST, " +
                "0, " +
                "1 " +
                "FROM suspend " +
                "JOIN customer ON suspend.customer_code = customer.customer_code  " +
                "WHERE customer.customer_code = '" + customer_code + "'";

        db.execSQL(ssql);
    }

    private String insertHeader(SQLiteDatabase db, int shiftNo) {
        String receiptNo = "";
        POS_Control pctrl = dataHelper.getPOSControl(db);

        if(pctrl != null) {
            receiptNo = pctrl.LAST_RCP;

            if (receiptNo == null || receiptNo.trim().isEmpty()) {
                receiptNo = "0000001";
            } else if (receiptNo == "9999999") {
                showMessage("Receipt number maxed out", "Please reach the Administrator");
                receiptNo = "0000001";
            } else {
                int tempRcp = Integer.parseInt(receiptNo) + 1;
                receiptNo = String.format("%07d", tempRcp); // 7 digit receipt no
            }

            String todaysDateInString = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String todaysTimeInString = new SimpleDateFormat("HHmm").format(new Date());
            String ssql = "INSERT INTO header ( " +
                    "COMPANY_CODE, " + 
                    "OUTLET_CODE, " +
                    "EMP_CODE, " + 
                    "POS_NO, " + 
                    "SHIFT_NO, " + // line 5
                    "RCP_NO, " + 
                    "TRANS_TYPE, " + 
                    "BUS_DATE, " + 
                    "TRANS_DATE, " +
                    "TRANS_TIME, " + // 10
                    "SALES_AMOUNT, " +
                    "TOTAL_TAX, " + 
                    "TOTAL_DISCOUNT, " + 
                    "ROUNDING, " + 
                    "ROUNDING_ADJ, " + // 15
                    "APPROVAL_ID, " + 
                    "CUSTOMER_CODE, " + 
                    "CUSTOMER_POINT, " + 
                    "REFUND_VOUCHER_CODE, " + 
                    "REFUND_VOUCHER_AMOUNT, " + // 20
                    "REFUND_VOUCHER_EXPIRE_DATE, " + 
                    "DRAWER_DECLARE_ID, " + 
                    "BOTRANS_NO, " + 
                    "MODIFIED_DATE, " + 
                    "MODIFIED_ID, " + // 25
                    "ITEM_VOID_COUNT, " + 
                    "REPRINT_COUNT, " +
                    "ITEM_VOID_AMOUNT, " +
                    "REPRINT_AMOUNT, " +
                    "PRICE_LEVEL, " + // 30
                    "REFUND_POS_NO, " +
                    "REFUND_RCP_NO, " +
                    "REFUND_REMARK, " +
                    "REFUND_RCP_BUS_DATE, " +
                    "IsFORCE_REFUND, " + // 35
                    "REPRINTCOUNT, " +
                    "ToSAP, " +
                    "MEMBER_IC, " +
                    "PROTRANS_NO, " +
                    "IsNewInDevice" + // 40
                    ") " +
                    "VALUES (" +
                    "'" + companyCode + "', " + 
                    "'" + outletCode + "', " +
                    "'" + empCode + "', " +
                    "'" + posNo + "', " +
                    "'1', " + // line 5
                    "?, " +
                    "'S', " +
                    "'" + todaysDateInString + "', " + // bus date
                    "'" + todaysDateInString + "', " + // trans dates
                    "'" + todaysTimeInString + "', " + // trans time / line 10
                    "?, " +
                    "0, " +
                    "0, " +
                    "0, " +
                    "0, " + // 15
                    "null, " +
                    "?, " +
                    "0, " +
                    "null, " +
                    "null, " + // 20
                    "null, " +
                    "null, " +
                    "null, " +
                    "'" + todaysDateInString + "', " + // modified date
                    "'" + empCode + "', " + // 25
                    "0, " +
                    "0, " +
                    "null, " +
                    "null, " +
                    "null, " + // 30
                    "null, " +
                    "null, " +
                    "null, " +
                    "null, " +
                    "null, " + // 35
                    "null, " +
                    "0, " +
                    "null, " +
                    "null, " +
                    "1); "; // 40

            pctrl.LAST_RCP = receiptNo;
            pctrl.EMP_CD = empCode;
            pctrl.BUS_DATE = todaysDateInString;
            pctrl.SHIFT_NUMBER = shiftNo;
            dataHelper.deleteAndInsertPOSControl(db, pctrl);

            String[] objs = new String[]{receiptNo, total.toString(), customer_code}; // Edited by Eddie 11/12/2016, changed uuid to receiptNo
            db.execSQL(ssql, objs);

            return receiptNo; // Edited by Eddie 11/12/2016, changed uuid to receiptNo
        }
        else {
            return "noposcontrol";
        }
    }

    public BigDecimal getCumulativeTotalPaymentsMade() {

        BigDecimal returnTotal = new BigDecimal("0.0");

        for (int i = 0; i < paymentsMade.size(); i++) {
            returnTotal = returnTotal.add(paymentsMade.get(i).getPrice());
        }

        return returnTotal;
    }

    public void printReceipt() {
        connect();
        context.getObject().CON_PageStart(context.getState(),false,0,0);

        printReceiptPart1();
        printReceiptPart2();

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            printReceiptItems(db);
            printReceiptTotal(db);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }

        context.getObject().ASCII_CtrlReset(context.getState());
        context.getObject().ASCII_CtrlCutPaper(context.getState(), 66, 0);
        context.getObject().CON_PageEnd(context.getState(),context.getPrintway());
    }

    private void printReceiptTotal(SQLiteDatabase db) {
        Cursor res = db.rawQuery("SELECT * FROM header WHERE RCP_NO = '" + thisRcpId + "';", null);

        if (res.getCount() > 0) {
            res.moveToFirst();

            String grandtotalll = res.getString(res.getColumnIndex("SALES_AMOUNT"));
            String taxx = res.getString(res.getColumnIndex("TOTAL_TAX"));

            res.close();

            String res2Query = "SELECT SUM(PAYMENT_AMOUNT) AS CASH, SUM(CHANGE_AMOUNT) AS CHANGE FROM PAYMENT WHERE RCP_NO = '" + thisRcpId + "'";
            Cursor res2 = db.rawQuery(res2Query, null);
            res2.moveToFirst();

            String cashhh = res2.getString(res2.getColumnIndex("CASH"));
            String changeee = res2.getString(res2.getColumnIndex("CHANGE"));

            res2.close();

            BigDecimal bdGrandTotalll = new BigDecimal(grandtotalll).setScale(2, RoundingMode.HALF_UP);
            BigDecimal bdTaxx = new BigDecimal(taxx).setScale(2, RoundingMode.HALF_UP);
            BigDecimal bdTotalll = bdGrandTotalll.subtract(bdTaxx).setScale(2, RoundingMode.HALF_UP);

            BigDecimal bdCashhh = new BigDecimal(cashhh).setScale(2, RoundingMode.HALF_UP);
            BigDecimal bdChangeee = new BigDecimal(changeee).setScale(2, RoundingMode.HALF_UP);

            context.getObject().ASCII_CtrlAlignType(context.getState(),
                    preDefiniation.AlignType.AT_RIGHT.getValue());
            context.getObject().ASCII_PrintString(context.getState(),0,
                    0 ,0, 0, 0, "Total:  " + bdTotalll.toString(), "gb2312");
            context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);

            context.getObject().ASCII_CtrlAlignType(context.getState(),
                    preDefiniation.AlignType.AT_RIGHT.getValue());
            context.getObject().ASCII_PrintString(context.getState(),0,
                    0 ,0, 0, 0, "GST:  " + bdTaxx.toString(), "gb2312");
            context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);

            context.getObject().ASCII_CtrlAlignType(context.getState(),
                    preDefiniation.AlignType.AT_LEFT.getValue());
            context.getObject().ASCII_PrintString(context.getState(),0,
                    0, 0, 0, 0, "--------------------------------",
                    "gb2312");
            context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);

            context.getObject().ASCII_CtrlAlignType(context.getState(),
                    preDefiniation.AlignType.AT_RIGHT.getValue());
            context.getObject().ASCII_PrintString(context.getState(),0,
                    0 ,0, 0, 0, "GRAND TOTAL:  " + bdGrandTotalll.toString(), "gb2312");
            context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);

            context.getObject().ASCII_CtrlAlignType(context.getState(),
                    preDefiniation.AlignType.AT_RIGHT.getValue());
            context.getObject().ASCII_PrintString(context.getState(),0,
                    0 ,0, 0, 0, "Cash:  " + bdCashhh.toString(), "gb2312");
            context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);

            context.getObject().ASCII_CtrlAlignType(context.getState(),
                    preDefiniation.AlignType.AT_RIGHT.getValue());
            context.getObject().ASCII_PrintString(context.getState(),0,
                    0 ,0, 0, 0, "Change:  " + bdChangeee.toString(), "gb2312");
            context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);
        }
    }

    private void printReceiptItems(SQLiteDatabase db) {
        Cursor res = db.rawQuery("SELECT * FROM detail WHERE RCP_NO = '" + thisRcpId + "';", null);

        while(res.moveToNext()) {
            String quantity = res.getString(res.getColumnIndex("QUANTITY"));
            String prodname = res.getString(res.getColumnIndex("PROD_NAME"));
            String pricet = res.getString(res.getColumnIndex("TOTAL_PRICE"));

            BigDecimal bdPricet = new BigDecimal(pricet).setScale(2, RoundingMode.HALF_UP);

            context.getObject().ASCII_CtrlAlignType(context.getState(),
                    preDefiniation.AlignType.AT_LEFT.getValue());
            context.getObject().ASCII_PrintString(context.getState(),0,
                    0 ,0, 0, 0, quantity + "x  " + prodname, "gb2312");
            context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);

            context.getObject().ASCII_CtrlAlignType(context.getState(),
                    preDefiniation.AlignType.AT_RIGHT.getValue());
            context.getObject().ASCII_PrintString(context.getState(),0,
                    0 ,0, 0, 0, bdPricet.toString(), "gb2312");
            context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);
        }

        res.close();

        context.getObject().ASCII_CtrlAlignType(context.getState(),
                preDefiniation.AlignType.AT_LEFT.getValue());
        context.getObject().ASCII_PrintString(context.getState(),0,
                0, 0, 0, 0, "--------------------------------",
                "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);
    }

    private void printReceiptPart1() {
        context.getObject().ASCII_CtrlAlignType(context.getState(),
                preDefiniation.AlignType.AT_CENTER.getValue());
        context.getObject().ASCII_PrintString(context.getState(),0,
                0 ,0, 0, 0, "DIAMOND PUBLIC", "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);
        context.getObject().ASCII_PrintString(context.getState(),0,
                0 ,0, 0, 0, "TUBE ICE TRADING", "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);
        context.getObject().ASCII_PrintString(context.getState(),0,
                0, 0, 0, 0, "(001500992-V)", "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);
        context.getObject().ASCII_PrintString(context.getState(),0,
                0, 0, 0, 0, "(GST No. 001022943232)", "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);
        context.getObject().ASCII_PrintString(context.getState(),0,
                0, 0, 0, 0, "38, Jalan Kati Fu 9/F", "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);
        context.getObject().ASCII_PrintString(context.getState(),0,
                0, 0, 0, 0, "Taman Medan Mas", "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);

        context.getObject().ASCII_PrintString(context.getState(),0,
                0, 0, 0, 0, "Kg.Baru Seri Sg. Buloh", "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);

        context.getObject().ASCII_PrintString(context.getState(),0,
                0, 0, 0, 0, "4060 Shah Alam, Selangor", "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);

        context.getObject().ASCII_PrintString(context.getState(),0,
                0, 0, 0, 0, "Tel:012-2126307, 014-3376307", "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);

        context.getObject().ASCII_PrintString(context.getState(),0,
                0, 0, 0, 0, "--------------------------------",
                "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);
    }

    private void printReceiptPart2() {
        context.getObject().ASCII_CtrlAlignType(context.getState(),
                preDefiniation.AlignType.AT_CENTER.getValue());
        context.getObject().ASCII_PrintString(context.getState(),0,
                0 ,0, 0, 0, "TAX INVOICE", "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);

        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);

        context.getObject().ASCII_CtrlAlignType(context.getState(),
                preDefiniation.AlignType.AT_LEFT.getValue());
        context.getObject().ASCII_PrintString(context.getState(),0,
                0 ,0, 0, 0, "Date:  " + strDate, "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);

        context.getObject().ASCII_CtrlAlignType(context.getState(),
                preDefiniation.AlignType.AT_LEFT.getValue());
        context.getObject().ASCII_PrintString(context.getState(),0,
                0 ,0, 0, 0, "Emp:  " + empName, "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);

        context.getObject().ASCII_CtrlAlignType(context.getState(),
                preDefiniation.AlignType.AT_LEFT.getValue());
        context.getObject().ASCII_PrintString(context.getState(),0,
                0 ,0, 0, 0, "POS:  " + posNo, "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);

        context.getObject().ASCII_CtrlAlignType(context.getState(),
                preDefiniation.AlignType.AT_LEFT.getValue());
        context.getObject().ASCII_PrintString(context.getState(),0,
                0 ,0, 0, 0, "RCP No:  " + thisRcpId, "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);

        context.getObject().ASCII_PrintString(context.getState(),0,
                0, 0, 0, 0, "--------------------------------",
                "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);
    }

    public void connect() {
        // /dev/ttyMT1:115200 //kt45
        // /dev/ttyG1:115200 //tt43
        modelJudgmen();
        if (mBconnect) {
            context.getObject().CON_CloseDevices(context.getState());
            mBconnect = false;
        } else {
            if (state > 0) {
                mBconnect = true;
                context.setState(state);
                context.setName("RG-E48");

            } else {
                Toast.makeText(this, R.string.mes_confail,
                        Toast.LENGTH_SHORT).show();
                mBconnect = false;
            }
        }
    }

    private void modelJudgmen() {
        state = context.getObject().CON_ConnectDevices("RG-E487", "/dev/ttyMT1:115200", 200);

        if (android.os.Build.VERSION.RELEASE.equals("5.1")) {
            DevCtrl = new DeviceControl(DeviceControl.powerPathKT);
            DevCtrl.setGpio(94);
            isTT43 = false;
            try {
                DevCtrl.PowerOnMTDevice();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.show();
    }
    public class PaymentMade {
        private int paymentType;
        private BigDecimal price;

        public PaymentMade(int paymentType, BigDecimal price) {
            this.paymentType = paymentType;
            this.price = price;
        }

        public int getPaymentType() {
            return this.paymentType;
        }

        public BigDecimal getPrice() {
            return this.price;
        }

    }
}