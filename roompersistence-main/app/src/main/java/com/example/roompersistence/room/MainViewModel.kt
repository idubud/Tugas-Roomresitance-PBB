package com.example.roompersistence.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel (application: Application) : AndroidViewModel(application) {
    private val noteDatabase = NoteDatabase.getDatabase(application)
    private val noteDao = noteDatabase.NoteDao()

    private val _allNotes = MutableLiveData<List<Note>>()
    val allNotes: LiveData<List<Note>> = _allNotes

    init {
        fetchNotes()
    }

    fun fetchNotes(){
        viewModelScope.launch{
            try {
                val notes = noteDao.getAllNotes()
                _allNotes.postValue(notes)
            } catch (e: Exception){
                _allNotes.postValue(emptyList())
            }
        }
    }

    fun updateNote(note:Note) {
        viewModelScope.launch ( Dispatchers.IO ) {
            noteDao.update(note)
        }
    }

    fun deleteNoteById(id: Int) {
        viewModelScope.launch (Dispatchers.IO) {
            noteDao.deleteById(id)
        }
    }
}