package com.example.vince.catch_point;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import org.json.JSONObject;

public class GpsJS{


        Location localisation;
        String jsonObject;
        WebView view;
        /** Instantiate the interface and set the context */
        GpsJS(String o, WebView view) {

            jsonObject = o;
            this.view = view;
        }

    /** Show a toast from the web page */
        @JavascriptInterface
        public String getObject() {
            return jsonObject;
        }

        @JavascriptInterface
        public void logTest(String str){
            Log.e("TESTMSG", str);
        }

        @JavascriptInterface
        public void updateMap(Location location){
            Log.e("testUpdate","testUpdate");
            view.evaluateJavascript("javascript: " + "updateGPS("+location.getLatitude()+","+location.getLongitude()+")",null);
        }


}
