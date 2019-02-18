package com.example.vince.catch_point;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class Point implements Parcelable {

    private int id;
    private String titre;
    private double lat;
    private double lng;
    private ArrayList<Indice> indicesO = new ArrayList<Indice>();
    private ArrayList<Indice> indicesF = new ArrayList<Indice>();
    private boolean trouve = false;

    public Point(int id, String titre, double lat, double lng) {
        this.id = id;
        this.titre = titre;
        this.lat = lat;
        this.lng = lng;
    }

    protected Point(Parcel in) {
        id = in.readInt();
        titre = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
        trouve = in.readByte() != 0;
    }

    public static final Creator<Point> CREATOR = new Creator<Point>() {
        @Override
        public Point createFromParcel(Parcel in) {
            return new Point(in);
        }

        @Override
        public Point[] newArray(int size) {
            return new Point[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public boolean isTrouve() {
        return trouve;
    }

    public void setTrouve(boolean trouve) {
        this.trouve = trouve;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setIndices(ArrayList<Indice> indices){


        Log.e("CreateInd","indices");
        Log.e("CreateInd",indices.toString());

        for (Indice indice :
                indices) {
            if (indice.isObligatoire()) {
                Log.e("CreateIndiceO", "setIndices: "+ titre );
                indicesO.add(indice);
            }else {
                indicesF.add(indice);

                Log.e("CreateIndiceF", "setIndices: "+ titre );
            }
        }
    }

    public ArrayList<Indice> getIndicesO() {
        Log.e("GetIndiceO",""+indicesO.size());
        return indicesO;
    }

    public ArrayList<Indice> getIndicesF() {
        return indicesF;
    }

    public boolean estProche(Location location){
        float[] distance = new float[2];
        Location.distanceBetween(this.lat,this.lng,location.getLatitude(),location.getLongitude(),distance);
        Log.e("estProche", "estProche: "+ distance[0] );
        if(distance[0] < 50){
            this.trouve = true;
            return true;
        }else return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(titre);
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);
        parcel.writeByte((byte) (trouve ? 1 : 0));
    }
}
