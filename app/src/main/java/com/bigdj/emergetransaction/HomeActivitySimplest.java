package com.bigdj.emergetransaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

/**
 * Created by thatkawaiiguy on 6/10/17.
 */

public class HomeActivitySimplest extends KairosActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_simplest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button purchase = (Button) findViewById(R.id.purchase);
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                timer.purge();
                Intent intent = new Intent(getApplicationContext(), TransactionActivitySimplest
                        .class);
                startActivity(intent);
            }
        });

        final AppCompatActivity activity = this;
        Helper.makeViewFlash(purchase, activity);

    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        timer.purge();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeCamera(true);
    }
}
