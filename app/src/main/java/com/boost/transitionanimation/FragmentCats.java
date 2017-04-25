package com.boost.transitionanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.boost.transitionanimation.model.ApiModule;
import com.boost.transitionanimation.model.CatModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.boost.transitionanimation.SharedElementTransitionListener.TransitionType.ENTER_TRANSITION;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FragmentCats extends Fragment implements OnBackPressedHelperListener {
    public static final String TAG = "FragmentCats";
    private static final int CATS_AMOUNT = 10;
    private static final String EXTRA_CATS = "EXTRA_CATS";

    @BindView(R.id.rl_profiles)
    RecyclerView mProfilesRecyclerView;

    @BindView(R.id.layout_fab)
    FrameLayout mFrameLayout;

    @BindView(R.id.fab)
    FloatingActionButton mFab;

    private ArrayList<Profile> mProfiles = new ArrayList<>();
    private ProfilesAdapter mProfilesAdapter;
    private OnListFragmentInteractionListener mListener;
    private FragmentCallback mFragmentCallback;

    @Override
    public void onBackpressed() {
        Log.d(TAG, "onBackpressed: ");
        int cx = mFrameLayout.getWidth() / 2;
        int cy = mFrameLayout.getHeight() / 2;

        float finalRadius = (float) Math.hypot(cx, cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(mFrameLayout, cx, cy, finalRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mFab.setVisibility(View.VISIBLE);
                mFrameLayout.setBackgroundColor(Color.TRANSPARENT);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        anim.start();
    }


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction();
    }

    private OnClickProfileListener mViewHolderListener = new OnClickProfileListener() {
        @Override
        public void onClickItem(View view) {
            int position = mProfilesRecyclerView.getChildAdapterPosition(view);
            Profile profile = mProfiles.get(position);

            ImageView ivAvatar = ButterKnife.findById(view, R.id.iv_avatar);

            profile.setTransitionName(ivAvatar.getTransitionName());
            openProfileFragment(ivAvatar, profile);
        }
    };

    public FragmentCats() {
    }


    public static FragmentCats newInstance() {
        FragmentCats fragment = new FragmentCats();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        }
        if (context instanceof FragmentCallback){
            mFragmentCallback = (FragmentCallback) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterTransition(new Slide());
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        TransitionSet transitionSet = (TransitionSet) getSharedElementEnterTransition();
        transitionSet.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                if (getView() == null) return;
                mFrameLayout.setVisibility(View.VISIBLE);
                mFab.setVisibility(View.GONE);

                int cx = mFrameLayout.getWidth() / 2;
                int cy = mFrameLayout.getHeight() / 2;

                float finalRadius = (float) Math.hypot(cx, cy);

                Animator anim = ViewAnimationUtils.createCircularReveal(mFrameLayout, cx, cy, 0, finalRadius);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        initRecyclerView();


        if (savedInstanceState != null){
            mProfiles = savedInstanceState.getParcelableArrayList(EXTRA_CATS);
        }
        if (mProfiles != null && mProfiles.isEmpty()) {
            setupDummyData();
        } else {
            mProfilesAdapter.notifyDataSetChanged();
        }
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.BOTTOM);
        slide.setInterpolator(new BounceInterpolator());
        slide.addTarget(mProfilesRecyclerView);
        setEnterTransition(slide);

        return view;
    }


    private void toggleViewVisibility(View view) {
        boolean visible = view.getVisibility() == View.VISIBLE;
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);

    }

    private void openProfileFragment(ImageView ivAvatar, Profile profile) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Profile.class.getSimpleName(), profile);
        mFragmentCallback.onChangeFragment(ProfileFragment.TAG, bundle, ivAvatar);
    }

    private void initRecyclerView() {
        mProfilesAdapter = new ProfilesAdapter(mProfiles, mViewHolderListener);

        mProfilesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mProfilesRecyclerView.setAdapter(mProfilesAdapter);
    }

    private void setupDummyData() {
        int cat = CATS_AMOUNT;
        mProfiles.clear();
        while (cat != 0) {
            Profile profile = new Profile();
            profile.setFullName("My cat " + cat);

            mProfiles.add(profile);
            initCall(cat - 1);
            cat--;
        }
    }

    private void initCall(final int cat) {
        Call<CatModel> meawCall = ApiModule.getApiInterface().getRandomUrl();
        meawCall.enqueue(new Callback<CatModel>() {
            @Override
            public void onResponse(Call<CatModel> call, Response<CatModel> response) {
                Log.d(TAG, "onResponse: ");
                if (response.body() != null)
                    Log.d(TAG, "onResponse: " + response.body());
                mProfiles.get(cat).setAvatarUrl(response.body().getFile());
                if (cat == 1) {
                    mProfilesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<CatModel> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(EXTRA_CATS, mProfiles);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
