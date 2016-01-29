package com.moonbytes.colorwheel.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dasteini on 28.01.16.
 */
public class DeviceDatabase {
    private final Context context;
    private DeviceDbHelper dbHelper;
    final static String TABLE_NAME = "DEVICES",
                        ID = "_ID",
                        DEV_NAME = "DEV_NAME",
                        DEV_IP = "DEV_IP";
    final static String CREATE_DB = "CREATE TABLE " + TABLE_NAME + " (" +
                                    ID + " INTEGER PRIMARY KEY, " +
                                    DEV_NAME + " TEXT, " +
                                    DEV_IP + " TEXT)";

    public DeviceDatabase(Context c) {
        this.context = c;
        initDb();
    }

    private void initDb() {
        dbHelper = new DeviceDbHelper(context);
    }


    public long addDevice(DeviceItem d) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DEV_NAME, d.getDeviceName());
        cv.put(DEV_IP, d.getIpAdress());
        long value = db.insert(TABLE_NAME, null, cv);
        db.close();
        return value;
    }

    public void deleteDevice(DeviceItem device) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.delete(TABLE_NAME, ID+" = ?", new String[] {String.valueOf(device.getId())});
        db.close();
    }

    public List<DeviceItem> getAllDevices() {
        List<DeviceItem> devices = new ArrayList<DeviceItem>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        DeviceItem devItem;

        if(cursor.moveToFirst()) {
            do {
                devItem = new DeviceItem();
                devItem.setId(cursor.getLong(0));
                devItem.setDeviceName(cursor.getString(1));
                devItem.setIpAddress(cursor.getString(2));
                devices.add(devItem);
            } while (cursor.moveToNext());

        }

        db.close();
        return devices;
    }



    public class DeviceDbHelper extends SQLiteOpenHelper {
        private final static String DATABASE_NAME = "deviceDatabase.db";
        private final static int DATABASE_VERSION = 1;

        public DeviceDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
