package ru.parvenu.zilandia;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {
    private static final String EXTRA_CRIME_ID =
            "ru.parvenu.android.zilandia.crime_id";
    private static final String EXTRA_CRIME_POS =
            "ru.parvenu.android.zilandia.crime_pos";

    public static Intent newIntent(Context packageContext, UUID crimeId, int pos) {
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        intent.putExtra(EXTRA_CRIME_POS, pos);
        return intent;
    }

    public static int getPos(Intent result) {
        return result.getIntExtra(EXTRA_CRIME_POS, 0);
    }

    @Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_CRIME_ID);
        int pos = (int) getIntent()
                .getIntExtra(EXTRA_CRIME_POS,0);
        return CrimeFragment.newInstance(crimeId, pos);
    }

}
