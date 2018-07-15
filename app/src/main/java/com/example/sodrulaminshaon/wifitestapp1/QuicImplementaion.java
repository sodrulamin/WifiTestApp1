package com.example.sodrulaminshaon.wifitestapp1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Struct;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Sodrul Amin Shaon on 9/10/2017.
 */

public class QuicImplementaion implements Runnable {
    MainActivity activity;
    int packetNumber;
    InetAddress [] addresses;
    int serverPort;
    QuicImplementaion(MainActivity a){
        activity=a;
        packetNumber=0;
        serverPort=443;
    }
    QuicImplementaion(MainActivity a,InetAddress [] ad){
        activity=a;
        packetNumber=0;
        addresses=ad;
        serverPort=443;
    }
    @Override
    public void run() {
        while (!activity.running);
        //new Thread(new QuicWithMultiSocket()).start();
        try {
            int i=0;
            while(true) {
                DatagramSocket socket = new DatagramSocket(2545+i);
                new Thread(new UdpSender(socket)).start();
                new Thread(new UdpReceiver(socket)).start();
                i++;
                break;
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private class UdpSender implements Runnable{
        DatagramSocket socket;
        int packetPerSocket=0;
        UdpSender(DatagramSocket s){
            socket=s;
        }
        @Override
        public void run() {
            while (!activity.running);
            serverPort=activity.port;
            int sleep=200;
            packetNumber=0;
            try {
                socket.send(getClientHello(activity.address,serverPort));
                packetNumber++;
                activity.sentCount++;
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true){
                try {
                    Thread.sleep(sleep);
                    if(!activity.running)continue;
                    socket.send(createPacket(activity.address,serverPort,sleep+1));
                    activity.sentCount++;
                    packetPerSocket++;
                    //if(packetPerSocket>activity.packetPerSocket)break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class UdpReceiver implements Runnable{
        DatagramSocket socket;
        UdpReceiver(DatagramSocket s){
            socket=s;
        }
        @Override
        public void run() {
            DatagramPacket packet=new DatagramPacket(new byte[2000],2000);
            while (true){
                try {
                    socket.receive(packet);
                    activity.receivedCount++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class QuicWithMultiSocket implements Runnable{
        Queue<socketUnit> sockets=new LinkedList<socketUnit>();
        @Override
        public void run() {
            while (!activity.running);
            int ip=3,port=activity.port;
            for(int i=0;i<port;i++){
                for(int j=0;j<ip;j++){
                    try {
                        DatagramSocket socket=new DatagramSocket(4564+i*port+j);
                        socket.send(getClientHello(addresses[j],serverPort));
                        activity.sentCount++;
                        socketUnit su=new socketUnit(socket);
                        su.count++;
                        sockets.add(su);

                        new Thread(new UdpReceiver(socket)).start();


                    } catch (SocketException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            DatagramSocket socket;
            socketUnit su;
            while (true){
                try {
                    Thread.sleep(activity.packetPerSocket);
                    if(!activity.running)continue;
                    su=sockets.poll();

                    if(su!=null){
                        socket=su.socket;
                        socket.send(createPacket(addresses[activity.sentCount%ip],serverPort,activity.packetPerSocket,su.count));
                        activity.sentCount++;
                        su.count++;
                        sockets.add(su);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    class socketUnit{
        public DatagramSocket socket;
        public int count;
        socketUnit(DatagramSocket s){
            count=1;
            socket=s;
        }
    }

    public DatagramPacket createPacket(InetAddress address, int port, int len){
        byte [] data,header;
        header=Functions.hexStringToByteArray("0cd368169dd4689cb700");
        packetNumber++;
        packetNumber=packetNumber%256;
        header[header.length-1]=(byte)packetNumber;
        data=Functions.getRandomData(len);
        data=Functions.concatenateByteArrays(header,data);
        return new DatagramPacket(data,data.length,address,port);
    }
    public DatagramPacket createPacket(InetAddress address, int port, int len, int count){
        byte [] data,header;
        header=Functions.hexStringToByteArray("0cff6097891500f22800");
        count=count%256;
        header[header.length-1]=(byte)count;
        data=Functions.getRandomData(len);
        data=Functions.concatenateByteArrays(header,data);
        return new DatagramPacket(data,data.length,address,port);
    }
    private DatagramPacket getClientHello(InetAddress address,int port){
        byte [] data=Functions.hexStringToByteArray("0dff6097891500f2285130333701367870a2ff0d990f75393366a001000443484c4f1c0000005041440021010000534e49002e01000053544b0064010000564552006801000043435300780100004e4f4e43980100004d5350439c01000041454144a001000055414944d001000053434944e001000054434944e401000050444d44e8010000534d484cec0100004943534cf00100004354494df80100004e4f4e501802000050554253380200004d4944533c02000053434c53400200004b45585344020000584c43544c020000435343544c020000434f5054500200004343525468020000495254546c0200004345545610030000434643571403000053464357180300002d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d67672e676f6f676c652e636f6ddf3183bef0edceec558625a035069cba0b63f98af00bc72c13e9a67dc7f1d3a4012545b99a7825de2909a4b54e21cf41af516466853f5130333701e8816092921ae87eed8086a215829159ba20f3303030303030303054e88212cfaa278ae7b289d821c7801a0260959c64000000414553474368726f6d652f36302e302e333131322e3131332057696e646f7773204e542031302e303b2057696e36343b20783634fa8d97332599e015f5b239f93492e7160000000058353039010000001e000000f320ba590000000080ec26d4789c3659b01306585cdafe9b4c4e14969a98331c63d192b8e931818b8650e2defc8f491d0647caa598e8177389403b6ef4b8348728544be9d05b7d65640000000100000043323535ef1eaa4f97e1cb2a3552544fef1eaa4f97e1cb2a9a91ed6f1c00810e400b7b90a9ae79eb17100100be313c6df965373342a3bc464bbab6e77fe1fa72ad1faadac8ffa5745661719331fd52ac7505933c5e19ff2634598d14e515bdcd22b1bc73dc954ca52f82b8358c0fcfc82d12d3a162b34213fee403205197218a0309aa9c2da02855445ece9766fce18403886192d8e8db0feefe30b5e1e084c8a1ce03f07d8dce561b5145ff9d37e6be29dbf1ec3be8d30cccd4823883a4ac383cf523243a002cbd7981f0968bf46c9f0000f000000060000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        return new DatagramPacket(data,data.length,address,port);
    }

}
