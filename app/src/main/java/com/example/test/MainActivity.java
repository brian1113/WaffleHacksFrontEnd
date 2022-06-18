package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.slider.Slider;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {

    int recordingMovement = 0;
    boolean debugMode = false;
    int strokeSize = 6;
    private Button button;

    int mDefaultColor;
    Button mButton;
    int Alpha = 255;
    int Red = 255;
    int Green = 0;
    int Blue = 0;

    Button debugButton;

    TextView display;
    String[] currentDisplay = new String[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = (TextView) findViewById(R.id.display);
        display.setBackgroundColor(Color.parseColor("lightgrey"));
        currentDisplay[0] = "Status: Stopped\n";
        currentDisplay[1] = "Color: (" + Red + ", " + Green + ", " + Blue + ")\n";
        currentDisplay[2] = "Stroke Size: " + strokeSize;
        currentDisplay[3] = ""; currentDisplay[4] = ""; currentDisplay[5] = "";
        updateDisplay(display);


        mDefaultColor = 0xffff0000;
        mButton = (Button) findViewById(R.id.colorPicker);
        mButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openColorPicker();
            }
        });

        button = (Button) findViewById(R.id.info);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                infoButtonPressed();
            }
        });

        debugButton = (Button) findViewById(R.id.debug);
        debugButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(debugMode == true) debugMode = false;
                else debugMode = true;
                debugPressed();
            }
        });

        Button startButton = (Button) findViewById(R.id.start);
        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(recordingMovement == 0){
                    startButton.setText("Stop");
                    currentDisplay[0] = "Status: Running\n";
                    recordingMovement = 1;
                }else{
                    startButton.setText("Start");
                    recordingMovement = 0;
                    currentDisplay[0] = "Status: Stopped\n";
                }
                updateDisplay(display);
            }
        });

        Slider slider = findViewById(R.id.slider);
        slider.addOnChangeListener((slider1, value, fromUser) -> {
            strokeSize = (int) value;
            currentDisplay[2] = "Stroke Size: " + strokeSize + "\n";
            updateDisplay(display);
        });
    }

    public void openColorPicker(){
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;
                Alpha = Color.alpha(mDefaultColor);
                Red = Color.red(mDefaultColor);
                Green = Color.green(mDefaultColor);
                Blue = Color.blue(mDefaultColor);
                currentDisplay[1] = "Color: (" + Red + ", " + Green + ", " + Blue + ")\n";
                updateDisplay(display);
            }
        });
        colorPicker.show();
    }

    public void infoButtonPressed(){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        startActivity(intent);
    }

    public void debugPressed(){
        if(debugMode == true){
            debugMode = false;
        }else debugMode = true;
    }

    private void updateDisplay(TextView display){
        display.setText(currentDisplay[0] + currentDisplay[1] + currentDisplay[2] + currentDisplay[3]
        + currentDisplay[4] + currentDisplay[5]);
    }
}