package com.blueitltd.loudalarm;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.icu.text.UnicodeSetSpanner;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {
    Cursor cursor;
    SqliteDatabaseHelper sqliteDatabaseHelper;
    ArrayList<String> times;
    ArrayList<String> dates;
    ArrayList<String> ringtone;
    static AudioManager mAudioManager;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @Override
    public void onCreate() {
        super.onCreate();
         sqliteDatabaseHelper = new SqliteDatabaseHelper(getApplicationContext());
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        cursor=sqliteDatabaseHelper.GetAllData();
        times=new ArrayList<>();
        dates=new ArrayList<>();
        ringtone=new ArrayList<>();

        Toast.makeText(getApplicationContext(),"onStartCalled",Toast.LENGTH_LONG).show();

        if(cursor!=null){
            while (cursor.moveToNext()){
                times.add(cursor.getString(1));
                dates.add(cursor.getString(2));
                ringtone.add(cursor.getString(3));
            }


            threadclass(dates,times,ringtone);

        }


        return START_STICKY;
    }

    static int period=0;

    static void playmusic(final String ringtone){

        Log.e("ringtone","called");
        final int originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        MediaPlayer mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);


      //  SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
       // Uri notificationSoundUri = Uri.parse(prefs.getString("notification_sound", "DEFAULT_SOUND"));
       // RingtoneManager ringtoneManager=new RingtoneManager(getApplicationContext());
      //  Cursor c=ringtoneManager.getCursor();
       // Uri uri=ringtoneManager.getRingtoneUri(c.getColumnIndex(ringtone));

        try {
            mp.setDataSource("/system/media/audio/ringtones/Aquila.ogg");
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                period++;
                if(period<10){
                    playmusic(ringtone);
                }else {
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
                    period=0;
                }
            }
        });
    }
  public static void threadclass(ArrayList<String>datearray, ArrayList<String>timearray, final ArrayList<String>ringtonearray){
      final ArrayList<String> timestd=timearray;
      final ArrayList<String> datestd=datearray;
      final ArrayList<String> ringtonetd=ringtonearray;
      Thread th=new Thread(new Runnable() {
          @Override
          public void run() {
              while(!Thread.currentThread().isInterrupted()){
                  try {

                      try{
                          String CurrentTime;
                          String CurrentDate;

                          Date dt = new Date();
                          int hours = dt.getHours();
                          int minutes = dt.getMinutes();
                          int seconds = dt.getSeconds();
                          String curTime = hours + ":" + minutes + ":" + seconds;
                          String part1 = null;
                          String part2 = null;

                          try {
                              String _24HourTime = curTime;
                              SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm:ss");
                              SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm:ss a");
                              Date _24HourDt = _24HourSDF.parse(_24HourTime);
                              System.out.println(_24HourDt);
                              System.out.println(_12HourSDF.format(_24HourDt));

                              curTime=_12HourSDF.format(_24HourDt);

                              part1 =curTime.split(" ")[0];
                              part2 = curTime.split(" ")[1];

                              String hour=part1.split(":")[0];
                              String minuite=part1.split(":")[1];
                              String ampm=part2;

                              CurrentTime=hour+" "+minuite+ " "+ampm.toLowerCase();
                              Log.e("tagggggg",CurrentTime);
                              Log.e("tagggggg",timestd.toString());
                              Log.e("tagggggg",datestd.toString());
                              Log.e("tagggggg",ringtonetd.toString());


                              Calendar cal = Calendar.getInstance();
                              SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE MMM dd hh:mm:ss 'GMT'Z yyyy");
                              System.out.println(dateFormat.format(cal.getTime()));
                              Log.e("date",dateFormat.format(cal.getTime()));


                              for(int i=0;i<timestd.size();i++){
                                  String status=timestd.get(i);
                                 if(status.contains(CurrentTime)){

                                     String date=dateFormat.format(cal.getTime()).toLowerCase().split(" ")[0];
                                     Log.e("balsal",date);

                                     Log.e("caldate",dateFormat.format(cal.getTime()).toLowerCase());
                                     Log.e("condate",datestd.get(i));
                                    ArrayList<String>datesp=new ArrayList<>();

                                     if(datestd.get(i).contains(" ")){
                                        String [] ring=datestd.get(i).split(" ");
                                         datesp.addAll(Arrays.asList(ring));
                                     }else {
                                        datesp.add(datestd.get(i));
                                     }

                                     for(int j=0;j<datesp.size();j++){
                                         if(dateFormat.format(cal.getTime()).toLowerCase().contains(datesp.get(j))){

                                             Log.e("status","matched");
                                             playmusic(ringtonetd.get(i));

                                         }
                                     }
                                 }
                              }

                          } catch (Exception e) {
                              e.printStackTrace();
                          }
                      }catch (Exception e) {
                          e.printStackTrace();
                      }
                      Thread.sleep(10000); // Pause of 1 Second
                  } catch (InterruptedException e) {
                      Thread.currentThread().interrupt();
                  }catch(Exception e){
                      e.printStackTrace();
                  }
              }
          }
      });

      th.start();
  }


}
