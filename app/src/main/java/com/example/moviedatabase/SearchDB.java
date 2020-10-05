package com.example.moviedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SearchDB {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "_name";

    public static final String DATABASE_NAME = "SearchDB";
    public static final String DATABASE_TABLE = "SearchesTable";
    private static final int DATABASE_VERSION = 1;

    private DBHelper helper;
    private final Context context;
    private SQLiteDatabase database;

    public SearchDB(Context context) {
        this.context = context;
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sqlCode = "CREATE TABLE " + DATABASE_TABLE + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_NAME + " NOT NULL);";
            db.execSQL(sqlCode);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    public SearchDB open() throws SQLException {
        helper = new DBHelper(context);
        database = helper.getWritableDatabase();

        return this;
    }

    public void close() {
        helper.close();
    }

    public long createEntry(String search) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, search);

        return database.insert(DATABASE_TABLE, null, contentValues);
    }

    public ArrayList<String> getSearches() {

        ArrayList<String> list = new ArrayList<>();
        String[] column = new String[]{KEY_ROWID, KEY_NAME};
        Cursor cursor = database.query(DATABASE_TABLE, column, null, null, null, null, null);

        int search = cursor.getColumnIndex(KEY_NAME);
        for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {
            list.add(cursor.getString(search));
        }
        cursor.close();

        return list;
    }

    public void deleteFirst() {
        String[] column = new String[]{KEY_ROWID, KEY_NAME};
        Cursor cursor = database.query(DATABASE_TABLE, column, null, null, null, null, null);
        int idInd = cursor.getColumnIndex(KEY_ROWID);

        cursor.moveToFirst();
        int id = cursor.getInt(idInd);
        cursor.close();
        String sqlCode = "DELETE FROM " + DATABASE_TABLE + " WHERE " + KEY_ROWID + " = " + id + ";";
        database.execSQL(sqlCode);
    }
}
