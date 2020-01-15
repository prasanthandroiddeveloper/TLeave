package com.naestech.f_tleave.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Utils {



    public interface Callback{ void getResponse(String response);}

    public static String ChangeDateFormat(String dates, int  format) {

        String ss;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = sdf.parse(dates);

            switch (format) {
                case 1: //20-SEP-2018
                    sdf.applyPattern("dd-MMM-yyyy");
                    break;
                case 2: //WED, SEP 20, 2018
                    sdf.applyPattern("EEE, MMM dd, yyyy");
                    break;
                case 3: //SEP 20
                    sdf.applyPattern("MMM dd");
                    break;
                case 4: //SEP 20 1994
                    sdf.applyPattern("MMM dd yyyy");
                    break;
                case 5: //20 SEP
                    sdf.applyPattern("dd MMM");
                    break;
                case 6:
                    sdf.applyPattern("dd");
                    break;
                case 7:
                    sdf.applyPattern("MMM");
                    break;
                case 8:
                    sdf.applyPattern("EEE");
                    break;
                case 9:
                    sdf.applyPattern("dd-MM-yyyy");
                    break;
                default://2018-09-22
                    sdf.applyPattern("yyyy-MM-dd");
                    break;
            }
            ss = sdf.format(date);
        } catch (ParseException e) {
            //e.printStackTrace();
            ss = dates;
        }

        return ss;

    }

    public static Date StrtoDate (int format,String string) {
        SimpleDateFormat sdf ;

        switch (format) {
            case 1:
                sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                break;
            case 2:
                sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.getDefault());
                break;
            default: // case 0
                sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                break;
        }
        try {
            return sdf.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String DatetoStr (Object date,int  format) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        String ss ;
        switch (format) {
            case 1: //20-SEP-2018
                sdf.applyPattern("dd-MMM-yyyy");
                break;
            case 2: //WED, SEP 20, 2018
                sdf.applyPattern("EEE, MMM dd, yyyy");
                break;
            case 3: //SEP 20
                sdf.applyPattern("MMM dd");
                break;
            case 4: //10:20 AM
                sdf.applyPattern("hh:mm aa");
                break;
            case 5: //Sep 20,1994
                sdf.applyPattern("MMM dd, yyyy");
                break;
            case 6:
                sdf.applyPattern("dd");
                break;
            case 7:
                sdf.applyPattern("MMM");
                break;
            case 8:
                sdf.applyPattern("EEE");
                break;
            default://2018-09-22
                sdf.applyPattern("yyyy-MM-dd");
                break;
        }
        ss = sdf.format(date);

        return ss;

    }



    public static void makeToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }


    public static boolean isNullOrEmpty(String string){
        return TextUtils.isEmpty(string);
    }
}