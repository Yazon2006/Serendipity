package com.example.bel.softwarefactory.ui.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.example.bel.softwarefactory.ui.activities.BaseActivity;
import com.trello.rxlifecycle.components.support.RxFragment;

public abstract class BaseFragment extends RxFragment {

    protected void handleError(Throwable throwable) {
        Activity activity = getActivity();
        if (activity != null) {
            ((BaseActivity) activity).handleError(throwable);
        }
    }

    protected void switchFragment(Fragment fragment) {
        Activity activity = getActivity();
        if (activity != null) {
            ((BaseActivity) activity).switchFragment(fragment);
        }
    }

    protected void showProgress(String message) {
        Activity activity = getActivity();
        if (activity != null) {
            ((BaseActivity) activity).showProgress(message);
        }
    }

    protected void hideProgress() {
        Activity activity = getActivity();
        if (activity != null) {
            ((BaseActivity) activity).hideProgress();
        }
    }
}
