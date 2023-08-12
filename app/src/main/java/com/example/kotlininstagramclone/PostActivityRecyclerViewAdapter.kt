package com.example.kotlininstagramclone

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlininstagramclone.databinding.PostRecyclerViewRowBinding
import com.squareup.picasso.Picasso

class PostActivityRecyclerViewAdapter(var posts :ArrayList<Posts>) : RecyclerView.Adapter<PostActivityRecyclerViewAdapter.PostAdapter> (){

    class PostAdapter(var binding :PostRecyclerViewRowBinding) :RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter {
        var postActivityIflater=PostRecyclerViewRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostAdapter(postActivityIflater)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostAdapter, position: Int) {
        holder.binding.textViewPostUserName.text=posts[position].userName
        holder.binding.textViewPostComment.text=posts[position].comment
        Picasso.get().load(posts[position].images.toUri()).into(holder.binding.imageViewPost)
    }
}