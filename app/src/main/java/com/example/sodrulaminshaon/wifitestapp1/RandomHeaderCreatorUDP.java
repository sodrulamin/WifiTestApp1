package com.example.sodrulaminshaon.wifitestapp1;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import static android.R.attr.data;

/**
 * Created by Sodrul Amin Shaon on 10/14/2017.
 */

public class RandomHeaderCreatorUDP implements Runnable{
    MainActivity activity;
    int checkedYet,startPort;
    RandomHeaderCreatorUDP(MainActivity a){
        activity=a;
        checkedYet=0;
        startPort=2000;
    }
    @Override
    public void run() {
        while (!activity.running);

        while (true){
            try {
                Thread.sleep(100);
                if(!activity.running)continue;
                new Thread(new UdpHeaderFinder()).start();
                checkedYet++;
                Thread.sleep(100*200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        /*writeFile("this is first line");
        writeFile("this is second line");*/
    }
    private class UdpHeaderFinder implements Runnable{
        int receivedCount;
        UdpHeaderFinder(){
            receivedCount=0;
        }
        @Override
        public void run() {
            //writeFile("Hi\n");
            DatagramSocket socket=null;
            byte [] data=null;
            try {
                socket=new DatagramSocket(startPort+checkedYet);
                new Sender(socket).start();
                //socket.send(createPacket(activity.address,activity.port,350));
                socket.setSoTimeout(20000);
                DatagramPacket packet=new DatagramPacket(new byte[2000],2000);
                while(true){
                    socket.receive(packet);
                    receivedCount++;
                    data=new byte[packet.getLength()];
                    System.arraycopy(packet.getData(),0,data,0,packet.getLength());

                }
            } catch (SocketException e) {
                //e.printStackTrace();
            } catch (IOException e) {
                //e.printStackTrace();
            }finally {
                if(socket!=null){
                    try {
                        socket.close();
                    }catch (Exception e){}
                }
            }
            if(receivedCount>0){
                if(data!=null && data.length>0){
                    String packet=Functions.bytesToHex(data);
                    packet="["+receivedCount+"]: "+packet;
                    writeFile(packet);
                }
            }

        }
    }
    private class Sender extends Thread{
        DatagramSocket socket;
        Sender(DatagramSocket s){
            socket=s;
        }
        @Override
        public void run(){
            for(int i=0;i<100;i++){
                try {
                    socket.send(createPacket(activity.address,activity.port,200));
                    Thread.sleep(200);
                } catch (IOException e) {
                    //
                } catch (InterruptedException e) {
                    //
                }
            }
        }
    }
    public static DatagramPacket createPacket(InetAddress address, int port, int len){
        byte [] data;
        data=Functions.getRandomData(len);
        return new DatagramPacket(data,data.length,address,port);
    }

    private void writeFile(String str){
        String folder_main = "ShaonTest";
        File folder = new File(Environment.getExternalStorageDirectory(),folder_main);
        if(!folder.exists())folder.mkdirs();
        File file = new File(Environment.getExternalStorageDirectory()+"/"+folder_main,"test.txt");
        if(!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            //
        }
        try {
            FileOutputStream fileinput = new FileOutputStream(file,true);
            PrintStream printstream = new PrintStream(fileinput);
            printstream.print(str+"\n");
            System.out.println("Writing: "+str);
            fileinput.close();
        }
        catch (Exception e)
        {
            Toast.makeText(activity,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
