package com.naestech.f_tleave;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.naestech.f_tleave.utils.Config.EMPREG;


public class Signup_Frag extends Fragment implements AdapterView.OnItemSelectedListener{


    public Signup_Frag() { }

    Activity activity;
    EditText NameEt,MobEt,EmailEt,PaswdEt,RPsedEt,Apwd;
    LinearLayout MainLayt;
    String Name,Email,Pswd,NPswd,spn_dept_type,dept_id,ETadminpwd,role_id;
    String a,b,c,d;
    Spinner dept;
    String[] department_Type ={"Department_Type","Admin","Development","Support","SEO","Super_Admin"};



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup, container, false);


        activity = getActivity();

       /* Signup_Frag articleFrag = (Signup_Frag)
                getSupportFragmentManager().findFragmentById(R.id.);*/
        NameEt = view.findViewById(R.id.nameEt);

        EmailEt = view.findViewById(R.id.emailEt);
        PaswdEt = view.findViewById(R.id.pwdEt);
        RPsedEt = view.findViewById(R.id.rpwdEt);
        MainLayt = view.findViewById(R.id.mainLayt);
        Apwd = view.findViewById(R.id.ApwdEt);

        //   enc = Base64.encodeToString(bndl.getBytes(), Base64.DEFAULT);


        dept = view.findViewById(R.id.deptspn);

        dept.setOnItemSelectedListener(this);


        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,department_Type);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dept.setAdapter(aa);

       /* a=NameEt.getText().toString();
        b=EmailEt.getText().toString();
        c=PaswdEt.getText().toString();
        d=RPsedEt.getText().toString();*/

        view.findViewById(R.id.btn_register).setOnClickListener(v -> validate_data());

        return view;
    }
    private void validate_data() {

        Name = NameEt.getText().toString();
        Email = EmailEt.getText().toString();
        Pswd = PaswdEt.getText().toString();
        NPswd = RPsedEt.getText().toString();
        ETadminpwd=Apwd.getText().toString();


        Log.i("ETadminpwd",ETadminpwd);

        if(Name.equals("")){
            Snackbar.make(MainLayt, "Enter Name", Snackbar.LENGTH_SHORT).show();
        }else if(Email.equals("") || (!(Email.contains("@tripnetra.com")))){
            Snackbar.make(MainLayt, Email.equals("") ? "Enter Email" : "Enter company Email id ends with tripnetra.com", Snackbar.LENGTH_SHORT).show();
        }else if(Pswd.equals("") || Pswd.length()<8){
            Snackbar.make(MainLayt, Pswd.equals("") ? "Enter Password" : "Password Should be more than 8 Cahracters", Snackbar.LENGTH_SHORT).show();
        }else if(!Pswd.equals(NPswd)){
            Snackbar.make(MainLayt, "Passwords do not Match", Snackbar.LENGTH_SHORT).show();
        }else if((spn_dept_type.equals("Admin")) && (!ETadminpwd.equals("Tripadmin"))){
            //UserRegistration();
            Snackbar.make(MainLayt, "Enter valid Password", Snackbar.LENGTH_SHORT).show();
        }else if((spn_dept_type.equals("Super_Admin")) && (!ETadminpwd.equals("Trip@superadmin"))){
            //UserRegistration();
            Snackbar.make(MainLayt, "Enter valid Password", Snackbar.LENGTH_SHORT).show();
        }else{
            UserRegistration();
            // Snackbar.make(MainLayt, "Success", Snackbar.LENGTH_SHORT).show();
        }
    }

    public void UserRegistration(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, EMPREG,
                ServerResponse -> {

                    Log.i("ServerResponse",ServerResponse);
                    if(ServerResponse.equals("User Registered Successfully")){
                        Toast.makeText(getActivity(), ServerResponse, Toast.LENGTH_LONG).show();
                        NameEt.getText().clear();
                        EmailEt.getText().clear();
                        PaswdEt.getText().clear();
                        RPsedEt.getText().clear();

                        ((Login_Main_Act) Objects.requireNonNull(getActivity())).selectPage(0);
                    }else{
                        Toast.makeText(getActivity(), ServerResponse, Toast.LENGTH_LONG).show();
                    }

                },
                volleyError -> Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("Email", Email);
                params.put("Password",Pswd );
                params.put("Name", Name);
                params.put("Depttype", spn_dept_type);
                params.put("Dept_id", dept_id);
                params.put("role_id", role_id);

                Log.i("pa", String.valueOf(params));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spn_dept_type = department_Type[position];
        Log.i("pofday",spn_dept_type);

        if(department_Type[position] == "Admin"){
            Apwd.setVisibility(View.VISIBLE);
            dept_id = "1";
            role_id="1";
        }else if(department_Type[position] == "Development"){
            Apwd.setVisibility(View.GONE);
            dept_id = "2";
            role_id="0";
        }else if(department_Type[position] == "Support"){
            Apwd.setVisibility(View.GONE);
            dept_id = "3";
            role_id="2";
        }else if(department_Type[position] == "SEO"){
            Apwd.setVisibility(View.GONE);
            dept_id = "4";
            role_id="3";
        }else if(department_Type[position] == "Super_Admin"){
            Apwd.setVisibility(View.VISIBLE);
            dept_id = "6";
            role_id="5";

        }else {
            Apwd.setVisibility(View.GONE);
            dept_id = "5";
        }
        Log.i("dept_id",dept_id);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
