package cs246.ironmanapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Robbie on 7/6/2015.
 */
public class AdapterContestant extends ArrayAdapter<Structs.Contestant> {
//public class AdapterContestant extends BaseAdapter {
    private Activity activity;
    ArrayList<Structs.Contestant> arrayContestant;
    //private ArrayList<String> arrayContestant;
    private static LayoutInflater inflater = null;
    private int layoutResourceId;




    public AdapterContestant (Activity activity, int textViewResourceId, ArrayList<Structs.Contestant> arrayContestant) {
    //public AdapterContestant (Activity activity, int textViewResourceId, ArrayList<String> arrayContestant) {

        super(activity, textViewResourceId, arrayContestant);
        //super(activity, lContestant);
        //try {

        arrayContestant = new ArrayList<Structs.Contestant>();
        SharedPreferences conPreferences = PreferenceManager.getDefaultSharedPreferences(activity);

        String contJson = conPreferences.getString("contestants", "Nothing found");
        //Log.i("tag Rank", "the JSON is: " + json);

        Gson contGson = new Gson();
        Type listCont = new TypeToken<ArrayList<Structs.Contestant>>() {
        }.getType();
        //
        arrayContestant = contGson.fromJson(contJson, listCont);

            this.activity = activity;
            //contestants = arrayContestant;
            //this.arrayContestants = arrayContestants;
            layoutResourceId = textViewResourceId;


            //inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater = LayoutInflater.from(activity);


    }

    public int getCount() {
        return arrayContestant.size();
        //return contestants.size();
    }

    public Structs.Contestant getItem(int position) {
        return arrayContestant.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public String getU_name(int i) {
        return arrayContestant.get(i).u_name;
    }

    public double getPercentage(int i) {

        return arrayContestant.get(i).percentage;
    }


    public static class ViewHolder {
        TextView txtDisplay_name;
        TextView txtPercentage;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //View vi = convertView;
        final ViewHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.contestantdisplay, null);
                holder = new ViewHolder();

                //holder.txtDisplay_name = (TextView) convertView.findViewById(R.id.u_name);
                holder.txtPercentage = (TextView) convertView.findViewById(R.id.percentage);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.txtDisplay_name.setText(arrayContestant.get(position).u_name);
            holder.txtPercentage.setText(Double.toString(arrayContestant.get(position).percentage));

        return convertView;
    }
}