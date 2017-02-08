package cs246.ironmanapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Calendar;

/**
 * Created by John on 7/1/15
 * ADD AN ENTRY
 * Adding entries for progression
 */
public class AddEntry extends FragmentActivity {

    private static final String NEW_ENTRY_URL = "http://robbise.no-ip.info/ironman/newEntry.php";
    private static final String TAG_OUTPUT_ALL_THE_THINGS = "For Debugging";
    private static final String TAG_ADD_ENTRY = "Add Entry";
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    public MainActivity mainActivity;

    private static int year = 0;
    private static int month = 0;
    private static int day = 0;

    /**
     * On create defines the size of the window and sets some onclick listeners
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //THIS CODE IS TO RESET USER_ID FOR TESTING!!!!!
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.context);
        //SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.remove("user_id");
        //editor.commit();

        Intent intent = getIntent();

        mainActivity = (MainActivity) intent.getSerializableExtra("activity");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.addentry);

        DisplayMetrics dm = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width), (int) (height * 0.6));

        Log.v(TAG_ADD_ENTRY, "Assigning buton");
        Button submit = (Button) findViewById(R.id.button);

        submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.v(TAG_ADD_ENTRY, "doing shared preferences stuff");
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.context);
                Log.v(TAG_ADD_ENTRY, "about to get string");
                String user = sharedPreferences.getString("user_id", "");

                if (user.isEmpty()) {

                    // prompt user if they want to have a display name or not and call
                    Log.v(TAG_ADD_ENTRY, "user is empty");
                    Intent intent = new Intent(AddEntry.this, UserName.class);
                    Log.v(TAG_ADD_ENTRY, "intent created: " + intent.toString());
                    startActivity(intent);
                } else {
                    sendNewEntry(user);
                    Intent intent = new Intent(AddEntry.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * This function will take a unique identifier for the current user and then send it, along with
     * the mode, distance, and date from a form in the app that the user will fill out with this
     * information. It will return a message object that contains a code and a message. If everythign succeded
     * the code will be 0
     * <p/>
     * See newEntry.php in the Ironman web services google doc for other message possibilities
     *
     * @param uuid - this is a 13 digit unique identifier that we will send to the web service so
     *             it knows what user we are trying to insert for
     * @return - this will return a ReturnMessage object for insertID to handle
     * @see <a href="https://docs.google.com/document/d/1tAoB8SyYUl-wQ2T6NnoV7d-_Y1-sQZ6a9S2OFklqa7A/edit#bookmark=id.3azfvv3x9lua">NewEntry.php Documentation</a>
     */
    private void sendNewEntry(String uuid) {

        Log.v(TAG_ADD_ENTRY, "In send New entry! yay!!! uuid: " + uuid);

        // Gets a reference to our radio group
        // rBtnDigits is the name of our radio group (code not shown)
        RadioGroup g = (RadioGroup) findViewById(R.id.Modes);

        Log.i(TAG_ADD_ENTRY, "Radio group added");

        // Returns an integer which represents the selected radio button's ID
        int selected = g.getCheckedRadioButtonId();

        Log.i(TAG_ADD_ENTRY, "got selected");

        // Gets a reference to our "selected" radio button
        RadioButton b = (RadioButton) findViewById(selected);

        // Now you can get the text or whatever you want from the "selected" radio button
        // b.getText();
        String mode = b.getText().toString();
        Log.v(TAG_ADD_ENTRY, "from mode: " + b.getText().toString());

        EditText distanceEditText;
        distanceEditText = (EditText) findViewById(R.id.miles);
        String distance = distanceEditText.getText().toString();
        Log.i(TAG_ADD_ENTRY, "got distance: " + distance);
        month += 1;
        String monthS = Integer.toString(month);
        String yearS = Integer.toString(year);
        String dayS = Integer.toString(day);
        String zero = "0";

        if (month < 10) {

            monthS = zero + monthS;
        }
        if (day < 10) {

            dayS = zero + dayS;
        }
        String date = yearS + "-" + monthS + "-" + dayS;
        Log.i(TAG_ADD_ENTRY, "got date: " + date);
        String params = "mode=" + mode + "&user=" + uuid + "&date=" + date + "&distance=" + distance;
        Log.i(TAG_ADD_ENTRY, "params sending to new entry: " + params);
        MainActivity.Task t = new MainActivity.Task(NEW_ENTRY_URL, params); // two parameters here because it's a post http request
        t.setTaskCompletion(new SubmitEntryFinisher());
        new Thread(t).start();
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            AddEntry.year = year;
            AddEntry.month = month;
            AddEntry.day = day;
        }
    }
}