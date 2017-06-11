package com.bigdj.emergetransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

/**
 * Created by thegr on 6/10/2017.
 */

public class Test1Code extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tests1poc);

        final long showTime = 0;
        //3 + 3 + 1
        //6 or higher material
        //4 or higher, simple
        //lower simplest

        final Button click = (Button) findViewById(R.id.click);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long difference = System.currentTimeMillis() - showTime;
                int result = 0;
                if (difference < 1000)
                    result = 3;
                else if (difference < 2000) {
                    result = 2;
                } else {
                    result = 3;
                }
                Intent intent = new Intent(getApplicationContext(), Test2Code.class);
                intent.putExtra("result", result);
                startActivity(intent);
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        click.setVisibility(View.VISIBLE);
                        showTime = System.currentTimeMillis();
                    }
                });
            }
        }, 3500 + (int) (Math.random()*1500));
    }
}
