package com.pandinu.criminalintent.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.pandinu.criminalintent.CrimeLab;
import com.pandinu.criminalintent.Models.Crime;
import com.pandinu.criminalintent.PictureUtils;
import com.pandinu.criminalintent.R;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class   CrimeFragment extends Fragment {
    public static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    public static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    public static final int REQUEST_CONTACT = 2;
    public static final int REQUEST_CRIME_PHOTO = 3;
    public static final int REQUEST_REPORTER_PHOTO = 4;


    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton, mTimeButton, mReportButton, mSuspectButton;
    private CheckBox mSolvedcheckbox, mRequirePolice;
    private ImageButton mCrimePhotoButton, mReporterPhotoButton;
    private ImageView mCrimePhotoView, mReporterPhotoView;

    private File mCrimePhotoFile;
    private File mReporterPhotoFile;

    private String onlyDate = "MM-dd-yyyy";
    private String onlyTime = "hh:mm:ss aaa";

    private static final String ARG_CRIME_ID = "crime_id";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID crimeId = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        mCrimePhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
        mReporterPhotoFile = CrimeLab.get(getActivity()).getReporterPhotoFile(mCrime);
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
        mDateButton.setText("Set Date: " + DateFormat.format(onlyDate, mCrime.getmDate()).toString());

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
        mTimeButton.setText("Set Time: " + DateFormat.format(onlyTime, mCrime.getmDate()).toString());
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        mReportButton = (Button)v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("Text/plain");
//                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
//                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
//
//                i = Intent.createChooser(i, getString(R.string.send_report));

                Intent intent = ShareCompat.IntentBuilder.from(getActivity())
                        .setType("Text/plain")
                        .setText(getCrimeReport())
                        .setSubject(getString(R.string.crime_report_subject))

                        .getIntent();
                intent.createChooser(intent, getString(R.string.send_report));

                startActivity(intent);
            }
        });

        mSuspectButton = (Button)v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if(mCrime.getmSuspect() != null){
            mSuspectButton.setText(mCrime.getmSuspect());
        }

        PackageManager pm = getActivity().getPackageManager();

        mCrimePhotoView = (ImageView)v.findViewById(R.id.crime_photo);
        mCrimePhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bm = null;
                if(mCrimePhotoFile == null || !mCrimePhotoFile.exists()){
                    Toast.makeText(getActivity(), "No picture has been loaded.", Toast.LENGTH_SHORT).show();
                }else{
                    bm = PictureUtils.getScaledBitmap(mCrimePhotoFile.getPath(), getActivity());
                    Dialog dialog = new Dialog(getContext()); // Context, this, etc.
                    dialog.setContentView(R.layout.dialog_image);
                    dialog.show();
                    ImageView iv = (ImageView)dialog.findViewById(R.id.iv_full_screen_crime);
                    iv.setImageBitmap(bm);
                }
            }
        });
        mCrimePhotoButton = (ImageButton)v.findViewById(R.id.crime_camera);

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE  );
        Boolean canTakePhoto = mCrimePhotoFile != null && captureImage.resolveActivity(pm) != null;
        mCrimePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(
                        getActivity(),
                        "com.pandinu.criminalintent.fileprovider",
                        mCrimePhotoFile);

                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities =
                        getActivity().getPackageManager().queryIntentActivities(
                                captureImage, PackageManager.MATCH_DEFAULT_ONLY
                        );

                for(ResolveInfo activity: cameraActivities){
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_CRIME_PHOTO);
            }
        });

        mReporterPhotoView = (ImageView)v.findViewById(R.id.reporter_photo);
        mReporterPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bm = null;
                if(mReporterPhotoFile == null || !mReporterPhotoFile.exists()){
                    Toast.makeText(getActivity(), "No picture has been loaded.", Toast.LENGTH_SHORT).show();
                }else{
                    bm = PictureUtils.getScaledBitmap(mReporterPhotoFile.getPath(), getActivity());
                    Dialog dialog = new Dialog(getContext()); // Context, this, etc.
                    dialog.setContentView(R.layout.dialog_image);
                    dialog.show();
                    ImageView iv = (ImageView)dialog.findViewById(R.id.iv_full_screen_crime);
                    iv.setImageBitmap(bm);
                }
            }
        });
        mReporterPhotoButton = (ImageButton)v.findViewById(R.id.reporter_camera);

        mReporterPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(
                        getActivity(),
                        "com.pandinu.criminalintent.fileprovider",
                        mReporterPhotoFile);

                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities =
                        getActivity().getPackageManager().queryIntentActivities(
                                captureImage, PackageManager.MATCH_DEFAULT_ONLY
                        );

                for(ResolveInfo activity: cameraActivities){
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_REPORTER_PHOTO);
            }
        });

        updatePhotoView();
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
            mDateButton.setText("Set Date: " + DateFormat.format(onlyDate, mCrime.getmDate()).toString());
        }
        if(requestCode == REQUEST_TIME){
            Date date = (Date)data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setmDate(date);
            mTimeButton.setText("Set Time: " + DateFormat.format(onlyTime, mCrime.getmDate()).toString());
        }

        if(requestCode == REQUEST_CONTACT && data!= null){
            Uri contactUri = data.getData();

            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };

            Cursor c = getActivity().getContentResolver().query(
                    contactUri, queryFields, null, null, null
            );

            try {
                if(c.getCount() == 0){ return; }
                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setmSuspect(suspect);
                mSuspectButton.setText(suspect);
            }finally {
                c.close();
            }


        }

        if(requestCode == REQUEST_CRIME_PHOTO){
            Toast.makeText(getActivity(), "Inside Crime", Toast.LENGTH_SHORT).show();
            updatePhotoView();

            Uri uri = FileProvider.getUriForFile(getActivity(), "com.pandinu.criminalintent.fileprovider", mCrimePhotoFile);
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        if(requestCode == REQUEST_REPORTER_PHOTO){
            Toast.makeText(getActivity(), "Inside Reporter", Toast.LENGTH_SHORT).show();
            updatePhotoView();

            Uri uri = FileProvider.getUriForFile(getActivity(), "com.pandinu.criminalintent.fileprovider", mReporterPhotoFile);
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
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
                CrimeLab.get(getActivity()).deleteCrime(mCrime);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    private String getCrimeReport(){
        String solvedString = null;
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getmDate()).toString();

        if (mCrime.isSolved()){
            solvedString = getString(R.string.crime_report_solved);
        }else{
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String suspect = mCrime.getmSuspect();
        if (suspect == null){
            suspect = getString(R.string.crime_report_no_suspect);
        }else{
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report, mCrime.getmTitle(), dateString, solvedString, suspect);

        return report;
    }

    public void updatePhotoView(){
        if(mCrimePhotoFile == null || !mCrimePhotoFile.exists()){
            mCrimePhotoView.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_add_a_photo_24));
        }else{
            Bitmap bm = PictureUtils.getScaledBitmap(mCrimePhotoFile.getPath(), getActivity());
            mCrimePhotoView.setImageBitmap(bm);
        }


        if(mReporterPhotoFile == null || !mReporterPhotoFile.exists()){
            mReporterPhotoView.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_add_a_photo_24));
        }else{
            Bitmap bm = PictureUtils.getScaledBitmap(mReporterPhotoFile.getPath(), getActivity());
            mReporterPhotoView.setImageBitmap(bm);
        }
    }


//    public class FireMissilesDialogFragment extends DialogFragment {
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Use the Builder class for convenient dialog construction
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setMessage(null)
//                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            // FIRE ZE MISSILES!
//                        }
//                    })
//                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            // User cancelled the dialog
//                        }
//                    });
//            // Create the AlertDialog object and return it
//            return builder.create();
//        }
//    }

}
