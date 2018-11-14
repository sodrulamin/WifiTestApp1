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
            case 103:
                WLCCPImplementation();
                break;
            case 104:
                PortMapImplementation();
                break;
            case 105:
                HeartIPImplementation();
                break;
            case 106:
                HSRPImplementation();
                break;
            case 107:
                MessengerImplementation();
                break;
        }
    }
    private DatagramPacket createDatagramPacket(byte [] data){
        return new DatagramPacket(data,data.length,activity.address,activity.port);
    }
    private void MessengerImplementation(){
        try{
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(3000);

            socket.send(createDatagramPacket(Functions.hexStringToByteArray("040028001000000000000000000000000000000000000000f8917b5a00ffd011a9b200c04fb6e6fcb05b8b3905acb97415260f7f7096c2210000000001000000000000000000ffffffff66010000000010000000000000001000000053454355524954590000000000000000100000000000000010000000414c45525400000000000000000000002201000000000000220100004d6963726f736f66742057696e646f77732068617320646574656374656420796f757220696e7465726e65742062726f777365720a697320696e666563746564207769746820537079776172652c2041642d776172652c20616e64205468696566776172652e0a0a596f7572207072697661637920697320696e2064616e6765722e0a5765207265636f6d6d656e6420616e20696d6d6564696174652073797374656d207363616e2e20506c656173652076697369740a0a687474703a2f2f53776970655370792e636f6d0a0a4661696c75726520746f20646973696e6665637420636f756c6420616c6c6f772074686972642070617274696573206163636573730a746f20796f757220706572736f6e616c20696e666f726d6174696f6e2e0a00")));
            activity.sentCount++;

            socket.receive(receivePacket);
            activity.receivedCount++;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void HSRPImplementation(){
        try{
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(3000);

            socket.send(createDatagramPacket(Functions.hexStringToByteArray("000010030a5a0a00636973636f0000000a1ca8fe")));
            activity.sentCount++;

            socket.receive(receivePacket);
            activity.receivedCount++;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void HeartIPImplementation(){
        try{
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(3000);

            socket.send(createDatagramPacket(Functions.hexStringToByteArray("010000000002000d0100007530")));
            activity.sentCount++;

            socket.receive(receivePacket);
            activity.receivedCount++;

            socket.send(createDatagramPacket(Functions.hexStringToByteArray("010003000003001182264e0000d2000038")));
            activity.sentCount++;

            socket.receive(receivePacket);
            activity.receivedCount++;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void PortMapImplementation(){
        try{
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(3000);

            socket.send(createDatagramPacket(Functions.hexStringToByteArray("365900b50000000000000002000186a00000000200000005000000010000001c3650f3880000000662696f63353700000000000000000000000000000000000000000000000186a40000000200000002000000100000000c62696f6368656d6973747279")));
            activity.sentCount++;

            socket.receive(receivePacket);
            activity.receivedCount++;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void WLCCPImplementation(){
        try{
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(3000);

            socket.send(createDatagramPacket(Functions.hexStringToByteArray("c1400008003e0b000006100000010011bb2281a2000800181823c25a00010011bb2281a20200000000140100001001020010110100089051f4127b7e68d4")));
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

}
