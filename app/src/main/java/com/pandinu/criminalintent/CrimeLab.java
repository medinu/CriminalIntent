package com.pandinu.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pandinu.criminalintent.Models.Crime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import database.CrimeCursorWraper;
import database.CrimeDbSchema;

public class CrimeLab {
    private  static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private List<Crime> mCrimes;

    public static CrimeLab get(Context context){
        if (sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){
        mContext = context.getApplicationContext();

        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

        //mCrimes = new ArrayList<>();
    }

    public void addCrime(Crime crime){
        ContentValues values = getContentValues(crime);

        mDatabase.insert(CrimeDbSchema.CrimeTable.NAME,null , values );
    }

    public List<Crime> getCrimes(){
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWraper cursor = queryCrimes(null , null) ;

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return crimes ;
    }

    public Crime getCrime(UUID uuid){

        CrimeCursorWraper cursor = queryCrimes(CrimeDbSchema.CrimeTable.Cols.UUID + " = ?", new String[]{uuid.toString()});

        try{
            if(cursor.getCount()==0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();;
        }
    }

    private CrimeCursorWraper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor =  mDatabase.query(CrimeDbSchema.CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);

        return new CrimeCursorWraper(cursor);
    }

    private static ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();

        values.put(CrimeDbSchema.CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeDbSchema.CrimeTable.Cols.TITLE, crime.getmTitle());
        values.put(CrimeDbSchema.CrimeTable.Cols.DATE, crime.getmDate().getTime()); //might be a problem
        values.put(CrimeDbSchema.CrimeTable.Cols.SOLVED, crime.isSolved()?1:0);
        values.put(CrimeDbSchema.CrimeTable.Cols.POLICE, crime.getmRequiresPolice()?1:0);

        return values;
    }

    public void updateCrime(Crime c){
        String uuidString = c.getId().toString();

        ContentValues values = getContentValues(c);

        mDatabase.update(
                CrimeDbSchema.CrimeTable.NAME,
                values,
                CrimeDbSchema.CrimeTable.Cols.UUID + " = ?",
                new String[]{uuidString}
        );
    }

//    public void  addCrime(Crime crime){
//        mCrimes.add(crime);
//    }
//
//    public List<Crime> getmCrimes(){
//        return mCrimes;
//    }
//
//    public Crime getCrime (UUID id){
//        for(Crime crime: mCrimes){
//            if (crime.getId().equals(id)){
//                return crime;
//            }
//        }
//        return null;
//    }
//
    public void deleteCrime(Crime crime){
        ContentValues values = getContentValues(crime);


        mDatabase.delete(
                CrimeDbSchema.CrimeTable.NAME,
                CrimeDbSchema.CrimeTable.Cols.UUID + " = ?",
                new String[]{crime.getId().toString()});
//        for(int i = 0; i < mCrimes.size(); i++){
//            if(crime.getId() == mCrimes.get(i).getId()){
//                mCrimes.remove(i);
//                break;
//            }
//        }
    }

}
