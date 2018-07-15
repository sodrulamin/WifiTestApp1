package com.example.sodrulaminshaon.wifitestapp1;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Sodrul Amin Shaon on 11/11/2017.
 */

public class DuFreeTry implements Runnable {
    MainActivity activity;
    DuFreeTry(MainActivity a){
        activity=a;
    }
    @Override
    public void run() {
        new Thread(new Sender()).start();
    }

    private class Sender implements Runnable{

        @Override
        public void run() {
            try {
                Socket socket=new Socket(activity.address,activity.port);
                socket.setTcpNoDelay(true);
                OutputStream os=socket.getOutputStream();
                os.write(Functions.hexStringToByteArray("474554202f20485454502f312e310d0a486f73743a207777772e64752e636f6d0d0a436f6e6e656374696f6e3a206b6565702d616c6976650d0a4163636570742d4c616e67756167653a20656e2d55532c656e0d0a557067726164652d496e7365637572652d52657175657374733a20310d0a557365722d4167656e743a204d6f7a696c6c612f352e3020284c696e75783b20416e64726f696420372e303b2053414d53554e4720534d2d4a37333046204275696c642f4e524439304d29204170706c655765624b69742f3533372e333620284b48544d4c2c206c696b65204765636b6f292053616d73756e6742726f777365722f352e32204368726f6d652f35312e302e323730342e313036204d6f62696c65205361666172692f3533372e33360d0a4163636570743a20746578742f68746d6c2c6170706c69636174696f6e2f7868746d6c2b786d6c2c6170706c69636174696f6e2f786d6c3b713d302e392c696d6167652f776562702c2a2f2a3b713d302e380d0a4163636570742d456e636f64696e673a20677a69702c206465666c6174650d0a436f6f6b69653a205f67613d4741312e322e3535343538383238342e313531303036323138363b205f6769643d4741312e322e313434363830363630372e313531303036323138360d0a0d0a"));
                activity.sentCount++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
