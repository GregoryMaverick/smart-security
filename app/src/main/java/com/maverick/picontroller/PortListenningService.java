package com.maverick.picontroller;

import android.app.IntentService;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

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

    String portStatus;
    public PortListenningService(String name) {
        super(name);
    }

    @Override
        protected void onHandleIntent(Intent workIntent) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.43.253:8000/GPIO/18/value";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        portStatus = response;
                        Log.d(TAG, "Response is: " + response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "That didn't work!");
                        Toast.makeText(getApplicationContext(), "That didn't work!", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                String creds = String.format("%s:%s", "webiopi", "raspberry");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                Log.d(TAG, "Auth is Done!");
                return params;

            }
        };

// Add the request to the RequestQueue.
        queue.add(stringRequest);



        }

}
