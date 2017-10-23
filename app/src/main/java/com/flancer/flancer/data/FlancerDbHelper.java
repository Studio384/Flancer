package com.flancer.flancer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.flancer.flancer.data.FlancerContract.JobEntry;
import com.flancer.flancer.data.FlancerContract.CompanyEntry;

/**
 * Created by Yannick on 23/10/2017.
 */

public class FlancerDbHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "flancer.db";
    private static final int DATABASE_VERSION = 2;

    public FlancerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_JOBS_TABLE = "CREATE TABLE " + JobEntry.TABLE_NAME + " (" +
                JobEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                JobEntry.COLUMN_COMPANY_KEY + " INTEGER NOT NULL, " +
                JobEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                JobEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                JobEntry.COLUMN_MINIMUM_BID + " REAL NOT NULL," +
                JobEntry.COLUMN_STREET + " TEXT NOT NULL, " +
                JobEntry.COLUMN_NUMBER + " TEXT NOT NULL, " +
                JobEntry.COLUMN_CITY + " TEXT NOT NULL, " +
                JobEntry.COLUMN_ZIP + " TEXT NOT NULL, " +
                JobEntry.COLUMN_COUNTRY + " TEXT NOT NULL, " +

                // Set up the foreign key to the company table
                " FOREIGN KEY (" + JobEntry.COLUMN_COMPANY_KEY + ") REFERENCES " +
                CompanyEntry.TABLE_NAME + " (" + CompanyEntry.COLUMN_ID + "), " +

                // If there are duplicates of this type, we've got to replace them
                " UNIQUE (" + JobEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_COMPANIES_TABLE = "CREATE TABLE " + CompanyEntry.TABLE_NAME + " (" +
                CompanyEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                CompanyEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                CompanyEntry.COLUMN_STREET + " TEXT NOT NULL, " +
                CompanyEntry.COLUMN_NUMBER + " TEXT NOT NULL, " +
                CompanyEntry.COLUMN_CITY + " TEXT NOT NULL, " +
                CompanyEntry.COLUMN_ZIP + " TEXT NOT NULL, " +
                JobEntry.COLUMN_COUNTRY + " TEXT NOT NULL, " +

                // If there are duplicates of this type, we've got to replace them
                " UNIQUE (" + CompanyEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_JOBS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_COMPANIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // We can safely remove any data as this is just a cache from the online database
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + JobEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CompanyEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}