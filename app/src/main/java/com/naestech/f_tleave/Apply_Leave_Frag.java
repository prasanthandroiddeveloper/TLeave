package com.naestech.f_tleave;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.naestech.f_tleave.utils.Config;
import com.naestech.f_tleave.utils.Date_Picker_Dialog;
import com.naestech.f_tleave.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.naestech.f_tleave.utils.Config.LEAVEINSERTDATA;
import static com.naestech.f_tleave.utils.Config.UPDATERSN;


public class Apply_Leave_Frag extends Fragment {


    public Apply_Leave_Frag() { }

    SharedPrefs sp;

    List<String> spnamelist,idslist,fcmlist;


    Button singlebtn,multiplebtn,submit;
    LinearLayout mullyt,sllyt,fllyt,tllyt,asgnlyt;
    String Fromdate,Todate,btnType,rsn,permission_hrs,
            dept_name,ename,Suppname,ltype,fetch_fcm,permsn_hrs,Todaydate,stainsrt,fetch_email;
    int Dpt_Id,userid;
    TextView FromTv,ToTv,tv,Adminname,fulltv,halftv,weektv,subtv;
    EditText reason,hrs;

    Progress_Dialog pdialog;
    RelativeLayout rlyt;
    long minDate = System.currentTimeMillis() - 1000;
    String FromDate,ToDate;
    Dialog subdlg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apply_leave, container, false);

        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE );

        FirebaseApp.initializeApp(getActivity());

        pdialog = new Progress_Dialog(getActivity());
        pdialog.setCancelable(false);
        Objects.requireNonNull(pdialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        FromDate = Utils.DatetoStr(System.currentTimeMillis(),0);
        ToDate = Utils.DatetoStr(System.currentTimeMillis()+86400000L,0);

       // blyt = view.findViewById(R.id.btnllyt);
        submit = view.findViewById(R.id.submit);
        rlyt = view.findViewById(R.id.RLlyt);
        singlebtn = view.findViewById(R.id.singlebtn);
        multiplebtn = view.findViewById(R.id.multiplebtn);

        mullyt = view.findViewById(R.id.multiplellyt);

        tv = view.findViewById(R.id.tv);
        hrs = view.findViewById(R.id.hrs);

        fllyt=view.findViewById(R.id.fllyt);
        tllyt=view.findViewById(R.id.tllyt);
        asgnlyt= view.findViewById(R.id.AsgnLyt);
        reason = view.findViewById(R.id.reasonEt);
        Adminname = view.findViewById(R.id.ANameTv);


        sp = new SharedPrefs(getActivity());
        Dpt_Id = Integer.parseInt(sp.getUId());
        ename = sp.getUName();
        userid = sp.getUTypeId();
        dept_name = sp.getdeptname();
        fetch_email = sp.getemail();

        Log.i("Did", String.valueOf(Dpt_Id));

        getspindata();


        multiplebtn.setOnClickListener(v -> {

            btnType = "multiple";
            stainsrt = "Pending";

            multiplebtn.setBackgroundResource(R.drawable.select);
            singlebtn.setBackgroundResource(R.drawable.btn_gradient);

            fllyt.setVisibility(View.VISIBLE);
            reason.setVisibility(View.VISIBLE);
            asgnlyt.setVisibility(View.VISIBLE);
            tllyt.setVisibility(View.VISIBLE);

            hrs.setVisibility(View.GONE);

            if(FromTv != null ){ FromTv.setText(""); }
            if(ToTv != null ){ ToTv.setText(""); }
            if(reason != null){ reason.setText(""); }

        });

        submit.setOnClickListener(v -> {
            rsn = reason.getText().toString();
            permission_hrs = hrs.getText().toString();
            Fromdate = FromTv.getText().toString();
            Todate = ToTv.getText().toString();

            ltype = "";

            //todo changed here 06-12-2019


            if(btnType.equals("single")){

                subdlg=new Dialog(getActivity());




                subdlg.setContentView(R.layout.slctleave);
                Objects.requireNonNull(subdlg.getWindow()).setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                subdlg.show();




                fulltv=subdlg.findViewById(R.id.fulldayTV);
                halftv=subdlg.findViewById(R.id.halfdayTV);
                weektv=subdlg.findViewById(R.id.woffTV);
                subtv=subdlg.findViewById(R.id.subTV);
                fulltv.setOnClickListener(view1 -> {
                    ltype = "fullday";
                    stainsrt = "Pending";
                    fulltv.setBackgroundResource(R.drawable.select);
                    halftv.setBackgroundResource(R.drawable.btn_day);
                    weektv.setBackgroundResource(R.drawable.btn_day);
                });
                halftv.setOnClickListener(view1 -> {
                    ltype = "halfday";
                    stainsrt = "Pending";
                    halftv.setBackgroundResource(R.drawable.select);
                    fulltv.setBackgroundResource(R.drawable.btn_day);
                    weektv.setBackgroundResource(R.drawable.btn_day);
                });
                weektv.setOnClickListener(view1 -> {
                    ltype = "weekoff";
                    stainsrt="Pending";

                    weektv.setBackgroundResource(R.drawable.select);
                    halftv.setBackgroundResource(R.drawable.btn_day);
                    fulltv.setBackgroundResource(R.drawable.btn_day);
                });
                subtv.setOnClickListener(view1 ->
                {
                    if(Utils.isNullOrEmpty(Suppname)){
                        Utils.makeToast(getActivity(), "Lead not Assigned");
                    }
                    else
                    { getdata2(); }
                });

            } else{
                if(Utils.isNullOrEmpty(Suppname)){
                    Utils.makeToast(getActivity(), "Lead not Assigned");
                }
                else
                {
                    getdata2();
                }
            }

        });

        fllyt.setOnClickListener(v -> {
            submit.setVisibility(View.VISIBLE);
            stdate();
        });

        FromTv = view.findViewById(R.id.fromTV);
        ToTv = view.findViewById(R.id.toTV);

        singlebtn.setOnClickListener(v -> {

            btnType = "single";

            asgnlyt.setVisibility(View.VISIBLE);
            singlebtn.setBackgroundResource(R.drawable.select);
            multiplebtn.setBackgroundResource(R.drawable.btn_gradient);

            fllyt.setVisibility(View.VISIBLE);
            tllyt.setVisibility(View.GONE);
            tv.setBackgroundColor(Color.WHITE);
            reason.setVisibility(View.VISIBLE);

            if(FromTv != null ){ FromTv.setText(""); }
            if(reason != null){ reason.setText(""); }
        });

        Todaydate = String.valueOf(android.text.format.DateFormat.format("dd-MM-yyyy", new java.util.Date()));

        return view;


    }
    public void stdate() {

        minDate = System.currentTimeMillis() - 1000;

        new Date_Picker_Dialog(getActivity(),minDate,System.currentTimeMillis() - 1000+31536000000L).DateDialog(sdate -> {

            FromDate = sdate;
            Calendar newcal = Calendar.getInstance();
            newcal.setTime(Utils.StrtoDate(0,sdate));
            newcal.add(Calendar.DATE, 1);

            minDate = newcal.getTimeInMillis();

            ToDate = Utils.DatetoStr(newcal.getTime(),0);
            FromTv.setText(Utils.ChangeDateFormat(FromDate,9));

            if(btnType.equals("multiple")){
                etdate();
            }

        });
    }
    public void etdate() {

        Calendar newcal = Calendar.getInstance();
        newcal.setTime(Utils.StrtoDate(0,FromDate));
        newcal.add(Calendar.DATE, 1);
        minDate = newcal.getTimeInMillis();

        new Date_Picker_Dialog(getActivity(),minDate,System.currentTimeMillis() - 1000+31536000000L).DateDialog(sdate -> {
            ToDate = sdate;
            ToTv.setText(Utils.ChangeDateFormat(ToDate,9));

        });
    }
    private void getspindata() {

        spnamelist = new ArrayList<>();
        idslist = new ArrayList<>();
        fcmlist = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.GETSPINNERDATA, response -> {

            Log.i("resp1",response);

            try {

                JSONArray jarr = new JSONArray(response);

                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject json = jarr.getJSONObject(i);

                    Suppname = json.getString("asgn_to");
                    Adminname.setText(Suppname);
                }

                getfcmtoken();

            } catch (JSONException e) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("empname", ename);
                Log.i("spparams", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);
    }

    private void getfcmtoken(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.FETCHFCMTOKEN, response -> {

            Log.i("t",response);
            try {
                JSONArray jarry = new JSONArray(response);

                for(int i=0;i<jarry.length();i++){

                    JSONObject jobj = jarry.getJSONObject(i);

                    fetch_fcm = jobj.getString("fcm_token");
                    Log.i("fetch_fcm",fetch_fcm);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            Toast.makeText(getActivity(),"something went wrong Try again 2",Toast.LENGTH_SHORT).show();
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Suppname", Suppname);
                Log.i("iparams", String.valueOf(params));
                return params;
            }
        };

        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);
    }

    private void getnot() {
        pdialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.GETNOT, response -> {
            pdialog.dismiss();
            Log.i("sr",response);
        }, error -> {
            Toast.makeText(getActivity(),"something went wrong Try again 1",Toast.LENGTH_SHORT).show();
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(userid));
                params.put("fcm_token", fetch_fcm);
                params.put("S_date", Fromdate);
                params.put("e_name", ename);
                params.put("freason", rsn);
                params.put("T_date", Todate);
                Log.i("ja", String.valueOf(params));
                return params;
            }
        };


        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);
    }
    private void getdata2() {
        Fromdate = FromTv.getText().toString();
        Todate = ToTv.getText().toString();
        pdialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.GETDATA,response -> {
            pdialog.dismiss();
            Log.i("fetchres",response);

            if(!(response.equals("No Result"))){
                Toast.makeText(getActivity()," Already applied leave for this date",Toast.LENGTH_SHORT).show();

                if(btnType.equals("single")){
                    subdlg.dismiss();
                }

                try {
                    JSONArray jarry = new JSONArray(response);

                    for(int i=0;i<jarry.length();i++){

                        JSONObject jobj = jarry.getJSONObject(i);

                        String stats = jobj.getString("status");
                        Log.i("status",stats);

                        if( stats.equals("Not Approved") || stats.equals("Pending") ){

                            Snackbar snackbar = Snackbar.make(rlyt, "Leave Not Approved So Sent notofication ", Snackbar.LENGTH_LONG);
                            snackbar.show();

                            getupdatersn();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else{

                if(Suppname!=null){ insertrow();}
                else{ Utils.makeToast(getActivity(),"Lead Not Assigned"); }
            }

        },error -> {

        }){
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("f_name",ename);
                params.put("f_id", String.valueOf(userid));
                params.put("date",Fromdate );
                Log.i("fpar", String.valueOf(params));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);

    }
    private void getupdatersn() {
        pdialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATERSN, response -> {
            pdialog.dismiss();

            reason.setText("");
            hrs.setText("");
            FromTv.setText("");

            ToTv.setText("");

            submit.setVisibility(View.GONE);
            getnot();
            Log.i("rsnRsp", response);
        }, error -> {
            Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name",ename);
                params.put("id", String.valueOf(userid));
                params.put("date", Fromdate);
                params.put("reason", rsn);
                //params.put("type", btnType);
                Log.i("par", String.valueOf(params));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);
    }
    private void insertrow() {
        pdialog.show();
        if(btnType.equals("single")){

            if(Fromdate.equals("") || rsn.length() == 0 || Suppname == null || ltype.equals("")){
                pdialog.dismiss();
                subdlg.dismiss();

                Snackbar snackbar = Snackbar.make(rlyt, "Fields should not be empty", Snackbar.LENGTH_LONG);
                snackbar.show();
            }else{
                insertdata();
            }

        }else {
            if(Fromdate.equals("") || rsn.length() == 0 || Suppname == null ){
                pdialog.dismiss();

                Snackbar snackbar = Snackbar.make(rlyt, "Fields should not be empty", Snackbar.LENGTH_LONG);
                snackbar.show();
            }else{
                insertdata();
            }
        }



    }


    private void insertdata() {
        pdialog.show();
        getnot();
        // getmail();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LEAVEINSERTDATA, response -> {
            pdialog.dismiss();

            Log.i("ServerResponse", response);
            if(btnType.equals("single")){
                subdlg.dismiss();
            }

            reason.setText("");
            hrs.setText("");
            FromTv.setText("");
            ToTv.setText("");


            Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
            dialog.setContentView(R.layout.textview_layout);
            ((TextView)dialog.findViewById(R.id.txt)).setText("You have sucessfully Applied Leave"+" "+Fromdate+" "+Todate);
            dialog.show();



        }, error -> {
            Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_LONG).show();

        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("type", btnType);
                params.put("dept_id", String.valueOf(Dpt_Id));
                params.put("FDate", Fromdate);
                params.put("reason", rsn);
                params.put("apr_by", Suppname);
                params.put("TDate", Todate);

                params.put("status",stainsrt);
                params.put("user_id", String.valueOf(userid));
                params.put("ltype", (ltype!=null) ? ltype:" ");
                Log.i("par", String.valueOf(params));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);


    }

}
