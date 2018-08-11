package com.example.sodrulaminshaon.wifitestapp1.dufreetest;

import android.net.ConnectivityManager;
import android.util.Log;

import com.example.sodrulaminshaon.wifitestapp1.Base64;
import com.example.sodrulaminshaon.wifitestapp1.Functions;
import com.example.sodrulaminshaon.wifitestapp1.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

/**
 * Created by User on 05-Jun-18.
 */

public class DNSMessageBuilderUpdated {
    public static MainActivity activity;





    /*public static void main(String args[]) throws IOException, InterruptedException {
        System.out.println("dns udp Server port, send_data_size: ");
        int delay = 1000;
        int sendDataSize = 10;
        System.out.println("enter dalay and size : 100 10" );
        if(args.length >= 1) delay = new Integer(args[0]);
        if(args.length >= 2) sendDataSize = new Integer(args[1]);

        System.out.println("delay: " + delay + " size: " + sendDataSize );
        updateStatics();

        for(int i = 0; i < 300; i++ ){

//            String data = helper.Helper.getRandomAlphaString(sendDataSize);
            byte [] data = getRandomData(sendDataSize);
            data = ("s_" + sent + "_" + getRandomAlphaString(sendDataSize)).getBytes();
            String sudDomain = ".dt.alalamin19.com";
            sudDomain = ".dtmx2.dubai3g.xyz";

            byte[] dnsResponse;
            dnsResponse = generateMXPacket(data, sudDomain);
//            System.out.println("dns response: " + dnsResponse.length);
            sendAndReceiveDnsReq(dnsResponse);

            Thread.sleep(delay);
        }

//        generateMXPacket("alamin.dt.alalamin19.com");

    }*/


    public static byte[] getMXDnsPingData(byte[] response) {


        byte[] data = response;
        int i;
        for (i = 12; i < data.length; i++) {
            int len = data[i];
            if (len <= 0) break;
            i += len;
        }

        i += 5; // question is over

        i += 10;
        i += 1;
        int ansLen = data[i] & 0xFF;

        i += 3;
        ByteArrayOutputStream bOutput = new ByteArrayOutputStream(ansLen);
//        i += 2;
        for (; i < data.length; i++) {

            int len = data[i];
//            System.out.println("i: " + i + " len: " + len);
            if (len <= 0) break;
//            i++;
            for (int j = 1; j <= len; j++) {
//                i++;
                bOutput.write(data[i + j]);
            }
            i += len;
        }

        return bOutput.toByteArray();

    }


    public static byte[] generateMXPacket(byte[] data, String subDomain) {
        String hexData = new String(Base64.encode(data));
//        System.out.println("orginal hex data: " + hexData);
        hexData = new String(data);
        if (hexData.length() > 63) {
            int maxSize = 63;
            int i;
            int offset = 0;
            StringBuilder sb = new StringBuilder();
            for (i = 0; i < hexData.length(); i++) {
                if (i != 0 && i % maxSize == 0) {
                    sb.append(".");
                }
                sb.append(hexData.charAt(i));

            }
            hexData = sb.toString();
//            System.out.println("now new hex data is: " + hexData);

        }
        String query = hexData + subDomain;

        return generateMXPacket(query);
    }

