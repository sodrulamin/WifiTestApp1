package com.example.sodrulaminshaon.wifitestapp1.tcp;

import com.example.sodrulaminshaon.wifitestapp1.Functions;
import com.example.sodrulaminshaon.wifitestapp1.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

/**
 * Created by Sodrul Amin Shaon on 10-Sep-18.
 */

public class TCPTest extends Thread {
    MainActivity activity;
    SSHImplementation sshImplementation;
    public final int SSH = 1,FIXED_BYTES_SENDING_TEST = 2,NORMAL_TCP = 3;
    Random random;
    public TCPTest(MainActivity a){
        activity = a;
        sshImplementation = new SSHImplementation();
        random = new Random();
    }

    @Override
    public void run(){
        try{
            while (!activity.running);
            while (true){
                Thread.sleep(50);
                if(!activity.running)continue;
                Socket socket = new Socket(activity.address,activity.port);
                new TCPManager(socket,FIXED_BYTES_SENDING_TEST).start();

                Thread.sleep(activity.packetSize * 150);
                break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class TCPManager extends Thread{
        Socket socket;
        int switcher;
        public TCPManager(Socket s,int sw){
            socket = s;
            switcher = sw;
        }


        @Override
        public void run(){
            try{
                socket.setSoTimeout(3000);
                socket.setTcpNoDelay(true);
                OutputStream os = socket.getOutputStream();
                byte [] data = new byte[2048];

                if(switcher == SSH) sshImplementation.startClientHandShake(socket);
                System.out.println("SSH Handshake done.");

                new TCPReceiver(socket,switcher).start();
                int len = 0;
                while (true){
                //for(int i=0;i<150;i++){
                    Thread.sleep(activity.packetSize);
                    if(!activity.running)continue;
                    switch (switcher) {
                        case SSH:
                            len = Functions.getRandomData(data, 0, activity.packetSize +random.nextInt(activity.headerNumber));
                            len = sshImplementation.createSSHPacket(data, 0, len);
                            break;
                        case FIXED_BYTES_SENDING_TEST:
                            len = Functions.hexStringToByteArray("0101000b0000000101470012617474726962757465732d6368617273657400057574662d3848001b617474726962757465732d6e61747572616c2d6c616e67756167650005656e2d757345000b7072696e7465722d757269001b6970703a2f2f31302e31302e31302e3235313a3633312f6970702f4400147265717565737465642d617474726962757465730010636f706965732d737570706f727465644400000019646f63756d656e742d666f726d61742d737570706f7274656444000000197072696e7465722d69732d616363657074696e672d6a6f6273440000000d7072696e7465722d737461746544000000157072696e7465722d73746174652d6d65737361676544000000157072696e7465722d73746174652d726561736f6e7303",data,0);
                            //len = createBGPData(data);
                            break;
                        case NORMAL_TCP:
                            len = Functions.getRandomData(data,0,activity.packetSize);
                            data[0] = (byte)((len - 2) >> 8 & 0xff);
                            data[1] = (byte)((len - 2) & 0xff);
                            break;

                    }
                    os.write(data,0,len);
                    activity.sentCount++;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    private int createBGPData(byte [] data){
        int index = 0;
        Functions.getRandomData(data,index,16);
        index+=16;
        index = Functions.hexStringToByteArray("002d0104fe4c00b40a0000011002060104000100010202800002020200",data,index);

        return index;
    }
    public class TCPReceiver extends Thread{
        Socket socket;
        int switcher;
        public TCPReceiver(Socket s,int sw){
            socket = s;
            switcher = sw;
        }
        @Override
        public void run(){
            try{

                byte [] data = new byte[2048];
                InputStream is = socket.getInputStream();
                int receivedLen = 0;
                while (true){
                    switch (switcher) {
                        case SSH:
                            receivedLen = sshImplementation.getDataFromSSHStream(is, data, 0);
                            break;
                        case FIXED_BYTES_SENDING_TEST:
                            receivedLen = readByte(is,data);
                            break;
                        case NORMAL_TCP:
                            receivedLen = readByte(is,data);
                            break;
                    }
                    if(receivedLen > 0)activity.receivedCount++;
                    //else break;
                }
            }catch (Exception e){
                //e.printStackTrace();
            }
        }
    }
    // ======================================================================== //
    public static int readByte(InputStream is,byte [] data){

        int minLen = 2;

        int rl, crl;
        int mlen = minLen;

        byte[] chunkHeader = new byte[minLen];
        crl = rl = 0;
        try {
            while (crl < minLen) {

                rl = is.read(chunkHeader, crl, minLen - crl);
                if (rl < 0) {

                    break;

                }
                crl += rl;

            }
        } catch (IOException ex) {

        }
        System.arraycopy(chunkHeader,0,data,0,mlen);
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
        } catch (IOException ex) {

        }

        //data=new byte[crl];
        System.arraycopy(b,0,data,mlen,b.length);
        return minLen;
    }
    public static int readByte(InputStream is,byte [] data,int len){
        int rl, crl,minLen;
        minLen=len;
        crl = 0;
        try {
            while (crl < minLen) {

                rl = is.read(data, crl, minLen - crl);
                if (rl < 0) // socket close case
                {
                    break;
                }
                crl += rl;

            }
        } catch (IOException ex) {
        }
        return crl;
    }
}
