package com.burak.eventgram.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.burak.eventgram.databinding.RowPostBinding
import com.burak.eventgram.model.Post
import com.squareup.picasso.Picasso

class PostAdapter(private val postList: ArrayList<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    class PostViewHolder(val binding: RowPostBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = RowPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        holder.binding.yorumtext.text = post.comment
        holder.binding.profiltext.text = post.userEmail
        Picasso.get().load(post.downloadUrl).into(holder.binding.imageView)
    }
}