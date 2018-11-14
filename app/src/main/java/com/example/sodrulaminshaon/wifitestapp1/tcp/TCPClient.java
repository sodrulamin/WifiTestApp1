package com.example.sodrulaminshaon.wifitestapp1.tcp;

import com.example.sodrulaminshaon.wifitestapp1.Functions;
import com.example.sodrulaminshaon.wifitestapp1.MainActivity;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import dalvik.system.PathClassLoader;

/**
 * Created by Sodrul Amin Shaon on 10-Jul-18.
 */

public class TCPClient extends Thread{
    MainActivity activity;
    public TCPClient(MainActivity a){
        activity = a;
    }
    @Override
    public void run(){
        while(!activity.running);
        int n = activity.headerNumber;
        switch (n){
            case 100:
                IMAPImplementation();
                break;
            case 101:
                TinkerForgeImplementation();
                break;
            case 102:
                SMBImplementation();
                break;
            case 103:
                SRVSVCImplementation();
                break;
            case 104:
                IRCImplementation();
                break;
            case 105:
                IPPImplementation();
                break;
            case 106:
                Plan9Implementation();
                break;
            case 107:
                RSYNCImplementation();
                break;
            case 108:
                ANCPImplementation();
                break;
            case 109:
                CMPIRImplementation();
                break;
            case 110:
                CMPImplementation();
                break;
            case 111:
                COPSImplementation();
                break;
            case 112:
                CouchBaseImplementation();
                break;
            case 113:
                EPMDImplementation();
                break;
            case 114:
                EXECImplementation();
                break;
            case 115:
                XMPPImplementation();
                break;
            case 116:
                FTPImplementation();
                break;
            case 117:
                POPImplementation();
                break;
            case 118:
                GryphonImplementation();
                break;
            case 119:
                HeartIPImplementation();
                break;
            case 120:
                TelnetImplementation();
                break;
            case 121:
                LLRPImplementation();
                break;
            case 122:
                GearmenImplementation();
                break;
            case 123:
                NFSImplementation();
                break;
            case 124:
                LustreImplementation();
                break;
            case 125:
                DCERPCImplementation();
                break;
            case 126:
                MMSImplementation();
                break;
            case 127:
                MSNMSImplementation();
                break;
        }
    }
    private void MSNMSImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            int n;
            //os.write(Functions.hexStringToByteArray("504e470d0a"));
            activity.sentCount++;
            n = Functions.readByte(is,data,8);
            if(n>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("504e470d0a"));
            activity.sentCount++;
            n = Functions.readByte(is,data,8);
            if(n>0)activity.receivedCount++;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void MMSImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            int n;
//            os.write(Functions.hexStringToByteArray("0300001611e00000b00100c0010ac1020001c2020002"));
            activity.sentCount++;
            n = Functions.readByte(is,data,14);
            if(n>0)activity.receivedCount++;

