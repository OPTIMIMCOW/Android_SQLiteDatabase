package com.example.kennethallan.testbuildofsqllightdatabase_01;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goals);

        Mydb = new DBHelper(this);
        setGoalsListView = (ListView)findViewById(R.id.ListViewSetGoals);
        TestButton = (Button) findViewById(R.id.TestButton_SetGoals);


        TEST();


        // Populate arrayAdaptor

        String[] testString = {"Test1", "Test2", "Test3"};

        ListAdapter themeListAdapter = new CustomAdaptor_InputSliders(this,testString);
        setGoalsListView.setAdapter(themeListAdapter);


        /*setGoalsListView.setOnItemClickListener(

                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String ThemeName = String.valueOf(parent.getItemAtPosition(position));
                        //Toast.makeText(EditThemePage.this,ThemeName,Toast.LENGTH_SHORT).show();

                        String IDtoDelete = Mydb.getThemeID(ThemeName);
                        int isDeleted = Mydb.deleteTheme(IDtoDelete);

                        if (isDeleted > 0)
                            Toast.makeText(SetGoals.this, "Theme Deleted", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(SetGoals.this, "No Themes were Deleted", Toast.LENGTH_SHORT).show();

                    }
                }
        );*/

    }

    public void TEST(){

        TestButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int value = testBean.getNumber();
                        Toast.makeText(SetGoals.this, Integer.toString(value), Toast.LENGTH_SHORT).show();

                    }
                }
        );
    }



// used as proof of concept to create old array and get all values direct from the array adaptor not by referencing the
    // id and looking at the database.
   /* // Populate arrayAdaptor

    ListAdapter themeListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,Mydb.getAllThemes());
    setGoalsListView.setAdapter(themeListAdapter);



    public void TEST(){

        TestButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> a = Mydb.getAllThemeIDs();

                        int totalvalues = Mydb.getNumberOfThemeIDs();

                        //Toast.makeText(SetGoals.this, Integer.toString(totalvalues), Toast.LENGTH_SHORT).show();

                        for (int x = 0; x < totalvalues ; x = x + 1) {
                            Toast.makeText(SetGoals.this, setGoalsListView.getItemAtPosition(x).toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }*/


    class CustomAdaptor_InputSliders extends ArrayAdapter<String> {

        SetGoals SG;

        int progressValue;

        public CustomAdaptor_InputSliders(Context context, String[] resource) {
            super(context, R.layout.input_slider_01, resource);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater Inflater = LayoutInflater.from(getContext());
            View customView = Inflater.inflate(R.layout.input_slider_01, parent, false);
            // 1) refence to data
            // 2) reference to text view
            // 3) Reference to seekbar.

            String singleViewItem = getItem(position);
            TextView title = (TextView) customView.findViewById(R.id.textView5);
            SeekBar seekbar = (SeekBar) customView.findViewById(R.id.seekBar2);

            title.setText(singleViewItem);


            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                int progressValue;

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    progressValue = progress;

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar bar) {
                    progressValue = bar.getProgress();
                    testBean.setNumber(progressValue);

                }
            });


            return customView;
        }


    }


}
