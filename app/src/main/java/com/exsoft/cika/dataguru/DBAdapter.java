package com.exsoft.cika.dataguru;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.exsoft.cika.dataguru.domain.Guru;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cika on 09/11/15.
 */
public class DBAdapter extends SQLiteOpenHelper {

    private static final String DB_NAME = "guruas";
    private static final String TABLE_NAME = "m_guru";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "nama";
    private static final String COL_ALAMAT = "alamat";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS "
            + TABLE_NAME + ";";
    private SQLiteDatabase sqliteDatabase = null;

    public DBAdapter(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL(DROP_TABLE);
    }

    public void openDB() {
        if (sqliteDatabase == null) {
            sqliteDatabase = getWritableDatabase();
        }
    }

    public void closeDB() {
        if (sqliteDatabase != null) {
            if (sqliteDatabase.isOpen()) {
                sqliteDatabase.close();
            }
        }
    }

    public void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COL_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + COL_NAME + " TEXT," + COL_ALAMAT + " TEXT);");
    }

    public void updateGuru(Guru guru) {
        sqliteDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, guru.getNama());
        cv.put(COL_ALAMAT, guru.getAlamat());
        String whereClause = COL_ID + "==?";
        String whereArgs[] = new String[] { guru.getId() };
        sqliteDatabase.update(TABLE_NAME, cv, whereClause, whereArgs);
        sqliteDatabase.close();
    }

    public void save(Guru guru) {
        sqliteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, guru.getNama());
        contentValues.put(COL_ALAMAT, guru.getAlamat());

        sqliteDatabase.insertWithOnConflict(TABLE_NAME, null,
                contentValues, SQLiteDatabase.CONFLICT_IGNORE);

        sqliteDatabase.close();
    }

    public void delete(Guru guru) {
        sqliteDatabase = getWritableDatabase();
        String whereClause = COL_ID + "==?";
        String[] whereArgs = new String[] { String.valueOf(guru.getId()) };
        sqliteDatabase.delete(TABLE_NAME, whereClause, whereArgs);
        sqliteDatabase.close();
    }

    public void deleteAll() {
        sqliteDatabase = getWritableDatabase();
        sqliteDatabase.delete(TABLE_NAME, null, null);
        sqliteDatabase.close();
    }

    public List<Guru> getAllGuru() {
        sqliteDatabase = getWritableDatabase();

        Cursor cursor = this.sqliteDatabase.query(TABLE_NAME, new String[] {
                COL_ID, COL_NAME, COL_ALAMAT }, null, null, null, null, null);
        List<Guru> gurus = new ArrayList<Guru>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Guru guru = new Guru();
                guru.setId(cursor.getString(cursor.getColumnIndex(COL_ID)));
                guru.setNama(cursor.getString(cursor
                        .getColumnIndex(COL_NAME)));
                guru.setAlamat(cursor.getString(cursor
                        .getColumnIndex(COL_ALAMAT)));
                gurus.add(guru);
            }
            sqliteDatabase.close();
            return gurus;
        } else {
            sqliteDatabase.close();
            return new ArrayList<Guru>();
        }
    }
}
