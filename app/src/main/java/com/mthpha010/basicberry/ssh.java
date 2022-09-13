package com.mthpha010.basicberry;

import android.os.Environment;
import android.os.StrictMode;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.File;
import java.util.Properties;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
//import com.jcraft.jsch.SftpException;


public class ssh {
    public String errorMessage = "";
    Session session;
    JSch jsch = new JSch();

    public void StartConnection(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String user = "pi";
        String password = "pi";
        String host = "192.168.1.59"; //43.132";
        int port=22;
//        TextView txt = findViewById(R.id.textView);
        try {
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            config.put("PreferredAuthentications", "password");
            session.setConfig(config);
            session.connect();

        }catch(Exception e){
            errorMessage = e.getMessage();
        }
    }

    public String getError(){
        return errorMessage;
    }

    public void Shutdown(){
        try {
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.connect();
            channel.setCommand("sudo shutdown now");
            try{Thread.sleep(1000);}catch(Exception ee){errorMessage = ee.getMessage();}
            channel.disconnect();
        }catch(Exception e){
            errorMessage = e.getMessage();
        }
    }

    public void senseAgain(){
        try {
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.connect();
            channel.setCommand("python3 mthpha010/EEE3097S/Project/ICM20948.py >> mthpha010/EEE3097S/Project/IMUdata.csv");
            try{Thread.sleep(1000);}catch(Exception ee){errorMessage = ee.getMessage();}
            channel.disconnect();
        }catch(Exception e){
            errorMessage = e.getMessage();
        }
    }

    public void executeFTP(){
        try{
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp ftransfer = (ChannelSftp) channel;

            try{
                ftransfer.cd("mthpha010/EEE3097S/Project/");
                try (FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory()+File.separator+"Vula/data.csv")) {
                    try (InputStream in = ftransfer.get("IMUdata.csv")) {
                        // read from in, write to out
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = in.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                        }
                    }
                    catch (Exception ee){errorMessage = ee.getMessage();}
                } catch(Exception eee){errorMessage = eee.getMessage();}
                //txt.setText("data.csv");
            }
            catch (Exception e) {
                errorMessage = e.getMessage();
            }
            ftransfer.exit();
            try{Thread.sleep(1000);}catch(Exception ee){errorMessage = ee.getMessage();}
            channel.disconnect();
        }
        catch(JSchException e){
            errorMessage = e.getMessage();
        }
    }
}
