package com.mthpha010.basicberry;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    ArrayList<String> myList;
    TextView mytextview;
    String ErrorMessage;
    ssh sshc = new ssh();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            sshc.StartConnection();
        } catch (Exception e) {
            ErrorMessage = sshc.getError();
            mytextview = (TextView) findViewById(R.id.ErrorTxt);
            mytextview.setText(ErrorMessage);
        }
    }

    private void prepArray() throws IOException {
        csv csvFile = new csv();
        myList = csvFile.readInitial();
    }

    public void ShutdownPi(View v){
        sshc.StartConnection();
        sshc.Shutdown();
    }

    @SuppressLint("SetTextI18n")
    public void UpdateTemp(View v){
//        Context context = getApplicationContext();
//        String pat = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Vula/data.csv";
//        Toast.makeText(context,pat, Toast.LENGTH_SHORT).show();
        try {
            sshc.StartConnection();
            sshc.senseAgain();
            mytextview = (TextView) findViewById(R.id.ErrorTxt);
            ErrorMessage = sshc.getError();
            mytextview.setText(ErrorMessage);
            sshc.StartConnection();
            long startTime = System.currentTimeMillis();
            sshc.executeFTP();
            prepArray();
            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime);
            mytextview = (TextView) findViewById(R.id.speedTxt2);
            mytextview.setText(""+duration);
            mytextview = (TextView) findViewById(R.id.tempText);
            mytextview.setText("Temperature: "+myList.get(10));
        } catch (Exception e) {
            mytextview = (TextView) findViewById(R.id.ErrorTxt);
            ErrorMessage = sshc.getError();
            mytextview.setText(ErrorMessage);
        }
//      CharSequence text = myList;
//      int duration = Toast.LENGTH_SHORT;

//      setContentView(R.layout.activity_main);
    }
}