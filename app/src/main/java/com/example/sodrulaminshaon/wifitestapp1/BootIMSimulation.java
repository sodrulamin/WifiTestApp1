package com.example.sodrulaminshaon.wifitestapp1;

import android.provider.ContactsContract;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * Created by Sodrul Amin Shaon on 10/8/2017.
 */

public class BootIMSimulation implements Runnable {
    MainActivity activity;
    InetAddress [] addresses;
    int count;
    BootIMSimulation(MainActivity a){
        activity=a;
        addresses=new InetAddress[2];
        count=1;
        try {
            addresses[0]=InetAddress.getByName("161.202.7.157");
            addresses[1]=InetAddress.getByName("161.202.7.156");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
            DatagramSocket socket=new DatagramSocket(54544);
            new Thread(new quicImplementation(socket)).start();
            Thread.sleep(1000);
            new  Thread(new udpImplementation(socket)).start();


            new Thread(new udpReceiver(socket)).start();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class udpImplementation implements Runnable{
        DatagramSocket socket1,socket2;
        udpImplementation(DatagramSocket s){
            socket1=s;
        }

        @Override
        public void run() {
            while (!activity.running);
            Random rand=new Random();
            int sleep=rand.nextInt(250);
            try {
                socket2=new DatagramSocket(25432);
                new Thread(new udpReceiver(socket1)).start();
                new Thread(new udpReceiver(socket2)).start();
                //DatagramSocket socket=socket1;
                int socketSentLimit=1,last=1;
                for(int i=0;i<10;i++){
                    for(int j=0;j<socketSentLimit;j++){
                        Thread.sleep(sleep);
                        if(last==1){
                            socket1.send(createPacket(addresses[0],activity.port,40+sleep));
                            activity.sentCount++;
                            sleep=rand.nextInt(250);
                        }
                        else{
                            socket2.send(createPacket(addresses[0],activity.port,40+sleep));
                            activity.sentCount++;
                            sleep=rand.nextInt(250);
                        }
                    }
                    last=3-last;
                }
                socketSentLimit=4;last=1;
                for(int i=0;i<5;i++){
                    for(int j=0;j<socketSentLimit;j++){
                        Thread.sleep(sleep);
                        if(last==1){
                            socket1.send(createPacket(addresses[0],activity.port,40+sleep));
                            activity.sentCount++;
                            sleep=rand.nextInt(250);
                        }
                        else{
                            socket2.send(createPacket(addresses[0],activity.port,40+sleep));
                            activity.sentCount++;
                            sleep=rand.nextInt(250);
                        }
                    }
                    last=3-last;
                }
                socketSentLimit=8;last=1;
                for(int i=0;i<4;i++){
                    for(int j=0;j<socketSentLimit;j++){
                        Thread.sleep(sleep);
                        if(last==1){
                            socket1.send(createPacket(addresses[0],activity.port,40+sleep));
                            activity.sentCount++;
                            sleep=rand.nextInt(250);
                        }
                        else{
                            socket2.send(createPacket(addresses[0],activity.port,40+sleep));
                            activity.sentCount++;
                            sleep=rand.nextInt(250);
                        }
                    }
                    last=3-last;
                }
                socketSentLimit=17;
                while (true) {
                    Thread.sleep(sleep);
                    if(!activity.running)continue;
                    socket1.send(createPacket(addresses[0],activity.port,40+sleep));
                    activity.sentCount++;
                    if(socketSentLimit==0){
                        socketSentLimit=17;
                        sleep=rand.nextInt(250);
                        socket2.send(createPacket(addresses[0],activity.port,40+sleep));
                        activity.sentCount++;
                    }
                    sleep=rand.nextInt(250);
                    socketSentLimit--;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private class quicImplementation implements Runnable{
        DatagramSocket socket;
        quicImplementation(DatagramSocket s){
            socket=s;
        }

        @Override
        public void run() {
            while (!activity.running);
            Random rand=new Random();
            int sleep=rand.nextInt(250);
            try {
                socket.send(getClientHello(addresses[1],443));
                activity.sentCount++;
                for (int i = 0; i < 30; i++) {
                    Thread.sleep(50);
                    socket.send(createQuicPacket(addresses[1],443,100+sleep));
                    activity.sentCount++;
                    sleep=rand.nextInt(250);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {

                while (true) {
                    Thread.sleep(100);
                    if(!activity.running)continue;
                    socket.send(createQuicPacket(addresses[1],443,100+sleep));
                    activity.sentCount++;
                    sleep=rand.nextInt(250);

                    Thread.sleep(20);
                    socket.send(createQuicPacket(addresses[1],443,100+sleep));
                    activity.sentCount++;
                    sleep=rand.nextInt(250);

                    Thread.sleep(9900);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class udpReceiver implements Runnable{
        DatagramSocket socket;
        udpReceiver(DatagramSocket s){
            socket=s;
        }
        @Override
        public void run() {
            DatagramPacket packet=new DatagramPacket(new byte[2000],2000);
            try {
                while (true) {
                    socket.receive(packet);
                    activity.receivedCount++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public DatagramPacket createQuicPacket(InetAddress address, int port, int len){
        byte [] data,header;
        header=Functions.hexStringToByteArray("0cff6097891500f22800");
        count=count%256;
        header[header.length-1]=(byte)count;
        data=Functions.getRandomData(len);
        data=Functions.concatenateByteArrays(header,data);
        count++;
        return new DatagramPacket(data,data.length,address,port);
    }
    private DatagramPacket getClientHello(InetAddress address,int port){
        byte [] data=Functions.hexStringToByteArray("0dff6097891500f2285130333701367870a2ff0d990f75393366a001000443484c4f1c0000005041440021010000534e49002e01000053544b0064010000564552006801000043435300780100004e4f4e43980100004d5350439c01000041454144a001000055414944d001000053434944e001000054434944e401000050444d44e8010000534d484cec0100004943534cf00100004354494df80100004e4f4e501802000050554253380200004d4944533c02000053434c53400200004b45585344020000584c43544c020000435343544c020000434f5054500200004343525468020000495254546c0200004345545610030000434643571403000053464357180300002d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d67672e676f6f676c652e636f6ddf3183bef0edceec558625a035069cba0b63f98af00bc72c13e9a67dc7f1d3a4012545b99a7825de2909a4b54e21cf41af516466853f5130333701e8816092921ae87eed8086a215829159ba20f3303030303030303054e88212cfaa278ae7b289d821c7801a0260959c64000000414553474368726f6d652f36302e302e333131322e3131332057696e646f7773204e542031302e303b2057696e36343b20783634fa8d97332599e015f5b239f93492e7160000000058353039010000001e000000f320ba590000000080ec26d4789c3659b01306585cdafe9b4c4e14969a98331c63d192b8e931818b8650e2defc8f491d0647caa598e8177389403b6ef4b8348728544be9d05b7d65640000000100000043323535ef1eaa4f97e1cb2a3552544fef1eaa4f97e1cb2a9a91ed6f1c00810e400b7b90a9ae79eb17100100be313c6df965373342a3bc464bbab6e77fe1fa72ad1faadac8ffa5745661719331fd52ac7505933c5e19ff2634598d14e515bdcd22b1bc73dc954ca52f82b8358c0fcfc82d12d3a162b34213fee403205197218a0309aa9c2da02855445ece9766fce18403886192d8e8db0feefe30b5e1e084c8a1ce03f07d8dce561b5145ff9d37e6be29dbf1ec3be8d30cccd4823883a4ac383cf523243a002cbd7981f0968bf46c9f0000f000000060000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        return new DatagramPacket(data,data.length,address,port);
    }
    public static DatagramPacket createPacket(InetAddress address,int port, int len){
        byte [] data;
        data=Functions.getRandomData(len);
        return new DatagramPacket(data,data.length,address,port);
    }
}
