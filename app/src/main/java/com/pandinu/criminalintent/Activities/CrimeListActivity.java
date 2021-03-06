package com.pandinu.criminalintent.Activities;

import androidx.fragment.app.Fragment;

import com.pandinu.criminalintent.Fragments.CrimeListFragment;

public class CrimeListActivity extends SingleFragmentActivity{
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
