package com.example.sodrulaminshaon.wifitestapp1.wifitest;

import com.example.sodrulaminshaon.wifitestapp1.Base64;
import com.example.sodrulaminshaon.wifitestapp1.Functions;

import java.util.Random;

/**
 * Created by Sodrul Amin Shaon on 12-Feb-18.
 */

public class DHCPTest{
    private static int ELAPSE_TIME = 0x0008;
    private static int CLIENT_IDENTIFIER =  0x0001;
    private static int IDENTITY_ASSOCIATION = 0x0003;
    private static int FULLY_QUALIFIED_DOMAIN_NAME = 0x0027;
    private static int VENDOR_CLASS = 0x0010;
    private static int OPTION_REQUEST = 0x0006;
    private static Random random;
    private static byte [] vendorClass = {0x00,0x00,0x01,0x37,0x00,0x08,0x4d,0x53,0x46,0x54,0x20,0x35,0x2e,0x30};
    private static byte [] optionRequest = {0x00,0x18,0x00,0x17,0x00,0x11,0x00,0x27};
    private static byte [] swapData = new byte[2048];
    static {
        random=new Random();
    }
    private static byte [] messageTypeArray={
            0x01,0x0b
    };
    public static int createPacket(byte [] data, int len){
        byte [] returnData = createDomainName(data,len);

        int index = 0;
        data[index++] = messageTypeArray[random.nextInt(messageTypeArray.length)];
        for(int i=0;i<3;i++)
            data[index++] = (byte)(random.nextInt(255) & 0xff);
        index = appendAttribute(data,index,ELAPSE_TIME,2);
        for(int i=0;i<2;i++)
            data[index++] = (byte)(random.nextInt(255) & 0xff);

        index = appendAttribute(data,index,CLIENT_IDENTIFIER,14);
        for(int i=0;i<14;i++)
            data[index++] = (byte)(random.nextInt(255) & 0xff);

        index = appendAttribute(data,index,IDENTITY_ASSOCIATION,12);
        for(int i=0;i<12;i++)
            data[index++] = (byte)(random.nextInt(255) & 0xff);

        index = appendAttribute(data,index,FULLY_QUALIFIED_DOMAIN_NAME,returnData.length);
        for(int i=0;i<returnData.length;i++)
            data[index++] = returnData[i];

        index = appendAttribute(data,index,VENDOR_CLASS,vendorClass.length);
        for(int i=0;i<vendorClass.length;i++)
            data[index++] = vendorClass[i];

        index = appendAttribute(data,index,OPTION_REQUEST,optionRequest.length);
        for(int i=0;i<optionRequest.length;i++)
            data[index++] = optionRequest[i];

        return index;
    }
    private static byte [] createDomainName(byte [] data, int len){
        //len = Base64.base64Encode(data,0,len);
        int totalLength = len+len/63+2;
        if(len%63 > 0)totalLength++;
        byte [] returnData = new byte [totalLength];
        returnData[0] = 0x00;
        int i =0,dataLen,index = 1;
        while(i<len){
            dataLen = (len-i)>63 ? 63:(len-i);
            returnData[index++] = (byte) (0xff & dataLen);
            System.arraycopy(data,i,returnData,index,dataLen);
            i+=dataLen;
            index+=dataLen;
        }
        returnData[index++] = 0x00;
        return returnData;
    }
    private static int appendAttribute(byte[] data,int index,int type,int len){
        data[index++] = (byte) ((type >> 8) & 0xFF);
        data[index++] = (byte) ((type) & 0xFF);
        //Length
        data[index++] = (byte) ((len >> 8) & 0xFF);
        data[index++] = (byte) (len & 0xFF);
        return index;
    }

    public static int getDataFromPacket(byte [] data, int len){
//        if(len<=48 || data == null || data.length<=48)return 0;
//        int domainLength = (int)data[46]
        int index = 4,type,length;
        while(index < len){
            type = byteArrayToint(data,index);
            index+=2;
            length = byteArrayToint(data,index);
            index+=2;
            if(type == FULLY_QUALIFIED_DOMAIN_NAME){
                System.arraycopy(data,index,swapData,0,length);
                len = getDataFromDomainName(swapData,length);
                System.arraycopy(swapData,0,data,0,len);
                return len;
            }
            if(length>=0)index+=length;
        }
        return 0;
    }
    public static int byteArrayToint(byte [] array,int startIndex){
        return ((array[startIndex] & 0xff) << 8) | (array[startIndex+1] & 0xff);
    }
    private static int getDataFromDomainName(byte [] domainName,int len){
        byte [] data = new byte[len];
        System.arraycopy(domainName,0,data,0,len);
        int index = 1,length,actualLen = 0;
        while(index < len){
            //System.out.println(data[index]);
            length = (data[index++] & 0xff);
            if(length == 0)return actualLen;
            //System.out.println("test: "+Functions.bytesToHex(data,index,length));
            System.arraycopy(data,index,domainName,actualLen,length);
            index+=length;
            actualLen+=length;
        }
        return actualLen;
    }

}
