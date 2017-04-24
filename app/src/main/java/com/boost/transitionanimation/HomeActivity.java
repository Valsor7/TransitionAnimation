package com.boost.transitionanimation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.boost.transitionanimation.dummy.DummyContent;

import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements FragmentCallback, FragmentCats.OnListFragmentInteractionListener{
    private static final String TAG = "HomeActivity";

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
    public void onChangeFragment(String tag, Bundle bundle) {
        Log.d(TAG, "onChangeFragment() called with: tag = [" + tag + "], bundle = [" + bundle + "]");
        switch (tag){
            case FragmentCats.TAG:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, FragmentCats.newInstance(), FragmentCats.TAG)
                        .addToBackStack(null)
                        .addSharedElement(ButterKnife.findById(this, R.id.fab), getString(R.string.shared_tag_fab))
                        .commit();
        }
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
