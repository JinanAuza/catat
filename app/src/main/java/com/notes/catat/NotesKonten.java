package com.notes.catat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotesKonten extends AppCompatActivity {

    private EditText tvTitle, tvNoteContent;
    private int noteId;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.konten_notes);

        tvTitle = findViewById(R.id.etTitle);
        tvNoteContent = findViewById(R.id.etNoteContent);
        FloatingActionButton fabSaveNote = findViewById(R.id.fabSaveNote);

        dbHelper = new DatabaseHelper(this);

        // Ambil ID note dari Intent
        noteId = getIntent().getIntExtra("noteId", -1);
        if (noteId != -1) {
            NotesModel note = dbHelper.getNoteById(noteId);
            if (note != null) {
                tvTitle.setText(note.getTitle());
                tvNoteContent.setText(note.getContent());
            }
        }

        fabSaveNote.setOnClickListener(v -> {
            String newTitle = tvTitle.getText().toString().trim();
            String newContent = tvNoteContent.getText().toString().trim();

            if (!newTitle.isEmpty() && !newContent.isEmpty()) {
                dbHelper.updateNote(noteId, newTitle, newContent);

                // Kirim hasil kembali ke MainActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("note_updated", true);
                setResult(RESULT_OK, resultIntent);

                Toast.makeText(this, "Note berhasil diperbarui!", Toast.LENGTH_SHORT).show();
                finish(); // Kembali ke MainActivity
            } else {
                Toast.makeText(this, "Judul dan isi tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
