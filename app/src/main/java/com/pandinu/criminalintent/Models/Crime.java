package com.pandinu.criminalintent.Models;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private String mTitle;
    private UUID mId;
    private Date mDate;
    private  Boolean mSolved;
    private Boolean mRequiresPolice;

    public Crime(){
        this.mId = UUID.randomUUID();
        this.mDate = new Date();
        this.mSolved = false;
        this.mRequiresPolice = false;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public Boolean isSolved() {
        return mSolved;
    }

    public void setmSolved(Boolean mSolved) {
        this.mSolved = mSolved;
    }

    public UUID getId() {
        return this.mId;
    }

    public void setmRequiresPolice(Boolean requiresPolice){
        this.mRequiresPolice = requiresPolice;
    }

    public Boolean getmRequiresPolice(){
        return this.mRequiresPolice;
    }
}
