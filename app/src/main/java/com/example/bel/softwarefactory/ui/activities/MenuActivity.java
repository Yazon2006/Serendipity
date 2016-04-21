package com.example.bel.softwarefactory.ui.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.bel.softwarefactory.R;
import com.example.bel.softwarefactory.entities.LeftMenuItem;
import com.example.bel.softwarefactory.preferences.SharedPreferencesManager;
import com.example.bel.softwarefactory.ui.adapters.DrawerListAdapter;
import com.example.bel.softwarefactory.ui.fragments.MapFragment_;
import com.example.bel.softwarefactory.ui.fragments.RecordFragment_;
import com.example.bel.softwarefactory.ui.fragments.AudioListFragment_;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;

@EActivity(R.layout.activity_menu)
public class MenuActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private final String TAG = this.getClass().getSimpleName();
    private ActionBarDrawerToggle drawerToggle;
    //facebook call back manager
    private CallbackManager facebookCallbackManager = CallbackManager.Factory.create();

    @ViewById
    protected View profile_layout;
    @ViewById
    protected TextView title_textView;
    @ViewById
    protected DrawerLayout drawerLayoutMenu;
    @ViewById
    protected ListView listViewMenu;
    @ViewById
    protected NavigationView navigationView;
    @ViewById
    protected Toolbar toolbar;
    @ViewById
    protected TextView userName_textView;
    @ViewById
    protected ImageView userPhoto_imageView;

    @StringArrayRes
    protected String[] itemTitles;
    @StringArrayRes
    protected String[] itemDescriptions;
    @InstanceState
    protected boolean proceedToExit = false;

    @Bean
    protected SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //при старте активити всегда будет первой стартовать карта
        switchFragment(MapFragment_.builder().build());
    }

    @AfterViews
    protected void afterViews() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
        title_textView.setText(itemTitles[0]);

        //здесь в зависимости от того залогинен ли юзер мы будем скрывать или показывать
        //layout с фото и именем пользователя
        if (sharedPreferencesManager.isUserLoggedIn()) {
            profile_layout.setVisibility(View.VISIBLE);
            userName_textView.setText(sharedPreferencesManager.getUser().getUsername());

            //c помощью пикассо мы асинхронно загружаем фото, сохраняя в кеш, и отображаем
            //в userPhoto_imageView
            if (sharedPreferencesManager.getProfilePictureUrl() != null) {
                Picasso.with(MenuActivity.this)
                        .load(sharedPreferencesManager.getProfilePictureUrl())
                        .into(userPhoto_imageView);
            }
        } else {
            profile_layout.setVisibility(View.GONE);
        }

        fillMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        Log.d(TAG, "onPrepareOptionsMenu()");
        MenuItem logOut = menu.findItem(R.id.overflowItemLogOut);

        if (!sharedPreferencesManager.isUserLoggedIn()) {
            logOut.setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu()");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_overflow, menu);

        if (sharedPreferencesManager.isUserLoggedIn()) {
            MenuItem item = menu.findItem(R.id.overflowItemLogOut);
            item.setVisible(true);
        }
        return true;
    }

    public void fillMenu() {
        Log.d(TAG, "fillMenu()");

        ArrayList<LeftMenuItem> leftMenuItems = new ArrayList<>();
        leftMenuItems.add(new LeftMenuItem(itemTitles[0], itemDescriptions[0], R.mipmap.ic_map_marker, 0));
        leftMenuItems.add(new LeftMenuItem(itemTitles[1], itemDescriptions[1], R.mipmap.ic_microphone, 1));
        leftMenuItems.add(new LeftMenuItem(itemTitles[2], itemDescriptions[2], R.mipmap.ic_note, 2));

        listViewMenu.setOnItemClickListener(this);
        DrawerListAdapter adapter = new DrawerListAdapter(this, leftMenuItems);
        listViewMenu.setAdapter(adapter);

        Log.d(TAG, "Before Menu Header initialization isLoggedIn: " + sharedPreferencesManager.isUserLoggedIn());

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayoutMenu, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                //Toast.makeText(MainActivity.this, "Drawer closed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //Toast.makeText(MainActivity.this, "Drawer opened", Toast.LENGTH_SHORT).show();
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.setHomeAsUpIndicator(R.mipmap.ic_menu);
        drawerLayoutMenu.addDrawerListener(drawerToggle);
    }

    @Click(R.id.profile_layout)
    protected void profile_layout_click() {
        drawerLayoutMenu.closeDrawers();
        if (!sharedPreferencesManager.isFacebookLoggedIn()) {
            ProfileActivity_.intent(MenuActivity.this).start();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onPostCreate()");
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected()");
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        switch (item.getItemId()) {
/*            case R.id.overflowItemSettings:
                //setting item in overflow menu
                break;*/
/*            case R.id.overflowItemAbout:
                //about item in overflow menu
                break;*/
            case R.id.overflowItemLogOut:
                if (sharedPreferencesManager.isUserLoggedIn()) {
                    //if logged in with facebook - log out
                    if (sharedPreferencesManager.isFacebookLoggedIn()) {
                        LoginManager.getInstance().logOut();
                        Log.d("DEBUG", "Log OUT FROM FACEBOOK" + LoginManager.getInstance());
                    }
                    //clean local store with user information
                    sharedPreferencesManager.clearUserData();
                    FirstActivity_.intent(MenuActivity.this).flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK).start();
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        title_textView.setText(itemTitles[position]);
        selectItem(position);
    }

    public void selectItem(int position) {
        listViewMenu.setItemChecked(position, true);
        switch (position) {
            case 0:
                switchFragment(MapFragment_.builder().build());
                break;
            case 1:
                if (sharedPreferencesManager.isUserLoggedIn()) {
                    switchFragment(RecordFragment_.builder().build());
                } else {
                    showLoginRequestDialog();
                }
                break;
            case 2:
                if (sharedPreferencesManager.getRecordsAvailability()) {
                    switchFragment(AudioListFragment_.builder().build());
                } else {
                    showToast(getString(R.string.no_audio_here));
                }
                break;
        }
        drawerLayoutMenu.closeDrawers();
    }

    private void showLoginRequestDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.you_should_login)
                .positiveText(R.string.goto_login)
                .negativeText(R.string.cancel)
                .onPositive((dialog, which) -> LoginActivity_.intent(MenuActivity.this).start())
                .show();
    }

    //facebook methods
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult()");
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayoutMenu.isDrawerVisible(GravityCompat.START) && drawerLayoutMenu.isDrawerOpen(GravityCompat.START)) {
            drawerLayoutMenu.closeDrawer(GravityCompat.START);
        } else {
            if (proceedToExit) {
                finish();
            } else {
                Toast.makeText(MenuActivity.this, getString(R.string.press_again_to_exit), Toast.LENGTH_LONG).show();
                proceedToExit = true;
                Observable.timer(2500, TimeUnit.MILLISECONDS)
                        .compose(bindToLifecycle())
                        .subscribe(ignored -> proceedToExit = false);
            }
        }
    }
}
