package com.naestech.f_tleave;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.naestech.f_tleave.utils.Config.FETCHFCMDATA;
import static com.naestech.f_tleave.utils.Config.LEAVEAPPROVE;
import static com.naestech.f_tleave.utils.Config.LEAVECONFIRMATION;

public class Approve_Leave_Act extends AppCompatActivity {

    Button aprv,notaprv;
    TextView name,reason,count,pmsn,datetv;
    String empname,Today,E_name,E_reason,E_daycount,E_FDate,status,fcm_name,E_Status,
            fcm_sdate,fcm_id,fcm_rsn,fcm_today,bund_id,permission,NotApprovedRsn,statuss,AsgnTo,LAsgn;
    double dcnt;
    int eid;
    int uid;
    LinearLayout btns,noofdayslyt,rsnlyt;
    EditText naet;
    SharedPrefs spfs;
    Progress_Dialog pdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_leave);


        spfs = new SharedPrefs(getApplicationContext());
        empname = spfs.getUName();
        uid = spfs.getUTypeId();
        Log.i("empname1",empname);
        btns = findViewById(R.id.LlytBtn);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        pdialog = new Progress_Dialog(this);
        pdialog.setCancelable(false);
        Objects.requireNonNull(pdialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        pdialog.show();
        Bundle bb = getIntent().getExtras();
        if(bb != null) {

            E_name=bb.getString("name");
            E_reason=bb.getString("reason");
            E_Status=bb.getString("status");

            dcnt=bb.getDouble("dayscount");

            eid=bb.getInt("bid");
            // Log.i("eid", String.valueOf(eid));
            bund_id= String.valueOf(eid);
            // Log.i("bund_id", bund_id);
            E_daycount = String.valueOf(dcnt);
            E_FDate = bb.getString("fdate");
            // permission = bb.getString("permission");

        }

        fcm_name = getIntent().getExtras().getString("name");
        fcm_id = getIntent().getExtras().getString("id");
        fcm_sdate = getIntent().getExtras().getString("from_date");
        fcm_rsn = getIntent().getExtras().getString("reason");
        fcm_today = getIntent().getExtras().getString("to_date");

       /* Log.i("fcm_name",fcm_name);
        Log.i("fcm_id",fcm_id);
        Log.i("fcm_sdate",fcm_sdate);
        Log.i("fcm_rsn",fcm_rsn);*/

        aprv = findViewById(R.id.approveBtn);
        notaprv = findViewById(R.id.notapproveBtn);
        name = findViewById(R.id.EnameTv);
        reason = findViewById(R.id.AreasonTv);
        count = findViewById(R.id.DcountTv);
        // pmsn = findViewById(R.id.PmsnTv);
        noofdayslyt = findViewById(R.id.noofdaysllyt);
        //permissionlyt = findViewById(R.id.permsnllyt);
        naet = findViewById(R.id.NARsnEt);

        datetv = findViewById(R.id.DateTv);
        rsnlyt=findViewById(R.id.rsnLNR);


        if ( E_name != null && E_FDate != null && bund_id != null ) {
            name.setText(E_name);
            reason.setText(E_reason);
            count.setText(E_daycount);
            // pmsn.setText(permission);
            datetv.setText(E_FDate);
            if(E_daycount == "0"){
                noofdayslyt.setVisibility(View.GONE);
            }

        }else{
            getfcmdata();
        }

        aprv.setOnClickListener(v -> {
            aprv.setBackgroundResource(R.drawable.btn_plain);
            aprv.setTextColor(Color.BLACK);
            notaprv.setBackgroundResource(R.drawable.btn_clr);
            notaprv.setTextColor(Color.WHITE);
            status = "Approved";
            getdata();
        });
        notaprv.setOnClickListener(v -> {
            notaprv.setBackgroundResource(R.drawable.btn_plain);
            aprv.setBackgroundResource(R.drawable.btn_clr);
            aprv.setTextColor(Color.WHITE);
            notaprv.setTextColor(Color.BLACK);
            status = "Not Approved";
            getdata();
        });

        getinfo();
    }

    private void getinfo() {
        // pdialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,LEAVECONFIRMATION, response -> {
            pdialog.dismiss();
            Log.i("datares",response);
            try {
                JSONArray jarry = new JSONArray(response);

                for(int i=0;i<jarry.length();i++){

                    JSONObject jobj = jarry.getJSONObject(i);


                    statuss = jobj.getString("status");
                    Log.i("stat",statuss);

                    LAsgn = jobj.getString("asgn_to");
                    Log.i("LAsgn",LAsgn);

                    AsgnTo = jobj.getString("assigned_to");
                    Log.i("AsgnTo",AsgnTo);

                    //Toast.makeText(this, statuss, Toast.LENGTH_SHORT).show();

                    if(((statuss.equals("Pending")) ||(statuss.equals("Not Approved"))) && (empname.equals(AsgnTo))){
                        btns.setVisibility(View.VISIBLE);
                    } else{
                        btns.setVisibility(View.GONE);
                        Toast.makeText(this, "Leave Approval is given to their respective leads", Toast.LENGTH_SHORT).show();
                    }

                    if((empname.equals(AsgnTo))){
                        rsnlyt.setVisibility(View.VISIBLE);
                    } else{
                        rsnlyt.setVisibility(View.GONE);
                        // Toast.makeText(this, "Leave Approval is given to their respective leads", Toast.LENGTH_SHORT).show();
                    }

                }

            }catch(JSONException e){
                // Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "SomeThing Went Wrong", Toast.LENGTH_SHORT).show();
            }

        },error -> {
            Toast.makeText(this,"something went wrong", Toast.LENGTH_SHORT).show();
        })
        {
            @Override
            protected Map<String, String> getParams()  {

                Map<String,String> params = new HashMap<>();

                if (E_name != null && E_FDate != null && bund_id != null) {
                    params.put("fdate",E_FDate);
                    params.put("emname",E_name);
                    params.put("id", bund_id);
                    Log.i("lparams", String.valueOf(params));
                }else {
                    params.put("fdate",fcm_sdate);
                    params.put("emname",fcm_name);
                    params.put("id", fcm_id);

                    Log.i("fparams", String.valueOf(params));
                }
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void getdata() {

          pdialog.show();
        NotApprovedRsn = naet.getText().toString();
        Log.i("text",NotApprovedRsn);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,LEAVEAPPROVE,response -> {
            pdialog.dismiss();
            Log.i("alres",response);
            btns.setVisibility(View.GONE);
            naet.setVisibility(View.GONE);

        },error -> {
            Toast.makeText(this,"something went wrong", Toast.LENGTH_SHORT).show();
        })
        {
            @Override
            protected Map<String, String> getParams(){

                Map<String,String> params = new HashMap<>();

                if (E_name != null && E_FDate != null && bund_id != null) {
                    params.put("fdate",E_FDate);
                    params.put("emname",E_name);
                    params.put("id", bund_id);
                    params.put("status",status);
                    if(status == "Not Approved"){
                        params.put("rejectleaversn",NotApprovedRsn);
                    }else{
                        params.put("rejectleaversn","");
                    }
                    Log.i("lparams", String.valueOf(params));
                }else {
                    params.put("fdate",fcm_sdate);
                    params.put("emname",fcm_name);
                    params.put("id", fcm_id);
                    params.put("status",status);
                    if(status == "Not Approved"){
                        params.put("rejectleaversn",NotApprovedRsn);
                    }else{
                        params.put("rejectleaversn","");
                    }
                    Log.i("fparams", String.valueOf(params));
                }
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        // requestQueue.setRetryPolicy(new DefaultRetryPolicy( 50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void getfcmdata(){
//        pdialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,FETCHFCMDATA, response -> {

            //  pdialog.dismiss();
            Log.i("response1",response);

            try {

                JSONArray jarr = new JSONArray(response);
                for(int i = 0; i< jarr.length(); i++) {

                    JSONObject json = jarr.getJSONObject(i);

                    String F_Name= json.getString("name");
                    String F_Rsn =  json.getString("reason");
                    String F_Dcnt = json.getString("no_of_days");
                    //String permsn = json.getString("permission");
                    String F_Date = (json.getString("from_date")+"\n"+json.getString("to_date"));

                    Log.i("F_Date",F_Date);

                    name.setText(F_Name);
                    reason.setText(F_Rsn);
                    count.setText(F_Dcnt);
                    //  pmsn.setText(permsn);
                    datetv.setText(F_Date);

                    if(F_Dcnt.equals("0")){
                        noofdayslyt.setVisibility(View.GONE);
                    }

                }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            Toast.makeText(getApplicationContext(), "Internet Connection is not available.", Toast.LENGTH_LONG).show();
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("f_name", fcm_name);
                params.put("f_date", fcm_sdate);
                params.put("f_id", fcm_id);

                Log.i("fdparam", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

}
