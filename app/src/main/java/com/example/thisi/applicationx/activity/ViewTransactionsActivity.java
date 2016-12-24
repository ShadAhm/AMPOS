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
import android.widget.Toast;

import com.example.thisi.applicationx.data.DatabaseHelper;
import com.example.thisi.applicationx.R;
import com.example.thisi.applicationx.model.Employee;
import com.example.thisi.applicationx.printutil.ApplicationContext;
import com.example.thisi.applicationx.printutil.DeviceControl;
import com.example.thisi.applicationx.printutil.preDefiniation;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class ViewTransactionsActivity extends Activity {
    DatabaseHelper myDb;

    final String headerRowColor = "#00688B";
    final String evenRowColor = "#E0FFFF";
    final String oddRowColor = "#00EEEE";

    // printer stuff
    public ApplicationContext context;
    public int state;
    public boolean mBconnect = false;
    DatabaseHelper dataHelper;
    private DeviceControl DevCtrl;
    private boolean isTT43 = false;

    // from settings :
    private static String posNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewtransactions);

        initializeVariables();
        addTransactionsToView();
    }

    private void initializeVariables() {
        context = (ApplicationContext) getApplicationContext();

        dataHelper = DatabaseHelper.getHelper(this);

        SharedPreferences prefs = this.getSharedPreferences("com.example.thisi.applicationx", Context.MODE_PRIVATE);
        posNo = prefs.getString("posnumber", null);
        myDb = DatabaseHelper.getHelper(this);
    }

    private void addTransactionsToView() {
             TableLayout tableLayout = (TableLayout) findViewById(R.id.tableTransactions);
             // Add header row
            TableRow rowHeader = createHeaderRow();
             tableLayout.addView(rowHeader);

             SQLiteDatabase db = myDb.getReadableDatabase();
             // Start the transaction.
             db.beginTransaction();

             try {
                 String todaysDateInString = new SimpleDateFormat("yyyyMMdd").format(new Date());

                 String selectQuery = "SELECT * FROM header " +
                         "WHERE trans_date = '" + todaysDateInString + // get today's transactions only
                         "' ORDER BY TRANS_TIME DESC LIMIT 10;";

                 Cursor cursor = db.rawQuery(selectQuery, null);

                 int rowCount = cursor.getCount();

                 if (rowCount > 0) {
                     int i = 0;

                     while (cursor.moveToNext()) {
                         i++;

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

                         String[] colText = { transTime, customerCode, rcpNo };

                         final String rcpnofinal = rcpNo;

                         int j = 0;
                         for (String text : colText) {
                             TextView tv = new TextView(this);
                             tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                     TableRow.LayoutParams.WRAP_CONTENT));

                             tv.setTextSize(13);
                             tv.setPadding(5, 10, 5, 10);
                             tv.setText(text);
                             row.addView(tv);

                             tv.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     reprintReceipt(rcpnofinal);
                                 }
                             });

                             j++;
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

    private TableRow createHeaderRow() {
        TableRow rowHeader = new TableRow(this);
        rowHeader.setBackgroundColor(Color.parseColor(headerRowColor));

        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        String[] headerText = {"Time", "Customer Code", "Receipt No" };

        int i = 0;
        for (String c : headerText) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            tv.setTextSize(13);
            tv.setPadding(5, 5, 5, 5);
            tv.setTextColor(Color.WHITE);
            tv.setText(c);
            rowHeader.addView(tv);

            i++;
        }
        return rowHeader;
    }

    private void reprintReceipt(String rcpNo) {
        connect();
        context.getObject().CON_PageStart(context.getState(),false,0,0);

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            Cursor res = db.rawQuery("SELECT * FROM header WHERE RCP_NO = '" + rcpNo + "';", null);

            Employee emp = new Employee();
            emp.EMP_NAME = "unknown";

            if (res.moveToFirst()) {
                String empCode = res.getString(res.getColumnIndex("EMP_CODE"));
                dataHelper.getEmployeeByEmployeeCode(db, empCode);
            }

            printReceiptPart1();
            printReceiptPart2(rcpNo, emp.EMP_NAME);

            printReceiptItems(db, rcpNo);
            printReceiptTotal(db, rcpNo);

            dataHelper.updateHeaderReprintCount(db, rcpNo);

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

    private void printReceiptTotal(SQLiteDatabase db, String thisRcpId) {
        Cursor res = db.rawQuery("SELECT * FROM header WHERE RCP_NO = '" + thisRcpId + "';", null);

        if (res.getCount() > 0) {
            res.moveToFirst();

            String grandtotalll = res.getString(res.getColumnIndex("SALES_AMOUNT"));
            String taxx = res.getString(res.getColumnIndex("TOTAL_TAX"));

            res.close();

            Cursor res2 = db.rawQuery("SELECT SUM(PAYMENT_AMOUNT) AS CASH, SUM(CHANGE_AMOUNT) AS CHANGE FROM PAYMENT WHERE RCP_NO = '" + thisRcpId + "'", null);
            res2.moveToFirst();

            String cashhh = res2.getString(res.getColumnIndex("CASH"));
            String changeee = res2.getString(res.getColumnIndex("CHANGE"));

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

    private void printReceiptItems(SQLiteDatabase db, String thisRcpId) {
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
                0 ,0, 0, 0, "************REPRINT************", "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);

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

    private void printReceiptPart2(String thisRcpId, String empCode) {
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
                0 ,0, 0, 0, "Emp:  " + empCode, "gb2312");
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
}