package com.bigdj.emergetransaction;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.View;
import android.view.ViewAnimationUtils;

/**
 * Created by thatkawaiiguy on 6/10/17.
 */

public class GUIUtils {

    public static void animateRevealShow(final Context ctx, final View view, final int startRadius,
                                         @ColorRes final int color, int x, int y, final
                                         OnRevealAnimationListener listener) {
        float finalRadius = (float) Math.hypot(view.getWidth(), view.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(view, x, y, startRadius,
                finalRadius);
        anim.setDuration(300);
        anim.setStartDelay(80);
        anim.setInterpolator(new FastOutLinearInInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setBackgroundColor(ContextCompat.getColor(ctx, color));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.VISIBLE);
                if(listener != null) {
                    listener.onRevealShow();
                }
            }
        });
        anim.start();
    }

    public static void animateRevealHide(final Context ctx, final View view, @ColorRes final int color,
                                         final int finalRadius, final OnRevealAnimationListener listener) {
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;
        int startRadius = view.getWidth();
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, startRadius, finalRadius);
        anim.setInterpolator(new FastOutLinearInInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setBackgroundColor(ContextCompat.getColor(ctx, color));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(listener != null) {
                    listener.onRevealHide();
                }
                view.setVisibility(View.INVISIBLE);
            }
        });
        anim.setDuration(300);
        anim.start();
    }
}
