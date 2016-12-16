package com.example.thisi.applicationx.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.example.thisi.applicationx.data.DatabaseHelper;
import com.example.thisi.applicationx.util.IWsdl2CodeEvents;

public class UploadDataService extends Service1 {
    DatabaseHelper dataHelper;

    // Edited by Eddie 11/12/2016, add company code, outlet code and pos number, begin
    // from settings :
    private static String companyCode;
    private static String posNo;
    private static String outletCode;
    private static String empCode;

    public UploadDataService(IWsdl2CodeEvents eventHandler,
                             String url,
                             Context context,
                             String comCode,
                             String posN,
                             String outCode,
                             String emplCode
    ) {
        super(eventHandler, url);

        companyCode = comCode;
        posNo = posN;
        outletCode = outCode;
        empCode = emplCode;

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
                    o = "failure";
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

                while(cursor.moveToNext()) {
                    String rcpNoColValue = cursor.getString(cursor.getColumnIndex("RCP_NO"));

                    // Edited by Eddie 11/12/2016, add the filter with company code and outlet code, begin
                    String deletef = "DELETE FROM payment WHERE company_code = '" + companyCode + "' AND outlet_code = '" + outletCode + "' AND rcp_no = '" + rcpNoColValue + "'; " +
                            "DELETE FROM detail WHERE company_code = '" + companyCode + "' AND outlet_code = '" + outletCode + "' AND rcp_no = '" + rcpNoColValue + "'; " +
                            "DELETE FROM header WHERE company_code = '" + companyCode + "' AND outlet_code = '" + outletCode + "' AND rcp_no = '" + rcpNoColValue + "';";
                    // Edited by Eddie 11/12/2016, end

                    super.SQLExec(deletef);
                }

                cursor.moveToFirst();

                sb.append("INSERT INTO header (COMPANY_CODE, OUTLET_CODE, EMP_CODE, POS_NO, SHIFT_NO, RCP_NO, TRANS_TYPE, BUS_DATE, TRANS_DATE, TRANS_TIME, SALES_AMOUNT, TOTAL_TAX, CUSTOMER_CODE, REFUND_VOUCHER_AMOUNT, DRAWER_DECLARE_ID, MODIFIED_DATE, MODIFIED_ID, REPRINTCOUNT, PROTRANS_NO) ");
                sb.append("VALUES ");

                do {
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

                    double salesAmountColValue = cursor.getDouble(cursor.getColumnIndex("SALES_AMOUNT"));
                    sb.append(Double.toString(salesAmountColValue));
                    sb.append(",");

                    // Edited by Eddie 11/12/2016, add missing field, begin
                    double totalTaxColValue = cursor.getDouble(cursor.getColumnIndex("TOTAL_TAX"));
                    sb.append(Double.toString(totalTaxColValue));
                    sb.append(",");

                    String customerCodeColValue = cursor.getString(cursor.getColumnIndex("CUSTOMER_CODE"));
                    sb.append(appendStringQueryVar(customerCodeColValue));
                    sb.append(",");
                    // Edited by Eddie 11/12/2016, end

                    double refundVoucherAmountColValue = cursor.getDouble(cursor.getColumnIndex("REFUND_VOUCHER_AMOUNT"));
                    sb.append(Double.toString(refundVoucherAmountColValue));
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
                    sb.append(Integer.toString(reprintCountColValue));
                    sb.append(",");

                    String protransNoColValue = cursor.getString(cursor.getColumnIndex("PROTRANS_NO"));
                    sb.append(appendStringQueryVar(protransNoColValue));

                    sb.append(")");

                    if(!cursor.isLast()) {
                        sb.append(",");
                    }
                } while (cursor.moveToNext());

                super.SQLExec(sb.toString());
            }
        }
        finally {
            cursor.close();
        }
    }

    private void UploadDetails(SQLiteDatabase db) {
        String selectQuery = "SELECT * FROM detail;";

        Cursor cursor = db.rawQuery(selectQuery, null);
        int rowCount = cursor.getCount();

        try {
            if (rowCount > 0) {
                StringBuilder sb = new StringBuilder();

                sb.append("INSERT INTO detail (" +
                        "COMPANY_CODE," +
                        "OUTLET_CODE," +
                        "EMP_CODE," +
                        "POS_NO," +
                        "SHIFT_NO," +
                        "RCP_NO," +
                        "TRANS_TYPE," +
                        "BUS_DATE," +
                        "TRANS_DATE," +
                        "TRANS_TIME," +
                        "ROW_NUMBER," +
                        "PROD_CODE," +
                        "PROD_NAME," +
                        "PROD_TYPE_CODE," +
                        "USAGE_UOM," +
                        "QUANTITY," +
                        "UOM_CONV," +
                        "UNIT_PRICE," +
                        "TOTAL_PRICE," +
                        "TAX_01," +
                        "TAX_02," +
                        "TAX_03," +
                        "TAX_04," +
                        "TAX_05," +
                        "BARCODE," +
                        "TAXCODE," +
                        "COST" +
                        ") ");
                sb.append("VALUES ");

                int rownumIn = 1;
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
                    sb.append(Integer.toString(rownumIn));
                    sb.append(",");

                    String prodCodeColValue = cursor.getString(cursor.getColumnIndex("PROD_CODE"));
                    sb.append(appendStringQueryVar(prodCodeColValue));
                    sb.append(",");

                    // Edited by Eddie 11/12/2016, add missing upload field, begin
                    String prodNameColValue = cursor.getString(cursor.getColumnIndex("PROD_NAME"));
                    sb.append(appendStringQueryVar(prodNameColValue));
                    sb.append(",");

                    String prodTypeCodeColValue = cursor.getString(cursor.getColumnIndex("PROD_TYPE_CODE"));
                    sb.append(appendStringQueryVar(prodTypeCodeColValue));
                    sb.append(",");

                    String usageUOMColValue = cursor.getString(cursor.getColumnIndex("USAGE_UOM"));
                    sb.append(appendStringQueryVar(usageUOMColValue));
                    sb.append(",");

                    int quantityColValue = cursor.getInt(cursor.getColumnIndex("QUANTITY"));
                    sb.append(Integer.valueOf(quantityColValue));
                    sb.append(",");

                    int uomConvColValue = cursor.getInt(cursor.getColumnIndex("UOM_CONV"));
                    sb.append(Integer.valueOf(uomConvColValue));
                    sb.append(",");
                    // Edited by Eddie 11/12/2016, End

                    double unitPriceColValue = cursor.getDouble(cursor.getColumnIndex("UNIT_PRICE"));
                    sb.append(Double.toString(unitPriceColValue));
                    sb.append(",");

                    double totalPriceColValue = cursor.getDouble(cursor.getColumnIndex("TOTAL_PRICE"));
                    sb.append(Double.toString(totalPriceColValue));
                    sb.append(",");

                    // Edited by Eddie 11/12/2016, add missing upload field, begin
                    double taxOneColValue = cursor.getDouble(cursor.getColumnIndex("TAX_01"));
                    sb.append(Double.toString(taxOneColValue));
                    sb.append(",");

                    double taxTwoColValue = cursor.getDouble(cursor.getColumnIndex("TAX_02"));
                    sb.append(Double.toString(taxTwoColValue));
                    sb.append(",");

                    double taxThreeColValue = cursor.getDouble(cursor.getColumnIndex("TAX_03"));
                    sb.append(Double.toString(taxThreeColValue));
                    sb.append(",");

                    double taxFourColValue = cursor.getDouble(cursor.getColumnIndex("TAX_04"));
                    sb.append(Double.toString(taxFourColValue));
                    sb.append(",");

                    double taxFiveColValue = cursor.getDouble(cursor.getColumnIndex("TAX_05"));
                    sb.append(Double.toString(taxFiveColValue));
                    sb.append(",");

                    String barcodeColValue = cursor.getString(cursor.getColumnIndex("BARCODE"));
                    sb.append(appendStringQueryVar(barcodeColValue));
                    sb.append(",");

                    String taxCodeColValue = cursor.getString(cursor.getColumnIndex("TAXCODE"));
                    sb.append(appendStringQueryVar(taxCodeColValue));
                    sb.append(",");

                    double costColValue = cursor.getDouble(cursor.getColumnIndex("COST"));
                    sb.append(Double.toString(costColValue));
                    // Edited by Eddie 11/12/2016, End

                    sb.append(")");

                    rownumIn++;

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

                sb.append("INSERT INTO payment(COMPANY_CODE,OUTLET_CODE,EMP_CODE,POS_NO,SHIFT_NO,RCP_NO,TRANS_TYPE,BUS_DATE,TRANS_DATE,TRANS_TIME,ROW_NUMBER,PAYMENT_CODE,PAYMENT_NAME,PAYMENT_TYPE,FOREX_CODE,PAYMENT_AMOUNT,CHANGE_AMOUNT,TENDER_AMOUNT,DRAWER_DECLARE_ID,MODIFIED_DATE,MODIFIED_ID) ");
                sb.append("VALUES ");

                int rownumIn = 1;
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

                    sb.append(Integer.toString(rownumIn));
                    sb.append(",");

                    String paymentcode = cursor.getString(cursor.getColumnIndex("PAYMENT_CODE"));
                    sb.append(appendStringQueryVar(paymentcode)); sb.append(",");

                    String paymentname = cursor.getString(cursor.getColumnIndex("PAYMENT_NAME"));
                    sb.append(appendStringQueryVar(paymentname)); sb.append(",");

                    String paymenttype = cursor.getString(cursor.getColumnIndex("PAYMENT_TYPE"));
                    sb.append(appendStringQueryVar(paymenttype)); sb.append(",");

                    String forexcode = cursor.getString(cursor.getColumnIndex("FOREX_CODE"));
                    sb.append(appendStringQueryVar(forexcode)); sb.append(",");

                    double paymentamount = cursor.getDouble(cursor.getColumnIndex("PAYMENT_AMOUNT"));
                    sb.append(Double.toString(paymentamount)); sb.append(",");

                    double changeamount = cursor.getDouble(cursor.getColumnIndex("CHANGE_AMOUNT"));
                    sb.append(Double.toString(changeamount)); sb.append(",");

                    double tenderamount = cursor.getDouble(cursor.getColumnIndex("TENDER_AMOUNT"));
                    sb.append(Double.toString(tenderamount)); sb.append(",");

                    String drawerdeclareid = cursor.getString(cursor.getColumnIndex("DRAWER_DECLARE_ID"));
                    sb.append(appendStringQueryVar(drawerdeclareid)); sb.append(",");

                    String modifieddate = cursor.getString(cursor.getColumnIndex("MODIFIED_DATE"));
                    sb.append(appendStringQueryVar(modifieddate)); sb.append(",");

                    String modifiedid = cursor.getString(cursor.getColumnIndex("MODIFIED_ID"));
                    sb.append(appendStringQueryVar(modifiedid));

                    sb.append(")");

                    rownumIn++;

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

