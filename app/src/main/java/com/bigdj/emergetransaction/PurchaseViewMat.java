package com.bigdj.emergetransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by thatkawaiiguy on 6/10/17.
 */

public class PurchaseViewMat extends KairosActivity {

    boolean firstRun = true;

    FloatingActionButton fab;

    RelativeLayout container;
    CardView realContainer;

    ImageButton imageButton;
    TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().getExtras().getInt("item") == 0)
            setContentView(R.layout.purchase_view_main_mat);
        else
            setContentView(R.layout.purchase_view_main_mat1);
        initializeCamera();

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccentDark));

        container = (RelativeLayout) findViewById(R.id.container);
        realContainer = (CardView) findViewById(R.id.realContainer);
        imageButton = (ImageButton) findViewById(R.id.close);
        title = (TextView) findViewById(R.id.title);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
