package com.example.kennethallan.testbuildofsqllightdatabase_01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;

public class EditThemePage extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    DBHelper Mydb;
    Button commitButton;
    EditText themeName;
    EditText themeDescription;
    ListView themeListView;

    Button TestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_theme_page);

        commitButton = (Button)findViewById(R.id.saveButton);
        themeName = (EditText) findViewById(R.id.editText);
        themeDescription = (EditText)findViewById(R.id.editText2);
        themeListView = (ListView)findViewById(R.id.ListViewSetGoals);
        TestButton = (Button) findViewById(R.id.TestButton);



        Mydb = new DBHelper(this);


        addTheme ();
        TEST();


        // Populate arrayAdaptor

        ListAdapter themeListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,Mydb.getAllThemes());
        themeListView.setAdapter(themeListAdapter);


        themeListView.setOnItemClickListener(

                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String ThemeName = String.valueOf(parent.getItemAtPosition(position));
                        //Toast.makeText(EditThemePage.this,ThemeName,Toast.LENGTH_SHORT).show();

                        String IDtoDelete = Mydb.getThemeID(ThemeName);
                        int isDeleted = Mydb.deleteTheme(IDtoDelete);

                        if (isDeleted > 0)
                            Toast.makeText(EditThemePage.this, "Theme Deleted", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(EditThemePage.this, "No Themes were Deleted", Toast.LENGTH_SHORT).show();

                    }
                }
        );

    }

    public void addTheme (){

        commitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = Mydb.insertTheme(themeName.getText().toString(),themeDescription.getText().toString());
                        if (isInserted == true)
                            Toast.makeText(EditThemePage.this, "Data is inserted", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(EditThemePage.this, "Data is NOT inserted", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(EditThemePage.this, a.get(0).toString(), Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(EditThemePage.this, "ArrayList Empty", Toast.LENGTH_SHORT).show();

                    }
                }
        );
    }


}
