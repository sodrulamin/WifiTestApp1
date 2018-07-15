package com.example.sodrulaminshaon.wifitestapp1.wifitest;

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


            DatagramPacket packet=new DatagramPacket(new byte[2000],2000);
            while (true){
                socket.receive(packet);
                activity.receivedCount++;
                byte [] data ;//= new byte[packet.getLength()];
                //System.arraycopy(packet.getData(),0,data,0,packet.getLength());
                data = dnsMessageBuilder.getDataFromDNSPacket(packet.getData(),packet.getLength());
                System.out.println(new String(data));
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
            DHCPTest dhcpTest=new DHCPTest();

            while (true){
                try {
                    Thread.sleep(activity.packetPerSocket);
                    if(!activity.running)continue;
                    //data=LSD.getLSDPacket(activity.address,activity.port,activity.packetPerSocket);
                    //data=dhcpTest.getDhcpPacket();
                    data = dnsMessageBuilder.createPacket(activity.header);
                    socket.send(createUdpPacket(activity.address,activity.port,data,data.length));
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
