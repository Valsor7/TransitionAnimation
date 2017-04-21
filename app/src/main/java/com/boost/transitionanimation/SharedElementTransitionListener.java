package com.boost.transitionanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;

import static com.boost.transitionanimation.SharedElementTransitionListener.TransitionType.ENTER_TRANSITION;
import static com.boost.transitionanimation.SharedElementTransitionListener.TransitionType.EXIT_TRANSITION;

/**
 * Created by yaroslav on 21.04.17.
 */

public class SharedElementTransitionListener implements Transition.TransitionListener {

    private TransitionType mTransitionType;
    private View mContainer;
    private View mBioContainer;

    public enum TransitionType {
        ENTER_TRANSITION, EXIT_TRANSITION
    }

    public SharedElementTransitionListener(TransitionType type, View container, View bioContainer) {
        mTransitionType = type;
        mContainer = container;
        mBioContainer = bioContainer;
    }

    @Override
    public void onTransitionStart(Transition transition) {

    }

    @Override
    public void onTransitionEnd(Transition transition) {
        if (mTransitionType == ENTER_TRANSITION){
            mBioContainer.setVisibility(View.VISIBLE);
            mContainer.setVisibility(View.VISIBLE);
            int cx = mContainer.getWidth();
            int cy = mContainer.getHeight();

            float finalRadius = (float) Math.hypot(cx, cy);

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(mContainer, cx, cy, 0, finalRadius);
            anim.start();
        }
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
}
