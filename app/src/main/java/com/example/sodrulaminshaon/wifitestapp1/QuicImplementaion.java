package com.example.sodrulaminshaon.wifitestapp1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sodrul Amin Shaon on 9/10/2017.
 */

public class QuicImplementaion implements Runnable {
    MainActivity activity;
    long packetNumber;
    InetAddress [] addresses;
    int serverPort;
    static byte [][] versions;
    ArrayList<String> versionString;
    static  int versionsArraySize = 0;
    private int flags = 0b00001101;
    private byte [] CID;
    int versionIndex;
    private static byte [] clientHelloData;
    private static byte [] authHash;
    private static byte [] fullClientHello;
    private Random random;
    QuicImplementaion(MainActivity a){
        activity=a;
        packetNumber=0;
        serverPort=443;
        random = new Random();
        init();
    }
    QuicImplementaion(MainActivity a,InetAddress [] ad){
        activity=a;
        //packetNumber=0;
        addresses=ad;
        serverPort=443;
        random = new Random();
        /*CID = Functions.getRandomData(8);
        rand = new Random().nextInt(versionsArraySize);*/
        init();
    }
    public void init(){
        packetNumber=0;
        CID = Functions.getRandomData(8);
        versionIndex = random.nextInt(versionsArraySize);
        flags = 0;
        int a = random.nextInt(2);
        flags = flags | a;
        a = random.nextInt(2);
        a = a << 3;
        flags = flags | a;
        a = random.nextInt(4);
        a = a << 4;
        flags = flags | a;
    }
    static {

        clientHelloData = Functions.hexStringToByteArray("a001000443484c4f1c0000005041440021010000534e49002e01000053544b0064010000564552006801000043435300780100004e4f4e43980100004d5350439" +
                "c01000041454144a001000055414944d001000053434944e001000054434944e401000050444d44e8010000534d484cec0100004943534cf00100004354494df80100004e4f4e501802000050554253380200004d4944" +
                "533c02000053434c53400200004b45585344020000584c43544c020000435343544c020000434f5054500200004343525468020000495254546c0200004345545610030000434643571403000053464357180300002d2" +
                "d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d" +
                "2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2" +
                "d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d" +
                "2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d67672e676f6f676c652e636f6ddf3183bef0edceec558625a035069cba0b63f98af00bc72c13e9a67dc7f1d3a4012545b99a7825de2909a4b54e2" +
                "1cf41af516466853f5130333701e8816092921ae87eed8086a215829159ba20f3303030303030303054e88212cfaa278ae7b289d821c7801a0260959c64000000414553474368726f6d652f36302e302e333131322e31" +
                "31332057696e646f7773204e542031302e303b2057696e36343b20783634fa8d97332599e015f5b239f93492e7160000000058353039010000001e000000f320ba590000000080ec26d4789c3659b01306585cdafe9b4" +
                "c4e14969a98331c63d192b8e931818b8650e2defc8f491d0647caa598e8177389403b6ef4b8348728544be9d05b7d65640000000100000043323535ef1eaa4f97e1cb2a3552544fef1eaa4f97e1cb2a9a91ed6f1c0081" +
                "0e400b7b90a9ae79eb17100100be313c6df965373342a3bc464bbab6e77fe1fa72ad1faadac8ffa5745661719331fd52ac7505933c5e19ff2634598d14e515bdcd22b1bc73dc954ca52f82b8358c0fcfc82d12d3a162b" +
                "34213fee403205197218a0309aa9c2da02855445ece9766fce18403886192d8e8db0feefe30b5e1e084c8a1ce03f07d8dce561b5145ff9d37e6be29dbf1ec3be8d30cccd4823883a4ac383cf523243a002cbd7981f096" +
                "8bf46c9f0000f0000000600000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        fullClientHello = Functions.hexStringToByteArray("0dff6097891500f2285130333701367870a2ff0d990f75393366a001000443484c4f1c0000005041440021010000534e49002e01000053544b006401000056455" +
                "2006801000043435300780100004e4f4e43980100004d5350439c01000041454144a001000055414944d001000053434944e001000054434944e401000050444d44e8010000534d484cec0100004943534cf001000043" +
                "54494df80100004e4f4e501802000050554253380200004d4944533c02000053434c53400200004b45585344020000584c43544c020000435343544c020000434f5054500200004343525468020000495254546c02000" +
                "04345545610030000434643571403000053464357180300002d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d" +
                "2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2" +
                "d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d" +
                "2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d67672e676f6f676c652e636f6ddf3183bef0edceec558625a035069cba0b63f98" +
                "af00bc72c13e9a67dc7f1d3a4012545b99a7825de2909a4b54e21cf41af516466853f5130333701e8816092921ae87eed8086a215829159ba20f3303030303030303054e88212cfaa278ae7b289d821c7801a0260959c" +
                "64000000414553474368726f6d652f36302e302e333131322e3131332057696e646f7773204e542031302e303b2057696e36343b20783634fa8d97332599e015f5b239f93492e7160000000058353039010000001e000" +
                "000f320ba590000000080ec26d4789c3659b01306585cdafe9b4c4e14969a98331c63d192b8e931818b8650e2defc8f491d0647caa598e8177389403b6ef4b8348728544be9d05b7d65640000000100000043323535ef" +
                "1eaa4f97e1cb2a3552544fef1eaa4f97e1cb2a9a91ed6f1c00810e400b7b90a9ae79eb17100100be313c6df965373342a3bc464bbab6e77fe1fa72ad1faadac8ffa5745661719331fd52ac7505933c5e19ff2634598d1" +
                "4e515bdcd22b1bc73dc954ca52f82b8358c0fcfc82d12d3a162b34213fee403205197218a0309aa9c2da02855445ece9766fce18403886192d8e8db0feefe30b5e1e084c8a1ce03f07d8dce561b5145ff9d37e6be29db" +
                "f1ec3be8d30cccd4823883a4ac383cf523243a002cbd7981f0968bf46c9f0000f000000060000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");

        authHash = Functions.hexStringToByteArray("367870a2ff0d990f75393366");

        versions = new byte[49][4];

        //versions[versionsArraySize++] = new byte[]{0x50, 0x43, 0x51, 0x30};
        versions[versionsArraySize++] = new byte[]{0x51,0x30,0x30,0x31};
        for(int i=0;i<8;i++){
            System.arraycopy(versions[versionsArraySize-1],0,versions[versionsArraySize],0,4);
            versions[versionsArraySize][3]++;
            versionsArraySize++;
        }

        versions[versionsArraySize++] = new byte[]{0x51,0x30,0x31,0x30};
        for(int i=0;i<9;i++){
            System.arraycopy(versions[versionsArraySize-1],0,versions[versionsArraySize],0,4);
            versions[versionsArraySize][3]++;
            versionsArraySize++;
        }

        versions[versionsArraySize++] = new byte[]{0x51,0x30,0x32,0x30};
        for(int i=0;i<9;i++){
            System.arraycopy(versions[versionsArraySize-1],0,versions[versionsArraySize],0,4);
            versions[versionsArraySize][3]++;
            versionsArraySize++;
        }
//        0x50435130
//        0x5130303[1-9]
//        0x5130313[0-9]
//        0x5130323[0-9]
//        0x5130333[0-9]
        versions[versionsArraySize++] = new byte[]{0x51,0x30,0x33,0x30};
        for(int i=0;i<9;i++){
            System.arraycopy(versions[versionsArraySize-1],0,versions[versionsArraySize],0,4);
            versions[versionsArraySize][3]++;
            versionsArraySize++;
        }
//        0x5130343[0-9]
        versions[versionsArraySize++] = new byte[]{0x51,0x30,0x34,0x30};
        for(int i=0;i<9;i++){
            System.arraycopy(versions[versionsArraySize-1],0,versions[versionsArraySize],0,4);
            versions[versionsArraySize][3]++;
            versionsArraySize++;
        }
//        0x51474f[0-255]
        /*versions[versionsArraySize++] = new byte[]{0x51,0x47,0x4f,0x00};
        for(int i=0;i<255;i++){
            System.arraycopy(versions[versionsArraySize-1],0,versions[versionsArraySize],0,4);
            versions[versionsArraySize][3]++;
            versionsArraySize++;
        }*/
//        0xabcd000[0-f]
        /*versions[versionsArraySize++] = new byte[]{(byte) 0xab, (byte) 0xcd,0x00,0x00};
        for(int i=0;i<15;i++){
            System.arraycopy(versions[versionsArraySize-1],0,versions[versionsArraySize],0,4);
            versions[versionsArraySize][3]++;
            versionsArraySize++;
        }*/
//        0xf123f0c[0-f]
        /*versions[versionsArraySize++] = new byte[]{(byte) 0xf1,0x23, (byte) 0xf0, (byte) 0xc0};
        for(int i=0;i<15;i++){
            System.arraycopy(versions[versionsArraySize-1],0,versions[versionsArraySize],0,4);
            versions[versionsArraySize][3]++;
            versionsArraySize++;
        }*/
//        0xfaceb00[0-f]
        /*versions[versionsArraySize++] = new byte[]{(byte) 0xfa, (byte) 0xce, (byte) 0xb0,0x00};
        for(int i=0;i<15;i++){
            System.arraycopy(versions[versionsArraySize-1],0,versions[versionsArraySize],0,4);
            versions[versionsArraySize][3]++;
            versionsArraySize++;
        }*/
//        0xff000001
//        0xff000002
//        0xff000003
//        0xff000004
//        0xff000005
//        0xff000006
//        0xff000007
//        0xff000008
//        0xff000009
//        0xff00000a
//        0xff00000b
        /*versions[versionsArraySize++] = new byte[]{(byte) 0xff,0x00,0x00,0x01};
        for(int i=0;i<11;i++){
            System.arraycopy(versions[versionsArraySize-1],0,versions[versionsArraySize],0,4);
            versions[versionsArraySize][3]++;
            versionsArraySize++;
        }*/
//        0xf0f0f0f[0-f]
        /*versions[versionsArraySize++] = new byte[]{(byte) 0xf0, (byte) 0xf0, (byte) 0xf0, (byte) 0xf0};
        for(int i=0;i<15;i++){
            System.arraycopy(versions[versionsArraySize-1],0,versions[versionsArraySize],0,4);
            versions[versionsArraySize][3]++;
            versionsArraySize++;
        }*/
        //System.out.println("Total array size: "+versionsArraySize);
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
            byte [] data = Functions.getRandomData(2048);
            DatagramPacket packet = new DatagramPacket(data,data.length);
            packetNumber=0;
            try {
                sleep = getQuicClientHello(data);
                packet.setData(data,0,sleep);
                packet.setAddress(activity.address);
                packet.setPort(serverPort);
                //socket.send(packet);
                socket.send(getClientHello(activity.address,serverPort));
                //packetNumber++;
                activity.sentCount++;
            } catch (IOException e) {
                e.printStackTrace();
            }
            String sendMsg = "Packet received length = "+activity.packetSize +" count -------- "+activity.sentCount;

            while (true){
                try {
                    Thread.sleep(activity.packetSize);
                    if(!activity.running)continue;
                    Functions.getRandomData(data,0,activity.packetSize);
                    /*sendMsg = "Shaon received length = "+activity.packetSize+" count -------- "+activity.sentCount;
                    System.arraycopy(sendMsg.getBytes(),0,data,0,sendMsg.length());*/
                    int len = createQuicPacket(data,activity.packetSize);
                    //socket.send(createPacket(activity.address,serverPort,activity.packetSize));
                    //DatagramPacket packet = new DatagramPacket(data,len,activity.address,serverPort);
                    packet.setData(data,0,len);
                    packet.setAddress(activity.address);
                    packet.setPort(serverPort);
                    socket.send(packet);
                    activity.sentCount++;
                    packetPerSocket++;
                    //if(packetSize>activity.packetSize)break;
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
    /*private class QuicWithMultiSocket implements Runnable{
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

                        new Thread(new UDPReceiver(socket)).start();


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
                    Thread.sleep(activity.packetSize);
                    if(!activity.running)continue;
                    su=sockets.poll();

                    if(su!=null){
                        socket=su.socket;
                        socket.send(createPacket(addresses[activity.sentCount%ip],serverPort,activity.packetSize,su.count));
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

    }*/
    /*class socketUnit{
        public DatagramSocket socket;
        public int count;
        socketUnit(DatagramSocket s){
            count=1;
            socket=s;
        }
    }*/
    public int createQuicPacket(byte [] data,int len){
        byte [] header = new byte[100];
        int index = 0;
        header[index++] = (byte) flags;
        int a;

        //////// adding CID
        a = flags>>3 & 0x01;
        a = a*8;
        for (int i = 0;i<a;i++){
            header[index++] = CID[i];
        }

        //////// adding versions
        a = flags & 0x01;
        a = a*4;
        //int versionIndex = new Random().nextInt(versionsArraySize);

        for(int i=0;i<a;i++){
            header[index++] = versions[versionIndex][i];
        }
        //rand++;
        versionIndex = versionIndex % versionsArraySize;

        //////// adding packet number
        a = flags>>4 & 0x03;
        a = a*2;
        packetNumber++;
        //System.out.println("Packet number len: "+a);
        if(a == 0){
            header[index++] = (byte)packetNumber;
        }else{
            for(int i=0;i<a;i++){
                header[index++] = (byte)((packetNumber >> 8*i) & 0xff);
            }
        }

        /////// adding data
        for(int i=0;i<len;i++)
            data[len+index-1-i] = data[len-i-1];

        /////// adding header
        for(int i=0;i<index;i++)
            data[i] = header[i];

        return index+len;
    }
    /*public DatagramPacket createPacket(InetAddress address, int port, int len){
        byte [] data,header;
        //header=Functions.hexStringToByteArray("0cd368169dd4689cb700");
        packetNumber++;
        packetNumber=packetNumber%256;
        CID[CID.length-1]=(byte)packetNumber;
        CID[0] = (byte) flags;
        data=Functions.getRandomData(len);
        data=Functions.concatenateByteArrays(CID,data);
        return new DatagramPacket(data,data.length,address,port);
    }
    public DatagramPacket createPacket(InetAddress address, int port, int len, int count){
        byte [] data,header;
        //header=Functions.hexStringToByteArray("0cff6097891500f22800");
        count=count%256;
        CID[CID.length-1]=(byte)count;
        CID[0] = (byte) flags;
        data=Functions.getRandomData(len);
        data=Functions.concatenateByteArrays(CID,data);
        return new DatagramPacket(data,data.length,address,port);
    }*/
    private DatagramPacket getClientHello(InetAddress address,int port){
        //byte [] data=Functions.hexStringToByteArray("0dff6097891500f2285130333701367870a2ff0d990f75393366a001000443484c4f1c0000005041440021010000534e49002e01000053544b0064010000564552006801000043435300780100004e4f4e43980100004d5350439c01000041454144a001000055414944d001000053434944e001000054434944e401000050444d44e8010000534d484cec0100004943534cf00100004354494df80100004e4f4e501802000050554253380200004d4944533c02000053434c53400200004b45585344020000584c43544c020000435343544c020000434f5054500200004343525468020000495254546c0200004345545610030000434643571403000053464357180300002d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d67672e676f6f676c652e636f6ddf3183bef0edceec558625a035069cba0b63f98af00bc72c13e9a67dc7f1d3a4012545b99a7825de2909a4b54e21cf41af516466853f5130333701e8816092921ae87eed8086a215829159ba20f3303030303030303054e88212cfaa278ae7b289d821c7801a0260959c64000000414553474368726f6d652f36302e302e333131322e3131332057696e646f7773204e542031302e303b2057696e36343b20783634fa8d97332599e015f5b239f93492e7160000000058353039010000001e000000f320ba590000000080ec26d4789c3659b01306585cdafe9b4c4e14969a98331c63d192b8e931818b8650e2defc8f491d0647caa598e8177389403b6ef4b8348728544be9d05b7d65640000000100000043323535ef1eaa4f97e1cb2a3552544fef1eaa4f97e1cb2a9a91ed6f1c00810e400b7b90a9ae79eb17100100be313c6df965373342a3bc464bbab6e77fe1fa72ad1faadac8ffa5745661719331fd52ac7505933c5e19ff2634598d14e515bdcd22b1bc73dc954ca52f82b8358c0fcfc82d12d3a162b34213fee403205197218a0309aa9c2da02855445ece9766fce18403886192d8e8db0feefe30b5e1e084c8a1ce03f07d8dce561b5145ff9d37e6be29dbf1ec3be8d30cccd4823883a4ac383cf523243a002cbd7981f0968bf46c9f0000f000000060000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        for(int i=0;i<8;i++)
            fullClientHello[i+1] = CID[i];
        for(int i=0;i<4;i++)
            fullClientHello[i+9] = versions[versionIndex][i];

        Functions.getRandomData(fullClientHello,14,12);
        return new DatagramPacket(fullClientHello,fullClientHello.length,address,port);
    }
    private int getQuicClientHello(byte [] data){
        //data=Functions.hexStringToByteArray("0dff6097891500f2285130333701367870a2ff0d990f75393366a001000443484c4f1c0000005041440021010000534e49002e01000053544b0064010000564552006801000043435300780100004e4f4e43980100004d5350439c01000041454144a001000055414944d001000053434944e001000054434944e401000050444d44e8010000534d484cec0100004943534cf00100004354494df80100004e4f4e501802000050554253380200004d4944533c02000053434c53400200004b45585344020000584c43544c020000435343544c020000434f5054500200004343525468020000495254546c0200004345545610030000434643571403000053464357180300002d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d67672e676f6f676c652e636f6ddf3183bef0edceec558625a035069cba0b63f98af00bc72c13e9a67dc7f1d3a4012545b99a7825de2909a4b54e21cf41af516466853f5130333701e8816092921ae87eed8086a215829159ba20f3303030303030303054e88212cfaa278ae7b289d821c7801a0260959c64000000414553474368726f6d652f36302e302e333131322e3131332057696e646f7773204e542031302e303b2057696e36343b20783634fa8d97332599e015f5b239f93492e7160000000058353039010000001e000000f320ba590000000080ec26d4789c3659b01306585cdafe9b4c4e14969a98331c63d192b8e931818b8650e2defc8f491d0647caa598e8177389403b6ef4b8348728544be9d05b7d65640000000100000043323535ef1eaa4f97e1cb2a3552544fef1eaa4f97e1cb2a9a91ed6f1c00810e400b7b90a9ae79eb17100100be313c6df965373342a3bc464bbab6e77fe1fa72ad1faadac8ffa5745661719331fd52ac7505933c5e19ff2634598d14e515bdcd22b1bc73dc954ca52f82b8358c0fcfc82d12d3a162b34213fee403205197218a0309aa9c2da02855445ece9766fce18403886192d8e8db0feefe30b5e1e084c8a1ce03f07d8dce561b5145ff9d37e6be29dbf1ec3be8d30cccd4823883a4ac383cf523243a002cbd7981f0968bf46c9f0000f000000060000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        int index = 0;
        data[index++] = (byte) flags;
        int a;

        //////// adding CID
        a = flags>>3 & 0x01;
        a = a*8;
        for (int i = 0;i<a;i++){
            data[index++] = CID[i];
        }

        //////// adding versions
        a = flags & 0x01;
        a = a*4;
        //int versionIndex = new Random().nextInt(versionsArraySize);

        for(int i=0;i<a;i++){
            data[index++] = versions[versionIndex][i];
        }
        //versionIndex++;
        versionIndex = versionIndex % versionsArraySize;

        //////// adding packet number
        a = flags>>4 & 0x03;
        a = a*2;
        packetNumber++;
        //System.out.println("Packet number len: "+a);
        if(a == 0){
            data[index++] = (byte)packetNumber;
        }else{
            for(int i=0;i<a;i++){
                data[index++] = (byte)((packetNumber >> 8*i) & 0xff);
            }
        }

        //////// adding authenticating hash
        //index = Functions.getRandomData(data,index,12);
        System.arraycopy(authHash,0,data,index,authHash.length);
        index+=authHash.length;

        System.arraycopy(clientHelloData,0, data,index,clientHelloData.length);
        index+=clientHelloData.length;

        return index;
    }

    private int receiveQuicPacket(byte [] data,int len){
        int flags = data[0];
        int version = flags & 0x01;
        int cidLen = flags >> 3 & 0x01;
        int packetNumberLen = flags >> 4 & 0x03;
        int dataStartIndex = 1;
        dataStartIndex = dataStartIndex + cidLen*8;
        dataStartIndex = dataStartIndex + version*4;
        dataStartIndex = dataStartIndex + packetNumberLen*2;

        for(int i=dataStartIndex;i<len;i++){
            data[i-dataStartIndex] = data[i];
        }
        return len-dataStartIndex;
    }

}
