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
                    UploadHeader(db);

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

    private void UploadHeader(SQLiteDatabase db) {
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

    public String appendStringQueryVar(String str) {
        if(str == null || str.trim().isEmpty()) {
            return "' '"; 
        }
        else {
            return "'" + str + "'";
        }
    }
}

