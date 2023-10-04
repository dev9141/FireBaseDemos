package com.firebase.firebasedemos.firebases_realtime_database

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.*
import com.firebase.firebasedemos.R
import com.firebase.firebasedemos.databinding.ActivityAddUpdateNotesBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class AddUpdateNotesActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddUpdateNotesBinding
    lateinit var database: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    private var mediaUrl: Uri? = null
    var mediaUrlString: String = ""
    var noteModel: NotesModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()

        if (intent.hasExtra("NoteModel")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                noteModel = intent.getParcelableExtra("NoteModel", NotesModel::class.java)!!
            } else {
                noteModel = intent.getParcelableExtra("NoteModel")!!
            }
            binding.etNoteTitle.setText(noteModel!!.title)
            binding.etNoteDescription.setText(noteModel!!.description)
            binding.btnAddNote.text = "Update"
            binding.tvHeader.text = "Update Notes"
        }
        binding.fabAdd.setOnClickListener {
            val pickGallery = Intent(Intent.ACTION_PICK)
            pickGallery.type = "image/*"
            activityResultLauncher.launch(pickGallery)
        }
        binding.btnAddNote.setOnClickListener {
            if (noteModel != null) {
                var imageUrl = ""
                if (mediaUrl != null) {
                    val storageReference =
                        FirebaseStorage.getInstance().reference.child("Note Images").child(
                            mediaUrl?.lastPathSegment!!
                        )
                    storageReference.putFile(mediaUrl!!).addOnSuccessListener {
                        val taskUrl = it.storage.downloadUrl
                        while (!taskUrl.isComplete) {
                        }
                        imageUrl = taskUrl.result.toString()
                        mediaUrlString = imageUrl

                        //push is use for generate unique key
                        databaseReference = database.getReference("Notes")
                        val title = binding.etNoteTitle.text.toString()
                        val description = binding.etNoteDescription.text.toString()

                        //mediaUrlString

                        //get auto generated key
                        val note =
                            mapOf("title" to title, "description" to description, "url" to imageUrl)

                        //if use unique key from data then
                        //databaseReference.child(title/timeMillis).setValue(note)
                        databaseReference.child(noteModel!!.id).updateChildren(note)
                            .addOnSuccessListener {
                                finish()
                            }.addOnFailureListener {
                                Toast.makeText(this, "${it.printStackTrace()}", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                } else {
                    //push is use for generate unique key
                    databaseReference = database.getReference("Notes")
                    val title = binding.etNoteTitle.text.toString()
                    val description = binding.etNoteDescription.text.toString()

                    //mediaUrlString

                    //get auto generated key
                    val note =
                        mapOf("title" to title, "description" to description, "url" to imageUrl)

                    //if use unique key from data then
                    //databaseReference.child(title/timeMillis).setValue(note)
                    databaseReference.child(noteModel!!.id).updateChildren(note)
                        .addOnSuccessListener {
                            finish()
                        }.addOnFailureListener {
                            Toast.makeText(this, "${it.printStackTrace()}", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            } else {
                var imageUrl = ""
                if (mediaUrl != null) {
                    val storageReference =
                        FirebaseStorage.getInstance().reference.child("Note Images").child(
                            mediaUrl?.lastPathSegment!!
                        )
                    storageReference.putFile(mediaUrl!!).addOnSuccessListener {
                        val taskUrl = it.storage.downloadUrl
                        while (!taskUrl.isComplete) {
                        }
                        imageUrl = taskUrl.result.toString()
                        mediaUrlString = imageUrl

                        //push is use for generate unique key
                        databaseReference = database.getReference("Notes").push()
                        val title = binding.etNoteTitle.text.toString()
                        val description = binding.etNoteDescription.text.toString()

                        //mediaUrlString

                        //get auto generated key
                        val uniqueKey = databaseReference.key.toString()
                        val note = NotesModel(title, description, uniqueKey, imageUrl)
                        //if use unique key from data then
                        //databaseReference.child(title/timeMillis).setValue(note)
                        databaseReference.setValue(note)
                            .addOnSuccessListener {
                                finish()
                            }.addOnFailureListener {
                                Toast.makeText(this, "${it.printStackTrace()}", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                } else {
                    //push is use for generate unique key
                    databaseReference = database.getReference("Notes").push()
                    val title = binding.etNoteTitle.text.toString()
                    val description = binding.etNoteDescription.text.toString()

                    //mediaUrlString

                    //get auto generated key
                    val uniqueKey = databaseReference.key.toString()
                    val note = NotesModel(title, description, uniqueKey, imageUrl)
                    //if use unique key from data then
                    //databaseReference.child(title/timeMillis).setValue(note)
                    databaseReference.setValue(note)
                        .addOnSuccessListener {
                            finish()
                        }.addOnFailureListener {
                            Toast.makeText(this, "${it.printStackTrace()}", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }
        }
    }

    private val activityResultLauncher = registerForActivityResult(StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            mediaUrl = it!!.data!!.data!!
        }
    }
}