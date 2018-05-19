package com.example.kennethallan.testbuildofsqllightdatabase_01;

import android.content.Context;
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
    Button commitButton;
    EditText eventName;
    EditText eventDescription;
    ListView eventsListView;

    Button TestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        commitButton = (Button)findViewById(R.id.saveButton);
        eventName = (EditText) findViewById(R.id.editText);
        eventDescription = (EditText)findViewById(R.id.editText2);
        eventsListView = (ListView)findViewById(R.id.ListViewSetActivityInputs);
        TestButton = (Button) findViewById(R.id.TestButton);

        Mydb = new DBHelper(this);

        addTheme ();
        TEST();

        // Populate arrayAdaptor
        String[] testString = {"Test1", "Test2", "Test3"};

        ListAdapter themeListAdapter = new AddEvent.CustomAdaptor_InputNumbers(this,testString);
        eventsListView.setAdapter(themeListAdapter);
    }

    public void addTheme (){

        commitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = Mydb.insertTheme(eventName.getText().toString(),eventDescription.getText().toString());
                        if (isInserted == true)
                            Toast.makeText(AddEvent.this, "Data is inserted", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(AddEvent.this, "Data is NOT inserted", Toast.LENGTH_SHORT).show();

                    }
                }
        );
    }


    public void TEST(){

        TestButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> a = Mydb.getAllThemeIDs();
                        if (a.size()>0)
                            Toast.makeText(AddEvent.this, a.get(0).toString(), Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(AddEvent.this, "ArrayList Empty", Toast.LENGTH_SHORT).show();

                    }
                }
        );
    }


    class CustomAdaptor_InputNumbers extends ArrayAdapter<String> {

        SetGoals SG;

        int progressValue;

        public CustomAdaptor_InputNumbers(Context context, String[] resource) {
            super(context, R.layout.input_activitynumbers, resource);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater Inflater = LayoutInflater.from(getContext());
            View customView = Inflater.inflate(R.layout.input_activitynumbers, parent, false);

            String singleViewItem = getItem(position);
            TextView title = (TextView) customView.findViewById(R.id.tv01);
            EditText value = (EditText) customView.findViewById(R.id.et01);

            title.setText(singleViewItem);

            return customView;
        }


    }
}
