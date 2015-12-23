package ru.mail.park.android_wikipedia.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.File;

import ru.mail.park.android_wikipedia.ApplicationModified;
import ru.mail.park.android_wikipedia.R;
import ru.mail.park.android_wikipedia.ServiceHelper;
import utils.OttoMessage;
import utils.ResultArticle;
import wikipedia.Article;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {Use the {@link ArticleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticleFragment extends Fragment {
    public  String webArchivPath;
    private WebView mWebView;
    public final static String ARTICLE_TITLE_TAG = "ARTICLE_TITLE";

    private FloatingActionButton tocButton;
    private static final int TOC_BUTTON_HIDE_DELAY = 2000;
    private Article article;
    private Handler handler;
    private String title;
    private String newTitle; // для перехода по ссылкам

    public static ArticleFragment newInstance(String title) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putString(ARTICLE_TITLE_TAG, title);
        fragment.setArguments(args);
        return fragment;
    }

    public ArticleFragment() {
        // Required empty public constructor
    }

    @Subscribe
    public void react(final OttoMessage message) {
        if (handler != null) {
            if (message.getMessageType().equals(OttoMessage.MessageType.ResultArticle)) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        article = ((ResultArticle)message).getArticle();
                        ArticleFragment.this.setArticle();
                    }
                });
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        title = getArguments().getString(ARTICLE_TITLE_TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bus bus = ((ApplicationModified) getActivity().getApplication()).getBus();
        bus.register(this);

        View rootView = inflater.inflate(R.layout.fragment_article, container, false);
        new ServiceHelper().getArticle(getActivity(), title);
        setArticle(rootView);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
//        toolbar.setVisibility(View.GONE);
//        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.base_activity);

//        toolbar.hideOverflowMenu();
//        toolbar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Bus bus = ((ApplicationModified) getActivity().getApplication()).getBus();
        bus.unregister(this);
    }

    private void setArticle(View view) {
//        ((ImageView)view.findViewById(R.id.main_article_image)).setImageBitmap(article.getLogoBitmap());
//        ((TextView)view.findViewById(R.id.article_title)).setText(article.getTitle());
//        ((TextView) view.findViewById(R.id.article_body)).setText("Body: " + article.getBody());
        mWebView = (WebView) view.findViewById(R.id.webView);
        mWebView.setWebViewClient(new MyWebViewClient());
        // включаем поддержку JavaScript
        mWebView.getSettings().setJavaScriptEnabled(true);

        // указываем страницу загрузки
        //заглушка
        //mWebView.loadUrl("https://en.m.wikipedia.org/wiki/" + "Pi");
        mWebView.loadUrl("https://ru.m.wikipedia.org/wiki/" + title);
    }

    private void setArticle() {
        View view = ArticleFragment.this.getView();
        setArticle(view);
    }



    private class MyWebViewClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            //view.loadUrl("javascript: (function(){document.getElementById(\"section_0\").innerHTML = \"yourTextHere\"; })()");
            view.loadUrl("javascript: (function(){document.getElementById(\"mw-mf-main-menu-button\").remove();" +
                    "document.getElementById(\"searchInput\").remove();" +
                    "document.getElementById(\"ca-edit\").remove();" +
                    "document.getElementById(\"ca-watch\").remove();" +
                    "document.getElementById(\"siteNotice\").remove();" +
                    " })()");


            saveArticleInMHT();

            //для загрузки сохраненной страницы
            //mWebView.loadUrl("file://"+path);

        }

    }
    // глюки из-зи захардкодиных статей, не понимаю почему 2 раза сохраняет - видимо из-зи асинхронности
    // если будет кнопка, то этот глюк исчезнет, т.к. к моменту сохранения вся статья уже будет прогруженна
    //TODO: додумать механизм сохранения, скорее всего забиндить на кнопку и проверять есть ли статья в базе
    public void saveArticleInMHT() {
        newTitle = title;
        String domen = "https://ru.m.wikipedia.org/wiki/";
        try {
            String AllUrl = mWebView.getUrl();
            newTitle = AllUrl.substring(domen.length(), AllUrl.length());
            webArchivPath = getActivity().getFilesDir().getAbsolutePath() + File.separator + newTitle + ".mht";
        } catch (NullPointerException e) {
            try {
                webArchivPath = getActivity().getFilesDir().getAbsolutePath() + File.separator + title + ".mht";
            } catch (NullPointerException e1) {
                showToast("Не так быстро!Я не успел сохранить статью!");
            }
        }

        mWebView.saveWebArchive(webArchivPath);
        showToast("Save in file://" + webArchivPath);

    }

    public void showToast(String path) {
        //создаем и отображаем текстовое уведомление
        try {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                    path,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } catch (NullPointerException e) {
            Log.d("hardcodeArticles", "Не так быстро!Я не успел сохранить статью!");
        }

    }


    private Runnable hideToCButtonRunnable = new Runnable() {
        @Override
        public void run() {
            tocButton.hide();
        }
    };

}
