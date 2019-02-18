package com.example.vince.catch_point;

import android.os.Parcel;
import android.os.Parcelable;

public class Score implements Parcelable {

    private int scorePoints = 1000;
    private int pointsMax;
    private int pointsValides = 0;

    public Score(int pm){
        pointsMax = pm;
    }

    protected Score(Parcel in) {
        scorePoints = in.readInt();
        pointsMax = in.readInt();
        pointsValides = in.readInt();
    }

    public static final Creator<Score> CREATOR = new Creator<Score>() {
        @Override
        public Score createFromParcel(Parcel in) {
            return new Score(in);
        }

        @Override
        public Score[] newArray(int size) {
            return new Score[size];
        }
    };

    public void countValides(Point[] points){
        for (Point point : points){
            if(point.isTrouve())pointsValides++;
        }
    }
    public void updateScore(int s){
        scorePoints += s;
    }

    public int getScorePoints() {
        return scorePoints;
    }

    public void setScorePoints(int scorePoints) {
        this.scorePoints = scorePoints;
    }

    public int getPointsValides() {
        return pointsValides;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(scorePoints);
        parcel.writeInt(pointsMax);
        parcel.writeInt(pointsValides);
    }
}
