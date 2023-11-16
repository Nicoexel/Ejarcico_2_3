package com.example.ejarcico_2_3;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "photographs.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "photographs";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_DESCRIPTION = "description";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_IMAGE + " BLOB, " +
            COLUMN_DESCRIPTION + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertPhotograph(Photograph photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE, photo.getImage());
        values.put(COLUMN_DESCRIPTION, photo.getDescription());
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public List<Photograph> getAllPhotographs() {
        List<Photograph> photographs = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") byte[] image = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                Photograph photo = new Photograph(image, description);
                photographs.add(photo);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return photographs;
    }
}
