package com.naestech.f_tleave;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import static com.naestech.f_tleave.utils.Config.LREPORT;


public class Report_Frag extends Fragment {


    public Report_Frag() {
        // Required empty public constructor
    }


    Button StartBtn,EndBtn,leave,wekoff;
    long minDate;
    String FromDate,ToDate,SDate,TDate,aut,cat;
    RecyclerView recyclerView;
    recycle_adapter recyclerAdapter;
    Progress_Dialog pdialog;
    Menu menu;
    ArrayList<String> namealst = new ArrayList<>();
    ArrayList<String> cntalst = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        recyclerView = view.findViewById(R.id.nameRclr);
        setHasOptionsMenu(true);
        StartBtn = view.findViewById(R.id.sdateBtn);
        EndBtn = view.findViewById(R.id.enddateBtn);

        leave = view.findViewById(R.id.LvsBtn);
        wekoff = view.findViewById(R.id.WoffBtn);

        FromDate = Utils.DatetoStr(System.currentTimeMillis(),0);
        ToDate = Utils.DatetoStr(System.currentTimeMillis()+86400000L,0);

        pdialog = new Progress_Dialog(getActivity());
        pdialog.setCancelable(false);
        Objects.requireNonNull(pdialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        StartBtn.setOnClickListener(v -> {
            stdate();
        });

        EndBtn.setOnClickListener(v -> {
            etdate();
        });

        leave.setOnClickListener(v -> {
            cat = "Leaves";
            leave.setBackgroundResource(R.drawable.btn_plain);
            leave.setTextColor(Color.BLUE);

            wekoff.setBackgroundResource(R.drawable.btn_dboard);
            wekoff.setTextColor(Color.WHITE);
            getdata();
        });
        wekoff.setOnClickListener(v -> {
            cat = "Weekoff";

            wekoff.setBackgroundResource(R.drawable.btn_plain);
            wekoff.setTextColor(Color.BLUE);

            leave.setBackgroundResource(R.drawable.btn_dboard);
            leave.setTextColor(Color.WHITE);
            getdata();
        });
        return view;
    }
    public void stdate() {

        minDate = System.currentTimeMillis() - 7776000000L;
        new Date_Picker_Dialog(getActivity(),minDate,System.currentTimeMillis() - 1000+31536000000L).DateDialog(sdate -> {

            FromDate = sdate;

            Calendar newcal = Calendar.getInstance();
            newcal.setTime(Utils.StrtoDate(0,sdate));
            newcal.add(Calendar.DATE, 1);

            minDate = newcal.getTimeInMillis();

            ToDate = Utils.DatetoStr(newcal.getTime(),0);
            StartBtn.setText(Utils.ChangeDateFormat(FromDate,9));
            SDate = StartBtn.getText().toString();
            etdate();

        });
    }
    public void etdate() {
        Calendar newcal = Calendar.getInstance();
        newcal.setTime(Utils.StrtoDate(0,FromDate));
        newcal.add(Calendar.DATE, 1);
        minDate = newcal.getTimeInMillis();

        new Date_Picker_Dialog(getActivity(),minDate,System.currentTimeMillis() - 1000+31536000000L).DateDialog(sdate -> {
            ToDate = sdate;
            EndBtn.setText(Utils.ChangeDateFormat(ToDate,9));
            TDate = EndBtn.getText().toString();
            //  getdata();

        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (recyclerAdapter != null) {
                    recyclerAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void getdata() {
        pdialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,LREPORT, response -> {
            pdialog.dismiss();
            Log.i("Leaveresponse",response);

            try {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                JSONArray jarr = new JSONArray(response);

                List<DataAdapter> list = new ArrayList<>();

                for(int i = 0; i< jarr.length(); i++) {

                    DataAdapter adapter = new DataAdapter();
                    JSONObject json = jarr.getJSONObject(i);
                    namealst.add(json.getString("name"));
                    cntalst.add(json.getString("ecount"));

                    Log.i("namealst", String.valueOf(namealst));

                    adapter.setname(json.getString("name"));
                    adapter.setcnt(json.getDouble("ecount"));
                    list.add(adapter);
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerAdapter = new recycle_adapter(list);
                recyclerView.setAdapter(recyclerAdapter);

            } catch (JSONException e) {
                recyclerView.setVisibility(View.GONE);

                //  Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),"No Data",Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_LONG).show();
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();
                params.put("FDate", SDate);
                params.put("TDate", TDate);
                params.put("category", cat);
                Log.i("Lparam", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);
    }

    public class recycle_adapter  extends RecyclerView.Adapter<recycle_adapter.ViewHolder>  {

        Context context;
        private   List<DataAdapter> listadapter;
        private List<DataAdapter> arrayadap;

        recycle_adapter(List<DataAdapter> list) {
            super();
            this.listadapter = list;
            this.arrayadap = list;
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
            vh.nameTv.setText(DDataAdapter.getname());
            // vh.dayscounttV.setText(String.valueOf(DDataAdapter.getNumber()));
            vh.dayscounttV.setText(String.valueOf(DDataAdapter.getcnt()));
            vh.work.setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() {
            Log.i("lstsize", String.valueOf(listadapter.size()));
            return listadapter.size();
        }

        private Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSeq) {
                    String charString = charSeq.toString();
                    if (charString.isEmpty()) {
                        listadapter = arrayadap;
                    } else {
                        ArrayList<DataAdapter> filteredList = new ArrayList<>();
                        for (DataAdapter filterdata : arrayadap) {
                            if (filterdata.getname().toLowerCase().contains(charString) || filterdata.getname().toUpperCase().contains(charString)) {
                                filteredList.add(filterdata);
                            }
                        }
                        listadapter = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = listadapter;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    listadapter = (ArrayList<DataAdapter>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView nameTv,dayscounttV;
            Button work;
            ViewHolder(View iv) {
                super(iv);
                context = iv.getContext();
                nameTv = iv.findViewById(R.id.RnameTv);
                dayscounttV = iv.findViewById(R.id.RdayscntTv);
                work = iv.findViewById(R.id.wrkBtn);
            }
        }
    }

}
