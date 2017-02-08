package cs246.ironmanapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by John on 7/8/15.
 * this class extends activty to display a new activty when needed in on create, this class will also display a list view with all the
 * contestants and percentage of each contestant
 */
public class Rank extends Activity {
    public final static String TAG_RANK = "Rank";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Context context = getApplicationContext();
        super.onCreate(savedInstanceState);
        ListView listView;
        ArrayList<Structs.Contestant> contestants = null;
        setContentView(R.layout.rankwindow);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width), (int) (height * 0.6));
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = preferences.getString("contestants", "Nothing found");
        Log.i(TAG_RANK, "the JSON is: " + json);
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Structs.Contestant>>() {
        }.getType();
        contestants = gson.fromJson(json, listType);
        ArrayList<String> userList = new ArrayList<String>();
        for (Structs.Contestant contestant : contestants) {
            String presicion = new String(contestant.u_name);
            //contestant.u_name.length();
            Double p = new Double(new DecimalFormat("#0.00").format(contestant.percentage));
            userList.add("contestant: " + presicion + "                                                                            " + "Percetage: " + p * 100 + "%");
        }

        listView = (ListView) findViewById(R.id.rankings);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, userList);

        listView.setAdapter(adapter);
    }
}
