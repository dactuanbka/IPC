package com.example.tuanqd_ipc_server;

import android.os.Parcel;
import android.os.Parcelable;

public final class MyParcel implements Parcelable {
    public int anInt;
    public long aLong;
    public boolean aBoolean;
    public float aFloat;
    public double aDouble;
    public String aString;

    public MyParcel() {

    }

    public int getAnInt() {
        return anInt;
    }

    public void setAnInt(int anInt) {
        this.anInt = anInt;
    }

    public long getaLong() {
        return aLong;
    }

    public void setaLong(long aLong) {
        this.aLong = aLong;
    }

    public boolean isaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public float getaFloat() {
        return aFloat;
    }

    public void setaFloat(float aFloat) {
        this.aFloat = aFloat;
    }

    public double getaDouble() {
        return aDouble;
    }

    public void setaDouble(double aDouble) {
        this.aDouble = aDouble;
    }

    public String getaString() {
        return aString;
    }

    public void setaString(String aString) {
        this.aString = aString;
    }

    private MyParcel(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        anInt = in.readInt();
        aLong = in.readLong();
        aBoolean = in.readByte() != 0;
        aFloat = in.readFloat();
        aDouble = in.readDouble();
        aString = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(anInt);
        dest.writeLong(aLong);
        dest.writeByte((byte) (aBoolean ? 1 : 0));
        dest.writeFloat(aFloat);
        dest.writeDouble(aDouble);
        dest.writeString(aString);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyParcel> CREATOR = new Creator<MyParcel>() {
        @Override
        public MyParcel createFromParcel(Parcel in) {
            return new MyParcel(in);
        }

        @Override
        public MyParcel[] newArray(int size) {
            return new MyParcel[size];
        }
    };
}
