package com.example.sodrulaminshaon.wifitestapp1.udp;

import com.example.sodrulaminshaon.wifitestapp1.Functions;
import com.example.sodrulaminshaon.wifitestapp1.MainActivity;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by Sodrul Amin Shaon on 18-Sep-18.
 */

public class UDPSender extends Thread {
    MainActivity activity;
    public DatagramSocket socket;
    public int totalPacket;
    public UDPSender(MainActivity a){
        activity = a;
        totalPacket = 9999999;
    }

    @Override
    public void run(){
        while (!activity.running);
        while (socket == null);
        try {
            byte [] data = new byte[2048];
            DatagramPacket packet = new DatagramPacket(data,data.length);
            //while (true)
            for(int i=0;i<totalPacket;i++)
            {
                Thread.sleep(activity.packetSize);
                if(!activity.running){
                    i--;
                    continue;
                }
                Functions.getRandomData(data,0,activity.packetSize);
                packet.setData(data,0,activity.packetSize);
                packet.setAddress(activity.address);
                packet.setPort(activity.port);
                socket.send(packet);
                activity.sentCount++;
            }
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }/*finally {
            socket.close();
        }*/


    }
}
