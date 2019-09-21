package com.blueitltd.loudalarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.UnicodeSetSpanner;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ItemListAdapter extends ArrayAdapter<ItemClass> {

    Context context;
    int resourse;


    public ItemListAdapter(@NonNull Context context, int resource, @NonNull List<ItemClass> objects) {
        super(context, resource, objects);
        this.context=context;
        resourse=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final String time=getItem(position).getTime();
        final String date=getItem(position).getDate();
        String ringtone=getItem(position).getRingtone();
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



        convertView= inflater.inflate(resourse,parent,false);

        TextView timeview=convertView.findViewById(R.id.customtime);
        TextView ringtoneview=convertView.findViewById(R.id.customringtone);
        CheckBox sat=convertView.findViewById(R.id.customsat);
        CheckBox sun=convertView.findViewById(R.id.customsun);
        CheckBox mon=convertView.findViewById(R.id.custommon);
        CheckBox tue=convertView.findViewById(R.id.customtue);
        CheckBox wed=convertView.findViewById(R.id.customwed);
        CheckBox thu=convertView.findViewById(R.id.customthu);
        CheckBox fri=convertView.findViewById(R.id.customfri);

        String realtime = time.replace(" ",":");

        timeview.setText(realtime);

        ringtoneview.setText(ringtone);


        ImageView delete=convertView.findViewById(R.id.customdel);



        if(date.contains("saturday")){
            sat.setChecked(true);
        }
        if(date.contains("sunday")){
            sun.setChecked(true);
        }

        if(date.contains("monday")){
            mon.setChecked(true);
        }
        if(date.contains("tuesday")){
            tue.setChecked(true);
        }
        if(date.contains("wednesday")){
            wed.setChecked(true);
        }
        if(date.contains("thursday")){
            thu.setChecked(true);
        }
        if(date.contains("friday")){
            fri.setChecked(true);
        }




        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SqliteDatabaseHelper sqliteDatabaseHelper=new SqliteDatabaseHelper(context);
                sqliteDatabaseHelper.DeleteData(time,date);
                Toast.makeText(context,"Alarm Successfully Deleted!", Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(context,MainActivity.class));
                ((Activity)context).finish();

            }
        });

        return convertView;
    }
}
