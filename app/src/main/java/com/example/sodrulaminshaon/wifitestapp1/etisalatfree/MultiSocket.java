package com.example.sodrulaminshaon.wifitestapp1.etisalatfree;

import android.util.Base64;

import com.example.sodrulaminshaon.wifitestapp1.Functions;
import com.example.sodrulaminshaon.wifitestapp1.MainActivity;
import com.example.sodrulaminshaon.wifitestapp1.wifitest.HttpTest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Random;

/**
 * Created by Sodrul Amin Shaon on 11/21/2017.
 */

public class MultiSocket extends Thread{
    MainActivity activity;
    Random random;
    boolean startReceive;
    InetAddress receiverAddress;
    String header = "";
    HashSet<String> receivedDatagramPacketNumberList = new HashSet<>();
    public MultiSocket(MainActivity a){
        activity=a;
        random=new Random();
        startReceive=true;
    }

    @Override
    public void run(){
        while (!activity.running);

        //OneThreadReceiver oneThreadReceiver=new OneThreadReceiver();
        OneThreadUdpReceiver oneThreadReceiver=new OneThreadUdpReceiver();
        oneThreadReceiver.start();
        /*try{
            receiverAddress=InetAddress.getByName(activity.header);
            int port = 5050;
            while (true){
                Thread.sleep(activity.packetPerSocket);
                if(!activity.running)continue;
                //new Sender().start();
                new UdpSender(port++).start();
                //if(startReceive)
                    //new Receiver().start();
                startReceive=!startReceive;
                //break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }*/
    }
    private class OneThreadUdpReceiver extends Thread{
        @Override
        public void run(){
            while (!activity.running);
            try {
                int startPort=5050;
                receiverAddress=InetAddress.getByName(activity.header);
                while(true){
                //for(int i=0;i<2;i++){
                    Thread.sleep(activity.packetPerSocket);
                    //Thread.sleep(3000);
                    if(!activity.running)continue;
                    try {
                        new UdpReceiver(startPort++).start();
                    }catch (Exception e){}
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }
    private class OneThreadReceiver extends Thread{
        @Override
        public void run(){
            while (!activity.running);
            try{
                receiverAddress=InetAddress.getByName(activity.header);
                while (true){
                    Thread.sleep(activity.headerNumber);
                    if(!activity.running)continue;
                    new Receiver().start();
                    //break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }
    private class Sender extends Thread{
        Socket socket;
        @Override
        public void run(){
            try {
                socket=new Socket(activity.address,activity.port);
                socket.setTcpNoDelay(true);
                OutputStream os=socket.getOutputStream();
                InputStream is = socket.getInputStream();

                byte [] data = HttpTest.getRequest(activity.packetPerSocket+random.nextInt(20));
                //os.write(createBase64Packet(activity.packetPerSocket+random.nextInt(20)));
                os.write(data);
                activity.sentCount++;
                data = HttpTest.receiveData(is);
                activity.receivedCount++;

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(socket!=null) try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class UdpSender extends Thread{
        DatagramSocket socket;
        public UdpSender(int port) throws SocketException {
            socket=new DatagramSocket(port);
        }
        @Override
        public void run(){
            try {
                //socket.setSoTimeout(2000);
                byte [] data;//=new byte[2048];
                int len=activity.packetPerSocket+random.nextInt(20);
                data=Functions.getRandomData(len);
                DatagramPacket packet=new DatagramPacket(data,len,receiverAddress,activity.port);
                //DatagramPacket packet=new DatagramPacket(data,len,activity.address,activity.port);
                /*PingTest pingTest = new PingTest(activity);
                DatagramPacket packet = pingTest.getRegPacket();*/
                socket.send(packet);
                activity.sentCount++;
                /*socket.receive(packet);
                activity.receivedCount++;*/
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(socket!=null)socket.close();
            }
        }
    }
    private class UdpReceiver extends Thread{
        DatagramSocket socket;
        UdpReceiver(int port) throws SocketException {
            socket=new DatagramSocket();
        }
        @Override
        public void run(){
            try {
                //socket.setSoTimeout(2000);
                byte [] data;//=new byte[2048];
                int len=random.nextInt(20);
                data=Base64.encode(Functions.getRandomData(len),0);
                DatagramPacket packet=new DatagramPacket(data,len,activity.address,activity.port);
                //DatagramPacket packet=new DatagramPacket(data,len,activity.address,activity.port);
                /*PingTest pingTest = new PingTest(activity);
                DatagramPacket packet = pingTest.getRegPacket();*/
                socket.send(packet);
                activity.sentCount++;
                packet = new DatagramPacket(new byte[2048],2048);
                socket.receive(packet);
                activity.receivedCount++;
                data = packet.getData();
                byte count = data[packet.getLength()-1];
                receivedDatagramPacketNumberList.add(count+"");
                header = "";
                for(String str: receivedDatagramPacketNumberList){
                    header = header+","+str;
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.headerEditText.setText(header);
                    }
                });
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(socket!=null)socket.close();
            }
        }
    }
    private class Receiver extends Thread{
        Socket socket;
        @Override
        public void run(){
            try {
                socket=new Socket(activity.address,3306);//+random.nextInt(5));
                socket.setSoTimeout(2000);
                InputStream is=socket.getInputStream();
                byte [] data = new byte[2048];
                if(Functions.readByte(is,data,activity.headerNumber)>0)activity.receivedCount++;
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    // ======================================================================== //
    public int readByte(InputStream is){

        int minLen = 2;

        int rl, crl;
        int mlen = minLen;

        byte[] chunkHeader = new byte[minLen];
        crl = rl = 0;
        while (crl < minLen) {
            try {
                rl = is.read(chunkHeader, crl, minLen - crl);
                if (rl < 0) {

                    break;

                }
                crl += rl;
            } catch (IOException ex) {

            }
        }
        minLen = chunkHeader[mlen - 2] & 0xff;
        minLen = (minLen << 8) | (chunkHeader[mlen - 1] & 0xff);
        byte[] b = new byte[minLen];
        crl = 0;
        try {
            while (crl < minLen) {
                rl = is.read(b, crl, minLen - crl);
                if (rl < 0) // socket close case
                {
                    break;
                }
                crl += rl;
            }
        } catch (IOException ex) {}
        //System.out.println("length:: " + minLen);
        return crl;
    }
    private byte[] createBase64Packet(int len){
        byte[] data=Base64.encode(Functions.getRandomData(len), 0);
        data[0]=(byte)((data.length-2)>>8 & 0xff);
        data[1]=(byte)((data.length-2) & 0xff);
        //data=Functions.concatenateByteArrays(Functions.hexStringToByteArray("5452414345202F48656164657226262626262626587859333033303336332A2A56000000002400046EFFFEFF80290008000000000000D6E78054000131000000807000040000000300080014A0E94361DC2C88A7BE46F89C97084CC551024C33802800049842B16A"),data);
        return  data;
    }
}
