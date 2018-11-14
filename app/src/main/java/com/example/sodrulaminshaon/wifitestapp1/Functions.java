package com.example.sodrulaminshaon.wifitestapp1;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by Sodrul Amin Shaon on 8/20/2017.
 */

public class Functions {
    static long count=1;
    static byte [] rand;
    static byte [] alphabet_array="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".getBytes();
    static byte [] numbers_array="0123456789".getBytes();
    static byte [] other_chars="?/\\[]{}()!@#$%^&*_+-,.<>~`".getBytes();
    public static Random random = new Random();
    public static DatagramPacket createPacket(InetAddress address,int port, int len){
        byte [] data,header;
        header=hexStringToByteArray("000300000002000000000000095f3233333633374445045f7375620b5f676f6f676c6563617374045f746370056c6f63616c00000c0001c01b000c0001");
       /* header[header.length-2]=(byte)((len+4)>>8 & 0xff);
        header[header.length-1]=(byte)((len+4) & 0xff);*/
        count++;
        header[0]=(byte)(count>>8 & 0xff);
        header[1]=(byte)(count & 0xff);
        data=getRandomData(len);
        data=concatenateByteArrays(header,data);
        return new DatagramPacket(data,data.length,address,port);
    }

    public static byte [] getRandomDataWithDummyData(int len)
    {

        byte [] message = getRandomData(len);
        message = concatenateByteArrays(hexStringToByteArray("0000"),message);
        message[0] = (byte) ((message.length-2)>>8 & 0xFF);
        message[1] = (byte) ((message.length-2) & 0xFF);
        int a = 1000 + new Random().nextInt(250);
        byte [] random = new byte[a];
        new Random().nextBytes(random);
        random = concatenateByteArrays(message,random);
        random = concatenateByteArrays(hexStringToByteArray("0000"),random);
        random[0]= (byte) ((random.length -2)>>8 & 0xFF);
        random[1]= (byte) ((random.length - 2) & 0xFF);
        random = concatenateByteArrays(hexStringToByteArray("170303"),random);
        return random;
    }
    // ======================================================================== //
    public static byte[] createTcpPacket(int len){
        byte[] data,header;
        /*header=concatenateByteArrays(hexStringToByteArray("1703030000"),rand);
        header[3]=(byte)((len+2)>>8 & 0xff);
        header[4]=(byte)((len+2) & 0xff);*/
        data=getRandomData(len+2);
        data[0]=(byte)((len)>>8 & 0xf);
        data[1]=(byte)((len) & 0xff);
        //data=concatenateByteArrays(header,data);
        return data;
    }
    public static int byteArrayToint(byte [] array,int startIndex){
        return ((array[startIndex] & 0xff) << 8) | (array[startIndex+1] & 0xff);
    }
    public  byte [] getDynamicFacebookClientHello(){
        byte [] clientHello=hexStringToByteArray("1603010214010002100303");
        byte [] randomByte=getRandomData(65);
        int dateInSec = (int) (System.currentTimeMillis() / 1000);
        byte[] bytes = ByteBuffer.allocate(4).putInt(dateInSec).array();
        System.arraycopy(bytes,0,randomByte,0,4);
        randomByte[32]=0x20;
        clientHello=concatenateByteArrays(clientHello,randomByte);
        clientHello=concatenateByteArrays(clientHello,hexStringToByteArray("0098c030c02cc028c024c014c00a00a500a300a1009f006b006a006900680039003800370036cca9cca8cc14cc13ccaacc15c032c02ec02ac026c00fc005009d003d0035c02fc02bc027c023c013c00900a400a200a0009e00670040003f003e0033003200310030c031c02dc029c025c00ec004009c003c002fc011c007c00cc00200050004c012c008001600130010000dc00dc003000a00ff0100012f0000001b0019000016656467652d6d7174742e66616365626f6f6b2e636f6d000b000403000102000a001c001a00170019001c001b0018001a0016000e000d000b000c0009000a002300c07f311f2e23201e6d49bc1fd896e678a687746f5275592753894287984cb0644ee8490a2ec31c4f30d6c8093d287aebc8677c4c6fdaca3ee9fc612c73356012f86463d995636f7908d4e73884426cc0e22f1e90c0710d9e7ba613e490546ed7b467d12a75f42c53a009ef77bf03bac8e0e5a313eecdfcd30164f42a1eacbd66f66df07c18778d0aca0e3d791d34cd029be5095c5e78425046b47a61960957e4d0f0d7f9d46ffbf10ae51cc4e7291eaf082fe2f6526cb69ef3d0a4a04c209eba18000d0020001e060106020603050105020503040104020403030103020303020102020203"));
        return clientHello;
    }
    // ======================================================================== //
    public static int getRandomData(byte [] array,int offset,int len) {
        for(int i=0;i<len;i++){
            array[offset+i] = (byte) random.nextInt(256);
        }
        return offset+len;
    }
    // ======================================================================== //
    public static byte[] getRandomData(int len) {
        byte[] data = new byte[len];
        new Random().nextBytes(data);
        return data;
    }
    // ========================================================================= //
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        if(len % 2 !=0)return null;
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
    public static int hexStringToByteArray(String s,byte [] destByteArray,int offset) {
        int len = s.length();
        if(len % 2 !=0)return offset;
        //byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            destByteArray[offset + i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return offset+len/2;
    }
    public static String createVisibleCharArray(int len){
        byte [] data=new byte[len];
        Random selector=new Random();
        for(int i=0;i<len;i++){
            int n=selector.nextInt(3);

            switch(n){
                case 0:
                    data[i]=alphabet_array[selector.nextInt(alphabet_array.length)];
                    break;
                case 1:
                    data[i]=numbers_array[selector.nextInt(numbers_array.length)];
                    break;
                case 2:
                    data[i]=other_chars[selector.nextInt(other_chars.length)];
                    break;
            }
        }
        return new String(data);
    }
    // ======================================================================== //
    public static byte [] intToByteArray(int number,int len){
        return ByteBuffer.allocate(len).order(ByteOrder.BIG_ENDIAN).putInt(number).array();
    }
    // ======================================================================== //
    public static byte[] concatenateByteArrays(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
    /*// ======================================================================== //
    public static byte [] getClientHello(){
        byte [] clientHello=hexStringToByteArray("16030300d2010000ce0303");//
        clientHello=concatenateByteArrays(clientHello,getRandomData(32));
        clientHello=concatenateByteArrays(clientHello,hexStringToByteArray( "00001cdadac02bc02fc02cc030cca9cca8c" +
                "013c014009c009d002f0035000a010000899a9a0000ff0100010000000020001e00001b73697465696e746572636570742e7175616c74726963732e636f6d0017000000230000000d001400120" +
                "40308040401050308050501080606010201000500050100000000001200000010000e000c02683208687474702f312e3175500000000b00020100000a000a00085a5a001d00170018dada000100"));
        return clientHello;
    }*/
    // ======================================================================== //
    public static byte [] getClientHello(){
        byte [] clientHello=hexStringToByteArray("1603010147010001430303369f2650570de3493acf78c44f706b388130324d2eb9c2e7b95ad04aa1a032e30000acc030c02cc028c024c014c00a00a500a300a1009f006b006a0069006800390038003700360088008700860085c032c02ec02ac026c00fc005009d003d00350084c02fc02bc027c023c013c00900a400a200a0009e00670040003f003e0033003200310030009a0099009800970045004400430042c031c02dc029c025c00ec004009c003c002f009600410007c011c007c00cc00200050004c012c008001600130010000dc00dc003000a00ff0100006e0000001500130000107777772e66616365626f6f6b2e636f6d000b000403000102000a001c001a00170019001c001b0018001a0016000e000d000b000c0009000a00230000000d0020001e060106020603050105020503040104020403030103020303020102020203000f000101");
        return clientHello;
    }
    // ======================================================================== //
    public static byte [] getClientKey(){
        return hexStringToByteArray("16030300461000004241049413f40023cabd1f221f87e387a946e54d0f58625e32a13139ffc62175041ac0a796af9271943669543f50dba468b239d219a1754a40f1c6b534aa2669d645b61403030001011603030028932388ee3552a1a296ef9736c2ffc5af5673e140f954cca91be6ca8d554ec1a2bd7ff5909593315f");
    }
    // ======================================================================== //
    public static byte [] getApplicationData(int len){
        byte [] data=getRandomData(len+5);
        data[0]=(byte)0x17;data[1]=(byte)0x03;data[2]=(byte)0x03;
        data[3]= (byte) ((len)>>8 & 0xFF);
        data[4]= (byte) ((len) & 0xFF);

        return data;
    }
    // ======================================================================== //
    public static Socket handShake(Socket socket) throws IOException{

        // Get the default SSLSocketFactory
        SSLSocketFactory sf = ((SSLSocketFactory) SSLSocketFactory.getDefault());

        // Wrap 'socket' from above in a SSL socket
        InetSocketAddress remoteAddress =
                (InetSocketAddress) socket.getRemoteSocketAddress();
        SSLSocket s = (SSLSocket) (sf.createSocket(socket, remoteAddress.getHostName(), socket.getPort(), true));


        // we are a server
        s.setUseClientMode(true);

        // allow all supported protocols and cipher suites
        s.setEnabledProtocols(s.getSupportedProtocols());
       // s.setEnabledCipherSuites(s.getSupportedCipherSuites());

        // and go!

        s.startHandshake();

        socket =s;

        return socket;
    }
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    public static String bytesToHex(byte[] bytes,int offset,int len) {
        char[] hexChars = new char[len * 2];
        for (int j = 0; j < len; j++) {
            int v = bytes[j+offset] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    // ======================================================================== //
    public static int readByte(InputStream is){

        int minLen = 5;

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
        return minLen;
    }
    public static String readLine(InputStream is,byte [] data) throws IOException {
        StringBuilder sb=new StringBuilder();
        int index=0;
            while(true){
                int a=is.read(data,index,1);
                if(a==-1)break;
                if(data[index]==0x0a){
                    if(index>0 && data[index-1]==0x0d)break;
                }
                index++;
            }
        String str=new String(data,0,index+1);
        if(index>2){
            return str;
        }
        return "";
    }
    // ======================================================================== //
    public static int readByte(InputStream is,byte [] data){

        int minLen = 5;

        int rl, crl;
        int mlen = minLen;

        byte[] chunkHeader = new byte[minLen];
        crl = rl = 0;
        try {
            while (crl < minLen) {

                rl = is.read(chunkHeader, crl, minLen - crl);
                if (rl < 0) {

                    break;

                }
                crl += rl;

            }
        } catch (IOException ex) {

        }
        System.arraycopy(chunkHeader,0,data,0,mlen);
        minLen = chunkHeader[mlen - 2] & 0xff;
        minLen = (minLen << 8) | (chunkHeader[mlen - 1] & 0xff);
        byte[] b = new byte[minLen];
        crl = 0;
        try {
            while (crl < minLen) {

                rl = is.read(b, crl, minLen - crl);
                if (rl < 0) // socket close case
                {
                    break;
                }
                crl += rl;

            }
        } catch (IOException ex) {

        }
        //data=new byte[crl];
        System.arraycopy(b,0,data,mlen,b.length);
        return minLen;
    }
    public static int readByte(InputStream is,byte [] data,int len) throws Exception{
        int rl, crl,minLen;
        minLen=len;
        crl = 0;
            while (crl < minLen) {

                rl = is.read(data, crl, minLen - crl);
                if (rl < 0) // socket close case
                {
                    break;
                }
                crl += rl;

            }

        return crl;
    }

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
    public static byte[] getHttpHeader()
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

        int index = new Random().nextInt(headersArray.length);
        str=headersArray[index];
        byte[] data = str.getBytes();
        if(data.length<55){
            data=concatenateByteArrays(data,getRandomData(55-data.length));
        }

        return data;
    }

    public static int appendAttribute(byte[] data, int index, int type, int len, byte [] value){
        data[index++]=(byte)(type>>8 & 0xff);
        data[index++]=(byte)(type & 0xff);
        data[index++]=(byte)(len>>8 & 0xff);
        data[index++]=(byte)(len & 0xff);
        for(int i=0;i<len;i++)
            data[index++] = value[i];

        return index;
    }
    public static byte [] appendAttribute(int type, int len, byte [] value){
        byte [] data=new byte[4];
        data[0]=(byte)(type>>8 & 0xff);
        data[1]=(byte)(type & 0xff);
        data[2]=(byte)(len>>8 & 0xff);
        data[3]=(byte)(len & 0xff);
        data=Functions.concatenateByteArrays(data,value);
        return data;
    }
    public static byte [] appendAttribute(int type, int len){
        byte [] data=new byte[4];
        data[0]=(byte)(type>>8 & 0xff);
        data[1]=(byte)(type & 0xff);
        data[2]=(byte)(len>>8 & 0xff);
        data[3]=(byte)(len & 0xff);
        data=Functions.concatenateByteArrays(data,Functions.getRandomData(len));
        return data;
    }


}
