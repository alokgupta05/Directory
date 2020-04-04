package com.ajit.bjp.activity.karyakarta;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.ajit.bjp.R;
import com.ajit.bjp.model.karyakarta.KaryaKarta;
import com.ajit.bjp.util.AppCache;
import com.ajit.bjp.util.AppConstants;

import java.util.List;

public class KaryakartaDetailsActivity extends AppCompatActivity {

    static final String SELECTED_INDEX = "selected_index";
    private List<KaryaKarta> mKaryakartaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karyakarta_details);

        getSupportActionBar().setTitle("Karyakarta");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int selectedIndex = getIntent().getIntExtra(SELECTED_INDEX, 0);
        mKaryakartaList = (List<KaryaKarta>) AppCache.INSTANCE.getValueOfAppCache(AppConstants.KARYAKARTA_LIST);

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new KaryakartaPagerAdapter(getSupportFragmentManager()));

        viewPager.setCurrentItem(selectedIndex);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class KaryakartaPagerAdapter extends FragmentPagerAdapter {

        KaryakartaPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(KarykartaItemFragment.KARYAKARTA_DATA, mKaryakartaList.get(i));

            KarykartaItemFragment fragment = new KarykartaItemFragment();
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return mKaryakartaList.size();
        }
    }

}
