package cs246.ironmanapp;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Robbie on 7/1/2015.
 *
 * This finisher takes the json message from submitting an entry and handles each case
 */
public class SubmitEntryFinisher implements TaskCompletion {
    public final static String TAG_SUBMIT_ENTRY_FINISHER = "Submit Entry Finisher";

    @Override
    public void finish(Activity activity, String json) {
        Structs.ReturnMessage submitEntryMessage = null;
        try {
            Log.v(TAG_SUBMIT_ENTRY_FINISHER, "Json in submit entry finisher: " + json);
            Gson gson = new Gson();
            submitEntryMessage = gson.fromJson(json, Structs.ReturnMessage.class);
            String output = "";
            output += submitEntryMessage.code + " " + submitEntryMessage.message;

            if(submitEntryMessage.code == 0){
                Log.v(TAG_SUBMIT_ENTRY_FINISHER, "all is well: " + output);
            }
            else{
                Log.v(TAG_SUBMIT_ENTRY_FINISHER, "There was an error: " + output);
            }

            Log.v(TAG_SUBMIT_ENTRY_FINISHER, output);
        } catch (Exception e) {
            Log.e(TAG_SUBMIT_ENTRY_FINISHER, "Error with gson or outputting or something", e);
        }

    }
}
