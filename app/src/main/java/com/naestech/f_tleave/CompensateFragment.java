package com.naestech.f_tleave;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class CompensateFragment extends Fragment {

    public CompensateFragment() {
        // Required empty public constructor
    }
    String startDate,endDate,Uid,edate,Deptid;
    SharedPrefs sharedPrefs;
    RecyclerView recyclerView;
    recycle_adapter radapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_compensate, container, false);


            sharedPrefs = new SharedPrefs(Objects.requireNonNull(getActivity()));
            Uid = String.valueOf(sharedPrefs.getUTypeId());
            Deptid = String.valueOf(sharedPrefs.getUId());


            //  deptid = spfs.getUId();
            // uid = String.valueOf(spfs.getUTypeId());

            Log.i("Uid",Uid);

            recyclerView = view.findViewById(R.id.weekoff);

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 0);
            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            Date monthFirstDay = calendar.getTime();
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date monthLastDay = calendar.getTime();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            startDate = df.format(monthFirstDay);
            endDate = df.format(monthLastDay);

            Log.i("date",startDate + " "+endDate);
            getdata();
            return view;
        }
        private void getdata() {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.FETCHWEEKOFF, response -> {

                Log.i("WeekRes",response);

                try{
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    JSONArray jarr = new JSONArray(response);
                    List<DataAdapter> list = new ArrayList<>();

                    for(int i = 0; i< jarr.length(); i++) {
                        DataAdapter adapter = new DataAdapter();
                        JSONObject json = jarr.getJSONObject(i);

                        adapter.setname(json.getString("leave_type"));
                        adapter.setdate(json.getString("from_date"));

                        list.add(adapter);
                    }
                    radapter = new recycle_adapter(list,getContext());
                    //    recycle = new recycle_adapter(list);
                    recyclerView.setAdapter(radapter);
                }catch (JSONException e){
                    Toast.makeText(getContext(),"Completed week offs For this Month to Compensate",Toast.LENGTH_LONG).show();
                    Log.i("error",e.getMessage());
                }

            },error -> {

            }){
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("uid", Uid);
                    params.put("sdate", startDate);
                    params.put("edate", endDate);

                    Log.i("pa1", String.valueOf(params));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }

        public class recycle_adapter extends RecyclerView.Adapter<recycle_adapter.Viewholder>{
            private Context context;
            private List<DataAdapter> listadapter;
            String[] Workinghrs = {"Full Day","Half Day"};
            double count;




            recycle_adapter(List<DataAdapter> list,Context c) {
                super();
                this.listadapter = list;
                this.context = c;

            }

            @Override
            public recycle_adapter.Viewholder onCreateViewHolder( ViewGroup viewGroup, int i) {
                return new recycle_adapter.Viewholder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.names_rcylr, viewGroup, false));
            }

            @Override
            public void onBindViewHolder( recycle_adapter.Viewholder viewholder, int i) {
                DataAdapter DDataAdapter =  listadapter.get(i);
                viewholder.LeaveTv.setText(DDataAdapter.getname());
                viewholder.Fdate.setText(DDataAdapter.getdate());
            }

            @Override
            public int getItemCount() {
                return listadapter.size();
            }

            class Viewholder extends RecyclerView.ViewHolder  {

                TextView LeaveTv,Fdate;
                Button work,submit;
                LinearLayout spn;
                Spinner wrkghrs;

                Viewholder(View itemView) {
                    super(itemView);
                    context = itemView.getContext();

                    LeaveTv = itemView.findViewById(R.id.RnameTv);
                    Fdate = itemView.findViewById(R.id.RdayscntTv);
                    work = itemView.findViewById(R.id.wrkBtn);
                    submit = itemView.findViewById(R.id.doneBtn);
                    spn = itemView.findViewById(R.id.spnLyt);
                    wrkghrs = itemView.findViewById(R.id.wrkngspnr);

                    ArrayAdapter aa = new ArrayAdapter(context,android.R.layout.simple_spinner_item,Workinghrs);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    wrkghrs.setAdapter(aa);

                    wrkghrs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // On selecting a spinner item
                            String item = parent.getItemAtPosition(position).toString();
                            // Showing selected spinner item
                            Toast.makeText(context, "Selected: " + item, Toast.LENGTH_LONG).show();
                            if(Workinghrs[position] .equals("Full Day")){
                                count = 1;
                            }else{
                                count = 0.5;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    work.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            spn.setVisibility(View.VISIBLE);
                        }
                    });


                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            edate = listadapter.get(getAdapterPosition()).getdate();

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.UPDATELEAVE, response -> {
                                submit.setVisibility(View.INVISIBLE);
                                radapter.notifyDataSetChanged();
                                //getdata();
                                Intent intent = new Intent(getContext(), Main_Screen_Activity.class);
                                intent.putExtra("Index",7);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                                Log.i("cres",response);

                            },error -> {
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }){
                                @Override
                                protected Map<String, String> getParams() {

                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("type", "compensate");
                                    params.put("dept_id", Deptid);
                                    params.put("date", edate);
                                    params.put("count", String.valueOf(count));
                                    params.put("user_id", Uid);
                                    Log.i("cpar", String.valueOf(params));

                                    return params;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                            requestQueue.add(stringRequest);
                        }
                    });



                }


            }

        }

}
