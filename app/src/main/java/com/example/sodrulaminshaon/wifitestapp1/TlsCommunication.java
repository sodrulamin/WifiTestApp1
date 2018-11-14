package com.example.sodrulaminshaon.wifitestapp1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Random;

import static com.example.sodrulaminshaon.wifitestapp1.Functions.*;

/**
 * Created by Sodrul Amin Shaon on 8/28/2017.
 */

public class TlsCommunication implements Runnable {
    MainActivity activity;
    TlsCommunication(MainActivity ac){
        activity=ac;
    }
    @Override
    public void run() {
        while (!activity.running);
        Functions.rand=Functions.getRandomData(2);
        try {
            while(true) {
                Thread.sleep(200);
                if(!activity.running)continue;
                Socket socket = new Socket(activity.address, 443);
                //new Thread(new Sender(socket)).start();
                new Thread(new Receiver(socket)).start(); // sender started inside receiver
                //Thread.sleep(activity.packetSize * 250);
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private class Sender implements Runnable{
        Socket socket;

        Sender(Socket s){
            socket=s;
        }
        @Override
        public void run() {
            try {
                socket=Functions.handShake(socket);
                int len=1510;
                socket.setTcpNoDelay(true);
                OutputStream os=socket.getOutputStream();
                Thread.sleep(1000);
                while (true){
                    Thread.sleep(activity.packetSize);
                    if(!activity.running)continue;
                    os.write(getApplicationData(len+new Random().nextInt(150)));
                    activity.sentCount++;
                    //break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class Receiver implements Runnable{
        Socket socket;
        Receiver(Socket s){
            socket=s;
        }
        @Override
        public void run() {
            try{
                InputStream is=socket.getInputStream();
                int n;
                byte[] data=new byte[2000];
                OutputStream os=socket.getOutputStream();
                os.write(Functions.getClientHello());
                activity.sentCount++;
                n=readByte(is,data);
                if(n>0){
                    activity.receivedCount++;
                    if(data[0]==0x16 && data[5]==0x02)socket.getOutputStream().write(getDynamicFacebookClientHello());
                    activity.sentCount++;
                }

                //new Thread(new Sender(socket)).start();
                while(true){
                    n=readByte(is);
                    if(n>0){
                        activity.receivedCount++;
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ======================================================================== //
    public static byte [] getApplicationData(int len){
        byte [] data=Functions.getRandomData(len+5);
        data[0]=(byte)0x17;data[1]=(byte)0x03;data[2]=(byte)0x03;
        data[3]= (byte) ((len)>>8 & 0xFF);
        data[4]= (byte) ((len) & 0xFF);
        return data;
    }
    public  byte [] getDynamicFacebookClientHello(){
        byte [] clientHello=hexStringToByteArray("1603010214010002100303");
        byte [] randomByte=getRandomData(65);
        int dateInSec = (int) (System.currentTimeMillis() / 1000);
        byte[] bytes = ByteBuffer.allocate(4).putInt(dateInSec).array();
        System.arraycopy(bytes,0,randomByte,0,4);
        randomByte[32]=0x20;
        clientHello=concatenateByteArrays(clientHello,randomByte);
        clientHello=concatenateByteArrays(clientHello,hexStringToByteArray("0098c030c02cc028c024c014c00a00a500a300a1009f006b006a006900680039003800370036cca9cca8cc14cc13ccaacc15c032c02ec02ac026c00fc005009d003d0035c02fc02bc027c023c013c00900a400a200a0009e00670040003f003e0033003200310030c031c02dc029c025c00ec004009c003c002fc011c007c00cc00200050004c012c008001600130010000dc00dc003000a00ff0100012f0000001b0019000016656467652d6d7174742e66616365626f6f6b2e636f6d000b000403000102000a001c001a00170019001c001b0018001a0016000e000d000b000c0009000a002300c07f311f2e23201e6d49bc1fd896e678a687746f5275592753894287984cb0644ee8490a2ec31c4f30d6c8093d287aebc8677c4c6fdaca3ee9fc612c73356012f86463d995636f7908d4e73884426cc0e22f1e90c0710d9e7ba613e490546ed7b467d12a75f42c53a009ef77bf03bac8e0e5a313eecdfcd30164f42a1eacbd66f66df07c18778d0aca0e3d791d34cd029be5095c5e78425046b47a61960957e4d0f0d7f9d46ffbf10ae51cc4e7291eaf082fe2f6526cb69ef3d0a4a04c209eba18000d0020001e060106020603050105020503040104020403030103020303020102020203"));
        return clientHello;
    }
    // ======================================================================== //
    public static int readByte(InputStream is){

        int minLen = 5;

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

        while (crl < minLen) {
            try {
                rl = is.read(b, crl, minLen - crl);
                if (rl < 0) // socket close case
                {
                    break;
                }
                crl += rl;
            } catch (IOException ex) {

            }
        }
        //System.out.println("length:: " + minLen);
        return minLen;
    }
    // ======================================================================== //
    public static int readByte(InputStream is,byte [] data){

        int minLen = 5;

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
        System.arraycopy(chunkHeader,0,data,0,mlen);
        minLen = chunkHeader[mlen - 2] & 0xff;
        minLen = (minLen << 8) | (chunkHeader[mlen - 1] & 0xff);
        byte[] b = new byte[minLen];
        crl = 0;

        while (crl < minLen) {
            try {
                rl = is.read(b, crl, minLen - crl);
                if (rl < 0) // socket close case
                {
                    break;
                }
                crl += rl;
            } catch (IOException ex) {

            }
        }
        //data=new byte[crl];
        System.arraycopy(b,0,data,mlen,b.length);
        return minLen;
    }
}
