package com.example.thisi.applicationx;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.ksoap2.serialization.SoapObject;

import java.math.BigDecimal;

public class UploadDataService extends Service1 {
    DatabaseHelper dataHelper;

    public UploadDataService(IWsdl2CodeEvents eventHandler, String url, Context context) {
        super(eventHandler, url);

        dataHelper = DatabaseHelper.getHelper(context);
    }

    public void UploadDataAsync() throws Exception {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected void onPreExecute() {
                eventHandler.UploadDataStartedRequest();
            }

            @Override
            protected Object doInBackground(Void... params) {
                Object o = null;
                SQLiteDatabase db = dataHelper.getReadableDatabase();
                db.beginTransaction();

                try {
                    UploadHeaders(db);
                    UploadDetails(db);
                    UploadPayments(db); 

                    db.setTransactionSuccessful();
                    o = "success";
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                    db.close();
                }

                return o;
            }

            @Override
            protected void onPostExecute(Object result) {
                eventHandler.UploadDataEndedRequest();
                if (result != null) {
                    eventHandler.UploadDataFinished("SQLResult", result);
                }
            }
        }.execute();
    }

    private void UploadHeaders(SQLiteDatabase db) {
        String selectQuery = "SELECT * FROM header;";

        Cursor cursor = db.rawQuery(selectQuery, null);
        int rowCount = cursor.getCount();

        try {
            if (rowCount > 0) {
                StringBuilder sb = new StringBuilder(); 

                sb.append("INSERT INTO header (COMPANY_CODE, OUTLET_CODE, EMP_CODE, POS_NO, SHIFT_NO, RCP_NO, TRANS_TYPE, BUS_DATE, TRANS_DATE, TRANS_TIME, SALES_AMOUNT, REFUND_VOUCHER_AMOUNT, DRAWER_DECLARE_ID, MODIFIED_DATE, MODIFIED_ID, REPRINTCOUNT, PROTRANS_NO) ");
                sb.append("VALUES ");

                while (cursor.moveToNext()) {
                    sb.append("("); 

                    String companyCodeColValue = cursor.getString(cursor.getColumnIndex("COMPANY_CODE"));
                    sb.append(appendStringQueryVar(companyCodeColValue));
                    sb.append(",");

                    String outletCodeColValue = cursor.getString(cursor.getColumnIndex("OUTLET_CODE"));
                    sb.append(appendStringQueryVar(outletCodeColValue));
                    sb.append(",");

                    String empCodeColValue = cursor.getString(cursor.getColumnIndex("EMP_CODE"));
                    sb.append(appendStringQueryVar(empCodeColValue));
                    sb.append(",");

                    String posNoColValue = cursor.getString(cursor.getColumnIndex("POS_NO"));
                    sb.append(appendStringQueryVar(posNoColValue));
                    sb.append(",");

                    String shiftNoColValue = cursor.getString(cursor.getColumnIndex("SHIFT_NO"));
                    sb.append(appendStringQueryVar(shiftNoColValue));
                    sb.append(",");

                    String rcpNoColValue = cursor.getString(cursor.getColumnIndex("RCP_NO"));
                    sb.append(appendStringQueryVar(rcpNoColValue));
                    sb.append(",");

                    String transTypeColValue = cursor.getString(cursor.getColumnIndex("TRANS_TYPE"));
                    sb.append(appendStringQueryVar(transTypeColValue));
                    sb.append(",");

                    String busDateColValue = cursor.getString(cursor.getColumnIndex("BUS_DATE")); //datetime YYYYMMDD
                    sb.append(appendStringQueryVar(busDateColValue));
                    sb.append(",");

                    String transDateColValue = cursor.getString(cursor.getColumnIndex("TRANS_DATE")); //datetime YYYYMMDD
                    sb.append(appendStringQueryVar(transDateColValue));
                    sb.append(",");
                    
                    String transTimeColValue = cursor.getString(cursor.getColumnIndex("TRANS_TIME"));
                    sb.append(appendStringQueryVar(transTimeColValue));
                    sb.append(",");

                    double salesAmountColValue = cursor.getDouble(cursor.getColumnIndex"SALES_AMOUNT")); 
                    sb.append(salesAmountColValue.toString());
                    sb.append(",");

                    double refundVoucherAmountColValue = cursor.getDouble(cursor.getColumnIndex"REFUND_VOUCHER_AMOUNT"));
                    sb.append(refundVoucherAmountColValue.toString());
                    sb.append(",");

                    String drawerDeclareIdColValue = cursor.getString(cursor.getColumnIndex("DRAWER_DECLARE_ID"));
                    sb.append(appendStringQueryVar(drawerDeclareIdColValue));
                    sb.append(",");

                    String modifiedDateColValue = cursor.getString(cursor.getColumnIndex("MODIFIED_DATE")); //datetime YYYYMMDD
                    sb.append(appendStringQueryVar(modifiedDateColValue));
                    sb.append(",");
                    
                    String modifiedIdColValue = cursor.getString(cursor.getColumnIndex("MODIFIED_ID"));
                    sb.append(appendStringQueryVar(modifiedIdColValue));
                    sb.append(",");

                    int reprintCountColValue = cursor.getInt(cursor.getColumnIndex("REPRINTCOUNT"));
                    sb.append(reprintCountColValue.toString());
                    sb.append(",");

                    String protransNoColValue = cursor.getString(cursor.getColumnIndex("PROTRANS_NO"));
                    sb.append(appendStringQueryVar(protransNoColValue));

                    sb.append(")");

                    if(!cursor.isLast()) {
                        sb.append(",");                        
                    }
                }

                super.SQLExec(sb.toString()); 
            }
        }
        finally {
            cursor.close(); 
        }
    }

    private void UploadDetail(SQLiteDatabase db) {
        String selectQuery = "SELECT * FROM detail;";

        Cursor cursor = db.rawQuery(selectQuery, null);
        int rowCount = cursor.getCount();

        try {
            if (rowCount > 0) {
                StringBuilder sb = new StringBuilder(); 

                sb.append("INSERT INTO detail (COMPANY_CODE,OUTLET_CODE,EMP_CODE,POS_NO,SHIFT_NO,RCP_NO,TRANS_TYPE,BUS_DATE,TRANS_DATE,TRANS_TIME,ROW_NUMBER,PROD_CODE,UNIT_PRICE,TOTAL_PRICE) ");
                sb.append("VALUES ");

                while (cursor.moveToNext()) {
                    sb.append("("); 

                    String companyCodeColValue = cursor.getString(cursor.getColumnIndex("COMPANY_CODE"));
                    sb.append(appendStringQueryVar(companyCodeColValue));
                    sb.append(",");

                    String outletCodeColValue = cursor.getString(cursor.getColumnIndex("OUTLET_CODE"));
                    sb.append(appendStringQueryVar(outletCodeColValue));
                    sb.append(",");

                    String empCodeColValue = cursor.getString(cursor.getColumnIndex("EMP_CODE"));
                    sb.append(appendStringQueryVar(empCodeColValue));
                    sb.append(",");

                    String posNoColValue = cursor.getString(cursor.getColumnIndex("POS_NO"));
                    sb.append(appendStringQueryVar(posNoColValue));
                    sb.append(",");

                    String shiftNoColValue = cursor.getString(cursor.getColumnIndex("SHIFT_NO"));
                    sb.append(appendStringQueryVar(shiftNoColValue));
                    sb.append(",");

                    String rcpNoColValue = cursor.getString(cursor.getColumnIndex("RCP_NO"));
                    sb.append(appendStringQueryVar(rcpNoColValue));
                    sb.append(",");

                    String transTypeColValue = cursor.getString(cursor.getColumnIndex("TRANS_TYPE"));
                    sb.append(appendStringQueryVar(transTypeColValue));
                    sb.append(",");

                    String busDateColValue = cursor.getString(cursor.getColumnIndex("BUS_DATE")); //datetime YYYYMMDD
                    sb.append(appendStringQueryVar(busDateColValue));
                    sb.append(",");

                    String transDateColValue = cursor.getString(cursor.getColumnIndex("TRANS_DATE")); //datetime YYYYMMDD
                    sb.append(appendStringQueryVar(transDateColValue));
                    sb.append(",");
                    
                    String transTimeColValue = cursor.getString(cursor.getColumnIndex("TRANS_TIME"));
                    sb.append(appendStringQueryVar(transTimeColValue));
                    sb.append(",");

                    int rowNumberColValue = cursor.getInt(cursor.getColumnIndex("ROW_NUMBER"));
                    sb.append(rowNumberColValue.toString());
                    sb.append(",");

                    String prodCodeColValue = cursor.getString(cursor.getColumnIndex("PROD_CODE"));
                    sb.append(appendStringQueryVar(prodCodeColValue));
                    sb.append(",");

                    double unitPriceColValue = cursor.getDouble(cursor.getColumnIndex"UNIT_PRICE"));
                    sb.append(unitPriceColValue.toString());
                    sb.append(",");

                    double totalPriceColValue = cursor.getDouble(cursor.getColumnIndex"TOTAL_PRICE"));
                    sb.append(totalPriceColValue.toString());

                    sb.append(")");

                    if(!cursor.isLast()) {
                        sb.append(",");                        
                    }
                }

                super.SQLExec(sb.toString()); 
            }
        }
        finally {
            cursor.close(); 
        }
    }

    private void UploadPayments(SQLiteDatabase db) {
        String selectQuery = "SELECT * FROM payment;";

        Cursor cursor = db.rawQuery(selectQuery, null);
        int rowCount = cursor.getCount();

        try {
            if (rowCount > 0) {
                StringBuilder sb = new StringBuilder(); 

                sb.append("INSERT INTO payment (COMPANY_CODE,OUTLET_CODE,EMP_CODE,POS_NO,SHIFT_NO,RCP_NO,TRANS_TYPE,BUS_DATE,TRANS_DATE,TRANS_TIME,ROW_NUMBER,PAYMENT_CODE,PAYMENT_NAME,PAYMENT_TYPE,FOREX_CODE,PAYMENT_AMOUNT,CHANGE_AMOUNT,TENDER_AMOUNT,DRAWER_DECLARE_ID,MODIFIED_DATE,MODIFIED_ID) ");
                sb.append("VALUES ");

                while (cursor.moveToNext()) {
                    sb.append("("); 

                    String companyCodeColValue = cursor.getString(cursor.getColumnIndex("COMPANY_CODE"));
                    sb.append(appendStringQueryVar(companyCodeColValue));
                    sb.append(",");

                    String outletCodeColValue = cursor.getString(cursor.getColumnIndex("OUTLET_CODE"));
                    sb.append(appendStringQueryVar(outletCodeColValue));
                    sb.append(",");

                    String empCodeColValue = cursor.getString(cursor.getColumnIndex("EMP_CODE"));
                    sb.append(appendStringQueryVar(empCodeColValue));
                    sb.append(",");

                    String posNoColValue = cursor.getString(cursor.getColumnIndex("POS_NO"));
                    sb.append(appendStringQueryVar(posNoColValue));
                    sb.append(",");

                    String shiftNoColValue = cursor.getString(cursor.getColumnIndex("SHIFT_NO"));
                    sb.append(appendStringQueryVar(shiftNoColValue));
                    sb.append(",");

                    String rcpNoColValue = cursor.getString(cursor.getColumnIndex("RCP_NO"));
                    sb.append(appendStringQueryVar(rcpNoColValue));
                    sb.append(",");

                    String transTypeColValue = cursor.getString(cursor.getColumnIndex("TRANS_TYPE"));
                    sb.append(appendStringQueryVar(transTypeColValue));
                    sb.append(",");

                    String busDateColValue = cursor.getString(cursor.getColumnIndex("BUS_DATE")); //datetime YYYYMMDD
                    sb.append(appendStringQueryVar(busDateColValue));
                    sb.append(",");

                    String transDateColValue = cursor.getString(cursor.getColumnIndex("TRANS_DATE")); //datetime YYYYMMDD
                    sb.append(appendStringQueryVar(transDateColValue));
                    sb.append(",");
                    
                    String transTimeColValue = cursor.getString(cursor.getColumnIndex("TRANS_TIME"));
                    sb.append(appendStringQueryVar(transTimeColValue));
                    sb.append(",");

                    int rowNumberColValue = cursor.getInt(cursor.getColumnIndex("ROW_NUMBER"));
                    sb.append(rowNumberColValue.toString());
                    sb.append(",");

                    String paymentcode = cursor.getString(cursor.getColumnIndex("PAYMENT_CODE")); 
                    sb.append(appendStringQueryVar(paymentcode)); sb.append(",");
                    sb.append(",");

                    String paymentname = cursor.getString(cursor.getColumnIndex("PAYMENT_NAME")); 
                    sb.append(appendStringQueryVar(paymentname)); sb.append(",");
                    sb.append(",");

                    String paymenttype = cursor.getString(cursor.getColumnIndex("PAYMENT_TYPE")); 
                    sb.append(appendStringQueryVar(paymenttype)); sb.append(",");
                    sb.append(",");

                    String forexcode = cursor.getString(cursor.getColumnIndex("FOREX_CODE")); 
                    sb.append(appendStringQueryVar(forexcode)); sb.append(",");
                    sb.append(",");

                    double paymentamount = cursor.getDouble(cursor.getColumnIndex"PAYMENT_AMOUNT")); 
                    sb.append(paymentamount.toString()); sb.append(",");
                    sb.append(",");

                    double changeamount = cursor.getDouble(cursor.getColumnIndex"CHANGE_AMOUNT")); 
                    sb.append(changeamount.toString()); sb.append(",");
                    sb.append(",");

                    double tenderamount = cursor.getDouble(cursor.getColumnIndex"TENDER_AMOUNT")); 
                    sb.append(tenderamount.toString()); sb.append(",");
                    sb.append(",");

                    String drawerdeclareid = cursor.getString(cursor.getColumnIndex("DRAWER_DECLARE_ID")); 
                    sb.append(appendStringQueryVar(drawerdeclareid)); sb.append(",");
                    sb.append(",");

                    String modifieddate = cursor.getString(cursor.getColumnIndex("MODIFIED_DATE")); 
                    sb.append(appendStringQueryVar(modifieddate)); sb.append(",");
                    sb.append(",");

                    String modifiedid = cursor.getString(cursor.getColumnIndex("MODIFIED_ID")); 
                    sb.append(appendStringQueryVar(modifiedid)); sb.append(",");

                    sb.append(")");

                    if(!cursor.isLast()) {
                        sb.append(",");                        
                    }
                }

                super.SQLExec(sb.toString()); 
            }
        }
        finally {
            cursor.close(); 
        }
    }

    public String appendStringQueryVar(String str) {
        if(str == null || str.trim().isEmpty()) {
            return "' '"; 
        }
        else {
            return "'" + str + "'";
        }
    }
}

