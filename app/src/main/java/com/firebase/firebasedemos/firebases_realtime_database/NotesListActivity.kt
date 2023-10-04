package com.firebase.firebasedemos.firebases_realtime_database

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.firebasedemos.databinding.ActivityNotesListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class NotesListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotesListBinding
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var adapter: NotesListAdapter
    var noteList:ArrayList<NotesModel> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityNotesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("Notes")

        adapter = NotesListAdapter(this)

        binding.rvNotesList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvNotesList.adapter = adapter

        adapter.setCallback(object : NotesListAdapter.NotesCallback{
            override fun onClick(position: Int) {
                val intent = Intent(this@NotesListActivity, AddUpdateNotesActivity::class.java)
                intent.putExtra("NoteModel", noteList[position])
                startActivity(intent)
            }

            override fun onDelete(position: Int) {
                databaseReference.child(noteList[position].id).removeValue().addOnSuccessListener {
                    noteList.removeAt(position)
                    adapter.notifyDataSetChanged()
                }
            }
        })

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                noteList.clear()
                adapter.notifyDataSetChanged()
                for(snap in snapshot.children){
                    val note = snap.getValue(NotesModel::class.java)
                    noteList.add(note!!)
                }
                adapter.setNotesList(noteList)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddUpdateNotesActivity::class.java))
            finish()
        }
    }
}