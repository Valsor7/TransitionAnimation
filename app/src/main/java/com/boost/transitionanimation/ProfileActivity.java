package com.boost.transitionanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.boost.transitionanimation.SharedElementTransitionListener.TransitionType.ENTER_TRANSITION;
import static com.boost.transitionanimation.SharedElementTransitionListener.TransitionType.EXIT_TRANSITION;

public class ProfileActivity extends AppCompatActivity  {
    private static final String TAG = "ProfileActivity";

    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;

    @BindView(R.id.action_container)
    View mContainer;

    @BindView(R.id.container_biography)
    ViewGroup mBioContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        getWindow().setEnterTransition(new AutoTransition());
        getWindow().setExitTransition(new AutoTransition());
        Profile profile = getIntent().getParcelableExtra(ProfileActivity.class.getSimpleName());

        if (profile != null){
            postponeEnterTransition();
            getWindow().getSharedElementEnterTransition().addListener(new SharedElementTransitionListener(ENTER_TRANSITION, mContainer, mBioContainer));
            getWindow().getSharedElementExitTransition().addListener(new SharedElementTransitionListener(EXIT_TRANSITION, mContainer, mBioContainer));
            Log.d(TAG, "onCreate: " + profile.getAvatarUrl());
            setupIcon(profile);
        }
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.START);
        TransitionManager.beginDelayedTransition(mBioContainer, slide);
    }

    private void setupIcon(Profile profile) {
        Glide.with(this).load(profile.getAvatarUrl()).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                Log.d(TAG, "onResourceReady: ");
                scheduleStartPostponedTransition();
                return false;
            }
        }).into(mIvAvatar);
    }

    private void scheduleStartPostponedTransition(){
        mIvAvatar.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mIvAvatar.getViewTreeObserver().removeOnPreDrawListener(this);
                startPostponedEnterTransition();
                return  true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int cx = mContainer.getWidth();
        int cy = mContainer.getHeight();

        float initialRadius = (float) Math.hypot(cx, cy);

        Animator anim =
                ViewAnimationUtils.createCircularReveal(mContainer, cx, cy, initialRadius, 0);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mContainer.setVisibility(View.INVISIBLE);
                supportFinishAfterTransition();
            }
        });
        anim.start();
    }
}
