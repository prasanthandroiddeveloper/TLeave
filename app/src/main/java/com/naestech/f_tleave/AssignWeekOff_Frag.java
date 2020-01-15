package com.naestech.f_tleave;

import android.annotation.SuppressLint;
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
import android.widget.CheckBox;
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
import java.util.List;
import java.util.Objects;

import static com.naestech.f_tleave.utils.Config.SUPPEMPNAMES;


public class AssignWeekOff_Frag extends Fragment {

    public AssignWeekOff_Frag() {
        // Required empty public constructor
    }

    RecyclerView recycle;
    Progress_Dialog pdialog;
    String wday;
    List<String> ids = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_assign_week_off, container, false);
        pdialog = new Progress_Dialog(getContext());
        pdialog.setCancelable(false);
        Objects.requireNonNull(pdialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);



        recycle = view.findViewById(R.id.rcitem);
        ((Button)view.findViewById(R.id.submit)).setOnClickListener(v -> {
            Intent i = new Intent(getActivity(),SlcweekOffAct.class);
            startActivity(i);

            //submit();
        });
        getdata();

        return view;
    }

    private void getdata() {
        pdialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,SUPPEMPNAMES, response -> {
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

                    adapter.setId(json.getInt("id"));
                    adapter.setname(json.getString("name"));
                    adapter.setDeptid(json.getString("dept_type_id"));
                    adapter.setAsg(json.getString("asgn_to"));
                    adapter.setLAsgn(json.getString("Dayname"));




                    list.add(adapter);
                }

                recycle.setAdapter(new recycle_adapter(list));

            } catch (JSONException e) {
                recycle.setVisibility(View.GONE);
                Toast.makeText(getActivity(),"No Data",Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_LONG).show();
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    public static class recycle_adapter  extends RecyclerView.Adapter<recycle_adapter.ViewHolder>  {

        private Context context;
        public static List<DataAdapter> listadapter;
        String ids,dept_id,AsgnTo;
        recycle_adapter(List<DataAdapter> list) {
            super();
            this.listadapter = list;

        }

        @NonNull
        @Override
        public recycle_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new recycle_adapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull recycle_adapter.ViewHolder vh, int pos) {

            DataAdapter DDataAdapter =  listadapter.get(pos);

            vh.checkBox.setChecked(listadapter.get(pos).getSelected());
            vh.nameTv.setText(DDataAdapter.getname());
            //  vh.assgnTv.setText(DDataAdapter.getLAsgn());

            vh.assgnTv.setText(DDataAdapter.getLAsgn());
            ids = String.valueOf(DDataAdapter.getId());
            dept_id = String.valueOf(DDataAdapter.getDeptid());
            AsgnTo = String.valueOf(DDataAdapter.getAsg());

            vh.checkBox.setOnClickListener(v -> {

                //  Toast.makeText(context, String.valueOf(listadapter.get(pos).getDeptid()) , Toast.LENGTH_SHORT).show();

                if (listadapter.get(pos).getSelected()) {
                    listadapter.get(pos).setSelected(false);
                } else {
                    listadapter.get(pos).setSelected(true);
                }
            });


        }

        @Override
        public int getItemCount() { return listadapter.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView nameTv,assgnTv;
            protected CheckBox checkBox;

            ViewHolder(View iv) {
                super(iv);

                context = iv.getContext();
                nameTv = iv.findViewById(R.id.empnameTv);
                assgnTv = iv.findViewById(R.id.assgnTv);
                checkBox =  itemView.findViewById(R.id.cb);


            }
        }
    }
}
