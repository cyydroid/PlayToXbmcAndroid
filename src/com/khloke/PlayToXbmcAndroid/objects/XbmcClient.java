package com.khloke.PlayToXbmcAndroid.objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.khloke.PlayToXbmcAndroid.database.PlayToXbmcDbOpenHelper;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: khloke
 * Date: 10/03/13
 * Time: 1:55 PM
 */
public class XbmcClient {

    private int mId = -1;
    private String mName;
    private String mAddress;
    private String mPort;
    private String mUsername;
    private String mPassword;

    public XbmcClient(String aName, String aAddress, String aPort) {
        mName = aName;
        mAddress = aAddress;
        mPort = aPort;
    }

    protected XbmcClient(int aId, String aName, String aAddress, String aPort, String aUsername, String aPassword) {
        mId = aId;
        mName = aName;
        mAddress = aAddress;
        mPort = aPort;
        mUsername = aUsername;
        mPassword = aPassword;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String aName) {
        mName = aName;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String aAddress) {
        mAddress = aAddress;
    }

    public String getPort() {
        return mPort;
    }

    public void setPort(String aPort) {
        mPort = aPort;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String aUsername) {
        mUsername = aUsername;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String aPassword) {
        mPassword = aPassword;
    }

    public void save(Context aContext) {
        PlayToXbmcDbOpenHelper playToXbmcDbOpenHelper = new PlayToXbmcDbOpenHelper(aContext);
        SQLiteDatabase db = playToXbmcDbOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (mId > 0) {
            contentValues.put("id", mId);
        }
        contentValues.put("name", mName);
        contentValues.put("address", mAddress);
        contentValues.put("port", mPort);
        if (mUsername != null && mPassword != null) {
            contentValues.put("username", mUsername);
            contentValues.put("password", mPassword);
        }

        if (mId > 0) {
            db.update("XbmcClient", contentValues, "id=" + mId, null);
        } else {
            db.insert("XbmcClient", null, contentValues);
        }
    }

    public static ArrayList<XbmcClient> loadAll(Context aContext) {
        PlayToXbmcDbOpenHelper dbOpener = new PlayToXbmcDbOpenHelper(aContext);
        SQLiteDatabase db = dbOpener.getReadableDatabase();
        Cursor query = db.query("XbmcClient", null, "", new String[0], "", "", "");
        ArrayList<XbmcClient> clients = new ArrayList<XbmcClient>();

        while (query.moveToNext()) {
            clients.add(
                    new XbmcClient(
                            query.getInt(query.getColumnIndex("id")),
                            query.getString(query.getColumnIndex("name")),
                            query.getString(query.getColumnIndex("address")),
                            query.getString(query.getColumnIndex("port")),
                            query.getString(query.getColumnIndex("username")),
                            query.getString(query.getColumnIndex("password"))));
        }

        return clients;
    }

    public static XbmcClient load(Context aContext, String aId) {
        PlayToXbmcDbOpenHelper dbOpener = new PlayToXbmcDbOpenHelper(aContext);
        SQLiteDatabase db = dbOpener.getReadableDatabase();
        Cursor query = db.query("XbmcClient", null, "id="+aId, new String[0], null, null, null);
        ArrayList<XbmcClient> clients = new ArrayList<XbmcClient>();

        while (query.moveToNext()) {
            clients.add(
                    new XbmcClient(
                            query.getInt(query.getColumnIndex("id")),
                            query.getString(query.getColumnIndex("name")),
                            query.getString(query.getColumnIndex("address")),
                            query.getString(query.getColumnIndex("port")),
                            query.getString(query.getColumnIndex("username")),
                            query.getString(query.getColumnIndex("password"))));
        }

        return clients.get(0);
    }

    public static void delete(Context aContext, String aId) {
        PlayToXbmcDbOpenHelper dbOpener = new PlayToXbmcDbOpenHelper(aContext);
        SQLiteDatabase db = dbOpener.getWritableDatabase();
        db.delete("XbmcClient", "id="+aId, null);
    }
}
