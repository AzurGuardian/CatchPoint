package com.example.vince.catch_point;

import android.Manifest;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class ParcoursActivity extends AppCompatActivity implements IndiceFragment.OnFragmentInteractionListener{

    private String STR_ID = "idUser";
    private String STR_PARCOURS = "idParcours";

    private String difficulty;
    private int idParcours;
    private SharedPreferences sharedPreferences;
    private Point[] points;
    private ConnexionAsync connexion;
    private AsyncPost scorePost;
    private WebView myWebView;
    private int noPoint = 0;
    private Button validPoint;
    private Button retourButton;
    private FusedLocationProviderClient mFusedLocationCLient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private boolean pReady = false;
    FragmentManager fm;
    Fragment fragment;
    GpsJS gpsJS;
    Score score;

    //  ArrayList<LocationProvider> providers = new ArrayList<LocationProvider>(); // permet de récuperer TOUT les fournisseurs GPS
    //Criteria criteres = new Criteria();
    //String provider; //on garde en mémoire le meilleur fournisseur


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("HEYO","yup");
        sharedPreferences = getSharedPreferences(getString(R.string.fichier_shared_preferences), this.MODE_PRIVATE);
        fm = getSupportFragmentManager();

        fragment = new IndiceFragment();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcours);
        retourButton = findViewById(R.id.retourButton);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeActivity = new Intent(getApplicationContext(), HomeClient.class);
                finish();
                startActivity(homeActivity);
            }
        });

        validPoint = findViewById(R.id.validButton);

        validPoint.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {


                if ( !pReady){
                    Log.e("PositionGPS", "onClick: "+mCurrentLocation.toString());
                    if (points[noPoint].estProche(mCurrentLocation)){
                        pReady = true;
                        pointToast("Lancement du parcours ! ");
                        points[noPoint].setTrouve(true);
                        noPoint++;
                        retourButton.setVisibility(View.INVISIBLE);
                        validPoint.setText("Trouvé ?");
                        if(difficulty.equals("difficile")){

                            myWebView.setVisibility(View.INVISIBLE);

                            fm.beginTransaction().add(R.id.clAll,fragment).commit();
                        }


                    }else {
                        pointToast("Vous n'êtes pas au point de départ ! ");
                    }
                }else{
                    if(points[noPoint].estProche(mCurrentLocation)){
                        pointToast("Point Trouvé ! ");
                        points[noPoint].setTrouve(true);
                        if(difficulty.equals("difficile")){

                            ((IndiceFragment)fragment).nextPoint();
                        }
                    } else {

                        if(difficulty.equals("difficile")){

                            pointToast("Raté, passez au point suivant");
                            ((IndiceFragment)fragment).nextPoint();
                        }else {

                            pointToast("Raté, réessayer en vous rapprochant du marqueur");
                        }
                        score.updateScore(-100);
                    }
                    noPoint++;
                }
                if(noPoint >= points.length){
                    pointToast("fin du parcours");
                    finParcours();
                    retourAccueil();

                }

            }

        });

        connexion = new ConnexionAsync();
        connexion.execute();


        //      provider = locationManager.getBestProvider(criteres, true); //on recupere le nom du fournisseur qui satisfait un maximum de criteres
//        Log.d("provider", "onCreate: " + provider);

        //Cette méthode permet l'actualisation de la position gps
        //void requestLocationUpdates(String provider, long minTime, float minDistance, LocationListener listener)

        //Méthode permettant de placer des repères qui envoient une alerte au systeme.
        // Elle envoie une alerte lors de l'entrée et la sortie de la zone.
        //Peut permettre la validation ou non lors du clic sur le bouton de validation
        // void addProximityAlert(double latitude, double longitude, float radius, long expiration, PendingIntent intent)


        myWebView = (WebView) findViewById(R.id.webview_map);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.e("MyApplication", consoleMessage.message() + " -- From line "
                        + consoleMessage.lineNumber() + " of "
                        + consoleMessage.sourceId());
                return super.onConsoleMessage(consoleMessage);
            }
        });

