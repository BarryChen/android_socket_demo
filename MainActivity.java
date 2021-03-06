/*
服务端，没有经过测试
用与APP中。
*/

package com.example.localsocketserver;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Activity;
import android.net.Credentials;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private ServerThread mThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startServer();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        stopServer();
    }

    private void startServer(){
        stopServer();
        mThread = new ServerThread();
        mThread.start();
    }
    private void stopServer(){
        if(mThread != null){
            mThread.exit();
            mThread = null;
        }
    }
    private class ServerThread extends Thread{
        private boolean exit = false;
        private int port = 3333;

        public void run() {  
            LocalServerSocket server = null;  
            BufferedReader mBufferedReader = null;  
            PrintWriter os = null;  
            String readString =null;  
            try {  
                server = new LocalServerSocket("com.repackaging.localsocket");        
                while (!exit) {  
                    LocalSocket connect = server.accept();  
                    Credentials cre = connect.getPeerCredentials();
                    Log.i(TAG,"accept socket uid:"+cre.getUid()); 
                    new ConnectThread(connect).start();

                }     
            } catch (IOException e) {  
                e.printStackTrace();  
            }finally{

                try {  
                    mBufferedReader.close();  
                    os.close();  
                    server.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        } 

        public void exit(){
            exit = true;
            this.interrupt();
            try {
                this.wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    class ConnectThread extends Thread{
        LocalSocket socket = null;
         BufferedReader mBufferedReader = null;  
         InputStream input = null;
         PrintWriter os = null;
         String readString =null;
        public ConnectThread(LocalSocket socket){    
            this.socket = socket;
        }

        @Override
        public void run(){
            try {
                input = socket.getInputStream();
                byte[] buffer = new byte[1024];
                int len = input.read(buffer);
                 Log.d(TAG,"mBufferedReader:"+new String(buffer,0,len)); 

               /* while((readString=mBufferedReader.readLine())!=null){  
                    //if(readString.equals("finish")) 
                    //  break;  
                    Log.d(TAG,"server recive :"+readString);  
                    break;
                }  */
                os = new PrintWriter(socket.getOutputStream());  
                os.println("this is server\0");  
                os.flush();  
                os.close();
                socket.close();
                Log.d(TAG,"server send over");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

}
