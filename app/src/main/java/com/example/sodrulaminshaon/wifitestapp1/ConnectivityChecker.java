package com.example.sodrulaminshaon.wifitestapp1;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Random;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Sodrul Amin Shaon on 9/7/2017.
 */

public class ConnectivityChecker extends Thread {
    MainActivity activity;
    String net,NetworkName;
    int checkedYet,receivedCount=0;
    String [] infovalue;//,apivalue;
    siteChecker [] siteCheckerlist;

    ConnectivityChecker(MainActivity ac){
        activity=ac;
        net="";
        checkedYet=0;
        NetworkName="";
        infovalue=new String[2];
        for(int i=0;i<infovalue.length;i++){
            infovalue[i]=null;
        }
    }
    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    //@Override
    public void run1(){
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(CONNECTIVITY_SERVICE);
        //activity.network = 103;
        NetworkInfo netInfo = manager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            NetworkName = getActiveNetworkName();
            net = NetworkName + " " + net;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                activity.networkDetails.setText(net);
            }
        });
    }
    @Override
    public void run() {
        long startTime=System.currentTimeMillis();
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = manager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            //System.out.println("Network activity detected");
            String name=netInfo.getTypeName();
            net+=name;
            checkSocialList();
            getIpInfo getIpInfot=new getIpInfo("http://ip-api.com/json",0);
            getIpInfot.start();
            getIpInfo getIpInfot1=new getIpInfo("http://ipinfo.io/json",1);
            getIpInfot1.start();
            long time=System.currentTimeMillis();
            while(System.currentTimeMillis()<(time+5000) && infovalue[0]==null && infovalue[1]==null) {
                try {Thread.sleep(200);} catch (InterruptedException e) {}
            }
            /*getIpInfot.Stop();
            getIpInfot1.Stop();
            for(int i=0;i<siteCheckerlist.length;i++)siteCheckerlist[i].Stop();*/
            if(infovalue[0]!=null) {
                int n = infovalue[0].indexOf("org") + 6;
                infovalue[0] = infovalue[0].substring(n);
                n = infovalue[0].indexOf("\"");
                infovalue[0] = infovalue[0].substring(0, n);
                if(infovalue[0].contains("Emirates Telecommunications Corporation"))net+="\nEtisalat-ae";
                else if(infovalue[0].contains("Emirates Integrated Telecommunications Company"))net+="\nDu-ae";
                else net+="\n"+infovalue[0];
            }
            else if(infovalue[1]!=null) {
                int n = infovalue[1].indexOf("org") + 7;
                infovalue[1] = infovalue[1].substring(n);
                n = infovalue[1].indexOf("\"");
                infovalue[1] = infovalue[1].substring(0, n);
                if(infovalue[1].contains("Emirates Telecommunications Corporation"))net+="\nEtisalat-ae";
                else if(infovalue[1].contains("Emirates Integrated Telecommunications Company"))net+="\nDu-ae";
                else net+="\n"+infovalue[1];
            }
            else{
                ///checking for social or free.
                net="";
                if(receivedCount>0)net+="\nSocial";
                else net+="\nFree";
                NetworkName= getActiveNetworkName();
                net=NetworkName+" "+net;
            }
        }
        else {
            net+="No network activity";
        }
        /*net=net.toLowerCase();
        if(net.contains("wifi") && net.contains("etisalat-ae"))activity.network=100;
        else if(net.contains("etisalat-ae") && net.contains("mobile"))activity.network=101;
        else if(net.contains("du-ae") && net.contains("mobile"))activity.network=104;
        else if(net.contains("etisalat-ae") && net.contains("social"))activity.network=102;
        else if(net.contains("du-ae") && net.contains("social"))activity.network=105;
        else if(net.contains("etisalat-ae") && net.contains("free"))activity.network=103;
        else if(net.contains("du-ae") && net.contains("free"))activity.network=106;
        else if(net.contains("du-ae") && net.contains("wifi"))activity.network = 107;
        else if(net.contains("no network activity"))activity.network=0;
        else activity.network=404;////Unknown Network*/


        startTime=System.currentTimeMillis()-startTime;
        net+="\n"+startTime/1000;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                activity.networkDetails.setText(net);
            }
        });
    }
    private class getIpInfo extends Thread{
        String urlString;
        URL url;
        InputStream is;
        int stringCount;
        HttpURLConnection urlConnection;
        getIpInfo(String u,int i){
            urlString=u;
            stringCount=i;
        }

        @Override
        public void run(){
            try {
                url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(1000);
                urlConnection.setReadTimeout(3000);
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == 200) {
                    is = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                    String line;
                    String st = "";

                    while ((line = bufferedReader.readLine()) != null) {
                        st += line;
                    }
                    bufferedReader.close();
                    is.close();
                    infovalue[stringCount] = st;
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }

        public void Stop(){
            try{
                urlConnection.disconnect();
                urlConnection=null;
            }catch (Exception e){}
        }
    }
    private String getActiveNetworkName(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager subscriptionManager = (SubscriptionManager) activity.getApplicationContext().getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            int nDataSubscriptionId = getDefaultDataSubscriptionId(subscriptionManager);
            if (nDataSubscriptionId != SubscriptionManager.INVALID_SUBSCRIPTION_ID){
                /*if(askForPermission(Manifest.permission.READ_PHONE_STATE,111)){
                    SubscriptionInfo si = subscriptionManager.getActiveSubscriptionInfo(nDataSubscriptionId);
                    if (si != null)
                        return (si.getCarrierName().toString());
                }*/

                System.out.println("nDataSubscriptionId: "+nDataSubscriptionId);
                SubscriptionInfo si = subscriptionManager.getActiveSubscriptionInfo(nDataSubscriptionId);
                if (si != null) {
                    //String str = si.getCountryIso();
                    int mcc = si.getMcc();
                    int mnc = si.getMnc();

                    //System.out.println("str: "+str+" mcc: "+mcc+" mnc: "+mnc);
                    if((mcc == 424 || mcc == 430 || mcc == 431) && mnc == 2) return "Etisalat-ae";
                    else if(mcc == 424 && mnc == 3) return "Du-ae";
                    return (si.getCarrierName().toString());
                }

            }
        }
        else{
            TelephonyManager Tmanager = (TelephonyManager)activity.getSystemService(Context.TELEPHONY_SERVICE);
            String networkOperator = Tmanager.getNetworkOperator();
            if (!TextUtils.isEmpty(networkOperator)) {
                int mcc = Integer.parseInt(networkOperator.substring(0, 3));
                int mnc = Integer.parseInt(networkOperator.substring(3));
                //System.out.println("networkOperator: "+networkOperator+" mcc: "+mcc+" mnc: "+mnc);
                if((mcc == 424 || mcc == 430 || mcc == 431) && mnc == 2) return "Etisalat-ae";
                else if(mcc == 424 && mnc == 3) return "Du-ae";
            }
            return Tmanager.getNetworkOperatorName();
        }
        return null;
    }
    int getDefaultDataSubscriptionId(final SubscriptionManager subscriptionManager)
    {
        if (Build.VERSION.SDK_INT >= 24){
            int nDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
            if (nDataSubscriptionId != SubscriptionManager.INVALID_SUBSCRIPTION_ID)return (nDataSubscriptionId);
        }
        try{
            Class<?> subscriptionClass = Class.forName(subscriptionManager.getClass().getName());
            try{
                Method getDefaultDataSubscriptionId = subscriptionClass.getMethod("getDefaultDataSubId");
                try{
                    return ((int) getDefaultDataSubscriptionId.invoke(subscriptionManager));
                }catch (IllegalAccessException e1){
                    e1.printStackTrace();
                }
                catch (InvocationTargetException e1){
                    e1.printStackTrace();
                }
            }catch (NoSuchMethodException e1){
                e1.printStackTrace();
            }
        }catch (ClassNotFoundException e1){
            e1.printStackTrace();
        }
        return (SubscriptionManager.INVALID_SUBSCRIPTION_ID);
    }

    private void checkSocialList() {
        receivedCount = 0;
        siteCheckerlist=new siteChecker[5];
        for(int i=0;i<5;i++){
            siteCheckerlist[i]=new siteChecker("m.facebook.com");
            siteCheckerlist[i].start();
        }
    }
    private class siteChecker extends Thread{
        String host;
        InetAddress address;
        Socket socket;
        siteChecker(String site){
            host=site;
        }

        @Override
        public void run() {
            try {
                address=InetAddress.getByName(host);
                socket=new Socket(address,443);
                socket.setSoTimeout(3000);
                socket.getOutputStream().write(getDynamicFacebookClientHello());
                int n=readByte(socket.getInputStream());
                //System.out.println(host+ " "+n);
                if(n>0)receivedCount++;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            checkedYet++;
        }
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
    // ========================================================================= //
    public byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
    // ======================================================================== //
    public static byte[] concatenateByteArrays(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
    // ========================================================================= //
    public static byte[] getRandomData(int len) {
        byte[] header = new byte[len];
        new Random().nextBytes(header);
        return header;
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
    private boolean askForPermission(String permission, Integer requestCode) {
        boolean result=true;
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            }

            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
            {
                result=false;
            }

        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
                }
            });

        }
        return result;
    }


}
