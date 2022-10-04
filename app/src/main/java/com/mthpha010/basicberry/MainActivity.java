package com.mthpha010.basicberry;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    ArrayList<String> myList;
    TextView mytextview;
    String ErrorMessage, host;
    ImageView myImg;
    ssh sshc = new ssh();
    csv csvFile = new csv();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        host = "192.168.1.59";
        setContentView(R.layout.activity_main);
        try {
            sshc.StartConnection(host);
        } catch (Exception e) {
            ErrorMessage = sshc.getError();
            mytextview = (TextView) findViewById(R.id.ErrorTxt);
            mytextview.setText(ErrorMessage);
        }
    }

    private void prepArray() throws IOException {
        myList = csvFile.readInitial();
    }

    public void ShutdownPi(View v){
        sshc.StartConnection(host);
        sshc.Shutdown();
    }

    public void setHost(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Host");

        final View setHost = getLayoutInflater().inflate(R.layout.host_dialog,null);
        builder.setView(setHost);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send data from the
                // AlertDialog to the Activity
                EditText editText = setHost.findViewById(R.id.host);
                host = editText.getText().toString();
            }
        });

        // create and show
        // the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @SuppressLint("SetTextI18n")
    public void UpdateTemp(View v){
//        Context context = getApplicationContext();
//        String pat = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Vula/data.csv";
//        Toast.makeText(context,pat, Toast.LENGTH_SHORT).show();
        try {
            long tstartTime = System.currentTimeMillis();
            sshc.StartConnection(host);
            sshc.senseAgain();
            mytextview = (TextView) findViewById(R.id.ErrorTxt);
            ErrorMessage = sshc.getError();
            mytextview.setText(ErrorMessage);
            sshc.StartConnection(host);

            long startTime = System.currentTimeMillis();
            sshc.executeFTP();
            long endTime = System.currentTimeMillis();

            prepArray();

            long duration = (endTime - startTime);
            long tendTime = System.currentTimeMillis();
            long tduration = (tendTime - tstartTime);
            mytextview = (TextView) findViewById(R.id.speedTxt2);
            //mytextview.setText(tduration+" "+duration);
            mytextview.setText(""+duration);
            mytextview = (TextView) findViewById(R.id.tempText);

            double tempVal = Double.parseDouble(myList.get(1).substring(1,myList.get(1).length()-2));
            mytextview.setText("Temperature: "+ myList.get(1));//myList.get(10));
            myImg = (ImageView) findViewById(R.id.tempImg);
            if (tempVal < 20){
                myImg.setImageResource(R.drawable.cold);
            }else{
                myImg.setImageResource(R.drawable.sunny);
            }
        } catch (Exception e) {
            mytextview = (TextView) findViewById(R.id.ErrorTxt);
            ErrorMessage = sshc.getError()+" "+csvFile.getErr() +" "+ e.getMessage();
            mytextview.setText(ErrorMessage);
        }
//      CharSequence text = myList;
//      int duration = Toast.LENGTH_SHORT;

//      setContentView(R.layout.activity_main);
    }
}