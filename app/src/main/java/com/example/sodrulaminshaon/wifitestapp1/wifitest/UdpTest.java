package com.example.sodrulaminshaon.wifitestapp1.wifitest;

import com.example.sodrulaminshaon.wifitestapp1.Functions;
import com.example.sodrulaminshaon.wifitestapp1.MainActivity;
import com.example.sodrulaminshaon.wifitestapp1.dufreetest.DNSMessageBuilder;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by Sodrul Amin Shaon on 13-Feb-18.
 */

public class UdpTest extends Thread {
    MainActivity activity;
    DNSMessageBuilder dnsMessageBuilder;// = new DNSMessageBuilder();
    public UdpTest(MainActivity a){
        activity=a;
        dnsMessageBuilder = new DNSMessageBuilder();
    }

    @Override
    public void run(){
        while (!activity.running);
        try {
            DatagramSocket socket = new DatagramSocket(54044);
            new Thread(new UdpSender(socket)).start();

            byte [] data = new byte[2048];
            int dataLen = 0;
            DatagramPacket packet=new DatagramPacket(data,data.length);
            while (true){
                socket.receive(packet);
                activity.receivedCount++;
                //= new byte[packet.getLength()];
                //System.arraycopy(packet.getData(),0,data,0,packet.getLength());
                //data = dnsMessageBuilder.getDataFromDNSPacket(packet.getData(),packet.getLength());
                //dataLen = DHCPTest.getDataFromPacket(data,packet.getLength());
                //System.out.println(new String(data,0,dataLen));
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class UdpSender implements Runnable{
        DatagramSocket socket;
        UdpSender(DatagramSocket s){
            socket=s;
        }
        @Override
        public void run() {
            while (!activity.running);
            byte [] data;
            int len = activity.packetSize;
            while (true)
            {
                try {
                    Thread.sleep(activity.packetSize);
                    if(!activity.running)continue;
                    data = Functions.getRandomData(len);
                    //data=LSD.getLSDPacket(activity.address,activity.port,activity.packetSize);
                    //data=dhcpTest.getDhcpPacket();
                    //data = dnsMessageBuilder.createPacket(activity.header);
                    //data = Functions.concatenateByteArrays("Shaonistestingthislineforbetterness".getBytes(),Functions.getRandomData(2000));
                    //len = DHCPTest.createPacket(data,activity.packetSize);
                    //data = AFSSimulation.getNextPacket(Functions.getRandomData(len),len);
                    socket.send(createUdpPacket(activity.address,activity.port,data,len));
                    activity.sentCount++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    private DatagramPacket createUdpPacket(InetAddress address, int port, byte[] data, int len){
        return new DatagramPacket(data,len,address,port);
    }
}
