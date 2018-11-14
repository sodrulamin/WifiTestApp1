package com.example.sodrulaminshaon.wifitestapp1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by Sodrul Amin Shaon on 9/6/2017.
 */

public class UdpMultiSocket implements Runnable{
    MainActivity activity;
    int senderPort,receiverPort;

    UdpMultiSocket(MainActivity a,int senderPort,int receiverPort){
        activity=a;
        this.senderPort=senderPort;
        this.receiverPort=receiverPort;
    }

    @Override
    public void run() {
        while (!activity.running);
        new Thread(new Sender()).start();
        new Thread(new Receiver()).start();
    }

    private class Sender implements Runnable{

        @Override
        public void run() {
            while (!activity.running);
            try {
                DatagramSocket socket=new DatagramSocket(12345);
                //DatagramPacket packet=createStun(senderPort);
                /*for(int i=0;i<3;i++)socket.send(packet);
                activity.sentCount+=3;*/
                Functions.count=0;
                while (true){
                    Thread.sleep(activity.packetSize);
                    if(!activity.running)continue;
                    socket.send(Functions.createPacket(activity.address,senderPort,activity.packetSize));
                    activity.sentCount++;
                }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
    private class Receiver implements Runnable{

        @Override
        public void run() {
            try {
                DatagramSocket socket=new DatagramSocket(12346);
                DatagramPacket packet=createStun(receiverPort);
                /*for(int i=0;i<3;i++)socket.send(packet);
                activity.sentCount+=3;*/
                socket.send(Functions.createPacket(activity.address1,receiverPort,activity.packetSize));
                activity.sentCount++;
                socket.send(Functions.createPacket(activity.address1,receiverPort,activity.packetSize));
                activity.sentCount++;
                while (true){
                    socket.receive(packet);
                    activity.receivedCount++;
                }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private DatagramPacket createStun(int port){
        byte [] data;
        data=Functions.concatenateByteArrays(Functions.hexStringToByteArray("0001004c2112a442"),Functions.getRandomData(12));
        //attribute 1
        data=Functions.concatenateByteArrays(data,addAttribute(6,9,Functions.hexStringToByteArray("517457683a47676648"))); // USERNAME
        data=Functions.concatenateByteArrays(data,Functions.hexStringToByteArray("000000")); ///padding
        //attribute 2
        data=Functions.concatenateByteArrays(data,addAttribute(36,4,Functions.hexStringToByteArray("6efffeff"))); //// PRIORITY
        //attribute 3
        data=Functions.concatenateByteArrays(data,addAttribute(32810,8,Functions.hexStringToByteArray("00000000142b8582"))); ////ICE - CONTROLLING
        //attribute 4
        data=Functions.concatenateByteArrays(data,addAttribute(32880,4,Functions.hexStringToByteArray("00000003"))); ////MS - IMPLEMENTATION - VERSION
        //attribute 5
        data=Functions.concatenateByteArrays(data,addAttribute(8,20)); ////MESSAGE INTEGRITY
        //attribute 6
        data=Functions.concatenateByteArrays(data,addAttribute(32808,4)); /// FINGERPRINT

        return new DatagramPacket(data,data.length,activity.address,port);
    }
    private byte [] addAttribute(int type, int len,byte [] value){
        byte [] data=new byte[4];
        data[0]=(byte)(type>>8 & 0xff);
        data[1]=(byte)(type & 0xff);
        data[2]=(byte)(len>>8 & 0xff);
        data[3]=(byte)(len & 0xff);
        data=Functions.concatenateByteArrays(data,value);
        return data;
    }
    private byte [] addAttribute(int type, int len){
        byte [] data=new byte[4];
        data[0]=(byte)(type>>8 & 0xff);
        data[1]=(byte)(type & 0xff);
        data[2]=(byte)(len>>8 & 0xff);
        data[3]=(byte)(len & 0xff);
        data=Functions.concatenateByteArrays(data,Functions.getRandomData(len));
        return data;
    }
}
