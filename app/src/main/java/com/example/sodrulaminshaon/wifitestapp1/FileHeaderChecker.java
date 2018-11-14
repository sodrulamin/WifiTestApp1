package com.example.sodrulaminshaon.wifitestapp1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sodrul Amin Shaon on 9/26/2017.
 */

public class FileHeaderChecker implements Runnable {
    MainActivity activity;
    ArrayList<String> header=new ArrayList<>();
    int checkedYet;
    private boolean running;
    FileHeaderChecker(MainActivity a){
        activity=a;
        checkedYet=0;
        running=true;
    }

    @Override
    public void run() {
        while (!activity.running);
        ReadFile();
        activity.runOnUiThread(new Thread(new Runnable() {
            @Override
            public void run() {
                activity.titleTextView.setText("Checking .......... ");
            }
        }));
        Random random=new Random();
        for(int i=0;i<header.size();i++){
        //while(running){
            checkHeader(header.get(i));
            //checkHeader(Functions.createVisibleCharArray(50+random.nextInt(50)));
            checkedYet++;
            activity.runOnUiThread(new Thread(new Runnable() {
                @Override
                public void run() {
                    activity.sentTextView.setText(checkedYet+"");
                }
            }));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            DatagramSocket socket=new DatagramSocket(2434+checkedYet);
            for (int i=0;i<10;i++){
                socket.send(createPacket(activity.address,activity.port,200,"0000000000000000000000000000000000000000000000000000000000000000"));
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        activity.runOnUiThread(new Thread(new Runnable() {
            @Override
            public void run() {
                activity.titleTextView.setText("CONGRATULATION");
                activity.sentTextView.setText("FINISHED");
            }
        }));

    }

    public void halt(){
        running=false;
    }

    private void ReadFile(){
        // The name of the file to open.
        String fileName=activity.filePath;

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                if(line.length()<1)continue;
                //System.out.println(line);
                header.add(line);
            }

            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
            ex.printStackTrace();
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
    }
    private void checkHeader(String header){
        try {
            DatagramSocket socket=new DatagramSocket(2434+checkedYet);
            int i=0;
            while (true){
                Thread.sleep(activity.packetSize);
                if(!activity.running)continue;
                socket.send(createPacket(activity.address,activity.port,activity.packetSize,header));
                //System.out.println("Packet sent len 200 count ------------ "+ i);
                i++;
                if(i>=100)break;
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static DatagramPacket createPacket(InetAddress address, int port, int len,String h){
        byte [] data,header;
        //header=Functions.hexStringToByteArray(h);
        header=h.getBytes();
        data=Functions.getRandomData(len);
        data=Functions.concatenateByteArrays(header,data);
        return new DatagramPacket(data,data.length,address,port);
    }
}
