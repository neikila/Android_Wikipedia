package ru.mail.park.android_wikipedia;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class PreloaderActivity extends AppCompatActivity {
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preloader);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
            }
        };

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(PreloaderActivity.this, BaseActivity.class);
                startActivity(i);
                finish();
            }
        }, 10000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ContextCompat.checkSelfPermission(PreloaderActivity.this,
                        Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(getApplication(), "Permision denied", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(PreloaderActivity.this,
                            new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS},
                            1);
                } else {
                    Toast.makeText(getApplication(), "Permision granted", Toast.LENGTH_LONG).show();
                }
            }
        }, 500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preloader, menu);
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
}
