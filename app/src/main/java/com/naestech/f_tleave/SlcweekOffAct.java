package com.naestech.f_tleave;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.naestech.f_tleave.utils.Date_Picker_Dialog;
import com.naestech.f_tleave.utils.Utils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.naestech.f_tleave.utils.Config.INSERTWEEKDAY;
import static com.naestech.f_tleave.utils.Config.INSERTWEEKOFF;
import static com.naestech.f_tleave.utils.Config.UPDATEWEEKOFF;

public class SlcweekOffAct extends AppCompatActivity {
    TextView tv,caldatetv;
    String Names,ids,FromDate,dept_id,asgndate,AsgnTo,Dayname;
    List<String> namelst,idlst,deptidlst,asgnlst;
    StringBuilder sbuid,sb1,sb2;
    Button submit,update;
    long minDate = System.currentTimeMillis() - 1000;
    DatePickerDialog datePickerDialog;
    int calmonth,calday, weekday;
    Progress_Dialog pdialog;
    Bundle bundle1;
    String[] days;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slcweek_off);

        tv = findViewById(R.id.slectEmpTv);
        caldatetv = findViewById(R.id.caldateTV);
        submit = findViewById(R.id.submit);
        update = findViewById(R.id.update);


        bundle1 = new Bundle();

        pdialog = new Progress_Dialog(this);
        pdialog.setCancelable(false);
        Objects.requireNonNull(pdialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        submit.setOnClickListener(v -> insertweekoff());
        update.setOnClickListener(v -> updateweekoff());

        FromDate = Utils.DatetoStr(System.currentTimeMillis(),0);

        namelst = new ArrayList<>();
        idlst = new ArrayList<>();
        deptidlst = new ArrayList<>();
        asgnlst = new ArrayList<>();
        for (int i = 0; i < AssignWeekOff_Frag.recycle_adapter.listadapter.size(); i++){
            if(AssignWeekOff_Frag.recycle_adapter.listadapter.get(i).getSelected()) {
                Names = AssignWeekOff_Frag.recycle_adapter.listadapter.get(i).getname().trim();
                ids = String.valueOf(AssignWeekOff_Frag.recycle_adapter.listadapter.get(i).getId());
                dept_id = String.valueOf(AssignWeekOff_Frag.recycle_adapter.listadapter.get(i).getDeptid());
                AsgnTo = String.valueOf(AssignWeekOff_Frag.recycle_adapter.listadapter.get(i).getAsg());
                // tv.setText(tv.getText() +"\n" +Assign_Emplys_Act.recycle_adapter.listadapter.get(i).getname());
                tv.setText(tv.getText().toString().trim() +"\n" +Names);
                tv.bringToFront();
                namelst.add(Names);
                idlst.add(ids);
                deptidlst.add(dept_id);
                asgnlst.add(AsgnTo);
                Log.i("Names",Names);
                Log.i("ids", String.valueOf(ids));
                Log.i("dept_id", dept_id);
                Log.i("AsgnTo", AsgnTo);

                sbuid = new StringBuilder(128);
                for (String value : idlst) {
                    if (sbuid.length() > 0) {
                        sbuid.append(",");
                    }
                    sbuid.append(value);
                }
                sbuid.insert(0, "[");
                sbuid.append("]");
                Log.i("sb", String.valueOf(sbuid));

                sb1 = new StringBuilder(128);
                for (String value : deptidlst) {
                    if (sb1.length() > 0) {
                        sb1.append(",");
                    }
                    sb1.append(value);
                }
                sb1.insert(0, "[");
                sb1.append("]");


                sb2 = new StringBuilder(128);
                for (String value : asgnlst) {
                    if (sb2.length() > 0) {
                        sb2.append(",");
                    }
                    sb2.append(value);
                }
                sb2.insert(0, "[");
                sb2.append("]");
                Log.i("sb1", String.valueOf(sb2));
            }
        }

        caldatetv.setOnClickListener(v -> {
            // mindate = System.currentTimeMillis() - 7776000000L;
            datedialog();
        });
    }

    private void datedialog() {

        minDate = System.currentTimeMillis() - 1000;

        new Date_Picker_Dialog(this,minDate,System.currentTimeMillis() - 1000+31536000000L).DateDialog(sdate -> {

            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

            FromDate = sdate;
            Calendar newcal = Calendar.getInstance();
            newcal.setTime(Utils.StrtoDate(0, sdate));
            newcal.add(Calendar.DATE, 1);
            minDate = newcal.getTimeInMillis();






            // days = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };


            weekday = (newcal.get(Calendar.DAY_OF_WEEK)-1) ;
            Log.i("weekday", String.valueOf(weekday));
            DateFormatSymbols dfs = new DateFormatSymbols();
            Log.i("wname", dfs.getWeekdays()[weekday]);
            if(weekday == 0){
                Dayname = "Saturday";
            }else{
                Dayname = dfs.getWeekdays()[weekday];
            }




            caldatetv.setText(Utils.ChangeDateFormat(FromDate, 9));

            asgndate = caldatetv.getText().toString();

        });
    }



    private void insertweekoff() {

        insertwdayname();

        pdialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,INSERTWEEKOFF, response -> {
            pdialog.dismiss();
            submit.setVisibility(View.INVISIBLE);
            Log.i("assgnresp",response);

            Intent intent = new Intent(this, Main_Screen_Activity.class);
           /* bundle1.putInt("Index", 4);
            bundle1.putString("weekday",Dayname);
            bundle1.putStringArrayList("ids", (ArrayList<String>) idlst);*/

            intent.putExtra("Index", 4);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);

            Toast.makeText(this, "Week Off Assigned Successfully", Toast.LENGTH_SHORT).show();
        }, error -> {
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG).show();
        }){

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("caldatetv", asgndate);
                params.put("jarray",String.valueOf(sbuid));
                params.put("deptidarray",String.valueOf(sb1));
                params.put("asgnarray",String.valueOf(sb2));

                Log.i("cal", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void updateweekoff() {

        insertwdayname();

        pdialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,UPDATEWEEKOFF, response -> {
            pdialog.dismiss();

            update.setVisibility(View.INVISIBLE);
            Log.i("updresp",response);

            Intent intent = new Intent(this, Main_Screen_Activity.class);
            intent.putExtra("Index",4);

          /*  bundle1.putInt("Index", 4);
            bundle1.putString("weekday",Dayname);
            bundle1.putStringArrayList("ids", (ArrayList<String>) idlst);*/
            // intent.putExtra("weekday",Dayname);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            Toast.makeText(this, "Week Off Updated Successfully", Toast.LENGTH_SHORT).show();

        }, error -> {
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG).show();
        }){

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("cal_date", asgndate);
                params.put("jarray",String.valueOf(sbuid));
                Log.i("upar", String.valueOf(params));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
    private void insertwdayname() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,INSERTWEEKDAY, response -> {
            //pdialog.dismiss();
            Log.i("indnameres",response);



        }, error -> {
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG).show();
        }){

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("assigned_to", Dayname);
                params.put("jarray",String.valueOf(sbuid));
                Log.i("indname", String.valueOf(params));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}
