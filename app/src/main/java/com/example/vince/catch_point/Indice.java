package com.example.vince.catch_point;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

public class Indice implements Parcelable {

    private String textIndice;
    private String type;
    private Bitmap bmImg;
    private boolean obligatoire;

    public Indice(String textIndice, boolean obligatoire, String type) {
        this.textIndice = textIndice;
        this.obligatoire = obligatoire;
        this.type = type;
    }
    //type text : type file


    protected Indice(Parcel in) {
        textIndice = in.readString();
        obligatoire = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(textIndice);
        dest.writeByte((byte) (obligatoire ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Indice> CREATOR = new Creator<Indice>() {
        @Override
        public Indice createFromParcel(Parcel in) {
            return new Indice(in);
        }

        @Override
        public Indice[] newArray(int size) {
            return new Indice[size];
        }
    };

    public String getTextIndice() {
        return textIndice;
    }

    public void setTextIndice(String textIndice) {
        this.textIndice = textIndice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Bitmap getBmImg() {
        return bmImg;
    }

    public void setBmImg(Bitmap bmImg) {
        this.bmImg = bmImg;
    }

    public boolean isObligatoire() {
        return obligatoire;
    }

    public void setObligatoire(boolean obligatoire) {
        this.obligatoire = obligatoire;
    }
}
