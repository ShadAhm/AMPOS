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
    ProgressDialog progress;
    DatabaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        mydb = DatabaseHelper.getHelper(this);

        enableButtons(false, false, false, false, false, false, false, false);

        boolean isSys = getIntent().getBooleanExtra("isSys", false);
        if (isSys) {
            enableButtons(false, false, false, false, false, false, true, true);
        } else {
            checkShiftsLongOperation();
        }
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
        //TODO if(checkIfTheresOpenShift()) {
        Intent intent = new Intent(this, EnterCustomerCodeActivity.class);
        startActivity(intent);
        //}
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
        DownloadDataService dds = new DownloadDataService(this, "http://175.136.237.81:8030/Service1.svc", this.getApplicationContext());

        try {
            dds.DownloadDataAsync();
        } catch (Exception e) {
            showMessage("Error", e.getMessage());
        }
    }

    public void onUploadDataClick(View view) {
        UploadDataService uds = new UploadDataService(this, "http://175.136.237.81:8030/Service1.svc", this.getApplicationContext());

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

        progress = new ProgressDialog(this);
        progress.setTitle("Downloading data...");
        progress.setMessage("Do not close this application");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
        showMessage("Success", "Data has been synced to the server");
    }

    @Override
    public void Wsdl2CodeFinishedWithException(Exception ex) {
        showMessage("Exception", ex.getMessage());
    }

    @Override
    public void Wsdl2CodeEndedRequest() {
        // To dismiss the dialog
        progress.dismiss();

        enableButtons(
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true
        );
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

        progress = new ProgressDialog(this);
        progress.setTitle("Uploading data...");
        progress.setMessage("Do not close this application");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }

    @Override
    public void UploadDataFinished(String methodName, Object Data) {
        showMessage("Success", "Data has been synced to the server");
    }

    @Override
    public void UploadDataFinishedWithException(Exception ex) {
        showMessage("Exception", ex.getMessage());
    }

    @Override
    public void UploadDataEndedRequest() {
        progress.dismiss();

        enableButtons(
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true
        );
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
                        if (todaysDateInString == pctrl.BUS_DATE) {
                            return "alreadyDayend";
                        } else {
                            pctrl.BUS_DATE = todaysDateInString;
                            pctrl.DAYEND = false;

                            mydb.deleteAndInsertPOSControl(db, pctrl);

                            Shift_Master openShift = mydb.lookForOpenShiftsAtDate(db, todaysDateInString);

                            if (openShift != null) {
                                return "shiftOpen";
                            } 
                            else {
                                return "noOpenShift";
                            }
                        }
                    }
                }
                db.setTransactionSuccessful();
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
                case "shiftOpen" : enableButtons(true, true, false, true, true, true, true, true);
                    break;
                case "alreadyDayend" : enableButtons(false, false, false, false, true, false, true, true); 
                    break; 
                case "noOpenShift" : showMessage("", "Please start a shift to continue");
                    enableButtons(false, false, true, false, true, false, true, true); 
                    break;
            }

            progress.dismiss();
        }

        @Override
        protected void onPreExecute() {
            enableButtons(false, false, false, false, false, false, false, false);

            progress = new ProgressDialog(_ctx);
            progress.setTitle("Checking shifts...");
            progress.setMessage("Do not close this application");
            progress.setCancelable(false);
            progress.show();
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
