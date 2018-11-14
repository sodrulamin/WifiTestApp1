package com.example.sodrulaminshaon.wifitestapp1.udpsimulation;

import com.example.sodrulaminshaon.wifitestapp1.Functions;
import com.example.sodrulaminshaon.wifitestapp1.MainActivity;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;

/**
 * Created by Sodrul Amin Shaon on 11-Aug-18.
 */

public class NewTelSImulation extends Thread {
    DatagramSocket socket;
    MainActivity activity;
    Random random;
    public NewTelSImulation(MainActivity a){
        activity = a;
        random = new Random();
    }

    @Override
    public void run(){
        while (!activity.running);
        try{
            socket = new DatagramSocket();

            new DummySender().start();
            new Sender().start();
            new Receiver().start();

        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    public class Receiver extends Thread {
        @Override
        public void run() {
            try
            {
                DatagramPacket packet = new DatagramPacket(new byte[2048], 2048);
                while (true) {
                    socket.receive(packet);
                    activity.receivedCount++;
                }
            }catch( Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    public class Sender extends Thread {
        @Override
        public void run() {
            try
            {
                DatagramPacket packet = new DatagramPacket(new byte[2048], 2048);
                while (true) {
                    Thread.sleep(activity.packetSize);
                    if (!activity.running) continue;
                    byte[] data = Functions.getRandomData(activity.packetSize + random.nextInt(20));
                    packet.setData(data);
                    packet.setAddress(activity.address);
                    packet.setPort(activity.port);
                    socket.send(packet);
                    activity.sentCount++;
                }
            }catch(
            Exception e)

            {
                e.printStackTrace();
            }
        }
    }
    public class DummySender extends Thread{

        @Override
        public void run(){
            try{
                byte [] data = Functions.hexStringToByteArray("657772321f057f7703090836717c06027e780b0c7c7e02047e78040e7e7a08067e78320f70750401767b020c747d0507707b06057774070c195f2623525b2a2f5e572e2b5a531217666f1613");

                DatagramPacket packet = new DatagramPacket(data,data.length);
                while(true){
                    Thread.sleep(3000);
                    if(!activity.running)continue;
                    for(int i = 0;i<3;i++){
                        packet.setAddress(activity.address);
                        packet.setPort(activity.port);
                        socket.send(packet);
                        activity.sentCount++;
                        Thread.sleep(1000);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
