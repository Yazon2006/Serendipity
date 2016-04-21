package com.example.bel.softwarefactory.ui.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;

import com.example.bel.softwarefactory.R;
import com.example.bel.softwarefactory.preferences.SharedPreferencesManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.first_activity)
public class FirstActivity extends BaseActivity {

    @Bean
    protected SharedPreferencesManager sharedPreferencesManager;

    @AfterViews
    protected void afterViews() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Click(R.id.login_button)
    protected void login_button_click() {
        LoginActivity_.intent(FirstActivity.this).start();
    }

    @Click(R.id.register_button)
    protected void register_button_click() {
        RegisterActivity_.intent(FirstActivity.this).start();
    }

    @Click(R.id.startAsVisitor_button)
    protected void startAsVisitor_button_click() {
        sharedPreferencesManager.setUserLoggedIn(false);
        MenuActivity_.intent(FirstActivity.this).flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK).start();
    }

}
