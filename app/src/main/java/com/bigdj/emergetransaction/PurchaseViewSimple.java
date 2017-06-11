package com.bigdj.emergetransaction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by thatkawaiiguy on 6/10/17.
 */

public class PurchaseViewSimple extends AppCompatActivity {

    boolean firstRun = true;

    FloatingActionButton fab;

    RelativeLayout container;
    CardView realContainer;

    Button imageButton;
    TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().getExtras().getInt("item") == 0)
            setContentView(R.layout.purchase_view_main_simple);
        else
            setContentView(R.layout.purchase_view_main_simple1);

        container = (RelativeLayout) findViewById(R.id.container);
        realContainer = (CardView) findViewById(R.id.realContainer);
        imageButton = (Button) findViewById(R.id.close);
        title = (TextView) findViewById(R.id.title);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
