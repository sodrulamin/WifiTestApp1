package com.example.sodrulaminshaon.wifitestapp1.tcp;

import com.example.sodrulaminshaon.wifitestapp1.Functions;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

/**
 * Created by Sodrul Amin Shaon on 10-Sep-18.
 */

public class SSHImplementation {

    public static Random random = new Random();
    private String [] kex_algorithms = {
            "diffie-hellman-group1-sha1","diffie-hellman-group14-sha1", "curve25519-sha256",
            "curve25519-sha256@libssh.org","ecdh-sha2-nistp256","ecdh-sha2-nistp384",
            "ecdh-sha2-nistp521","diffie-hellman-group-exchange-sha256","ext-info-c",
            "diffie-hellman-group16-sha512","diffie-hellman-group18-sha512",
            "diffie-hellman-group-exchange-sha1","diffie-hellman-group14-sha256"
    };
    private String [] server_host_key_algorightms = {
            "ssh-dss","ssh-rsa","x509v3-sign-dss","x509v3-sign-rsa",
            "ssh-rsa-cert-v01@openssh.com","rsa-sha2-512","rsa-sha2-256",
            "ecdsa-sha2-nistp256-cert-v01@openssh.com","ecdsa-sha2-nistp384-cert-v01@openssh.com",
            "ecdsa-sha2-nistp521-cert-v01@openssh.com","ssh-ed25519-cert-v01@openssh.com",
            "ecdsa-sha2-nistp256","ecdsa-sha2-nistp384","ecdsa-sha2-nistp521","ssh-ed25519"
    };
    private String [] encryption_algorithms_client_to_server = {
            "aes128-cbc","3des-cbc","blowfish-cbc","cast128-cbc","twofish-cbc","arcfour",
            "aes128-ctr","aes192-ctr","aes256-ctr","arcfour256","arcfour128","aes192-cbc",
            "aes256-cbc","rijndael-cbc@lysator.liu.se","aes192-cbc",
            "chacha20-poly1305@openssh.com","aes128-gcm@openssh.com", "aes256-gcm@openssh.com"

    };
    private String [] mac_algorithms_server_to_client = {
            "hmac-md5","hmac-sha1","umac-128@openssh.com","hmac-sha2-512",
            "umac-64@openssh.com","hmac-sha2-256","hmac-sha2-512","hmac-ripemd160",
            "hmac-ripemd160@openssh.com","hmac-sha1-96","hmac-md5-96",
            "umac-64-etm@openssh.com","umac-128-etm@openssh.com","hmac-sha2-256-etm@openssh.com",
            "hmac-sha2-512-etm@openssh.com","hmac-sha1-etm@openssh.com","umac-64@openssh.com"
    };
    private String [] compression_algorithms_client_to_server = {
            "none", "zlib@openssh.com","zlib"
    };

    /*byte         SSH_MSG_KEXINIT
    byte[16]     cookie (random bytes)
    name-list    kex_algorithms
    name-list    server_host_key_algorithms
    name-list    encryption_algorithms_client_to_server
    name-list    encryption_algorithms_server_to_client
    name-list    mac_algorithms_client_to_server
    name-list    mac_algorithms_server_to_client
    name-list    compression_algorithms_client_to_server
    name-list    compression_algorithms_server_to_client
    name-list    languages_client_to_server
    name-list    languages_server_to_client
    boolean      first_kex_packet_follows
    uint32       0 (reserved for future extension)*/

    public int packetLength;
    public int paddingLen;
    public int messageCode;
    public byte [] paddingString;

