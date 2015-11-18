package ru.mail.park.android_wikipedia;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dbservice.DbService;

public class HistoryActivity extends AppCompatActivity {
    DbService dbService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbService = ((ApplicationModified)getApplication()).getDbService();
        setContentView(R.layout.activity_history);
        new GetArticlesNameFromHistoryAsyncTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ListItemTranslationAdapter extends ArrayAdapter<String> {
        public ListItemTranslationAdapter(List<String> objects) {
            super(HistoryActivity.this, 0, objects);
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

    private class GetArticlesNameFromHistoryAsyncTask extends AsyncTask<String, Void, Void> {
        List <String> result;

        @Override
        protected Void doInBackground(String... params) {
            if (params.length > 0) {
                result = dbService.getArticlesNameFromHistory(Integer.parseInt(params[0]));
            } else {
                result = dbService.getArticlesNameFromHistory();
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
            ListView list = (ListView) findViewById(R.id.listWords);
            list.setAdapter(adapter);
        }
    }
}
