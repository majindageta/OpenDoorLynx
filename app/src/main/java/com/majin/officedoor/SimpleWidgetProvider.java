package com.majin.officedoor;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.RemoteViews;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by Majin on 26/02/2017.
 */

public class SimpleWidgetProvider extends AppWidgetProvider {

    private RemoteViews remoteViews;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;
        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            remoteViews  = new RemoteViews(context.getPackageName(),
                    R.layout.widget);

            remoteViews.setViewVisibility(R.id.main_progress_bar_widget, View.INVISIBLE);
            if (Registry.getInstance().isButtonEnabled()) {
                callService(context);
            }
            Intent intent = new Intent(context, SimpleWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.actionButton, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    private void callService(final Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Registry.URL;

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        ComponentName thisWidget = new ComponentName(context, SimpleWidgetProvider.class);
        remoteViews.setViewVisibility(R.id.main_progress_bar_widget, View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callIntent(context, response);

                        Registry.getInstance().setButtonEnabled(false);
                        callHandler(context);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callIntent(context, "KO");
                callHandler(context);
            }
        });
        queue.add(stringRequest);
    }

    private void callIntent(Context context, String value) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        ComponentName thisWidget = new ComponentName(context, SimpleWidgetProvider.class);

        remoteViews.setViewVisibility(R.id.main_progress_bar_widget, View.INVISIBLE);

        if (value.equalsIgnoreCase("!"))
            remoteViews.setImageViewResource(R.id.actionButton, R.drawable.open);
        else if (value.equalsIgnoreCase("OK") || value.contains("open"))
            remoteViews.setImageViewResource(R.id.actionButton, R.drawable.open_green);
        else if (value.equalsIgnoreCase("KO") || value.contains("close"))
            remoteViews.setImageViewResource(R.id.actionButton, R.drawable.open_red);

        appWidgetManager.updateAppWidget(thisWidget, remoteViews);

    }

    private void callHandler(final Context context) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callIntent(context, "!");
                Registry.getInstance().setButtonEnabled(true);
            }
        },4000);
    }

}