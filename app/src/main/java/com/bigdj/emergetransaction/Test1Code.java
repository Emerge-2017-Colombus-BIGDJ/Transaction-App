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

public class Test1Code extends AppCompatActivity {

    int textTest = 0;
    int result = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test1);

        final Button choice1 = (Button) findViewById(R.id.choice_1_button);
        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textTest = 3;
                launchActivity();
            }
        });
        final Button choice2 = (Button) findViewById(R.id.choice_2_button);
        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textTest = 2;

                launchActivity();
            }
        });
        final Button choice3 = (Button) findViewById(R.id.choice_3_button);
        choice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textTest = 1;

                launchActivity();
            }
        });
    }


    private void launchActivity() {

        Intent intent = new Intent(this, Test2Code.class);
        intent.putExtra("result", result + textTest);
        startActivity(intent);
        finish();
    }
}
