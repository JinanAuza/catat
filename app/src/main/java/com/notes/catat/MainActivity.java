package com.notes.catat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listViewNotes;
    private ArrayList<NotesModel> noteList;
    private NotesAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_utama);

        listViewNotes = findViewById(R.id.rvKaryawan);
        dbHelper = new DatabaseHelper(this);

        // Ambil data dari SQLite
        noteList = dbHelper.getAllNotes();

        // Set adapter ke ListView
        adapter = new NotesAdapter(this, noteList);
        listViewNotes.setAdapter(adapter);

        FloatingActionButton fabAddNote = findViewById(R.id.fabAddNote); // Referensi ke FAB

        fabAddNote.setOnClickListener(v -> {
            // Buat note baru dengan judul "Judul Baru"
            String newTitle = "Judul Baru";
            String newContent = "";

            // Simpan note baru ke database
            dbHelper.insertNote(newTitle, newContent);

            // Ambil note terakhir untuk mendapatkan ID-nya
            noteList = dbHelper.getAllNotes();
            NotesModel lastNote = noteList.get(noteList.size() - 1);

            // Perbarui adapter
            adapter.notifyDataSetChanged();

            // Buka halaman NotesKonten dengan ID note baru
            Intent intent = new Intent(this, NotesKonten.class);
            intent.putExtra("noteId", lastNote.getId());
            startActivity(intent);

            Toast.makeText(this, "Note Baru Ditambahkan", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Muat ulang data dari database
        noteList = dbHelper.getAllNotes();

        // Perbarui adapter
        adapter.setNotes(noteList); // Pastikan ada metode setNotes di adapter
        adapter.notifyDataSetChanged();
    }
}
