package com.firebase.firebasedemos.firebase_cloud_firestore

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.firebasedemos.databinding.ActivityNotesListBinding
import com.google.firebase.firestore.FirebaseFirestore

class NotesListStoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotesListBinding
    lateinit var firebaseDatabase: FirebaseFirestore
    lateinit var adapter: NotesListStoreAdapter
    var noteList: ArrayList<NotesModelStore> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityNotesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseFirestore.getInstance()

        adapter = NotesListStoreAdapter(this)

        binding.rvNotesList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvNotesList.adapter = adapter

        adapter.setCallback(object : NotesListStoreAdapter.NotesCallback {
            override fun onClick(position: Int) {
                val intent = Intent(this@NotesListStoreActivity, AddUpdateNotesStoreActivity::class.java)
                intent.putExtra("NoteModel", noteList[position])
                startActivity(intent)
            }

            override fun onDelete(position: Int) {
                firebaseDatabase
                    .collection("NotesFireStore")
                    .document(noteList[position].id)
                    .delete()
                    .addOnSuccessListener {
                        noteList.removeAt(position)
                        adapter.notifyDataSetChanged()
                    }
            }
        })

        firebaseDatabase.collection("NotesFireStore").get().addOnCompleteListener {
            if(it.isSuccessful){
                for(task in it.result){
                    val note:NotesModelStore = task.toObject(NotesModelStore::class.java)
                    note.id = task.id
                    noteList.add(note)
                }

                adapter.setNotesList(noteList)
            }
        }

        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddUpdateNotesStoreActivity::class.java))
            finish()
        }
    }
}