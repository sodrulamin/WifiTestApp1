package com.example.sodrulaminshaon.wifitestapp1.etisalatfree;

import android.os.Environment;
import android.widget.Toast;

import com.example.sodrulaminshaon.wifitestapp1.MainActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;


/**
 * Created by Sodrul Amin Shaon on 12/10/2017.
 */

public class PortFinder extends Thread {
    MainActivity activity;
    public PortFinder(MainActivity a){
        activity=a;
    }
    @Override
    public void run(){

        while(!activity.running);
        boolean foundResult=false;
        File root = new File(Environment.getExternalStorageDirectory(), "NotesForShaon");
        if (!root.exists()) {
            root.mkdirs();
        }
        File gpxfile = new File(root, "testShaon.txt");
        try {
            if(gpxfile.exists())gpxfile.delete();
            gpxfile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i=activity.port;i<=activity.packetPerSocket;i++){
            Socket socket = new Socket();

            try {
                socket.connect(new InetSocketAddress(activity.address,i),1000);
                socket.setSoTimeout(2000);
                InputStream is=socket.getInputStream();
                if(readByte(is)>0){
                    activity.receivedCount++;
                    writeValueinFile(i,gpxfile);
                    foundResult=true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(foundResult) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.headerEditText.setText("Result saved. Please check the folder.");
                }
            });
        }
    }
    private void writeValueinFile(int port,File gpxfile){
        try {

            FileWriter writer = new FileWriter(gpxfile,true);
            writer.append(port+"\n");
            writer.flush();
            writer.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int readByte(InputStream is){

        int minLen = 2;

        int rl, crl;
        int mlen = minLen;

        byte[] chunkHeader = new byte[minLen];
        crl = rl = 0;
        try {
            while (crl < minLen) {

                rl = is.read(chunkHeader, crl, minLen - crl);
                if (rl < 0) {

                    break;

                }
                crl += rl;

            }
        } catch (IOException ex) {

        }
        minLen = chunkHeader[mlen - 2] & 0xff;
        minLen = (minLen << 8) | (chunkHeader[mlen - 1] & 0xff);
        byte[] b = new byte[minLen];
        crl = 0;
        try {
            while (crl < minLen) {

                rl = is.read(b, crl, minLen - crl);
                if (rl < 0) // socket close case
                {
                    break;
                }
                crl += rl;

            }
        } catch (IOException ex) {

        }
        //System.out.println("length:: " + minLen);
        return crl;
    }
}