    public int addIgnoreBytes(byte [] data,int offset){
        paddingLen = (Functions.random.nextInt(3)+3)*2;
        paddingString = Functions.getRandomData(paddingLen);
        messageCode = 2;
        int n = (random.nextInt(3)+2)*2;
        packetLength = n+paddingLen+2;
        int index = offset;
        data[index++] = 0x00;
        data[index++] = 0x00;
        data[index++] = (byte)((packetLength >> 8) & 0xff);
        data[index++] = (byte)(packetLength & 0xff);
        data[index++] = (byte)(paddingLen & 0xff);
        data[index++] = (byte)(messageCode & 0xff);
        for(int i=0;i<n;i++){
            data[index++] = 0x00;
        }
        for(int i=0;i<paddingLen;i++){
            data[index++] = paddingString[i];
        }
        return index;
    }
    public int addKeyExchange(byte [] data,int offset){
        paddingLen = (Functions.random.nextInt(3)+3)*2;
        paddingString = Functions.getRandomData(paddingLen);
        messageCode = 20;
        int index = offset;
        data[index++] = 0x00;
        data[index++] = 0x00;
        index+=2;
        data[index++] = (byte)(paddingLen & 0xff);
        data[index++] = (byte)(messageCode & 0xff);
        index = Functions.getRandomData(data,index,16);



        StringBuilder str = new StringBuilder();
        int n;
        //n = random.nextInt(kex_algorithms.length)+1;
        n = random.nextInt(2)+1;
        shuffle(kex_algorithms);
        for(int i=0;i<n;i++){
            str = str.append(",");
            str = str.append(kex_algorithms[i]);
        }
        n = str.length();
        data[index++] = 0x00;
        data[index++] = 0x00;
        data[index++] = (byte)(((n-1) >> 8) & 0xff);
        data[index++] = (byte)((n-1) & 0xff);
        for(int i=1;i<n;i++){
            data[index++] = (byte)str.charAt(i);
        }


        str = new StringBuilder();
        //n = random.nextInt(server_host_key_algorightms.length-3)+1+3;
        n = random.nextInt(5)+2;
        shuffle(server_host_key_algorightms);
        for(int i=0;i<n;i++){
            str = str.append(",");
            str = str.append(server_host_key_algorightms[i]);
        }
        n = str.length();
        data[index++] = 0x00;
        data[index++] = 0x00;
        data[index++] = (byte)(((n-1) >> 8) & 0xff);
        data[index++] = (byte)((n-1) & 0xff);
        for(int i=1;i<n;i++){
            data[index++] = (byte)str.charAt(i);
        }

        str = new StringBuilder();
        //n = random.nextInt(encryption_algorithms_client_to_server.length)+1;
        n = random.nextInt(5)+2;
        shuffle(encryption_algorithms_client_to_server);
        for(int i=0;i<n;i++){
            str = str.append(",");
            str = str.append(encryption_algorithms_client_to_server[i]);
        }
        n = str.length();
        for(int j=0;j<2;j++) {
            data[index++] = 0x00;
            data[index++] = 0x00;
            data[index++] = (byte) (((n - 1) >> 8) & 0xff);
            data[index++] = (byte) ((n - 1) & 0xff);
            for (int i = 1; i < n; i++) {
                data[index++] = (byte) str.charAt(i);
            }
        }


        str = new StringBuilder();
        //n = random.nextInt(mac_algorithms_server_to_client.length-3)+1+3;
        n = random.nextInt(4)+2;
        shuffle(mac_algorithms_server_to_client);
        for(int i=0;i<n;i++){
            str = str.append(",");
            str = str.append(mac_algorithms_server_to_client[i]);
        }
        n = str.length();
        for(int j=0;j<2;j++) {
            data[index++] = 0x00;
            data[index++] = 0x00;
            data[index++] = (byte) (((n - 1) >> 8) & 0xff);
            data[index++] = (byte) ((n - 1) & 0xff);
            for (int i = 1; i < n; i++) {
                data[index++] = (byte) str.charAt(i);
            }
        }

        str = new StringBuilder();
        n = random.nextInt(compression_algorithms_client_to_server.length)+1;
        shuffle(compression_algorithms_client_to_server);
        for(int i=0;i<n;i++){
            str = str.append(",");
            str = str.append(compression_algorithms_client_to_server[i]);
        }
        n = str.length();
        for(int j=0;j<2;j++) {
            data[index++] = 0x00;
            data[index++] = 0x00;
            data[index++] = (byte) (((n - 1) >> 8) & 0xff);
            data[index++] = (byte) ((n - 1) & 0xff);
            for (int i = 1; i < n; i++) {
                data[index++] = (byte) str.charAt(i);
            }
        }


        for(int i=0;i<13;i++)
            data[index++] = 0x00;

        for(int i=0;i<paddingLen;i++){
            data[index++] = paddingString[i];
        }
        packetLength = index-offset-4;
        data[offset+2] = (byte)((packetLength >> 8) & 0xff);
        data[offset+3] = (byte)(packetLength & 0xff);
        return index;
    }
    public int addDiffieHellmanKeyExInit(byte [] data,int offset){
        paddingLen = (Functions.random.nextInt(10)+3);
        paddingString = Functions.getRandomData(paddingLen);
        messageCode = 30;
        int n = random.nextInt(30)+120;
        int index = offset;
        data[index++] = 0x00;
        data[index++] = 0x00;
        index+=2;

        data[index++] = (byte)(paddingLen & 0xff);
        data[index++] = (byte)(messageCode & 0xff);


        data[index++] = 0x00;
        data[index++] = 0x00;
        data[index++] = 0x00;
        data[index++] = (byte)(n & 0xff);
        index = Functions.getRandomData(data,index,n);

        for(int i=0;i<paddingLen;i++){
            data[index++] = paddingString[i];
        }
        packetLength = index-offset-4;
        data[offset+2] = (byte)((packetLength >> 8) & 0xff);
        data[offset+3] = (byte)(packetLength & 0xff);
        return index;
    }
    public int addDiffieHellmanKeyExReply(byte [] data,int offset){
        paddingLen = (Functions.random.nextInt(10)+3);
        paddingString = Functions.getRandomData(paddingLen);
        messageCode = 31;
        int index = offset,offset2,n;
        data[index++] = 0x00;
        data[index++] = 0x00;
        index+=2;
        data[index++] = (byte)(paddingLen & 0xff);
        data[index++] = (byte)(messageCode & 0xff);

        offset2 = index;
        data[index++] = 0x00;
        data[index++] = 0x00;
        index+=2;


        String hostKeyType = server_host_key_algorightms[random.nextInt(server_host_key_algorightms.length)];
        data[index++] = 0x00;
        data[index++] = 0x00;
        n = hostKeyType.length();
        data[index++] = (byte)((n >> 8) & 0xff);
        data[index++] = (byte)(n & 0xff);
        for(int i=0;i<n;i++){
            data[index++] = (byte) hostKeyType.charAt(i);
        }

        n = random.nextInt(30)+120;
        data[index++] = 0x00;
        data[index++] = 0x00;
        data[index++] = (byte)((n >> 8) & 0xff);
        data[index++] = (byte)(n & 0xff);
        index = Functions.getRandomData(data,index,n);

        n = random.nextInt(30)+20;
        data[index++] = 0x00;
        data[index++] = 0x00;
        data[index++] = (byte)((n >> 8) & 0xff);
        data[index++] = (byte)(n & 0xff);
        index = Functions.getRandomData(data,index,n);

        n = random.nextInt(30)+120;
        data[index++] = 0x00;
        data[index++] = 0x00;
        data[index++] = (byte)((n >> 8) & 0xff);
        data[index++] = (byte)(n & 0xff);
        index = Functions.getRandomData(data,index,n);

        n = random.nextInt(30)+120;
        data[index++] = 0x00;
        data[index++] = 0x00;
        data[index++] = (byte)((n >> 8) & 0xff);
        data[index++] = (byte)(n & 0xff);
        index = Functions.getRandomData(data,index,n);

        n = index-offset2-4;
        data[offset2+2] = (byte)((n >> 8) & 0xff);
        data[offset2+3] = (byte)(n & 0xff);

        n = random.nextInt(30)+120;
        data[index++] = 0x00;
        data[index++] = 0x00;
        data[index++] = (byte)((n >> 8) & 0xff);
        data[index++] = (byte)(n & 0xff);
        index = Functions.getRandomData(data,index,n);

        n = random.nextInt(30)+50;
        data[index++] = 0x00;
        data[index++] = 0x00;
        data[index++] = (byte)((n >> 8) & 0xff);
        data[index++] = (byte)(n & 0xff);
        index = Functions.getRandomData(data,index,n);

        for(int i=0;i<paddingLen;i++){
            data[index++] = paddingString[i];
        }

        packetLength = index-offset-4;
        data[offset+2] = (byte)((packetLength >> 8) & 0xff);
        data[offset+3] = (byte)(packetLength & 0xff);
        return index;
    }
    public int addNewKeys(byte [] data,int offset){
        paddingLen = (Functions.random.nextInt(3)+3)*2;
        paddingString = Functions.getRandomData(paddingLen);
        messageCode = 21;
        //int n = (random.nextInt(3)+2)*2;
        packetLength = paddingLen+2;
        int index = offset;
        data[index++] = 0x00;
        data[index++] = 0x00;
        data[index++] = (byte)((packetLength >> 8) & 0xff);
        data[index++] = (byte)(packetLength & 0xff);
        data[index++] = (byte)(paddingLen & 0xff);
        data[index++] = (byte)(messageCode & 0xff);

        for(int i=0;i<paddingLen;i++){
            data[index++] = paddingString[i];
        }
        return index;
    }


