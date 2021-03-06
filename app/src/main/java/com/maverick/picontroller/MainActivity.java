package com.maverick.picontroller;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.ToggleButton;
import android.widget.VideoView;
//import co.teubi.raspberrypi.io.*;


public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,GPIO.PortUpdateListener, GPIO.ConnectionEventListener {

    public static final String TAG = "PICONTROLLERAPP";
    CheckBox cb;
    public static ToggleButton tb;
    // "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4"
    //"http://192.168.43.253:8160
    public String vidAddress = "http://192.168.43.253:8160";
    VideoView vidView;
    private ProgressBar progressBar;
    private GPIO gpioPort;
    public String portStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

//        server = new Server(this);
//        Log.d(TAG, "Server initialized");
//        Log.d(TAG, "Results: " + server.getIpAddress() + ":" + server.getPort());
        startService();
        videoPlayer();
        this.gpioPort = new GPIO(
                new GPIO.ConnectionInfo(
                        "192.168.43.253",
                        8000,
                        "webiopi",
                        "raspberry"
                )
        );
        Log.d(TAG, "GPIO port created");

        cb = (CheckBox) findViewById(R.id.chkIsInput);

        cb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cb.isChecked()) {
                    gpioPort.setFunction(18, PORTFUNCTION.OUTPUT);
                    Log.d(TAG, "GPIO port set to output");
                } else {
                    gpioPort.setFunction(18, PORTFUNCTION.INPUT);
                    Log.d(TAG, "GPIO port set to input");
                }
            }
        });

        tb = (ToggleButton) findViewById(R.id.btnPort);

        tb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Only change port value if the port is an "output"
                if (!cb.isChecked()) {
                    if (!tb.isChecked()) {
                        gpioPort.setValue(18, 0);
                        tb.setBackgroundColor(Color.parseColor("#898686"));
                        Log.d(TAG, "toggle button set top 0");

                    } else {
                        gpioPort.setValue(18, 1);
                        tb.setBackgroundColor(Color.parseColor("#0DFA00"));
                        Log.d(TAG, "toggle button set top 1");

                    }
                }
            }
        });




//        // Method to stop the service
//        public void stopService(View view) {
//            stopService(new Intent(getBaseContext(), MyService.class));
//        }





        // Log.d(TAG, "Value of port 18 is: " + stat.ports.get(18).value);

        //tb.setChecked(gpioPort.getValue(18).toBool());
//    try {
//        JSONParser parser = new JSONParser("webiopi",
//                "raspberry", "192.168.43.253");
//        Log.d(TAG, "Parser parsed" );
//        JSONObject obj = parser.getJSONFromUrl("http://192.168.43.253:" + 8000 + "/GPIO/" + 18 + "/value", 8000);
//        Log.d(TAG, "JSON gotten" );
//        if (obj != null) {
//            Log.d(TAG, " URL empty" );
//        } else {
//            Log.d(TAG,"Response is: "+  obj.toString());
//            Toast.makeText(getApplicationContext(), "Response is: "+  obj.toString(), Toast.LENGTH_LONG).show();
//        }
//    } catch (JSONException e) {
//
//    } catch (Exception e) {
//
//        Log.d(TAG, "Unable to connect" );
//        Toast.makeText(getApplicationContext(), "Unable to connect", Toast.LENGTH_LONG).show();
//
//    }

//        Thread tee = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                URL url1 = null;
//                try {
//                    url1 = new URL("http://192.168.43.253:8000/GPIO/18/value");
//
//                    String userPass = "username:password";
//                    String basicAuth = "Basic " + Base64.encodeToString(userPass.getBytes(), Base64.DEFAULT);//or
//                    //String basicAuth = "Basic " + new String(Base64.encode(userPass.getBytes(), Base64.No_WRAP));
//                    HttpURLConnection urlConnection = null;
//
//                    urlConnection = (HttpURLConnection) url1.openConnection();
//                    Log.d(TAG, "Connection open");
//
//
//                    urlConnection.setRequestProperty("Authorization", basicAuth);
//
//
//                    urlConnection.connect();
//                    Log.d(TAG, "URL Connect");
//
//
//                } catch (MalformedURLException e) {
//                    Log.d(TAG, "Unable to connect/auth");
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    Log.d(TAG, "Unable to connect/auth");
//                    e.printStackTrace();
//                }
//
//            }
//        });
//        tee.start();
         //Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = "http://192.168.43.253:8000/GPIO/18/value";
