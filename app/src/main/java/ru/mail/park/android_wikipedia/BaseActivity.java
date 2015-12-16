package ru.mail.park.android_wikipedia;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ru.mail.park.android_wikipedia.fragments.ArticleFragment;
import ru.mail.park.android_wikipedia.fragments.HistoryFragment;
import ru.mail.park.android_wikipedia.fragments.MainFragment;
import ru.mail.park.android_wikipedia.fragments.SavedArticlesFragment;
import ru.mail.park.android_wikipedia.fragments.SettingsFragment;



public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.base_activity);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {
            setFragment(MainFragment.newInstance());
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.base_activity);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            setFragment(SettingsFragment.newInstance());
        } else if (id == R.id.nav_main) {
            setFragment(MainFragment.newInstance());
        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_history) {
            setFragment(HistoryFragment.newInstance());
        } else if (id == R.id.nav_saved_articles) {
            setFragment(SavedArticlesFragment.newInstance());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.base_activity);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String title = intent.getStringExtra(ArticleFragment.ARTICLE_TITLE_TAG);
        setFragment(ArticleFragment.newInstance(title));
    }
}
