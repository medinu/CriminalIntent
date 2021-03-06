package com.pandinu.criminalintent.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pandinu.criminalintent.Activities.CrimeActivity;
import com.pandinu.criminalintent.Activities.CrimePagerActivity;
import com.pandinu.criminalintent.CrimeLab;
import com.pandinu.criminalintent.Models.Crime;
import com.pandinu.criminalintent.R;

import java.util.List;

public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = (RecyclerView)view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getmCrimes();

        if(mAdapter == null){
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }
        else{
            mAdapter.notifyDataSetChanged();
        }
    }


    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> mCrimes;

        public CrimeAdapter(List <Crime> crimes){
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

//            if (viewType == 1){
//                return new CrimeHolder(layoutInflater, parent, viewType);
//            }
            return new CrimeHolder(layoutInflater, parent, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            holder.bind(mCrimes.get(position));
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @Override
        public int getItemViewType(int position) {
            if(mCrimes.get(position).getmRequiresPolice()){
                return 1;
            }
            return 0;
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title, date;
        private Crime mCrime;
        private Button mCallPolice;

//        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
//            super(inflater.inflate(R.layout.list_item_crime, parent, false));
//            title = (TextView)itemView.findViewById(R.id.crime_title);
//            date = (TextView)itemView.findViewById(R.id.crime_date);
//            itemView.setOnClickListener(this);
//        }

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
            super(inflater.inflate((viewType == 1)? R.layout.list_item_crime_police: R.layout.list_item_crime, parent, false));
            title = (TextView)itemView.findViewById(R.id.crime_title);
            date = (TextView)itemView.findViewById(R.id.crime_date);

            if (viewType == 1){
                mCallPolice = (Button)itemView.findViewById(R.id.btnCallPolice);

                mCallPolice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "The police are on their way!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            itemView.setOnClickListener(this);
        }

        public void bind(Crime crime){
            mCrime = crime;
            //Log.i("CrimeListFragment", crime.getmTitle());
            this.title.setText(mCrime.getmTitle());
            //Log.i("CrimeListFragment", crime.getmDate().toString());
            date.setText(mCrime.getmDate().toString());
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), mCrime.getmTitle() + " clicked!", Toast.LENGTH_SHORT).show();
            Intent i = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivity(i);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
}
