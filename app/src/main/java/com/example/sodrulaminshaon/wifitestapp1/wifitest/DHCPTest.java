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
    private static int MIN_RAND_LEN = 10;
    private static int MAX_RAND_LEN = 100;
    private static Random random;
    static {
        random=new Random();
    }
    private static byte [] messageTypeArray={
            0x01,0x0b
    };
    public byte [] getDhcpPacket(){
        DhcpClass dhcpClass=new DhcpClass();
        byte[] data=dhcpClass.getDhcpData();
        return data;
    }

    private class DhcpClass{
        public byte messageType;
        public byte [] transId;

        public TLV elapseTime;
        public TLV clientIdentifier;
        public TLV identityAssociationForNonTemporaryAddress;
        public TLV fullyQualifiedDomainName;
        public TLV vendorClass;
        public TLV optionRequest;

        public DhcpClass(){
            messageType = messageTypeArray[random.nextInt(2)];
            transId=Functions.getRandomData(3);
            //elapseTime=new TLV(ELAPSE_TIME);
            elapseTime=new TLV(ELAPSE_TIME,2);
            clientIdentifier=new TLV(CLIENT_IDENTIFIER,14);
            identityAssociationForNonTemporaryAddress=new TLV(IDENTITY_ASSOCIATION,12);
            fullyQualifiedDomainName=new TLV(FULLY_QUALIFIED_DOMAIN_NAME);
            vendorClass=new TLV(VENDOR_CLASS,Functions.hexStringToByteArray("0000013700084d53465420352e30"));
            optionRequest = new TLV(OPTION_REQUEST,Functions.hexStringToByteArray("0018001700110027"));
        }


        public byte [] getDhcpData(){
            byte [] data=new byte[2048];
            int index=0;

            data[index++] = messageType;
            for(int i=0;i<transId.length;i++){
                data[index++]=transId[i];
            }

            index=appendAttribute(data,index,elapseTime);
            index=appendAttribute(data,index,clientIdentifier);
            index=appendAttribute(data,index,identityAssociationForNonTemporaryAddress);
            index=appendAttribute(data,index,fullyQualifiedDomainName);
            index=appendAttribute(data,index,vendorClass);
            index=appendAttribute(data,index,optionRequest);


            byte [] retData=new byte[index];
            System.arraycopy(data,0,retData,0,index);
            return retData;
        }
    }
    private int appendAttribute(byte[] data,int index,TLV attr){
        data[index++] = (byte) ((attr.type >> 8) & 0xFF);
        data[index++] = (byte) ((attr.type) & 0xFF);
        //Length
        data[index++] = (byte) ((attr.len >> 8) & 0xFF);
        data[index++] = (byte) (attr.len & 0xFF);

        for (int i = 0; i < attr.len; i++)
        {
            data[index++] = attr.value[i];
        }
        return index;
    }
    private class TLV {
        public int type;
        public int len;
        public byte[] value;
        public TLV(){
            ////do nothing
        }
        public TLV(int type,byte [] data){
            this.type=type;
            len=data.length;
            value=new byte[len];
            System.arraycopy(data,0,value,0,len);
        }
        public TLV(int type){
            this.type=type;
            len=MIN_RAND_LEN+random.nextInt(MAX_RAND_LEN-MIN_RAND_LEN);

            value= Base64.encode(Functions.getRandomData(len));
            len=value.length;
        }
        public TLV(int type,int len){
            this.type=type;
            this.len=len;
            value=Functions.getRandomData(len);
        }
    }
}
