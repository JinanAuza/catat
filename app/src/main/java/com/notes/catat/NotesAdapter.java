package com.notes.catat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class NotesAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NotesModel> notes;
    private DatabaseHelper dbHelper;  // Tambahkan objek DatabaseHelper

    // Konstruktor adapter
    public NotesAdapter(Context context, ArrayList<NotesModel> notes) {
        this.context = context;
        this.notes = notes;
        this.dbHelper = new DatabaseHelper(context);  // Inisialisasi DatabaseHelper
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return notes.get(position).getId();  // Mengembalikan id dari NotesModel
    }

    /**
     * Memperbarui data notes di adapter.
     */
    public void setNotes(ArrayList<NotesModel> newNotes) {
        this.notes = newNotes; // Perbarui data dengan daftar baru
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_notes, parent, false);
        }

        // Ambil data dari NotesModel
        NotesModel note = notes.get(position);

        // Mengambil referensi untuk CardView
        androidx.cardview.widget.CardView cardView = convertView.findViewById(R.id.card_view);

        // Array warna
        int[] colors = {
                ContextCompat.getColor(context, R.color.color1),
                ContextCompat.getColor(context, R.color.color2),
                ContextCompat.getColor(context, R.color.color3),
                ContextCompat.getColor(context, R.color.color4),
                ContextCompat.getColor(context, R.color.color5)
        };

        // Tetapkan warna berdasarkan posisi
        cardView.setCardBackgroundColor(colors[position % colors.length]);

        // Set data ke TextView
        TextView tvNote = convertView.findViewById(R.id.tvNama);
        tvNote.setText(note.getTitle());

        // Tombol delete
        Button btnDelete = convertView.findViewById(R.id.deteleButtonList);
        btnDelete.setOnClickListener(v -> {
            dbHelper.deleteNote(note.getId());
            notes.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show();
        });

        // Set click listener pada item untuk membuka detail
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NotesKonten.class);
            intent.putExtra("noteId", note.getId());
            context.startActivity(intent);
        });

        return convertView;
    }


}
