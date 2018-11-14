package com.example.sodrulaminshaon.wifitestapp1.udp;

import java.util.Random;

/**
 * Created by Sodrul Amin Shaon on 03-Sep-18.
 */

public class AFSSimulation {
    static long CID = new Random().nextLong();
    public static byte [] getNextPacket(byte [] data,int len){
        byte [] returnData = new byte[len+4];
        int index = 0;
        for(int i=0;i<4;i++)
            returnData[index++] = data[i];
        CID++;
        returnData[index++] = (byte)((CID>>24) & 0x00ff);
        returnData[index++] = (byte)((CID>>16) & 0x00ff);
        returnData[index++] = (byte)((CID>>8) & 0x00ff);
        returnData[index++] = (byte)((CID) & 0x00ff);

        for(int i=4;i<len;i++){
            returnData[index++] = data[i];
        }
        return returnData;
    }
}
