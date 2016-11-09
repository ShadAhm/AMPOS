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

        if (rowCount > 0) {
            while (cursor.moveToNext()) {
                String nameColumnValue = cursor.getString(cursor.getColumnIndex("PROD_NAME"));
                String rmColumnValue = cursor.getString(cursor.getColumnIndex("PRICE"));

                if(rmColumnValue == null)
                    rmColumnValue = "0.0";

                BigDecimal bigDecimalRMColumnValue = new BigDecimal(rmColumnValue);

            }

            cursor.close();
        }
    }
}

