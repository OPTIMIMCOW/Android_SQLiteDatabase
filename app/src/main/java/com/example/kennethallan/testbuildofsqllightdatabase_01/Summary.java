package com.example.kennethallan.testbuildofsqllightdatabase_01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Summary extends AppCompatActivity {

    DBHelper Mydb;

    Button TestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        TestButton = (Button) findViewById(R.id.TestButton_Summary);


        //TODO make a new column in the set goals DB to distinguish set goals and carried over goals
        //TODO make an activity to display a summary of the activities totals etc and how they map up. Need
        // to put in overlays of progressbars etc to see if it looks ok etc.


        TEST();

    }

    public void TEST(){

        TestButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Mydb.AddColumn();

//                        goal_InputTime = getFreeTime(); // passing to the global variable.
//                        //Toast.makeText(SetGoals.this, Integer.toString(goal_InputTime), Toast.LENGTH_SHORT).show();
//                        calculateGoals(arrayList_GlobalValues,goal_InputTime);
//                        boolean tempresult = Mydb.insertGoal(arrayList_GoalsValues);
//                        if (tempresult){
//                            Toast.makeText(SetGoals.this, "Succeeded To Input Goals", Toast.LENGTH_SHORT).show();
//                        }else{
//                            Toast.makeText(SetGoals.this, "Failed To Input Goals", Toast.LENGTH_SHORT).show();
//
//                        }


                    }
                }
        );
    }
}
