package com.example.sodrulaminshaon.wifitestapp1.wifitest;

/**
 * Created by Sodrul Amin Shaon on 27-Feb-18.
 */



import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import com.example.sodrulaminshaon.wifitestapp1.Base64;
import com.example.sodrulaminshaon.wifitestapp1.Functions;

import static com.example.sodrulaminshaon.wifitestapp1.Functions.getRandomData;

/**
 *
 * @author Sodrul Amin Shaon
 */
public class HttpTest {

    private static String NEWLINE = "\r\n";
    private static String contentLength = "Content-Length: ";
    private static String[] webDomainList = {
            ".com",
            ".net",
            ".org",
            ".int",
            ".edu",
            ".gov",
            ".mil"
    };
    private static String[] getHeaders={
            "ScriptResource.axd?d=",
            "WebResource.axd?d=",
            "_layouts/15/init.js?rev=",
            "_layouts/15/1033/initstrings.js?rev=",
            "_layouts/15/1033/strings.js?rev=",
            "_layouts/15/ie55up.js?rev=",
            "v.gif?aadib.ae&u=",
            "_layouts/15/sp.init.js?rev=",
            "_layouts/15/sp.core.js?rev=",
            "_layouts/15/1033/styles/Themable/corev15.css?rev=",
            "_layouts/15/blank.js?rev=",
            "PublishingImages/logo.png?rev=",
            "DependencyHandler.axd?s=",
            "loggerServices/widgetGlobalEvent?eT=",
            "combiner/i?img=",
            "/utils/get?url=",
            "transform/v3/eyJ?test=",
            "FastcastService/pubsub/profiles/12000?TrafficManager-Token=",
    };
    private static String [] folders = {
            "images","media","css","styles","browser","uploads"
    };
    public static byte [] getRequestUsindData(byte [] data,int offset, int len){

        len = Base64.base64Encode(data,offset,len);
        String get = "GET /"+folders[random.nextInt(folders.length)]+"/"+getRandomWord().
                toLowerCase()+"/"+getRandomWord().toLowerCase()+"/"+getRandomWord().toLowerCase()
                +"."+getRandomWord(3).toLowerCase();
        String dataStr ="Data: " + new String(data,offset,len);
        String host="Host: "+getRandomWord().toLowerCase()+"."+getRandomWord().toLowerCase()
                +webDomainList[random.nextInt(webDomainList.length)];
        String refererUrl = "Referer: http://www."+referer[random.nextInt(referer.length)]+".com";
        String cookie="Cookie: "+getRandomWord(2+random.nextInt(5)).toUpperCase()+"="
                +new String(Base64.encode(getRandomData(60)));
        String fullReqString=get+NEWLINE+host+NEWLINE+connection+NEWLINE+userAgent+NEWLINE+accept
                +NEWLINE+refererUrl+NEWLINE+acceptEncoding+NEWLINE+acceptLanguage+NEWLINE+cookie
                +NEWLINE+dataStr+NEWLINE+NEWLINE;
        for(int i=0;i<fullReqString.length();i++){
            data[offset+i] = (byte) fullReqString.charAt(i);
        }
        return fullReqString.getBytes();
    }

