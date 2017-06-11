package com.bigdj.emergetransaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.View;
import android.view.MenuItem;

public class MatHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Slide slide = new Slide();
                //slide.setDuration(500);
                Intent intent = new Intent(getApplicationContext(), MatPurchaseView.class);
                intent.putExtra("item", 0);
                startActivity(intent);
            }
        });

        findViewById(R.id.second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Slide slide = new Slide();
                //slide.setDuration(500);
                Intent intent = new Intent(getApplicationContext(), MatPurchaseView.class);
                intent.putExtra("item", 1);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MatTransactionActivity.class);
                startActivity(intent);
            }
        });
    }
}
