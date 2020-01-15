package com.naestech.f_tleave;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class Leave_info_Act extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    int dt,uid;
    String name,Dpt_name;
    SharedPrefs spfs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_info);


        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
        spfs = new SharedPrefs(getApplicationContext());
        dt = Integer.parseInt(spfs.getUId());
        name = spfs.getUName();
        uid = spfs.getUTypeId();
        Dpt_name = spfs.getdeptname();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Log.i("dt", String.valueOf(dt));
        Log.i("name", name);
        Log.i("uid", String.valueOf(uid));

        Leave_info_Act.ViewPagerAdapter adapter = new Leave_info_Act.ViewPagerAdapter(Leave_info_Act.this.getSupportFragmentManager());

        Apply_Leave_Frag alfg = new Apply_Leave_Frag();
        Leave_Status_Frag lstatus = new Leave_Status_Frag();

        adapter.addFrag(alfg,"ApplyLeave");
        adapter.addFrag(lstatus,"Leavestatus");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {super(manager);}

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {return mFragmentList.size();}

        void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {return mFragmentTitleList.get(position);}

    }
}
