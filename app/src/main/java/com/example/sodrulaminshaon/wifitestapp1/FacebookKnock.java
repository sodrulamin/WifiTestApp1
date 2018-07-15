package com.example.sodrulaminshaon.wifitestapp1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Sodrul Amin Shaon on 11/6/2017.
 */

public class FacebookKnock implements Runnable {
    MainActivity activity;
    FacebookKnock(MainActivity a){
        activity=a;
    }
    @Override
    public void run() {
        try {
            while(!activity.running);

            int n;
            while(true){
                Thread.sleep(200);
                if(!activity.running)continue;
                Socket socket=new Socket(InetAddress.getByName("m.facebook.com"),443);
                OutputStream os=socket.getOutputStream();
                InputStream is=socket.getInputStream();

                os.write(getClientHello());
                activity.sentCount++;
                n=readByte(is);
                if(n>0){
                    activity.receivedCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private byte[] getClientHello(){
        byte[] packet=Functions.hexStringToByteArray("16030100c7010000c30303ca3451a5d78f036d6c83e6e99d5e0dd3199e31aa6e5fbc4525385ce87871633b00001ccacac02bc02fc02cc030cca9cca8c013c014009c009d002f0035000a0100007e5a5a0000ff010001000000001500130000107777772e66616365626f6f6b2e636f6d0017000000230000000d00140012040308040401050308050501080606010201000500050100000000001200000010000e000c02683208687474702f312e3175500000000b00020100000a000a00083a3a001d00170018aaaa000100");
        return packet;
    }
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
}
