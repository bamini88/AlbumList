package com.coding.albums.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.coding.albums.R
import com.coding.albums.model.Album
import java.util.*

class RecyclerAdapter:
    RecyclerView.Adapter<RecyclerAdapter.AlbumsViewHolder>() {
    private var albums: List<Album> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_album_list_item, parent, false)
        return AlbumsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumsViewHolder, position: Int) {
        holder.bind(albums[position])
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    fun setAlbums(albums: List<Album>) {
        this.albums = albums
        notifyDataSetChanged()
    }

    inner class AlbumsViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.title)
        fun bind(album: Album) {
            title.text = album.title
        }

    }
}