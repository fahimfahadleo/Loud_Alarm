package com.blueitltd.loudalarm;

public class ItemClass {
    String time;
    String date;
    String ringtone;

    public ItemClass(String time,String date,String ringtone){
        this.time=time;
        this.date=date;
        this.ringtone=ringtone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRingtone() {
        return ringtone;
    }

    public void setRingtone(String ringtone) {
        this.ringtone = ringtone;
    }
}
