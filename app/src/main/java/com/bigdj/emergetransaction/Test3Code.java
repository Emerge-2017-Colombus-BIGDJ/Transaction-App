package com.bigdj.emergetransaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by thegr on 6/10/2017.
 */

public class Test3Code extends AppCompatActivity {

    int option = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test3);

        final int results = getIntent().getExtras().getInt("option");

        final Button choice1 = (Button) findViewById(R.id.settings_test_button);
        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                option = 1 + results;
                launchActivity();
            }
        });

        final Button choice2 = (Button) findViewById(R.id.flower_button);
        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                option = results;
                launchActivity();
            }
        });

    }

    private void launchActivity() {
        Intent intent;
        if(option > 6) {
            intent = new Intent(this, HomeActivityMat.class);
        } else if(option >= 4)
            intent = new Intent(this, HomeActivitySimple.class);
        else
            intent = new Intent(this, HomeActivitySimplest.class);
        intent.putExtra("option", option);
        startActivity(intent);
    }
}
