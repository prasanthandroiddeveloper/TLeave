package com.naestech.f_tleave;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.naestech.f_tleave.utils.Config;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class ProfileFrag extends Fragment {

    public ProfileFrag() {
        // Required empty public constructor
    }

    View v;
    EditText nameet,mobet,mob1et,padrset,tempadrset,emailet;
    String cpyadrs,dojs,names,phn,phn2,padrs,tpadrs,dob,dt,mnth,email,cemail;
    CheckBox adrscb;
    Button sdate,sbtn,birth;
    long mindate;
    int calmonth,calday,userid;
    DatePickerDialog datePickerDialog;
    Spinner dtspn,mntspn;
    SharedPrefs sp;
    ArrayAdapter<String> aa,aaa;
    String[] dates = {"01","02","03","04","05","06","07","08","09","10","11","12","13",
            "14","15","16","17","18","19","20","21","22","23",
            "24","25","26","27","28","29","30","31"};

    String[] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile, container, false);

        nameet = v.findViewById(R.id.nameET);
        mobet = v.findViewById(R.id.phnET);
        mob1et = v.findViewById(R.id.phnsET);
        adrscb = v.findViewById(R.id.adrsCB);
        padrset = v.findViewById(R.id.paET);
        tempadrset = v.findViewById(R.id.taET);
        emailet=v.findViewById(R.id.emailET);
        sdate = v.findViewById(R.id.sdateBtn);
        sbtn = v.findViewById(R.id.subBTN);
        birth=v.findViewById(R.id.birth);
       /* birth.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(),BirthdayWishes.class));
        });*/
        sp = new SharedPrefs(Objects.requireNonNull(getActivity()));
        userid= Integer.parseInt(String.valueOf(sp.getUTypeId()));
        cemail= sp.getmail();

        emailet.setText(cemail);

        Log.i("usr", String.valueOf(userid));


        dtspn=v.findViewById(R.id.dtSPNR);
        aa = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, dates);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dtspn.setAdapter(aa);
        dtspn.setPrompt("Date");
        dtspn.setOnItemSelectedListener(new SpinnerClass());

        mntspn=v.findViewById(R.id.mntSPNR);
        aaa = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, months);
        aaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mntspn.setAdapter(aaa);
        dtspn.setPrompt("Month");
        mntspn.setOnItemSelectedListener(new SpinnerClass1());

        sdate.setOnClickListener(this::onClick);
        adrscb.setOnClickListener(this::onClick2);
        sbtn.setOnClickListener(this::onClick3);


        return v;
    }
    private void onClick(View v) {
        mindate = System.currentTimeMillis() - 7776000000L;

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), (view, year, monthOfYear, dayOfMonth) -> {

            calmonth = (monthOfYear + 1);
            Log.i("calmonth", String.valueOf(calmonth));
            String sMonth;
            if (calmonth < 10) {
                sMonth = "0" + calmonth;
            } else {
                sMonth = String.valueOf(calmonth);
            }
            calday = dayOfMonth;
            Log.i("calday", String.valueOf(calday));
            String sday;
            if (calday < 10) {
                sday = "0" + calday;
            } else {
                sday = String.valueOf(calday);
            }
            dojs=sday + "-" + sMonth + "-" + year;
            sdate.setText(dojs);

        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void onClick2(View view12) {
        cpyadrs = padrset.getText().toString();
        tempadrset.setText(cpyadrs);

        if (!(adrscb.isChecked())) {
            tempadrset.setText("");
        }
    }

    private class SpinnerClass implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            //  Toast.makeText(v.getContext(), "Your choose :" + dates[i], Toast.LENGTH_SHORT).show();
            dt=dates[i];


        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private class SpinnerClass1 implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            mnth = months[i];
            Toast.makeText(v.getContext(), "" + months[i], Toast.LENGTH_SHORT).show();
            //  mnth=months[i];
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }


    private void onClick3(View view) {

        names = nameet.getText().toString().trim();
        phn = mobet.getText().toString().trim();
        phn2 = mob1et.getText().toString().trim();
        padrs = padrset.getText().toString().trim();
        tpadrs = tempadrset.getText().toString().trim();
        email=emailet.getText().toString().trim();
        dob = dt + "-" + mnth;

        //  Log.i("adrs", names + "\n" + phn + "\n" + phn2 + "\n" + padrs + "\n" + tpadrs + "\n" + dob);

        if (names.equals("")|| phn.equals("") || padrs.equals("") || email.equals("") || (!email.contains("tripnetra.com"))) {
            Toast.makeText(getActivity(), "Please Enter valid details", Toast.LENGTH_SHORT).show();
        } else {


            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), Main_Screen_Activity.class);
            intent.putExtra("Index",0);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.PROFILE,
                    ServerResponse -> {

                        Log.i("ServerResponse", ServerResponse);
                        if (ServerResponse.equals("Success")) {
//                            Toast.makeText(getActivity(), ServerResponse, Toast.LENGTH_LONG).show();
                            nameet.getText().clear();
                            mobet.getText().clear();
                            mob1et.getText().clear();
                            padrset.getText().clear();
                            tempadrset.getText().clear();
                            emailet.getText().clear();
                            dojs="";
                            sdate.setText("");
                            aa.notifyDataSetChanged();
                            aaa.notifyDataSetChanged();



                        } else {
                            Toast.makeText(getActivity(), ServerResponse, Toast.LENGTH_LONG).show();
                        }

                    },
                    volleyError -> Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_LONG).show()) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<>();

                    params.put("usrid", String.valueOf(userid));
                    params.put("name", names);
                    params.put("phn", phn);
                    params.put("phn2", phn2);
                    params.put("padrs", padrs);
                    params.put("tpadrs", tpadrs);
                    params.put("doj", dojs);
                    params.put("dob", dob);
                    params.put("email", email);

                    Log.i("pa", String.valueOf(params));

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
            requestQueue.add(stringRequest);
        }
    }


}
