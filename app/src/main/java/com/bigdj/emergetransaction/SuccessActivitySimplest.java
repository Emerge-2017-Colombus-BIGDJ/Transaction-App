package com.bigdj.emergetransaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by thatkawaiiguy on 6/10/17.
 */

public class SuccessActivitySimplest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_success_main_simplest);
        TextView aprovalCode = (TextView) findViewById(R.id.approvalCode);
        aprovalCode.setText("Loading...");
        while(aprovalCode.getText() == "Loading...") {
            if(TransactionActivityMat.approvalCode != null) {
                if(!TransactionActivityMat.approvalCode.isEmpty()){
                aprovalCode.setText("Approval code: "+ TransactionActivityMat.approvalCode);
                }
            }
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView time = (TextView) findViewById(R.id.time);
        SimpleDateFormat sdf = new SimpleDateFormat("K:mm a");
        String currentDate = sdf.format(new Date());
        time.setText("Time: " + currentDate);

        Button home = (Button) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivitySimplest.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        Helper.makeViewFlash(home, this);
    }
}
