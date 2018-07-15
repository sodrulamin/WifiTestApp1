package com.example.sodrulaminshaon.wifitestapp1;

import com.example.sodrulaminshaon.wifitestapp1.wifitest.RTLS;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Sodrul Amin Shaon on 9/10/2017.
 */

public class TcpHeaderChecker implements Runnable{
    MainActivity activity;
    int packetSize;
    TcpHeaderChecker(MainActivity a){
        activity=a;
        packetSize=500;
    }
    @Override
    public void run() {
        while(!activity.running);

        try {
            //while (true){
            for(int i=0; i<1; i++){
                Thread.sleep(activity.packetPerSocket);
                if(!activity.running)continue;
                Socket socket= RTLS.createSSLSocket2(activity.address,activity.port);//new Socket(activity.address,activity.port);
                new Thread(new Sender(socket)).start();
                new Thread(new Receiver(socket)).start();
                //Thread.sleep(Math.max(activity.packetPerSocket*packetSize-350,10));
                //break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class Sender implements Runnable{
        Socket socket;
        Sender(Socket s){
            socket=s;
        }
        @Override
        public void run() {
            try {
                socket.setTcpNoDelay(true);
                OutputStream os=socket.getOutputStream();
                //while (true) {
                for(int i = 0;i<10;i++){
                    Thread.sleep(activity.packetPerSocket);
                    if(!activity.running)continue;
                    os.write(createTcpPacket(activity.packetPerSocket));
                    activity.sentCount++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private class Receiver implements Runnable{
        Socket socket;
        Receiver(Socket s){
            socket=s;
        }
        @Override
        public void run() {
            try{
                InputStream is=socket.getInputStream();
                while (true){
                    int n=readByte(is);
                    if(n>1)activity.receivedCount++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public byte[] createTcpPacket(int len){
        byte[] data,header;
        //header=Functions.hexStringToByteArray("474554202f636f6e6e65637420485454502f312e310d0a486f73743a20636865636b2e676f6f676c657a69702e6e65740d0a436f6e6e656374696f6e3a206b6565702d616c6976650d0a557365722d4167656e743a0d0a4163636570742d456e636f64696e673a20677a69702c206465666c6174650d0a0d0a");
        //header=getHttpHeader();
        //header=activity.header.getBytes();
        data= Functions.getRandomData(len);//com.example.sodrulaminshaon.wifitestapp1.Base64.encode(Functions.getRandomData(len));
        data[0]=(byte)((data.length-2)>>8 & 0xf);
        data[1]=(byte)((data.length-2) & 0xff);
        //data=Functions.concatenateByteArrays(header,data);
        return data;
        //return header;
    }
    // ======================================================================== //
    public int readByte(InputStream is){

        int minLen = 2;

        int rl, crl;
        int mlen = minLen;

        byte[] chunkHeader = new byte[minLen];
        crl = rl = 0;
        while (crl < minLen) {
            try {
                rl = is.read(chunkHeader, crl, minLen - crl);
                if (rl < 0) {

                    break;

                }
                crl += rl;
            } catch (IOException ex) {

            }
        }
        minLen = chunkHeader[mlen - 2] & 0xff;
        minLen = (minLen << 8) | (chunkHeader[mlen - 1] & 0xff);
        byte[] b = new byte[minLen];
        crl = 0;

        while (crl < minLen) {
            try {
                rl = is.read(b, crl, minLen - crl);
                if (rl < 0) // socket close case
                {
                    break;
                }
                crl += rl;
            } catch (IOException ex) {

            }
        }
        //System.out.println("length:: " + minLen);
        return crl;
    }
    /*public byte[] getHttpHeader()
    {
        String str = "HTTP/1.1 200 OK";
        String[] headersArray = {"HTTP/1.1 200 OK","HTTP/1.1 100 Continue",
                "HTTP/1.1 101 Switching Protocols",
                "HTTP/1.1 102 Processing",
                "HTTP/1.1 201 Created",
                "HTTP/1.1 202 Accepted",
                "HTTP/1.1 203 Non-Authoritative Information",
                "HTTP/1.1 204 No Content",
                "HTTP/1.1 205 Reset Content",
                "HTTP/1.1 206 Partial Content",
                "HTTP/1.1 207 Multi-Status",
                "HTTP/1.1 208 Already Reported",
                "HTTP/1.1 226 IM Used",
                "HTTP/1.1 300 Multiple Choices",
                "HTTP/1.1 301 Moved Permanently",
                "HTTP/1.1 302 Found",
                "HTTP/1.1 303 See Other",
                "HTTP/1.1 304 Not Modified",
                "HTTP/1.1 305 Use Proxy",
                "HTTP/1.1 306 Switch Proxy",
                "HTTP/1.1 307 Temporary Redirect",
                "HTTP/1.1 308 Permanent Redirect",
                "HTTP/1.1 400 Bad Request",
                "HTTP/1.1 401 Unauthorized",
                "HTTP/1.1 402 Payment Required",
                "HTTP/1.1 403 Forbidden",
                "HTTP/1.1 404 Not Found",
                "HTTP/1.1 405 Method Not Allowed",
                "HTTP/1.1 406 Not Acceptable",
                "HTTP/1.1 407 Proxy Authentication Required",
                "HTTP/1.1 408 Request Timeout",
                "HTTP/1.1 409 Conflict",
                "HTTP/1.1 410 Gone",
                "HTTP/1.1 411 Length Required",
                "HTTP/1.1 412 Precondition Failed",
                "HTTP/1.1 413 Payload Too Large",
                "HTTP/1.1 414 URI Too Long",
                "HTTP/1.1 415 Unsupported Media Type",
                "HTTP/1.1 416 Range Not Satisfiable",
                "HTTP/1.1 417 Expectation Failed",
                "HTTP/1.1 418 I'm a teapot",
                "HTTP/1.1 421 Misdirected Request",
                "HTTP/1.1 422 Unprocessable Entity",
                "HTTP/1.1 423 Locked",
                "HTTP/1.1 424 Failed Dependency",
                "HTTP/1.1 426 Upgrade Required",
                "HTTP/1.1 428 Precondition Required",
                "HTTP/1.1 429 Too Many Requests",
                "HTTP/1.1 431 Request Header Fields Too Large",
                "HTTP/1.1 451 Unavailable For Legal Reasons",
                "HTTP/1.1 500 Internal Server Error",
                "HTTP/1.1 501 Not Implemented",
                "HTTP/1.1 502 Bad Gateway",
                "HTTP/1.1 503 Service Unavailable",
                "HTTP/1.1 504 Gateway Timeout",
                "HTTP/1.1 505 HTTP Version Not Supported",
                "HTTP/1.1 506 Variant Also Negotiates",
                "HTTP/1.1 507 Insufficient Storage",
                "HTTP/1.1 508 Loop Detected",
                "HTTP/1.1 510 Not Extended",
                "HTTP/1.1 511 Network Authentication Required",
                "HTTP/1.1 103 Checkpoint",
                "HTTP/1.1 420 Method Failure",
                "HTTP/1.1 420 Enhance Your Calm",
                "HTTP/1.1 450 Blocked by Windows Parental Controls",
                "HTTP/1.1 498 Invalid Token",
                "HTTP/1.1 499 Token Required",
                "HTTP/1.1 499 Request has been forbidden by antivirus",
                "HTTP/1.1 509 Bandwidth Limit Exceeded",
                "HTTP/1.1 530 Site is frozen",
                "HTTP/1.1 440 Login Timeout",
                "HTTP/1.1 449 Retry With",
                "HTTP/1.1 451 Redirect",
                "HTTP/1.1 444 No Response",
                "HTTP/1.1 495 SSL Certificate Error",
                "HTTP/1.1 496 SSL Certificate Required",
                "HTTP/1.1 497 HTTP Request Sent to HTTPS Port",
                "HTTP/1.1 499 Client Closed Request",
                "HTTP/1.1 520 Unknown Error",
                "HTTP/1.1 521 Web Server Is Down",
                "HTTP/1.1 522 Connection Timed Out",
                "HTTP/1.1 523 Origin Is Unreachable",
                "HTTP/1.1 524 A Timeout Occurred",
                "HTTP/1.1 525 SSL Handshake Failed",
                "HTTP/1.1 526 Invalid SSL Certificate"};

        int index = activity.headerNumber;
        if(activity.headerNumber>headersArray.length)index=0;
        str=headersArray[index];
        byte[] data = str.getBytes();
        if(data.length<55){
            data=Functions.concatenateByteArrays(data,Functions.getRandomData(55-data.length));
        }

        return data;
    }*/
}
