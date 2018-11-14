package com.example.sodrulaminshaon.wifitestapp1.etisalatfree;

import com.example.sodrulaminshaon.wifitestapp1.Base64;
import com.example.sodrulaminshaon.wifitestapp1.Functions;
import com.example.sodrulaminshaon.wifitestapp1.MainActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

/**
 * Created by Sodrul Amin Shaon on 07-May-18.
 */

public class PacketSender extends Thread {
    MainActivity activity;
    Random random;
    byte [] header = "DELETE/".getBytes();
    public PacketSender(MainActivity a){
        activity = a;
        random = new Random();
    }

    @Override
    public void run(){
        //singleSocketTCP();
        multiSocketTCP();

    }
    public void singleSocketTCP(){

    }
    public void multiSocketTCP(){
        try {
            while (true) {
                Thread.sleep(activity.packetSize);
                if (!activity.running) continue;
                new Sender().start();
            }
        }catch (Exception e){}
    }
    private class Sender extends Thread{
        Socket socket;
        @Override
        public void run(){
            try {
                socket=new Socket(activity.address,activity.port);
                socket.setTcpNoDelay(true);
                OutputStream os=socket.getOutputStream();
                byte [] data;
                data = createEtiFreePacket(activity.packetSize);

                os.write(data,0,activity.packetSize);
                activity.sentCount++;
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(socket!=null) try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private byte [] createEtiFreePacket(int len){
//        String str = Functions.getRandomWord();
        byte [] data = (Functions.getRandomWord()+"/").getBytes();
        data = Functions.concatenateByteArrays(header,data);
        byte [] data1 = Base64.encode(Functions.getRandomData(len));
        data = Functions.concatenateByteArrays(data,data1);
        return data;
    }


}
