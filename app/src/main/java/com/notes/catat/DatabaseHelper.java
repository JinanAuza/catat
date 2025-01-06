package com.notes.catat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nama database dan tabel
    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 2;  // Increment versi database
    private static final String TABLE_NAME = "notes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";  // Kolom baru untuk isi note

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Membuat tabel
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_CONTENT + " TEXT)";  // Tambahkan kolom content
        db.execSQL(createTable);
    }

    // Upgrade database (untuk menambah kolom baru)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {  // Cek versi sebelumnya
            // Menambahkan kolom content pada tabel
            String alterTable = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_CONTENT + " TEXT";
            db.execSQL(alterTable);
        }
    }

    // Menambahkan data
    public void insertNote(String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_CONTENT, content);  // Simpan isi note
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Mengambil semua data dan mengembalikan List<Note>
    public ArrayList<NotesModel> getAllNotes() {
        ArrayList<NotesModel> noteList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Mengambil semua kolom
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_CONTENT}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                String content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));

                NotesModel note = new NotesModel(id, title, content);  // Buat objek Note
                noteList.add(note);  // Menambahkan objek Note ke list
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return noteList;
    }

    public NotesModel getNoteById(int noteId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_CONTENT},
                COLUMN_ID + "=?", new String[]{String.valueOf(noteId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Ambil data berdasarkan ID
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
            String content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));
            cursor.close();
            db.close();
            return new NotesModel(id, title, content);
        }

        cursor.close();
        db.close();
        return null;
    }

    public void deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Memperbarui data note berdasarkan ID
    public void updateNote(int id, String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_CONTENT, content);

        // Update berdasarkan ID
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

}
