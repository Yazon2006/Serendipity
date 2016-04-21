package com.example.bel.softwarefactory.ui.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.example.bel.softwarefactory.R;
import com.example.bel.softwarefactory.utils.AlertDialogHelper;
import com.example.bel.softwarefactory.utils.AlertDialogHelper_;
import com.facebook.appevents.AppEventsLogger;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

public abstract class BaseActivity extends RxAppCompatActivity {

    private static final String FRAGMENT_TAG = "CurrentNavigationFragment";

    private ProgressDialog progressDialog;

    private AlertDialogHelper alertDialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppEventsLogger.activateApp(this);
        alertDialogHelper = AlertDialogHelper_.getInstance_(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        AppEventsLogger.deactivateApp(this);
    }

    public void switchFragment(Fragment fragment) {
        switchFragmentInternal(fragment, true);
    }

    public void switchFragment(Fragment fragment, Boolean addToBackStack) {
        switchFragmentInternal(fragment, addToBackStack);
    }

    public void removeCurrentFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(getCurrentFragment());
        trans.commit();
        manager.popBackStack();
    }

    public void showAlert(String message) {
        alertDialogHelper.showError(message);
    }

    public void showProgress(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void handleError(Throwable throwable) {
        hideProgress();
        showAlert(throwable.getMessage());
    }


    protected void switchFragmentInternal(Fragment fragment, boolean addToBackStack) {
        if (getCurrentFragment() == null || !getCurrentFragment().getTag().equals(fragment.getTag())) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment, FRAGMENT_TAG);
            if (addToBackStack)
                fragmentTransaction.addToBackStack(null);

            fragmentTransaction.commit();
        }
    }

    protected void switchFragmentRemovingTop(Fragment fragment) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment, FRAGMENT_TAG);

        fragmentTransaction.commit();
    }

    protected Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }

    protected void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    //code from http://stackoverflow.com/questions/6119722/how-to-check-edittexts-text-is-email-address-or-not
    protected boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
