package com.example.sodrulaminshaon.wifitestapp1.udp;

import com.example.sodrulaminshaon.wifitestapp1.Functions;
import com.example.sodrulaminshaon.wifitestapp1.MainActivity;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

/**
 * Created by Sodrul Amin Shaon on 10-Jul-18.
 */

public class UDPClient extends Thread {
    MainActivity activity;
    DatagramPacket receivePacket;
    public UDPClient(MainActivity a){
        activity = a;
        receivePacket = new DatagramPacket(new byte[2048],2048);
    }

    @Override
    public void run(){
        switch (activity.headerNumber){
            case 100:
                AFSImplementation();
                break;
            case 101:
                CIGIImplementation();
                break;
            case 102:
                DNSImplementation();
                break;
        }
    }
    private DatagramPacket createDatagramPacket(byte [] data){
        return new DatagramPacket(data,data.length,activity.address,activity.port);
    }
    private void AFSImplementation(){
        try{
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(3000);

            socket.send(createDatagramPacket(Functions.hexStringToByteArray("bfcdb4be1b557a5c0000012200000001000001af010500026513000100000084200000ba0000034e0010049d")));
            activity.sentCount++;

            socket.receive(receivePacket);
            activity.receivedCount++;

            socket.send(createDatagramPacket(Functions.hexStringToByteArray("bfcdb4be1b557a5c0000012200000000000001b002210002851400010000000000000002000000010000015b0800696e650000163c000005a40000002000000004")));
            activity.sentCount++;

            socket.send(createDatagramPacket(Functions.hexStringToByteArray("bfcdb4be1b557a5c0000012300000001000001b1010501028b2d00010000008d200000ba000000010000000100000006746d7064697200000000000d382b3930cfd69f5800000000000001ed00002800")));
            activity.sentCount++;

            socket.receive(receivePacket);
            activity.receivedCount++;

            socket.send(createDatagramPacket(Functions.hexStringToByteArray("9705850ddfdcf3f80000048d00000001000004820104000000000001")));
            activity.sentCount++;

            socket.send(createDatagramPacket(Functions.hexStringToByteArray("9705850ddfdcf3f80000048d00000001000004820104000000000001")));
            activity.sentCount++;

            socket.receive(receivePacket);
            activity.receivedCount++;

            socket.send(createDatagramPacket(Functions.hexStringToByteArray("bfcdb4be1b557a5c0000012400000001000001b201050002a940000100000084200000ba000002a50010bee8")));

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void CIGIImplementation(){
        try{
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(3000);

            socket.send(createDatagramPacket(Functions.hexStringToByteArray("0110020040000000006ce72f000000002c18001800000000403a58a0902de00d405ff010624dd2f22b300015428ed4c7be152f99000000000000000043a875ec40341de6e8db8bad403a58a30e8293d9405ff0113d6bf064")));
            activity.sentCount++;

            socket.receive(receivePacket);
            activity.receivedCount++;

            socket.send(createDatagramPacket(Functions.hexStringToByteArray("0110020040000000006ce730000000002c18001900000000403a58a0902de00d405ff010624dd2f22b30001641f579c5be152ff4000000000000000043a875ed40341de6e8db8bad403a58a30e8293d9405ff0113d6bf064")));
            activity.sentCount++;

            socket.receive(receivePacket);
            activity.receivedCount++;

            socket.send(createDatagramPacket(Functions.hexStringToByteArray("0110020040000000006ce73100000000")));
            activity.sentCount++;

            socket.receive(receivePacket);
            activity.receivedCount++;

            socket.send(createDatagramPacket(Functions.hexStringToByteArray("0110020040000000006ce732000000002c18001a00000000403a58a0902de00d405ff010624dd2f22b300017424c3338be155b64000000000000000043a8413940341de6e8db8bad403a58a30e8293d9405ff0113d6bf064")));
            activity.sentCount++;

            socket.receive(receivePacket);
            activity.receivedCount++;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void DNSImplementation(){
        try{
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(3000);

            socket.send(createDatagramPacket(Functions.hexStringToByteArray("10320100000100000000000006676f6f676c6503636f6d0000100001")));
            activity.sentCount++;

            socket.receive(receivePacket);
            activity.receivedCount++;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
