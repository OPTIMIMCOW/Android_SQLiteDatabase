package com.example.kennethallan.testbuildofsqllightdatabase_01;

/**
 * Created by Kenneth Allan on 01/04/2018.
 */

public class Beans {

    Beans oneBeantoRuleThemAll;

    int theImportantNumber;

    public Beans(int num){
        theImportantNumber = num;
    }

    public void setNumber(int value){
        theImportantNumber = value;
    }

    public int getNumber(){
        return theImportantNumber;
    }

}
