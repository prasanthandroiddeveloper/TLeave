package com.naestech.f_tleave;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.naestech.f_tleave.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.naestech.f_tleave.utils.Config.GETNAME;

public class Main_Screen_Activity extends AppCompatActivity {
    SharedPrefs sp;
    NavigationView navigationView;
    DrawerLayout drawerLayt;
    Toolbar toolbar;
    static int navItemIndex = 0,Titleindex=0;
    static String CURRENT_TAG = "All Bookings";
    String uid,uname,dept_name,name;
    String[] activityTitles;
    int userid;
    Bundle bb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sp=new SharedPrefs(this);
        dept_name = sp.getdeptname();
        uname = sp.getUName();
        Log.i("dept_name",dept_name);
        Log.i("uname",uname);
        userid= sp.getUTypeId();

       /*  bb = getIntent().getExtras();
        assert bb !=null;*/


        if (!uname.equals("")) {

            drawerLayt = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);

            navigationView.setItemIconTintList(null);
            View navHeader = navigationView.getHeaderView(0);

            activityTitles = new String[8];
            activityTitles[0] = "Dashboard";
            activityTitles[1] = "Report";
            activityTitles[2] = "Approve Leave";
            activityTitles[3] = "Assigning";
            activityTitles[4] = "Assign Weekoff";
            activityTitles[5] = "Profile";
            activityTitles[6] = "Employee Details";
            activityTitles[7] = "Compensate Leave";

            ((TextView) navHeader.findViewById(R.id.nameTv)).setText(uname);

            setUpNavigationView();
            navItemIndex = getIntent().getIntExtra("Index",0);

            if (savedInstanceState == null) {

                getHomeFragment();
            } else {
                //  force_logout();
                Toast.makeText(Main_Screen_Activity.this, "Something Went Wrong Try Again", Toast.LENGTH_SHORT).show();
            }

        }
        getname();
    }


    private void getname(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,GETNAME, response -> {

            Log.i("leaverespe",response);

            try {

                JSONArray jarr = new JSONArray(response);
                List<DataAdapter> list = new ArrayList<>();

                for(int i = 0; i< jarr.length(); i++) {
                    DataAdapter adapter = new DataAdapter();
                    JSONObject json = jarr.getJSONObject(i);
                    name= json.getString("name");
                    Utils.makeToast(this,name);

                    Log.i("nameedit",name);

                    list.add(adapter);
                }

            } catch (JSONException e) {

                //  Utils.makeToast(this,e.getMessage());
            }

        }, error -> {

            Utils.makeToast(this,"No Data");
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("id", String.valueOf(userid));

                Log.i("param", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void setUpNavigationView() {

        if(!(dept_name.equals("Super_Admin"))){
            navigationView.getMenu().findItem(R.id.asgn).setVisible(false);
        }

        if(!((dept_name.equals("Admin")) ||(dept_name.equals("Super_Admin")))){
            navigationView.getMenu().findItem(R.id.Weekoff).setVisible(false);
            navigationView.getMenu().findItem(R.id.reports).setVisible(false);
            navigationView.getMenu().findItem(R.id.empdtls).setVisible(false);
            navigationView.getMenu().findItem(R.id.AprvL).setVisible(false);
        }

        navigationView.setNavigationItemSelectedListener(menuItem -> {

            Fragment fragment=null;
            switch (menuItem.getItemId()) {

                case R.id.dashboard:
                    navItemIndex = 0;
                    Titleindex=0;
                    CURRENT_TAG = activityTitles[0];
                    fragment=new Dashboard();
                    break;
                case R.id.reports:
                    navItemIndex = 1;
                    Titleindex=1;
                    CURRENT_TAG = activityTitles[1];
                    fragment=new Report_Frag();
                    break;
                case R.id.AprvL:
                    navItemIndex = 2;
                    Titleindex=2;
                    CURRENT_TAG = activityTitles[2];
                    fragment=new LConfirmationFrag();
                    break;

                case R.id.asgn:
                    navItemIndex = 3;
                    Titleindex=3;
                    CURRENT_TAG = activityTitles[3];
                    fragment=new AssignEmplysFrag();
                    break;
                case R.id.Weekoff:
                    navItemIndex = 4;
                    Titleindex=4;
                    CURRENT_TAG = activityTitles[4];
                    fragment=new AssignWeekOff_Frag();
                    break;

                case R.id.profile:
                    navItemIndex = 5;
                    Titleindex=5;
                    CURRENT_TAG = activityTitles[5];

                    if(name == null){
                        fragment=new ProfileFrag();

                    }else{
                        fragment=new ProfileEdit();

                    } break;

                case R.id.empdtls:
                    navItemIndex = 6;
                    Titleindex=6;
                    CURRENT_TAG = activityTitles[6];
                    fragment=new EmpDetails();
                    break;


                case R.id.CLeave:
                    navItemIndex = 7;
                    Titleindex=7;
                    CURRENT_TAG = activityTitles[7];
                    fragment=new CompensateFragment();
                    break;

                default:
                    navItemIndex = 0;
                    Titleindex=0;
                    CURRENT_TAG = activityTitles[0];
                    fragment=new Dashboard();
                    break;
            }

            menuItem.setChecked(true);
            loadHomeFragment(fragment);

            return true;
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayt, toolbar, R.string.app_name, R.string.dummy_content) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayt.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }




    private void loadHomeFragment( Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();
        assert getSupportActionBar()!=null;
        getSupportActionBar().setTitle(activityTitles[Titleindex]);

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) { drawerLayt.closeDrawers();return; }

        drawerLayt.closeDrawers();

        invalidateOptionsMenu();
    }

    private void getHomeFragment() {
        Fragment fragment=null;
        Titleindex=navItemIndex;
        CURRENT_TAG = activityTitles[navItemIndex];

        switch (navItemIndex) {
            case 0:
                fragment=new Dashboard();
                break;

            case 1:
                fragment=new Report_Frag();
                break;

            case 2:
                fragment=new LConfirmationFrag();
                break;

            case 3:
                fragment=new AssignEmplysFrag();
                break;

            case 4:

               /* bb.getString("weekday");
                ArrayList<String> ar = bb.getStringArrayList("ids");

                  Log.i("ar", String.valueOf(ar));*/
                fragment = new AssignWeekOff_Frag();

                //fragment.setArguments(bb);

                break;

            case 5:

                if(name==null)
                { fragment=new ProfileFrag();

                }else{
                    fragment=new ProfileEdit();

                }break;

            case 6:


                fragment=new EmpDetails();
                break;

            case 7:
                fragment = new CompensateFragment();
                break;

            default:
                fragment=new Dashboard();

        }
        loadHomeFragment(fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        new AlertDialog.Builder(this).setMessage("Do you Want to Logout")
                .setPositiveButton("Yes", (dialog, id) -> {


                    sp.ClearAll();
                    Intent b =new Intent(getApplicationContext(),Login_Main_Act.class);
                    b.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(b);

                })
                .setNegativeButton("No", (dialog, id) -> {})
                .setCancelable(true).create().show();
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {

        if (exit) {finish();
        } else {
            Toast.makeText(this, "Press Back again to Exit.",Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(() -> exit = false, 3 * 1000);
        }
    }
}
