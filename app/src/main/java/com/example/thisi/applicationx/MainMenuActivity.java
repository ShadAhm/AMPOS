package com.example.thisi.applicationx;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.media.audiofx.BassBoost;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.content.Intent;
import android.widget.Button;

import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by thisi on 10/24/2016.
 */

public class MainMenuActivity extends Activity implements IWsdl2CodeEvents {
    ProgressDialog downloadProgress;
    ProgressDialog uploadProgress;
    DatabaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        mydb = DatabaseHelper.getHelper(this);

        enableButtons(false, false, false, false, false, false, false, false);
        checkShiftsLongOperation();
    }

    private void enableButtons(boolean enableOrder, boolean enableDayend, boolean enableStartShift, boolean enableEndShift, boolean enableDownload, boolean enableUpload, boolean enableLogout, boolean enableSettings) {
        Button btnOrder = (Button) findViewById(R.id.buttonOrder);
        btnOrder.setEnabled(enableOrder);

        Button buttonDayend = (Button) findViewById(R.id.buttonDayend);
        buttonDayend.setEnabled(enableDayend);

        Button btnStartShift = (Button) findViewById(R.id.buttonStartShift);
        btnStartShift.setEnabled(enableStartShift);

        Button btnEndShift = (Button) findViewById(R.id.buttonEndShift);
        btnEndShift.setEnabled(enableEndShift);

        Button btnDownloadData = (Button) findViewById(R.id.buttonDownloadData);
        btnDownloadData.setEnabled(enableDownload);

        Button buttonUploadData = (Button) findViewById(R.id.buttonUploadData);
        buttonUploadData.setEnabled(enableUpload);

        Button buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonLogout.setEnabled(enableLogout);

        Button buttonSettings = (Button) findViewById(R.id.buttonSettings);
        buttonSettings.setEnabled(enableSettings);
    }

    public void onOrderButtonClick(View view) {
        Intent intent = new Intent(this, EnterCustomerCodeActivity.class);
        startActivity(intent);
    }

    public void onSettingsClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onLogoutClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.show();
    }

    public void onStartShiftClick(View view) {
        Intent intent = new Intent(this, EnterFloatAmountActivity.class);
        startActivity(intent);
    }

    public void onEndShiftClick(View view) {
        Intent intent = new Intent(this, DeclareShiftMoneyActivity.class);
        startActivity(intent);
    }

    public void onDownloadDataClick(View view) {
        SharedPreferences prefs = this.getSharedPreferences("com.example.thisi.applicationx", Context.MODE_PRIVATE);
        String serverConne = prefs.getString("serverconnection", null);

        if(serverConne != null) {
            DownloadDataService dds = new DownloadDataService(this, serverConne, this.getApplicationContext());

            try {
                dds.DownloadDataAsync();
            } catch (Exception e) {
                showMessage("Error", e.getMessage());
            }
        }
        else {
            showMessage("Error", "Could not find server. Please go to Settings and ensure the server connection is correct.")
        }
    }

    public void onUploadDataClick(View view) {
        SharedPreferences prefs = this.getSharedPreferences("com.example.thisi.applicationx", Context.MODE_PRIVATE);
        String serverConne = prefs.getString("serverconnection", null);

        if(serverConne != null) {
            UploadDataService uds = new UploadDataService(this, serverConne, this.getApplicationContext());

            if (!thereExistSuspends()) {
                try {
                    uds.UploadDataAsync();
                } catch (Exception e) {
                    showMessage("Error", e.getMessage());
                }
            } else {
                showMessage("Error", "There are still orders on-hold");
            }
        }
        else {
            showMessage("Error", "Could not find server. Please go to Settings and ensure the server connection is correct.")
        }
    }

    public void onDayendClick(View view) {
        if (!thereExistSuspends()) {
                SQLiteDatabase db = mydb.getReadableDatabase();
                db.beginTransaction();
                try {
                    POS_Control pctrl = mydb.getPOSControl(db);
                    pctrl.DAYEND = true;
                    mydb.deleteAndInsertPOSControl(db, pctrl);
                    db.setTransactionSuccessful();

                    finish(); 
                    this.recreate();
                } catch (SQLiteException e) {
                    e.printStackTrace();
                    showMessage("Error", e.getMessage());
                } finally {
                    db.endTransaction();
                    db.close();
                }
            } else {
                showMessage("Error", "There are still orders on-hold");
            }
    }

    @Override
    public void Wsdl2CodeStartedRequest() {
        enableButtons(
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false
        );

        downloadProgress = new ProgressDialog(this);
        downloadProgress.setTitle("Downloading data...");
        downloadProgress.setMessage("Do not close this application");
        downloadProgress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        downloadProgress.show();
    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
        if(Data == "success") {
            showMessage("Success", "Data has been synced to the server");
        }
        else {
            showMessage("Failed to sync data", "Please try again later");
        }
        finish(); 
        this.recreate();
    }

    @Override
    public void Wsdl2CodeFinishedWithException(Exception ex) {
        showMessage("Exception", ex.getMessage());
    }

    @Override
    public void Wsdl2CodeEndedRequest() {
        // To dismiss the dialog
        downloadProgress.dismiss();
    }

    @Override
    public void UploadDataStartedRequest() {
        enableButtons(
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false
        );

        uploadProgress = new ProgressDialog(this);
        uploadProgress.setTitle("Uploading data...");
        uploadProgress.setMessage("Do not close this application");
        uploadProgress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        uploadProgress.show();
    }

    @Override
    public void UploadDataFinished(String methodName, Object Data) {
        if(Data == "success") {
            showMessage("Success", "Data has been synced to the server");
        }
        else {
            showMessage("Failure to sync data", "Please try again later");
        }
    }

    @Override
    public void UploadDataFinishedWithException(Exception ex) {
        showMessage("Exception", ex.getMessage());
    }

    @Override
    public void UploadDataEndedRequest() {
        uploadProgress.dismiss();
        finish(); 
        this.recreate();
    }

    public boolean thereExistSuspends() {
        return mydb.thereExistSuspends();
    }

    private void checkShiftsLongOperation() {
        SharedPreferences prefs = this.getSharedPreferences("com.example.thisi.applicationx", Context.MODE_PRIVATE);
        new checkShiftsLongOperationTask(this, prefs).execute();
    }

    public class checkShiftsLongOperationTask extends AsyncTask<Void, Void, String> {
        public MainMenuActivity _ctx;
        public SharedPreferences _sp;

        public checkShiftsLongOperationTask(MainMenuActivity ctx, SharedPreferences sp) {
            this._ctx = ctx;
            this._sp = sp;
        }

        @Override
        protected String doInBackground(Void... params) {
            if (!isSettingsComplete()) {
                return "settingsIncomplete";
            }

            SQLiteDatabase db = mydb.getReadableDatabase();

            db.beginTransaction();
            try {
                POS_Control pctrl = mydb.getPOSControl(db);

                String todaysDateInString = new SimpleDateFormat("yyyyMMdd").format(new Date());

                if (pctrl == null) {
                    String posNo = _sp.getString("posnumber", null);
                    String companyCode = _sp.getString("companycode", null);

                    POS_Control newPostControl = new POS_Control();
                    newPostControl.COMPANY_CODE = companyCode;
                    newPostControl.OUTLET_CODE = companyCode;
                    newPostControl.POS_NO = posNo;
                    newPostControl.BUS_DATE = todaysDateInString;
                    newPostControl.SHIFT_NUMBER = 0;
                    newPostControl.REPRINT_COUNT = 0;
                    newPostControl.DAYEND = false;
                    mydb.deleteAndInsertPOSControl(db, newPostControl);

                    Shift_Master openShift = mydb.lookForOpenShiftsAtDate(db, todaysDateInString);

                    db.setTransactionSuccessful();
                    if (openShift != null) {
                        return "shiftOpen";
                    } 
                    else {
                        return "noOpenShift";
                    }
                } 
                else {
                    if (pctrl.DAYEND) // checks if latest POS_Control already dayend
                    {
                        if (todaysDateInString.equals(pctrl.BUS_DATE)) {
                            return "alreadyDayend";
                        } else {
                            Shift_Master openShift = mydb.lookForOpenShiftsAtDate(db, todaysDateInString);

                            db.setTransactionSuccessful();
                            if (openShift != null) {
                                return "shiftOpen";
                            } 
                            else {
                                return "noOpenShift";
                            }
                        }
                    }
                    else {
                        Shift_Master openShift = mydb.lookForOpenShiftsAtDate(db, todaysDateInString);
                        db.setTransactionSuccessful();
                        if (openShift != null) {
                            return "shiftOpen";
                        }
                        else {
                            return "noOpenShift";
                        }
                    }
                }
            } catch (SQLiteException e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
                db.close();
            }

            return "bulldog";
        }

        @Override
        protected void onPostExecute(String result) {
            switch(result) 
            {
                case "settingsIncomplete" : showMessage("Settings Incomplete", "Please go to settings and complete all required information"); 
                    enableButtons(false, false, false, false, false, false, true, true); 
                    break; 
                case "shiftOpen" : enableButtons(true, false, false, true, true, false, true, true);
                    break;
                case "alreadyDayend" :
                    showMessage("Day Ended", "Dayend has already been performed for today.");
                    enableButtons(false, false, false, false, true, true, true, true);
                    break; 
                case "noOpenShift" :
                    enableButtons(false, true, true, false, true, false, true, true);
                    break;
            }
        }

        @Override
        protected void onPreExecute() {
            enableButtons(false, false, false, false, false, false, false, false);
        }

        protected boolean isSettingsComplete() {
            String default_price_field = _sp.getString("defaultprice", null);
            String posNo = _sp.getString("posnumber", null);
            String companyCode = _sp.getString("companycode", null);
            String serverConne = _sp.getString("serverconnection", null);

            if (default_price_field == null || posNo == null || companyCode == null || serverConne == null
                    || default_price_field.trim().isEmpty() || posNo.trim().isEmpty() || companyCode.trim().isEmpty() || serverConne.trim().isEmpty()) {
                return false;
            }
            return true;
        }
    }
}
