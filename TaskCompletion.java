package cs246.ironmanapp;

import android.app.Activity;

/**
 * Created by Robbie on 7/1/2015.
 * Interface for the different http completion tasks
 */
public interface TaskCompletion {
    public void finish(Activity activity, String json);
}
