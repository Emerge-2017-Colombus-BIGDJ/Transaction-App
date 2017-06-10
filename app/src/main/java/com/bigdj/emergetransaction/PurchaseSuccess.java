package com.bigdj.emergetransaction;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

/**
 * Created by thatkawaiiguy on 6/10/17.
 */

public class PurchaseSuccess extends AppCompatActivity {

    FloatingActionButton fab;

    RelativeLayout container;
    CardView realContainer;

    ImageButton imageButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_success_main_mat);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        container = (RelativeLayout) findViewById(R.id.container);
        realContainer = (CardView) findViewById(R.id.realContainer);
        imageButton = (ImageButton) findViewById(R.id.close);

        setupEnterAnimation();
    }

    private void setupEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition
                .changebounds_with_arcmotion);
        transition.setDuration(500);
        getWindow().setSharedElementEnterTransition(transition);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
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
                /*Animation animation = AnimationUtils.loadAnimation(activity, android.R.anim.fade_in);
                animation.setDuration(500);
                realContainer.startAnimation(animation);
                realContainer.setVisibility(View.VISIBLE);*/
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
