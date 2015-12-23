package ru.mail.park.android_wikipedia.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class HistoryFragment extends Fragment {
    private Handler handler;
    private RecyclerView recList;
    private static List<Article> articlesList;

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Subscribe
    public void react(final ResultArticle message) {
        if (handler != null) {
            if (message.getMessageType().equals(OttoMessage.MessageType.ResultArticle)) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ArticlesAdapter adapter = (ArticlesAdapter) recList.getAdapter();
                        articlesList = ((ResultArticle) message).getArticles();
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
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        handler = new Handler();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO пока тут ничего делать
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
            new ServiceHelper().getArticlesFromHistory(this.getActivity());
        } else {
            articlesAdapter = new ArticlesAdapter(listener, articlesList);
        }
        recList.setAdapter(articlesAdapter);

        return myFragment;
    }

    private class ListItemTranslationAdapter extends ArrayAdapter<Article> {
        public ListItemTranslationAdapter(List<Article> objects) {
            super(getActivity(), 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.articleName = (TextView) convertView.findViewById(R.id.article_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.articleName.setText(getItem(position).getTitle());
            return convertView;
        }

        private class ViewHolder {
            public TextView articleName;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Bus bus = ((ApplicationModified) getActivity().getApplication()).getBus();
        bus.unregister(this);
    }
}
