package com.boost.transitionanimation;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.List;

import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements FragmentCallback, FragmentCats.OnListFragmentInteractionListener{
    private static final String TAG = "HomeActivity";
    private String mCurrentFragmentTag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new HomeFragment())
                .commit();
    }

    @Override
    public void onChangeFragment(String tag, Bundle bundle, View sharedView) {
        Log.d(TAG, "onChangeFragment() called with: tag = [" + tag + "], bundle = [" + bundle + "]");
        mCurrentFragmentTag = tag;
        Fragment fragment = new Fragment();
        switch (tag){
            case FragmentCats.TAG:
                fragment = FragmentCats.newInstance();
                break;
            case ProfileFragment.TAG:
                Profile profile = new Profile();
                if (bundle != null){
                    profile = bundle.getParcelable(Profile.class.getSimpleName());
                }
                fragment = ProfileFragment.newInstance(profile);
                break;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .addSharedElement(sharedView, ViewCompat.getTransitionName(sharedView))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onListFragmentInteraction() {

    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(mCurrentFragmentTag);
        Log.d(TAG, "onBackPressed: " + fragment);
        if (fragment != null && fragment instanceof ProfileFragment){
            ((ProfileFragment)fragment).onBackpressed();
        } else if (fragment != null && fragment instanceof FragmentCats) {
            ((FragmentCats)fragment).onBackpressed();
        } else {
            super.onBackPressed();
        }
    }
}
