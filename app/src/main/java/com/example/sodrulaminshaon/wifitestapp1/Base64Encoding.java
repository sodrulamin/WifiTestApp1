package com.example.sodrulaminshaon.wifitestapp1;

import android.util.Base64;

import com.example.sodrulaminshaon.wifitestapp1.tcp.TCPReceiver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sodrul Amin Shaon on 10/14/2017.
 */

public class Base64Encoding implements Runnable {
    MainActivity activity;
    Random random=new Random();
    ArrayList<String> headerList;
    Base64Encoding(MainActivity a){
        activity=a;
        headerList=new ArrayList<>();
    }
    @Override
    public void run() {
        headerList.add("shaontest&&&&&&&&");
        headerList.add("nasirvairock&&&&&");
        headerList.add("monirvaibo&&&&&&&");
        headerList.add("wedidit&&&&&&&&&&");
        headerList.add("alaminvaiisonfire");
        headerList.add("newline&&&&&&&&&&");
        headerList.add("testingyou&&&&&&&");
        headerList.add("screwetisalat&&&&");
        while (!activity.running);
        while (true) {
            try {
                Thread.sleep(activity.packetSize);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(!activity.running)continue;
            new Thread(new Sender()).start();
            //new Thread(new Receiver()).start();
            break;
        }
    }

    private class Sender implements Runnable{
        Socket socket;
        @Override
        public void run() {
            try {
                socket=new Socket(activity.address,activity.port);
                //new Thread(new Receiver(socket)).start();
                OutputStream os=socket.getOutputStream();
                InputStream is = socket.getInputStream();

                //**** starting receiver *********//
                new TCPReceiver(activity,socket).start();

                while(true) {
                    Thread.sleep(activity.packetSize);
                    if(!activity.running)continue;
                    os.write(createBase64Packet(activity.packetSize +random.nextInt(20)));
                    activity.sentCount++;
                    //if(readByte(is) > 0)activity.receivedCount++;

                    //break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //////****************** creating dummy socket *******************///////////
            int n=new Random().nextInt(10);
            if(n>=5){
                try {
                    socket=new Socket(activity.address,activity.port);
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
    }
    private class Receiver implements Runnable{
        Socket socket;
        Receiver(Socket s){
            socket=s;
        }
        Receiver(){
            try {
                socket=new Socket(activity.address1,activity.port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            try {
                socket.setSoTimeout(2000);
                InputStream is=socket.getInputStream();
                int n;
                while(true){
                    n=readByte(is);
                    if(n>1)activity.receivedCount++;
                   // break;
                }
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
        return crl;
    }
    private byte[] createBase64Packet(int len){
        byte[] data=Base64.encode(Functions.getRandomData(len), 0),header=Functions.hexStringToByteArray("434f4e4e454354202f");
        header=Functions.concatenateByteArrays(header,headerList.get(random.nextInt(headerList.size())).getBytes());
        header=Functions.concatenateByteArrays(header,Functions.hexStringToByteArray("0d0a"));
        //data = Functions.getRandomData(len);
        data[0]=(byte)((data.length-2)>>8 & 0xff);
        data[1]=(byte)((data.length-2) & 0xff);
        //data=Functions.concatenateByteArrays(header,data);
        return  data;
    }
}
