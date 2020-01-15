package com.naestech.f_tleave;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.naestech.f_tleave.utils.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.naestech.f_tleave.utils.Config.DELETEMPDETAILS;
import static com.naestech.f_tleave.utils.Config.DELETEONSWIPE;


public class EmpDetails extends Fragment {

    public EmpDetails() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    View v;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_emp_details, container, false);
        recyclerView=v.findViewById(R.id.empRecycl);
        Empdetails();
        return v;
}
    private void Empdetails() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.EMPDETAILS, response -> {
            List<DataAdapter> list = new ArrayList<>();
            //  Log.i("res",response);
            recyclerView.setVisibility(View.VISIBLE);

            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    DataAdapter dataAdapter = new DataAdapter();

                    JSONObject jobj = jsonarray.getJSONObject(i);

                    dataAdapter.setname(jobj.getString("name"));//name
                    dataAdapter.setdate(jobj.getString("date_of_joining"));//joining date
                    dataAdapter.setstatus(jobj.getString("phone_no"));//phone 1
                    dataAdapter.setsdate(jobj.getString("alter_phone_no"));//phone 2
                    dataAdapter.setType(jobj.getString("prmnt_addrs"));//addrs
                    dataAdapter.setDeptid(jobj.getString("temp_addrs"));//addrs
                    dataAdapter.setPermission(jobj.getString("date_of_birth"));//dob
                    dataAdapter.setLAsgn(jobj.getString("email"));//email
                    dataAdapter.setAsg(jobj.getString("user_id"));//email
                    list.add(dataAdapter);


                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(new hotels_adapter(list,getContext())); }
            catch (JSONException e) {

                recyclerView.setVisibility(View.GONE);

            }
            //  Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();


        }, error -> {
            Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_LONG).show();
        });
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);

    }


    class hotels_adapter extends RecyclerView.Adapter<hotels_adapter.ViewHolder> {

        private List<DataAdapter> listadapter;
        private Context con;
        String id;

        hotels_adapter(List<DataAdapter> listAdapter, Context c) {
            super();
            this.listadapter = listAdapter;
            this.con = c;
        }

        @NonNull
        @Override
        public hotels_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new hotels_adapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_marriage, parent, false));
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView AdminnameTv,status,email,mobileno,desgn,dept,share,flw,emailid,temp;
            Button deletebtn;



            @SuppressLint("SetJavaScriptEnabled")
            ViewHolder(View iv) {

                super(iv);

                AdminnameTv = iv.findViewById(R.id.AdmnnameTV);
                status = iv.findViewById(R.id.AstatusTv);
                email = iv.findViewById(R.id.AemailTV);
                dept = iv.findViewById(R.id.DeptTV);
                flw = iv.findViewById(R.id.flwTV);
                emailid = iv.findViewById(R.id.emailTV);
                temp=iv.findViewById(R.id.tempTV);
                deletebtn=iv.findViewById(R.id.deleteBtn);

                deletebtn.setOnClickListener(v -> {
                    id = listadapter.get(getAdapterPosition()).getAsg();

                    Dialog dialog=new Dialog(con);
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


        private void delete() {

            StringRequest stringRequest = new StringRequest(Request.Method.POST,DELETEMPDETAILS,response -> {
               Log.i("del",response);

                Toast.makeText(con, response, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), Main_Screen_Activity.class);
                intent.putExtra("Index",0);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            },error -> {

            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params =new HashMap<>();
                    params.put("uid",id);
                    Log.i("param", String.valueOf(params));
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
            requestQueue.add(stringRequest);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull hotels_adapter.ViewHolder vh, int pos) {

            DataAdapter dataAdap = listadapter.get(pos);
            vh.AdminnameTv.setText(dataAdap.getstatus()+","+dataAdap.getsdate());//phone
            vh.status.setText(dataAdap.getdate());//joining date
            vh.email.setText(dataAdap.getPermission());//date of birth
//            vh.desgn.setText(dataAdap.getsdate());//phone2
            vh.dept.setText(dataAdap.getType());//adrs
            vh.flw.setText(dataAdap.getname());//name
            vh.emailid.setText(dataAdap.getLAsgn());//name
            vh.temp.setText(dataAdap.getDeptid());//temp addrs

        }
        @Override
        public int getItemCount() { return listadapter.size(); }
    }
}
