package com.naestech.f_tleave;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.naestech.f_tleave.utils.Config.INSERTOKEN;
import static com.naestech.f_tleave.utils.Config.LEAVECOUNT;


public class Dashboard extends Fragment {

    public Dashboard() { }

    RecyclerView recyclerView;
    String Todaydate,caldate,category,dept_name,role,dep,day,FcmToken,type;
    Button sdate,leavebtn,applyLvebtn,suprt,Dev,seo,busact;
    long mindate;
    DatePickerDialog datePickerDialog;
    int calmonth,calday,dept_Id,userid;
    Progress_Dialog pdialog;
    SharedPrefs sp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_dashboard1, container, false);

        sp=new SharedPrefs(Objects.requireNonNull(getActivity()));
        dept_name = sp.getdeptname();
        dept_Id = Integer.parseInt(sp.getUId());

        userid = sp.getUTypeId();
        Log.i("dept_name",dept_name);

        Todaydate = String.valueOf(android.text.format.DateFormat.format("dd-MM-yyyy", new java.util.Date()));

        pdialog = new Progress_Dialog(getActivity());
        pdialog.setCancelable(false);
        Objects.requireNonNull(pdialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        recyclerView = view.findViewById(R.id.namecntRcylr);

        sdate = view.findViewById(R.id.sdateBtn);
        leavebtn = view.findViewById(R.id.LeaveBtn);
        // permsnbtn = view.findViewById(R.id.PermissionBtn);

        suprt = view.findViewById(R.id.supBtn);
        Dev = view.findViewById(R.id.devBtn);
        seo = view.findViewById(R.id.seoBtn);
        busact = view.findViewById(R.id.busBtn);

        sdate.setText(Todaydate);

        Toast.makeText(getActivity(), "dashboard", Toast.LENGTH_SHORT).show();


        if(dept_name.equals("Admin") || dept_name.equals("Super_Admin")){

            FirebaseApp.initializeApp(getContext());
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( getActivity(), instanceIdResult -> {
                FcmToken = instanceIdResult.getToken();
                Log.e("newToken",FcmToken);
                inserttoken();
                sp.setfcm(FcmToken);
            });

        }

        applyLvebtn = view.findViewById(R.id.aplyBtn);

        sdate.setOnClickListener(v -> {
            mindate = System.currentTimeMillis() - 7776000000L;

            datedialog();
            leavebtn.setBackgroundResource(R.drawable.btn_plain);
            leavebtn.setTextColor(Color.parseColor("#4e54c8"));
        });
        leavebtn.setOnClickListener(v -> {


            recyclerView.setVisibility(View.VISIBLE);
            leavebtn.setBackgroundResource(R.drawable.btn_grdnt1);
            leavebtn.setTextColor(Color.WHITE);


            getdata2();
        });


        applyLvebtn.setOnClickListener(v -> {
            Intent d = new Intent(getActivity(),Leave_info_Act.class);
            startActivity(d);
        });

        suprt.setOnClickListener(v -> {
            dep = "3";
            role="2";
            type="support";
            suprt.setBackgroundResource(R.drawable.btn_plain);
            suprt.setTextColor(Color.BLACK);
            seo.setBackgroundResource(R.drawable.btn_dboard);
            Dev.setBackgroundResource(R.drawable.btn_dboard);
            busact.setBackgroundResource(R.drawable.btn_dboard);
            Dev.setTextColor(Color.WHITE);
            seo.setTextColor(Color.WHITE);
            busact.setTextColor(Color.WHITE);

            getdata2();
        });


        busact.setOnClickListener(v -> {
            dep = "1";
            role="1";
            busact.setBackgroundResource(R.drawable.btn_plain);
            busact.setTextColor(Color.BLACK);
            seo.setBackgroundResource(R.drawable.btn_dboard);
            Dev.setBackgroundResource(R.drawable.btn_dboard);
            suprt.setBackgroundResource(R.drawable.btn_dboard);
            Dev.setTextColor(Color.WHITE);
            seo.setTextColor(Color.WHITE);
            suprt.setTextColor(Color.WHITE);

            getdata2();
        });

        seo.setOnClickListener(v -> {
            dep = "4";
            role="3";
            suprt.setBackgroundResource(R.drawable.btn_dboard);
            suprt.setTextColor(Color.WHITE);
            seo.setBackgroundResource(R.drawable.btn_plain);
            seo.setTextColor(Color.BLACK);
            Dev.setBackgroundResource(R.drawable.btn_dboard);
            Dev.setTextColor(Color.WHITE);
            busact.setBackgroundResource(R.drawable.btn_dboard);
            busact.setTextColor(Color.WHITE);
            getdata2();
        });

        Dev.setOnClickListener(v -> {
            dep = "2";
            role="0";
            suprt.setBackgroundResource(R.drawable.btn_dboard);
            suprt.setTextColor(Color.WHITE);
            seo.setBackgroundResource(R.drawable.btn_dboard);
            seo.setTextColor(Color.WHITE);
            Dev.setBackgroundResource(R.drawable.btn_plain);
            Dev.setTextColor(Color.BLACK);
            busact.setBackgroundResource(R.drawable.btn_dboard);
            busact.setTextColor(Color.WHITE);

            getdata2();
        });


        category = "Leave";
        caldate=Todaydate;
        suprt.setBackgroundResource(R.drawable.btn_plain);
        suprt.setTextColor(Color.BLACK);
        recyclerView.setVisibility(View.VISIBLE);
        leavebtn.setBackgroundResource(R.drawable.btn_grdnt1);
        leavebtn.setTextColor(Color.BLACK);



        day = Calendar.getInstance().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()); // Thu

        Log.i("dayOfWeek", day);


        role = "2";
        getdata2();
        return view;
    }
    private void inserttoken() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, INSERTOKEN, response -> {

            //getspindata();
        }, error -> {
            Toast.makeText(getActivity(),"something went wrong Try again",Toast.LENGTH_SHORT).show();
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fcm_token", sp.getfcm());
                params.put("dept_name", dept_name);
                params.put("uid", String.valueOf(userid));

                Log.i("iparams", String.valueOf(params));
                return params;
            }
        };

        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);
    }
    private void datedialog() {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        // date picker dialog
        datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), (view, year, monthOfYear, dayOfMonth) -> {

            calmonth = (monthOfYear +1);
            Log.i("calmonth", String.valueOf(calmonth));
            String sMonth;
            if (calmonth < 10) {
                sMonth = "0"+ calmonth;
            } else {
                sMonth = String.valueOf(calmonth);
            }
            calday = dayOfMonth;
            Log.i("calday", String.valueOf(calday));
            String sday;
            if (calday < 10) {
                sday = "0"+ calday;
            } else {
                sday = String.valueOf(calday);
            }

            sdate.setText(sday + "-" +sMonth + "-" + year);
            caldate = sdate.getText().toString();
            Log.i("caldate",caldate);
            if(!(Todaydate.equals(caldate))){
                dep = "3";
            }

        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void getdata2() {

        // pdialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,LEAVECOUNT, response -> {
            // pdialog.dismiss();
            Log.i("Lresponse1",response);

            try {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                JSONArray jarr = new JSONArray(response);

                List<DataAdapter> list = new ArrayList<>();
                for(int i = 0; i< jarr.length(); i++) {

                    DataAdapter adapter = new DataAdapter();
                    JSONObject json = jarr.getJSONObject(i);


                    String name = json.getString("name");
                    Log.i("name",name);
                    String Leavet = json.getString("leave_type");
                    Log.i("Leavet",Leavet);


                    if(Leavet.equals("compensate")){
                        adapter.setreason(json.getString("name")+" "+"(C)"+"-"+json.getString("CompensateLveCnt"));
                    }else if(Leavet.equals("weekoff")){
                        adapter.setreason(json.getString("name")+" "+"(W)");
                    }else{
                        adapter.setreason(json.getString("name"));
                    }

                    list.add(adapter);
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(new recycle_adapter(list));
            } catch (JSONException e) {
                recyclerView.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "No Leaves For This Date", Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            Toast.makeText(getActivity(), "No Leaves For This Date.", Toast.LENGTH_LONG).show();
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();
                //params.put("category",category);
                params.put("date",caldate);
                // params.put("dept_id", dep);
                params.put("role_id", role);
                Log.i("rparam", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);
    }
    public class recycle_adapter  extends RecyclerView.Adapter<recycle_adapter.ViewHolder>  {

        private Context context;
        private List<DataAdapter> listadapter;

        recycle_adapter(List<DataAdapter> list) {
            super();
            this.listadapter = list;
        }

        @NonNull
        @Override
        public recycle_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new recycle_adapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.names_rcylr, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull recycle_adapter.ViewHolder vh, int pos) {

            DataAdapter DDataAdapter =  listadapter.get(pos);


            vh.work.setVisibility(View.GONE);
            vh.dcounttV.setVisibility(View.GONE);
            vh.nameTv.setText(DDataAdapter.getreason());

        }

        @Override
        public int getItemCount() {
            Log.i("lstsize", String.valueOf(listadapter.size()));
            return listadapter.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView nameTv,dcounttV,dnametv;
            Button work;


            ViewHolder(View iv) {
                super(iv);
                context = iv.getContext();
                nameTv = iv.findViewById(R.id.RnameTv);
                dcounttV = iv.findViewById(R.id.RdayscntTv);
                work = iv.findViewById(R.id.wrkBtn);

            }

        }
    }




}
