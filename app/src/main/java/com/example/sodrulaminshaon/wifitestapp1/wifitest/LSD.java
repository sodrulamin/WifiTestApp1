package com.example.sodrulaminshaon.wifitestapp1.wifitest;

import android.util.Base64;

import java.net.InetAddress;
import java.util.Random;
import static com.example.sodrulaminshaon.wifitestapp1.Functions.*;

/**
 * Created by Sodrul Amin Shaon on 13-Feb-18.
 */

public class LSD {
    private static byte [] header={0x42,0x54,0x2d,0x53,0x45,0x41,0x52,0x43,0x48,0x20,0x2a,0x20,0x48,0x54,0x54,0x50,0x2f,0x31,0x2e,0x31};
    private static String hostStr;
    private static Random random;
    private static String infoHashStr;
    static {
        hostStr=new String(hexStringToByteArray("486f73743a20"));
        random=new Random();
        infoHashStr=new String(hexStringToByteArray("496e666f686173683a20"));
    }
    public static byte [] getLSDPacket(InetAddress address,int port,int len){
        String host=hostStr+address.toString().substring(1)+":"+port;
        String portStr="Port: "+(5000+random.nextInt(4000));
        byte [] infoHash=concatenateByteArrays(infoHashStr.getBytes(), Base64.encode(getRandomData(len),0));
        infoHash=concatenateByteArrays(infoHash,hexStringToByteArray("0d0a0d0a"));
        byte [] data=concatenateByteArrays(header,hexStringToByteArray("0d0a"));
        data=concatenateByteArrays(data,concatenateByteArrays(host.getBytes(),hexStringToByteArray("0d0a")));
        data=concatenateByteArrays(data,concatenateByteArrays(portStr.getBytes(),hexStringToByteArray("0d0a")));
        data=concatenateByteArrays(data,concatenateByteArrays(infoHash,hexStringToByteArray("0d0a")));

        return data;
    }

}