    public static void shuffle(String [] array) {
        //if (random == null) random = new Random();
        int count = array.length;
        for (int i = count; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i));
        }
    }
    private static void swap(String[] array, int i, int j) {
        String temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private static byte [] msgS1 = "SSH-2.0-OpenSSH_5.3\r\n".getBytes();
    private static byte [] msgC1 = "SSH-1.99-3.2.9 SSH Secure Shell for Windows\r\n".getBytes();
    byte [] tempArray = new byte[2048];
    byte [] tempArray2 = new byte[2048];

    public boolean startClientHandShake(Socket socket){
        try{
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            //byte [] data = new byte[2048];
            int len;
            String str = Functions.readLine(is,tempArray);
            System.out.println("Received:"+ str);

            os.write(msgC1);
            len = createMsgC2(tempArray,0);
            os.write(tempArray,0,len);
            for(int i=0;i<1;i++)
                readSSHUnit(is,tempArray);
            len = createMsgC3(tempArray,0);
            os.write(tempArray,0,len);
            for(int i=0;i<2;i++)
                readSSHUnit(is,tempArray);
            len = createMsgC4(tempArray,0);
            os.write(tempArray,0,len);

            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public int createMsgC2(byte [] data,int offset){
        int index = offset;
        index = addIgnoreBytes(data,index);
        index = addKeyExchange(data,index);
        return index;
    }
    public int createMsgS2(byte [] data,int offset){
        int index = offset;
        index = addKeyExchange(data,index);
        return index;
    }
    public int createMsgC3(byte [] data,int offset){
        int index = offset;
        index = addIgnoreBytes(data,index);
        index = addDiffieHellmanKeyExInit(data,index);
        return index;
    }
    public int createMsgS3(byte [] data,int offset){
        int index = offset;
        index = addDiffieHellmanKeyExReply(data,index);
        index = addNewKeys(data,index);
        return index;
    }
    public int createMsgC4(byte [] data,int offset){
        int index = offset;
        index = addIgnoreBytes(data,index);
        index = addNewKeys(data,index);
        return index;
    }

    public boolean startServerHandShake(Socket socket){
        try{
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            int len = 0;
            os.write(msgS1);
            Functions.readLine(is,tempArray);
            for(int i=0;i<2;i++){
                readSSHUnit(is,tempArray);
            }
            len = createMsgS2(tempArray,0);
            os.write(tempArray,0,len);
            for(int i=0;i<2;i++){
                readSSHUnit(is,tempArray);
            }
            len = createMsgS3(tempArray,0);
            os.write(tempArray,0,len);
            for(int i=0;i<2;i++){
                readSSHUnit(is,tempArray);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public int createSSHPacket(byte [] data,int offset,int len){
        System.arraycopy(data,offset,tempArray,0,len);
        Functions.getRandomData(data, 0, 2);
        data[2] = (byte)((len >> 8) & 0xff);
        data[3] = (byte)(len & 0xff);

        data[2] = (byte) (data[2] ^ data[0]);
        data[3] = (byte) (data[3] ^ data[1]);
        System.arraycopy(tempArray,0,data,4,len);
        return len+4;
    }
    public int getDataFromSSHStream(InputStream is,byte [] data,int offset) throws Exception{
        int len = 0;
        int n = Functions.readByte(is,tempArray2,4);
        if(n == 4){
            tempArray2[2] = (byte) (tempArray2[2] ^ tempArray2[0]);
            tempArray2[3] = (byte) (tempArray2[3] ^ tempArray2[1]);
            len = (tempArray2[2] & 0xff);
            len = (len << 8) | (tempArray2[3] & 0xff);
            //System.out.println("len: "+len);
            len = Functions.readByte(is,data,len);
        }

        return len;
    }

    public int readSSHUnit(InputStream is,byte [] data) throws Exception{
        int minLen = 4;
        int rl, crl;
        int mlen = minLen;
        crl = rl = 0;
        while (crl < minLen) {
            rl = is.read(data, crl, minLen - crl);
            if (rl < 0) {
                break;
            }
            crl += rl;
        }
        minLen = data[mlen - 2] & 0xff;
        minLen = (minLen << 8) | (data[mlen - 1] & 0xff);
        //byte[] b = new byte[minLen];
        crl = 0;
        while (crl < minLen) {
            rl = is.read(tempArray2, crl, minLen - crl);
            if (rl < 0) // socket close case
            {
                break;
            }
            crl += rl;
        }
        return minLen;
    }

}
