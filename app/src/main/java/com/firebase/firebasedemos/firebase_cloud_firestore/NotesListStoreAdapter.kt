package com.firebase.firebasedemos.firebase_cloud_firestore

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.firebase.firebasedemos.R
import com.firebase.firebasedemos.databinding.SingleNoteViewBinding

class NotesListStoreAdapter(private val mContext: Context): RecyclerView.Adapter<NotesListStoreAdapter.MyViewHolder>() {
    private val notesList: ArrayList<NotesModelStore> = arrayListOf()
    lateinit var notesCallback: NotesCallback
    class MyViewHolder(val binding: SingleNoteViewBinding): RecyclerView.ViewHolder(binding.root) {

    }

    fun setNotesList(list: ArrayList<NotesModelStore>){
        notesList.clear()
        notesList.addAll(list)
        notifyDataSetChanged()
    }

    fun setCallback(notesCallback: NotesCallback){
        this.notesCallback = notesCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = SingleNoteViewBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            with(notesList[position]){
                binding.ivEdit.setOnClickListener {
                    notesCallback.onClick(position)
                }
                binding.ivDelete.setOnClickListener {
                    notesCallback.onDelete(position)
                }
                binding.tvTitle.text = this.title
                binding.tvDescription.text = this.description
                Glide.with(mContext)
                    .setDefaultRequestOptions(
                        RequestOptions()
                            .placeholder(R.drawable.coverpic)
                            .error(R.drawable.coverpic)
                    )
                    .load(this.url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.ivImageView)

                /*if(this.url != null && this.url.isNotEmpty()) {
                    //binding.ivImageView.visibility = View.VISIBLE

                    Glide.with(mContext)
                        .setDefaultRequestOptions(
                            RequestOptions()
                                .placeholder(R.drawable.coverpic)
                                .error(R.drawable.coverpic)
                        )
                        .load(this.url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.ivImageView)
                }
                else{
                   // binding.ivImageView.visibility = View.GONE
                }*/
            }
        }
    }

    interface NotesCallback{
        fun onClick(position: Int){}
        fun onDelete(position: Int){}
    }
}