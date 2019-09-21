package com.blueitltd.loudalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class SqliteDatabaseHelper extends SQLiteOpenHelper {
    private static final String DatabaseName = "Loud Ringtone.db";
    private static final String TableName= "AlarmList";
    private static final int DatabaseVersion = 1;

    Context context;
    public SqliteDatabaseHelper(@Nullable Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL("CREATE TABLE "+TableName+ "( ID INTEGER PRIMARY KEY AUTOINCREMENT, TIME VARCHAR(255), DAY VARCHAR(255), RINGTONE VARCHAR(255) ); ");

            Toast.makeText(context,"on created called",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS AlarmList");
            Toast.makeText(context,"on update called",Toast.LENGTH_SHORT).show();
            onCreate(sqLiteDatabase);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public long InsertData(String time,String date,String ringtone){

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("TIME",time);
        contentValues.put("DAY",date);
        contentValues.put("RINGTONE",ringtone);
        return sqLiteDatabase.insert(TableName,null,contentValues);
    }

    public Cursor GetAllData(){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();

        return sqLiteDatabase.rawQuery("SELECT * FROM "+TableName,null);
    }
    public void DeleteData(String time,String date){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM AlarmList WHERE TIME = '"+time+ "' AND DAY = '"+date+"'");
        sqLiteDatabase.close();

    }
}
