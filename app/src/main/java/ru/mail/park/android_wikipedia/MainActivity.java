package ru.mail.park.android_wikipedia;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import dbservice.DbService;
import ru.mail.park.android_wikipedia.fragments.ArticleFragment;
import wikipedia.Article;

public class MainActivity extends FragmentActivity {
    private DbService dbService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbService = ((ApplicationModified) getApplication()).getDbService();

        new GetArticleAsyncTask().execute();
        new GetArticleAsyncTask().execute();
        new GetArticleAsyncTask().execute();
        new GetArticleAsyncTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
                i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.menu_history:
                i = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(i);
                return true;
        }
        //noinspection SimplifiableIfStatement
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
            transaction.add(R.id.articles_container, ArticleFragment.newInstance(article.getTitle()));
            transaction.commit();
//            ((TextView)findViewById(R.id.main_article_title)).setText(article.getTitle());
//
//            Button showArticle = (Button) findViewById(R.id.show_article);
//            showArticle.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(MainActivity.this, ArticleActivity.class);
//                    i.putExtra(ArticleActivity.ARTICLE_TITLE_TAG, article.getTitle());
//                    startActivity(i);
//                }
//            });
        }
    }
}
