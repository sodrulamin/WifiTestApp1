package com.example.sodrulaminshaon.wifitestapp1;

import android.util.Log;

import com.example.sodrulaminshaon.wifitestapp1.dufreetest.DNSMessageBuilder;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sodrul Amin Shaon on 8/20/2017.
 */

public class UdpHeaderChecker implements Runnable {
    MainActivity activity;
    long sequenceNumber=456746;
    int sleep;
    DNSMessageBuilder dnsMessageBuilder;// = new DNSMessageBuilder();
    ArrayList<String> headers;
    UdpHeaderChecker(MainActivity ac){
        initializeHeaders();
        activity=ac;
        sleep=200;
        dnsMessageBuilder = new DNSMessageBuilder();
    }
    private void initializeHeaders(){
        headers = new ArrayList<>();

        headers.add("1102f901c0a8018c008a00bb000020454545464644454c464545504641434e464644424442455045444643444943410020464845504643454c45484643455046464641434143414341434143414341424f00");
        headers.add("110293bdc0a80174008a00bb000020454545464644454c464545504641434e464345494549454445474546464643410020464845504643454c45484643455046464641434143414341434143414341424e00");
        headers.add("11029046c0a80186008a00ca000020454545464644454c464545504641434e4544454d454f4448454e4545454f414100204142414346504650454e4644454346434550464846444546465046504143414200");
        headers.add("1102f902c0a8018c008a00ca000020454545464644454c464545504641434e4646444244424550454446434449414100204142414346504650454e4644454346434550464846444546465046504143414200");


        headers.add("110eb6f3c0a80165008a00bb000020454a4542464545424549454e454a4545434143414341434143414341434143410020464845504643454c45484643455046464641434143414341434143414341424f00");
        headers.add("110eb6f3c0a80165008a00bb000020454a4542464545424549454e454a4545434143414341434143414341434143410020464845504643454c45484643455046464641434143414341434143414341424f00");
    }
    @Override
    public void run() {
        while (!activity.running);
        try {
            int i=0;
            while(true) {
                DatagramSocket socket = new DatagramSocket(54044+i);
                new Thread(new UdpSender(socket)).start();
                new Thread(new UdpReceiver(socket)).start();
                Thread.sleep(activity.packetPerSocket*sleep);
                i++;
                break;
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private class UdpSender implements Runnable{
        DatagramSocket socket;
        int packetPerSocket=0;
        UdpSender(DatagramSocket s){
            socket=s;
        }
        @Override
        public void run() {
            while (!activity.running);
            DatagramPacket packet = new DatagramPacket(new byte[2048],2048);
            packet.setAddress(activity.address);
            packet.setPort(activity.port);
            while (true){
                try {
                    Thread.sleep(activity.packetPerSocket);
                    if(!activity.running)continue;
                    /*byte [] pingData = dnsMessageBuilder.createPacket(activity.header);
                    packet.setData(pingData);*/

                    socket.send(createPacket(activity.address,activity.port,activity.packetPerSocket));
                    //socket.send(packet);
                    activity.sentCount++;
                    ///packetPerSocket++;
                    //if(System.currentTimeMillis()>(startTime+packetPerSocket*sleep))break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    private void bootInit(DatagramSocket socket){
        Random random=new Random();
        for(int i=0;i<10;i++){
            try {
                socket.send(createPacket(activity.address,activity.port,140+random.nextInt(10)));
                activity.sentCount++;
                Thread.sleep(10);
                socket.send(pureUdp(activity.address1,10344,140+random.nextInt(10)));
                //activity.sentCount++;
                Thread.sleep(50);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private class UdpReceiver implements Runnable{
        DatagramSocket socket;
        UdpReceiver(DatagramSocket s){
            socket=s;
        }
        @Override
        public void run() {
            DatagramPacket packet=new DatagramPacket(new byte[8192],8192);
            int count = 1;
            while (true){
                try {
                    socket.receive(packet);
                    activity.receivedCount++;
                    /*byte [] data = dnsMessageBuilder.getDataFromDNSPacket(packet.getData(),packet.getLength());

                    if(data == null)continue;
                    String receiveString  = new String(data);
                    System.out.println("Received data: \n"+receiveString+"  ---------------- "+count++);
                    //if(receiveString.contains("SHAON_GOT_IT"))activity.receivedCount++;
                    if(dnsMessageBuilder.isValid(data))activity.receivedCount++;
                    else{
                        int len = (int) data[0];
                        Log.i("failed","datalen: "+len);
                    }*/
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private DatagramPacket pureUdp(InetAddress address,int port,int len){
        byte[] data=Functions.getRandomData(len);
        return new DatagramPacket(data,data.length,address,port);
    }
    private DatagramPacket createUdpPacket(InetAddress address,int port,byte[] data,int len){
        return new DatagramPacket(data,len,address,port);
    }

    private DatagramPacket getLanForgePacket(InetAddress address,int port, int len){
        byte [] data,header;

        header=Functions.hexStringToByteArray("011C00011a2b3c4d00010000010000000000000100000001ffff0100");
        header[0]=(byte)((len+28)>>8 & 0xff);
        header[1]=(byte)((len+28) & 0xff);
        data=Functions.getRandomData(4);
        System.arraycopy(data,0,header,8,4);
        header[12]=(byte)((len)>>8 & 0xff);
        header[13]=(byte)((len) & 0xff);
        header[16]=(byte)((sequenceNumber)>>24 & 0xff);
        header[17]=(byte)((sequenceNumber)>>16 & 0xff);
        header[18]=(byte)((sequenceNumber)>>8 & 0xff);
        header[19]=(byte)((sequenceNumber) & 0xff);
        sequenceNumber++;

        data=Functions.getRandomData(len);
        data=Functions.concatenateByteArrays(header,data);

        return new DatagramPacket(data,data.length,address,port);
    }
    public DatagramPacket createPacket(InetAddress address,int port, int len){
        byte [] data,header;
        //header=Functions.hexStringToByteArray(activity.header);
        header = Functions.hexStringToByteArray(headers.get(activity.headerNumber % headers.size()));
        //header=Functions.hexStringToByteArray("05641ac403000400c9b7c1c1030c0128010001000301640000007b5e6400000000005b");
        //header=activity.header.getBytes();
        //header=Functions.concatenateByteArrays(Functions.getRandomData(2),header);
        data=Functions.getRandomData(len);
        data=Functions.concatenateByteArrays(header,data);
        //data=Functions.concatenateByteArrays(data,Functions.hexStringToByteArray("00010001"));
        return new DatagramPacket(data,data.length,address,port);
        //return new DatagramPacket(header,header.length,address,port);
    }
    private DatagramPacket DHCPpacketCreator(InetAddress address,int port,int len){
        //01b4b1b300080002012f0001000e000100011b0165dac4346b47f54d0003+(data.length,2byte)+data+002700060004726576650010000e0000013700084d53465420352e30000600080011001700180027
        byte [] data;
        data=Functions.concatenateByteArrays(Functions.hexStringToByteArray("01b4b1b300080002012f0001000e000100011b0165dac4346b47f54d00030000"),Functions.getRandomData(len));
        data[30]=(byte)(len>>8 & 0xff);
        data[31]=(byte)(len & 0xff);
        data=Functions.concatenateByteArrays(data,Functions.hexStringToByteArray("002700060004726576650010000e0000013700084d53465420352e30000600080011001700180027"));
        return new DatagramPacket(data,data.length,address,port);
    }

}
