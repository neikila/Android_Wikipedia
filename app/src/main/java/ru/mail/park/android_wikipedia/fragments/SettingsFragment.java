package ru.mail.park.android_wikipedia.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import ru.mail.park.android_wikipedia.ApplicationModified;
import ru.mail.park.android_wikipedia.ArticlesAdapter;
import ru.mail.park.android_wikipedia.R;
import ru.mail.park.android_wikipedia.ServiceHelper;
import utils.CleanSuccess;
import utils.CustomSettings;
import utils.ResultArticle;

public class SettingsFragment extends Fragment {
    private Handler handler;
    public static final String APP_PREFERENCES = "settings";

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Subscribe
    public void react(final CleanSuccess message) {
        if (handler != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "База данных очищена", Toast.LENGTH_LONG).show();
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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        CustomSettings settings = new CustomSettings(getActivity().getApplication());
        Boolean offline = settings.getOfflineSettings();
        CheckBox offlineCheckbox = (CheckBox) view.findViewById(R.id.setting_offline);
        offlineCheckbox.setChecked(offline);
        // TODO спросить где лучше вызывать АсинкТаск. (гарантировано ли, что когда я его вызову вьюшка уже будет создана)
        Button cleanButton = (Button) view.findViewById(R.id.clean_button);
        cleanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
                new ServiceHelper().cleanDB(getActivity());
            }
        });
        CheckBox offline_settings = (CheckBox) view.findViewById(R.id.setting_offline);
        offline_settings.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CustomSettings settings = new CustomSettings(getActivity().getApplication());
                if (settings.setOfflineSettings(getActivity().getApplication(), isChecked)) {
                    refresh();
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Bus bus = ((ApplicationModified) getActivity().getApplication()).getBus();
        bus.unregister(this);
    }

    private void refresh() {
        MainFragment.refresh();
        SavedArticlesFragment.refresh();
    }
}
