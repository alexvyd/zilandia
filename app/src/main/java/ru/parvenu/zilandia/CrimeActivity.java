package ru.parvenu.zilandia;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {
    private static final String EXTRA_CRIME_ID =
            "ru.parvenu.android.zilandia.crime_id";
    private static final String EXTRA_CRIME_LISTPOS =
            "ru.parvenu.android.zilandia.crime_listpos";
    private static final String EXTRA_CRIME_PAGEPOS =
            "ru.parvenu.android.zilandia.crime_pagepos";

    //Чтобы сообщить CrimeFragment, какой объект Crime следует отображать, можно передать идентификатор в дополнении (extra) объекта Intent при запуске CrimeActivity.
    public static Intent newIntent(Context packageContext, UUID crimeId, int listpos) {
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        intent.putExtra(EXTRA_CRIME_LISTPOS, listpos);
        intent.putExtra(EXTRA_CRIME_PAGEPOS, listpos);
        return intent;
    }



    @Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_CRIME_ID);
        int listpos = (int) getIntent()
                .getIntExtra(EXTRA_CRIME_LISTPOS,0);
        int pagepos = (int) getIntent()
                .getIntExtra(EXTRA_CRIME_PAGEPOS,0);
        return CrimeFragment.newInstance(crimeId, listpos, pagepos);
    }

}
