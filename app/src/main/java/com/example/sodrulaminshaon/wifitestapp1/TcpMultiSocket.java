package com.example.sodrulaminshaon.wifitestapp1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

/**
 * Created by Sodrul Amin Shaon on 9/17/2017.
 */

public class TcpMultiSocket implements Runnable {
    MainActivity activity;
    Socket [] sockets;
    TcpMultiSocket(MainActivity a){
        activity=a;
        sockets=new Socket[2];
    }
    @Override
    public void run() {
        while (!activity.running);
        try {
            sockets[0]=new Socket(activity.address,activity.port);
            sockets[1]=new Socket(activity.address,activity.port+1);

            for(int i=0;i<sockets.length;i++)new Thread(new Receiver(sockets[i])).start();

            new Thread(new Sender()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Sender implements Runnable{

        @Override
        public void run() {

            try {
                for(int i=0;i<sockets.length;i++){
                    sockets[i].setTcpNoDelay(true);
                }
                while (true) {
                    Thread.sleep(activity.packetSize);
                    if(!activity.running)continue;
                    OutputStream os=sockets[activity.sentCount%sockets.length].getOutputStream();
                    os.write(Functions.createTcpPacket(new Random().nextInt(20)));
                    activity.sentCount++;
                    os.write(Functions.createTcpPacket(600+new Random().nextInt(60)));
                    activity.sentCount++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class Receiver implements Runnable{
        Socket socket;
        Receiver(Socket s){
            socket=s;
        }

        @Override
        public void run() {
            try {
                socket.setSoTimeout(1000);
                InputStream is=socket.getInputStream();
                while(true){
                    if(Functions.readByte(is)>0)activity.receivedCount++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
