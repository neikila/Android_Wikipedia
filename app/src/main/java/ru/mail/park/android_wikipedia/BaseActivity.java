package ru.mail.park.android_wikipedia;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import dbservice.DbService;
import ru.mail.park.android_wikipedia.fragments.ArticleFragment;
import wikipedia.Article;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private DbService dbService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbService = ((ApplicationModified) getApplication()).getDbService();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.base_activity);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        new GetArticleAsyncTask().execute();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        this.closeOptionsMenu();
        int id = item.getItemId();
        Intent i;
        switch (id){
            case R.id.menu_settings:
                i = new Intent(BaseActivity.this, SettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.menu_history:
                i = new Intent(BaseActivity.this, HistoryActivity.class);
                startActivity(i);
                return true;
        }
        //noinspection SimplifiableIfStatement
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.base_activity);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class GetArticleAsyncTask extends AsyncTask<String, Void, Void> {
        private Article article;

        @Override
        protected Void doInBackground(String... params) {
            if (params.length == 0) {
                article = dbService.getRandomArticle();
            } else {
                article = dbService.getArticleByTitle(params[0]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, ArticleFragment.newInstance(article.getTitle()));
            transaction.commit();
        }
    }
}