    // if any error then return null
    // if you want to send to server "alamin" then your data will be something like: data=alamin.dt.alalamin19.com
    public static byte[] generateMXPacket(String data) {
//        System.out.println("quere is: " + data);
        String transectionID = byteArrayToHexString(getRandomData(2));  // random 2 byte transection id
        String flags = "0130";
        String noOfQuestions = "0001";
        String answerRR = "0000";
        String authorityRR = "0000";
        String additionalRR = "0000";
        byte[] question;
//        byte [] response;
        byte[] dnsResponse;
        String footer = "00000f0001";


        String responseHeaderHex = transectionID + flags + noOfQuestions + answerRR + authorityRR + additionalRR;

        String[] parts = data.split("\\.");

        ByteArrayOutputStream bOutput = new ByteArrayOutputStream(data.length() + 1);
        for (int i = 0; i < parts.length; i++) {

            bOutput.write(parts[i].length());
            try {
                bOutput.write(parts[i].getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                return null;

            }
        }
        question = bOutput.toByteArray();
//        System.out.println("question size: " + question.length + " ques: " + new String(question));

        byte[] responseHeader = getByteArrayFromHexString(responseHeaderHex);
        dnsResponse = concatenateByteArrays(responseHeader, question);
        dnsResponse = concatenateByteArrays(dnsResponse, getByteArrayFromHexString(footer));

        return dnsResponse;
//        return bOutput.toByteArray();
    }


    public static void sendAndReceiveDnsReq(final byte[] dnsResponse) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String parseDelimeter = "###";
                try {
                    if (dnsResponse == null) return;
                    DatagramSocket clientSocket = new DatagramSocket();
                    byte[] receiveData = new byte[512];
//                    DatagramPacket sendPacket = new DatagramPacket(dnsResponse, dnsResponse.length, InetAddress.getByName("8.8.8.8"), 53);
                    DatagramPacket sendPacket = new DatagramPacket(dnsResponse, dnsResponse.length, InetAddress.getByName("8.8.8.8"), 53);
                    DatagramPacket sendPacket2 = new DatagramPacket(dnsResponse, dnsResponse.length, InetAddress.getByName("192.168.18.32"), 53);

                    clientSocket.send(sendPacket);
                    activity.sentCount++;
//                    clientSocket.send(sendPacket2);
                    //sent++;

//                    System.out.println("dns request has been sent: ");

                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    clientSocket.receive(receivePacket);

//                    System.out.println("data rec in hex :" + Helper.byteArrayToHexString(receivePacket.getData()));

                    byte[] response = getMXDnsPingData(receivePacket.getData());
//                    System.out.println("Get dns ping data has been called");

                    if (response != null && response.length > 0) {
                        activity.receivedCount++;
                        if (isValidResponse(response, 'a')) {

                            /*receive++;
                            updateMsg();*/
                        } else {
//                            System.out.println("data is not valid: ");
//                            System.exit(1);
                        }
//                        System.out.println("Rec count should increase: " + receive);
                    } else {
//                        System.out.println("******* in error block: ");
//                        System.exit(1);
//                        int a  = 5 / 0;
                        Log.i("reve", "Invalid response");


                    }


                    if (doesContainMXAnswer(receivePacket.getData())) {
                        //Log.i("reve", "contains answer: " + skip);
                    } else {
                        /*skip++;
                        updateMsg();*/
                        Log.i("reve", "Error got response but does not contain answer");
                    }


                    System.out.println("datalen: " + response.length + " data: " + new String(response));


//                    System.out.println("FROM SERVER:" + modifiedSentence.length());

                } catch (Exception e) {
                    System.out.println(e);
                }

            }
        }).start();
    }

    public static boolean doesContainMXAnswer(byte[] data) {
        String hex = Functions.bytesToHex(data);
        String delimeter = "c00c000f0001";
        if (hex.contains(delimeter)) return true;
        return false;
    }


    public static void updateStatics() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        //System.out.println("sent: " + sent + " rec: " + receive );

                        Thread.sleep(1000);

                    } catch (Exception e) {
                    }
                }
            }
        }).start();
    }


    public static byte[] decodeCname(byte[] data) {
//        System.out.println("datalen: " + data.length + " data: " + new String(data));
        ByteArrayOutputStream bOutput = new ByteArrayOutputStream(data.length + 1);
        for (int i = 0; i < data.length; i++) {

            int len = data[i];
            System.out.println("i: " + i + " len: " + len);
            if (len <= 0) break;
//            i++;
            for (int j = 1; j <= len; j++) {
//                i++;
                bOutput.write(data[i + j]);
            }
            i += len;
        }

        return bOutput.toByteArray();
    }

    public static byte[] getByteArrayFromInt(int data) {
        byte[] result = new byte[4];

        result[0] = (byte) ((data & 0xFF000000) >> 24);
        result[1] = (byte) ((data & 0x00FF0000) >> 16);
        result[2] = (byte) ((data & 0x0000FF00) >> 8);
        result[3] = (byte) ((data & 0x000000FF) >> 0);

        return result;
    }

    public static boolean isValidResponse(byte[] data, char ch) {
        int len = data[0] & 0xff;
        if (len != data.length) return false;
        for (int i = 1; i < data.length; i++) {
            if (data[i] != ch) return false;
        }
        return true;
    }

    public static String byteArrayToHexString(byte[] in) {
        final StringBuilder builder = new StringBuilder();

        for (byte b : in) {
            builder.append(String.format("%02x", b));
        }

        return builder.toString();
    }

    public static byte[] getRandomData(int len) {
        byte[] header = new byte[len];
        new Random().nextBytes(header);
//        header = concatenateByteArrays(hexStringToByteArray("0000"), header);
//        header[0] = (byte) ((len - 2)>>8 & 0xFF);
//        header[1] = (byte) ((len - 2) & 0xFF);
        return header;

    }

    public static byte[] getByteArrayFromHexString(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }


    public static byte[] concatenateByteArrays(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }


    public static String getRandomAlphaString(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            char c = (char) ('a' + (random() % 26));
            sb.append(c);

        }
//        return sb.toString();
        return sb.toString().substring(0, n);

    }

    public static int random() {
        int n = (int) (Math.random() * Integer.MAX_VALUE);
        return n;
    }


    public static void testMXTunnel(final ConnectivityManager manager, final String[] domainNames, final int count, final int delay, final int size) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    InetAddress.getByName("facebook.com");
//                    HelperSolution.DNSCNAMERecord("calamin.alalamin19.com", null);
//                    DNSResolving();
//                    DNSResolving("www.alalamin19.com");
//                    String parseDelimeter = "###";
                    for (int i = 0; i < count; i++) {
                        for (int j = 0; j < domainNames.length; j++) {
                            String sudDomain = domainNames[j];

                            // byte [] data = ("zs_" + sent + "_r_" + receive + "_sk_" + skip + "_" + getRandomAlphaString(size)).getBytes();
                            byte[] dnsResponse;
//                            dnsResponse = CNAME.generateMXPacket(data, sudDomain);
                            dnsResponse = DNSMessageBuilderUpdated.generateMXPacket("Shaon".getBytes(), sudDomain);
//            System.out.println("dns response: " + dnsResponse.length);
                            DNSMessageBuilderUpdated.sendAndReceiveDnsReq(dnsResponse);
//                            CNAME.sendDnsReq(manager, dnsResponse, parseDelimeter);

                            // generate using library
//                            String s = new String(data) + sudDomain;
//                            DNSCNAMERecord(s, null);
                            Thread.sleep(delay);


//                            Log.i("reve", message);

                        }

                    }

                } catch (Exception e) {
//                    System.out.println(e);
                    Log.e("reve", e.toString());
                }
            }
        }).start();
    }
}