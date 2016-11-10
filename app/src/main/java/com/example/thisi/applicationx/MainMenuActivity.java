package com.example.thisi.applicationx;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by thisi on 10/24/2016.
 */

public class MainMenuActivity extends Activity implements IWsdl2CodeEvents{
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        boolean isSys = getIntent().getBooleanExtra("isSys", false);

        if(isSys) {
            enableButtons(
                    false,
                    false,
                    true,
                    false,
                    true,
                    true
            );
        }
    }

    private void enableButtons(boolean enableOrder, boolean enableDayend, boolean enableDownload, boolean enableUpload, boolean enableLogout, boolean enableSettings) {
        Button btnOrder = (Button)findViewById(R.id.buttonOrder);
        btnOrder.setEnabled(enableOrder);

        Button buttonDayend = (Button)findViewById(R.id.buttonDayend);
        buttonDayend.setEnabled(enableDayend);

        Button btnDownloadData = (Button)findViewById(R.id.buttonDownloadData);
        btnDownloadData.setEnabled(enableDownload);

        Button buttonUploadData = (Button)findViewById(R.id.buttonUploadData);
        buttonUploadData.setEnabled(enableUpload);

        Button buttonLogout = (Button)findViewById(R.id.buttonLogout);
        buttonLogout.setEnabled(enableLogout);

        Button buttonSettings = (Button)findViewById(R.id.buttonSettings);
        buttonSettings.setEnabled(enableSettings);
    }

    public void onOrderButtonClick(View view) {
        Intent intent = new Intent(this, EnterCustomerCodeActivity.class);
        startActivity(intent);
    }

    public void onSettingsClick(View view)
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onLogoutClick(View view)
    {
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

    public void onDownloadDataClick(View view) {
        DownloadDataService fhh = new DownloadDataService(this, "http://175.136.237.81:8030/Service1.svc", this.getApplicationContext());

        try {
            fhh.DownloadDataAsync();
        }
        catch(Exception e)
        {
            showMessage("Error", e.getMessage());
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
            false
        );
        
        progress = new ProgressDialog(this);
        progress.setTitle("Uploading data...");
        progress.setMessage("Do not close this application");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        }
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
                true
        );
    }

    public boolean thereExistSuspends() {

    }
}
