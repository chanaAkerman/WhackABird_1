package com.perrchick.dbapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Player {
    private int rate;
    private String name;
    private int score;
    private int time;
    private String lat;
    private String lng;

    public Player(){}

    public Player(String name,int score, int time,String lat,String lng){
        rate=0;
        this.name=name;
        this.score=score;
        this.time=time;
        this.lat=lat;
        this.lng=lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public LatLng getPlayerLocation(){
        LatLng location = new LatLng(
                Double.parseDouble(this.getLat()),Double.parseDouble(this.getLng()));
        return location;
    }

    @NonNull
    @Override
    public String toString() {
        return "Player: [ Rate: "+rate+", Name "+name+", Score "+score+", Time "+time+", Lat "+lat+", Lng "+lng+" ]";
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(this.name.equals( ((Player)obj).getName())
        && this.score==( ((Player)obj).getScore())
        && this.time==(((Player)obj).getTime() ) && this.lat.equals( ((Player)obj).getLat())
            && this.lng.equals( ((Player)obj).getLng())
                && this.rate==( ((Player)obj).getRate())){
            return true;
        }
        return false;
    }
}
