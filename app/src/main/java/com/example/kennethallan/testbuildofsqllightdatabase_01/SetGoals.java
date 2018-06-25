package com.example.kennethallan.testbuildofsqllightdatabase_01;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;


import java.util.ArrayList;

public class SetGoals extends AppCompatActivity {

    DBHelper Mydb;
    ListView setGoalsListView;
    Button TestButton;
    CustomAdaptor_InputSliders CA;
    Beans testBean = new Beans(1);
    EditText et_goalsInput_Minutes;
    EditText et_goalsInput_Hours;


    ArrayList<String> arrayList_GlobalValues = new ArrayList<String>(); // arraylist for the values extracted from the sliders
    int numAdapterValues;
    int goal_InputTime = 0; // to initalise the values
    ArrayList<String> arrayList_GoalsValues = new ArrayList<String>(); // arraylist for the factored values  for goal after being combined with the time input. to be saved in the DB database.

    //TODO figure out if the database is based on current themes or all themes and alter the golasValues arraylist as required to input into SqliteDB.




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goals);

        Mydb = new DBHelper(this);
        setGoalsListView = (ListView)findViewById(R.id.ListViewSetGoals);
        TestButton = (Button) findViewById(R.id.TestButton_SetGoals);
        et_goalsInput_Hours = (EditText) findViewById(R.id.et_inputFreeTime_Hours);
        et_goalsInput_Minutes = (EditText) findViewById(R.id.et_inputFreeTime_Minutes);

        TEST();


        // Populate arrayAdaptor

        //String[] testString = {"Test1", "Test2", "Test3"}; Currently not using.
        ArrayList<String> themeValues = Mydb.getCURRENTThemeNames();

        ListAdapter themeListAdapter = new CustomAdaptor_InputSliders(this,themeValues);
        setGoalsListView.setAdapter(themeListAdapter);

    }


    public void TEST(){

        TestButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goal_InputTime = getFreeTime(); // passing to the global variable.
                        //Toast.makeText(SetGoals.this, Integer.toString(goal_InputTime), Toast.LENGTH_SHORT).show();
                        calculateGoals(arrayList_GlobalValues,goal_InputTime);
                        String manualSet = "Y"; // Strring to distinguish this input as manual input
                        boolean tempresult = Mydb.insertGoal(arrayList_GoalsValues,manualSet);
                        if (tempresult){
                            Toast.makeText(SetGoals.this, "Succeeded To Input Goals", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(SetGoals.this, "Failed To Input Goals", Toast.LENGTH_SHORT).show();

                        }

                   }
                }
        );
    }
// get the free time from the edit texts and convert them to one figure in minutes

    public int getFreeTime(){
        int  goal_InputTime_Hours = Integer.parseInt(et_goalsInput_Hours.getText().toString());
        int  goal_InputTime_Minutes = Integer.parseInt(et_goalsInput_Minutes.getText().toString());
        double a = 60.0;
        int temp = goal_InputTime_Hours*60 + goal_InputTime_Minutes;
        return temp;
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
        arrayList_GoalsValues.clear();

        for (int i = 0; i < valuesFromSliders.size(); i++) {
            int factoredGoalTime = (int) Math.round(Integer.parseInt(valuesFromSliders.get(i)) * ratio);
            arrayList_GoalsValues.add(Integer.toString(factoredGoalTime));
        }

    }




    class CustomAdaptor_InputSliders extends ArrayAdapter<String> {


        SetGoals SG;
        ArrayList<String> arrayList_Values = new ArrayList<>();



        int progressValue;
        int recordFirstCounter = 0;

        public CustomAdaptor_InputSliders(Context context, ArrayList<String>  resource) {
            super(context, R.layout.input_slider_01, resource);
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater Inflater = LayoutInflater.from(getContext());
            View customView = Inflater.inflate(R.layout.input_slider_01, parent, false);
            // 1) refence to data
            // 2) reference to text view
            // 3) Reference to seekbar.

            String singleViewItem = getItem(position);
            TextView title = (TextView) customView.findViewById(R.id.textView5);
            final SeekBar seekbar = (SeekBar) customView.findViewById(R.id.seekBar2);

            //set values to view
            title.setText(singleViewItem);
            seekbar.setProgress(0);

            numAdapterValues = setGoalsListView.getAdapter().getCount(); // returns total number of childen// seems to need to be in the adapter to work properly????


            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                int progressValue;

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



        public void startBuild(){

            for (int i=0;i<numAdapterValues;i++){

                arrayList_Values.add("0");

            }

        }

        public void compileValues(){

            //int numValues = setGoalsListView.getAdapter().getCount(); // returns total number of childen
            //int numValues2 = setGoalsListView.getChildCount(); // returns number of children made (visible) right now.

            for (int i=0; i < numAdapterValues;i++ ){
                try{
                    SeekBar seekbar = (SeekBar) setGoalsListView.getChildAt(i).findViewById(R.id.seekBar2);
                    String value = Integer.toString(seekbar.getProgress());
                    arrayList_Values.set(i,value);
                }catch (Exception e) {

                }

            }

            arrayList_GlobalValues = arrayList_Values;

            //makeGlobal(arrayList_Values);

            //SeekBar tempView2 = (SeekBar) setGoalsListView.getChildAt(0).findViewById(R.id.seekBar2); // this one gets children ie the altered values not the original views when loaded ie progress = 0
            // limitation is that can only get visible values so must use in way to catch exception.
            //SeekBar seekbar = (SeekBar) setGoalsListView.getAdapter().getView(i,null,setGoalsListView).findViewById(R.id.seekBar2); // gets the original values put in when making the view - ok  but progress = 0 unless we save it or something somehow.


        }

//        public void makeGlobal(ArrayList<String> values){
//            arrayList_GlobalValues = values;
//        }


    }


}
