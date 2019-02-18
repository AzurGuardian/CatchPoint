package com.example.vince.catch_point;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ParcoursMenuActivty extends Activity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String STR_ID = "idUser";
    private SharedPreferences sharedPreferences;
    private ArrayList<Parcours> parcoursMenu;
    private AsyncMenu asyncMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_parcours);

        sharedPreferences = getSharedPreferences(getString(R.string.fichier_shared_preferences), this.MODE_PRIVATE);

        mLayoutManager = new LinearLayoutManager(this);
        asyncMenu = new AsyncMenu(this);
        asyncMenu.execute();

        // use a linear layout manager


        // specify an adapter (see also next example)



    }

    // ...
    private class AsyncMenu extends AsyncTask<String, Void, String> {
        String URL;
        ParcoursMenuActivty pmActivity;
        public AsyncMenu( ParcoursMenuActivty pma){
            this.pmActivity = pma;
        }

        @Override
        protected String doInBackground(String... strings) {

            //A remplacer par l'adresse IP de la machine dans le réseau
            //php bin/console server:run adresse_ip_machine:port
            URL = sharedPreferences.getString("IP","")+"map/parcoursRejoint?idUser="+ sharedPreferences.getInt(STR_ID, 0);


            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(URL).build();
            Response response = null;

            try{
                response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            Log.e("tryAdapt", "onPostExecute: " + o );
            try {
                Log.e("JSONADAPTER", "onPostExecute: " + o );

                JSONArray arrayRetour = new JSONArray(o);
                parcoursMenu = new ArrayList<Parcours>();

                for(int i = 0  ; i< arrayRetour.length(); i++){
                    JSONObject jsonParcours = arrayRetour.getJSONObject(i);
                    parcoursMenu.add(new Parcours(jsonParcours.getInt("idParcours"), jsonParcours.getString("nomParcours"), jsonParcours.getDouble("longueurParcours"), jsonParcours.getString("difficulte")));
                }


                mRecyclerView = (RecyclerView) findViewById(R.id.menu_parcours);
                Log.e("testAdapter2", "ParcoursMenuAdapter: " );
                mAdapter = new ParcoursMenuAdapter(parcoursMenu, pmActivity, sharedPreferences);
                Log.e("testAdapter1", "ParcoursMenuAdapter: " );

                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(mLayoutManager);


            } catch (JSONException e) {
                Log.e("catchAdapt", "onPostExecute: " + e.getMessage());

            }
        }
    }
}
