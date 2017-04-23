package com.boost.transitionanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.Visibility;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

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

    @BindView(R.id.tv_bio)
    TextView mTvbio;

    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        Profile profile = getIntent().getParcelableExtra(ProfileActivity.class.getSimpleName());

        if (profile != null){
//            postponeEnterTransition();
            getWindow().getSharedElementEnterTransition().addListener(new SharedElementTransitionListener(ENTER_TRANSITION, mContainer, mFab));
            getWindow().getSharedElementExitTransition().addListener(new SharedElementTransitionListener(EXIT_TRANSITION, mContainer, mFab));
            Log.d(TAG, "onCreate: " + profile.getAvatarUrl());
//            setupIcon(profile);
        }
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.BOTTOM);
        slide.addTarget(R.id.tv_bio);
        slide.addTarget(R.id.fab);

        getWindow().setEnterTransition(slide);


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
                ViewAnimationUtils.createCircularReveal(mContainer, cx, cy, initialRadius, - 10);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mContainer.setBackgroundColor(Color.TRANSPARENT);
                supportFinishAfterTransition();
            }
        });
        anim.start();
    }
}
