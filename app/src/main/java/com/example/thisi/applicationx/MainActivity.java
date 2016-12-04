package com.example.thisi.applicationx;

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
    public ApplicationContext context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = (ApplicationContext) getApplicationContext();
        myDb = DatabaseHelper.getHelper(this);

        if(!myDb.thereExistEmployees()) {
                DownloadEmployees();
        }
    }

    public void printOrder1(View view) {
        connect("");
        context.getObject().CON_PageStart(context.getState(),false,0,0);
        context.getObject().ASCII_CtrlAlignType(context.getState(),
                preDefiniation.AlignType.AT_CENTER.getValue());
        context.getObject().ASCII_PrintString(context.getState(),0,
                1,0, 0, 0, "We finally can print now", "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);
        context.getObject().ASCII_PrintString(context.getState(),0,
                0, 0, 0, 0, "We finally can print now", "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);
        context.getObject().ASCII_PrintString(context.getState(),0,
                0, 0, 0, 0, "We finally can print now", "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);
        context.getObject().ASCII_PrintString(context.getState(),0,
                0, 0, 0, 0, "2015-01-01", "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);
        context.getObject().ASCII_PrintString(context.getState(),0,
                0, 0, 0, 0, "----------------------------",
                "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);
        context.getObject().ASCII_PrintString(context.getState(),0,
                0, 1, 0, 0, "We finally can print now",
                "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);
        context.getObject().ASCII_PrintString(context.getState(),0,
                0, 0, 0,
                0,"����           2.00      1      2.00", "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);
        context.getObject().ASCII_PrintString(context.getState(),0,
                0, 0, 0, 0, "----------------------------",
                "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);
        context.getObject().ASCII_PrintString(context.getState(),0,
                0, 0, 0, 0, "�ϼ�:��100Ԫ", "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);
        context.getObject().ASCII_PrintString(context.getState(),0,
                0, 0, 0, 0, "�Żݽ���:��90Ԫ", "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);
        context.getObject().ASCII_PrintString(context.getState(),0,
                0, 0, 0, 0, "�绰��01062985019", "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);
        context.getObject().ASCII_PrintString(context.getState(),0,
                0, 0, 0, 0, "��ַ��������ʵ����", "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);
        context.getObject().ASCII_PrintString(context.getState(),0,
                0, 0, 0, 0, "лл�ݹˣ���ӭ�´ι��٣�", "gb2312");
        context.getObject().ASCII_CtrlPrintCRLF(context.getState(),1);
        context.getObject().ASCII_CtrlReset(context.getState());
        context.getObject().ASCII_CtrlCutPaper(context.getState(), 66, 0);//切纸
        context.getObject().CON_PageEnd(context.getState(),context.getPrintway());
    }

    public int state;
    public boolean mBconnect = false;
    public void connect(String port) {
        // /dev/ttyMT1:115200 //kt45
        // /dev/ttyG1:115200 //tt43
        modelJudgmen();
        if (mBconnect) {
            context.getObject().CON_CloseDevices(context.getState());
            mBconnect = false;
        } else {
            if (state > 0) {
                Toast.makeText(this, R.string.mes_consuccess,
                        Toast.LENGTH_SHORT).show();

                mBconnect = true;
                context.setState(state);
                context.setName("RG-E48");

            } else {
                Toast.makeText(this, R.string.mes_confail,
                        Toast.LENGTH_SHORT).show();
                mBconnect = false;
            }
        }
    }
    private DeviceControl DevCtrl;
    private boolean isTT43 = false;
    private void modelJudgmen() {
        state = context.getObject().CON_ConnectDevices("RG-E487", "/dev/ttyMT1:115200", 200);
        Toast.makeText(
                this,
                "" + android.os.Build.MODEL + " release:"
                        + android.os.Build.VERSION.RELEASE, Toast.LENGTH_LONG)
                .show();

        if (android.os.Build.VERSION.RELEASE.equals("5.1")) {
            DevCtrl = new DeviceControl(DeviceControl.powerPathKT);
            DevCtrl.setGpio(94);
            isTT43 = false;
            try {
                DevCtrl.PowerOnMTDevice();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onLogin(View view) {
        EditText usernameTextbox = (EditText) findViewById(R.id.editText);
        EditText passwordTextbox = (EditText) findViewById(R.id.editText2);

        String username = usernameTextbox.getText().toString();
        String password = passwordTextbox.getText().toString();

        boolean loginOk = myDb.loginEmployee(username, password);

        Intent intent = new Intent(this, MainMenuActivity.class);
        
        if (!loginOk) {
            showMessage("Error", "Invalid username or password");
            return; 
        }

        finish();
        startActivity(intent);
    }

    private void DownloadEmployees() {
        SharedPreferences prefs = this.getSharedPreferences("com.example.thisi.applicationx", Context.MODE_PRIVATE);
        String serverConne = prefs.getString("serverconnection", "http://175.136.237.81:8030/Service1.svc");

        DownloadDataService dds = new DownloadDataService(this, serverConne, this.getApplicationContext());

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
    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
        if(Data == "success") {
            enableDisableControls(true);
        }
        else {
            showMessage("System unavailable", "Please hit Refresh and try again later");
            enableDisableControls(false);
        }
    }

    @Override
    public void Wsdl2CodeFinishedWithException(Exception ex) {
        showMessage("Exception", ex.getMessage());
    }

    @Override
    public void Wsdl2CodeEndedRequest() {

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
