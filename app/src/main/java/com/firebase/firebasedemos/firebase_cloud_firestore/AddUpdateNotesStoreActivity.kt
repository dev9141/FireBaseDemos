package com.firebase.firebasedemos.firebase_cloud_firestore

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.*
import com.firebase.firebasedemos.databinding.ActivityAddUpdateNotesBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class AddUpdateNotesStoreActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddUpdateNotesBinding
    lateinit var database: FirebaseFirestore
    private var mediaUrl: Uri? = null
    var mediaUrlString: String = ""
    var noteModel: NotesModelStore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Firebase FireStore"
        database = FirebaseFirestore.getInstance()

        if (intent.hasExtra("NoteModel")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                noteModel = intent.getParcelableExtra("NoteModel", NotesModelStore::class.java)!!
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
                        FirebaseStorage.getInstance().reference.child("Note Images Storage").child(
                            mediaUrl?.lastPathSegment!!
                        )
                    storageReference.putFile(mediaUrl!!).addOnSuccessListener {
                        val taskUrl = it.storage.downloadUrl
                        while (!taskUrl.isComplete) {
                        }
                        imageUrl = taskUrl.result.toString()
                        mediaUrlString = imageUrl
                        val title = binding.etNoteTitle.text.toString()
                        val description = binding.etNoteDescription.text.toString()

                        val note = hashMapOf("title" to title, "description" to description, "url" to imageUrl)
                        database.collection("NotesFireStore").document(noteModel!!.id).set(note)
                            .addOnSuccessListener {
                                finish()
                            }.addOnFailureListener {
                                Toast.makeText(this, "${it.printStackTrace()}", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                } else {
                    val title = binding.etNoteTitle.text.toString()
                    val description = binding.etNoteDescription.text.toString()

                    val note = hashMapOf("title" to title, "description" to description, "url" to imageUrl)
                    database.collection("NotesFireStore").document(noteModel!!.id).set(note)
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
                        FirebaseStorage.getInstance().reference.child("Note Images Storage").child(
                            mediaUrl?.lastPathSegment!!
                        )
                    storageReference.putFile(mediaUrl!!).addOnSuccessListener {
                        val taskUrl = it.storage.downloadUrl
                        while (!taskUrl.isComplete) {
                        }
                        imageUrl = taskUrl.result.toString()
                        mediaUrlString = imageUrl
                        val title = binding.etNoteTitle.text.toString()
                        val description = binding.etNoteDescription.text.toString()

                        val note = hashMapOf("title" to title, "description" to description, "url" to imageUrl)
                        database.collection("NotesFireStore").add(note)
                            .addOnSuccessListener {
                                finish()
                            }.addOnFailureListener {
                                Toast.makeText(this, "${it.printStackTrace()}", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                } else {
                    val title = binding.etNoteTitle.text.toString()
                    val description = binding.etNoteDescription.text.toString()

                    val note = hashMapOf("title" to title, "description" to description, "url" to imageUrl)
                    database.collection("NotesFireStore").add(note)
                        .addOnCompleteListener {
                            finish()
                        }
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