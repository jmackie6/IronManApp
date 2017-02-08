package cs246.ironmanapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Robbie on 7/1/2015.
 */
public class EntryFinisher implements TaskCompletion {
    private static final String TAG_ENTRY_FINISHER = "Entry Finisher";

    @Override
    public void finish(Activity activity, String json) {
        
            SharedPreferences sharePreferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
            SharedPreferences.Editor editor = sharePreferences.edit();
            editor.putString("entries", json);
            editor.commit();


    }
}
