package com.example.roompersistence

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roompersistence.room.MainViewModel
import com.example.roompersistence.room.Note
import com.example.roompersistence.room.NoteAdapter
import com.example.roompersistence.ui.AddNoteActivity
import com.example.roompersistence.ui.EditNoteActivity

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var mainViewModel: MainViewModel

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(this,"Note Add Successfully", Toast.LENGTH_SHORT).show()
            mainViewModel.fetchNotes()
        }
    }

    private val startForResultEdit = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val updatedNote = result.data?.getSerializableExtra("updatedNote") as Note
            updatedNote.let {
                Toast.makeText(this,"Data Berhasil di edit", Toast.LENGTH_SHORT).show()
                mainViewModel.fetchNotes()
            }

        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerViewNotes)
        recyclerView .layoutManager = LinearLayoutManager(this)

        mainViewModel = ViewModelProvider(this).get(mainViewModel::class.java)

        mainViewModel.allNotes.observe(this){notes ->
            updateRecyclerView(notes)
        }

        val buttonAddNote: Button = findViewById(R.id.buttonAddNote)
        buttonAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startForResult.launch(intent)
        }
    }

    private fun updateRecyclerView(notes: List<Note>) {
        noteAdapter = NoteAdapter(notes,
            onDeleteListener = {noteId -> showDeleteConfirmationDialog(noteId)},
            onEditListener = {note -> showEditConfirmationDialog(note)})
        recyclerView.adapter = noteAdapter
        noteAdapter.notifyDataSetChanged()
    }

    private fun showEditConfirmationDialog(note: Note) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit Note")
            .setMessage("Do you want to edit this note?")
            .setPositiveButton("Yes"){dialog, _ ->
                val intent = Intent(this, EditNoteActivity::class.java)
                intent.putExtra("note", note)
                startForResultEdit.launch(intent)
                dialog.dismiss()
            }
            .setNegativeButton("No") {
                    dialog, _->
                dialog.dismiss()
            }
        builder.create().show()
    }

    private fun showDeleteConfirmationDialog(noteID: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("delete note")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Yes"){dialog, _->
                mainViewModel.deleteNoteById(noteID)
                Toast.makeText(this, "Note delete successfully", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                mainViewModel.fetchNotes()

            }
            .setNegativeButton("No"){
                    dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }
}