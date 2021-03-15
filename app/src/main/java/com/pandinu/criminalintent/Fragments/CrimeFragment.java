package com.pandinu.criminalintent.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.pandinu.criminalintent.Activities.CrimePagerActivity;
import com.pandinu.criminalintent.CrimeLab;
import com.pandinu.criminalintent.Models.Crime;
import com.pandinu.criminalintent.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class   CrimeFragment extends Fragment {
    public static final String DIALOG_DATE = "DialogDate";
    public static final int REQUEST_DATE = 0;
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_TIME = 1;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton, mTimeButton;
    private CheckBox mSolvedcheckbox, mRequirePolice;

    private DateFormat onlyDate = new SimpleDateFormat("MM-dd-yyyy");
    private DateFormat onlyTime = new SimpleDateFormat("hh:mm:ss aaa");

    private static final String ARG_CRIME_ID = "crime_id";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID crimeId = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(
                R.layout.fragment_crime,    // Layout
                container,                  // the views parent
                false           // view gets added in view activity's code
                );

        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getmTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setmTitle(s.toString());
                Log.i("CrimeFragment", s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        mDateButton = (Button)v.findViewById(R.id.crime_date);
        mDateButton.setText("Set Date: " + onlyDate.format(mCrime.getmDate()).toString());
        //mDateButton.setEnabled(false);

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();

                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getmDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);

            }
        });

        mTimeButton = (Button)v.findViewById(R.id.crime_time);
        mTimeButton.setText("Set Time: " + onlyTime.format(mCrime.getmDate()).toString());
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Get Date", Toast.LENGTH_SHORT).show();
                FragmentManager fm = getFragmentManager();

                TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getmDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(fm, DIALOG_TIME);
            }
        });

        mSolvedcheckbox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedcheckbox.setChecked(mCrime.isSolved());
        mSolvedcheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setmSolved(isChecked);
            }
        });

        mRequirePolice = (CheckBox)v.findViewById(R.id.cbRequirePolice);
        mRequirePolice.setChecked(mCrime.getmRequiresPolice());
        mRequirePolice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setmRequiresPolice(isChecked);
            }
        });
        setHasOptionsMenu(true);
        return v;
    }

    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != Activity.RESULT_OK){
            Toast.makeText(getActivity(),  "Unsuccessful", Toast.LENGTH_SHORT );
            return;
        }

        if(requestCode == REQUEST_DATE){
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setmDate(date);
            //mDateButton.setText(date.toString());
            mDateButton.setText("Set Date: " + onlyDate.format(mCrime.getmDate()).toString());
            //Toast.makeText(getActivity(),  "Date set: " +date.toString(), Toast.LENGTH_SHORT ).show();
        }
        if(requestCode == REQUEST_TIME){
            Date date = (Date)data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setmDate(date);
            //mTimeButton.setText(date.toString());
            mTimeButton.setText("Set Time: " + onlyTime.format(mCrime.getmDate()).toString());
            //Toast.makeText(getActivity(),  "Time set: " + date.toString(), Toast.LENGTH_SHORT ).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.miDelete:
                //Toast.makeText(getActivity(), mCrime.getmTitle() + " has been removed", Toast.LENGTH_SHORT).show();
                CrimeLab.get(getActivity()).removeCrime(mCrime);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
