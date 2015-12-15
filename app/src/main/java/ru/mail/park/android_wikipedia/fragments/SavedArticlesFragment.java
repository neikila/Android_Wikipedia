package ru.mail.park.android_wikipedia.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import dbservice.DbService;
import ru.mail.park.android_wikipedia.ApplicationModified;
import ru.mail.park.android_wikipedia.R;
import ru.mail.park.android_wikipedia.ServiceHelper;
import utils.ResultArticle;
import wikipedia.Article;

public class SavedArticlesFragment extends Fragment {
    private Handler handler;

    public static SavedArticlesFragment newInstance() {
        SavedArticlesFragment fragment = new SavedArticlesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SavedArticlesFragment() {
        // Required empty public constructor
    }

    @Subscribe
    public void react(final ResultArticle message) {
        if (handler != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ListItemTranslationAdapter adapter = new ListItemTranslationAdapter(message.getArticles());
                    ListView list = (ListView) getActivity().findViewById(R.id.listWords);
                    list.setAdapter(adapter);
                }
            });
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bus bus = ((ApplicationModified) getActivity().getApplication()).getBus();
        bus.register(this);
        // Inflate the layout for this fragment
        new ServiceHelper().getSavedArticles(getActivity());
        return inflater.inflate(R.layout.fragment_saved_articles, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Bus bus = ((ApplicationModified) getActivity().getApplication()).getBus();
        bus.unregister(this);
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
}
