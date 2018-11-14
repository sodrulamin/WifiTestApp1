package com.example.sodrulaminshaon.wifitestapp1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Sodrul Amin Shaon on 9/9/2017.
 */

class TcpFlowControl implements Runnable {
    MainActivity activity;
    int packetSize;
    TcpFlowControl(MainActivity ac){
        activity=ac;
    }

    @Override
    public void run() {
        while (!activity.running);
        int totalActiveSocket=activity.packetSize;
        packetSize=1000/totalActiveSocket;
        if(packetSize%20 > 0){
            packetSize= ((int) Math.ceil(((double)packetSize)/20.0))*20;
        }
        System.out.println(packetSize);
        for(int i=0;i<totalActiveSocket;i++){
            new Thread(new TcpCommunication()).start();
            try {
                Thread.sleep(packetSize);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private class TcpCommunication implements Runnable{
        Socket socket;

        @Override
        public void run() {
            while (!activity.running);
            try {
                socket=new Socket(activity.address,activity.port);
                new Thread(new TcpReceiver(socket)).start();
                socket.setTcpNoDelay(true);
                OutputStream os=socket.getOutputStream();
                while (true){
                    Thread.sleep(1000);
                    if(!activity.running)continue;
                    os.write(Functions.createTcpPacket(packetSize));
                    activity.sentCount++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private class TcpReceiver implements Runnable{
        Socket socket;
        TcpReceiver(Socket s){
            socket=s;
        }
        @Override
        public void run() {
            try {
                InputStream is=socket.getInputStream();
                while (true){
                    int n=Functions.readByte(is);
                    if(n>0)activity.receivedCount++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
