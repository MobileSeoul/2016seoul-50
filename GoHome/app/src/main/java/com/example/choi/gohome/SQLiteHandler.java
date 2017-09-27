package com.example.choi.gohome;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by choi on 2016-08-27.
 */
public class SQLiteHandler extends SQLiteOpenHelper {

    public SQLiteHandler(Context context) {
        super(context, "gohome.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table = "CREATE TABLE map_route (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "lat DOUBLE NOT NULL, " +
                "lng DOUBLE NOT NULL, " +
                "size INTEGER NOT NULL)";
        db.execSQL(table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<LatLngSize> selectAll() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        List<LatLngSize> latLngSize = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from map_route", null);
        while(cursor.moveToNext()) {
            LatLngSize mLatLngSize = new LatLngSize(cursor.getDouble(1), cursor.getDouble(2), cursor.getInt(3));
            latLngSize.add(mLatLngSize);
        }
        return latLngSize;
    }

    public void insert(String _query) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(_query);
        sqLiteDatabase.close();
    }

    public void delete(String _query) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(_query);
        sqLiteDatabase.close();
    }
}
