package com.boost.transitionanimation;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    FragmentCallback mChangeFragmentCallback;

    public HomeFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCallback){
            Log.d(TAG, "onAttach: FragmentCallback");
            mChangeFragmentCallback = (FragmentCallback) context;
        }
//        if (context instanceof HomeInteraction){
//            Log.d(TAG, "onAttach: HomeInteraction");
//            mHomeInteraction = (HomeInteraction) context;
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChangeFragmentCallback != null){
                    mChangeFragmentCallback.onChangeFragment(FragmentCats.TAG, null);
                }
            }
        });
    }
}
