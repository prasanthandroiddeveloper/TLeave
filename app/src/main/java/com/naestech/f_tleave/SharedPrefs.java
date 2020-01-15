package com.naestech.f_tleave;

/* Created by G46567 */

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SharedPrefs(Context ctx){
        prefs = ctx.getSharedPreferences("TripLeave", Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.apply();
    }

    public void setLoggedin(boolean logdin){
        editor.putBoolean("loggedInmode",logdin);
        editor.apply();
    }
    public void setUserDet(String id,String uname,String deptname,String mail){
        editor.putString("userid",id);

        editor.putString("username",uname);
        editor.putString("dptname",deptname);
        editor.putString("mail",mail);

        editor.commit();
    }

    public void setUTypeId(int utid){
        editor.putInt("usertype_id",utid);

        editor.commit();
    }

    public void setfcm(String fcmtoken){

        editor.putString("fcmtoken",fcmtoken);

        editor.commit();
    }

    public void setemail(String Email){

        editor.putString("email",Email);

        editor.commit();
    }
    public void setAdname(String AName){

        editor.putString("adminname",AName);

        editor.commit();
    }

    public String getUId(){return prefs.getString("userid","");}



    public int getUTypeId(){return prefs.getInt("usertype_id",-1);}

    public String getUName(){return prefs.getString("username","");}
    public String getmail(){return prefs.getString("mail","");}

    public String getdeptname(){return prefs.getString("dptname","");}

    public boolean getLoggedin(){return prefs.getBoolean("loggedInmode", false);}
    public String getfcm(){return prefs.getString("fcmtoken","");}

    public String getemail(){return prefs.getString("email","");}
    public String getAdname(){return prefs.getString("adminname","");}

    public void ClearAll(){
        editor.clear();
        editor.apply();
    }

}