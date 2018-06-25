package com.example.kennethallan.testbuildofsqllightdatabase_01;

import android.content.Context;
import android.icu.text.StringSearch;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddEvent extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    DBHelper Mydb;
    Button TestButton;
    Button saveButton;
    EditText eventName;
    EditText eventDescription;
    ListView eventsListView;
    EditText et_eventInput_Hours;
    EditText et_eventInput_Minutes;
    ArrayList<String> arrayList_GlobalSliderValues = new ArrayList<String>(); // arraylist for the values extracted from the sliders
    int numAdapterValues;
    int event_InputTime = 0; // to initalise the values
    ArrayList<String> arrayList_EventsValues = new ArrayList<String>(); // arraylist for the factored values  for goal after being combined with the time input. to be saved in the DB database.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        saveButton = (Button)findViewById(R.id.saveButton);
        eventName = (EditText) findViewById(R.id.editText);
        eventDescription = (EditText)findViewById(R.id.editText2);
        eventsListView = (ListView)findViewById(R.id.ListViewSetActivityInputs);
        TestButton = (Button) findViewById(R.id.TestButton);
        et_eventInput_Hours = (EditText) findViewById(R.id.et_inputFreeTime_Hours);
        et_eventInput_Minutes = (EditText) findViewById(R.id.et_inputFreeTime_Minutes);

        Mydb = new DBHelper(this);

        addEvent ();
        TEST();

        // Populate arrayAdaptor
        ArrayList<String> themeValues = Mydb.getCURRENTThemeNames();

        ListAdapter themeListAdapter = new AddEvent.CustomAdaptor_InputNumbers(this,themeValues);
        eventsListView.setAdapter(themeListAdapter);
    }

    public void addEvent (){

        saveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        event_InputTime = getEventTime(); // passing to the global variable.
                        calculateGoals(arrayList_GlobalSliderValues,event_InputTime);
                        String temp_eventName = eventName.getText().toString();
                        String temp_eventDescription = eventDescription.getText().toString();

                        boolean tempresult = Mydb.insertActivity(temp_eventName,temp_eventDescription,arrayList_EventsValues);
                        if (tempresult){
                            Toast.makeText(AddEvent.this, "Succeeded To Input Goals", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(AddEvent.this, "Failed To Input Goals", Toast.LENGTH_SHORT).show();
                        }

//                        boolean isInserted = Mydb.insertTheme(eventName.getText().toString(),eventDescription.getText().toString());
//                        if (isInserted == true)
//                            Toast.makeText(AddEvent.this, "Data is inserted", Toast.LENGTH_SHORT).show();
//                        else
//                            Toast.makeText(AddEvent.this, "Data is NOT inserted", Toast.LENGTH_SHORT).show();

                    }
                }
        );
    }

    public int getEventTime(){
        int  goal_InputTime_Hours = Integer.parseInt(et_eventInput_Hours.getText().toString());
        int  goal_InputTime_Minutes = Integer.parseInt(et_eventInput_Minutes.getText().toString());
        double a = 60.0;
        int sum = goal_InputTime_Hours*60 + goal_InputTime_Minutes;
        return sum;
    }

    public void calculateGoals(ArrayList<String> valuesFromSliders,Integer freeTime ) {
        //sum values
        Integer sumNew = 0;
        Integer sumOld = 0;

        for (int i = 0; i < valuesFromSliders.size(); i++) {
            sumNew = Integer.parseInt(valuesFromSliders.get(i));
            sumOld = sumOld + sumNew;
        }

        double max = (double) freeTime;

        double ratio = max / (double) sumOld;
        arrayList_EventsValues.clear(); // clear slider to ensure no mixing with old values.

        for (int i = 0; i < valuesFromSliders.size(); i++) {
            int factoredGoalTime = (int) Math.round(Integer.parseInt(valuesFromSliders.get(i)) * ratio);
            arrayList_EventsValues.add(Integer.toString(factoredGoalTime));
        }

    }

    public void TEST(){

        TestButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> a = Mydb.getCURRENTThemeIDs();
                        if (a.size()>0)
                            Toast.makeText(AddEvent.this, a.get(0).toString(), Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(AddEvent.this, "ArrayList Empty", Toast.LENGTH_SHORT).show();

                    }
                }
        );
    }


    class CustomAdaptor_InputNumbers extends ArrayAdapter<String> {

        ArrayList<String> arrayList_Values = new ArrayList<>();
        int progressValue;
        int recordFirstCounter = 0;

        public CustomAdaptor_InputNumbers(Context context, ArrayList<String> resource) {
            super(context, R.layout.input_slider_01, resource);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater Inflater = LayoutInflater.from(getContext());
            View customView = Inflater.inflate(R.layout.input_slider_01, parent, false);

            String singleViewItem = getItem(position);
            TextView title = (TextView) customView.findViewById(R.id.textView5);
            final SeekBar seekbar = (SeekBar) customView.findViewById(R.id.seekBar2);

            //set values to view
            title.setText(singleViewItem);
            seekbar.setProgress(0); // CONCLUDED: this is not the setting that resets the seekbars when they are destroyed by the listview.

            numAdapterValues = eventsListView.getAdapter().getCount(); // returns total number of childen// seems to need to be in the adapter to work properly????

            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    if (recordFirstCounter == 0){
                        startBuild();
                        compileValues();
                        recordFirstCounter=1;
                    }else{
                        compileValues();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar bar) {

                }
            });

            return customView;
        }

        // Method used to build the array of values such that we can change values rather than add new values (appending a list)
        // thus avoiding a situation where the value arraylist could be larger than the number of values we have.
        public void startBuild(){

            for (int i=0;i<numAdapterValues;i++){

                arrayList_Values.add("0");

            }

        }

        // this method compiles the values in the arrayList_Values method. It needs an arrayList of size equal to the
        // number of slider to work since it alters values not adds values (appends to list).

        public void compileValues() {

            for (int i = 0; i < numAdapterValues; i++) {
                try {
                    SeekBar seekbar = (SeekBar) eventsListView.getChildAt(i).findViewById(R.id.seekBar2);
                    String value = Integer.toString(seekbar.getProgress());
                    arrayList_Values.set(i, value);
                } catch (Exception e) {

                }

            }

            arrayList_GlobalSliderValues = arrayList_Values;
        }
    }
}
