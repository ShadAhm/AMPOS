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
    public Spinner type;
    public Spinner usb;
    public Spinner com;
    public Spinner combaud;
    public Spinner porttype;
    public Spinner printway;
    public Spinner btName;
    private ArrayAdapter<String> mAdapter;
    public LinearLayout show;
    public View view1;
    public TextView link;
    public TextView version;
    public Button linktest;
    public EditText wifiport;
    public EditText wifiip;
    public EditText Adress;
    public ApplicationContext context;
    public String error;
    public boolean mBconnect = false;
    ArrayList<String> getbtName = new ArrayList<String>();
    ArrayList<String> getbtNM = new ArrayList<String>();
    ArrayList<String> getbtMax = new ArrayList<String>();
    public static ConnectAvtivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        mActivity = this;
        // ����ҳ��֮�����ݹ���
        context = (ApplicationContext) getApplicationContext();
        context.setObject();

        linktest = (Button) findViewById(R.id.TextView_linktest);
        link = (TextView) findViewById(R.id.TextView_link);
        version = (TextView) findViewById(R.id.text_version);
        version.setText("V " + context.getObject().CON_QueryVersion());
        type = (Spinner) findViewById(R.id.spinner_type);
        porttype = (Spinner) findViewById(R.id.spinner_porttype);
        printway = (Spinner) findViewById(R.id.spinner_printway);
        show = (LinearLayout) findViewById(R.id.linearLayout0);

        ArrayList<String> printName = new ArrayList<String>();
        printName = (ArrayList<String>) context.getObject()
                .CON_GetSupportPrinters();

        mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, printName);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(mAdapter);

        String[] printInterface = context.getObject().CON_GetSupportPageMode();
        mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, printInterface);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        printway.setAdapter(mAdapter);

        type.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                PrintName = parent.getItemAtPosition(position).toString();

            }

            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        linktest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mBconnect) {
                    Intent intent = new Intent(ConnectAvtivity.this,
                            MainActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(ConnectAvtivity.this, R.string.mes_nextpage,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        porttype.setOnItemSelectedListener(new OnItemSelectedListener() {
            @SuppressLint("InflateParams")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {
                    case 0:// bt
                        view1 = getLayoutInflater().inflate(
                                R.layout.activity_bluetooth, null); // �°벿�ָı�����Layout
                        show.removeAllViews();
                        show.addView(view1);

                        Adress = (EditText) findViewById(R.id.edit_btmax);
                        btName = (Spinner) findViewById(R.id.spinner_btname);

                        getbtName.clear();
                        mAdapter = new ArrayAdapter<String>(ConnectAvtivity.this,
                                android.R.layout.simple_spinner_item, getbtName);
                        btName.setAdapter(mAdapter);

                        getbtNM = (ArrayList<String>) context.getObject()
                                .CON_GetWirelessDevices(0);
                        // �Ի��õ�������ַ�����ƽ��в����Զ��Ž��в���
                        for (int i = 0; i < getbtNM.size(); i++) {
                            getbtName.add(getbtNM.get(i).split(",")[0]);
                            getbtMax.add(getbtNM.get(i).split(",")[1].substring(0,
                                    17));
                        }

                        mAdapter = new ArrayAdapter<String>(ConnectAvtivity.this,
                                android.R.layout.simple_spinner_item, getbtName);
                        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        btName.setAdapter(mAdapter);
                        btName.setOnItemSelectedListener(new OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> arg0,
                                                       View arg1, int arg2, long arg3) {
                                // TODO Auto-generated method stub
                                Adress.setText(getbtMax.get(arg2));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub
                            }
                        });

                        // btName����Ҫ���Ӽ����¼���������
                        con = (Button) findViewById(R.id.button_btcon);
                        con.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                connect(Adress.getText().toString());
                            }
                        });
                        break;
                    case 1:// wifi
                        view1 = getLayoutInflater().inflate(R.layout.activity_wifi,
                                null); // �°벿�ָı�����Layout
                        show.removeAllViews();
                        show.addView(view1);
                        wifiport = (EditText) findViewById(R.id.edit_wifiport);
                        wifiport.setText("9100");
                        wifiip = (EditText) findViewById(R.id.edit_wifiip);
                        wifiip.setText("192.168.1.199");
                        con = (Button) findViewById(R.id.button_wificon);
                        con.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                connect(wifiip.getText().toString() + ":"
                                        + wifiport.getText().toString());
                            }
                        });
                        break;
                    case 2:// usb
                        view1 = getLayoutInflater().inflate(R.layout.activity_usb,
                                null); // �°벿�ָı�����Layout
                        show.removeAllViews();
                        show.addView(view1);
                        con = (Button) findViewById(R.id.button_usbcon);
                        usb = (Spinner) findViewById(R.id.spinner_usb);
                        con.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                connect("usb");
                            }
                        });
                        break;
                    case 3:// com
                        view1 = getLayoutInflater().inflate(R.layout.activity_com,
                                null); // �°벿�ָı�����Layout
                        show.removeAllViews();
                        show.addView(view1);
                        con = (Button) findViewById(R.id.button_comcon);
                        com = (Spinner) findViewById(R.id.spinner_com);
                        combaud = (Spinner) findViewById(R.id.spinner_Baud);

                        con.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // connect(com.getSelectedItem().toString());
                                connect(com.getSelectedItem().toString() + ":"
                                        + combaud.getSelectedItem().toString());

                            }
                        });
                        break;
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        link.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ConnectAvtivity.this,
                        LinkContactActivity.class);
                startActivity(intent);
            }
        });
        connect("");

    }

    private DeviceControl DevCtrl;

    public void connect(String port) {
        // /dev/ttyMT1:115200 //kt45
        // /dev/ttyG1:115200 //tt43

        // �����жϷ���
        modelJudgmen();
        if (mBconnect) {
            context.getObject().CON_CloseDevices(context.getState());

            con.setText(R.string.button_btcon);// "����"
            mBconnect = false;
        } else {

            System.out.println("----RG---CON_ConnectDevices");

            if (state > 0) {
                Toast.makeText(ConnectAvtivity.this, R.string.mes_consuccess,
                        Toast.LENGTH_SHORT).show();

                mBconnect = true;
                // con.setText(R.string.TextView_close);// "�ر�"
                Intent intent = new Intent(ConnectAvtivity.this,
                        MainActivity.class);
                context.setState(state);
                context.setName("RG-E48");
                context.setPrintway(printway.getSelectedItemPosition());
                startActivity(intent);
                // finish();
            } else {
                Toast.makeText(ConnectAvtivity.this, R.string.mes_confail,
                        Toast.LENGTH_SHORT).show();
                mBconnect = false;
                // finish();
                // con.setText(R.string.button_btcon);// "����"
            }
        }
    }

    // �����ж�

    private boolean isTT43 = false;

    private void modelJudgmen() {
        // Ĭ��45 45��45Q�Ĵ�ӡ��������ͬ gpio��ͬ
        state = context.getObject().CON_ConnectDevices("RG-E487",
                "/dev/ttyMT1:115200", 200);
        Toast.makeText(
                this,
                "" + android.os.Build.MODEL + " release:"
                        + android.os.Build.VERSION.RELEASE, Toast.LENGTH_LONG)
                .show();
        // kt45
        if (android.os.Build.VERSION.RELEASE.equals("4.4.2")) {
            DevCtrl = new DeviceControl(DeviceControl.powerPathKT);
            isTT43 = false;
            try {
                DevCtrl.PowerOnMTDevice();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if (android.os.Build.VERSION.RELEASE.equals("5.1")) {
            DevCtrl = new DeviceControl(DeviceControl.powerPathKT);
            DevCtrl.setGpio(94);
            isTT43 = false;
            try {
                DevCtrl.PowerOnMTDevice();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // // TT43
        else if (android.os.Build.VERSION.RELEASE.equals("4.0.3")) {
            // /dev/ttyG1:115200
            state = context.getObject().CON_ConnectDevices("RG-E487",
                    "/dev/ttyG1:115200", 200);
            DevCtrl = new DeviceControl(DeviceControl.powerPathTT);
            isTT43 = true;
            try {
                DevCtrl.PowerOnDevice();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    // �����˳�ʱ��Ҫ�رյ�Դ ʡ��
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
