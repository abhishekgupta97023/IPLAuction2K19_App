package com.tachyon.techlabs.iplauction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String database_name = "User.db";
    private static final String table_name = "User_table";

    private static final String c1 = "one";
    private static final String c2 = "Current_Amount";
    private static final String c3 = "Initial_Amount";
    private static final String c4 = "Owner";
    private static final String c5 = "legend";
    private static final String c6 = "noball";
    private static final String c7 = "numofcards";
    private static final String c8 = "righttomatch";
    private static final String c9 = "yorker";
    private static final String c10 = "inRoom";
    private static final String c11 = "joinkey";


    public DataBaseHelper(@Nullable Context context) {
        super(context,database_name,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE  "+table_name+ "(one Integer , Current_Amount Integer , Initial_Amount Integer, Owner Text,legend Integer,noball Integer,numofcards Integer,righttomatch Integer,yorker Integer,inRoom Integer,joinkey Integer)");
    }

    public boolean insertData(String name,String ca,String ia,String own,String legend,String noball,String numofcards,String rtm,String yorker,String inRoom,String joinkey)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(c1,name);
        contentValues.put(c2,ca);
        contentValues.put(c3,ia);
        contentValues.put(c4,own);
        contentValues.put(c5,legend);
        contentValues.put(c6,noball);
        contentValues.put(c7,numofcards);
        contentValues.put(c8,rtm);
        contentValues.put(c9,yorker);
        contentValues.put(c10,inRoom);
        contentValues.put(c11,joinkey);

        long result = db.insert(table_name,null,contentValues);

        return result != -1;
    }

    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from "+table_name,null);
        return res;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+table_name);
    }
}
