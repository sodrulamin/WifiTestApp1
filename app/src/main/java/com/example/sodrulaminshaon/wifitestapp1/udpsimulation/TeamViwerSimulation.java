package com.example.sodrulaminshaon.wifitestapp1.udpsimulation;

import com.example.sodrulaminshaon.wifitestapp1.Functions;
import com.example.sodrulaminshaon.wifitestapp1.MainActivity;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by Sodrul Amin Shaon on 17-Sep-18.
 */

public class TeamViwerSimulation extends Thread {
    MainActivity activity;
    byte [] dummy;
    DatagramSocket socket;
    public TeamViwerSimulation(MainActivity a){
        activity = a;
        dummy = Functions.hexStringToByteArray("0408000017030000ab840b17246b5e020c00000063" +
                "07000003000000f4910197e8a6198b8ab6effb8e8f32dc51fbd7777d7f04043cc7176e616f10" +
                "de0bd074b93fa75854fe9ef42d64ed8bc477ecf7aa3481e2aaa477b18ee783f258c19cab0dfd" +
                "badf2b1020a3cdfca946b22869dcb103f851ce86734f72cb6438af69402c2e2e5f095341367f" +
                "a620bb9dd2a76fa37114c7ed968bb210b741814c0d0e85ce67e3ff3776843bdb07b2fcc46b5a" +
                "ce3f0fb723df7c9aad7b83bac873120b450be5290c7e4acf09d44806644e7fdd075c54cb8b97" +
                "9b501bb147f5e07240cd8e04b7f7d1a258b60997b1c85b33027333347814548711c0743eec12" +
                "468473f7b59258874ac77c3c981b2dfa699811c351a2e0153ede41433a8133da1edb0aac2c6e" +
                "0c2fb7fec5045739343ef3ed821dd92ff6971025d0603cfb312bbaad89943f73ea1c149fd74d" +
                "9178b80d84add44115764ee189686757579d660da85680db3d48638928671c3d6c427878db76" +
                "629d5dea7332ae48abb3fdf312748a4694c8813aa285f9aa28240ebb1197c448c7a862712b28" +
                "98868293f5a19346f20fbc63e850c5c8e2feb0941fe034a9b7ab33dde30372532bbc011bc6e8" +
                "57dff1723114645012131d4049f14ff29dc864c57077fbedfdf24e4404d2ec90ea32e4bd64ac" +
                "8f23fa3851f3df047eaf56d69ffc763dcdc2d463ddbf40a3afb27c4a31286df07a5202d691a6" +
                "a099372dc9183027cfc8278a5425891825c3952d34d4eab05a918a813df93f85575f75c416e1" +
                "6afd4ad415b653d0177bb49ee8faedb7c001738cfa42d7d5361575312ac5faea58de91cdd4db" +
                "1bad2d719c99d57ef9a93eb0ce240f1c09ccfd6d57a52a0e378e8c2b900f74");
    }
    @Override
    public void run(){
        while (!activity.running);
        try{
            socket = new DatagramSocket();

            new Receiver().start();
            new DummySender().start();
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
    public class DummySender extends Thread{

        @Override
        public void run(){
            try{
                DatagramPacket packet = new DatagramPacket(dummy,dummy.length);
                while(true){
                    Thread.sleep(activity.packetSize);
                    if(!activity.running)continue;
                    //for(int i = 0;i<3;i++)
                    {
                        packet.setAddress(activity.address);
                        packet.setPort(activity.port);
                        socket.send(packet);
                        activity.sentCount++;
                        //Thread.sleep(1000);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
