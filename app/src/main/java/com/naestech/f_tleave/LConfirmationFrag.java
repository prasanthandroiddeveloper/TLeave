package com.naestech.f_tleave;

import android.annotation.SuppressLint;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.naestech.f_tleave.utils.Config.DELETEONSWIPE;
import static com.naestech.f_tleave.utils.Config.RECYCLERSTATUS;


public class LConfirmationFrag extends Fragment {


    public LConfirmationFrag() {
        // Required empty public constructor
    }
    RecyclerView recycle;
    LinearLayout Lyyt;
    recycle_adapter radapter;
    String  ename, status, sts, dayscnt,  swipename, swipedate, SFdate, SUserId;
    Button processbtn, Aprbtn, NAprvbtn;
    SharedPrefs sp;
    Progress_Dialog pdialog;
    TextView yestv, notv;
    int swipeUserId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lconfirmation, container, false);

        recycle = view.findViewById(R.id.lapprovercylr);
        processbtn = view.findViewById(R.id.processBtn);
        Aprbtn = view.findViewById(R.id.AprBtn);
        NAprvbtn = view.findViewById(R.id.NAprvBtn);
        Lyyt = view.findViewById(R.id.SLlyt);

        pdialog = new Progress_Dialog(getContext());
        pdialog.setCancelable(false);
        Objects.requireNonNull(pdialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);




        sp = new SharedPrefs(getActivity());
        ename = sp.getUName();
        Log.i("ename", ename);
        sts = "Pending";
        getdata();
        processbtn.setBackgroundResource(R.drawable.btn_plain);
        processbtn.setTextColor(Color.BLACK);
        processbtn.setOnClickListener(v -> {

            sts = "Pending";
            processbtn.setBackgroundResource(R.drawable.btn_plain);
            processbtn.setTextColor(Color.BLACK);

            Aprbtn.setBackgroundResource(R.drawable.btn_dboard);
            Aprbtn.setTextColor(Color.WHITE);
            NAprvbtn.setBackgroundResource(R.drawable.btn_dboard);
            NAprvbtn.setTextColor(Color.WHITE);
            getdata();
        });
        Aprbtn.setOnClickListener(v -> {

            sts = "Approved";

            Aprbtn.setBackgroundResource(R.drawable.btn_plain);
            Aprbtn.setTextColor(Color.BLACK);

            processbtn.setBackgroundResource(R.drawable.btn_dboard);
            processbtn.setTextColor(Color.WHITE);
            NAprvbtn.setBackgroundResource(R.drawable.btn_dboard);
            NAprvbtn.setTextColor(Color.WHITE);
            getdata();
        });
        NAprvbtn.setOnClickListener(v -> {
            sts = "Not Approved";

            NAprvbtn.setBackgroundResource(R.drawable.btn_plain);
            NAprvbtn.setTextColor(Color.BLACK);

            processbtn.setBackgroundResource(R.drawable.btn_dboard);
            processbtn.setTextColor(Color.WHITE);
            Aprbtn.setBackgroundResource(R.drawable.btn_dboard);
            Aprbtn.setTextColor(Color.WHITE);
            getdata();
        });

        return view;
    }
    private void getdata() {
        pdialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,RECYCLERSTATUS, response -> {
            pdialog.dismiss();
            Log.i("stares",response);

            try {
                recycle.setVisibility(View.VISIBLE);
                recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
                JSONArray jarr = new JSONArray(response);
                List<DataAdapter> list = new ArrayList<>();

                for(int i = 0; i< jarr.length(); i++) {
                    DataAdapter adapter = new DataAdapter();
                    JSONObject json = jarr.getJSONObject(i);

                    adapter.setname(json.getString("name"));
                    adapter.setstatus(json.getString("status"));
                    adapter.setreason(json.getString("reason"));
                    adapter.setdate(json.getString("from_date")+"\n"+json.getString("to_date"));
                    adapter.setsdate(json.getString("from_date"));
                    adapter.setcnt(json.getDouble("no_of_days"));
                    adapter.setId(json.getInt("id"));
                    adapter.setAsg(json.getString("assigned_to"));
                    adapter.setUserid(json.getString("user_id"));
                    adapter.setLAsgn(json.getString("asgn_to"));



                    list.add(adapter);
                }
                radapter = new recycle_adapter(list,getContext());
                recycle.setAdapter(radapter);

            } catch (JSONException e) {
                recycle.setVisibility(View.GONE);


                Toast.makeText(getActivity(),"No Data",Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_LONG).show();
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();
                params.put("status",sts);
                Log.i("param", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
        public recycle_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new recycle_adapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_dashboard, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull recycle_adapter.ViewHolder vh, int pos) {

            DataAdapter DDataAdapter =  listadapter.get(pos);

            SFdate = DDataAdapter.getsdate();
            SUserId = DDataAdapter.getUserid();

            vh.nameTv.setText(DDataAdapter.getname());
            vh.statusTV.setText(DDataAdapter.getstatus());

            vh.datetv.setText(DDataAdapter.getdate());
            vh.dcounttV.setText(String.valueOf(DDataAdapter.getNumber()));
            // vh.permissiontv.setText(DDataAdapter.getPermission());
            vh.asgnto.setText(DDataAdapter.getAsg());



            status = DDataAdapter.getstatus();
            if(status.contains("Approved")){
                vh.statusTV.setTextColor(Color.parseColor("#008000"));
                vh.Delete.setVisibility(View.GONE);
            }
            if(status.contains("Not Approved")){
                vh.statusTV.setTextColor(Color.parseColor("#FF0000"));
                vh.Delete.setVisibility(View.VISIBLE);
            }

            if(status.contains("Pending")){
                vh.statusTV.setTextColor(Color.parseColor("#FFC300"));
                vh.Delete.setVisibility(View.VISIBLE);
            }






            if((DDataAdapter.getAsg()).equals(ename)){

                vh.rsnlyt.setVisibility(View.VISIBLE);
                vh.reasontv.setText(DDataAdapter.getreason());
            }else{
                vh.rsnlyt.setVisibility(View.GONE);
            }

            dayscnt = String.valueOf(DDataAdapter.getNumber());


            if(dayscnt.equals("0")){ vh.daysllyt.setVisibility(View.GONE); }


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

        public void restoreItem(DataAdapter item, int position) {
            listadapter.add(position, item);
            notifyItemInserted(position);
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView nameTv,statusTV,reasontv,datetv,dcounttV,asgnto;
            LinearLayout daysllyt,rsnlyt;
            Button Delete;

            ViewHolder(View iv) {
                super(iv);

                context = iv.getContext();
                nameTv = iv.findViewById(R.id.NameTv);
                statusTV = iv.findViewById(R.id.StatusTv);
                reasontv = iv.findViewById(R.id.RsnTv);
                datetv = iv.findViewById(R.id.TdateTv);
                dcounttV = iv.findViewById(R.id.dayscountTv);
                asgnto = iv.findViewById(R.id.AsgnTv);
                daysllyt = iv.findViewById(R.id.dayscntLlyt);
                rsnlyt =iv.findViewById(R.id.rsnllyt);

                Delete =iv.findViewById(R.id.deleteBtn);
                Delete.setOnClickListener(v -> {
                    swipename = listadapter.get(getAdapterPosition()).getname();
                    swipedate = listadapter.get(getAdapterPosition()).getsdate();
                    swipeUserId = listadapter.get(getAdapterPosition()).getId();

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


                iv.setOnClickListener(v -> {

                    // int position  =   getAdapterPosition();
                    String name = listadapter.get(getAdapterPosition()).getname();
                    String reason = listadapter.get(getAdapterPosition()).getreason();
                    double dcnt = listadapter.get(getAdapterPosition()).getcnt();
                    int empid = listadapter.get(getAdapterPosition()).getId();
                    String  from_date = listadapter.get(getAdapterPosition()).getsdate();
                    // String  permission = listadapter.get(getAdapterPosition()).getPermission();

                    Log.i("empid", String.valueOf(empid));
                    Log.i("from_date", from_date);
                    Log.i("dcntt", String.valueOf(dcnt));

                    //  Toast.makeText(context, "item clicked", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getActivity(),Approve_Leave_Act.class);

                    Bundle b = new Bundle();

                    b.putString("name",name);
                    b.putString("reason",reason);
                    b.putDouble("dayscount",dcnt);
                    b.putInt("bid",empid);
                    b.putString("fdate",from_date);
                    // b.putString("permission",permission);
                    b.putString("status",status);

                    i.putExtras(b);
                    startActivity(i);

                });
            }
        }
    }

    private void delete() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,DELETEONSWIPE,response -> {
            Log.i("deleteres",response);

            Intent intent = new Intent(getContext(), Main_Screen_Activity.class);
            intent.putExtra("Index",2);
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

                Log.i("param", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);
    }


}
