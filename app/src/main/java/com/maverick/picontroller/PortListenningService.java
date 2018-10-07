package com.maverick.picontroller;

import android.app.IntentService;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import static com.maverick.picontroller.MainActivity.TAG;

/**
 * Created by Maverick on 03/10/2018.
 */

public class PortListenningService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     *
     */
    Server server;
    String portStatus;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */
    public PortListenningService() {
        super("Hello Intent Service");
    }


    @Override
    protected void onHandleIntent(Intent workIntent) {

        Log.d(TAG, "Handling Service");

        NotificationCompat.Builder protectBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("YOUR HOME IS PROTECTED");


       startForeground(2, protectBuilder.build());
        Log.d(TAG, "Home Protected Notification");
        checkForIntruder();

    }


    public void checkForIntruder() {

        server = new Server(this);
        Log.d(TAG, "Server initialized");
        Log.d(TAG, "Address: " + server.getIpAddress() + ":" + server.getPort());

//        while (true) {
//            Log.d(TAG, "Entered service loop");
//            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//            String url = "http://192.168.43.253:8000/GPIO/18/value";
//
//            // Request a string response from the provided URL.
//            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            // Display the first 500 characters of the response string.
//                            portStatus = response;
//                            Log.d(TAG, "Response has been gotten and it is: " + response);
//
//
//                            Log.d(TAG, "This is the latest Port Status: " + portStatus);
//                            if (Objects.equals(portStatus, "1")) {
//                                tb.setChecked(true);
//
//                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
//                                long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
//                                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//                                NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
//                                        .setSmallIcon(R.mipmap.ic_launcher)
//                                        .setContentTitle("INTRUDER ALERT!!!")
//                                        .setContentText("There is an intruder in your house, click to view")
//                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
//                                        .setFullScreenIntent(pendingIntent, true)
//                                        .setContentIntent(pendingIntent)
//                                        .setSound(alarmSound)
//                                        //.setStyle(new NotificationCompat.CATEGORY_EVENT)
//                                        .setLights(Color.RED, 500, 500)
//                                        .setVibrate(pattern)
//                                        .setAutoCancel(true);
//
//
//                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
//                                Log.d(TAG, "Intruder Alert Notification");
//                                // notificationId is a unique int for each notification that you must define
//                                notificationManager.notify(1, mBuilder.build());
//
//                                try {
//                                    Log.d(TAG, "Thread sleeping for 60s");
//                                    Thread.sleep(60000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//
//
//                            //                            Toast.makeText(getApplicationContext(), portStatus, Toast.LENGTH_LONG).show();
//
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.d(TAG, "Unable to get response");
//
//                        }
//                    }) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    HashMap<String, String> params = new HashMap<String, String>();
//                    params.put("Content-Type", "application/json");
//                    String creds = String.format("%s:%s", "webiopi", "raspberry");
//                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
//                    params.put("Authorization", auth);
//                    Log.d(TAG, "Auth is Done!");
//                    return params;
//
//                }
//            };
//
//            // Add the request to the RequestQueue.
//            queue.add(stringRequest);
//
//
//        }

    }
}


