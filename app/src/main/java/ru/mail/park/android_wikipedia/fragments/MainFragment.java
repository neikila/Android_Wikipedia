package ru.mail.park.android_wikipedia.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;


import java.util.ArrayList;
import java.util.List;

import ru.mail.park.android_wikipedia.ApplicationModified;
import ru.mail.park.android_wikipedia.ArticlesAdapter;
import ru.mail.park.android_wikipedia.R;
import ru.mail.park.android_wikipedia.ServiceHelper;
import utils.ResultArticle;
import wikipedia.Article;

public class MainFragment extends Fragment {
    private Handler handler;
    private RecyclerView recList;
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
//                    transaction.add(R.id.fragment_article, ArticleFragment.newInstance(message.getArticle().getTitle()));
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

    private List<Article> createList(int size) {

        List<Article> result = new ArrayList<Article>();
        for (int i=1; i <= size; i++) {
            Article art = new Article("Title"+i,"","");
            result.add(art);

        }

        return result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bus bus = ((ApplicationModified) getActivity().getApplication()).getBus();
        bus.register(this);

//        new ServiceHelper().getRandomArticle(this.getActivity());
        View myFragment = inflater.inflate(R.layout.fragment_main, container, false);
        recList = (RecyclerView) myFragment.findViewById(R.id.card_list);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        ArticlesAdapter articlesAdapter = new ArticlesAdapter(createList(2));
        recList.setAdapter(articlesAdapter);

        return myFragment;
    }

    @Override
    public void onDestroyView() {
       /* ViewGroup rootGroup = (ViewGroup) super.getView().getParent();
        rootGroup.removeView(recList);*/
        super.onDestroyView();
        Bus bus = ((ApplicationModified) getActivity().getApplication()).getBus();
        bus.unregister(this);
    }
}
