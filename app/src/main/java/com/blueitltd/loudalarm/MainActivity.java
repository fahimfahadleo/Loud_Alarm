package com.blueitltd.loudalarm;

import android.content.Intent;
import android.database.Cursor;
import android.icu.text.UnicodeSetSpanner;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.adfendo.sdk.ads.AdFendo;
import com.adfendo.sdk.ads.AdFendoInterstitialAd;
import com.adfendo.sdk.interfaces.InterstitialAdListener;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    AdFendoInterstitialAd adfi;
    ListView listView;

    FloatingActionButton add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listview);

        add=findViewById(R.id.fabadd);

        SqliteDatabaseHelper  sqliteDatabaseHelper=new SqliteDatabaseHelper(this);
        Cursor c=sqliteDatabaseHelper.GetAllData();


        final ArrayList<ItemClass> mylist=new ArrayList<>();


        if(c!=null){
            while (c.moveToNext()){
                String time= c.getString(1);
                String date = c.getString(2);
                String ringtone=c.getString(3);

                ItemClass itemClass=new ItemClass(time,date,ringtone);
                mylist.add(itemClass);
            }
        }


        startService(new Intent(this,MyService.class));


        ItemListAdapter adapter=new ItemListAdapter(this,R.layout.customlayout,mylist);

        listView.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,addalarm.class));
                finish();
            }
        });


        Thread myThread = null;
        Runnable myRunnableThread = new CountDownRunner();
        myThread= new Thread(myRunnableThread);
        myThread.start();
        AdFendo.initialize("test-app-146514415");
        adfi=new AdFendoInterstitialAd(this,"test-ad-unit-id-146514415~9142051414");
        adfi.setInterstitialAdListener(new InterstitialAdListener() {
            @Override
            public void onClosed() {
                Toast.makeText(MainActivity.this,"ad cloased",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailedToLoad(int i) {
                Toast.makeText(MainActivity.this,"ad load failed reasone "+ i,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void isLoaded(boolean b) {
                Toast.makeText(MainActivity.this,"ad loaded "+b,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onImpression() {
                Toast.makeText(MainActivity.this,"ad impression called",Toast.LENGTH_SHORT).show();
            }
        });
        //adfi.requestAd();
      /*  mybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adfi.isLoaded()){
                    adfi.showAd();
                }else {
                    adfi.requestAd();
                }
            }
        });*/
    }
    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try{
                    TextView txtCurrentTime= findViewById(R.id.text);
                    TextView txtCurrentTime2=findViewById(R.id.textpart2);
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    txtCurrentTime.setText(part1);
                    txtCurrentTime2.setText(part2);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    class CountDownRunner implements Runnable{
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    doWork();
                    Thread.sleep(1000); // Pause of 1 Second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
