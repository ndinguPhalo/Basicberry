package com.mthpha010.basicberry;

import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;


public class csv extends AppCompatActivity{
    ArrayList<String> data = new ArrayList<String>();
    public String errorMessage = "";

    public String getErr(){
        return errorMessage;
    }

    public ArrayList<String> readInitial(){
        File csvf = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +"data.csv" );
        try {
            //getExternalStorageDirectory() + "/data.csv");
            CSVReader reader = new CSVReader(new FileReader(csvf));
            String[] nextLine;
            while ((nextLine = reader.readNext())!= null) {
                data.add(nextLine[12]);
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        return data;
    }
}
