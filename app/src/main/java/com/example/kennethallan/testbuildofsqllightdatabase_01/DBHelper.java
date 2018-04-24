package com.example.kennethallan.testbuildofsqllightdatabase_01;

/**
 * Created by Kenneth Allan on 06/07/2017.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CantThinkOfNewName.db";

    public static final int DATABASE_VERSION = 1;


    // The Activity Table

    public static final String TABLE_ACTIVITIES = "Activities";
    public static final String COL1_ACTIVITIES = "ID2";
    public static final String COL2_ACTIVITIES = "NAME";
    public static final String COL3_ACTIVITIES = "MARKS";

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



    public boolean insetData(String name2,String Value2){
        SQLiteDatabase Mydb =this.getWritableDatabase();
        ContentValues newThingAdd = new ContentValues();
        newThingAdd.put(COL2_ACTIVITIES,name2);
        newThingAdd.put(COL3_ACTIVITIES,Value2);
        long result = Mydb.insert(TABLE_ACTIVITIES,null,newThingAdd);
        if (result==-1)
            return false;
            else
            return true;
    }

    public boolean insetTheme(String name2,String Description){
        SQLiteDatabase Mydb =this.getWritableDatabase();
        ContentValues newThingAdd = new ContentValues();
        newThingAdd.put(COL2_ALLTHEMES,name2);
        newThingAdd.put(COL3_ALLTHEMES,Description);
        long result = Mydb.insertOrThrow(TABLE_ALLTHEMES,null,newThingAdd);

        //Cursor res = Mydb.rawQuery("select * from "+TABLE_ALLTHEMES + " where 'ID2'" ,null);
        Cursor res = Mydb.rawQuery("select * from "+TABLE_ALLTHEMES ,null);
        res.moveToLast();
        if (res != null) {
            //res.move(-1);
        }

        ContentValues newThingAdd123 = new ContentValues();
        newThingAdd123.put(COL2_CURRENTTHEMES,name2);
        newThingAdd123.put(COL3_CURRENTTHEMES,Description);
        newThingAdd123.put("ID3",res.getString(0));
        long result2 = Mydb.insertOrThrow(TABLE_CURRENTTHEMES,null,newThingAdd123);

        if ((result==-1)&(result2==-1))
            return false;
        else
            return true;
    }



    public Cursor getAllData(){
        SQLiteDatabase Mydb =this.getWritableDatabase();

        Cursor res = Mydb.rawQuery("select * from "+TABLE_ACTIVITIES,null);

        return res;

    }

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

    public void AddColumn(){
        SQLiteDatabase Mydb =this.getWritableDatabase();
        //Mydb.execSQL("ALTER TABLE Activities ADD COLUMN SEX char(1)");
        //Mydb.execSQL("ALTER TABLE CurrentThemes ADD COLUMN ID3 char(1)");
        //Mydb.execSQL("ALTER TABLE Goals ADD COLUMN ID3 char(1)");
        //Mydb.execSQL("ALTER TABLE History ADD COLUMN ID3 char(1)");
    }

    public Cursor getSpecificID(){

        SQLiteDatabase Mydb =this.getWritableDatabase();

        Cursor res = Mydb.rawQuery("select * from "+TABLE_ACTIVITIES + " where directory = 'ID2'" ,null);
        res.moveToLast();

        //Cursor cursor = database.rawQuery("SELECT * FROM episode WHERE directory = '"
        ///        + directory + "'", null);

        return res;

    }

    ArrayList<String> a = new ArrayList<String>();
    int b;

    public ArrayList<String> getAllThemes() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase Mydb = this.getReadableDatabase();
        Cursor res =  Mydb.rawQuery( "select * from " +TABLE_CURRENTTHEMES, null );
        res.moveToFirst();

        b=0;

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(COL2_CURRENTTHEMES)));

            // stuff to do with getting the ID of the strings later
            a.clear();
            a.add(res.getString(res.getColumnIndex("ID3"))); // had to use the column name because i manually added it because i was dumb before. need to change in final build
            res.moveToNext();
            b=b+1;
        }
        return array_list;
    }

    public ArrayList<String> getAllThemeIDs() {

        return a;
    }// this is only used for the test button to check the concept worked.

    public String getThemeID(String value){

        return a.get(Integer.parseInt(value));
    }

    public int getNumberOfThemeIDs() {

        return b;
    }// this is only used for the test button to check the concept worked.


    // Below is superceeded because stopped looking up a Theme name to get ID (because open to ambiguity error)
    // and just got ID from mapping it usaing an arraylist when getting the strings in the first place. (see above)
   /* public String getThemeID(String value){
        SQLiteDatabase Mydb =this.getWritableDatabase();
        //Cursor res = Mydb.rawQuery("select * from "+TABLE_CURRENTTHEMES,null);
        String[] Value2 = new  String[]{value};
        Cursor res = Mydb.rawQuery("select * from "+TABLE_CURRENTTHEMES+" where "+
               COL2_CURRENTTHEMES+" = ?" ,Value2);
        res.moveToLast();

        String themeCurrentID =res.getString(0);
        return themeCurrentID;

    }*/

    public int deleteTheme(String id) {
        SQLiteDatabase Mydb = this.getWritableDatabase();
        return Mydb.delete(TABLE_CURRENTTHEMES, COL1_CURRENTTHEMES+" = " +"?", new String[]{id});
    }
}
