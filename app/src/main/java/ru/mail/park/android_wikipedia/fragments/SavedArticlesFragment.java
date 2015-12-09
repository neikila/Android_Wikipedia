package ru.mail.park.android_wikipedia.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dbservice.DbService;
import ru.mail.park.android_wikipedia.ApplicationModified;
import ru.mail.park.android_wikipedia.R;

public class SavedArticlesFragment extends Fragment {
    private DbService dbService;

    public static SavedArticlesFragment newInstance() {
        SavedArticlesFragment fragment = new SavedArticlesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SavedArticlesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        dbService = ((ApplicationModified)getActivity().getApplication()).getDbService();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        new GetSavedArticlesNameAsyncTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_articles, container, false);
    }

    private class ListItemTranslationAdapter extends ArrayAdapter<String> {
        public ListItemTranslationAdapter(List<String> objects) {
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

            viewHolder.articleName.setText(getItem(position));
            return convertView;
        }

        private class ViewHolder {
            public TextView articleName;
        }
    }

    private class GetSavedArticlesNameAsyncTask extends AsyncTask<String, Void, Void> {
        List <String> result;

        @Override
        protected Void doInBackground(String... params) {
            if (params.length > 0) {
                result = dbService.getSavedArticlesNames(Integer.parseInt(params[0]));
            } else {
                result = dbService.getSavedArticlesName();
            }
            if (result == null) {
                result = new ArrayList<>();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ListItemTranslationAdapter adapter = new ListItemTranslationAdapter(result);
            ListView list = (ListView) getActivity().findViewById(R.id.listWords);
            list.setAdapter(adapter);
        }
    }
}
