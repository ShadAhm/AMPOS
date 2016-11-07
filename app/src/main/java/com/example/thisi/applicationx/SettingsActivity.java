package com.example.thisi.applicationx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by thisi on 10/28/2016.
 */

public class SettingsActivity extends Activity implements AdapterView.OnItemSelectedListener {
    private String selectedDefaultPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupSpinner();

        populatePreviousValues();
    }

    private void populatePreviousValues() {
        SharedPreferences prefs = this.getSharedPreferences("com.example.thisi.applicationx", Context.MODE_PRIVATE);

        String serverConne = prefs.getString("serverconnection", "http://175.136.237.81:8030/Service1.svc");
        String defPric = prefs.getString("defaultprice", "PRICE_01");

        EditText textServerConnection = (EditText)findViewById(R.id.textServerConnection);

        textServerConnection.setText(serverConne);

        int selectedIndex = 0;

        switch (defPric) {
            case "PRICE_01":  selectedIndex = 0;
                break;
            case "PRICE_02":  selectedIndex = 1;
                break;
            case "PRICE_03":  selectedIndex = 2;
                break;
            case "PRICE_04":  selectedIndex = 3;
                break;
            case "PRICE_05":  selectedIndex = 4;
                break;
            case "PRICE_06":  selectedIndex = 5;
                break;
            case "PRICE_07":  selectedIndex = 6;
                break;
            case "PRICE_08":  selectedIndex = 7;
                break;
            case "PRICE_09":  selectedIndex = 8;
                break;
            case "PRICE_10":  selectedIndex = 9;
                break;
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinnerDefaultPrice);
        spinner.setSelection(selectedIndex);
    }

    private void setupSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinnerDefaultPrice);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.prices_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);
    }

    public void onCancelClick(View view)
    {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        String p = parent.getItemAtPosition(pos).toString();

        switch (p) {
            case "Price 1":  selectedDefaultPrice = "PRICE_01";
                break;
            case "Price 2":  selectedDefaultPrice = "PRICE_02";
                break;
            case "Price 4":  selectedDefaultPrice = "PRICE_04";
                break;
            case "Price 5":  selectedDefaultPrice = "PRICE_05";
                break;
            case "Price 6":  selectedDefaultPrice = "PRICE_06";
                break;
            case "Price 7":  selectedDefaultPrice = "PRICE_07";
                break;
            case "Price 8":  selectedDefaultPrice = "PRICE_08";
                break;
            case "Price 9":  selectedDefaultPrice = "PRICE_09";
                break;
            case "Price 10":  selectedDefaultPrice = "PRICE_10";
                break;
            case "Price 3":  selectedDefaultPrice = "PRICE_03";
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void onSaveClick(View view)
    {
        SharedPreferences prefs = this.getSharedPreferences("com.example.thisi.applicationx", Context.MODE_PRIVATE);

        EditText textServerConnection = (EditText)findViewById(R.id.textServerConnection);

        String serverConnection = textServerConnection.getText().toString();

        prefs.edit().putString("serverconnection", serverConnection).apply();
        prefs.edit().putString("defaultprice", selectedDefaultPrice).apply();

        Toast.makeText(getApplicationContext(), "Changes saved", Toast.LENGTH_SHORT).show();
    }
}
