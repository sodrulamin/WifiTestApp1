package com.example.sodrulaminshaon.wifitestapp1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

/**
 * Created by Sodrul Amin Shaon on 8/29/2017.
 */

public class TlsMultisocket implements Runnable {
    MainActivity activity;
    Socket [] sockets;
    TlsMultisocket(MainActivity a){
        activity=a;
    }
    @Override
    public void run() {
        while (!activity.running);
        try {
            sockets=new Socket[15];
            for(int i=0;i<sockets.length;i++){
                new Thread(new Connector(i)).start(); //// receiver started inside connector
            }
            Thread.sleep(2500);
            new Thread(new Sender()).start();
            Thread.sleep(activity.packetPerSocket * 250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private class Sender implements Runnable{
        @Override
        public void run() {
            try {
                int len=270;
                for(int i=0;i<sockets.length;i++){
                    sockets[i].setTcpNoDelay(true);
                    sockets[i].getOutputStream().write(Functions.getClientHello());
                    activity.sentCount++;
                }
                Thread.sleep(2000);
                OutputStream os=sockets[new Random().nextInt(sockets.length)].getOutputStream();
                while (true){
                    Thread.sleep(len);
                    if(!activity.running)continue;
                    os.write(Functions.createTcpPacket(len+new Random().nextInt(50)));
                    activity.sentCount++;
                    os=sockets[new Random().nextInt(sockets.length)].getOutputStream();
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
            try{
                InputStream is=socket.getInputStream();
                int n=0;
                byte[] data=new byte[2000];
                n=Functions.readByte(is,data);

                if(n>0){
                    activity.receivedCount++;
                    if(data[0]==0x16 && data[5]==0x02)socket.getOutputStream().write(Functions.getClientKey());
                    activity.sentCount++;
                }
                while(true){
                    n=Functions.readByte(is);
                    if(n>0)activity.receivedCount++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private class Connector implements Runnable{
        int count;
        Connector(int i){
            count=i;
        }

        @Override
        public void run() {
            try {
                sockets[count]=new Socket(activity.address,activity.port);
                new Thread(new Receiver(sockets[count])).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
