package com.bigdj.emergetransaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by thatkawaiiguy on 6/10/17.
 */

public class TransactionActivitySimplest extends AppCompatActivity {
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_transaction_main_simplest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button button = (Button) findViewById(R.id.next);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(count) {
                    case 0:
                        findViewById(R.id.simple1).setVisibility(View.GONE);
                        findViewById(R.id.simple2).setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        findViewById(R.id.simple2).setVisibility(View.GONE);
                        findViewById(R.id.simple3).setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        findViewById(R.id.simple3).setVisibility(View.GONE);
                        findViewById(R.id.simple4).setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        findViewById(R.id.simple4).setVisibility(View.GONE);
                        findViewById(R.id.simple5).setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        findViewById(R.id.simple5).setVisibility(View.GONE);
                        findViewById(R.id.simple6).setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        Intent intent = new Intent(getApplicationContext(), SuccessActivitySimplest.class);
                        startActivity(intent);
                        break;
                }
                count++;
            }
        });

        final Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(count) {
                    case 0:
                        finish();
                        break;
                    case 1:
                        findViewById(R.id.simple1).setVisibility(View.VISIBLE);
                        findViewById(R.id.simple2).setVisibility(View.GONE);
                        break;
                    case 2:
                        findViewById(R.id.simple2).setVisibility(View.VISIBLE);
                        findViewById(R.id.simple3).setVisibility(View.GONE);
                        break;
                }
                count--;
            }
        });

        Helper.makeViewFlash(button, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
