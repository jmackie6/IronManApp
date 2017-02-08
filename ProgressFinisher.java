package cs246.ironmanapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Robbie on 7/18/2015.
 * This finisher stores the json returned from the progress task
 */
public class ProgressFinisher implements TaskCompletion {
    private static final String TAG_PROGRESS_FINISHER = "Progress Finisher";
    @Override
    public void finish(Activity activity, String json) {
        Log.v(TAG_PROGRESS_FINISHER, "The Json in progress finisher: " + json);
        try {
            SharedPreferences sharePreferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
            SharedPreferences.Editor editor = sharePreferences.edit();
            editor.putString("progress", json);
            editor.commit();
            String output = json;
            Log.v(TAG_PROGRESS_FINISHER, output);
        } catch (Exception e) {
            Log.e(TAG_PROGRESS_FINISHER, "Error with gson or outputting or something", e);

        }
    }
}
