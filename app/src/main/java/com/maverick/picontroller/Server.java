package com.maverick.picontroller;

/**
 * Created by Maverick on 06/10/2018.
 */

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Objects;


public class Server {
    Service activity;
    ServerSocket serverSocket;
    String TAG = "PICONTROLLERAPP";
    String message = "";
    static final int socketServerPORT = 8080;

    public Server(Service activity) {
        Log.d(TAG, "Server Activity");
        this.activity = activity;
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }

    public int getPort() {
        return socketServerPORT;
    }

    public void onDestroy() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class SocketServerThread extends Thread {

        int count = 0;
        private BufferedReader reader = null;
        public String msg;

        private PrintWriter writer = null;

        @Override
        public void run() {
            try {
                // create ServerSocket using specified port
                serverSocket = new ServerSocket(socketServerPORT);
                Log.d(TAG, "ServerSocket Created");


                while (true) {
                    // block the call until connection is created and return
                    // Socket object
                    Socket socket = serverSocket.accept();
                    Log.d(TAG, "ServerSocket Accepted");
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    Log.d(TAG, "Buffer Reader Initialized");
                    msg = reader.readLine();
                    reader.close();
                    Log.d(TAG, "Reader read from line");
                    Log.d(TAG, "Response: " + msg);
                    count++;

                    message += "#" + count + " from "
                            + socket.getInetAddress() + ":"
                            + socket.getPort() + "\n";
                    Log.d(TAG, "THis is the message: " + message);
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
                            Log.d(TAG, "Response in Run: " + msg);
                            if (Objects.equals(msg, "1")) {
                                Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);
                                long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
                                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                                NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(activity.getApplicationContext())
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setContentTitle("INTRUDER ALERT!!!")
                                        .setContentText("There is an intruder in your house, click to view")
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setFullScreenIntent(pendingIntent, true)
                                        .setContentIntent(pendingIntent)
                                        .setSound(alarmSound)
                                        //.setStyle(new NotificationCompat.CATEGORY_EVENT)
                                        .setLights(Color.RED, 500, 500)
                                        .setVibrate(pattern)
                                        .setAutoCancel(true);


                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity.getApplicationContext());
                                Log.d(TAG, "Intruder Alert Notification");
                                // notificationId is a unique int for each notification that you must define
                                notificationManager.notify(1, mBuilder.build());
                                break;
                                //activity.msg.setText(message);
//                                try {
//                                    Log.d(TAG, "Thread Sleeping");
//                                    msg = "0";
//                                    Log.d(TAG, "Response se to null");
//                                    Thread.sleep(15000);
//
//                                } catch (InterruptedException e) {
//                                    Log.d(TAG, "Error in Sleeping");
//                                    e.printStackTrace();
//                                }
                            }
                        }
//                    });

//                    SocketServerReplyThread socketServerReplyThread =
//                            new SocketServerReplyThread(socket, count);
//                    socketServerReplyThread.run();



            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


//    private class SocketServerReplyThread extends Thread {
//
//        private Socket hostThreadSocket;
//        int cnt;
//
//        SocketServerReplyThread(Socket socket, int c) {
//            hostThreadSocket = socket;
//            cnt = c;
//        }
//
////        @Override
////        public void run() {
////            OutputStream outputStream;
////            String msgReply = "Hello from Server, you are #" + cnt;
////
////            try {
////                outputStream = hostThreadSocket.getOutputStream();
////                PrintStream printStream = new PrintStream(outputStream);
////                printStream.print(msgReply);
////                printStream.close();
////
////                message += "replayed: " + msgReply + "\n";
////
////                activity.runOnUiThread(new Runnable() {
////
////                    @Override
////                    public void run() {
////                        //activity.msg.setText(message);
////                        Log.d(TAG,"Replay message: " + message );
////                    }
////                });
////
////            } catch (IOException e) {
////                // TODO Auto-generated catch block
////                e.printStackTrace();
////                message += "Something wrong! " + e.toString() + "\n";
////                Log.d(TAG,"Error message: " + message );
////            }
////
////            activity.runOnUiThread(new Runnable() {
////
////                @Override
////                public void run() {
////                    //activity.msg.setText(message);
////                    Log.d(TAG,"This is the message2: " + message );
////                }
////            });
////        }
//
//    }

    public String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress
                            .nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "Server running at : "
                                + inetAddress.getHostAddress();
                    }
                }
            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
        Log.d(TAG,"IP Address: " + ip );
        return ip;
    }
}