package com.pandinu.criminalintent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pandinu.criminalintent.Models.Crime;

import database.CrimeDbSchema;

public class CrimeBaseHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "crimebase.db";

    public CrimeBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CrimeDbSchema.CrimeTable.NAME +
                "(" + "_id integer primary key autoincrement, " +
                CrimeDbSchema.CrimeTable.Cols.TITLE     + ", " +
                CrimeDbSchema.CrimeTable.Cols.DATE      + ", " +
                CrimeDbSchema.CrimeTable.Cols.UUID      + ", " +
                CrimeDbSchema.CrimeTable.Cols.POLICE    + ", " +
                CrimeDbSchema.CrimeTable.Cols.SOLVED    + ", " +
                CrimeDbSchema.CrimeTable.Cols.SUSPECT + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