//
//// Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        portStatus = response;
//                        Log.d(TAG, "Response has been gotten and it is: " + response);
//
//
//
//                        Log.d(TAG, "This is the latest Port Status: " + portStatus);
//                        if(Objects.equals(portStatus, "1")) {
//                            tb.setChecked(true);
//                            //Create an explicit intent for an Activity in your app
//                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
//                            long[] pattern = {500,500,500,500,500,500,500,500,500};
//                            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//                            NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
//                                    .setSmallIcon(R.mipmap.ic_launcher)
//                                    .setContentTitle("INTRUDER ALERT!!!")
//                                    .setContentText("There is an intruder in your house, click to view")
//                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
//                                    .setFullScreenIntent(pendingIntent, true)
//                                    .setContentIntent(pendingIntent)
//                                    .setSound(alarmSound)
//                                   // .setStyle(new NotificationCompat.CATEGORY_EVENT)
//                                    .setLights(Color.RED, 500, 500)
//                                    .setVibrate(pattern)
//                                    .setAutoCancel(true);
//
//                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
//
//                            Log.d(TAG, "Notify about to start ");
//                            // notificationId is a unique int for each notification that you must define
//                            notificationManager.notify(1, mBuilder.build());
//                        }
//
//
//                        Toast.makeText(getApplicationContext(), portStatus, Toast.LENGTH_LONG).show();
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d(TAG, "Unable to get response");
//
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/json");
//                String creds = String.format("%s:%s", "webiopi", "raspberry");
//                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
//                params.put("Authorization", auth);
//                Log.d(TAG, "Auth is Done!");
//                return params;
//
//            }
//        };
//
//// Add the request to the RequestQueue.
//        queue.add(stringRequest);
//

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void onPortUpdated(final GPIOStatus stat) {
        runOnUiThread(new Runnable() {
            public void run() {
                // First check if the port is configured
                // as an input or output
                Log.d(TAG, "Port Update begun");
                if (stat.ports.get(18).function == PORTFUNCTION.INPUT) {

                    // Check the checkbox
                    cb.setChecked(true);


                    // If is an Input disable the button
                    tb.setEnabled(false);

                    // Set the checked state based on the current port value
                    tb.setChecked(stat.ports.get(18).value.toBool());
                } else if (stat.ports.get(18).function == PORTFUNCTION.OUTPUT) {

                    // Un-check the checkbox
                    cb.setChecked(false);


                    // If is an Output enable the button
                    tb.setEnabled(true);

                    // Set the checked state based on the current port value
                    tb.setChecked(stat.ports.get(18).value.toBool());

                } else {
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(String message) {
        Log.d(TAG, "Connection Failed");
    }

    public void startService() {
        Log.d(TAG, "Start Service");
        startService(new Intent(this, PortListenningService.class));

    }

    public void videoPlayer() {
        vidView = (VideoView) findViewById(R.id.myVideo);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        Uri vidUri = Uri.parse(vidAddress);
        vidView.setVideoURI(vidUri);
        MediaController vidControl = new MediaController(this);
        vidControl.setAnchorView(vidView);
        vidView.setMediaController(vidControl);
        vidView.setOnPreparedListener(this);
        vidView.setOnErrorListener(this);


//        Log.d(TAG, "Is Mediaplayer playing: " + vidView.isPlaying());
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(TAG, "Mediaplayer prepared" );

        mp.start();
        Log.d(TAG, "Mediaplayer started" );
        Log.d(TAG, "Is Mediaplayer playing: " + mp.isPlaying());
        progressBar.setVisibility(View.GONE);


    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d(TAG, "Mediaplayer failed to prepare" );
        progressBar.setVisibility(View.GONE);
        mp.reset();

        Log.d(TAG, "Mediaplayer Reset" );
        return false;

    }
}

