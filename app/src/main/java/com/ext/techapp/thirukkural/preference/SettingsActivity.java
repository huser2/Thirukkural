package com.ext.techapp.thirukkural.preference;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.ext.techapp.thirukkural.R;

public class SettingsActivity extends AppCompatActivity implements SettingsFragment.OnSettingsFragmentInteractionListener
         {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.title_activity_settings);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set default values
        // commented for bug fix:
       // PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        SettingsFragment settings = new SettingsFragment();
        getFragmentManager().beginTransaction().replace(R.id.action_settings_layout, settings).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                navigateUpTo(getIntent());
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSettingsFragmentInteraction(Uri uri) {

    }




}
