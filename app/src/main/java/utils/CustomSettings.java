package utils;

import android.content.Context;
import android.content.SharedPreferences;

import ru.mail.park.android_wikipedia.BaseActivity;
import ru.mail.park.android_wikipedia.fragments.SettingsFragment;

/**
 * Created by ivansemenov on 23.12.15.
 */
public class CustomSettings {
    private Boolean OFFLINE;
    public static final String APP_PREFERENCES_OFFLINE = "offline_checkbox";
    public SharedPreferences settings;

    public CustomSettings(Context context) {
        settings = context.getSharedPreferences(SettingsFragment.APP_PREFERENCES, Context.MODE_PRIVATE);
        OFFLINE = settings.getBoolean(APP_PREFERENCES_OFFLINE, false);
    }
    public Boolean getOfflineSettings(){
        return OFFLINE;
    }
    public void setOfflineSettings(Context context, Boolean value){
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(APP_PREFERENCES_OFFLINE, value);
        editor.commit();
    }
}
