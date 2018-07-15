package com.example.sodrulaminshaon.wifitestapp1.etisalatfree;

import com.example.sodrulaminshaon.wifitestapp1.Functions;
import com.example.sodrulaminshaon.wifitestapp1.MainActivity;

import java.net.DatagramPacket;

/**
 * Created by Sodrul Amin Shaon on 03-May-18.
 */

public class PingTest extends Thread {
    String packetStr = "272a70061f2f6b463422533225324d75633404835c47168540366d1a4f024e2e04325911654b406f4677023532363f1a6f344140146a8429487534147838215c802909864f086e3332781a4238094c8038501e544023246a34186d8745077c8434675e55824d7d1377633e412d7a5676051a600116037b45570109890c7a25b47606592f4208011330056f255d464a2e0833507a7b7232103909766e2644b52442768dbe08648f427d5d";
    String regStr = "3d2f3f1f0c49666a586d36776f18776c5b7e7940351907483a638143593f4401293b75294e5b05061c7c14665183562f494e0674093f4e5514474275531a875f1d67402e090e86760c7a18682725082271806c78344b3b4d673d57465e7c754f2725301586466b626f6a782d5974565b605e05445e2e47586e737303082936700950b2726f0f7034243125147d6a33362849196c1ebe62187f7a3e79037e001146080b315e10d1622a49720038038c38492e255c2a2f5b6d32c92d3439413e6e8497e2c9";
    byte [] data = Functions.hexStringToByteArray(packetStr);
    byte [] regData = Functions.hexStringToByteArray(regStr);
    MainActivity activity;
    public DatagramPacket packet,regPacket;
    public PingTest(MainActivity a){
        activity = a;
        regStr = "4c205541363e167167841c1d02443f3f674a76056f48672d213641540c284d0a6f325d3651224e12460321613f6c3c41827663417e773b6c5a1d5b42546f3d6782376e777c2a170e48110b12655f737d851a06511914736e081e454d5486843e866e6f29390251641e651273630321495ebd111741790c0d567f7b0f3f755e1419014c5e4315793d5906653b5b630056144c682532666a7cb558516f4f7a31221f49667659656e28c17e2f3b7120411dc2b9e3de00";
        packetStr = "272a70061f2f6b463422533225324d75633404835c47168540366d1a4f024e2e04325911654b406f4677023532363f1a6f344140146a8429487534147838215c802909864f086e3332781a4238094c8038501e544023246a34186d8745077c8434675e55824d7d1377633e412d7a5676051a600116037b45570109890c7a25b47606592f4208011330056f255d464a2e0833507a7b7232103909766e2644b52442768dbe08648f427d5d";
        data = Functions.hexStringToByteArray(packetStr);
        regData = Functions.hexStringToByteArray(regStr);
        packet = new DatagramPacket(data,data.length);
        regPacket = new DatagramPacket(regData,regData.length);
    }

    @Override
    public void run(){

    }
    public DatagramPacket getRegPacket(){
        regPacket.setAddress(activity.address);
        regPacket.setPort(activity.port);
        return regPacket;
    }
    public DatagramPacket getPingPacket(){
        packet.setAddress(activity.address);
        packet.setPort(activity.port);
        return packet;
    }

}
