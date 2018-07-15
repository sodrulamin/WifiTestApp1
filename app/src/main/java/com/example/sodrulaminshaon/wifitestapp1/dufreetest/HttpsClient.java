package com.example.sodrulaminshaon.wifitestapp1.dufreetest; /**
 * Created by Tahmid on 11/13/2017.
 */

import java.net.MalformedURLException;
import java.net.URL;
import java.io.*;

import javax.net.ssl.HttpsURLConnection;

import static com.example.sodrulaminshaon.wifitestapp1.Functions.getRandomData;

//import org.jnetpcap.protocol.tcpip.Http;

public class HttpsClient implements Runnable{
    
    public void run(){

        String https_url = "https://dfwithgo.appspot.com/";
        URL url;
        try {
            url = new URL(https_url);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            OutputStream os=con.getOutputStream();
            os.write(createTcpPacket(200));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    



    public static byte[] createTcpPacket(int len){
        byte[] data;
        data=getRandomData(len+2);
        data[0]=(byte)((len)>>8 & 0xf);
        data[1]=(byte)((len) & 0xff);
        return data;
    }

}