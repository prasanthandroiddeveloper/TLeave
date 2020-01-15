package com.naestech.f_tleave;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.naestech.f_tleave.utils.Config.ASSIGN;
import static com.naestech.f_tleave.utils.Config.FETCHNOT;

public class SelectedEmplysAct extends AppCompatActivity {
    TextView tv;
    Bundle bundle;
    String Names;
    List<String> spnamelist,idslist,fcmlist;
    String Suppname,ids;
    List<String> namelst,idlst;
    StringBuilder sb;
    Spinner Adminnames;
    Button submitBtn,goBtn;
    Progress_Dialog pdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_emplys);
        tv=findViewById(R.id.selectedTv);
        Adminnames = findViewById(R.id.ANames);
        submitBtn = findViewById(R.id.asgnBtn);


        pdialog = new Progress_Dialog(this);
        pdialog.setCancelable(false);
        Objects.requireNonNull(pdialog.getWindow()).
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        namelst = new ArrayList<>();
        idlst = new ArrayList<>();
        for (int i = 0; i < AssignEmplysFrag.recycle_adapter.listadapter.size(); i++){
            if(AssignEmplysFrag.recycle_adapter.listadapter.get(i).getSelected()) {
                Names = AssignEmplysFrag.recycle_adapter.listadapter.get(i).getname().trim();
                ids = String.valueOf(AssignEmplysFrag.recycle_adapter.listadapter.get(i).getId());
                tv.setText(tv.getText().toString().trim() +"\n" +Names.trim());
                tv.bringToFront();
                namelst.add(Names);
                idlst.add(ids);
                Log.i("Names",Names);
                Log.i("ids", String.valueOf(ids));

                sb = new StringBuilder(128);
                for (String value : namelst) {
                    if (sb.length() > 0) {
                        sb.append(",");
                    }
                    sb.append(value);
                }
                sb.insert(0, "[");
                sb.append("]");
                Log.i("sb", String.valueOf(sb));
            }
        }
        Log.i("namelst",String.valueOf(idlst));
        submitBtn.setOnClickListener(v -> data());
        getspindata();

    }

    private void getspindata(){
        spnamelist = new ArrayList<>();
        idslist = new ArrayList<>();
        fcmlist = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,FETCHNOT, response -> {

            Log.i("resp1",response);

            try {

                JSONArray jarr = new JSONArray(response);

                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject json = jarr.getJSONObject(i);
                    spnamelist.add(json.getString("name"));
                    idslist.add(String.valueOf(json.getInt("id")));
                    fcmlist.add(json.getString("fcm_token"));
                }


                ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,spnamelist);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Adminnames.setAdapter(aa);
                Adminnames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.i("text",parent.getItemAtPosition(position).toString()) ;

                        Suppname = parent.getItemAtPosition(position).toString();
                        Log.i("Suppname",Suppname);



                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });




            } catch (JSONException e) {
                Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG).show();
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
    private void data() {
        update();

    }
    private void update() {
        pdialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,ASSIGN, response -> {
            pdialog.dismiss();
            Log.i("assgnresp",response);

            Intent intent = new Intent(this, Main_Screen_Activity.class);
            intent.putExtra("Index",3);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            Toast.makeText(this, "Emlpoyees Assigned Successfully", Toast.LENGTH_SHORT).show();

        }, error -> {
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG).show();
        }){

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("assigned_to", Suppname);
                params.put("jarray",String.valueOf(sb));
                Log.i("apar", String.valueOf(params));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}
