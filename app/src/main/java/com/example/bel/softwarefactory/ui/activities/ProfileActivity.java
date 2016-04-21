package com.example.bel.softwarefactory.ui.activities;

import android.app.AlertDialog;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bel.softwarefactory.R;
import com.example.bel.softwarefactory.api.Api;
import com.example.bel.softwarefactory.entities.ChangePasswordRequest;
import com.example.bel.softwarefactory.entities.ChangeUserData;
import com.example.bel.softwarefactory.preferences.SharedPreferencesManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_profile)
public class ProfileActivity extends BaseActivity {

    @ViewById
    protected TextView userName_textView;
    @ViewById
    protected EditText username_editText;
    @ViewById
    protected EditText email_editText;
    @ViewById
    protected EditText password_editText;
    @ViewById
    protected EditText passwordConfirmation_editText;

    @Bean
    protected SharedPreferencesManager sharedPreferencesManager;

    @AfterViews
    protected void afterViews() {
        userName_textView.setText(sharedPreferencesManager.getUser().getUsername());
        username_editText.setText(sharedPreferencesManager.getUser().getUsername());
        email_editText.setText(sharedPreferencesManager.getUser().getEmail());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Click(R.id.saveChanges_button)
    protected void saveChanges_button_click() {
        String userName = username_editText.getText().toString();
        String email = email_editText.getText().toString();

        final String previousEmail = sharedPreferencesManager.getUser().getEmail();
        boolean isNameChanging = true;
        boolean isEmailChanging = true;

        if (userName.isEmpty() || userName.equals(sharedPreferencesManager.getUser().getUsername())) {
            userName = sharedPreferencesManager.getUser().getUsername();
            isNameChanging = false;
        }

        if (email.isEmpty() || email.equals(previousEmail)) {
            email = sharedPreferencesManager.getUser().getEmail();
            isEmailChanging = false;
        }

        if (isNameChanging || isEmailChanging) {
            if (isEmailValid(email)) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
                //create EditText inside of the Alert
                final EditText input = new EditText(ProfileActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                //code taken from
                //http://stackoverflow.com/questions/2586301/set-inputtype-for-an-edittext
                input.setTransformationMethod(PasswordTransformationMethod.getInstance());
                input.setGravity(Gravity.CENTER);
                dialogBuilder.setView(input);
                dialogBuilder.setMessage("Verify password");

                final String usernameToChange = userName;
                final String emailToChange = email;

                dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
                    if (input.getText().toString().equals(sharedPreferencesManager.getUser().getPassword())) {
                        Api api = new Api();
                        showProgress(getString(R.string.changing_user_info));
                        api.changeUserData(new ChangeUserData(usernameToChange, emailToChange, previousEmail))
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .compose(bindToLifecycle())
                                .subscribe(resultEntity -> {
                                    hideProgress();
                                    showToast(resultEntity.getResult());
                                }, this::handleError);
                    } else
                        showAlert("Incorrect password");
                });
                dialogBuilder.setNegativeButton("Cancel", null);
                dialogBuilder.show();
            } else
                showAlert("Email incorrect");
        } else
            showAlert("Nothing to change");
    }

    @Click(R.id.changePass_button)
    protected void changePass_button_click() {
        final String password = password_editText.getText().toString();
        String passwordConf = passwordConfirmation_editText.getText().toString();

        if (password.equals(passwordConf)) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
            //create EditText inside of the Alert
            final EditText input = new EditText(ProfileActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);

            //code taken from
            //http://stackoverflow.com/questions/2586301/set-inputtype-for-an-edittext
            input.setTransformationMethod(PasswordTransformationMethod.getInstance());
            input.setGravity(Gravity.CENTER);
            input.setSingleLine();
            dialogBuilder.setView(input);
            dialogBuilder.setMessage("Verify password");

            dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
                if (input.getText().toString().equals(sharedPreferencesManager.getUser().getPassword())) {
                    ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(sharedPreferencesManager.getUser().getEmail(), sharedPreferencesManager.getUser().getPassword(), password);
                    Api api = new Api();
                    api.changePassword(changePasswordRequest)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .compose(bindToLifecycle())
                            .subscribe(resultEntity -> {
                                hideProgress();
                                showToast(resultEntity.getResult());
                            }, this::handleError);
                } else
                    showAlert("Incorrect password");
            });
            dialogBuilder.setNegativeButton("Cancel", null);
            dialogBuilder.show();
        } else {
            showAlert("Passwords does not match. Please, try again.");
        }
    }

}