// Acquire a reference to the system Location Manager


        //critere pour le choix du fournisseur de gps


        //Log.d("GPSPROV", "onCreate: "+locationManager.isProviderEnabled(provider));


    }

    private void retourAccueil() {
        Intent homeClient = new Intent(getApplicationContext(), HomeClient.class);
        finish();
        startActivity(homeClient);
    }


    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        Log.e("TestGPSCurrent","0");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        Log.e("TestGPSCurrent","hey1");

        if (checkPermissions()) {
            Log.e("testStartLocation", "testStartLocation");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                return;
            }
            getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            // do work here
                            Log.e("TestGPSCurrent","3");
                            Log.e("TestLocation", "testLocation");
                            onLocationChanged(locationResult.getLastLocation());
                        }
                    },

                    Looper.myLooper());
        }


    }

    public void onLocationChanged(Location location) {
        Log.e("TestGPSCurrent","2");
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
            //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        mCurrentLocation = location;
        gpsJS.updateMap(location);

    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions();
            return false;
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
    }

    private void pointToast(String msg){
        Toast.makeText(this, msg , Toast.LENGTH_SHORT).show();
    }

    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            onLocationChanged(location);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }

    public String getEtatPoints(){
        String pointsString = "";
        for(int i =0; i<points.length; i++){
            pointsString += "{\"idPoint\": "+ points[i].getId()+", \"trouve\": "+ points[i].isTrouve()+"}";
            if(i!= points.length-1)pointsString+="/";
        }
        Log.e("ReturnPoint", pointsString);
        return pointsString;

    }

    public void finParcours(){
        score.countValides(points);
        noPoint = 0;
        AsyncPost scorePost = new AsyncPost();
        scorePost.execute();



    }

    public void erreurParcours(){/*
        Intent homeActivity = new Intent(getApplicationContext(), HomeClient.class);
        finish();
        startActivity(homeActivity);*/
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }



    private class ConnexionAsync extends AsyncTask<String, Void, String> {
        String URL;
        IndiceAsync indiceAsync;
        @Override
        protected String doInBackground(String... strings) {
            indiceAsync = new IndiceAsync();
            int idParcours = sharedPreferences.getInt(STR_PARCOURS, -1);
            /*
            if(idParcours == -1){
                erreurParcours();
            }*/


            //A remplacer par l'adresse IP de la machine dans le réseau
            //php bin/console server:run adresse_ip_machine:port
            URL = sharedPreferences.getString("IP","")+"map/getParcours?idParcours=" + idParcours;


            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url(URL).build();
            Response response = null;

            try {
                response = client.newCall(request).execute();
                Log.d("WIN", "doInBackground: WIN ");
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("FAIL", "doInBackground: FAIL");
            }

            return null;
        }

        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);

            Log.e("OBJECTO", "onPostExecute: "+o );

            try {
                JSONObject jsonObject = new JSONObject(o);
                Log.e("JSONTest",jsonObject.toString()+"");
                idParcours = jsonObject.getInt("idParcours");
                JSONArray array = new JSONArray(jsonObject.getString("points"));
                points = new Point[array.length()];
                Log.e("POINTARR",points.length+"");
                difficulty = jsonObject.getString("typeParcours");


                for (int i = 0; i < array.length(); i++) {
                    // On récupère un objet JSON du tableau
                    JSONObject obj = new JSONObject(array.getString(i));
                    Log.e("JSONPOINT", "onPostExecute: " +obj );

                    Point point = new Point( obj.getInt("idPoint") ,obj.getString("titre"), obj.getDouble("lat"),obj.getDouble("lng"));
                    JSONArray arrayInd = new JSONArray(obj.getString("indices"));
                    ArrayList<Indice> arrayI = new ArrayList<Indice>();
                    for (int j = 0; j <arrayInd.length();j++){

                        Log.e("BREF", "onPostExecute: "+point.getTitre() + " "+ arrayInd.length() );
                        JSONObject objInd = new JSONObject(arrayInd.getString(j));
                        Log.e("IndiceArr", "onPostExecute: "+objInd.toString() );
                        Log.e("indiceNom", "onPostExecute: " + objInd.getString("nom") );
                        Log.e("indiceNom", "onPostExecute: " + objInd.getBoolean("obligatoire") );
                        Indice indice = new Indice(objInd.getString("nom"), objInd.getBoolean("obligatoire"), objInd.getString("type"));
                        if(indice.getType().equals("file")){
                            indiceAsync.execute(indice);
                            indiceAsync = new IndiceAsync();
                        }

                        arrayI.add(indice);
                    }

                    Log.e("TaillePoint", "onPostExecute: "+arrayI.size() );
                    point.setIndices(arrayI);
                    Log.e("TestIndicePoint", "onPostExecute: "+point.getIndicesO().size()+ " I: "+i );

                    points[i] = point;


                }

                gpsJS = new GpsJS(jsonObject.toString(),myWebView);
                myWebView.addJavascriptInterface(gpsJS,"Android");
                myWebView.loadUrl("file:///android_asset/map.html");
                score = new Score(points.length);
                fragment = ((IndiceFragment) fragment).newInstance(points,score);

                for (Point point :
                        points) {
                    System.out.println(point.getTitre());
                }

                Log.e("onPostExecute","startLocation");
                startLocationUpdates();
            } catch (JSONException e) {

                Log.e("azerty", "onPostExecute: " + e.getMessage());

            }
        }
    }



    private class AsyncPost extends AsyncTask<String, Void, String> {
        String URL;

        @Override
        protected String doInBackground(String... strings) {
            //A remplacer par l'adresse IP de la machine dans le réseau
            //php bin/console server:run adresse_ip_machine:port
            URL = sharedPreferences.getString("IP","")+"saveScore";
            OkHttpClient client = new OkHttpClient();


            RequestBody formBody = new FormBody.Builder()
                    .add("idUser", ""+sharedPreferences.getInt(STR_ID,0))
                    .add("idParcours", idParcours+"")
                    .add("score",score.getScorePoints()+"")
                    .add("pointsTrouves", getEtatPoints())
                    .add("pointsValides",score.getPointsValides()+"")
                    .add("temps","01:00:00")
                    .build();
            Request request = new Request.Builder()
                    .url(URL)
                    .post(formBody)
                    .build();
            Response response = null;

            try {
                response = client.newCall(request).execute();
                Log.d("WIN", "doInBackground: WIN ");

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("FAIL", "doInBackground: FAIL");
            }

            return null;
        }


    }


    private class IndiceAsync extends AsyncTask<Indice, Void, Bitmap> {
        Indice indice;

        @Override
        protected Bitmap doInBackground(Indice... indices) {

            indice = indices[0];
            String imageurl = sharedPreferences.getString("IP","")+"map/image/download?image=" + indice.getTextIndice() ;
            InputStream in = null;
            Bitmap ivIcon = null;

            try{
                InputStream inputStream = new URL(imageurl).openStream();
                ivIcon = BitmapFactory.decodeStream(inputStream);
                Log.e("ReponseImg", "doInBackground: " );

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("erreurimg", "doInBackground: " + e.getMessage() );
            }
            return ivIcon;
        }

        @Override
        protected void onPostExecute(Bitmap o) {
            indice.setBmImg(o);
        }
    }




}





