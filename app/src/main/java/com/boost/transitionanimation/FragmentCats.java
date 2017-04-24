package com.boost.transitionanimation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boost.transitionanimation.dummy.DummyContent.DummyItem;
import com.boost.transitionanimation.model.ApiModule;
import com.boost.transitionanimation.model.CatModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FragmentCats extends Fragment {
    public static final String TAG = "FragmentCats";
    private static final int CATS_AMOUNT = 10;

    @BindView(R.id.rl_profiles)
    RecyclerView mProfilesRecyclerView;

    private List<Profile> mProfiles = new ArrayList<>();
    private ProfilesAdapter mProfilesAdapter;
    private OnListFragmentInteractionListener mListener;
    private FragmentCallback mFragmentCallback;

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(DummyItem item);
    }

    private OnClickProfileListener mViewHolderListener = new OnClickProfileListener() {
        @Override
        public void onClickItem(View view) {
            int position = mProfilesRecyclerView.getChildAdapterPosition(view);
            Profile profile = mProfiles.get(position);

            ImageView ivAvatar = ButterKnife.findById(view, R.id.iv_avatar);
            ivAvatar.setTransitionName(getString(R.string.shared_image_tag) + position);

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
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterTransition(new Slide());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        initRecyclerView();

        toggleViewsVisibility(mProfilesRecyclerView);

        return view;
    }

    private void toggleViewsVisibility(View view) {
        boolean visible = view.getVisibility() == View.VISIBLE;
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);

    }

    private void openProfileFragment(ImageView ivAvatar, Profile profile) {
        setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.move));
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment.newInstance(profile), ProfileFragment.TAG)
                .addSharedElement(ivAvatar, ViewCompat.getTransitionName(ivAvatar))
                .addToBackStack(null)
                .commit();
    }

    private void initRecyclerView() {
        mProfilesAdapter = new ProfilesAdapter(mProfiles, mViewHolderListener);

        mProfilesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mProfilesRecyclerView.setAdapter(mProfilesAdapter);

        setupDummyData();
    }

    private void setupDummyData() {
        int cat = CATS_AMOUNT;

        while (cat != 0) {
            Profile profile = new Profile();
            profile.setFullName("My cat " + cat);
            mProfiles.add(profile);
//            initCall(cat - 1);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
