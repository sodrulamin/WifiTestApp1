package com.example.sodrulaminshaon.wifitestapp1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * Created by Sodrul Amin Shaon on 8/20/2017.
 */

public class ExactSkype implements Runnable {
    MainActivity activity;
    static long count1=10051,count2=823827,count3=104;
    private long lastStunSend=0,lastDumiSend=0;
    ExactSkype(MainActivity ac){
        activity=ac;
    }
    @Override
    public void run() {
        try {
            while(!activity.running);
            int i=0;
            while (true) {
                DatagramSocket socket = new DatagramSocket(8520+i);
                new Thread(new UdpSender(socket)).start();
                new Thread(new UdpReceiver(socket)).start();
                Thread.sleep(activity.packetPerSocket*200);
                i++;
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
        UdpSender(DatagramSocket socket){
            this.socket=socket;
        }

        @Override
        public void run() {
            while(!activity.running);
            try {
                socket.send(createStun());
                activity.sentCount++;
                Thread.sleep(500);
                /*socket.send(createDamiUdp(activity.address, activity.port, 0));
                socket.send(createDamiUdp(activity.address, activity.port, 1));
                socket.send(createDamiUdp(activity.address, activity.port, 2));
                activity.sentCount+=3;
                lastDumiSend=System.currentTimeMillis();*/
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (true){
                try {
                    Thread.sleep(200);
                    if(!activity.running)continue;
                    socket.send(createUdp(activity.address,activity.port,200));
                    activity.sentCount++;
                    packetPerSocket++;
                    if(packetPerSocket>=activity.packetPerSocket)break;
                    if(System.currentTimeMillis()>lastStunSend+1000){
                        socket.send(createStun());
                        activity.sentCount++;
                        lastStunSend=System.currentTimeMillis();
                    }
                    if(System.currentTimeMillis()>lastDumiSend+2000){
                        socket.send(createDamiUdp(activity.address, activity.port, activity.sentCount%3));
                        activity.sentCount++;
                        lastDumiSend=System.currentTimeMillis();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class UdpReceiver implements Runnable{
        DatagramSocket socket;
        UdpReceiver (DatagramSocket s){
            socket=s;
        }

        @Override
        public void run() {
            DatagramPacket packet=new DatagramPacket(new byte[2000],2000);
            while (true){
                try {
                    socket.receive(packet);
                    activity.receivedCount++;
                    byte [] data=packet.getData();
                    if(data[0]==0x00 && data[1]==0x01){
                        if(activity.running) {
                            socket.send(stunReply(packet));
                            activity.sentCount++;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private DatagramPacket createUdp(InetAddress address, int port, int len){
        //9068+(2,21071,1)+(3,115200,1)+(3/7/b/f)e+96940100bede000+(1 for 7 times, 3 for 8th time)+12+(1,15,12)+data
        byte [] data,header;
        header=Functions.hexStringToByteArray("90680000000000");
        header[2]=(byte)(count1>>8 & 0xff);
        header[3]=(byte)(count1 & 0xff);
        header[4]=(byte)(count2>>16 & 0xff);
        header[5]=(byte)(count2>>8 & 0xff);
        header[6]=(byte)(count2 & 0xff);
        String s="";
        switch ((int) (count1%4)){
            case 0:
                s+="12";
                break;
            case 1:
                s+="52";
                break;
            case 2:
                s+="92";
                break;
            case 3:
                s+="d2";
                break;
        }
        header=Functions.concatenateByteArrays(header,Functions.hexStringToByteArray(s+"554f8165bede00011200"));
        if(count1%8==0)header[header.length-3]=(byte)(3 & 0xff);
        header[header.length-1]=(byte)(count3 & 0xff);

        data=Functions.getRandomData(len);
        data=Functions.concatenateByteArrays(header,data);
        count1++;count2++;
        if(count1%12==0)count3++;
        //data=Functions.getRandomData(len);
        return new DatagramPacket(data,data.length,address,port);
    }
    private DatagramPacket createStun(){
        byte [] data;
        data=Functions.concatenateByteArrays(Functions.hexStringToByteArray("0001004c2112a442"),Functions.getRandomData(12));
        //attribute 1
        data=Functions.concatenateByteArrays(data,addAttribute(6,9,Functions.hexStringToByteArray("517457683a47676648"))); // USERNAME
        data=Functions.concatenateByteArrays(data,Functions.hexStringToByteArray("000000")); ///padding
        //attribute 2
        data=Functions.concatenateByteArrays(data,addAttribute(36,4,Functions.hexStringToByteArray("6efffeff"))); //// PRIORITY
        //attribute 3
        data=Functions.concatenateByteArrays(data,addAttribute(32810,8,Functions.hexStringToByteArray("00000000142b8582"))); ////ICE - CONTROLLING
        //attribute 4
        data=Functions.concatenateByteArrays(data,addAttribute(32880,4,Functions.hexStringToByteArray("00000003"))); ////MS - IMPLEMENTATION - VERSION
        //attribute 5
        data=Functions.concatenateByteArrays(data,addAttribute(8,20)); ////MESSAGE INTEGRITY
        //attribute 6
        data=Functions.concatenateByteArrays(data,addAttribute(32808,4)); /// FINGERPRINT

        return new DatagramPacket(data,data.length,activity.address,activity.port);
    }
    private DatagramPacket stunReply(DatagramPacket packet){
        byte [] data=packet.getData();
        byte [] trxId=new byte[12],cookie=new byte[4];
        System.arraycopy(data,8,trxId,0,12);
        System.arraycopy(data,4,cookie,0,4);
        data=Functions.concatenateByteArrays(Functions.hexStringToByteArray("010100342112a442"),trxId);
        byte [] xorValue=new byte[6];
        xorValue[0]= (byte) ((byte)(activity.port>>8 & 0xff) ^ (cookie[0]));
        xorValue[1]= (byte) ((byte)(activity.port & 0xff) ^ (cookie[1]));
        byte [] ip=activity.address.getAddress();
        for(int i=0;i<4;i++)xorValue[2+i]= (byte) (ip[i] ^ cookie[i]);
        xorValue=Functions.concatenateByteArrays(Functions.hexStringToByteArray("0001"),xorValue);
        data=Functions.concatenateByteArrays(data,addAttribute(32,8,xorValue)); //// XOR MAPPED ADDRESS

        data=Functions.concatenateByteArrays(data,addAttribute(32880,4,Functions.hexStringToByteArray("00000003"))); /// MS - IMPLEMENTATION - VERSION

        data=Functions.concatenateByteArrays(data,addAttribute(8,20)); /// MESSAGE - INTEGRITY

        data=Functions.concatenateByteArrays(data,addAttribute(32808,4));/// FINGERPRINT

        return new DatagramPacket(data,data.length,activity.address,activity.port);
    }
    private DatagramPacket createDamiUdp(InetAddress address,int port,int type){
        byte [] data=null,header;
        switch(type){
            case 0:
                header=Functions.hexStringToByteArray("8fce0014b66a2b00");
                data=Functions.getRandomData(90);
                data=Functions.concatenateByteArrays(header, data);
                break;
            case 1:
                header=Functions.hexStringToByteArray("80c80006b66a2b00");
                data=Functions.getRandomData(34);
                data=Functions.concatenateByteArrays(header, data);
                break;
            case 2:
                header=Functions.hexStringToByteArray("81c90010b66a2b00");
                data=Functions.getRandomData(158);
                data=Functions.concatenateByteArrays(header, data);

        }

        return new DatagramPacket(data, data.length,address,port);
    }

    private byte [] addAttribute(int type, int len,byte [] value){
        byte [] data=new byte[4];
        data[0]=(byte)(type>>8 & 0xff);
        data[1]=(byte)(type & 0xff);
        data[2]=(byte)(len>>8 & 0xff);
        data[3]=(byte)(len & 0xff);
        data=Functions.concatenateByteArrays(data,value);
        return data;
    }
    private byte [] addAttribute(int type, int len){
        byte [] data=new byte[4];
        data[0]=(byte)(type>>8 & 0xff);
        data[1]=(byte)(type & 0xff);
        data[2]=(byte)(len>>8 & 0xff);
        data[3]=(byte)(len & 0xff);
        data=Functions.concatenateByteArrays(data,Functions.getRandomData(len));
        return data;
    }
}
