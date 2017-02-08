package cs246.ironmanapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements Serializable {
    private static final String GET_CONTESTANTS_URL = "http://robbise.no-ip.info/ironman/getContestants.php?";
    private static final String GET_ENTRIES_URL = "http://robbise.no-ip.info/ironman/getEntries.php?";
    private static final String GET_PROGRESS_URL = "http://robbise.no-ip.info/ironman/getProgress.php?";
    private static android.os.Handler handler;
    public ListView lView;
    public static Context context;
    public static Activity activity;
    public ArrayAdapter<String> adapter;
    private final String USER_AGENT = "Mozilla/5.0";
    private static final String TAG_MAIN_ACTIVITY = "Main Activity";
    private static final String TAG_OUTPUT_ALL_THE_THINGS = "For Debugging";
    private static String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MainActivity.context = MainActivity.this.getApplicationContext();

        activity = this;

        Log.v(TAG_OUTPUT_ALL_THE_THINGS, "We have started!");
        super.onCreate(savedInstanceState);
        Log.v(TAG_OUTPUT_ALL_THE_THINGS, "setContentView!");
        setContentView(R.layout.activity_main);

        Log.v(TAG_OUTPUT_ALL_THE_THINGS, "Creating Handler");
        handler = new android.os.Handler();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Log.v(TAG_OUTPUT_ALL_THE_THINGS, "Shared preference getting user");
        user = sharedPreferences.getString("user_id", "");

        ProgressBar progress1 = (ProgressBar) findViewById(R.id.progressBar);

        progress1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EntryHistory.class);
                intent.putExtra("activity", MainActivity.this);
                startActivity(intent);
            }
        });

        progress1 = (ProgressBar) findViewById(R.id.progressBar2);

        progress1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EntryHistory.class);
                intent.putExtra("activity", MainActivity.this);
                startActivity(intent);
            }
        });

        progress1 = (ProgressBar) findViewById(R.id.progressBar3);

        progress1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, EntryHistory.class);
                intent.putExtra("activity", MainActivity.this);
                startActivity(intent);
            }
        });

        ImageView add = (ImageView) findViewById(R.id.imageView3);
        ImageView rank = (ImageView) findViewById(R.id.imageView5);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AddEntry.class);
                intent.putExtra("activity", MainActivity.this);
                startActivity(intent);

            }
        });

        rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, Rank.class));
            }
        });

        handler = new android.os.Handler();
        Log.v(TAG_OUTPUT_ALL_THE_THINGS, "About to get contestants!");
        getContestants();

        Log.v(TAG_OUTPUT_ALL_THE_THINGS, "About to get entries!");
        getEntries();

        Log.v(TAG_OUTPUT_ALL_THE_THINGS, "About to get progress!");
        getProgress();
    }


    public static Context getContext() {
        return context;
    }

    /**
     * getEntries
     * <p/>
     * Handles the task object to take care of getting entries
     */
    public void getContestants() {
        Task t = new Task(GET_CONTESTANTS_URL + "semester=" + getSelectedSemester());

        t.setTaskCompletion(new ContestantFinisher());

        new Thread(t).start();
    }

    /**
     * getProgress
     * Handles the taks object for calls to the progress php page
     * Gets instances of the progress bars and sets them according to the values returned by the php
     * page
     */
    public void getProgress() {
        ProgressBar gprogress = (ProgressBar) this.findViewById(R.id.progressBar2);
        ProgressBar bprogress = (ProgressBar) this.findViewById(R.id.progressBar);
        ProgressBar pprogress = (ProgressBar) this.findViewById(R.id.progressBar3);
        Task t = new Task(GET_PROGRESS_URL + "semester=" + getSelectedSemester() + "&id=" + getContestantID());

        t.setTaskCompletion(new ProgressFinisher());

        Thread progressT = new Thread(t);
        progressT.start();

        try {
            Log.v(TAG_OUTPUT_ALL_THE_THINGS, "setting up shared prefs progress");
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String progress = sharedPreferences.getString("progress", "{}");
            Log.v(TAG_OUTPUT_ALL_THE_THINGS, "FROM SHARED PREFS IN PROGRESS: " + progress);
            ArrayList<Structs.Total> progressArray = null;

            double bikeTotal = 0;
            double swimTotal = 0;
            double runTotal = 0;

            if (progress != "{}") {
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Structs.Total>>() {
                }.getType();

                progressArray = gson.fromJson(progress, listType);

                progressT.join();

                for (Structs.Total total : progressArray) {
                    switch (total.mode) {
                        case "Bike":
                            bikeTotal = total.distance;
                            Log.i(TAG_MAIN_ACTIVITY, "This is bike total: " + bikeTotal);
                            double bikeP = (bikeTotal / 112) * 100;
                            int b = (int) bikeP;
                            gprogress.setProgress(b);

                            //update bike;
                            break;
                        case "Swim":
                            swimTotal = total.distance;
                            Log.i(TAG_MAIN_ACTIVITY, "This is swim total: " + swimTotal);
                            double swimP = (swimTotal / 85) * 100;
                            int s = (int) swimP;
                            bprogress.setProgress(s);

                            //update swim;
                            break;
                        case "Run":
                            runTotal = total.distance;
                            Log.i(TAG_MAIN_ACTIVITY, "This is run total: " + runTotal);
                            double runP = (runTotal / 26.2) * 100;
                            int r = (int) runP;
                            pprogress.setProgress(r);

                            //update run
                            break;
                        default:
                            Log.wtf(TAG_MAIN_ACTIVITY, "what mode is this??? " + total.mode);
                    }
                }
            }

            Log.i(TAG_MAIN_ACTIVITY, "TOTALS: Swim: " + swimTotal + " bike: " + bikeTotal + " run: " + runTotal);

            double generalPercent = ((swimTotal + bikeTotal + runTotal) / 223) * 100;

            TextView total = (TextView) findViewById(R.id.completion);

            Double percent = new Double(new DecimalFormat("#0").format(generalPercent));

            String percentage =  Double.toString(percent) + "%";

            total.setText(percentage);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * getEntries
     * <p/>
     * Handles the task object to take care of getting entries
     */
    public void getEntries() {
        Task t = new Task(GET_ENTRIES_URL + "semester=" + getSelectedSemester() + "&id=" + getContestantID());
        t.setTaskCompletion(new EntryFinisher());
        new Thread(t).start();
    }

    public static class Task implements Runnable {
        public String json;
        private String url;
        private String params;
        private boolean isPost;
        private TaskCompletion taskCompletion;

        public void setTaskCompletion(TaskCompletion t) {
            taskCompletion = t;
        }

        /**
         * Constructor for a GET request takes only a url ex. "http://robbise.no-ip.info/ironman/getContestants.php?semester=FALL2015"
         *
         * @param url
         */
        public Task(String url) {
            this.url = url;
            this.params = "";
            isPost = false;
        }

        /**
         * Constructor for a POST request takes a url string and parameters
         * ex. url = "http://robbise.no-ip.info/ironman/newUser.php"
         * params = username=batman
         *
         * @param url
         * @param params
         */
        public Task(String url, String params) {
            Log.v(TAG_MAIN_ACTIVITY, "making a post request");
            this.url = url;
            this.params = params;
            if (params.isEmpty()) {
                Log.v(TAG_MAIN_ACTIVITY, "params is empty! it's not really a post request");
                isPost = false;
            } else
                Log.v(TAG_MAIN_ACTIVITY, "params not empty! it really is a post request");
            isPost = true;
        }

        /**
         * This is the overridden run method for Task which is a runnable class. It creates a
         * new urlReader with the url that was passed in and then sends a get request and assigns the
         * returned data to a static json object.
         */

        @Override
        public void run() {
            try {
                Log.i(TAG_MAIN_ACTIVITY, url);
                if (isPost == true) {
                    Log.v(TAG_MAIN_ACTIVITY, "sending a post request");
                    json = URLReader.sendPost(url, params);
                } else {
                    Log.v(TAG_MAIN_ACTIVITY, "sending a get request");
                    json = URLReader.sendGet(url);
                }
            } catch (Exception e) {
                Log.e(TAG_MAIN_ACTIVITY, "Error trying to send a request request with: ", e);
                e.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    taskCompletion.finish(activity, json);

                }
            });
        }
    }

    /**
     * this method will need to get the semester from a select
     * drop down menu on activity_main.xml and return it.
     *
     * @return a string that is the selected semester
     */
    private String getSelectedSemester() {

        return "FALL2015"; // a default value for testing
    }

    /**
     * this method will need to get the ID from a locally stored ID number
     * and return it.
     *
     * @return a string that is the ID of the current user
     */
    private String getContestantID() {


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        user = sharedPreferences.getString("user_id", "");
        Log.v(TAG_OUTPUT_ALL_THE_THINGS, " user from shared preffs CONTESTANDID: " + user);

        return user;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
