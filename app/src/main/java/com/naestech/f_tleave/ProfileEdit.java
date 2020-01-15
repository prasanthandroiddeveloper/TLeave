package com.naestech.f_tleave;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.naestech.f_tleave.utils.Config;
import com.naestech.f_tleave.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.naestech.f_tleave.utils.Config.UPDATEPROF;


public class ProfileEdit extends Fragment {


    public ProfileEdit() {
        // Required empty public constructor
    }
    View v;

    EditText nameetx,mobiletx,emailtx,dobetx,dojetx,addressetx;
    String name,mobile,email,dob,doj,addrs;
    int userid;
    SharedPrefs sp;
    Button savebtn;
    TextView edittv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile_edit, container, false);



        nameetx=v.findViewById(R.id.nameET);
        mobiletx=v.findViewById(R.id.mobileET);
        emailtx=v.findViewById(R.id.EmailET);
        dobetx=v.findViewById(R.id.dobET);
        dojetx=v.findViewById(R.id.dojET);
        addressetx=v.findViewById(R.id.addrsET);
        savebtn =v.findViewById(R.id.saveBTN);
        edittv=v.findViewById(R.id.editTV);

        check();


        edittv.setOnClickListener(view -> {
            nameetx.setFocusable(true);nameetx.requestFocus();nameetx.setEnabled(true);
            mobiletx.setEnabled(true);
            emailtx.setEnabled(true);
            dobetx.setEnabled(true);
            addressetx.setEnabled(true);
            dojetx.setEnabled(true);
        });

        savebtn.setOnClickListener(view -> {
            check();
            update();
        });

        sp = new SharedPrefs(Objects.requireNonNull(getActivity()));
        userid= sp.getUTypeId();
        fetchdetails();
        return v;
    }
    private void check(){
        nameetx.setEnabled(false);
        mobiletx.setEnabled(false);
        emailtx.setEnabled(false);
        dobetx.setEnabled(false);
        addressetx.setEnabled(false);
        dojetx.setEnabled(false);
    }


    private void update(){

        name=nameetx.getText().toString().trim();
        mobile=mobiletx.getText().toString().trim();
        email=emailtx.getText().toString().trim();
        dob=dobetx.getText().toString().trim();
        addrs=addressetx.getText().toString().trim();
        doj=dojetx.getText().toString().trim();


        if(Utils.isNullOrEmpty(name) || Utils.isNullOrEmpty(mobile)
                ||Utils.isNullOrEmpty(addrs) || Utils.isNullOrEmpty(dob) || Utils.isNullOrEmpty(doj) ){
            Utils.makeToast(getActivity(),"Please Fill all the fields");
        }



        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATEPROF, response -> Log.i("rsnRsp", response), error -> {

            Utils.makeToast(getActivity(),"Profile Edit Success");
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("mobile", mobile);
                params.put("email", email);
                params.put("dob", dob);
                params.put("addrs", addrs);
                params.put("doj", doj);
                params.put("userid", String.valueOf(userid));
                Log.i("par", String.valueOf(params));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);


    }

    private void fetchdetails(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.GETPROFILE, response -> {

            Log.i("fetchres",response);

            try {
                JSONArray jarry = new JSONArray(response);

                for(int i=0;i<jarry.length();i++){

                    JSONObject jobj = jarry.getJSONObject(i);

                    name = jobj.getString("name"); nameetx.setText(name);
                    mobile= jobj.getString("phone_no"); mobiletx.setText(mobile);
                    email =  jobj.getString("email");emailtx.setText(email);
                    dob = jobj.getString("date_of_birth");dobetx.setText(dob);
                    doj = jobj.getString("date_of_joining");dojetx.setText(doj);
                    addrs = jobj.getString("prmnt_addrs");addressetx.setText(addrs);
                }
            } catch (JSONException e) {
                //  e.printStackTrace();
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        },error -> {

        }){
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(userid));
                Log.i("fpar", String.valueOf(params));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);
    }



}
