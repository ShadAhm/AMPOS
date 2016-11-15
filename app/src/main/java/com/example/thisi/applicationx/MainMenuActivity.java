package com.example.thisi.applicationx;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteStatement;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.content.Intent;
import android.widget.Button;

import org.ksoap2.serialization.SoapObject;

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
        boolean isSys = getIntent().getBooleanExtra("isSys", false);

        if (isSys) {
            enableButtons(
                    false,
                    false,
                    false,
                    false,
                    true,
                    false,
                    true,
                    true
            );
        }
        else {
            checkStateOfShiftsToEnableDisableButtons(); 
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

    private void checkStateOfShiftsToEnableDisableButtons() {
        new LongOperation().execute("");
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
        StartShiftLongOperation.execute(""); 
    }

    public void onEndShiftClick(View view) {

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

    private class StartShiftLongOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            SQLiteDatabase db = dataHelper.getReadableDatabase();

            db.beginTransaction();
            try {
                db.setTransactionSuccessful();
            } catch (SQLiteException e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
                db.close();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            
        }

        @Override
        protected void onPreExecute() {

        }
    }

    // Hi Shad,

    // Attach are the files that needed to develop a start shift and end shift function. Below is the logic:

    // Start shift: 
    // 1) User must start a shift in order to start to place order, if there is no shift started then system should prompt user a message ask to start shift when user press on order.
    // 2) User can only have one shift at a time. No multiple concurrent shift allow. But user can have multiple shift in a day (if they done shift start and shift end properly). Thus you need to check the table provided in the excel, to make sure there is one and only one shift started.
    // 3) User are require to enter float amount when start a shift. Float amount is a text box that accept any number of money include 0.00. No negative amount is allowed. This amount 

    // Shift end: 
    // 1) Shift end is to close the shift started. And to consolidate all the transactions done within that shift. 
    // 2) User must declare the money on their hand during shift end. Formula as below:
    //           ---> DECLARE AMOUNT (CASH ON HAND)  = FLOAT AMOUNT + TOTAL PAYMENT RECEIVED by CASH In the shift.
    //           ---> FLOAT = 100.00, CASH = 389.00, CREDIT = 250.00.
    //           ---> CASH ON HAND = 100 + 389 = 489.00. (Not include credit)
    // 3) User cannot perform dayend if there is a shift started and not yet close.
    // 4) Shift end will print out a shift report to show some basic info. Provide later.
    // 5) If there is any single HOLD ORDER in the system, system should now allow to shift end.
    
    // Changes in Dayend:
    // 1) Whenever there is a shift haven't end, user can't do dayend. 
    // 2) Day end will consolidate transactions from all shifts in a day.
    // 3) If user do not do dayend of today, they can't perform shift start or order on tomorrow, system must prompt user to ask them perform proper shift end and dayend before start a new shift on tomorrow. 

    // For table details, please refer the excel. Thanks.
}
