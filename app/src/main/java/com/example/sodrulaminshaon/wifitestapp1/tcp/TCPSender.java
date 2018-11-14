package com.example.sodrulaminshaon.wifitestapp1.tcp;

import com.example.sodrulaminshaon.wifitestapp1.Functions;
import com.example.sodrulaminshaon.wifitestapp1.MainActivity;

import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Sodrul Amin Shaon on 25-Sep-18.
 */

public class TCPSender extends Thread {
    Socket socket;
    MainActivity activity;
    public byte [] header;

    public TCPSender(MainActivity a,Socket s){
        socket = s;
        activity = a;
    }

    @Override
    public void run(){
        while(!activity.running);
        try{
            socket.setTcpNoDelay(true);
            OutputStream os = socket.getOutputStream();
            byte [] data = new byte[2048];
            int len,index = 0;
            while(true){
                Thread.sleep(activity.packetSize);
                if(header != null){
                    System.arraycopy(header,0,data,index,header.length);
                    index+= header.length;
                }
                len = activity.packetSize;
                Functions.getRandomData(data,index,len);
                data[index+0] = (byte)((len -2) >> 8 & 0xff);
                data[index+1] = (byte)( (len - 2) & 0xff);
                index+=len;
                os.write(data,0,index);
                activity.sentCount++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
