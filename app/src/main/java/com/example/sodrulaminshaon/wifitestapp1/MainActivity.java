package com.example.sodrulaminshaon.wifitestapp1;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sodrulaminshaon.wifitestapp1.etisalatfree.MultiSocket;
import com.example.sodrulaminshaon.wifitestapp1.etisalatfree.PacketSender;
import com.example.sodrulaminshaon.wifitestapp1.etisalatfree.Test;
import com.example.sodrulaminshaon.wifitestapp1.tcp.TCPClient;
import com.example.sodrulaminshaon.wifitestapp1.wifitest.UdpTest;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    Button start;
    Button stop;
    TextView sentTextView;
    TextView receivedTextView;
    EditText ipEditText;
    EditText headerNumberEditText;
    public EditText headerEditText;
    EditText portEditText;
    EditText packetCountEditText;
    TextView filePathTextView;
    TextView titleTextView;
    public int sentCount=0,receivedCount=0;
    public boolean running;
    public InetAddress address,address1;
    public int port,packetPerSocket,len,headerNumber;
    public String header,filePath;
    private static final int FILE_SELECT_CODE = 1;
    public TextView networkDetails;
    ConnectivityChecker netDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.copy_activity_main);

        start=(Button)findViewById(R.id.button);
        stop=(Button)findViewById(R.id.button2);

        sentTextView=(TextView)findViewById(R.id.sent);
        receivedTextView=(TextView)findViewById(R.id.received);

        ipEditText=(EditText)findViewById(R.id.ip);
        headerNumberEditText=(EditText)findViewById(R.id.headerNumber);
        headerEditText=(EditText)findViewById(R.id.header);
        portEditText=(EditText)findViewById(R.id.port);
        packetCountEditText=(EditText)findViewById(R.id.packetCount);
        //filePathTextView=(TextView) findViewById(R.id.path);
        ///titleTextView=(TextView)findViewById(R.id.title);
        networkDetails = (TextView)findViewById(R.id.title);

        running=false;
        sentCount=0;receivedCount=0;
        filePath=null;
        InetAddress [] addresses=new InetAddress[14];
        try{
            addresses[0]=InetAddress.getByName("72.249.184.143");
            addresses[1]=InetAddress.getByName("181.41.196.251");
            addresses[2]=InetAddress.getByName("65.99.254.233");
            addresses[3]=InetAddress.getByName("181.41.196.242");
            addresses[4]=InetAddress.getByName("181.41.196.45");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        netDetector = null;
        //Button fileChooserButton=(Button)findViewById(R.id.fileChooser);
        //new ConnectivityChecker(MainActivity.this).start();
        new Thread(new screenController()).start();
        //new Thread(new ExactSkype(MainActivity.this)).start();
        //new Thread(new UdpHeaderChecker(MainActivity.this)).start();
        //new Thread(new TlsCommunication(MainActivity.this)).start();
        //new Thread(new TlsMultisocket(MainActivity.this)).start();
        //new Thread(new UdpMultiSocket(MainActivity.this,443,443)).start();
        //new Thread(new TcpFlowControl(MainActivity.this)).start();
        //new Thread(new TcpHeaderChecker(MainActivity.this)).start();
        //new Thread(new QuicImplementaion(MainActivity.this)).start();
        //new Thread(new QuicImplementaion(MainActivity.this,addresses)).start();
        //new Thread(new TcpMultiSocket(MainActivity.this)).start();
        //new Thread(new ClipChampVideoRecording(MainActivity.this)).start();
        //new Thread(new FileHeaderChecker(MainActivity.this)).start();
        /*fileChooserButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                chooseFromLocalDrive();
                //test();
            }
        });*/
        //new Thread(new BootIMSimulation(MainActivity.this)).start();
        //new Thread(new Base64Encoding(MainActivity.this)).start();
        //new Thread(new FacebookKnock(MainActivity.this)).start();
        //new Thread(new DuFreeTry(MainActivity.this)).start();
        //new Thread(new DomainFronting(MainActivity.this)).start();
        //new Thread(new HttpsClient()).start();
        /*MultiSocket ms=new MultiSocket(MainActivity.this);
        ms.start();*/
       /* PortFinder pf=new PortFinder(MainActivity.this);
        pf.start();*/
        /*Test test=new Test(MainActivity.this);
        test.start();*/
        //new Thread(new RandomHeaderCreatorUDP(MainActivity.this)).start();
        new UdpTest(MainActivity.this).start();
        //new PacketSender(MainActivity.this).start();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(filePath==null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("File Not Seleted");
                    alertDialog.setMessage("Please choose a file.");
                    alertDialog.show();
                    return;
                }*/
                if(netDetector == null)
                {
                    netDetector = new ConnectivityChecker(MainActivity.this);
                    netDetector.start();
                }
                port=Integer.parseInt(portEditText.getText().toString());
                headerNumber=Integer.parseInt(headerNumberEditText.getText().toString());
                //port=24434;
                packetPerSocket=Integer.parseInt(packetCountEditText.getText().toString());
                header=headerEditText.getText().toString();
                try {
                    address=InetAddress.getByName(ipEditText.getText().toString());
                    //address1=InetAddress.getByName(headerEditText.getText().toString());
                    running=true;

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //new Test(MainActivity.this).start()
                // new TCPClient(MainActivity.this).start();

            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                running=false;
                //System.exit(0);
            }
        });

    }

    private class screenController implements Runnable{
        int i=0,j;
        @Override
        public void run() {
            while (true){
                i=sentCount;
                j=receivedCount;
                runOnUiThread(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sentTextView.setText(i+"");
                        receivedTextView.setText(j+"");
                    }
                }));
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void chooseFromLocalDrive() {
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath());
        //Uri uri = Uri.parse(getFilesDir().getPath());

        Intent intent = new Intent(MainActivity.this, LocalFileImportActivity.class);
        intent.putExtra(LocalFileImportActivity.START_PATH, uri + "/");

//        Toast.makeText(getActivity(), uri.toString(), Toast.LENGTH_SHORT).show();
        intent.putExtra(LocalFileImportActivity.CAN_SELECT_DIR, true);

        startActivityForResult(intent, FILE_SELECT_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == FILE_SELECT_CODE) {
                filePath = getLocalDriveFilePath(data);
                System.out.println("\n\n***************************** File path: " + filePath + "******************************************\n\n");
                filePathTextView.setText(filePath);
            }
        }
    }

    private String getLocalDriveFilePath(Intent intent) {
        if (intent == null || !intent.hasExtra("RESULT_PATH")) {
            return "";
        }
        return intent.getStringExtra("RESULT_PATH");
    }
}
