package com.example.bel.softwarefactory.ui.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.widget.EditText;

import com.example.bel.softwarefactory.R;
import com.example.bel.softwarefactory.api.Api;
import com.example.bel.softwarefactory.entities.RegisterRequest;
import com.example.bel.softwarefactory.entities.UserEntity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {

    @ViewById
    protected EditText username_editText;

    @ViewById
    protected EditText email_editText;

    @ViewById
    protected EditText password_editText;

    @ViewById
    protected EditText passwordConfirmation_editText;

    @Click(R.id.signIn_textView)
    protected void signIn_textView_click() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Click(R.id.confirmRegistration_imageButton)
    protected void confirmRegistration_imageButton_click() {
        String username = username_editText.getText().toString();
        String email = email_editText.getText().toString();
        String password = password_editText.getText().toString();
        String passwordConf = passwordConfirmation_editText.getText().toString();

        if (isEmailValid(email)) {
            // Check Password and Confirmed Password
            // if not matched show toast
            if (password.equals(passwordConf)) {
                UserEntity newUser = new UserEntity(username, email, password);
                signUpUser(newUser);
            } else {
                showToast("Passwords does not match. Please, try again.");
            }
        } else {
            showToast("Email is incorrect. Please, try again.");
        }
    }

    @AfterViews
    protected void afterViews() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void signUpUser(UserEntity user) {
        Api api = new Api();
        RegisterRequest registerRequest = new RegisterRequest(user);
        showProgress(getString(R.string.registering));
        api.registerUser(registerRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(resultEntity -> {
                    hideProgress();
                    if (resultEntity.isOk()) {
                        LoginActivity_.intent(RegisterActivity.this).flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK).start();
                    } else {
                        showToast(resultEntity.getResult());
                    }
                }, this::handleError);
    }

}
