package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

    int portNumber = 3030;
    EditText portInput;
    Button serverButton;
    boolean serverStarted = false;
    TextView serverDisplay;

    TextView colorDisplay;

    TextView infoTitle;
    TextView description;
    Button backButton;


    Button startButton;
    Slider slider;
    TextView caption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






        infoTitle = (TextView) findViewById(R.id.info_title);
        description = (TextView) findViewById(R.id.description);
        backButton = (Button) findViewById(R.id.returnButton);


        infoTitle.setVisibility(View.GONE);
        description.setVisibility(View.GONE);
        backButton.setVisibility(View.GONE);

        caption = (TextView) findViewById(R.id.sliderCaption);

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                button.setVisibility(View.VISIBLE);
                mButton.setVisibility(View.VISIBLE);
                debugButton.setVisibility(View.VISIBLE);
                display.setVisibility(View.VISIBLE);
                portInput.setVisibility(View.VISIBLE);
                serverButton.setVisibility(View.VISIBLE);
                serverDisplay.setVisibility(View.VISIBLE);
                colorDisplay.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.VISIBLE);
                slider.setVisibility(View.VISIBLE);
                caption.setVisibility(View.VISIBLE);
                infoTitle.setVisibility(View.GONE);
                description.setVisibility(View.GONE);
                backButton.setVisibility(View.GONE);
            }
        });




        colorDisplay = (TextView) findViewById(R.id.colorDisplay);
        colorDisplay.setBackgroundColor(0xffff0000);

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
                debugMode = !debugMode;
                debugPressed();
            }
        });

        startButton = (Button) findViewById(R.id.start);
        startButton.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("SetTextI18n")
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

        slider = findViewById(R.id.slider);
        slider.addOnChangeListener((slider1, value, fromUser) -> {
            strokeSize = (int) value;
            currentDisplay[2] = "Stroke Size: " + strokeSize + "\n";
            updateDisplay(display);
        });


        portInput = (EditText) findViewById(R.id.portInput);
        serverButton = (Button) findViewById(R.id.serverButton);
        serverButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                serverButtonClicked(v);
            }
        });

        serverDisplay = (TextView) findViewById(R.id.serverDisplay);
    }

    public void openColorPicker(){
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {}

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;
                Alpha = Color.alpha(mDefaultColor);
                Red = Color.red(mDefaultColor);
                Green = Color.green(mDefaultColor);
                Blue = Color.blue(mDefaultColor);
                currentDisplay[1] = "Color: (" + Red + ", " + Green + ", " + Blue + ")\n";
                updateDisplay(display);

                colorDisplay.setBackgroundColor(mDefaultColor);
            }
        });
        colorPicker.show();
    }

    public void infoButtonPressed(){
            button.setVisibility(View.GONE);
            mButton.setVisibility(View.GONE);
            debugButton.setVisibility(View.GONE);
            display.setVisibility(View.GONE);
            portInput.setVisibility(View.GONE);
            serverButton.setVisibility(View.GONE);
            serverDisplay.setVisibility(View.GONE);
            colorDisplay.setVisibility(View.GONE);
            startButton.setVisibility(View.GONE);
            slider.setVisibility(View.GONE);
            caption.setVisibility(View.GONE);
            infoTitle.setVisibility(View.VISIBLE);
            description.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.VISIBLE);

    }

    public void debugPressed(){
        debugMode = !debugMode;
    }

    @SuppressLint("SetTextI18n")
    private void updateDisplay(TextView display){
        display.setText(currentDisplay[0] + currentDisplay[1] + currentDisplay[2] + currentDisplay[3]
        + currentDisplay[4] + currentDisplay[5]);
    }


    public void serverButtonClicked(View v){
        if(!serverStarted){

            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);

            String input = portInput.getText().toString();
            if(input.length() == 0){
                input = "3030";
                portNumber = 3030;
            }else{
                portNumber = Integer.parseInt(input);
            }

            input = "http://localhost:" + input + "/";

            serverDisplay.setText("Address: "+input);
            serverButton.setText("Disconnect");
            portInput.setFocusable(false);
        }else{
            serverDisplay.setText("Connect to a server!");
            serverButton.setText("Connect");
            portInput.setFocusableInTouchMode(true);
        }
        serverStarted = !serverStarted;
    }


}
