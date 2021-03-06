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
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by thatkawaiiguy on 6/10/17.
 */

public class SuccessActivityMat extends AppCompatActivity {

    boolean firstRun = true;

    FloatingActionButton fab;

    RelativeLayout container;
    CardView realContainer;

    ImageButton imageButton;
    TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_success_main_mat);
        TextView aprovalCode = (TextView) findViewById(R.id.approvalCode);
        aprovalCode.setText("Loading...");
        while(aprovalCode.getText() == "Loading...") {
            if(TransactionActivityMat.approvalCode != null) {
               aprovalCode.setText(TransactionActivityMat.approvalCode);
            }
        }
        fab = (FloatingActionButton) findViewById(R.id.fab);
        container = (RelativeLayout) findViewById(R.id.container);
        realContainer = (CardView) findViewById(R.id.realContainer);
        imageButton = (ImageButton) findViewById(R.id.close);
        title = (TextView) findViewById(R.id.title);
        TextView time = (TextView) findViewById(R.id.time);
        SimpleDateFormat sdf = new SimpleDateFormat("K:mm a");
        String currentDate = sdf.format(new Date());
        time.setText(currentDate);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setupEnterAnimation();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (!firstRun) {
            container.setVisibility(View.VISIBLE);
            realContainer.setVisibility(View.VISIBLE);
            imageButton.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
        }
        firstRun = false;
    }

    private void setupEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition
                .changebounds_with_arcmotion);
        transition.setDuration(500);
        final AppCompatActivity activity = this;
        getWindow().setSharedElementEnterTransition(transition);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.colorAccentDark));
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow(container);
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }

    private void initViews() {
        final AppCompatActivity activity = this;

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(activity, android.R.anim
                        .fade_in);
                animation.setDuration(500);
                realContainer.startAnimation(animation);
                imageButton.startAnimation(animation);
                title.startAnimation(animation);
                realContainer.setVisibility(View.VISIBLE);
                imageButton.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);
                /*for(int i = 0; i < container.getChildCount(); i++) {
                    View child = container.getChildAt(i);
                    child.animate()
                            .setStartDelay(100 + i * 50)
                            .setInterpolator(new LinearOutSlowInInterpolator())
                            .alpha(1)
                            .scaleX(1)
                            .scaleY(1);
                }*/
            }
        });
    }

    @Override
    public void onBackPressed() {
        Slide slide = new Slide();
        slide.setDuration(600);
        getWindow().setExitTransition(slide);
        Intent intent = new Intent(this, HomeActivityMat.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, (View)fab, "reveal");
        //ActivityCompat.startActivity(this, intent, options.toBundle());
        /*GUIUtils.animateRevealHide(this, container, R.color.colorAccent, fab.getWidth() / 2,
                new OnRevealAnimationListener() {
                    @Override
                    public void onRevealHide() {
                        finish();
                    }

                    @Override
                    public void onRevealShow() {

                    }
                });*/
    }

    private void animateRevealShow(final View viewRoot) {
        int x = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int y = (viewRoot.getTop() + viewRoot.getBottom()) / 2;

        GUIUtils.animateRevealShow(this, viewRoot, fab.getWidth() / 2, R.color.colorAccent,
                x, y, new OnRevealAnimationListener() {
                    @Override
                    public void onRevealHide() {

                    }

                    @Override
                    public void onRevealShow() {
                        initViews();
                    }
                });
    }

}
