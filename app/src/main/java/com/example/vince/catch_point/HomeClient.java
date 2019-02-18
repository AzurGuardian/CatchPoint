package com.example.vince.catch_point;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeClient extends AppCompatActivity {
    private ImageView ivTest;
    private TextView viewLogin;
    private Button deconnxion;
    private Button menuParcours;
    private SharedPreferences sharedPreferences;
    private String STR_LOGIN = "Login";
    private String STR_MDP = "MDP";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_client);


        ivTest = findViewById(R.id.ivtest);


        viewLogin = (TextView) findViewById(R.id.afficheLogin);
        deconnxion = (Button) findViewById(R.id.deconnexionButton);
        menuParcours = (Button)findViewById(R.id.parcoursMenu);
        deconnxion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deconnxionClient();
            }
        });
        menuParcours.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                afficheMenuParcours();
            }
        });
        sharedPreferences = getSharedPreferences(getString(R.string.fichier_shared_preferences), this.MODE_PRIVATE);
        viewLogin.setText("Connecté avec le login : " + sharedPreferences.getString(STR_LOGIN,""));
    }


    private void deconnxionClient() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(STR_LOGIN,"");
        editor.putString(STR_MDP,"");
        editor.commit();
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        finish();
        startActivity(mainActivity);
    }



    private void afficheMenuParcours(){
        Intent parcoursMenuActivity = new Intent(getApplicationContext(), ParcoursMenuActivty.class);
        finish();
        startActivity(parcoursMenuActivity);
    }


}