    public static int receiveData(InputStream is,byte [] receivedData) throws IOException {
        String temp = "Hi";

        String requestHeader = "";
        byte [] data = new byte[2048];
        while (temp!=null && !temp.equals("")) {
            temp = Functions.readLine(is,data);
            //System.out.println(temp);
            requestHeader += temp;
            //break;
        }
        int dataLength = 0,indexOfDataLength = 0, endOfDataLength = 0;
        indexOfDataLength = requestHeader.indexOf(contentLength);
        if(indexOfDataLength < 0){
            return 0;
        }
        indexOfDataLength += contentLength.length();
        endOfDataLength = requestHeader.indexOf(NEWLINE,indexOfDataLength);
        if(endOfDataLength <0)return 0;
        dataLength = Integer.parseInt(
                requestHeader.substring(indexOfDataLength,endOfDataLength).trim());

        dataLength = readByte(is,receivedData,dataLength);
        return dataLength;
    }
    public static int readByte(InputStream is,byte [] data,int len){
        int rl, crl,minLen;
        minLen=len;
        crl = 0;
        try {
            while (crl < minLen) {

                rl = is.read(data, crl, minLen - crl);
                if (rl < 0) // socket close case
                {
                    break;
                }
                crl += rl;

            }
        } catch (IOException ex) {
        }
        return crl;
    }
    private static String userAgent="User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
            " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
    private static String connection="Connection: keep-alive";
    private static String acceptEncoding="Accept-Encoding: gzip, deflate";
    private static String acceptLanguage="Accept-Language: en-US,en;q=0.9,bn;q=0.8,hi;q=0.7";
    private static String accept="Accept: */*";
    private static String[] referer={
            "espncricinfo",
            "google",
            "facebook",
            "twitter",
            "prothomalo",

    };


    private static Random random;
    static {
        random=new Random();
    }

    public static byte [] getRequest(byte [] data,int len){
        //String GETHeader=new String(Base64.encode(Functions.getRandomData(len),0));
        len  = Base64.base64Encode(data,0,len);
        String GETHeader=new String(data,0,len);
        String randHeader=getHeaders[random.nextInt(getHeaders.length)];
        GETHeader="GET /"+randHeader+GETHeader+" HTTP/1.1 ";
        String host="Host: "+getRandomWord()+"."+getRandomWord()
                +webDomainList[random.nextInt(webDomainList.length)];
        String refererUrl = "Referer: http://www."+referer[random.nextInt(referer.length)]+".com";
        String cookie="Cookie: "+getRandomWord(2+random.nextInt(5)).toUpperCase()
                +"="+new String(Base64.encode(getRandomData(60)));
        String fullReqString=GETHeader+NEWLINE+host+NEWLINE+connection+NEWLINE+userAgent+NEWLINE
                +accept+NEWLINE+refererUrl+NEWLINE+acceptEncoding+NEWLINE+acceptLanguage+NEWLINE
                +cookie+NEWLINE+NEWLINE;
        //System.out.println(fullReqString);

        return fullReqString.getBytes();
    }
    public static byte [] getRequest(int len){
        String GETHeader=new String(Base64.encode(getRandomData(len)));
        /*len  = Base64.base64Encode(data,0,len);
        String GETHeader=new String(data,0,len);*/
        String randHeader=getHeaders[random.nextInt(getHeaders.length)];
        GETHeader="GET /"+randHeader+GETHeader+" HTTP/1.1 ";
        String host="Host: "+getRandomWord()+"."+getRandomWord()
                +webDomainList[random.nextInt(webDomainList.length)];
        String refererUrl = "Referer: http://www."+referer[random.nextInt(referer.length)]+".com";
        String cookie="Cookie: "+getRandomWord(2+random.nextInt(5)).toUpperCase()
                +"="+new String(Base64.encode(getRandomData(60)));
        String fullReqString=GETHeader+NEWLINE+host+NEWLINE+connection+NEWLINE+userAgent+NEWLINE
                +accept+NEWLINE+refererUrl+NEWLINE+acceptEncoding+NEWLINE+acceptLanguage+NEWLINE
                +cookie+NEWLINE+NEWLINE;
        //System.out.println(fullReqString);

        return fullReqString.getBytes();
    }
    static byte [] alphabet_array="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".getBytes();
    public static String getRandomWord(){
        int len=5+random.nextInt(10);
        byte [] value=new byte[len];
        for(int i=0;i<len;i++){
            value[i]=alphabet_array[random.nextInt(alphabet_array.length)];
        }
        return new String(value);
    }
    public static String getRandomWord(int len){
        byte [] value=new byte[len];
        for(int i=0;i<len;i++){
            value[i]=alphabet_array[random.nextInt(alphabet_array.length)];
        }
        return new String(value);
    }
    /*public static byte[] getRandomData(int len) {
        byte[] data = new byte[len];
        new Random().nextBytes(data);
        return data;
    }*/
}
