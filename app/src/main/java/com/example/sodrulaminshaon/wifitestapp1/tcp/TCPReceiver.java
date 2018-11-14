package com.example.sodrulaminshaon.wifitestapp1.tcp;

import com.example.sodrulaminshaon.wifitestapp1.Functions;
import com.example.sodrulaminshaon.wifitestapp1.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Sodrul Amin Shaon on 27-Sep-18.
 */

public class TCPReceiver extends Thread {
    Socket socket;
    MainActivity activity;
    public byte [] header;

    public TCPReceiver(MainActivity a,Socket s){
        socket = s;
        activity = a;
    }

    @Override
    public void run(){
        while(!activity.running);
        try{
            System.out.println("Going to receive data here.");
            //socket.setTcpNoDelay(true);
            socket.setSoTimeout(3000);
            InputStream is = socket.getInputStream();
            byte [] data = new byte[2048];
            int len;
            System.out.println("Header Len: "+activity.headerNumber);
            while(true){
                len = readByte(is,data);
                System.out.println("Len: "+len);
                if(len > 0 )activity.receivedCount++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    // ======================================================================== //
    public int readByte(InputStream is,byte [] data) throws Exception{
        int index = 0,dataLen,headerLen,read;

        while(index < activity.headerNumber+2){
            read = is.read(data,index,(activity.headerNumber +2 - index));
            if(read < 1)return index;
            index += read;
        }
        headerLen = index;
        dataLen = data[index - 2];
        dataLen = (dataLen << 8 | (int)(data[index - 1] & 0xff));
        while(index < headerLen + dataLen){
            read = is.read(data,index,(headerLen + dataLen - index));
            if(read < 1)return index;
            index += read;
        }

        return index;
    }
}
