package com.example.dummyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Home");

        ImageButton settingButt= (ImageButton) findViewById(R.id.buttonSetting);
        settingButt.setOnClickListener(this);
        Button detailButt= (Button) findViewById(R.id.buttonDetail);
        detailButt.setOnClickListener(this);
        Button heaterButt= (Button) findViewById(R.id.buttonHeater);
        heaterButt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ImageButton settingButt= (ImageButton) findViewById(R.id.buttonSetting);
        switch (v.getId()) {
            case R.id.buttonSetting:
                settingButt.setEnabled(false);
                break;
            case R.id.buttonDetail:
                Toast.makeText(this,"clicked",Toast.LENGTH_LONG).show();
                break;
            case R.id.buttonHeater:
                Toast.makeText(this,"heated",Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }
}