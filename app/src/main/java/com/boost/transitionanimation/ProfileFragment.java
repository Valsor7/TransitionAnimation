package com.boost.transitionanimation;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.boost.transitionanimation.SharedElementTransitionListener.TransitionType.ENTER_TRANSITION;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements OnBackPressedHelperListener {
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
    private TransitionSet transitionEnter;


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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();

        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.BOTTOM);
        slide.addTarget(R.id.tv_bio);
        slide.addTarget(R.id.fab);

        setEnterTransition(slide);

        setSharedElementEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.move));
        setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.move));
        transitionEnter = (TransitionSet) getSharedElementEnterTransition();
        transitionEnter.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                Log.d(TAG, "onTransitionEnd: ");
                Log.d(TAG, "onTransitionEnd: enter");
                mContainer.setVisibility(View.VISIBLE);
                int cx = mContainer.getWidth();
                int cy = mContainer.getHeight();

                float finalRadius = (float) Math.hypot(cx, cy);

                Animator anim =
                        ViewAnimationUtils.createCircularReveal(mContainer, cx, cy, 0, finalRadius);
                anim.start();
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


        mProfile = getArguments().getParcelable(Profile.class.getSimpleName());
        Log.d(TAG, "onCreate: ");
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

        Log.d(TAG, "onViewCreated: ");
        if (mProfile != null) {
            Log.d(TAG, "onCreate: setTransitionName " + mProfile.getTransitionName());
            mIvAvatar.setTransitionName(mProfile.getTransitionName());
            Log.d(TAG, "onCreate: " + mProfile.getAvatarUrl());
            setupIcon(mProfile);
        }
    }

    private void setupIcon(Profile profile) {
        Glide.with(this).load(profile.getAvatarUrl()).dontAnimate().skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
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

    @Override
    public void onBackpressed() {
        int cx = mContainer.getWidth();
        int cy = mContainer.getHeight();

        float initialRadius = (float) Math.hypot(cx, cy);

        Animator anim =
                ViewAnimationUtils.createCircularReveal(mContainer, cx, cy, initialRadius, 0);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mContainer.setBackgroundColor(Color.TRANSPARENT);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        anim.start();
    }
}
