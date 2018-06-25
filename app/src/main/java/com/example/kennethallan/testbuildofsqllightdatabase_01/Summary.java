package com.example.kennethallan.testbuildofsqllightdatabase_01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.Calendar;
import java.lang.Object;


import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Summary extends AppCompatActivity {

    DBHelper Mydb;
    Button TestButton;
    ListView setGoalsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Mydb = new DBHelper(this);
        TestButton = (Button) findViewById(R.id.TestButton_Summary);
        setGoalsListView = (ListView)findViewById(R.id.ListViewSetGoals);



        //TODO make an activity to display a summary of the activities totals etc and how they map up. Need
        // to put in overlays of progressbars etc to see if it looks ok etc.

        //define values to use in building
        ArrayList<String> themeValues = getCurrentThemeNames();

        //TODO get current goal values
        //TODO get attained theme values

        //ListAdapter themeListAdapter = new SetGoals.CustomAdaptor_InputSliders(this,themeValues);
        //setGoalsListView.setAdapter(themeListAdapter);




        TEST();

    }

    public ArrayList<String> getCurrentThemeNames(){
        return Mydb.getCURRENTThemeNames();
    }

    public ArrayList<String> getCurrentGoalValues(){
        // return the cursor object of the set goals table and cycle through here?
        // build an arraylist and return

        return Mydb.getGoals_CURRENT();
    }

    public ArrayList<String> calculateAttainmentValues(){
        // maybe do in Mydb?
        // get current date
        //get previous summary date/ or getdate the previous goals were set for the summary.
        // Can get if we save the date from the getCurrentGoalValues in a variable.
        // get cursor object of the activities list
        // filter by date ie compare if it is possible. do a test function for this using the test button. Can we convert strings back to dates???

        return null;
    }

    public void TEST(){

        TestButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // TODO implement carryover funcationality
                        // TODO implement filtering of dates cuncationaliy.
                        Date TEMPdate = Calendar.getInstance().getTime();
                        long TEMPdate_long = TEMPdate.getTime();

                        ArrayList<String> temp = new ArrayList<String>();
                        temp = Mydb.getGoals_CURRENT();
                        String TEMPdate_fetch = Mydb.getGoalStartDate();

                        Date reconstructedDate = new Date(Long.parseLong(TEMPdate_fetch));

                        if (TEMPdate_long > Long.parseLong(TEMPdate_fetch)) {
                            Toast.makeText(Summary.this, "Gives Correct Date Comparrisson", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Summary.this, "Gives InCorrect Date Comparrisson", Toast.LENGTH_SHORT).show();
                        }

                        // CONCLUSION: can reconstruct date from a long but unnecessary since for a simple comparisson the long values can be compared.
                        // CONCLUSION: since we are saving these as Longs you can compare the longs (num of seconds since 1 Jan 1970.




                        //Date theSameDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(TEMPdate);



                    }
                }
        );
    }
}
