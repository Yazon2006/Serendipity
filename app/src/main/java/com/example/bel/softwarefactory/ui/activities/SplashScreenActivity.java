package com.example.bel.softwarefactory.ui.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bel.softwarefactory.R;
import com.example.bel.softwarefactory.api.Api;
import com.example.bel.softwarefactory.preferences.SharedPreferencesManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_splash)
public class SplashScreenActivity extends BaseActivity {

    @Bean
    protected SharedPreferencesManager sharedPreferencesManager;

    @ViewById
    protected ImageView logo_imageView;
    @ViewById
    protected TextView copyright_textView;

    private boolean weCanGoToNextActivity = false;

    @AfterViews
    protected void afterView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        //стартуем анимацию появления лого и копирайта
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo_imageView.startAnimation(animation);
        copyright_textView.startAnimation(animation);

        //Здесь я запускаю таймер на 2,5 секунды, который после отработки будет проверять
        //отработал ли наш запрос на получение данных, если да, то стартуем следующую activity
        //если нет, то даем разрешение на переход к следующей activity
        Observable.timer(2500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(ignored -> proceedToNextActivity(), this::handleError);

        //здесь я запускаю запрос на получение данных с сервера, если мы получаем их раньше,
        //чем отработал таймер, даем разрешение на переход к следуюещй activity,
        //а иначе сразу переходим
        Api api = new Api();
        api.getAudioRecordsList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::doOnError)
                .subscribe(audioRecordEntities -> {
                    sharedPreferencesManager.saveAudioRecordsList(audioRecordEntities);
                    proceedToNextActivity();
                }, this::doOnError);
    }

    //в зависимости от того залогинен ли пользователь мы переходим на след. activity
    //если залогинен, то MenuActivity, если нет, то FirstActivity
    private void proceedToNextActivity() {
        if (weCanGoToNextActivity) {
            boolean serendipityUser = sharedPreferencesManager.isUserLoggedIn() && sharedPreferencesManager.isRememberMe();
            if (serendipityUser || sharedPreferencesManager.isFacebookLoggedIn()) {
                MenuActivity_.intent(SplashScreenActivity.this).flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK).start();
            } else {
                FirstActivity_.intent(SplashScreenActivity.this).flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK).start();
            }
        } else {
            weCanGoToNextActivity = true;
        }
    }

    private void doOnError(Throwable throwable) {
        if (sharedPreferencesManager.getAudioRecordsList() == null || sharedPreferencesManager.getAudioRecordsList().isEmpty()) {
            runOnUiThread(() -> SplashScreenActivity.this.showToast(getString(R.string.something_wrong_cant_start)));
            finish();
        } else {
            proceedToNextActivity();
        }
    }
}
