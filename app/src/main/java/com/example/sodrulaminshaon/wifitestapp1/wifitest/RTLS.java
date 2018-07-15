package com.example.sodrulaminshaon.wifitestapp1.wifitest;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Sodrul Amin Shaon on 09-Jun-18.
 */

public class RTLS {
    public static Socket createSSLSocket2(InetAddress serverIP, int serverPort){
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)  {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());


            final SSLSocketFactory delegate = sslContext.getSocketFactory();
            SocketFactory factory = new SSLSocketFactory() {
                @Override
                public Socket createSocket(String host, int port)
                        throws IOException, UnknownHostException {

                    InetAddress addr = InetAddress.getByName(host);
                    injectHostname(addr, host);
                    return delegate.createSocket(addr, port);
                }
                @Override
                public Socket createSocket(InetAddress host, int port)
                        throws IOException {

                    return delegate.createSocket(host, port);
                }
                @Override
                public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
                        throws IOException, UnknownHostException {

                    return delegate.createSocket(host, port, localHost, localPort);
                }
                @Override
                public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort)
                        throws IOException {

                    return delegate.createSocket(address, port, localAddress, localPort);
                }
                private void injectHostname(InetAddress address, String host) {
                    try {
                        Field field = InetAddress.class.getDeclaredField("hostName");
                        field.setAccessible(true);
                        field.set(address, host);
                    } catch (Exception ignored) {
                    }
                }
                @Override
                public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {

                    injectHostname(s.getInetAddress(), host);
                    return delegate.createSocket(s, host, port, autoClose);
                }
                @Override
                public String[] getDefaultCipherSuites() {
                    return delegate.getDefaultCipherSuites();
                }
                @Override
                public String[] getSupportedCipherSuites() {
                    return delegate.getSupportedCipherSuites();
                }
            };



            // Create an ssl socket factory with our all-trusting manager
//            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

//            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(serverIP, serverPort);
            SSLSocket sslSocket = (SSLSocket)factory.createSocket(serverIP, serverPort);
            sslSocket.startHandshake();
            return (Socket) sslSocket;


        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("reve", ex.toString());
        }
        return null;
    }
    public static Socket createSSLSocket(InetAddress serverIP, int serverPort){
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)  {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(serverIP, serverPort);
            OutputStream os = sslSocket.getOutputStream();
            os.flush();
            sslSocket.startHandshake();
            return (Socket) sslSocket;


        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("reve", ex.toString());
        }
        return null;
    }





    /*public static Socket sslSocket(final String serverIP, final int serverPort, final int count, final int delay, final int size){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = createSSLSocket(serverIP, serverPort);
//                    receiverThread(socket, count, size, delay);
//                    senderThread(socket, count, size, delay);

                } catch (Exception e) {
//                    System.out.println(e);
                    Log.e("reve", e.toString());
                }
            }
        }).start();
    }*/
}
