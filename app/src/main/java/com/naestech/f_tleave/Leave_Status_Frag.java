package com.naestech.f_tleave;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.naestech.f_tleave.utils.Config.DELETEONSWIPE;
import static com.naestech.f_tleave.utils.Config.GETSTATUS;


public class Leave_Status_Frag extends Fragment {

    public Leave_Status_Frag() {
        // Required empty public constructor
    }
    RecyclerView recyclerView;
    SharedPrefs sp;
    int DeptId;
    TextView refresh;
    String ename,status,datefilter,typefd,dayscnt,permsncnt,swipename,swipedate,SFdate,SUserId;
    Progress_Dialog pdlg;
    Button filterdate;
    long mindate;
    DatePickerDialog datePickerDialog;
    int calmonth,calday;
    LinearLayout LsLlyt;
    int swipeUserId;

    recycle_adapter radapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leave_status, container, false);

        recyclerView = view.findViewById(R.id.recycle);

        filterdate = view.findViewById(R.id.dateBtn);
        LsLlyt = view.findViewById(R.id.lsllyt);
        refresh = view.findViewById(R.id.refreshTv);

        filterdate.setOnClickListener(v -> {
            typefd = "calfilterdate";
            mindate = System.currentTimeMillis() - 7776000000L;
            datedialog();

        });

        sp = new SharedPrefs(getActivity());
        ename = sp.getUName();


        refresh.setOnClickListener(v -> {
            typefd="no";
            getdata();
        });


        pdlg = new Progress_Dialog(getActivity());
        pdlg.setCancelable(false);
        Objects.requireNonNull(pdlg.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        getdata();
        typefd="no";


        return view;
    }


    private void datedialog() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        // date picker dialog
        datePickerDialog = new DatePickerDialog(getActivity(), (view, year, monthOfYear, dayOfMonth) -> {

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

            filterdate.setText(sday + "-" +sMonth + "-" + year);
            //filterdate.setText(sday +""+sMonth+""+year);

            Log.i("fdate", String.valueOf(filterdate));
            getdata();
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void getdata() {

        pdlg.show();
        datefilter = filterdate.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,GETSTATUS, response -> {

            Log.i("leaverespe",response);
            pdlg.dismiss();

            try {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                JSONArray jarr = new JSONArray(response);
                List<DataAdapter> list = new ArrayList<>();

                for(int i = 0; i< jarr.length(); i++) {
                    DataAdapter adapter = new DataAdapter();
                    JSONObject json = jarr.getJSONObject(i);

                    adapter.setname(json.getString("name"));
                    adapter.setstatus(json.getString("status"));
                    adapter.setreason(json.getString("reason"));
                    adapter.setdate(json.getString("from_date")+" - "+json.getString("to_date"));
                    adapter.setRjctReason(json.getString("reject_leave_rsn"));

                    adapter.setNumber(json.getInt("no_of_days"));
                    adapter.setPermission(json.getString("permission"));
                    adapter.setUserid(json.getString("user_id"));
                    adapter.setsdate(json.getString("from_date"));



                    list.add(adapter);
                }

                // recyclerView.setAdapter(new recycle_adapter(list));

                radapter = new recycle_adapter(list,getContext());
                //    recycle = new recycle_adapter(list);
                recyclerView.setAdapter(radapter);

            } catch (JSONException e) {
                Toast.makeText(getActivity(),"No Data.",Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            Toast.makeText(getActivity(), "No Data.", Toast.LENGTH_LONG).show();
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("type",typefd);
                params.put("ename", ename);
                params.put("fdate", datefilter);

                Log.i("param", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);
    }
    public class recycle_adapter  extends RecyclerView.Adapter<recycle_adapter.ViewHolder>  {

        private Context context;
        private List<DataAdapter> listadapter;

        recycle_adapter(List<DataAdapter> list,Context c) {
            super();
            this.listadapter = list;
            this.context=c;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_emp_list, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull recycle_adapter.ViewHolder vh, int pos) {

            DataAdapter DDataAdapter =  listadapter.get(pos);


            SFdate = DDataAdapter.getsdate();
            SUserId = DDataAdapter.getUserid();

            Log.i("dateuid",SFdate + " " + SUserId);

            vh.nameTv.setText(DDataAdapter.getname());
            vh.statusTV.setText(DDataAdapter.getstatus());
            vh.rsnTv.setText(DDataAdapter.getreason());
            vh.Ldate.setText(DDataAdapter.getdate());
            vh.rjctTv.setText(DDataAdapter.getRjctReason());

            vh.dcounttV.setText(String.valueOf(DDataAdapter.getNumber()));
            vh.permissiontv.setText(DDataAdapter.getPermission());

            status = DDataAdapter.getstatus();
            if(status.contains("Approved")){
                vh.statusTV.setTextColor(Color.parseColor("#008000"));
                vh.Delete.setVisibility(View.GONE);
            }
            if(status.contains("Not Approved")){
                vh.rjct.setVisibility(View.VISIBLE);
                vh.statusTV.setTextColor(Color.parseColor("#FF0000"));
                vh.Delete.setVisibility(View.VISIBLE);
            }

            if(status.contains("Pending")){
                vh.statusTV.setTextColor(Color.parseColor("#008000"));
                vh.Delete.setVisibility(View.VISIBLE);
            }

            dayscnt = String.valueOf(DDataAdapter.getNumber());
            permsncnt = DDataAdapter.getPermission();

            if(dayscnt.equals("0")){
                vh.daysllyt.setVisibility(View.GONE);
            }
            if(permsncnt.equals("")){
                vh.perllyt.setVisibility(View.GONE);
            }

        }

        @Override
        public int getItemCount() { return listadapter.size(); }

        public List<DataAdapter> getData() {
            return listadapter;
        }

        public void removeItem(int position) {
            listadapter.remove(position);
            notifyItemRemoved(position);
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView nameTv,statusTV,rsnTv,Ldate,rjctTv,dcounttV,permissiontv;
            LinearLayout rjct,daysllyt,perllyt;
            Button Delete;

            ViewHolder(View iv) {
                super(iv);

                context = iv.getContext();
                nameTv = iv.findViewById(R.id.NameTv);
                statusTV = iv.findViewById(R.id.StatusTv);
                rsnTv = iv.findViewById(R.id.reasonTv);
                Ldate = iv.findViewById(R.id.LDateTv);
                rjctTv = iv.findViewById(R.id.rjctlveTv);
                rjct = iv.findViewById(R.id.rjctLlyt);

                dcounttV = iv.findViewById(R.id.dayscountTv);
                permissiontv = iv.findViewById(R.id.permissionTv);
                daysllyt = iv.findViewById(R.id.dayscntLlyt);
                perllyt =iv.findViewById(R.id.permsnLlyt);

                Delete =iv.findViewById(R.id.deleteBtn);
                Delete.setOnClickListener(v -> {
                    swipename = listadapter.get(getAdapterPosition()).getname();
                    swipedate = listadapter.get(getAdapterPosition()).getsdate();
                    swipeUserId = Integer.parseInt(listadapter.get(getAdapterPosition()).getUserid());

                    Dialog dialog=new Dialog(context);
                    dialog.setContentView(R.layout.sure_delete);
                    Button yes=dialog.findViewById(R.id.yesButton);
                    Button no=dialog.findViewById(R.id.noButton);
                    Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.show();

                    yes.setOnClickListener(view -> {
                        delete();
                    });

                    no.setOnClickListener(view -> {
                        dialog.dismiss();
                    });


                });

            }
        }

    }
    private void delete() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,DELETEONSWIPE,response -> {
            Log.i("delres",response);

            Intent intent = new Intent(getContext(), Main_Screen_Activity.class);
            intent.putExtra("Index", 0);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        },error -> {

        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();
                params.put("name",swipename);
                params.put("sdate",swipedate);
                params.put("UId", String.valueOf(swipeUserId));

                Log.i("paramdel", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);
    }
}
