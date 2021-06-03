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
import com.example.library.model.Book
import com.squareup.picasso.Picasso

class BookActivityAdapter(private val context: Context, private val bookList: ArrayList<Book>):
    RecyclerView.Adapter<BookActivityAdapter.BookActivityViewHolder>() {

    class BookActivityViewHolder(view: View):RecyclerView.ViewHolder(view){

        val imgBook:ImageView = view.findViewById(R.id.imgBook)
        val txtBookTitle:TextView = view.findViewById(R.id.txtBookTitle)
        val txtAuthor:TextView = view.findViewById(R.id.txtAuthor)
        val txtPrice:TextView = view.findViewById(R.id.txtPrice)
        val llRecyclerBookRow:LinearLayout = view.findViewById(R.id.llRecyclerBookRow)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookActivityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_books_row,parent,false)

        return BookActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookActivityViewHolder, position: Int) {

        val list = bookList[position]
        holder.txtBookTitle.text = list.bookTitle
        holder.txtAuthor.text =list.bookAuthor
        holder.txtPrice.text = list.bookPrice
        Picasso.get().load(list.bookImage).error(R.drawable.ic_launcher_background).into(holder.imgBook)

        holder.llRecyclerBookRow.setOnClickListener {
            val intent = Intent(context,DescriptionActivity::class.java)
            intent.putExtra("id", list.id)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return bookList.size
    }

}