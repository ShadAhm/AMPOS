package com.example.thisi.applicationx;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;
import android.widget.Toast;

import java.io.IOException;

import rego.printlib.export.regoPrinter;

public class MainActivity 
extends AppCompatActivity implements IWsdl2CodeEvents {
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = DatabaseHelper.getHelper(this);

        if(!myDb.thereExistEmployees()) {
                DownloadEmployees();
        }
    }
    ProgressDialog downloadProgress;
    public void onLogin(View view) {
        SharedPreferences prefs = this.getSharedPreferences("com.example.thisi.applicationx", Context.MODE_PRIVATE);
        prefs.edit().putString("empcode", null).apply();
        prefs.edit().putString("empname", null).apply();

        EditText usernameTextbox = (EditText) findViewById(R.id.editText);
        EditText passwordTextbox = (EditText) findViewById(R.id.editText2);

        String username = usernameTextbox.getText().toString();
        String password = passwordTextbox.getText().toString();

        Employee loginOk = myDb.loginEmployee(username, password);

        if(username.equals("@@sys")) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra("isSys", true);
            finish();
            startActivity(intent);
        }
        else if (loginOk == null) {
            showMessage("Error", "Invalid username or password");
            return; 
        }
        else {
            prefs.edit().putString("empcode", loginOk.EMP_CODE).apply();
            prefs.edit().putString("empname", loginOk.EMP_NAME).apply();

            Intent intent = new Intent(this, MainMenuActivity.class);
            finish();
            startActivity(intent);
        }
    }

    private void DownloadEmployees() {
        SharedPreferences prefs = this.getSharedPreferences("com.example.thisi.applicationx", Context.MODE_PRIVATE);
        String serverConne = prefs.getString("serverconnection", null);

        if(serverConne != null) {
            DownloadDataService dds = new DownloadDataService(this, serverConne, this.getApplicationContext());

            try {
                dds.DownloadEmployeesAsync();
            } catch (Exception e) {
                showMessage("Error", e.getMessage());
            }
        }
        else {
            showMessage("No server connection", "Please login as @@sys and enter a server address in Settings");
        }
    }

    public void selfDestruct(View view) {
        EditText usernameTextbox = (EditText) findViewById(R.id.editText);
        EditText passwordTextbox = (EditText) findViewById(R.id.editText2);

        usernameTextbox.setText(null);
        passwordTextbox.setText(null);
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.show();
    }

    public void onRefresh(View view) {
        DownloadEmployees();
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public void Wsdl2CodeStartedRequest() {
        enableDisableControls(false);
        downloadProgress = new ProgressDialog(this);
        downloadProgress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        downloadProgress.show();
    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
        downloadProgress.dismiss();
    }

    @Override
    public void Wsdl2CodeFinishedWithException(Exception ex) {
        showMessage("Exception", ex.getMessage());
    }

    @Override
    public void Wsdl2CodeEndedRequest() {
        enableDisableControls(true);
    }

    private void enableDisableControls(boolean enable) {
        EditText usernameTextbox = (EditText) findViewById(R.id.editText);
        usernameTextbox.setEnabled(enable);

        EditText passwordTextbox = (EditText) findViewById(R.id.editText2);
        passwordTextbox.setEnabled(enable);

        Button loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setEnabled(enable);
        
        Button cancelButton = (Button)findViewById(R.id.cancelButton);
        cancelButton.setEnabled(enable);
    }

    @Override
    public void UploadDataStartedRequest() {
    }

    @Override
    public void UploadDataFinished(String methodName, Object Data) {
    }

    @Override
    public void UploadDataFinishedWithException(Exception ex) {
    }

    @Override
    public void UploadDataEndedRequest() {
    }

}
