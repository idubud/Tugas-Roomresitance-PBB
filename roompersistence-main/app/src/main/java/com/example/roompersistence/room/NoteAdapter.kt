package com.example.roompersistence.room

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.roompersistence.R

class NoteAdapter(
    private val notes:List<Note>,
    private val onDeleteListener: (Note) -> Unit,
    private val onEditListener: (Note) -> Unit
    ) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteAdapter.NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
        holder.deleteButton.setOnClickListener {
            onDeleteListener(note.id)
        }

        holder.editButton.setOnClickListener {
            onEditListener(note)

        }
    }

    override fun getItemCount() : Int = notes.size

    class NoteViewHolder(private val view: android.view.View) : RecyclerView.ViewHolder(view) {

        val deleteButton: ImageButton = view.findViewById(R.id.buttonDelete)
        val editButton: ImageButton = view.findViewById(R.id.buttonEdit)

        fun bind(note:Note){
            val titleTextView = view.findViewById<TextView>(R.id.textViewTitle)
            val contentTextView = view.findViewById<TextView>(R.id.textViewContent)
            val timestampTextView = view.findViewById<TextView>(R.id.textViewTimestamp)

            titleTextView.text = note.title
            contentTextView.text = note.content
            timestampTextView.text = note.timestamp
        }
    }
}