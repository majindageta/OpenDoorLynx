package com.majin.officedoor;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends Activity {

    private TextView resultText;
    private ProgressBar progressBar;
    private ImageButton mainButton;

    private boolean enableButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enableButton = true;

        resultText = (TextView) findViewById(R.id.main_text_result);

        progressBar  = (ProgressBar) findViewById(R.id.main_progress_bar);
        progressBar.setIndeterminate(true);

        mainButton = (ImageButton) findViewById(R.id.main_open_button);

        stopRequest();
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRequest();
                if (enableButton)
                    callSimpleServer();
            }
        });
    }

    private void callSimpleServer() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Registry.URL;
        enableButton = false;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        stopRequest();
                        if (response != null && response.contains("open")) {
                            mainButton.setImageResource(R.drawable.open_green);
                            resultText.setText("Door opened");
                        } else {
                            mainButton.setImageResource(R.drawable.open_red);
                            resultText.setText("Door closed");
                        }

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mainButton.setImageResource(R.drawable.open);

                            }
                        }, 4000);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultText.setText("That didn't work!");
                stopRequest();
                mainButton.setImageResource(R.drawable.open_red);
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void startRequest() {
        resultText.setVisibility(View.VISIBLE);

        mainButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void stopRequest() {
        mainButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        enableButton = true;
    }
}
