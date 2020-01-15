package com.naestech.f_tleave;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.naestech.f_tleave.utils.Config.LOGIN;
import static com.naestech.f_tleave.utils.Config.UPDATEPASS;


public class Login_Frag extends Fragment {


    public Login_Frag() { }
    String Email_Mob,Passwd,name,deptname,F_Email,Admin_asgn,newpass,cnfpss,updt;
    EditText EmailEt,PassEt;
    Activity activity;
    LinearLayout MainLayt,frgtlnr;
    int Dept_id,id;
    TextView frgt;

    EditText newpassEt,cnfpassEt,emailet;
    Button updte;
    String es,ns,cs;

    SharedPrefs sp;

    Dialog log;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        activity = getActivity();
        EmailEt = view.findViewById(R.id.unameEt);
        PassEt = view.findViewById(R.id.pswdEt);
        MainLayt = view.findViewById(R.id.mainLayt);
        frgt = view.findViewById(R.id.frgtTV);




        // view.findViewById(R.id.fgtpwd).setOnClickListener(v -> frgtdialog());
        view.findViewById(R.id.btn_login).setOnClickListener(v -> validlogin());

        sp = new SharedPrefs(activity);

        if(sp.getLoggedin()){
            startActivity(new Intent(getActivity(),Main_Screen_Activity.class));
            Objects.requireNonNull(getActivity()).finish();
        }



        frgt.setOnClickListener(view1 -> {



            log=new Dialog(Objects.requireNonNull(getActivity()));
            log.setContentView(R.layout.forgot_password);



            emailet=log.findViewById(R.id.enmailET);
            newpassEt = log.findViewById(R.id.nwpET);
            cnfpassEt = log.findViewById(R.id.CnfET);
            updte = log.findViewById(R.id.updtBTN);

            log.show();
            Objects.requireNonNull(log.getWindow()).setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            updte.setOnClickListener(view2 -> {
                update();


                // Toast.makeText(activity, "this", Toast.LENGTH_SHORT).show();
            });

        });


        return view;
    }
    private void validlogin() {

        Email_Mob = EmailEt.getText().toString().trim();
        Passwd = PassEt.getText().toString();




        // encpswrd = Base64.encodeToString(Passwd.getBytes(), Base64.DEFAULT);

        if(Email_Mob.equals("")){
            Snackbar.make(MainLayt,"Enter Email ",Snackbar.LENGTH_SHORT).show();
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(Email_Mob).matches() ){

            Snackbar.make(MainLayt, "Enter Valid Email", Snackbar.LENGTH_SHORT).show();

        }else{
            //  Snackbar.make(MainLayt, "Success", Snackbar.LENGTH_SHORT).show();
            UserLogin();
        }
    }

    public void UserLogin() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,LOGIN,
                ServerResponse -> {

                    Log.i("re",ServerResponse);
                    //  Toast.makeText(activity, ServerResponse, Toast.LENGTH_SHORT).show();

                    Bundle bb = new Bundle();

                    if(ServerResponse.equals("error")){
                        Toast.makeText(activity, "Invalid UserName and Password", Toast.LENGTH_SHORT).show();
                    }else{
                        try {
                            JSONObject jObj = new JSONObject(ServerResponse);
                            Dept_id = jObj.getInt("dept_type_id");
                            name = jObj.getString("name");
                            deptname = jObj.getString("dept_type");

                            id = jObj.getInt("id");
                            F_Email = jObj.getString("email");
                            Admin_asgn = jObj.getString("asgn_to");

                            Log.i("Dept_id", String.valueOf(Dept_id));
                            Log.i("mailid", name);
                            Log.i("dame", deptname);
                            Log.i("Admin_asgn", Admin_asgn);
                            Log.i("id", String.valueOf(id));

                          /*  if(Admin_asgn != null){
                                fetchemail();
                            }
*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(getActivity(),Main_Screen_Activity.class);
                        intent.putExtra("Index",0);
                        sp.setUserDet(String.valueOf(Dept_id),name,deptname,F_Email);
                        sp.setUTypeId(id);
                        sp.setLoggedin(true);


                        startActivity(intent);
                    }

                },
                volleyError -> Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("Email", Email_Mob);
                params.put("Password", Passwd);

                Log.i("pa1", String.valueOf(params));
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }


    public void update() {

        es=emailet.getText().toString();
        ns=newpassEt.getText().toString();
        cs=cnfpassEt.getText().toString();

        if(!es.contains("tripnetra.com")){
            Toast.makeText(activity, "Enter Company Email id", Toast.LENGTH_SHORT).show();
        }
        else{
            StringRequest stringRequest = new StringRequest(Request.Method.POST,UPDATEPASS,
                    ServerResponse -> {

                        Log.i("re",ServerResponse);
                        log.dismiss();
                        Toast.makeText(activity, ServerResponse, Toast.LENGTH_SHORT).show();
                    },
                    volleyError -> Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_LONG).show()) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();

                    params.put("password", ns);
                    params.put("email", es);

                    Log.i("pa1", String.valueOf(params));
                    return params;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
            requestQueue.add(stringRequest);

        }}

}
