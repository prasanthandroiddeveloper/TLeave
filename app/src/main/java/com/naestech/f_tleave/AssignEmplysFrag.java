package com.naestech.f_tleave;

import android.annotation.SuppressLint;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.naestech.f_tleave.utils.Config.EMPNAMES;


public class AssignEmplysFrag extends Fragment {


    public AssignEmplysFrag() {
        // Required empty public constructor
    }

    RecyclerView recycle;

    public static ArrayList<DataAdapter> checkedplayers;

    ArrayList<String> cblst;
    recycle_adapter radapter;
    Button selectall,deselectall,Dev,Sup,Seo,busac;
    String role_id;
    Progress_Dialog pdialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assign_emplys, container, false);


        recycle = view.findViewById(R.id.rcitem);
        selectall = view.findViewById(R.id.selectall);
        deselectall = view.findViewById(R.id.deselect);
        Dev = view.findViewById(R.id.devBtn);
        Sup = view.findViewById(R.id.suptBtn);
        Seo = view.findViewById(R.id.seoBtn);
        busac = view.findViewById(R.id.baBtn);


        pdialog = new Progress_Dialog(getContext());
        pdialog.setCancelable(false);
        Objects.requireNonNull(pdialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);



        ((Button)view.findViewById(R.id.sbtn)).setOnClickListener(v -> {
            cblst = new ArrayList<>();
            for(DataAdapter ch : checkedplayers){
                cblst.add(ch.getname());
            }
            Intent i = new Intent(getActivity(),SelectedEmplysAct.class);


            //todo on intent goes to SelectedEmplysAct

            startActivity(i);

        });

        selectall.setOnClickListener(v -> radapter.selectAll());

        deselectall.setOnClickListener(v -> radapter.unselectall());


        Dev.setOnClickListener(v -> {
            role_id = "0";

            Dev.setBackgroundResource(R.drawable.btn_plain);
            Sup.setBackgroundResource(R.drawable.btn_dboard);
            Seo.setBackgroundResource(R.drawable.btn_dboard);
            busac.setBackgroundResource(R.drawable.btn_dboard);
            Dev.setTextColor(Color.BLACK);
            Sup.setTextColor(Color.WHITE);
            Seo.setTextColor(Color.WHITE);

            getdata();
        });
        Sup.setOnClickListener(v -> {
            role_id = "2";
            Dev.setBackgroundResource(R.drawable.btn_dboard);
            Sup.setBackgroundResource(R.drawable.btn_plain);
            Seo.setBackgroundResource(R.drawable.btn_dboard);
            busac.setBackgroundResource(R.drawable.btn_dboard);
            Sup.setTextColor(Color.BLACK);

            Dev.setTextColor(Color.WHITE);
            Seo.setTextColor(Color.WHITE);
            getdata();
        });
        Seo.setOnClickListener(v -> {
            role_id = "3";
            Dev.setBackgroundResource(R.drawable.btn_dboard);
            Sup.setBackgroundResource(R.drawable.btn_dboard);
            Seo.setBackgroundResource(R.drawable.btn_plain);
            busac.setBackgroundResource(R.drawable.btn_dboard);
            Seo.setTextColor(Color.BLACK);
            Dev.setTextColor(Color.WHITE);
            Sup.setTextColor(Color.WHITE);


            getdata();
        });

        busac.setOnClickListener(v -> {
            role_id = "1";
            Dev.setBackgroundResource(R.drawable.btn_dboard);
            Sup.setBackgroundResource(R.drawable.btn_dboard);
            Seo.setBackgroundResource(R.drawable.btn_dboard);
            busac.setBackgroundResource(R.drawable.btn_plain);
            Seo.setTextColor(Color.WHITE);
            busac.setTextColor(Color.BLACK);
            Dev.setTextColor(Color.WHITE);
            Sup.setTextColor(Color.WHITE);


            getdata();
        });


        Sup.setBackgroundResource(R.drawable.btn_plain);
        Sup.setTextColor(Color.BLACK);
        role_id = "2";
        getdata();
        return view;
    }
    private void getdata() {

        pdialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,EMPNAMES, response -> {
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
                    adapter.setLAsgn(json.getString("asgn_to"));


                    list.add(adapter);
                }

                radapter = new recycle_adapter(list);

                recycle.setAdapter(radapter);

            } catch (JSONException e) {
                //  recycle.setVisibility(View.GONE);

                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                //   Toast.makeText(getActivity(),"No Data",Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_LONG).show();
        }){
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("role_id",role_id);

                Log.i("dp", String.valueOf(params));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);
    }


    public static class recycle_adapter  extends RecyclerView.Adapter<recycle_adapter.ViewHolder>  {

        private Context context;
        public static List<DataAdapter> listadapter;
        String ids;
        boolean isSelectedAll;
        recycle_adapter(List<DataAdapter> list) {
            super();
            this.listadapter = list;
            checkedplayers = new ArrayList<>();
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
            vh.assgnTv.setText(DDataAdapter.getLAsgn());

            ids = String.valueOf(DDataAdapter.getId());

            vh.checkBox.setOnClickListener(v -> {


                if (listadapter.get(pos).getSelected()) {
                    listadapter.get(pos).setSelected(false);
                } else {
                    listadapter.get(pos).setSelected(true);
                }
            });


            if(!isSelectedAll){
                vh.checkBox.setChecked(false);
                vh.checkBox.setSelected(false);

                checkedplayers.remove(listadapter.get(pos));
                listadapter.get(pos).setSelected(false);

            }else{
                checkedplayers.add(listadapter.get(pos));
                vh.checkBox.setChecked(true);
                vh.checkBox.setSelected(true);
                listadapter.get(pos).setSelected(true);
            }


        }

        @Override
        public int getItemCount() { return listadapter.size(); }

        public void selectAll(){
            isSelectedAll=true;
            notifyDataSetChanged();
        }
        public void unselectall(){
            isSelectedAll=false;
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView nameTv,assgnTv;
            protected CheckBox checkBox;

            ViewHolder(View iv) {
                super(iv);

                context = iv.getContext();
                nameTv = iv.findViewById(R.id.empnameTv);
                assgnTv = iv.findViewById(R.id.assgnTv);
                checkBox = (CheckBox) itemView.findViewById(R.id.cb);



            }
        }
    }
}
