package com.example.sodrulaminshaon.wifitestapp1.dufreetest;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;
import com.example.sodrulaminshaon.wifitestapp1.Base64;
import com.example.sodrulaminshaon.wifitestapp1.Functions;

/**
 * Created by Ibrahim Tahmid
 * Date: 5 May, 2018
 */

public class DNSMessageBuilder {

    private static final String TAG = "DNSMessageBuilder";
    private Random random = new Random();

    public static String DNS_IP = "8.8.8.8";
    public static int DNS_PORT = 53;
    public static String[] SUBDOMAIN_PROVISIONING = new String[]{
            ".dp.al-alamin.com",
            ".dp.alalamin19.com"
    };

    public static String SIG_RESPONSE_DELIMITER = "###";




    public byte[] createPacket(String subDomain){
        byte [] data = concatenateByteArrays("SHAON".getBytes(), Functions.getRandomData(5+random.nextInt(20)));
        String hexData = new String(Base64.encode(data));
        //if(SIPProvider.DEBUG) Log.i(TAG, "PingData: " + hexData);

        int maxSize = 63;
        if(hexData.length()> maxSize){
            int i;
            StringBuilder sb = new StringBuilder();
            for (i = 0; i < hexData.length(); i++) {
                if(i != 0 && i % maxSize == 0 ){
                    sb.append(".");
                }
                sb.append(hexData.charAt(i));
            }
            hexData = sb.toString();
        }

        String query = hexData + (!subDomain.startsWith(".") ? "." + subDomain : subDomain);
        return generateDNSPacket(query);
    }

    // Source: Alamin
    // if any error then return null
    // if you want to send to server "alamin" then your data will be something like: data=alamin.dt.alalamin19.com
    public byte[] generateDNSPacket(String data){

        String transactionID = byteArrayToHexString(randomByteArray(2));  // random 2 byte transaction id
        String flags = "0100";
        String noOfQuestions = "0001";
        String answerRR = "0000";
        String authorityRR = "0000";
        String additionalRR = "0000";
        byte [] question;
        byte[] dnsResponse;
        String footer = "0000050001";

        String responseHeaderHex = transactionID + flags + noOfQuestions + answerRR + authorityRR + additionalRR;

        String [] parts = data.split("\\.");

        ByteArrayOutputStream bOutput = new ByteArrayOutputStream(data.length() + 1);
        for(int i = 0; i < parts.length; i++){

            bOutput.write(parts[i].length());
            try {
                bOutput.write(parts[i].getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                return null;

            }
        }
        question = bOutput.toByteArray();

        byte[] responseHeader = hexStringToByteArray(responseHeaderHex);
        dnsResponse = concatenateByteArrays(responseHeader, question);
        dnsResponse = concatenateByteArrays(dnsResponse, hexStringToByteArray(footer));

//        return new ByteArray(dnsResponse);
        return dnsResponse;
    }
    public boolean isValid(byte [] data){
        int len = (data[0] & 0xff);
        if(len>data.length || len<0)return false;
        for(int i=1;i<len+1;i++){
            if(data[i] != (byte)'a')return false;
        }
        return true;
    }
    public byte[] getDataFromDNSPacket(byte[] response,int len){

        //if(SIPProvider.DEBUG) Log.i(TAG, "Getting DNS Ping Response");
        String parseDelimiter = "###";
        String queryStr = byteArrayToHexString(response,len);
        String parseDelimiterHex = byteArrayToHexString(parseDelimiter.getBytes());

        int start = queryStr.indexOf(parseDelimiterHex, 24);
        int end = queryStr.lastIndexOf(parseDelimiterHex);

        if(start == -1 || end == -1){
            System.out.println("Invalid packet. start = "+start+" end = "+end);
            return null;
        }

        String data; // = queryStr.substring(start + parseDelimiterHex.length(), end);
        data = queryStr.substring(start - 2, end) + parseDelimiterHex;
       // if(SIPProvider.DEBUG) Log.i(TAG, "datalen: " + data.length());

        byte[] cnameData = hexStringToByteArray(data);
        byte[] cname = decodeCname(cnameData);
        //if(SIPProvider.DEBUG) Log.i(TAG, "after_decode: " + byteArrayToHexString(cname).length());

        byte[] result = new byte[cname.length - 2 * parseDelimiter.length() ];
        System.arraycopy(cname, parseDelimiter.length(), result, 0, cname.length - parseDelimiter.length() * 2);
       // if(SIPProvider.DEBUG) Log.i(TAG, "datalen: " + result.length);

//        return new ByteArray(result);
        return result;
    }


    private byte[] decodeCname(byte[] data){
//        System.out.println("datalen: " + data.length + " data: " + new String(data));
        ByteArrayOutputStream bOutput = new ByteArrayOutputStream(data.length + 1);
        for(int i = 0; i < data.length; i++){

            int len = data[i];
            //System.out.println("i: " + i + " len: " + len);
            if(len <= 0) break;
//            i++;
            for(int j = 1; j <= len; j++){
//                i++;
                bOutput.write(data[i + j]);
            }
            i += len;
        }

        return bOutput.toByteArray();
    }

    private String byteArrayToHexString(byte[] in) {
        final StringBuilder builder = new StringBuilder();

        for(byte b : in) {
            builder.append(String.format("%02x", b));
        }

        return builder.toString();
    }
    private String byteArrayToHexString(byte[] in,int len) {
        final StringBuilder builder = new StringBuilder();

        for(int i = 0; i < len; i++) {
            builder.append(String.format("%02x", in[i]));
        }

        return builder.toString();
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    private static byte[] randomByteArray(int size) {
        byte[] data = new byte[size];

        Random rand = new Random();
        rand.nextBytes(data);

        return data;
    }

    public static byte[] concatenateByteArrays(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];

        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);

        return result;
    }
}
