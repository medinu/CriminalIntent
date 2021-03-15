package com.pandinu.criminalintent;

import android.content.Context;

import com.pandinu.criminalintent.Models.Crime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private  static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;

    public static CrimeLab get(Context context){
        if (sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){
        mCrimes = new ArrayList<>();
    }

    public void  addCrime(Crime crime){
        mCrimes.add(crime);
    }

    public List<Crime> getmCrimes(){
        return mCrimes;
    }

    public Crime getCrime (UUID id){
        for(Crime crime: mCrimes){
            if (crime.getId().equals(id)){
                return crime;
            }
        }
        return null;
    }

    public void removeCrime(Crime crime){
        for(int i = 0; i < mCrimes.size(); i++){
            if(crime.getId() == mCrimes.get(i).getId()){
                mCrimes.remove(i);
                break;
            }
        }
    }

}
