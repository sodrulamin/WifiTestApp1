package com.example.sodrulaminshaon.wifitestapp1.etisalatfree;

import android.util.Base64;

import static com.example.sodrulaminshaon.wifitestapp1.Functions.*;
import com.example.sodrulaminshaon.wifitestapp1.MainActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by Sodrul Amin Shaon on 10-Jan-18.
 */

public class Test extends Thread{
    MainActivity activity;
    int port;
    public Test(MainActivity a){
        activity=a;
        port=5656;
    }
    @Override
    public void run(){
        while (!activity.running);
        while(true) {
            try {
                Thread.sleep(100);
                if(!activity.running)continue;
                activity.running=false;
                //send();
                receive();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //testSockedAllive();


    }
    public void testSockedAllive(){
        try {
            Socket socket = new Socket(InetAddress.getByName("65.99.254.11"),7654);
            Thread.sleep(2000);
            socket.close();
            System.out.println("Socket closed................");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Exited test.");
    }

    public void send() throws IOException {
        Socket socket=new Socket(activity.address,activity.port);
        OutputStream os=socket.getOutputStream();
        os.write(createTcpPacket(activity.packetSize));
        activity.sentCount++;
    }
    public void receive(){
        DatagramSocket socket=null;
        try {
            InetAddress address=InetAddress.getByName(activity.header);
            socket=new DatagramSocket(port);
            socket.setSoTimeout(3000);
            port++;
            byte [] data=getRandomData(activity.headerNumber);
            DatagramPacket packet=new DatagramPacket(data,data.length,address,activity.port);
            socket.send(packet);
            activity.sentCount++;
            packet.setAddress(activity.address);
            socket.send(packet);
            socket.receive(packet);
            activity.receivedCount++;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(socket!=null)socket.close();
        }

    }
    // ======================================================================== //
    public static byte[] createTcpPacket(int len){
        byte[] data;
        data=getRandomData(len+2);
        data= Base64.encode(data,0);
        data[0]=(byte)((data.length-2)>>8 & 0xf);
        data[1]=(byte)((data.length-2) & 0xff);
        return data;
    }
}
