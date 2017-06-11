package com.bigdj.emergetransaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by thatkawaiiguy on 6/10/17.
 */

public class TransactionActivitySimple extends KairosActivity {
    int count = 0;
    public static String approvalCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_transaction_main_simple);
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
                        timer.cancel();
                        timer.purge();
                        Intent intent = new Intent(getApplicationContext(), SuccessActivitySimple.class);
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
                        timer.cancel();
                        timer.purge();
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

    @Override
    protected void onResume() {
        super.onResume();
        reinitTimer();
    }
    private static String readStream(InputStream in) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = in.read();
            while(i != -1) {
                bo.write(i);
                i = in.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }
    public static String visaRequest() {
        URL url = null;
        try {
            url = new URL("http://880f2ff2.ngrok.io/visa");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            approvalCode = readStream(in);
            Log.d("AprovalCodeMeme",approvalCode);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return approvalCode;
    }
}
