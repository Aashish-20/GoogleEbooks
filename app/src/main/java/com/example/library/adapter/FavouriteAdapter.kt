package com.example.library.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.library.DescriptionActivity
import com.example.library.R
import com.example.library.model.Favourite
import com.squareup.picasso.Picasso

class FavouriteAdapter(private val context: Context,private val favList:ArrayList<Favourite>):RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>() {

    class FavouriteViewHolder(view: View):RecyclerView.ViewHolder(view){
        val link:ImageView = view.findViewById(R.id.imgFavBookImage)
        val title:TextView = view.findViewById(R.id.txtFavBookTitle)
        val author:TextView = view.findViewById(R.id.txtFavBookAuthor)
        val price:TextView = view.findViewById(R.id.txtFavBookPrice)
        val llFavContent:LinearLayout = view.findViewById(R.id.llFavContent)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_favourite_row,parent,false)

        return FavouriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val list = favList[position]
        if (list.title.isNotEmpty()){
            holder.title.text = list.title
        }else{
            holder.title.text = "NO_TITLE"
        }
        if (list.authorName.isNotEmpty()) {
            holder.author.text = list.authorName
        }else{
            holder.author.text = "NO_AUTHOR"
        }
        if (list.price.isNotEmpty()){
            holder.price.text = list.price
        }else{
            holder.price.text = "NOT_FOR_SALE"
        }
        if (list.link.isNotEmpty()){
            Picasso.get().load(list.link).error(R.drawable.ic_launcher_background).into(holder.link)
        }else{
            val image = "https://cdn1.iconfinder.com/data/icons/get-me-home/512/square_blank_check_empty-128.png"
            Picasso.get().load(image).error(R.drawable.ic_launcher_background).into(holder.link)
        }

        holder.llFavContent.setOnClickListener {
            val intent = Intent(context,DescriptionActivity::class.java)
            intent.putExtra("id",list.id)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return favList.size
    }
}