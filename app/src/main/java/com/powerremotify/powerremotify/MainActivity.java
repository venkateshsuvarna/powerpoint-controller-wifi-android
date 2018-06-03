package com.powerremotify.powerremotify;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    Button button_startpresentation;
    Button button_stoppresentation;
    Button button_previousslide;
    Button button_nextslide;
    Button button_gotoparticularslide;

    EditText editTextSlideNumber;
    TextView textViewTotalSlideNumber;
    String msg;
    String numberofslides = "abc";
    int currentSlideNumber = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_startpresentation = (Button)findViewById(R.id.button_startpresentation);
        button_stoppresentation = (Button)findViewById(R.id.button_stoppresentation);
        button_previousslide = (Button)findViewById(R.id.button_gotopreviousslide);
        button_nextslide = (Button)findViewById(R.id.button_gotonextslide);
        button_gotoparticularslide = (Button)findViewById(R.id.button_gotoparticularslide);

        editTextSlideNumber = (EditText)findViewById(R.id.editTextSlideNumber);
        textViewTotalSlideNumber = (TextView)findViewById(R.id.textViewTotalSlideNumber);

        editTextSlideNumber.setText("1");

        Thread myThread = new Thread(new MyServerThread());
        myThread.start();

        button_startpresentation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = "start";
                send(msg);
                Toast.makeText(getApplicationContext(),"start is sent",Toast.LENGTH_SHORT).show();
            }
        });
        button_stoppresentation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = "stop";
                send(msg);
                Toast.makeText(getApplicationContext(),"stop is sent",Toast.LENGTH_SHORT).show();
            }
        });
        button_previousslide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = "prev";
                send(msg);

                if(currentSlideNumber >= 2){
                    currentSlideNumber --;
                    editTextSlideNumber.setText(Integer.toString(currentSlideNumber));
                }

                Toast.makeText(getApplicationContext(),"prev is sent",Toast.LENGTH_SHORT).show();
            }
        });
        button_nextslide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = "next";
                send(msg);

                if(currentSlideNumber <= Integer.parseInt(textViewTotalSlideNumber.getText().toString()) - 1){
                    currentSlideNumber ++;
                    editTextSlideNumber.setText(Integer.toString(currentSlideNumber));
                }

                Toast.makeText(getApplicationContext(),"next is sent",Toast.LENGTH_SHORT).show();
            }
        });
        button_gotoparticularslide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = editTextSlideNumber.getText().toString();
                Toast.makeText(getApplicationContext(),msg+" is sent",Toast.LENGTH_SHORT).show();
                send(msg);
            }
        });

    }

    class MyServerThread implements Runnable{

        Socket s;
        ServerSocket ss;
        InputStreamReader isr;
        BufferedReader bufferedReader;
        String message;

        Handler h = new Handler();

        @Override
        public void run() {
            try{
                ss = new ServerSocket(7801);
                while (true){
                    s = ss.accept();
                    isr = new InputStreamReader(s.getInputStream());
                    bufferedReader = new BufferedReader(isr);
                    message = bufferedReader.readLine();

                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Message : "+message,Toast.LENGTH_LONG).show();
                            textViewTotalSlideNumber.setText(message);
                        }
                    });

                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public void send(String message){
        MessageSender messageSender = new MessageSender();
        messageSender.execute(message);
    }
}
