package com.example.kennethallan.testbuildofsqllightdatabase_01;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.view.View;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.EditText;
import android.util.Log;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

// insert activity has a lot of stuff from the tutorial but also is where activities are set into the database.
// TODO Add a timer to reset all activities in the summation.
// TODO Transfer adding activities to another activity just for clarity and have a summary of the activities there. Essentially copy the add themes activity.
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String EXTRA_MESSAGE = "com.example.kennethallan.testbuildofsqllightdatabase_01.MESSAGE";

    ListView obj;
    DBHelper Mydb;
    TextView mytextview;

    EditText ActivityName;
    EditText Value;

    TextView idUpdate;
    Context contextWord = this;
    Button syncButton;
    Button deleteTable;
    Button showAll;
    Button updateButton;

    Button deleteActivityButton;
    Button addColumnButton;
    Button addThemeButton;
    Button setGoalButton;
    Button addActivityButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         /* Stetho */
        Stetho.initializeWithDefaults(this);

        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        Log.d(TAG,"In OnCreate()");

        Mydb = new DBHelper(this); // can call because it is a public class in the package

        ActivityName = (EditText) findViewById(R.id.EditTextActivityName);
        Value = (EditText) findViewById(R.id.EditTextValue);

        syncButton = (Button) findViewById(R.id.Button_ID);
        showAll = (Button) findViewById(R.id.button_showAll);

        deleteTable = (Button) findViewById(R.id.deleteTable);

        updateButton = (Button) findViewById(R.id.UpdateButton);
        idUpdate = (EditText) findViewById(R.id.editTextID);
        deleteActivityButton = (Button) findViewById(R.id.DeleteActivityButton);
        addColumnButton = (Button) findViewById(R.id.AddColumn);
        addThemeButton = (Button) findViewById(R.id.AddThemeButton);
        setGoalButton = (Button) findViewById(R.id.SetGoalButton);
        addActivityButton = (Button) findViewById(R.id.SetActivity);


        deleteTable();
        syncContacts();
        showAllContacts();
        idUpdateActivity();
        deleteActivity();
        addColumn();
        addTheme();
        setGoal();
        addActivity();

    }

    public void deleteTable() {

        Log.d(TAG,"In deleteTable()");

        deleteTable.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG,"Now In deleteTable.onclick listener");
                        Mydb.deleteTable();
                    }
                }
        );


    }


    public void syncContacts() {
        syncButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        boolean IsInserted = Mydb.insertActivity(ActivityName.getText().toString(), Value.getText().toString());
//                        if (IsInserted == true)
//                            Toast.makeText(MainActivity.this, "Data is inserted", Toast.LENGTH_SHORT).show();
//                        else
//                            Toast.makeText(MainActivity.this, "Data is NOT inserted", Toast.LENGTH_SHORT).show();

                    }// need to come back and extend the insert activity method to have an array adapter input or just delete it from here since its no longet necessary
                }
        );

    }

    public void showAllContacts() {
        showAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res2 = Mydb.getAllData();
                        if (res2.getCount() == 0){
                            // message saying error
                            showMessage("Error", "No Data Found");
                        }
                        StringBuffer buffer = new StringBuffer();
                        // while loop only returns false when its past the last real value in set ie it exits
                        while(res2.moveToNext()){
                            buffer.append("ID: "+res2.getString(0).toString()+" \n"); // \n means break line so when this is printed the values wont be right next to each other on a single line
                            buffer.append("Name: "+res2.getString(1).toString()+" \n");
                            buffer.append("Marks: "+res2.getString(2).toString()+" \n\n"); //\n\n is a double line break so between data sets there will be a gap

                        }
                        showMessage("List", buffer.toString());
                    }
                }
        );

    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void idUpdateActivity(){

        updateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isUpdated=Mydb.updateData(idUpdate.getText().toString(),ActivityName.getText().toString(), Value.getText().toString());
                        if (isUpdated==true)

                            Toast.makeText(MainActivity.this, "Data is updated", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(MainActivity.this, "Data is NOT updated", Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

    public void deleteActivity(){
        deleteActivityButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer isDeleted = Mydb.deleteActivity(idUpdate.getText().toString());
                        if (isDeleted > 0)
                            Toast.makeText(MainActivity.this, isDeleted.toString()+" Activities are Deleted", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(MainActivity.this, "No Activities have been deleted", Toast.LENGTH_SHORT).show();

                    }// when using ".toString()" the variable neets to be an Integer not int type.
                    //when you delete the activity row the id number is permanantly deleted and will not come back when a
                    // new activity is added. ie id 3 is permanantly gone if deleted once.
                }
        );
    }

    public void addColumn(){
        addColumnButton.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Mydb.AddColumn();
                    }
                }
        );

    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
    }

    // opens a new activity to where themes are set and viewed.
    public void addTheme (){
        addThemeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,EditThemePage.class);
                        //String message = "Successful Intent";
                        //intent.putExtra(EXTRA_MESSAGE, message);
                        startActivity(intent);
                    }
                }
        );
    }

    // opens an activity to where goals are set.
    public void setGoal(){
        setGoalButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,SetGoals.class);
                        //String message = "Successful Intent";
                        //intent.putExtra(EXTRA_MESSAGE, message);
                        startActivity(intent);
                    }
                }
        );
    }

    public void addActivity(){
        addActivityButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,AddEvent.class);
                        startActivity(intent);
                    }
                }
        );
    }





        /*syncButton.setOnClickListener(new View.OnClickListener() {

          public void onClick(View v) {

                // initialise adding to the database method
                //mydb.updateValues(id, ActivityName.getText().toString(), Value.getText().toString());
              // mydb.updateValues(ActivityName.getText().toString(), Value.getText().toString());

              // display();



                // initilise diplaying all the value names
                ArrayList value_array_list = mydb.getAllValues();
                ArrayAdapter arrayAdapter=new ArrayAdapter(contextWord, android.R.layout.simple_list_item_1, value_array_list);
                obj = (ListView)findViewById(R.id.listView1);
                obj.setAdapter(arrayAdapter);
            }

        });*/

    //}


 /*   public void display() {

    ArrayList value_array_list = mydb.getAllValues();
    ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, value_array_list);
    obj = (ListView)findViewById(R.id.listView1);
    obj.setAdapter(arrayAdapter);

    }*/

}