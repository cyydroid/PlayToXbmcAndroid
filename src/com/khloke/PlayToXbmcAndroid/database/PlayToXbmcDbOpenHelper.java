package com.khloke.PlayToXbmcAndroid.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by IntelliJ IDEA.
 * User: khloke
 * Date: 19/01/13
 * Time: 2:23 AM
 */
public class PlayToXbmcDbOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "PlayToXbmc";
    public static final String CREATE_XBMC_CLIENT_TABLE_STATEMENT =
            "CREATE  TABLE `XbmcClient` (" +
                    "  `id` INTEGER PRIMARY KEY," +
                    "  `name` TEXT NOT NULL," +
                    "  `address` TEXT NOT NULL," +
                    "  `port` TEXT NOT NULL," +
                    "  `username` TEXT DEFAULT NULL," +
                    "  `password` TEXT DEFAULT NULL" +
                    ");";


    public PlayToXbmcDbOpenHelper(Context aContext) {
        super(aContext, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        db.execSQL(CREATE_XBMC_CLIENT_TABLE_STATEMENT);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                //Do nothing
        }
    }
}
