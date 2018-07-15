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
            os.write(Functions.hexStringToByteArray("6130303030204341504142494c4954590d0a"));
            activity.sentCount++;
            n = Functions.readByte(is,data,130);
            if(n>0)activity.receivedCount++;
            os.write(Functions.hexStringToByteArray("6130303031204c4f47494e20226e65756c696e6765726e2220225858585858220d0a4f"));
            activity.sentCount++;
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
            os.write(Functions.hexStringToByteArray("0000000008fe2000"));
            activity.sentCount++;
            for(int i=0;i<3;i++)
                if(Functions.readByte(is,data,34)>0)activity.receivedCount++;

            for(int i=0;i<4;i++){
                os.write(Functions.hexStringToByteArray("323270e008053800"));
                activity.sentCount++;
                if(Functions.readByte(is,data,9)>0)activity.receivedCount++;
            }

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
                os.write(Functions.hexStringToByteArray("000000beff534d4272000000000801c80000000000000000000000000000c61c00000100009b00025043204e4554574f524b2050524f4752414d20312e3000024d4943524f534f4654204e4554574f524b5320312e303300024d4943524f534f4654204e4554574f524b5320332e3000024c414e4d414e312e3000024c4d312e32583030320002444f53204c414e4d414e322e3100024c414e4d414e322e31000253616d626100024e54204c414e4d414e20312e3000024e54204c4d20302e313200"));
                activity.sentCount++;
                if(Functions.readByte(is,data,131)>0)activity.receivedCount++;
            }

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


            os.write(Functions.hexStringToByteArray("8100004420464545494550454e46444550454f43414341434143414341434143414341434100204545455045444550454e455045454542454c454643414341434143414341414100"));
            activity.sentCount++;
            if(Functions.readByte(is,data,4)>0)activity.receivedCount++;

            os.write(Functions.hexStringToByteArray("000000beff534d4272000000000801c800000000000000000000000000004a1b00000200009b00025043204e4554574f524b2050524f4752414d20312e3000024d4943524f534f4654204e4554574f524b5320312e303300024d4943524f534f4654204e4554574f524b5320332e3000024c414e4d414e312e3000024c4d312e32583030320002444f53204c414e4d414e322e3100024c414e4d414e322e31000253616d626100024e54204c414e4d414e20312e3000024e54204c4d20302e313200"));
            activity.sentCount++;
            if(Functions.readByte(is,data,84)>0)activity.receivedCount++;

            os.write(Functions.hexStringToByteArray("0000005eff534d42730000000008014800000000000000000000000000004a1b000003000dff000000ffff02004a1ba2080000010000000000000050000000210000766d6c656d6f6e00484f4d4500556e69780053616d626120332e302e32356200"));
            activity.sentCount++;
            if(Functions.readByte(is,data,66)>0)activity.receivedCount++;

            os.write(Functions.hexStringToByteArray("00000041ff534d42750000000008014800000000000000000000000000004a1b0000040004ff000000080001001600005c5c54484f4d534f4e5c49504324003f3f3f3f3f00"));
            activity.sentCount++;
            if(Functions.readByte(is,data,53)>0)activity.receivedCount++;

            os.write(Functions.hexStringToByteArray("0000005bff534d42a20000000008014800000000000000000000000001004a1b0000050018ff00000000070000000000000000009f010200000000000000000000000000030000000100000000000000020000000008005c73727673766300"));
            activity.sentCount++;
            if(Functions.readByte(is,data,107)>0)activity.receivedCount++;

            os.write(Functions.hexStringToByteArray("00000092ff534d42250000000008014800000000000000000000000001004a1b0000060010000048000000b8100000000000000000000000004a0048004a000200260054704f005c504950455c0005000b03100000004800000001000000b810b810000000000100000000000100c84f324b7016d30112785a47bf6ee18803000000045d888aeb1cc9119fe808002b10486002000000"));
            activity.sentCount++;
            if(Functions.readByte(is,data,128)>0)activity.receivedCount++;

            os.write(Functions.hexStringToByteArray("000000a2ff534d42250000000008014800000000000000000000000001004a1b000007001000005800000030160000000000000000000000004a0058004a000200260054705f005c504950455c00050000031000000058000000020000004000000000000f00010000000a000000000000000a0000005c005c00540048004f004d0053004f004e0000000100000001000000010000000000000000000000ffffffff00000000"));
            activity.sentCount++;
            if(Functions.readByte(is,data,332)>0)activity.receivedCount++;

            os.write(Functions.hexStringToByteArray("00000029ff534d42040000000008014800000000000000000000000001004a1b00000800035470ffffffff0000"));
            activity.sentCount++;
            if(Functions.readByte(is,data,39)>0)activity.receivedCount++;

            os.write(Functions.hexStringToByteArray("00000023ff534d42710000000008014800000000000000000000000001004a1b00000900000000"));
            activity.sentCount++;
            if(Functions.readByte(is,data,39)>0)activity.receivedCount++;



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


            os.write(Functions.hexStringToByteArray("49534f4e205468756e666973636820536d696c657920536d696c6579470a"));
            activity.sentCount++;
            if(Functions.readByte(is,data,46)>0)activity.receivedCount++;
            if(Functions.readByte(is,data,69)>0)activity.receivedCount++;
            if(Functions.readByte(is,data,98)>0)activity.receivedCount++;


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


            os.write(Functions.hexStringToByteArray("504f5354202f6970702f20485454502f312e310d0a436f6e74656e742d4c656e6774683a203232380d0a436f6e74656e742d547970653a206170706c69636174696f6e2f6970700d0a486f73743a2031302e31302e31302e3235310d0a557365722d4167656e743a20435550532f312e332e340d0a4578706563743a203130302d636f6e74696e75650d0a0d0a010100090000000101470012617474726962757465732d6368617273657400057574662d3848001b617474726962757465732d6e61747572616c2d6c616e67756167650005656e2d757345000b7072696e7465722d757269001b6970703a2f2f31302e31302e31302e3235313a3633312f6970702f2100066a6f622d696400040000001042001472657175657374696e672d757365722d6e616d65000567756573744400147265717565737465642d61747472696275746573001a6a6f622d6d656469612d7368656574732d636f6d706c6574656444000000096a6f622d737461746503"));
            activity.sentCount++;
            if(Functions.readByte(is,data,25)>0)activity.receivedCount++;
            if(Functions.readByte(is,data,255)>0)activity.receivedCount++;

            os.write(Functions.hexStringToByteArray("504f5354202f6970702f20485454502f312e310d0a436f6e74656e742d4c656e6774683a203232300d0a436f6e74656e742d547970653a206170706c69636174696f6e2f6970700d0a486f73743a2031302e31302e31302e3235310d0a557365722d4167656e743a20435550532f312e332e340d0a4578706563743a203130302d636f6e74696e75650d0a0d0a0101000b0000000101470012617474726962757465732d6368617273657400057574662d3848001b617474726962757465732d6e61747572616c2d6c616e67756167650005656e2d757345000b7072696e7465722d757269001b6970703a2f2f31302e31302e31302e3235313a3633312f6970702f42001472657175657374696e672d757365722d6e616d65000567756573744400147265717565737465642d6174747269627574657300157072696e7465722d73746174652d6d65737361676544000000157072696e7465722d73746174652d726561736f6e7303"));
            activity.sentCount++;
            if(Functions.readByte(is,data,25)>0)activity.receivedCount++;
            if(Functions.readByte(is,data,267)>0)activity.receivedCount++;

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


            os.write(Functions.hexStringToByteArray("1300000064ffff182000000600395032303030"));
            activity.sentCount++;
            if(Functions.readByte(is,data,19)>0)activity.receivedCount++;

            os.write(Functions.hexStringToByteArray("17000000660d004a01000008006e6f73656c6173640000"));
            activity.sentCount++;
            if(Functions.readByte(is,data,20)>0)activity.receivedCount++;

            os.write(Functions.hexStringToByteArray("17000000740d004a010000000000000000000000080000"));
            activity.sentCount++;
            if(Functions.readByte(is,data,45)>0)activity.receivedCount++;

            os.write(Functions.hexStringToByteArray("39000000760d004a0100002200000000000000220000007039736b31206f7574736964652e706c616e392e62656c6c2d6c6162732e636f6d00"));
            activity.sentCount++;
            if(Functions.readByte(is,data,11)>0)activity.receivedCount++;

            os.write(Functions.hexStringToByteArray("1b000000680d00fd0000004a01000008006e6f73656c6173640000"));
            activity.sentCount++;
            if(Functions.readByte(is,data,20)>0)activity.receivedCount++;

            os.write(Functions.hexStringToByteArray("0b000000780d004a010000"));
            activity.sentCount++;
            if(Functions.readByte(is,data,7)>0)activity.receivedCount++;

            os.write(Functions.hexStringToByteArray("200000006e0d00fd0000003501000002000700636f6e74726962040074657374"));
            activity.sentCount++;
            if(Functions.readByte(is,data,22)>0)activity.receivedCount++;
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


            os.write(Functions.hexStringToByteArray("405253594e43443a2032390a"));
            activity.sentCount++;
            if(Functions.readByte(is,data,12)>0)activity.receivedCount++;

            os.write(Functions.hexStringToByteArray("67656e746f6f2d706f72746167650a"));
            activity.sentCount++;
            if(Functions.readByte(is,data,400)>0)activity.receivedCount++;

            os.write(Functions.hexStringToByteArray("2d2d7365727665720a"));
            activity.sentCount++;
            if(Functions.readByte(is,data,45)>0)activity.receivedCount++;
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


            os.write(Functions.hexStringToByteArray("880c002c310a6401aabbcc007c0000000000000000000000000000000100000100000000050200080001000000040000"));
            activity.sentCount++;
            if(Functions.readByte(is,data,48)>0)activity.receivedCount++;
            if(Functions.readByte(is,data,48)>0)activity.receivedCount++;

            os.write(Functions.hexStringToByteArray("880c002c310a6402aabbcc007c00aabbcc007a0000000000000000000100000100000001050200080001000000040000880c002c310a6403aabbcc007c00aabbcc007a0000000000000000000100000100000001050200080001000000040000"));
            activity.sentCount++;
            if(Functions.readByte(is,data,48)>0)activity.receivedCount++;
            if(Functions.readByte(is,data,80)>0)activity.receivedCount++;

            os.write(Functions.hexStringToByteArray("880c00343120450000000000800100340000000000000000000000000000090000000000002005000002001c000100057465737431000000"));
            activity.sentCount++;
            if(Functions.readByte(is,data,48)>0)activity.receivedCount++;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
