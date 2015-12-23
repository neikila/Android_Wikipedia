package ru.mail.park.android_wikipedia.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import ru.mail.park.android_wikipedia.ApplicationModified;
import ru.mail.park.android_wikipedia.ArticlesAdapter;
import ru.mail.park.android_wikipedia.BaseActivity;
import ru.mail.park.android_wikipedia.R;
import ru.mail.park.android_wikipedia.ServiceHelper;
import utils.OttoMessage;
import utils.ResultArticle;
import wikipedia.Article;

public class MainFragment extends Fragment {
    private Handler handler;
    private RecyclerView recList;
    private static List<Article> articlesList;

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
    public void react(final OttoMessage message) {
        if (handler != null) {
            if (message.getMessageType().equals(OttoMessage.MessageType.ResultArticle)) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ArticlesAdapter adapter = (ArticlesAdapter) recList.getAdapter();
                        articlesList = ((ResultArticle) message).getArticles();
                        TextView no_Results = (TextView) getActivity().findViewById(R.id.NoResultMessage);
                        if(articlesList.size() != 0) {
                            no_Results.setVisibility(View.GONE);
                        } else {
                            no_Results.setVisibility(View.VISIBLE);
                        }
                        adapter.setArticles(articlesList);
                        adapter.notifyDataSetChanged();
                    }
                });
            } else if (message.getMessageType().equals(OttoMessage.MessageType.UpdateAdapter)) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ArticlesAdapter adapter = (ArticlesAdapter) recList.getAdapter();
                        adapter.notifyDataSetChanged();
                    }
                });
            } else if (message.getMessageType().equals(OttoMessage.MessageType.NoResult)) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "No article found", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (message.getMessageType().equals(OttoMessage.MessageType.NoInternet)) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "No internet", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        handler = new Handler();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO пока пусто
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getActivity(), "Still not implemented\n" + "Query: " + query, Toast.LENGTH_SHORT).show();
                new ServiceHelper().findArticles(getActivity(), query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        Bus bus = ((ApplicationModified) getActivity().getApplication()).getBus();
        bus.register(this);

        View myFragment = inflater.inflate(R.layout.fragment_main, container, false);
        recList = (RecyclerView) myFragment.findViewById(R.id.card_list);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = ((TextView) v.findViewById(R.id.title)).getText().toString();
                ((BaseActivity) getActivity()).openArticle(title);
            }
        };
        ArticlesAdapter articlesAdapter;
        if (articlesList == null) {
            articlesAdapter = new ArticlesAdapter(listener);
            new ServiceHelper().getSavedArticles(this.getActivity());
        } else {
            articlesAdapter = new ArticlesAdapter(listener, articlesList);
        }
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

    public static void refresh() {
        articlesList = null;
    }
}
