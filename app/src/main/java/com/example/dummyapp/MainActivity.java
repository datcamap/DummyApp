package com.example.dummyapp;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.dummyapp.data.model.AquaAnswersResponse;
import com.example.dummyapp.data.remote.ApiUtils;
import com.example.dummyapp.data.remote.AquaService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    int minTemper = 15;
    boolean onHeat = false;
    boolean onChill = false;
    boolean onAuto = false;
    int desiredTemper = 22;
    public AquaService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Home");

        mService = ApiUtils.getAquaService();
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

        yourSeekBar.setProgress(desiredTemper-minTemper);
        String temper = getString(R.string.symbolDegree,desiredTemper);
        ((TextView)findViewById(R.id.desiredTemperature)).setText(temper);

        heatButtHandler();
        chillButtHandler();
        autoButtHandler();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSetting:
                loadAnswers();
                break;
            case R.id.buttonDetail:
                Toast.makeText(this,"Clicked",Toast.LENGTH_LONG).show();
                Intent i = new Intent(this,ChartView.class);
                startActivity(i);
                break;
            case R.id.buttonHeater:
                if(onAuto) {
                    Toast.makeText(this,"Auto is ON",Toast.LENGTH_SHORT).show();
                }
                else {
                    onHeat = !onHeat;
                }
                heatButtHandler();
                break;
            case R.id.buttonChiller:
                if(onAuto) {
                    Toast.makeText(this,"Auto is ON",Toast.LENGTH_SHORT).show();
                }
                else {
                    onChill = !onChill;
                }
                chillButtHandler();
                break;
            case R.id.buttonMode:
                Toast.makeText(this,"Mode Changed",Toast.LENGTH_SHORT).show();
                onAuto = !onAuto;
                autoButtHandler();
                break;
            default:
                break;
        }
    }

    public void loadAnswers() {
        mService.getAnswers().enqueue(new Callback<AquaAnswersResponse>() {
            @Override
            public void onResponse(@NonNull Call<AquaAnswersResponse> call, @NonNull Response<AquaAnswersResponse> response) {
                if(response.isSuccessful()) {
                    Log.d("MainActivity", "posts loaded from API");
                }else {
                    int statusCode  = response.code();
                    // handle request errors depending on status code
                    Log.d("MainActivity", "Boo!");
                }
            }
            @Override
            public void onFailure(@NonNull Call<AquaAnswersResponse> call, @NonNull Throwable t) {
                Log.d("MainActivity", t.getMessage().toString());
            }
        });
    }

    public void autoButtHandler(){
        if(onAuto) {
            ((TextView)findViewById(R.id.modeState)).setText(R.string.autoMode);
        }
        else {
            ((TextView)findViewById(R.id.modeState)).setText(R.string.manuMode);
        }
    }

    public void heatButtHandler() {
        ConstraintLayout heatButt = findViewById(R.id.heatButtHolder);
        TextView txt = findViewById(R.id.heatOnIndicator);
        if(onHeat) {
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
        if(onChill) {
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
            desiredTemper = minTemper + progress;
            String temper = getString(R.string.symbolDegree,desiredTemper);
            //set textView's text
            ((TextView)findViewById(R.id.desiredTemperature)).setText(temper);
        }

        public void onStartTrackingTouch(SeekBar seekBar) {}

        public void onStopTrackingTouch(SeekBar seekBar) {
            // Log the progress
            Log.d("DEBUG", "Progress is: "+desiredTemper);
        }

    }
}