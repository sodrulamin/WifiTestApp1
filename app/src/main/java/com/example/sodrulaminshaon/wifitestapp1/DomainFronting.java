package com.example.sodrulaminshaon.wifitestapp1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static com.example.sodrulaminshaon.wifitestapp1.Functions.getRandomData;

/**
 * Created by Sodrul Amin Shaon on 11/14/2017.
 */

public class DomainFronting implements Runnable{
    MainActivity activity;
    DomainFronting(MainActivity a){
        activity=a;
    }
    @Override
    public void run() {
        String https_url = "https://dfwithgo.appspot.com/";
        URL url;
        try {
            url = new URL(https_url);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            new Thread(new Sender(con)).start();
            new Thread(new Receiver(con)).start();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private class Sender implements Runnable{
        HttpsURLConnection socket;
        Sender(HttpsURLConnection s){
            socket=s;
        }
        @Override
        public void run() {
            try {
                OutputStream os=socket.getOutputStream();
                while (true) {
                    Thread.sleep(activity.packetSize);
                    if(!activity.running)continue;
                    os.write(createTcpPacket(activity.packetSize));
                    activity.sentCount++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private class Receiver implements Runnable{
        HttpsURLConnection socket;
        Receiver(HttpsURLConnection s){
            socket=s;
        }
        @Override
        public void run() {
            try{
                InputStream is=socket.getInputStream();
                while (true){
                    int n=readByte(is);
                    if(n>1)activity.receivedCount++;
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static byte[] createTcpPacket(int len){
        byte[] data;
        data=getRandomData(len+2);
        data[0]=(byte)((len)>>8 & 0xf);
        data[1]=(byte)((len) & 0xff);
        return data;
    }
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
}
