package ru.mail.park.android_wikipedia.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;


import dbservice.DbService;
import ru.mail.park.android_wikipedia.ApplicationModified;
import ru.mail.park.android_wikipedia.R;
import ru.mail.park.android_wikipedia.ServiceHelper;
import utils.ResultArticle;
import wikipedia.Article;

public class MainFragment extends Fragment {
    private Handler handler;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Subscribe
    public void react(final ResultArticle message) {
        if (handler != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.add(R.id.fragment_article, ArticleFragment.newInstance(message.getArticle().getTitle()));
                    transaction.commit();
                }
            });
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        handler = new Handler();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO пока пусто
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bus bus = ((ApplicationModified) getActivity().getApplication()).getBus();
        bus.register(this);

        new ServiceHelper().getRandomArticle(this.getActivity());
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Bus bus = ((ApplicationModified) getActivity().getApplication()).getBus();
        bus.unregister(this);
    }
}
