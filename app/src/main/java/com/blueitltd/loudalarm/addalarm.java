package com.blueitltd.loudalarm;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class addalarm extends AppCompatActivity {

    EditText hour,min,ampm;
    CheckBox sat,sun,mon,tue,wed,thu,fri;
    TextView ringtone,warning;
    ImageButton chooseringtone,addalarm;
    SqliteDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addalarm);
        hour=findViewById(R.id.edthour);
        min=findViewById(R.id.edtmin);
        ampm=findViewById(R.id.edtampm);
        sat=findViewById(R.id.satarday);
        sun=findViewById(R.id.sunday);
        mon=findViewById(R.id.monday);
        tue=findViewById(R.id.tuesday);
        wed=findViewById(R.id.wednesday);
        thu=findViewById(R.id.thursday);
        fri=findViewById(R.id.friday);
        ringtone=findViewById(R.id.ringtone);
        chooseringtone=findViewById(R.id.selectring);
        addalarm=findViewById(R.id.addalarm);
        warning=findViewById(R.id.warning);


        databaseHelper =new SqliteDatabaseHelper(addalarm.this);
        SQLiteDatabase sqLiteDatabase=databaseHelper.getWritableDatabase();

        addalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFunctionality();
            }
        });
        chooseringtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Ringtone");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
                startActivityForResult(intent,5);
            }
        });

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Ringtone ringtoneone=RingtoneManager.getRingtone(this,uri);

        ringtone.setText(ringtoneone.getTitle(this));




    }

    void checkFunctionality(){
        String h =hour.getText().toString();
        String m=min.getText().toString();
        String am=ampm.getText().toString();


        String days = "";

        boolean cancle=false;
        View vi=null;

        if(sat.isChecked()){
            days= days+ "saturday";
        }
        if(sun.isChecked()){
            days=days+" sunday";
        }

        if(mon.isChecked()){
            days=days+" monday";
        }

        if(tue.isChecked()){
            days=days+" tuesday";
        }
        if(wed.isChecked()){
            days=days+" wednesday";
        }
        if(thu.isChecked()){
            days=days+" thursday";
        }
        if(fri.isChecked()){
            days=days+" friday";
        }

        Toast.makeText(addalarm.this,days,Toast.LENGTH_LONG).show();

        String sound=ringtone.getText().toString();

        if(TextUtils.isEmpty(h)){
            cancle=true;
            vi=hour;
            hour.setError("Field Empty!");
        }else if(TextUtils.isEmpty(m)){
            cancle=true;
            vi=min;
            min.setError("Field Empty!");
        }else if(TextUtils.isEmpty(am)){
            cancle=true;
            vi=ampm;
            ampm.setError("Field Empty!");

        }else if(TextUtils.isEmpty(days)){
            cancle=true;
            vi=warning;
            warning.setVisibility(View.VISIBLE);
            warning.setText("Please Select Minimum One Day!");
        }else if(sound.isEmpty()){
            cancle=true;
            vi=chooseringtone;
            warning.setVisibility(View.VISIBLE);
            warning.setText("Please Choose A Ringtone!");
        }

        if(cancle){
            vi.requestFocus();

        }else{
            Toast.makeText(com.blueitltd.loudalarm.addalarm.this,"all ok",Toast.LENGTH_SHORT).show();

            String dbtime=h+" "+m+" "+am;
            String dbringtone = ringtone.getText().toString();

           long success= databaseHelper.InsertData(dbtime,days,dbringtone);


           if(success==-1){
               Toast.makeText(addalarm.this,"failed",Toast.LENGTH_SHORT).show();
           }else {
               Toast.makeText(addalarm.this,"success",Toast.LENGTH_SHORT).show();

           }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == Activity.RESULT_OK && requestCode == 5)
        {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            Ringtone ringtonetitle = RingtoneManager.getRingtone(this, uri);
            String title = ringtonetitle.getTitle(this);

            if (uri != null)
            {
                ringtone.setText(title);
            }
            else
            {
                ringtone.setText("");
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
