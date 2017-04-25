package com.boost.transitionanimation;

import android.os.Bundle;
import android.view.View;

/**
 * Created by yaroslav on 24.04.17.
 */

public interface FragmentCallback {
    void onChangeFragment(String tag, Bundle bundle, View sharedView);
}
