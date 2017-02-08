package cs246.ironmanapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robbie on 7/1/2015.
 */

public class ContestantFinisher implements  TaskCompletion {
    private static final String TAG_CONTESTANT_FINISHER = "Contestant Finisher";
    @Override
    public void finish(Activity activity, String json) {

        ArrayList<Structs.Contestant> contestants = null;

        try {
            Log.i(TAG_CONTESTANT_FINISHER, "Contestant string to convert: " + json);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Structs.Contestant>>() {
            }.getType();
            contestants = gson.fromJson(json, listType);
            ListView contestantView;
            List<String> names = new ArrayList<String>();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("contestants", json);
            editor.commit();
            Log.v(TAG_CONTESTANT_FINISHER, "The output after conversion to a list: ");
        } catch (Exception e) {
            Log.e(TAG_CONTESTANT_FINISHER, "Error with gson or outputting or something", e);
        }

    }

}
