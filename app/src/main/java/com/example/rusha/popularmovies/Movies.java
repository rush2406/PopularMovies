package com.example.rusha.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rusha on 6/14/2017.
 */

public class Movies implements Parcelable {
    private int id;
    private String title;
    private String image;
    private String votercount;
    private String voterrate;
    private String release;
    private String overview;
    private int pos;
    private boolean value;

    public int getPos() {
        return pos;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease() {

        return release;
    }

    public String getVoterrate() {

        return voterrate;
    }

    public String getVotercount() {

        return votercount;
    }

    public Movies(Parcel p) {
        this.id = p.readInt();
        this.title = p.readString();
        this.image = p.readString();
        this.votercount = p.readString();
        this.voterrate = p.readString();
        this.release = p.readString();
        this.overview = p.readString();

    }

    public Movies(int id, String title, String image, String votercount, String voterrate, String release, String overview) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.votercount = votercount;
        this.voterrate = voterrate;
        this.release = release;
        this.overview = overview;

    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public boolean isValue() {

        return value;
    }


    public Movies(int id, String title, String image, String votercount, String voterrate, String release, String overview, int pos) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.votercount = votercount;
        this.voterrate = voterrate;
        this.release = release;
        this.overview = overview;
        this.pos = pos;

    }

    public String getImage() {
        return image;
    }

    public String getTitle() {

        return title;
    }

    public int getId() {

        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(image);
        parcel.writeString(votercount);
        parcel.writeString(voterrate);
        parcel.writeString(release);
        parcel.writeString(overview);
    }

    public static final Parcelable.Creator<Movies> CREATOR
            = new Parcelable.Creator<Movies>() {

        // This simply calls our new constructor (typically private) and 
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };
}
