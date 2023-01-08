package com.example.dummyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    boolean onHeat = true;
    boolean onChill = false;
    boolean onAuto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Home");

        ImageButton settingButt= findViewById(R.id.buttonSetting);
        settingButt.setOnClickListener(this);
        Button detailButt= findViewById(R.id.buttonDetail);
        detailButt.setOnClickListener(this);
        Button heaterButt= findViewById(R.id.buttonHeater);
        heaterButt.setOnClickListener(this);
        Button chillerButt= findViewById(R.id.buttonChiller);
        chillerButt.setOnClickListener(this);
        Button modeButt= findViewById(R.id.buttonMode);
        modeButt.setOnClickListener(this);
        SeekBar yourSeekBar= findViewById(R.id.seekBar2);
        yourSeekBar.setOnSeekBarChangeListener(new seekHandler());

        heatButtHandler();
        chillButtHandler();
    }

    @Override
    public void onClick(View v) {
        ImageButton settingButt= findViewById(R.id.buttonSetting);
        switch (v.getId()) {
            case R.id.buttonSetting:
                settingButt.setEnabled(false);
                break;
            case R.id.buttonDetail:
                Toast.makeText(this,"Clicked",Toast.LENGTH_LONG).show();
                Intent i = new Intent(this,ChartView.class);
                startActivity(i);
                break;
            case R.id.buttonHeater:
                heatButtHandler();
                break;
            case R.id.buttonChiller:
                chillButtHandler();
                break;
            case R.id.buttonMode:
                Toast.makeText(this,"Mode Changed",Toast.LENGTH_SHORT).show();
                onAuto = !onAuto;
                if (onAuto) {
                    ((TextView) findViewById(R.id.modeState)).setText(R.string.autoMode);
                }
                else {
                    ((TextView) findViewById(R.id.modeState)).setText(R.string.manuMode);
                }
                break;
            default:
                break;
        }
    }

    public void heatButtHandler() {
        if(onAuto) {
            Toast.makeText(this,"Auto is ON",Toast.LENGTH_SHORT).show();
        }
        else {
            onHeat = !onHeat;
        }
        ConstraintLayout heatButt = findViewById(R.id.heatButtHolder);
        TextView txt = findViewById(R.id.heatOnIndicator);
        if(onHeat) {
            txt.setText(R.string.on);
            heatButt.setBackgroundTintList(AppCompatResources.getColorStateList(this,R.color.butt_oncolor));
            txt.setTypeface(null, Typeface.BOLD);        }
        else {
            txt.setText(R.string.off);
            heatButt.setBackgroundTintList(AppCompatResources.getColorStateList(this,R.color.butt_offcolor));
            txt.setTypeface(null, Typeface.NORMAL);
        }
    }

    public void chillButtHandler() {
        if(onAuto) {
            Toast.makeText(this,"Auto is ON",Toast.LENGTH_SHORT).show();
        }
        else {
            onChill = !onChill;
        }
        ConstraintLayout heatButt = findViewById(R.id.chillButtHolder);
        TextView txt = findViewById(R.id.chillOnIndicator);
        if(onChill) {
            txt.setText(R.string.on);
            heatButt.setBackgroundTintList(AppCompatResources.getColorStateList(this,R.color.butt_oncolor));
            txt.setTypeface(null, Typeface.BOLD);        }
        else {
            txt.setText(R.string.off);
            heatButt.setBackgroundTintList(AppCompatResources.getColorStateList(this,R.color.butt_offcolor));
            txt.setTypeface(null, Typeface.NORMAL);
        }
    }

    private class seekHandler implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // Log the progress
            Log.d("DEBUG", "Progress is: "+progress);
            //set textView's text
            ((TextView)findViewById(R.id.temperValue)).setText(""+progress);
        }

        public void onStartTrackingTouch(SeekBar seekBar) {}

        public void onStopTrackingTouch(SeekBar seekBar) {}

    }
}