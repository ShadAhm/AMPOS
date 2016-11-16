package com.example.thisi.applicationx;

import android.content.Intent;
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

public class MainActivity 
extends AppCompatActivity implements IWsdl2CodeEvents {
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = DatabaseHelper.getHelper(this);

        DownloadEmployees();
    }

    public void onLogin(View view) {
        EditText usernameTextbox = (EditText) findViewById(R.id.editText);
        EditText passwordTextbox = (EditText) findViewById(R.id.editText2);

        String username = usernameTextbox.getText().toString();
        String password = passwordTextbox.getText().toString();

        boolean loginOk = myDb.loginEmployee(username, password);
        boolean isSys = username.equals("@sys") && password.equals("123");

        Intent intent = new Intent(this, MainMenuActivity.class);
        
        if (!loginOk && !isSys) {
            showMessage("Error", "Invalid username or password");
            return; 
        } else if (isSys) {
            intent.putExtra("isSys", true);
        }

        finish();
        startActivity(intent);
    }

    private void DownloadEmployees() {
        DownloadDataService dds = new DownloadDataService(this, "http://175.136.237.81:8030/Service1.svc", this.getApplicationContext());

        try {
            dds.DownloadEmployeesAsync();
        }
        catch(Exception e)
        {
            showMessage("Error", e.getMessage());
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

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public void Wsdl2CodeStartedRequest() {
        enableDisableControls(false);
    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
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
