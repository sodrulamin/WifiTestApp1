package com.example.sodrulaminshaon.wifitestapp1;

import android.renderscript.ScriptGroup;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Sodrul Amin Shaon on 10/13/2017.
 */

public class RandomHeaderCreatorTCP implements Runnable {
    MainActivity activity;
    int checkedYet;
    RandomHeaderCreatorTCP(MainActivity a){
        activity=a;
        checkedYet=0;
    }
    @Override
    public void run() {
        while(!activity.running);
        activity.runOnUiThread(new Thread(new Runnable() {
            @Override
            public void run() {
                activity.titleTextView.setText("Checking .......... ");
            }
        }));
        while (true){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(!activity.running)continue;
            /*activity.runOnUiThread(new Thread(new Runnable() {
                @Override
                public void run() {
                    activity.sentTextView.setText(checkedYet+"");
                }
            }));*/
            new Thread(new Sender()).start();
            try {
                Thread.sleep(100*200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            checkedYet++;
        }
    }

    private class Sender implements Runnable{
        byte[] header;
        int sentCount;
        Sender(){
            sentCount=0;
            header=Functions.getRandomData(150);
        }

        @Override
        public void run() {
            try {
                Socket socket=new Socket(activity.address,activity.port);
                socket.setTcpNoDelay(true);
                socket.setSoTimeout(1000);
                OutputStream os=socket.getOutputStream();
                InputStream is=socket.getInputStream();
                new Thread(new Receiver(is)).start();
                for(int i=0;i<100;i++){
                    os.write(Functions.concatenateByteArrays(header,Functions.getRandomData(200)));
                    Thread.sleep(200);
                }
            } catch (IOException e) {

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            activity.runOnUiThread(new Thread(new Runnable() {
                @Override
                public void run() {
                    activity.sentTextView.setText(checkedYet+"");
                }
            }));
        }
    }

    private class Receiver implements Runnable{
        InputStream is;
        int receivedCount;
        Receiver(InputStream i){
            is=i;
            receivedCount=0;
        }
        @Override
        public void run() {
            try{
                int n;
                while(true){
                    n=readByte(is);
                    if(n>0)receivedCount++;
                }
            } catch (IOException e) {
                //e.printStackTrace();
            }
            if(receivedCount>90){
                ////write the header in file.
            }
        }
    }

    // ======================================================================== //
    public int readByte(InputStream is) throws IOException {

        int minLen = 152;

        int rl, crl;
        int mlen = minLen;

        byte[] chunkHeader = new byte[minLen];
        crl = rl = 0;
        while (crl < minLen) {
            rl = is.read(chunkHeader, crl, minLen - crl);
            if (rl < 0)break;
            crl += rl;
        }
        minLen = chunkHeader[mlen - 2] & 0xff;
        minLen = (minLen << 8) | (chunkHeader[mlen - 1] & 0xff);
        byte[] b = new byte[minLen];
        crl = 0;

        while (crl < minLen) {
            rl = is.read(b, crl, minLen - crl);
            if (rl < 0) // socket close case
            {
                break;
            }
            crl += rl;
        }
        //System.out.println("length:: " + minLen);
        return minLen;
    }
}
