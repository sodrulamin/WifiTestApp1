package com.example.sodrulaminshaon.wifitestapp1.wifitest;

/**
 * Created by Sodrul Amin Shaon on 27-Feb-18.
 */



import android.util.Base64;

import com.example.sodrulaminshaon.wifitestapp1.Functions;
import java.io.InputStream;
import java.util.Random;

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

    private static String readLine(InputStream is) {
        byte [] data=new byte[2048];
        StringBuilder sb=new StringBuilder();
        int index=0;
        try{

            while(true){
                int a=is.read(data,index,1);
                if(a==-1)break;
                if(data[index]==0x0a){
                    if(index>0 && data[index-1]==0x0d)break;
                }
                index++;
            }
        }catch(Exception e){
            System.out.println("error: "+e.getMessage());
        }
        String str=new String(data,0,index+1);
        if(index>2){
            return str;
        }
        return "";
    }
    public static byte [] receiveData(InputStream is){
        String temp = "Hi";

        String requestHeader = "";

        while (temp!=null && !temp.equals("")) {
            temp = readLine(is);
            //System.out.println(temp);
            requestHeader += temp;
            //break;
        }
        int dataLength = 0,indexOfDataLength = 0, endOfDataLength = 0;
        indexOfDataLength = requestHeader.indexOf(contentLength);
        if(indexOfDataLength < 0){
            return null;
        }
        indexOfDataLength += contentLength.length();
        endOfDataLength = requestHeader.indexOf(NEWLINE,indexOfDataLength);
        if(endOfDataLength <0)return null;
        dataLength = Integer.parseInt(requestHeader.substring(indexOfDataLength,endOfDataLength).trim());

        byte [] data= new byte[dataLength];
        dataLength = Functions.readByte(is,data,dataLength);
        return data;
    }
    private static String userAgent="User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
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

    public static byte [] getRequest(int len){
        String GETHeader=new String(Base64.encode(Functions.getRandomData(len),0));
        String randHeader=getHeaders[random.nextInt(getHeaders.length)];
        GETHeader="GET /"+randHeader+GETHeader+" HTTP/1.1 ";
        String host="Host: "+Functions.getRandomWord()+"."+Functions.getRandomWord()+webDomainList[random.nextInt(webDomainList.length)];
        String refererUrl = "Referer: http://www."+referer[random.nextInt(referer.length)]+".com";
        String cookie="Cookie: "+Functions.getRandomWord(2+random.nextInt(5)).toUpperCase()+"="+new String(Base64.encode(Functions.getRandomData(60),0));
        String fullReqString=GETHeader+NEWLINE+host+NEWLINE+connection+NEWLINE+userAgent+NEWLINE+accept+NEWLINE+refererUrl+NEWLINE+acceptEncoding+NEWLINE+acceptLanguage+NEWLINE+cookie+NEWLINE+NEWLINE;
        //System.out.println(fullReqString);

        return fullReqString.getBytes();
    }

}
