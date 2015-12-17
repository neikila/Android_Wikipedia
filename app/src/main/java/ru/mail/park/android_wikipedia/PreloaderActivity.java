package ru.mail.park.android_wikipedia;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import utils.BitmapReady;
import utils.ResultArticle;

public class PreloaderActivity extends AppCompatActivity {
    Handler handler;

    @Subscribe
    public void react(final BitmapReady message) {
        if (handler != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ArticlesAdapter.setDefaultBitmap(message.getBitmap());
                    Intent i = new Intent(PreloaderActivity.this, BaseActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preloader);

        handler = new Handler();
        new ServiceHelper().prepareTestData(this);
        new ServiceHelper().getDefaultBitmap(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bus bus = ((ApplicationModified) getApplication()).getBus();
        bus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Bus bus = ((ApplicationModified) getApplication()).getBus();
        bus.unregister(this);
    }
}
