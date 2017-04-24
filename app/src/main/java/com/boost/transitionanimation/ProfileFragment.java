package com.boost.transitionanimation;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.Fade;
import android.support.transition.TransitionSet;
import android.support.v4.app.Fragment;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    public static final String TAG = "ProfileFragment";

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
    private Profile mProfile;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(Profile profile) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(Profile.class.getSimpleName(), profile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.BOTTOM);
        slide.addTarget(R.id.tv_bio);
        slide.addTarget(R.id.fab);

        setEnterTransition(slide);
        slide.setSlideEdge(Gravity.TOP);
        setExitTransition(slide);
        setSharedElementEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.move));
        setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.move));
        mProfile = getArguments().getParcelable(Profile.class.getSimpleName());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (mProfile != null) {
            Log.d(TAG, "onCreate: setTransitionName " + mProfile.getTransitionName());
            mIvAvatar.setTransitionName(mProfile.getTransitionName());
//            postponeEnterTransition();
//            Transition transitionEnter = (Transition) getSharedElementEnterTransition();

//            transitionEnter.addListener(new SharedElementTransitionListener(ENTER_TRANSITION, mContainer, mFab));
//            transitionExit.addListener(new SharedElementTransitionListener(EXIT_TRANSITION, mContainer, mFab));

            Log.d(TAG, "onCreate: " + mProfile.getAvatarUrl());
//            setupIcon(profile);
        }
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

    private void scheduleStartPostponedTransition() {
        mIvAvatar.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mIvAvatar.getViewTreeObserver().removeOnPreDrawListener(this);
                startPostponedEnterTransition();
                return true;
            }
        });
    }

    public void onBackPressed() {
        int cx = mContainer.getWidth();
        int cy = mContainer.getHeight();

        float initialRadius = (float) Math.hypot(cx, cy);

        Animator anim =
                ViewAnimationUtils.createCircularReveal(mContainer, cx, cy, initialRadius, -10);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mContainer.setBackgroundColor(Color.TRANSPARENT);
            }
        });
        anim.start();
    }
}
