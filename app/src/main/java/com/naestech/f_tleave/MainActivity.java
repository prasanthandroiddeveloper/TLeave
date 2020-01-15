package com.naestech.f_tleave;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(){
            @Override
            public void run(){
                try {
                    sleep(1000);
                    startActivity(new Intent(MainActivity.this,Login_Main_Act.class));
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    Toast.makeText(MainActivity.this,"System Busy , Try Again",Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        }.start();
    }
}
