package ru.parvenu.zilandia;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    private static final String ARG_CRIME_ID = "crime_id";
    private static final String ARG_CRIME_LISTPOS = "crime_listpos";
    private static final String ARG_CRIME_PAGEPOS = "crime_pagepos";
    private static final String EXTRA_CRIME_LISTPOS =
            "ru.parvenu.zilandia.crime_listpos";
    private static final String EXTRA_CRIME_PAGEPOS =
            "ru.parvenu.android.zilandia.crime_pagepos";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;

    private Crime mCrime;
    private int listpos,pagepos;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    public static CrimeFragment newInstance(UUID crimeId, int listpos, int pagepos) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        args.putInt(ARG_CRIME_LISTPOS, listpos);
        args.putInt(ARG_CRIME_PAGEPOS, pagepos);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //mCrime = new Crime();
       // UUID crimeId = (UUID) getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        listpos = (int) getArguments().getInt(ARG_CRIME_LISTPOS);
        pagepos = (int) getArguments().getInt(ARG_CRIME_PAGEPOS);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        //mCrime = CrimeLab.get(getActivity()).getCrimeByPos(crimePos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                CharSequence s, int start, int count, int after) {
                // Здесь намеренно оставлено пустое место
            }
            @Override
            public void onTextChanged(
                CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                // И здесь тоже
            }
        });

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });


        String subtitle = getString(R.string.subtitle_crime, listpos, pagepos);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);

        return v;
    }
    @Override
    public void onPause() {
        super.onPause();
        Intent data = new Intent();
        data.putExtra(EXTRA_CRIME_PAGEPOS, pagepos);
        getActivity().setResult(Activity.RESULT_OK, data);
        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
    }
    @Override //Передача данных во фрагмент
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //int CrimeSize=CrimeLab.get(getActivity()).CrimesSize();
        switch (item.getItemId()) {
            case R.id.save_crime:
                getActivity().onBackPressed();
                //getActivity().finish();
                return true;
            case R.id.delete_crime:
                CrimeLab.get(getActivity()).delCrime(mCrime.getId(),listpos);
                getActivity().onBackPressed();
                //getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public static int getPos(Intent result) {
        return result.getIntExtra(EXTRA_CRIME_PAGEPOS, -1);
    }
}
