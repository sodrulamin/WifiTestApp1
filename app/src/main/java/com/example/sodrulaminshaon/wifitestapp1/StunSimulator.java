package com.example.sodrulaminshaon.wifitestapp1;

import com.example.sodrulaminshaon.wifitestapp1.udp.StunImplementation;
import com.example.sodrulaminshaon.wifitestapp1.udp.UDPReceiver;
import com.example.sodrulaminshaon.wifitestapp1.udp.UDPSender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import static com.example.sodrulaminshaon.wifitestapp1.Functions.appendAttribute;

/**
 * Created by Sodrul Amin Shaon on 8/20/2017.
 */

public class StunSimulator implements Runnable {
    MainActivity activity;
    static long count1=10051,count2=823827,count3=104;
    private long lastStunSend=0,lastDumiSend=0;
    private static int typeUserName = 6,typePriority = 0x24,typeIceControlling = 0x802a,typeMsImplementationVersion = 0x8070,typeMessageIntegrity = 0x08,typeFingerPrint = 0x8028;
    StunSimulator(MainActivity ac){
        activity=ac;
    }
    private static byte [] userName = Functions.getRandomWord(9).getBytes();

    static{
        userName[4] = 0x3a;
    }

    @Override
    public void run() {
        //exactSkypeTest();
        //firstStunThenUdp();
        test();
    }
    private void test(){
        temparray = new byte[2048];
        StunImplementation stunImplementation = new StunImplementation();
        int len = stunImplementation.createDummyStun(temparray);
//        System.out.println("Len:1 "+len);
//        len = stunImplementation.decodeStunPacket(temparray,0,len);
//        System.out.println("Len:2 "+len);
//
//
//        len = Functions.getRandomData(temparray,0,205);
//        System.out.println("Len:3 "+len);
//        System.out.println("DataString: "+Functions.bytesToHex(temparray,0,len));
//        len = stunImplementation.createStunFromData(temparray,0,len);
//        System.out.println("Len:4 "+len);
//        len = stunImplementation.decodeStunPacket(temparray,0,len);
//        System.out.println("Len:5 "+len);
//        System.out.println("DataString: "+Functions.bytesToHex(temparray,0,len));
        try {
            while(!activity.running);
            DatagramSocket socket = new DatagramSocket();
            new Thread(new UdpReceiver(socket)).start();
            DatagramPacket packet = new DatagramPacket(temparray,len);
            packet.setAddress(activity.address);
            packet.setPort(activity.port);
            /*socket.send(packet);
            socket.send(packet);
            socket.send(packet);
            activity.sentCount+=3;*/
            while(true){
                Thread.sleep(activity.packetSize);
                if(!activity.running)continue;
                len = Functions.getRandomData(temparray,0,activity.packetSize);
                len = stunImplementation.createStunFromData(temparray,0,len);
                packet.setData(temparray,0,len);
                packet.setAddress(activity.address);
                packet.setPort(activity.port);
                socket.send(packet);
                activity.sentCount++;
            }


        } catch (SocketException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void firstStunThenUdp(){

        try {
            while(!activity.running);
            int i=0;
            byte[] data = new byte[2048];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            while(true) {
                Thread.sleep(20);
                if(!activity.running)continue;
                DatagramSocket socket = new DatagramSocket();
                packet = createStun();
                socket.send(packet);
                socket.send(packet);
                socket.send(packet);
                activity.sentCount += 3;

                UDPSender sender = new UDPSender(activity);
                sender.socket = socket;
                UDPReceiver receiver = new UDPReceiver(activity);
                receiver.socket = socket;
                sender.totalPacket = activity.headerNumber-3;
                sender.start();
                receiver.start();
                //sender.join();
                Thread.sleep(sender.totalPacket * activity.packetSize);
                i++;
                System.out.println("Finished "+i+"th time");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exactSkypeTest(){
        try {
            while(!activity.running);
            int i=0;
            while (true) {
                DatagramSocket socket = new DatagramSocket(8520+i);
                new Thread(new UdpSender(socket)).start();
                new Thread(new UdpReceiver(socket)).start();
                Thread.sleep(activity.headerNumber * activity.packetSize);
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
                DatagramPacket packet = createStun();
                socket.send(packet);
                socket.send(packet);
                socket.send(packet);
                activity.sentCount+=3;
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
            packetPerSocket = 0;
            byte [] data = new byte[2048];
            while (true)
            {
                try {
                    Thread.sleep(activity.packetSize);
                    if(!activity.running)continue;
                    //socket.send(createUdp(activity.address,activity.port,activity.packetSize));
                    //socket.send(createUdp());
                    socket.send(createStunPacket(data));
                    activity.sentCount++;
                    packetPerSocket++;
                    if(packetPerSocket>=activity.headerNumber)break;
                    /*if(System.currentTimeMillis()>lastStunSend+1000){
                        socket.send(createStun());
                        activity.sentCount++;
                        lastStunSend=System.currentTimeMillis();
                    }
                    if(System.currentTimeMillis()>lastDumiSend+2000){
                        socket.send(createDamiUdp(activity.address, activity.port, activity.sentCount%3));
                        activity.sentCount++;
                        lastDumiSend=System.currentTimeMillis();
                    }*/
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private DatagramPacket createStunPacket(byte [] data){
        int len = Functions.getRandomData(data,0,activity.packetSize);
        len = createStun(data,0,len);
        System.out.println("Packet size: "+len);
        DatagramPacket packet = new DatagramPacket(data,len,activity.address,activity.port);
        return packet;
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
    private DatagramPacket createUdp(){
        byte [] data = Functions.getRandomData(activity.packetSize);
        return new DatagramPacket(data,data.length,activity.address,activity.port);
    }
    private DatagramPacket createStun(){
        byte [] data;
        data=Functions.concatenateByteArrays(Functions.hexStringToByteArray("0001004c2112a442"),Functions.getRandomData(12));
        //attribute 1
        data=Functions.concatenateByteArrays(data, appendAttribute(6,9,userName)); // USERNAME
        data=Functions.concatenateByteArrays(data,Functions.hexStringToByteArray("000000")); ///padding
        //attribute 2
        data=Functions.concatenateByteArrays(data, appendAttribute(36,4,Functions.hexStringToByteArray("6efffeff"))); //// PRIORITY
        //attribute 3
        data=Functions.concatenateByteArrays(data, appendAttribute(32810,8,Functions.hexStringToByteArray("00000000142b8582"))); ////ICE - CONTROLLING
        //attribute 4
        data=Functions.concatenateByteArrays(data, appendAttribute(32880,4,Functions.hexStringToByteArray("00000003"))); ////MS - IMPLEMENTATION - VERSION
        //attribute 5
        data=Functions.concatenateByteArrays(data, appendAttribute(8,20)); ////MESSAGE INTEGRITY
        //attribute 6
        data=Functions.concatenateByteArrays(data, appendAttribute(32808,4)); /// FINGERPRINT

        return new DatagramPacket(data,data.length,activity.address,activity.port);
    }
    byte [] temparray;
    private int createStun(byte [] data,int offset,int len){
        temparray  = new byte[2048];
        System.arraycopy(data,offset,temparray,0,len);
        int index = 0,dataAddedIndex = 0,a,trxData;
        data[index++] = 0x00;
        data[index++] = 0x01;
        index+=2;

        data[index++] = 0x21;
        data[index++] = 0x12;
        data[index++] = (byte) 0xa4;
        data[index++] = 0x42;

        trxData = 12;
        System.arraycopy(temparray,dataAddedIndex,data,index,trxData);
        index+=trxData;
        dataAddedIndex+=trxData;
        index = appendAttribute(data,index,typeUserName,userName.length,userName);
        if(userName.length%4 != 0) {
            for (int i = 0; i < (4 - (userName.length % 4)); i++)
                data[index++] = 0x00;
        }
        a = (len - dataAddedIndex)/3;
        a-=a%4;

        data[index++] = (byte)(typePriority>>8 & 0xff);
        data[index++] = (byte)(typePriority & 0xff);
        data[index++] = (byte)(a>>8 & 0xff);
        data[index++] = (byte)(a & 0xff);
        for(int i=0;i<a;i++)
            data[index++] = temparray[dataAddedIndex++];

        data[index++] = (byte)(typeIceControlling>>8 & 0xff);
        data[index++] = (byte)(typeIceControlling & 0xff);
        data[index++] = (byte)(a>>8 & 0xff);
        data[index++] = (byte)(a & 0xff);
        for(int i=0;i<a;i++)
            data[index++] = temparray[dataAddedIndex++];

        data[index++] = (byte)(typeMsImplementationVersion>>8 & 0xff);
        data[index++] = (byte)(typeMsImplementationVersion & 0xff);
        data[index++] = (byte)(4>>8 & 0xff);
        data[index++] = (byte)(4 & 0xff);
        data[index++] = 0x00;
        data[index++] = 0x00;
        data[index++] = 0x00;
        data[index++] = 0x03;

        data[index++] = (byte)(typeMessageIntegrity>>8 & 0xff);
        data[index++] = (byte)(typeMessageIntegrity & 0xff);
        data[index++] = (byte)(a>>8 & 0xff);
        data[index++] = (byte)(a & 0xff);
        for(int i=0;i<a;i++)
            data[index++] = temparray[dataAddedIndex++];

        //a = len - dataAddedIndex;
        a = 4;
        data[index++] = (byte)(typeFingerPrint>>8 & 0xff);
        data[index++] = (byte)(typeFingerPrint & 0xff);
        data[index++] = (byte)(a>>8 & 0xff);
        data[index++] = (byte)(a & 0xff);
        for(int i=0;i<a;i++)
            data[index++] = temparray[dataAddedIndex++];

        index = index-20;
        data[2] = (byte)(index>>8 & 0xff);
        data[3] = (byte)(index & 0xff);
        return index+20;
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
        data=Functions.concatenateByteArrays(data, appendAttribute(32,8,xorValue)); //// XOR MAPPED ADDRESS

        data=Functions.concatenateByteArrays(data, appendAttribute(32880,4,Functions.hexStringToByteArray("00000003"))); /// MS - IMPLEMENTATION - VERSION

        data=Functions.concatenateByteArrays(data, appendAttribute(8,20)); /// MESSAGE - INTEGRITY

        data=Functions.concatenateByteArrays(data, appendAttribute(32808,4));/// FINGERPRINT

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
                break;
        }

        return new DatagramPacket(data, data.length,address,port);
    }

}
