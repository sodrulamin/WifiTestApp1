package com.example.sodrulaminshaon.wifitestapp1.udp;

import com.example.sodrulaminshaon.wifitestapp1.MainActivity;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by Sodrul Amin Shaon on 18-Sep-18.
 */

public class UDPReceiver extends Thread {
    MainActivity activity;
    public DatagramSocket socket;
    public UDPReceiver(MainActivity a){
        activity = a;
    }

    @Override
    public void run(){
        //while (!activity.running);
        while (socket == null);
        try{
            byte [] data = new byte[2048];
            DatagramPacket packet = new DatagramPacket(data,data.length);
            while (true){
                socket.receive(packet);
                activity.receivedCount++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
