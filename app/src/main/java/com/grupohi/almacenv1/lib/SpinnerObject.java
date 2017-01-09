package com.grupohi.almacenv1.lib;

public class SpinnerObject {

    private String databaseValue;
	private int databaseId;

    public SpinnerObject ( int databaseId , String databaseValue ) {
        this.databaseId = databaseId;
        this.databaseValue = databaseValue;
    }

    public int getId () {
        return databaseId;
    }

    public String getValue () {
        return databaseValue;
    }

    @Override
    public String toString () {
        return databaseValue;
    }

}
