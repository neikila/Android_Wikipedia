package ru.mail.park.android_wikipedia.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dbservice.DbService;
import ru.mail.park.android_wikipedia.ApplicationModified;
import ru.mail.park.android_wikipedia.ArticleActivity;
import ru.mail.park.android_wikipedia.R;
import wikipedia.Article;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {Use the {@link ArticleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticleFragment extends Fragment {

    private DbService dbService;
    private Article article;

    public static ArticleFragment newInstance(String title) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putString(ArticleActivity.ARTICLE_TITLE_TAG, title);
        fragment.setArguments(args);
        return fragment;
    }

    public ArticleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbService = ((ApplicationModified) getActivity().getApplication()).getDbService();
        new GetArticleAsyncTask().execute(getArguments().getString(ArticleActivity.ARTICLE_TITLE_TAG));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    private void updateFragment(final Article article) {
        ((TextView) getView().findViewById(R.id.article_title)).setText(article.getTitle());
        getView().findViewById(R.id.show_article).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ArticleFragment.this.getActivity(), ArticleActivity.class);
                i.putExtra(ArticleActivity.ARTICLE_TITLE_TAG, article.getTitle());
                startActivity(i);
            }
        });
    }

    private class GetArticleAsyncTask extends AsyncTask<String, Void, Void> {
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
            updateFragment(article);
        }
    }
}
