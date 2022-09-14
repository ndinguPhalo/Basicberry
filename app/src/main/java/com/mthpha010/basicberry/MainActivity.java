package com.mthpha010.basicberry;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    ArrayList<String> myList;
    TextView mytextview;
    String ErrorMessage;
    ImageView myImg;
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
            double tempVal = Double.parseDouble(myList.get(10).substring(1,myList.get(10).length()-2));
            mytextview.setText("Temperature: "+ myList.get(10));//myList.get(10));
            myImg = (ImageView) findViewById(R.id.tempImg);
            if (tempVal < 20){
                myImg.setImageResource(R.drawable.cold);
            }else{
                myImg.setImageResource(R.drawable.sunny);
            }

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