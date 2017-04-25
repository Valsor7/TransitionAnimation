package com.boost.transitionanimation;

import android.animation.Animator;
import android.support.design.widget.FloatingActionButton;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;

import static com.boost.transitionanimation.SharedElementTransitionListener.TransitionType.ENTER_TRANSITION;

/**
 * Created by yaroslav on 21.04.17.
 */

public class SharedElementTransitionListener implements Transition.TransitionListener {
    private static final String TAG = "SharedElementTransition";
    private TransitionType mTransitionType;
    private View mContainer;
    private FloatingActionButton mFab;

    public enum TransitionType {
        ENTER_TRANSITION, EXIT_TRANSITION
    }

    public SharedElementTransitionListener(TransitionType type, View container, FloatingActionButton fab) {
        mTransitionType = type;
        mContainer = container;
        mFab = fab;
    }

    @Override
    public void onTransitionStart(Transition transition) {

    }

    @Override
    public void onTransitionEnd(Transition transition) {
        Log.d(TAG, "onTransitionEnd: " + transition);

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
