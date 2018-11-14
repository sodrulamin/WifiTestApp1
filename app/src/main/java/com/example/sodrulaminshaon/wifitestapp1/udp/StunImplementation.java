package com.example.sodrulaminshaon.wifitestapp1.udp;

import com.example.sodrulaminshaon.wifitestapp1.Functions;


/**
 * Created by Sodrul Amin Shaon on 19-Sep-18.
 */

public class StunImplementation {
    private static int typeUserName = 6,typePriority = 0x24,typeIceControlling = 0x802a,typeMsImplementationVersion = 0x8070,typeMessageIntegrity = 0x08,typeFingerPrint = 0x8028;
    private static byte [] userName = Functions.getRandomWord(9).getBytes();
    private static byte [] magicCookie = Functions.hexStringToByteArray("2112a442");
    byte [] temparray,receivedData;
    static{
        userName[4] = 0x3a;
    }

    public StunImplementation(){
        temparray = new byte[2048];
        receivedData = new byte[2048];
    }

    public int createStunFromData(byte [] data,int offset,int len){
        if(len < 28) len = 28;
        System.arraycopy(data,offset,temparray,0,len);
        int index = 0,dataAddedIndex = 0,a,trxData;
        data[index++] = 0x00;
        data[index++] = 0x01;
        index+=2;

        /*data[index++] = 0x21;
        data[index++] = 0x12;
        data[index++] = (byte) 0xa4;
        data[index++] = 0x42;*/
        for(int i=0;i<magicCookie.length;i++){
            data[index++] = magicCookie[i];
        }

        trxData = 12;
        System.arraycopy(temparray,dataAddedIndex,data,index,trxData);
        index+=trxData;
        dataAddedIndex+=trxData;
        index = Functions.appendAttribute(data,index,typeUserName,userName.length,userName);
        if(userName.length%4 != 0) {
            for (int i = 0; i < (4 - (userName.length % 4)); i++)
                data[index++] = 0x00;
        }
        a = (len - dataAddedIndex - 4)/3;
        a-=a%4;

        data[index++] = (byte)(typePriority>>8 & 0xff);
        data[index++] = (byte)(typePriority & 0xff);
        data[index++] = (byte)(a>>8 & 0xff);
        data[index++] = (byte)(a & 0xff);
        for(int i=0;i<a;i++)
            data[index++] = temparray[dataAddedIndex++];

        data[index++] = (byte)(typeIceControlling>>8 & 0xff);
        data[index++] = (byte)(typeIceControlling & 0xff);
        data[index++] = (byte)(a>>8 & 0xff);
        data[index++] = (byte)(a & 0xff);
        for(int i=0;i<a;i++)
            data[index++] = temparray[dataAddedIndex++];

        data[index++] = (byte)(typeMsImplementationVersion>>8 & 0xff);
        data[index++] = (byte)(typeMsImplementationVersion & 0xff);
        data[index++] = (byte)(4>>8 & 0xff);
        data[index++] = (byte)(4 & 0xff);
        data[index++] = 0x00;
        data[index++] = 0x00;
        data[index++] = 0x00;
        data[index++] = 0x03;

        a = len - dataAddedIndex - 4;
        data[index++] = (byte)(typeMessageIntegrity>>8 & 0xff);
        data[index++] = (byte)(typeMessageIntegrity & 0xff);
        data[index++] = (byte)(a>>8 & 0xff);
        data[index++] = (byte)(a & 0xff);
        for(int i=0;i<a;i++)
            data[index++] = temparray[dataAddedIndex++];
        if(a%4!=0){
            for(int i=0;i<4-a%4;i++){
                data[index++] = 0x00;
            }
        }

        //a = len - dataAddedIndex;
        a = 4;
        data[index++] = (byte)(typeFingerPrint>>8 & 0xff);
        data[index++] = (byte)(typeFingerPrint & 0xff);
        data[index++] = (byte)(a>>8 & 0xff);
        data[index++] = (byte)(a & 0xff);
        for(int i=0;i<a;i++)
            data[index++] = temparray[dataAddedIndex++];
        if(a%4!=0){
            for(int i=0;i<4-a%4;i++){
                data[index++] = 0x00;
            }
        }

        index = index-20;
        data[2] = (byte)(index>>8 & 0xff);
        data[3] = (byte)(index & 0xff);
        return index+20;
    }
    public int createDummyStun(byte [] data){
        int index = 0,a;
        data[index++] = 0x00;
        data[index++] = 0x01;
        index+=2;

        /*data[index++] = 0x21;
        data[index++] = 0x12;
        data[index++] = (byte) 0xa4;
        data[index++] = 0x42;*/
        for(int i=0;i<magicCookie.length;i++){
            data[index++] = magicCookie[i];
        }

        index = Functions.getRandomData(data,index,12);
        index = Functions.appendAttribute(data,index,typeUserName,userName.length,userName);
        if(userName.length%4 != 0) {
            for (int i = 0; i < (4 - (userName.length % 4)); i++)
                data[index++] = 0x00;
        }

        a = 4;
        data[index++] = (byte)(typePriority>>8 & 0xff);
        data[index++] = (byte)(typePriority & 0xff);
        data[index++] = (byte)(a>>8 & 0xff);
        data[index++] = (byte)(a & 0xff);
        index = Functions.getRandomData(data,index,a);

        a = 8;
        data[index++] = (byte)(typeIceControlling>>8 & 0xff);
        data[index++] = (byte)(typeIceControlling & 0xff);
        data[index++] = (byte)(a>>8 & 0xff);
        data[index++] = (byte)(a & 0xff);
        index = Functions.getRandomData(data,index,a);

        data[index++] = (byte)(typeMsImplementationVersion>>8 & 0xff);
        data[index++] = (byte)(typeMsImplementationVersion & 0xff);
        data[index++] = (byte)(4>>8 & 0xff);
        data[index++] = (byte)(4 & 0xff);
        data[index++] = 0x00;
        data[index++] = 0x00;
        data[index++] = 0x00;
        data[index++] = 0x03;


        a = 20;
        data[index++] = (byte)(typeMessageIntegrity>>8 & 0xff);
        data[index++] = (byte)(typeMessageIntegrity & 0xff);
        data[index++] = (byte)(a>>8 & 0xff);
        data[index++] = (byte)(a & 0xff);
        index = Functions.getRandomData(data,index,a);

        //a = len - dataAddedIndex;
        a = 4;
        data[index++] = (byte)(typeFingerPrint>>8 & 0xff);
        data[index++] = (byte)(typeFingerPrint & 0xff);
        data[index++] = (byte)(a>>8 & 0xff);
        data[index++] = (byte)(a & 0xff);
        index = Functions.getRandomData(data,index,a);

        a = index-20;
        data[2] = (byte)(a>>8 & 0xff);
        data[3] = (byte)(a & 0xff);

        return index;
    }
    public int decodeStunPacket(byte [] data,int offset,int len){
        if(len <= 20)return len;
        for(int i=0;i<4;i++){
            if(data[i+offset+4] != magicCookie[i]){
                return len;
            }
        }
        int packetLen,packetType;
        int index = offset+8,receivedDataLen = 0;
        for(int i=0;i<12;i++){
            receivedData[receivedDataLen++] = data[index++];
        }
        index+=16;
        /*packetLen = Functions.byteArrayToint(data,index);
        index+=2;
        if(packetLen <= 4 || packetLen > 500)return len;
        for(int i=0;i<packetLen;i++){
            receivedData[receivedDataLen++] = data[index++];
        }*/

        while(index < len) {
            packetType = Functions.byteArrayToint(data, index);
            index += 2;
            packetLen = Functions.byteArrayToint(data, index);
            index += 2;
            if( (packetType != typeUserName && packetType != typeMsImplementationVersion) && packetLen <= 500) {
                for (int i = 0; i < packetLen; i++) {
                    receivedData[receivedDataLen++] = data[index++];
                }
            }else
                index+=packetLen;
            if(packetLen % 4 != 0){
                int a = 4 - (packetLen % 4);
                index += a;
            }
        }
        System.arraycopy(receivedData,0,data,0,receivedDataLen);
        return receivedDataLen;
    }
}
