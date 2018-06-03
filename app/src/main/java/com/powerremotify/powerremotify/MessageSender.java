package com.powerremotify.powerremotify;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Venkatesh Suvarna on 11/19/2017.
 */

public class MessageSender extends AsyncTask<String,Void,Void> {

    Socket s;
    DataOutputStream dos;
    PrintWriter pw;

    @Override
    protected Void doInBackground(String... voids) {

        String message = voids[0];
        try{
            //s = new Socket("192.168.43.67",61628); //
            s = new Socket("192.168.43.252",63981); //Put IP Address of PC here PLLEAAASSSEEEEEE
            pw = new PrintWriter(s.getOutputStream());
            pw.write(message);
            pw.flush();
            pw.close();
            s.close();
        }
        catch (Exception e){
            Log.e("IOExcptnMsgSender",e.toString());
            e.printStackTrace();
        }


        return null;
    }
}
