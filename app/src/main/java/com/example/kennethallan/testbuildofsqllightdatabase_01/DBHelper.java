package com.example.kennethallan.testbuildofsqllightdatabase_01;

/**
 * Created by Kenneth Allan on 06/07/2017.
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.renderscript.Sampler;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;
import java.util.Calendar;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CantThinkOfNewName.db";

    public static final int DATABASE_VERSION = 1;


    // The Activity Table

    public static final String TABLE_ACTIVITIES = "Activities";
    public static final String COL1_ACTIVITIES = "ID2";
    public static final String COL2_ACTIVITIES = "NAME";
    public static final String COL3_ACTIVITIES = "MARKS";
    public static final String COL4_ACTIVITIES = "Date";


    // avoid the sqlite keywords of "Table", "Values" etc in the database name

    //The AllThemes Table
    public static final String TABLE_ALLTHEMES = "AllThemes";
    public static final String COL1_ALLTHEMES = "ID2";
    public static final String COL2_ALLTHEMES = "NAME";
    public static final String COL3_ALLTHEMES = "DESCRIPTION";

    //The Current Themes Table
    public static final String TABLE_CURRENTTHEMES = "CurrentThemes";
    public static final String COL1_CURRENTTHEMES = "ID_ALL";
    public static final String COL2_CURRENTTHEMES = "NAME";
    public static final String COL3_CURRENTTHEMES = "DESCRIPTION";

    //The Goals Table
    public static final String TABLE_GOALS = "Goals";
    public static final String COL1_GOALS = "ID2";
    public static final String COL2_GOALS = "DATE";
    public static final String COL3_GOALS = "IsManualSet";

    //The History Table
    public static final String TABLE_HISTORY = "History";
    public static final String COL1_HISTORY = "ID2";
    public static final String COL2_HISTORY = "START_DATE";
    public static final String COL3_HISTORY = "END_DATE";


    public DBHelper(Context context) {

        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }
    // the constructor

    @Override
    public void onCreate(SQLiteDatabase Mydb) {
        //close(); // after a table has been dropped the database remains open and recreating an old table is
        // not possible hence you need to close the database to then start a new databse.
        // not sure close is doing anything now. seems to only work when you change the name of the
        //database to something different which makes no sense at all.
        // TODO Auto-generated method stub
        Mydb.execSQL("create table " + TABLE_ACTIVITIES +" ("+COL1_ACTIVITIES+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COL2_ACTIVITIES+" TEXT,"+COL3_ACTIVITIES+" INTEGER)");
        Mydb.execSQL("create table " + TABLE_ALLTHEMES +" ("+COL1_ALLTHEMES+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COL2_ALLTHEMES+" TEXT,"+COL3_ALLTHEMES+" TEXT)");
        Mydb.execSQL("create table " + TABLE_CURRENTTHEMES +" ("+COL1_CURRENTTHEMES+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COL2_CURRENTTHEMES+" TEXT,"+COL3_CURRENTTHEMES+" TEXT)");
        Mydb.execSQL("create table " + TABLE_GOALS +" ("+COL1_GOALS+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COL2_GOALS+" TEXT)");
        Mydb.execSQL("create table " + TABLE_HISTORY +" ("+COL1_HISTORY+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COL2_HISTORY+" TEXT,"+COL3_HISTORY+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase Mydb, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Mydb.execSQL("DROP TABLE IF EXISTS "+TABLE_ACTIVITIES);
        Mydb.execSQL("DROP TABLE IF EXISTS "+TABLE_ALLTHEMES);

        onCreate(Mydb);
    }

    public void deleteTable(){
        SQLiteDatabase Mydb =this.getWritableDatabase();
        Mydb.execSQL("delete from "+ TABLE_ALLTHEMES);
        Mydb.execSQL("delete from "+ TABLE_CURRENTTHEMES);
        //Mydb.execSQL("DROP TABLE IF EXISTS "+TABLE_ACTIVITIES);
    }



    public boolean insertActivity(String eventTitle,String eventDescription,ArrayList<String> eventValues){
        SQLiteDatabase Mydb =this.getWritableDatabase();

        ContentValues newThingAdd = new ContentValues();
        newThingAdd.put(COL2_ACTIVITIES,eventTitle);
        newThingAdd.put(COL3_ACTIVITIES,eventDescription);

        Date currentDate = Calendar.getInstance().getTime();
        long currentDate_asLong = currentDate.getTime(); // get current date as a long so that we can save that as a string then we can get date object later.
        //set up theme to be added to ALL themes
        newThingAdd.put(COL4_ACTIVITIES,Long.toString(currentDate_asLong));

        if (arrayList_CURRENTTheme_IDs.size()==0){
            getCURRENTThemeIDs();
        }

        for (int i=0;i < arrayList_CURRENTTheme_IDs.size();i++){
            String colName = "ID"+ arrayList_CURRENTTheme_IDs.get(i);
            newThingAdd.put(colName,eventValues.get(i));
        }

        long result = Mydb.insert(TABLE_ACTIVITIES,null,newThingAdd);
        if (result==-1)
            return false;
            else
            return true;
    }


// this inserts themes (types) into both the ALLthemes and the CURRENTthemes tables and adds columns for the new themes in the SetGoals and Activities tables.
    // it does this by first building the content values for the ALLthemes table and autoincrementing the ID column. Then is repeats for the CURRENTtheme
    //table but also adds gets the ID from the ALLthemes table and adds that. After it just extends columns.
    public boolean insertTheme(String name2,String Description){

        //allow to write to databases
        SQLiteDatabase Mydb =this.getWritableDatabase();

        //set up theme to be added to ALL themes
        ContentValues newThingAdd = new ContentValues();
        newThingAdd.put(COL2_ALLTHEMES,name2);
        newThingAdd.put(COL3_ALLTHEMES,Description);

        // actually add to ALL themes database
        long result = Mydb.insertOrThrow(TABLE_ALLTHEMES,null,newThingAdd);

       // fetch id of last added thing ALLtheme to add to CURRENTtheme.
        Cursor res = Mydb.rawQuery("select * from "+TABLE_ALLTHEMES ,null);
        res.moveToLast();
        if (res != null) { // to catch if error and not crash
        }
        String allID = res.getString(0); // this is getting the "All ID" for this specific value.
        String allIDColumnName = "ID"+allID;

        //set up theme to be added to CURRENT themes
        ContentValues newThingAdd123 = new ContentValues();
        newThingAdd123.put(COL2_CURRENTTHEMES,name2);
        newThingAdd123.put(COL3_CURRENTTHEMES,Description);
        newThingAdd123.put("ID3",allID); //TODO update this to a string resource and the name "ALLid"

        //Actually add to CURRENT database
        long result2 = Mydb.insertOrThrow(TABLE_CURRENTTHEMES,null,newThingAdd123);
        if ((result==-1)&(result2==-1))
            return false;
        else
            Mydb.execSQL("ALTER TABLE "+TABLE_ACTIVITIES+" ADD COLUMN "+ allIDColumnName +" char(1)");
            Mydb.execSQL("ALTER TABLE "+TABLE_GOALS+" ADD COLUMN "+ allIDColumnName +" char(1)");
        return true;

        ///Added the column extensions with themes 02/01/2018. TBC if they work.
    }


// method returns a cursor of all the activity table data.
    // this is used in MainActivity (left over from tutorial) in the ShowAllCotacts method. I generally dont do this approach
    //keeping the cursor in the method that calls it and cycling through here.
    public Cursor getAllData(){
        SQLiteDatabase Mydb =this.getWritableDatabase();

        Cursor res = Mydb.rawQuery("select * from "+TABLE_ACTIVITIES,null);

        return res;

    }

    //not sure what this does.

    public boolean updateData(String id,String ActName, String ActValue){
        SQLiteDatabase Mydb =this.getWritableDatabase();
        ContentValues thingUpdate = new ContentValues();
        thingUpdate.put(COL1_ACTIVITIES,id);
        thingUpdate.put(COL2_ACTIVITIES,ActName);
        thingUpdate.put(COL3_ACTIVITIES,ActValue);
        Mydb.update(TABLE_ACTIVITIES,thingUpdate,"ID = ?",new String[]{id});
        return true;

        //"ID = ?" is the where clause ie its the question that is asked to decide if that row will be updated. the answer
        //to the question in the where clause is the "new String[]{id}" array.

    }

    public int deleteActivity(String id){
        SQLiteDatabase Mydb =this.getWritableDatabase();
        return Mydb.delete(TABLE_ACTIVITIES,"ID = ?",new String[]{id});
    }


// adds a column to a table. Currently not in active use. Only used here when I need to extend a table becasuse I didnt create it properly in the first place.
    public void AddColumn(){
        SQLiteDatabase Mydb =this.getWritableDatabase();
        Mydb.execSQL("ALTER TABLE Activities ADD COLUMN Date char(1)");
        //Mydb.execSQL("ALTER TABLE CurrentThemes ADD COLUMN ID3 char(1)");
        //Mydb.execSQL("ALTER TABLE Goals ADD COLUMN IsManualSet char(1)");
        //Mydb.execSQL("ALTER TABLE History ADD COLUMN ID3 char(1)");
    }

    // gets ID numbers of all activities and returns a cursor object.
    //currently not used.
    public Cursor getSpecificID(){

        SQLiteDatabase Mydb =this.getWritableDatabase();

        Cursor res = Mydb.rawQuery("select * from "+TABLE_ACTIVITIES + " where directory = 'ID2'" ,null);
        res.moveToLast();

        //Cursor cursor = database.rawQuery("SELECT * FROM episode WHERE directory = '"
        ///        + directory + "'", null);

        return res;

    }

    ArrayList<String> arrayList_CURRENTTheme_IDs = new ArrayList<String>();
    int numberCURRENTThemes;
    ArrayList<String> arrayList_CURRENTTheme_Names = new ArrayList<String>();

    // this method returns all of the CURRENT theme names in a single array.
    public ArrayList<String> getCURRENTThemeNames() {
        numberCURRENTThemes=0;
        arrayList_CURRENTTheme_Names.clear();

        //hp = new HashMap();
        SQLiteDatabase Mydb = this.getReadableDatabase();
        Cursor res =  Mydb.rawQuery( "select * from " +TABLE_CURRENTTHEMES, null );
        res.moveToFirst();


        // this while loop builds up the arrays containing the CURRENT ids and the CURRENT theme names.
        while(res.isAfterLast() == false){
            arrayList_CURRENTTheme_Names.add(res.getString(res.getColumnIndex(COL2_CURRENTTHEMES)));
            // stuff to do with getting the ID of the strings later
            numberCURRENTThemes=numberCURRENTThemes+1;
            res.moveToNext();
        }
        return arrayList_CURRENTTheme_Names;
    }


// this returns all CURRENT theme ids
    public ArrayList<String> getCURRENTThemeIDs() {
        arrayList_CURRENTTheme_IDs.clear();
        numberCURRENTThemes=0;

        SQLiteDatabase Mydb = this.getReadableDatabase();
        Cursor res =  Mydb.rawQuery( "select * from " +TABLE_CURRENTTHEMES, null );
        res.moveToFirst();

        // this while loop builds up the arrays containing the CURRENT ids and the CURRENT theme names.
        while(res.isAfterLast() == false){

            arrayList_CURRENTTheme_IDs.add(res.getString(res.getColumnIndex("ID3"))); // had to use the column name because i manually added it because i was dumb before. need to change in final build
            numberCURRENTThemes=numberCURRENTThemes+1;
            res.moveToNext();

        }

        return arrayList_CURRENTTheme_IDs;
    }// this is only used for the test button to check the concept worked.

    public String getThemeID(String value){

        return arrayList_CURRENTTheme_IDs.get(Integer.parseInt(value));
    }

    public int getNumberOfCURRENTThemeIDs() {

        numberCURRENTThemes=0;

        SQLiteDatabase Mydb = this.getReadableDatabase();
        Cursor res =  Mydb.rawQuery( "select * from " +TABLE_CURRENTTHEMES, null );
        res.moveToFirst();

        // this while loop builds up the arrays containing the CURRENT ids and the CURRENT theme names.
        while(res.isAfterLast() == false){
            numberCURRENTThemes=numberCURRENTThemes+1;
            res.moveToNext();
        }

        return numberCURRENTThemes;
    }// this is only used for the test button to check the concept worked.




    public int deleteTheme(String id) {
        SQLiteDatabase Mydb = this.getWritableDatabase();
        return Mydb.delete(TABLE_CURRENTTHEMES, COL1_CURRENTTHEMES+" = " +"?", new String[]{id});
    }

    public boolean insertGoal(ArrayList<String> goalValues, String manualSet) {

        ArrayList<String> temp = goalValues;

        //allow to write to databases
        SQLiteDatabase Mydb = this.getWritableDatabase();
        Date currentDate = Calendar.getInstance().getTime(); //TODO maybe in next build try to make a column which can be a date format???? so its easier to compare and read?
        long currentDate_asLong = currentDate.getTime(); // get current date as a long so that we can save that as a string then we can get date object later.
        //set up theme to be added to ALL themes
        ContentValues newThingAdd = new ContentValues();
        newThingAdd.put(COL2_GOALS, Long.toString(currentDate_asLong)); // date as a long but in string for database

        ArrayList<String> themeIDs = getCURRENTThemeIDs();

        for (int i = 0; i < goalValues.size(); i++) {
            newThingAdd.put("ID" + themeIDs.get(i), goalValues.get(i));
        }

        if (manualSet =="Y"){
            newThingAdd.put(COL3_GOALS, "Y");
        }

        // actually add to ALL themes database
        long result = Mydb.insertOrThrow(TABLE_GOALS, null, newThingAdd);
        // this errors if you give it a column name it cant find in the table.
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    ArrayList<String> arrayList_CURRENTGoal_Values = new ArrayList<String>();
    ArrayList<String> arrayList_MANUALGoal_Values = new ArrayList<String>();
    String goal_StartDate = null;
    String goal_ManualIndicator = null;


    // this method returns the values of the current goal targests we are looking at (inc both manual and carry over sets)
    public ArrayList<String> getGoals_CURRENT() {
        goal_StartDate="";
        goal_ManualIndicator="";
        arrayList_CURRENTGoal_Values.clear();

        //hp = new HashMap();
        SQLiteDatabase Mydb = this.getReadableDatabase();
        Cursor res =  Mydb.rawQuery( "select * from " +TABLE_GOALS, null );

        // trying to move to last row
        res.moveToLast();

        // used to make sure we are getting the last row
        while(res.isAfterLast() == true){
            int pos_CURRENT = res.getPosition();
            int pos_NEW = pos_CURRENT - 1;
            res.moveToPosition(pos_NEW);
        }

        // sets the date value of the current goal sequence we are looking at.
        goal_StartDate = res.getString(res.getColumnIndex(COL2_GOALS));

        // sets the manual indicator value of the current goal sequence we are looking at.
        goal_ManualIndicator = res.getString(res.getColumnIndex(COL3_GOALS));

        if (arrayList_CURRENTTheme_IDs.size()==0){
            getCURRENTThemeIDs();
        }

        for (int i=0; i<arrayList_CURRENTTheme_IDs.size();i++){

            String columName = "ID" + arrayList_CURRENTTheme_IDs.get(i);
            arrayList_CURRENTGoal_Values.add(res.getString(res.getColumnIndex(columName)));
        }

        return arrayList_CURRENTGoal_Values;
    }

    public ArrayList<String> getGoals_MANUAL() {
        goal_StartDate="";
        goal_ManualIndicator="";
        arrayList_MANUALGoal_Values.clear();

        //hp = new HashMap();
        SQLiteDatabase Mydb = this.getReadableDatabase();
        Cursor res =  Mydb.rawQuery( "select * from " +TABLE_GOALS, null );

        // trying to move to last row
        res.moveToLast();

        // used to make sure we are getting the last row
        while(res.isAfterLast() == true){
            int pos_CURRENT = res.getPosition();
            int pos_NEW = pos_CURRENT - 1;
            res.moveToPosition(pos_NEW);
        }

        // used to move to the most recent value of goals that were manually set.
        while(res.getString(res.getColumnIndex(COL3_GOALS)) != "Y"){
            int pos_CURRENT = res.getPosition();
            int pos_NEW = pos_CURRENT - 1;
            res.moveToPosition(pos_NEW);
        }

        // sets the date value of the current goal sequence we are looking at.
        goal_StartDate = res.getString(res.getColumnIndex(COL2_GOALS));

        // sets the manual indicator value of the current goal sequence we are looking at.
        goal_ManualIndicator = res.getString(res.getColumnIndex(COL3_GOALS));

        if (arrayList_CURRENTTheme_IDs.size()==0){
            getCURRENTThemeIDs();
        }

        for (int i=0; i<arrayList_CURRENTTheme_IDs.size();i++){

            String columName = "ID" + arrayList_CURRENTTheme_IDs.get(i);
            arrayList_MANUALGoal_Values.add(res.getString(res.getColumnIndex(columName)));

        }

        return arrayList_MANUALGoal_Values;
    }

    // returns the start date for the goals sequence we are looking at. This can either be
    // a manual set list or a carry over list since the variable is overwritten in both.
    public String getGoalStartDate(){
        return goal_StartDate;
    }

    public String getManualIndicator(){
        return goal_ManualIndicator;
    }

}
