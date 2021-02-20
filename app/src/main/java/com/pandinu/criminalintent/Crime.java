package com.pandinu.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private String mTitle;
    private UUID mId;
    private Date mDate;
    private  Boolean mSolved;

    public Crime(){
        this.mId = UUID.randomUUID();
        this.mDate = new Date();
        //this.mSolved = false;
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

    public Boolean getmSolved() {
        return mSolved;
    }

    public void setmSolved(Boolean mSolved) {
        this.mSolved = mSolved;
    }
}
