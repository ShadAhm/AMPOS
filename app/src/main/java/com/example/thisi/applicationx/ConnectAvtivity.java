package com.example.thisi.applicationx;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class ConnectAvtivity extends Activity {

    public int state;
    public String PrintName;
    public Button con;
    public Spinner com;
    public TextView version;
    public ApplicationContext context;
    public String error;
    public boolean mBconnect = false;
    public static ConnectAvtivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        mActivity = this;

        context = (ApplicationContext) getApplicationContext();
        context.setObject();

        connect("");
    }

    private DeviceControl DevCtrl;

    public void connect(String port) {
        // /dev/ttyMT1:115200 //kt45
        // /dev/ttyG1:115200 //tt43
        modelJudgmen();
        if (mBconnect) {
            context.getObject().CON_CloseDevices(context.getState());

            con.setText(R.string.button_btcon);
            mBconnect = false;
        } else {
            if (state > 0) {
                Toast.makeText(ConnectAvtivity.this, R.string.mes_consuccess,
                        Toast.LENGTH_SHORT).show();

                mBconnect = true;

                Intent intent = new Intent(ConnectAvtivity.this,
                        MainActivity.class);
                context.setState(state);
                context.setName("RG-E48");
                startActivity(intent);

            } else {
                Toast.makeText(ConnectAvtivity.this, R.string.mes_confail,
                        Toast.LENGTH_SHORT).show();
                mBconnect = false;
            }
        }
    }

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

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            if (isTT43) {
                DevCtrl.PowerOffDevice();
            } else {
                DevCtrl.PowerOffMTDevice();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