            os.write(Functions.hexStringToByteArray("030000c502f0800dbc0506130100160102140200023302000134020002c1a63181a3a003800101a2819b80020780810400000001820400000002a423300f0201010604520100013004060251013010020103060528ca220201300406025101880206006160305e020101a059605780020780a107060528ca220101a20406022902a303020102a60406022901a703020101be32283006025101020103a027a82580027d00810114820114830104a416800101810305fb00820c036e1d000000000064000198"));
            activity.sentCount++;
            n = Functions.readByte(is,data,158);
            if(n>0)activity.receivedCount++;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void DCERPCImplementation(){
        try{
            byte []  data = new byte[8000];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            int n;
//            os.write(Functions.hexStringToByteArray("0500000310000000e0001000a41e0000a20000000000020000000000906038c81796d3118962006094516eaf00160000000000007e000000fba5a4a5a5a4a5a4a4a5a7a4a5a6a4a5a1a7a5a0a3a4a5a5a5a67cc24ba5a4a5a2a2a5a3a5a5a5a5a8a5b1a5ecc2a6a551aabba54093bba54393bba5a495a6a5a493a6a5a793a6a5a693aea5af93bba5b693a7a4b393a7a44593a6a5449304a7a5a5829ca5a5dbafa5a5b8e5a5a5d38ca5a5868ca5a55a5a5a5ab1b7a5a57e000016a48525a7a48425a7a48725a7a4860a020e00805b0c0001000000000000000000000000000000"));
            activity.sentCount++;
            n = Functions.readByte(is,data,240);
            if(n>0)activity.receivedCount++;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void LustreImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            int n;
//            os.write(Functions.hexStringToByteArray("c100000000000000000000000000000000000000000000000c38a8c0000002001f38a8c00000020039300000393000000100000008020000ffffffffffffffffffffffffffffffff7077a952bea3050000000000000000001a000000000000000500000000000003d30bd00b2002000000000000000000000000000000000000b8000000270000002700000008000000c00000000000000000000000000000006712000003000100fa00000087040000000000000000000000000000000000000000000000000000000000000000000000000000200000000100000005000000040000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000004d47530000000000000000000000000000000000000000000000000000000000000000000000000032366165666539382d626337332d643636342d643363332d37363636613634393063333200000000b6d0fbcf28370ff4202000051041002000010a02000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"));
            activity.sentCount++;
            n = Functions.readByte(is,data,480);
            if(n>0)activity.receivedCount++;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void NFSImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            int n;
//            os.write(Functions.hexStringToByteArray("80000058440646ee0000000000000002000186a30000000300000004000000010000002000443447000000086965656c2d6330320000000000000000000000010000000000000000000000000000000801000100000000000000001f"));
            activity.sentCount++;
            n = Functions.readByte(is,data,124);
            if(n>0)activity.receivedCount++;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void GearmenImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            int n;
//            os.write(Functions.hexStringToByteArray("043f000000200000000000f600160080000c01c81d8fda5017b0010000060000"));
            activity.sentCount++;
            n = Functions.readByte(is,data,20);
            if(n>0)activity.receivedCount++;

            n = Functions.readByte(is,data,62);
            if(n>0)activity.receivedCount++;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void LLRPImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            int n;
//            os.write(Functions.hexStringToByteArray("043f000000200000000000f600160080000c01c81d8e6f706ea0010000060000"));
            activity.sentCount++;
            n = Functions.readByte(is,data,20);
            if(n>0)activity.receivedCount++;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void TelnetImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            int n;
//            os.write(Functions.hexStringToByteArray("000d12a0000004000000050233ffef"));
            activity.sentCount++;
            n = Functions.readByte(is,data,659);
            if(n>0)activity.receivedCount++;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void HeartIPImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            int n;
//            os.write(Functions.hexStringToByteArray("010000000002000d0100007530"));
            activity.sentCount++;
            n = Functions.readByte(is,data,13);
            if(n>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("010003000003000d0200000002"));
            activity.sentCount++;
            n = Functions.readByte(is,data,37);
            if(n>0)activity.receivedCount++;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void GryphonImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            int n;
//            os.write(Functions.hexStringToByteArray("030002000034010050000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"));
            activity.sentCount++;
            n = Functions.readByte(is,data,20);
            if(n>0)activity.receivedCount++;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void POPImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            if(Functions.readByte(is,data,52)>0)activity.receivedCount++;
//            os.write(Functions.hexStringToByteArray("5553455220746573740d0a"));
            activity.sentCount++;

            if(Functions.readByte(is,data,25)>0)activity.receivedCount++;
//            os.write(Functions.hexStringToByteArray("50415353200d0a"));
            activity.sentCount++;

            if(Functions.readByte(is,data,52)>0)activity.receivedCount++;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void FTPImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();

            if(Functions.readByte(is,data,41)>0)activity.receivedCount++;


            if(Functions.readByte(is,data,45)>0)activity.receivedCount++;

            if(Functions.readByte(is,data,61)>0)activity.receivedCount++;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void XMPPImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            //os.write(Functions.hexStringToByteArray("3c3f786d6c2076657273696f6e3d27312e302720656e636f64696e673d275554462d3827203f3e"));
            activity.sentCount++;
            //os.write(Functions.hexStringToByteArray("3c73747265616d3a73747265616d20746f3d276c6f63616c686f73742720786d6c6e733d276a61626265723a636c69656e742720786d6c6e733a73747265616d3d27687474703a2f2f6574686572782e6a61626265722e6f72672f73747265616d73273e"));
            activity.sentCount++;
            if(Functions.readByte(is,data,137)>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("3c697120747970653d27676574272069643d277265675f30312720746f3d276c6f63616c686f7374273e0a0909090909093c717565727920786d6c6e733d276a61626265723a69713a7265676973746572273e0a090909090909090a0909090909093c2f71756572793e0a09090909093c2f69713e"));
            activity.sentCount++;
            if(Functions.readByte(is,data,265)>0)activity.receivedCount++;


//            os.write(Functions.hexStringToByteArray("3c697120747970653d27736574272069643d277265675f30312720746f3d276c6f63616c686f7374273e0a0909090909093c717565727920786d6c6e733d276a61626265723a69713a7265676973746572273e0a090909090909093c757365726e616d653e6a757365723c2f757365726e616d653e0a0909090909093c70617373776f72643e6a3c2f70617373776f72643e0a0909090909093c656d61696c3e3c2f656d61696c3e0a0909090909093c6e616d653e3c2f6e616d653e0a0a0909090909093c2f71756572793e0a09090909093c2f69713e"));
            activity.sentCount++;
            if(Functions.readByte(is,data,31)>0)activity.receivedCount++;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void EXECImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            //os.write(Functions.hexStringToByteArray("3338343000"));
            activity.sentCount++;
            //os.write(Functions.hexStringToByteArray("737465666973383600"));
            activity.sentCount++;
            //os.write(Functions.hexStringToByteArray("6d792d70617373776f726400757074696d6500"));
            activity.sentCount++;
            if(Functions.readByte(is,data,1)>0)activity.receivedCount++;
            if(Functions.readByte(is,data,76)>0)activity.receivedCount++;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void EPMDImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            //os.write(Functions.hexStringToByteArray("001278a8264d000005000500056e6f6465320000"));
            activity.sentCount++;
            if(Functions.readByte(is,data,4)>0)activity.receivedCount++;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void CouchBaseImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            //os.write(Functions.hexStringToByteArray("800100030800016e0000001502000000000000000000000002000000000000006170707b22656767223a39387d"));
            activity.sentCount++;
            if(Functions.readByte(is,data,24)>0)activity.receivedCount++;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void COPSImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();


            //os.write(Functions.hexStringToByteArray("1006000000000040001f0b01412050455020666f72206578616d706c6520707572706f736573000000181001000000010000000067fa197f98283b0aefa31042"));
            activity.sentCount++;
            if(Functions.readByte(is,data,40)>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("1006005800000040001e0b01412050455020666f72206578616d706c6520707572706f736573000000181001000000010000000183ada8635fe2ab2fd148f078"));
            activity.sentCount++;
            if(Functions.readByte(is,data,40)>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("1009000000000020001810010000000100000001cac80a20373e1360fe59b0a2"));
            activity.sentCount++;
            if(Functions.readByte(is,data,32)>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("10010058000000a4001901015468697320697320636c69656e742068616e646c65000000000802010008000000600902000c010106062a03040503010027030142016304164c696e757820726f7574657220726f6d756b6f70706142020800420200fa00000c010106062a0304050101001903014202014106062a03040502010404112233444201420000000018100100000001000000030e9982ab6fa9640d44ed6a23"));
            activity.sentCount++;
            if(Functions.readByte(is,data,216)>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("100900000000002000181001000000010000000208db0b2a3fef5ba6b4654ad5"));
            activity.sentCount++;
            if(Functions.readByte(is,data,32)>0)activity.receivedCount++;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void CMPImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();


            //os.write(Functions.hexStringToByteArray("0000011800308201133081d9020101a4023000a4023000a011180f32303035313032353039353933385aa132303006092a864886f67d07420d30230404481daa53300906052b0e03021a050002020400300c06082b060105050801020500a20a04083739373238383238a416041431ea802725771ec089aad47138372243b579cb51a516041436cd84c431c86bf28e581412ca3cea7185ba41fea72930270c13456e74727573742056657273696f6e20352e300c10456e7472757374205573657220352e30a81e301c301a06096086480186fa6b3208300d040530030201340c0445455350b51c301a300b06096086480186fa6b3203300b06096086480186fa6b320ca01703150105d7f04d68661397e5890ecc4c12939e9acff71e"));
            activity.sentCount++;
            if(Functions.readByte(is,data,2000)>0)activity.receivedCount++;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void CMPIRImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();


            //os.write(Functions.hexStringToByteArray("504f535420687474703a2f2f3132372e302e302e313a383038302f20485454502f312e310d0a486f73743a203132372e302e302e313a383038300d0a436f6e74656e742d747970653a206170706c69636174696f6e2f706b6978636d700d0a436f6e74656e742d4c656e6774683a203537380d0a436f6e6e656374696f6e3a204b6565702d416c6976650d0a43616368652d436f6e74726f6c3a206e6f2d63616368650d0a0d0a3082023e3081d5020102a4023000a4463044310b3009060355040613024445310c300a060355040a13034e534e3111300f060355040b13085047205244452033311430120603550403130b4d617274696e2773204341a011180f32303130303730353037333533385aa13c303a06092a864886f67d07420d302d04109e44a520f7cca8ef9b31c42b9f9aa000300906052b0e03021a0500020201f4300a06082b06010505080102a20b04096ff2863b420b349a80a4120410918e2851bff5cae4c14fb3fc8e4809aea51204109e76ddec4076423442c0e1edf5059d04a082014930820145308201413081a80201003081a2a6819f300d06092a864886f70d010101050003818d0030818902818100a954ceb631046042e4a26737b0c3b24e8a396bd61e0bdd48327e76715c7b76863801e3ba27d7bca7e0ad58d6f3545f1a4601d4861df59befd7dcc91039a6398bc9c5df7bb10310cb3035edb478ecaab17fb6c818bae1d2b3f667cf83c0d642e4761284f432e21a50856891f9418e47ce5e9b712dc0a6e5829f2d11473200d7f70203010001a18193300d06092a864886f70d0101050500038181002b3a576383be5f0f1ec1685a74a009272c3444bc1d4ac84f8157de91fa507eb51461c2fdddc9cfc64ef470e06b5a7838d60a334f6830a5f59bef5658da47668c66f85a415f8352608c8c548fdc33e21aeb33040e91d9acd734ab67a49dd21b1daf934f88bf4e6d5cb85d9ea40966ce5add2dd1df4927e15c3ee16426de46f47ca017031500f4af2efbb86fe5183a1839fa6a83f16d4a3a7e95"));
            activity.sentCount++;
            if(Functions.readByte(is,data,1098)>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("308201103081d5020102a4023000a4463044310b3009060355040613024445310c300a060355040a13034e534e3111300f060355040b13085047205244452033311430120603550403130b4d617274696e2773204341a011180f32303130303730353037333533385aa13c303a06092a864886f67d07420d302d04109e44a520f7cca8ef9b31c42b9f9aa000300906052b0e03021a0500020201f4300a06082b06010505080102a20b04096ff2863b420b349a80a4120410918e2851bff5cae4c14fb3fc8e4809aea5120410b9ca139743d727556e7f60964effb071b81d301b301904140aeb8a138e0221e54e235b0a89aca6236ddea2b4020100a0170315002869c4e0956fc5935ae11a68c6b989ecae099ba7"));
            activity.sentCount++;
            if(Functions.readByte(is,data,427)>0)activity.receivedCount++;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void ANCPImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();


            //os.write(Functions.hexStringToByteArray("880c002c310a6401aabbcc007c0000000000000000000000000000000100000100000000050200080001000000040000"));
            activity.sentCount++;
            if(Functions.readByte(is,data,48)>0)activity.receivedCount++;
            if(Functions.readByte(is,data,48)>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("880c002c310a6402aabbcc007c00aabbcc007a0000000000000000000100000100000001050200080001000000040000880c002c310a6403aabbcc007c00aabbcc007a0000000000000000000100000100000001050200080001000000040000"));
            activity.sentCount++;
            if(Functions.readByte(is,data,48)>0)activity.receivedCount++;
            if(Functions.readByte(is,data,80)>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("880c00343120450000000000800100340000000000000000000000000000090000000000002005000002001c000100057465737431000000"));
            activity.sentCount++;
            if(Functions.readByte(is,data,48)>0)activity.receivedCount++;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void RSYNCImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();


            //os.write(Functions.hexStringToByteArray("405253594e43443a2032390a"));
            activity.sentCount++;
            if(Functions.readByte(is,data,12)>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("67656e746f6f2d706f72746167650a"));
            activity.sentCount++;
            if(Functions.readByte(is,data,400)>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("2d2d7365727665720a"));
            activity.sentCount++;
            if(Functions.readByte(is,data,45)>0)activity.receivedCount++;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void Plan9Implementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();


            //os.write(Functions.hexStringToByteArray("1300000064ffff182000000600395032303030"));
            activity.sentCount++;
            if(Functions.readByte(is,data,19)>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("17000000660d004a01000008006e6f73656c6173640000"));
            activity.sentCount++;
            if(Functions.readByte(is,data,20)>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("17000000740d004a010000000000000000000000080000"));
            activity.sentCount++;
            if(Functions.readByte(is,data,45)>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("39000000760d004a0100002200000000000000220000007039736b31206f7574736964652e706c616e392e62656c6c2d6c6162732e636f6d00"));
            activity.sentCount++;
            if(Functions.readByte(is,data,11)>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("1b000000680d00fd0000004a01000008006e6f73656c6173640000"));
            activity.sentCount++;
            if(Functions.readByte(is,data,20)>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("0b000000780d004a010000"));
            activity.sentCount++;
            if(Functions.readByte(is,data,7)>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("200000006e0d00fd0000003501000002000700636f6e74726962040074657374"));
            activity.sentCount++;
            if(Functions.readByte(is,data,22)>0)activity.receivedCount++;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void IPPImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();


            //os.write(Functions.hexStringToByteArray("504f5354202f6970702f20485454502f312e310d0a436f6e74656e742d4c656e6774683a203232380d0a436f6e74656e742d547970653a206170706c69636174696f6e2f6970700d0a486f73743a2031302e31302e31302e3235310d0a557365722d4167656e743a20435550532f312e332e340d0a4578706563743a203130302d636f6e74696e75650d0a0d0a010100090000000101470012617474726962757465732d6368617273657400057574662d3848001b617474726962757465732d6e61747572616c2d6c616e67756167650005656e2d757345000b7072696e7465722d757269001b6970703a2f2f31302e31302e31302e3235313a3633312f6970702f2100066a6f622d696400040000001042001472657175657374696e672d757365722d6e616d65000567756573744400147265717565737465642d61747472696275746573001a6a6f622d6d656469612d7368656574732d636f6d706c6574656444000000096a6f622d737461746503"));
            activity.sentCount++;
            if(Functions.readByte(is,data,25)>0)activity.receivedCount++;
            if(Functions.readByte(is,data,255)>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("504f5354202f6970702f20485454502f312e310d0a436f6e74656e742d4c656e6774683a203232300d0a436f6e74656e742d547970653a206170706c69636174696f6e2f6970700d0a486f73743a2031302e31302e31302e3235310d0a557365722d4167656e743a20435550532f312e332e340d0a4578706563743a203130302d636f6e74696e75650d0a0d0a0101000b0000000101470012617474726962757465732d6368617273657400057574662d3848001b617474726962757465732d6e61747572616c2d6c616e67756167650005656e2d757345000b7072696e7465722d757269001b6970703a2f2f31302e31302e31302e3235313a3633312f6970702f42001472657175657374696e672d757365722d6e616d65000567756573744400147265717565737465642d6174747269627574657300157072696e7465722d73746174652d6d65737361676544000000157072696e7465722d73746174652d726561736f6e7303"));
            activity.sentCount++;
            if(Functions.readByte(is,data,25)>0)activity.receivedCount++;
            if(Functions.readByte(is,data,267)>0)activity.receivedCount++;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void IRCImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();


            //os.write(Functions.hexStringToByteArray("49534f4e205468756e666973636820536d696c657920536d696c6579470a"));
            activity.sentCount++;
            if(Functions.readByte(is,data,46)>0)activity.receivedCount++;
            if(Functions.readByte(is,data,69)>0)activity.receivedCount++;
            if(Functions.readByte(is,data,98)>0)activity.receivedCount++;


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void SRVSVCImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();


            //os.write(Functions.hexStringToByteArray("8100004420464545494550454e46444550454f43414341434143414341434143414341434100204545455045444550454e455045454542454c454643414341434143414341414100"));
            activity.sentCount++;
            if(Functions.readByte(is,data,4)>0)activity.receivedCount++;

            //os.write(Functions.hexStringToByteArray("000000beff534d4272000000000801c800000000000000000000000000004a1b00000200009b00025043204e4554574f524b2050524f4752414d20312e3000024d4943524f534f4654204e4554574f524b5320312e303300024d4943524f534f4654204e4554574f524b5320332e3000024c414e4d414e312e3000024c4d312e32583030320002444f53204c414e4d414e322e3100024c414e4d414e322e31000253616d626100024e54204c414e4d414e20312e3000024e54204c4d20302e313200"));
            activity.sentCount++;
            if(Functions.readByte(is,data,84)>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("0000005eff534d42730000000008014800000000000000000000000000004a1b000003000dff000000ffff02004a1ba2080000010000000000000050000000210000766d6c656d6f6e00484f4d4500556e69780053616d626120332e302e32356200"));
            activity.sentCount++;
            if(Functions.readByte(is,data,66)>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("00000041ff534d42750000000008014800000000000000000000000000004a1b0000040004ff000000080001001600005c5c54484f4d534f4e5c49504324003f3f3f3f3f00"));
            activity.sentCount++;
            if(Functions.readByte(is,data,53)>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("0000005bff534d42a20000000008014800000000000000000000000001004a1b0000050018ff00000000070000000000000000009f010200000000000000000000000000030000000100000000000000020000000008005c73727673766300"));
            activity.sentCount++;
            if(Functions.readByte(is,data,107)>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("00000092ff534d42250000000008014800000000000000000000000001004a1b0000060010000048000000b8100000000000000000000000004a0048004a000200260054704f005c504950455c0005000b03100000004800000001000000b810b810000000000100000000000100c84f324b7016d30112785a47bf6ee18803000000045d888aeb1cc9119fe808002b10486002000000"));
            activity.sentCount++;
            if(Functions.readByte(is,data,128)>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("000000a2ff534d42250000000008014800000000000000000000000001004a1b000007001000005800000030160000000000000000000000004a0058004a000200260054705f005c504950455c00050000031000000058000000020000004000000000000f00010000000a000000000000000a0000005c005c00540048004f004d0053004f004e0000000100000001000000010000000000000000000000ffffffff00000000"));
            activity.sentCount++;
            if(Functions.readByte(is,data,332)>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("00000029ff534d42040000000008014800000000000000000000000001004a1b00000800035470ffffffff0000"));
            activity.sentCount++;
            if(Functions.readByte(is,data,39)>0)activity.receivedCount++;

//            os.write(Functions.hexStringToByteArray("00000023ff534d42710000000008014800000000000000000000000001004a1b00000900000000"));
            activity.sentCount++;
            if(Functions.readByte(is,data,39)>0)activity.receivedCount++;



        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void SMBImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            for(int i=0;i<4;i++){
                //os.write(Functions.hexStringToByteArray("000000beff534d4272000000000801c80000000000000000000000000000c61c00000100009b00025043204e4554574f524b2050524f4752414d20312e3000024d4943524f534f4654204e4554574f524b5320312e303300024d4943524f534f4654204e4554574f524b5320332e3000024c414e4d414e312e3000024c4d312e32583030320002444f53204c414e4d414e322e3100024c414e4d414e322e31000253616d626100024e54204c414e4d414e20312e3000024e54204c4d20302e313200"));
                activity.sentCount++;
                if(Functions.readByte(is,data,131)>0)activity.receivedCount++;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void TinkerForgeImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            //os.write(Functions.hexStringToByteArray("0000000008fe2000"));
            activity.sentCount++;
            for(int i=0;i<3;i++)
                if(Functions.readByte(is,data,34)>0)activity.receivedCount++;

            for(int i=0;i<4;i++){
//                os.write(Functions.hexStringToByteArray("323270e008053800"));
                activity.sentCount++;
                if(Functions.readByte(is,data,9)>0)activity.receivedCount++;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void IMAPImplementation(){
        try{
            byte []  data = new byte[2048];
            Socket socket = new Socket(activity.address,activity.port);
            socket.setSoTimeout(10000);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            int n = Functions.readByte(is,data,81);
            if(n > 0)activity.receivedCount++;
//            os.write(Functions.hexStringToByteArray("6130303030204341504142494c4954590d0a"));
            activity.sentCount++;
            n = Functions.readByte(is,data,130);
            if(n>0)activity.receivedCount++;
//            os.write(Functions.hexStringToByteArray("6130303031204c4f47494e20226e65756c696e6765726e2220225858585858220d0a4f"));
            activity.sentCount++;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
