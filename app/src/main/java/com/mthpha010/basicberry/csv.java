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

    public csv() throws FileNotFoundException, IOException {
    }

    public ArrayList<String> readInitial(){
        File csvfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +"Vula/data.csv" );
        try {
            //getExternalStorageDirectory() + "/data.csv");
            CSVReader reader = new CSVReader(new FileReader(csvfile.getAbsolutePath()));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                // System.out.println(nextLine[0] + nextLine[1] + "etc...");
                data.add(nextLine[12]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
