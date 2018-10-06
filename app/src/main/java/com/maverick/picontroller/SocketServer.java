
package com.maverick.picontroller;

/**
 * Created by Maverick on 05/10/2018.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;




public class SocketServer extends Service {

    String TAG = "PICONTROLLERAPP";
    /** Socket port */

    private static final int PORT = 6666;

    /** The Socket service */

    private ServerSocket server = null;

    /** The Socket connection pool */

    private ExecutorService mExecutorService = null; // thread pool



    @Override

    public IBinder onBind(Intent intent) {

        return null;

    }



    @Override

    public void onCreate() {
        Log.d(TAG, "OnCreate method" );

        new Thread(new Runnable() {

            @Override

            public void run() {

                creatSocket();

            }

        }).start();

    }



    /**

     * To create a Socket service

     */

    public void creatSocket() {

        try {

            server = new ServerSocket(PORT);

            mExecutorService = Executors.newCachedThreadPool();

            while (true) {

                Socket client = server.accept();

                mExecutorService.execute(new SockectService(client));

                Log.d(TAG, "Creating Socket" );

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }



    class SockectService implements Runnable {

        Socket socket = null;

        private BufferedReader reader = null;

        private PrintWriter writer = null;

        /** The client sends a message */

        private String msg = null;



        public SockectService(Socket socket) {

            this.socket = socket;

            try {

                reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

            } catch (IOException e) {

                e.printStackTrace();

            }

        }



        @Override

        public void run() {

            while (!socket.isClosed() && socket.isConnected()) {

                try {

                    if ((msg = reader.readLine()) != null && msg.toString().length() > 0) {

                        Log.d(TAG, "The received data is: " + msg);

                        this.sendMsgToClient();

                    } else {
                        Log.d(TAG, "No data received from server");
                    }

                } catch (Exception e) {

                    e.printStackTrace();

                    return;

                }

            }

        }



        /**

         * Send message to clients

         */

        public void sendMsgToClient() {

            try {

                writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                Log.d(TAG, "The transmitted data:" + msg);

                writer.println(msg);

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    }

}
