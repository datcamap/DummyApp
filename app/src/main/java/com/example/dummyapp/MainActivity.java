package com.example.dummyapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.example.dummyapp.data.model.AquaDeviceTwin;
import com.example.dummyapp.data.model.Data_O_Sensors;
import com.example.dummyapp.data.model.Desired;
import com.example.dummyapp.data.model.SensorsData;
import com.example.dummyapp.data.remote.ApiUtils;
import com.example.dummyapp.data.remote.AquaService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    int minTemper = 15;
    Desired desired = new Desired();
    Data_O_Sensors dataOSensors = new Data_O_Sensors();
    public AquaService aquaService;
    private final Handler mainHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Home");

        aquaService = ApiUtils.getAquaService();
        loadAnswers();

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

        BackgroundThread updateSensor = new BackgroundThread();
        new Thread(updateSensor).start();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonSetting:
                loadSensorValues();
                break;
            case R.id.buttonDetail:
                Toast.makeText(this,"Clicked",Toast.LENGTH_LONG).show();
                Intent i = new Intent(this,ChartView.class);
                startActivity(i);
                break;
            case R.id.buttonHeater:
                if(desired.getAutoMode()) {
                    Toast.makeText(this,"Auto is ON",Toast.LENGTH_SHORT).show();
                }
                else {
                    desired.setHeater(!desired.getHeater());
                }
                heatButtHandler();
                break;
            case R.id.buttonChiller:
                if(desired.getAutoMode()) {
                    Toast.makeText(this,"Auto is ON",Toast.LENGTH_SHORT).show();
                }
                else {
                    desired.setChiller(!desired.getChiller());
                }
                chillButtHandler();
                break;
            case R.id.buttonMode:
                Toast.makeText(this,"Mode Changed",Toast.LENGTH_SHORT).show();
                desired.setAutoMode(!desired.getAutoMode());
                autoButtHandler();
                break;
            default:
                break;
        }
    }

    public void loadSensorValues() {

        aquaService.getLastestSensor().enqueue(new Callback<SensorsData>() {
            @Override
            public void onResponse(@NonNull Call<SensorsData> call, @NonNull Response<SensorsData> response) {
                if (response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    dataOSensors = gson.fromJson(response.body().getData(),Data_O_Sensors.class);
                    Log.d("MainActivity", response.body().getData());
                    sensorsTextHandler();
                }
            }
            @Override
            public void onFailure(@NonNull Call<SensorsData> call, @NonNull Throwable t) {
                Log.d("MainActivity", t.getMessage());
            }
        });
    }

    public void loadAnswers() {

        aquaService.getAnswers().enqueue(new Callback<AquaDeviceTwin>() {
            @Override
            public void onResponse(@NonNull Call<AquaDeviceTwin> call, @NonNull Response<AquaDeviceTwin> response) {
                if (response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    desired = gson.fromJson(response.body().getData().getDesired(), Desired.class);
                    Log.d("MainActivity", response.body().getData().getDesired());
                    seekbarTextHandler();
                    heatButtHandler();
                    chillButtHandler();
                    autoButtHandler();
                    Log.d("MainActivity", "posts loaded from API");
                } else {
                    int statusCode = response.code();
                    // handle request errors depending on status code
                    Log.d("MainActivity", response.toString());
                }
            }
            @Override
            public void onFailure(@NonNull Call<AquaDeviceTwin> call, @NonNull Throwable t) {
                Log.d("MainActivity", t.getMessage());
            }
        });
    }

    public void sensorsTextHandler(){

        try {
            DecimalFormat formatter = new DecimalFormat("#0.0");
            String temperValue = getString(R.string.stringPlaceHolderWithDegreeSymbol,formatter.format(dataOSensors.getNhietdo()));
            ((TextView) findViewById(R.id.temperDisplay)).setText(temperValue);
            ((TextView) findViewById(R.id.tdsDisplay)).setText(formatter.format(dataOSensors.getTds()));
            ((TextView) findViewById(R.id.pHDisplay)).setText(formatter.format(dataOSensors.getpH()));
        } catch (Exception e) {
            Log.d("MainActivity", e.getMessage());
        };
    }

    public void seekbarTextHandler(){

        SeekBar theSeekBar= findViewById(R.id.seekBar2);
        theSeekBar.setProgress(desired.getSetTemper()-minTemper);
        String temper = getString(R.string.stringPlaceHolderWithDegreeSymbol,Integer.toString(desired.getSetTemper()));
        ((TextView)findViewById(R.id.desiredTemperature)).setText(temper);
    }

    public void autoButtHandler(){

        if(desired.getAutoMode()) {
            ((TextView)findViewById(R.id.modeState)).setText(R.string.autoMode);
        }
        else {
            ((TextView)findViewById(R.id.modeState)).setText(R.string.manuMode);
        }
    }

    public void heatButtHandler() {

        ConstraintLayout heatButt = findViewById(R.id.heatButtHolder);
        TextView txt = findViewById(R.id.heatOnIndicator);
        if(desired.getHeater()) {
            txt.setText(R.string.on);
            heatButt.setBackgroundTintList(AppCompatResources.getColorStateList(this,R.color.butt_oncolor));
            txt.setTypeface(ResourcesCompat.getFont(this,R.font.open_sans_bold));        }
        else {
            txt.setText(R.string.off);
            heatButt.setBackgroundTintList(AppCompatResources.getColorStateList(this,R.color.butt_offcolor));
            txt.setTypeface(ResourcesCompat.getFont(this,R.font.open_sans_light));
        }
    }

    public void chillButtHandler() {

        ConstraintLayout heatButt = findViewById(R.id.chillButtHolder);
        TextView txt = findViewById(R.id.chillOnIndicator);
        if(desired.getChiller()) {
            txt.setText(R.string.on);
            heatButt.setBackgroundTintList(AppCompatResources.getColorStateList(this,R.color.butt_oncolor));
            txt.setTypeface(ResourcesCompat.getFont(this,R.font.open_sans_bold));        }
        else {
            txt.setText(R.string.off);
            heatButt.setBackgroundTintList(AppCompatResources.getColorStateList(this,R.color.butt_offcolor));
            txt.setTypeface(ResourcesCompat.getFont(this,R.font.open_sans_light));
        }
    }

    private class seekHandler implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            desired.setSetTemper(minTemper + progress);
            String temper = getString(R.string.stringPlaceHolderWithDegreeSymbol,Integer.toString(desired.getSetTemper()));
            //set textView's text
            ((TextView)findViewById(R.id.desiredTemperature)).setText(temper);
        }

        public void onStartTrackingTouch(SeekBar seekBar) {}

        public void onStopTrackingTouch(SeekBar seekBar) {
            // Log the progress
            Log.d("DEBUG", "Progress is: "+desired.getSetTemper());
        }

    }

    class BackgroundThread implements Runnable {

        @Override
        public void run(){
            while (true) {
                mainHandler.post(MainActivity.this::loadSensorValues);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}