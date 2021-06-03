package com.example.library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.library.adapter.FavouriteAdapter
import com.example.library.model.Favourite
import com.google.firebase.firestore.FirebaseFirestore

class FavouritesActivity : AppCompatActivity() {

    lateinit var recyclerFav:RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var favouriteAdapter: FavouriteAdapter

    var favList = arrayListOf<Favourite>()
    var id:String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        recyclerFav = findViewById(R.id.recyclerFav)
        layoutManager = GridLayoutManager(this@FavouritesActivity,2)

        readData()
    }

    fun readData(){
        val db = FirebaseFirestore.getInstance()

        if (intent!=null){
            id = intent.getStringExtra("id")
        }
        db.collection("users")
            .get()
            .addOnCompleteListener{
                for (document in it.result!!){
                    Log.d("document","${document.data}")
                    val favDetails = Favourite(
                        id.toString(),
                        document.data.getValue("title") as String,
                        document.data.getValue("link") as String,
                        document.data.getValue("authorName") as String,
                        document.data.getValue("listPrice") as String
                    )

                    favList.add(favDetails)
                    favouriteAdapter = FavouriteAdapter(this,favList)
                    recyclerFav.adapter = favouriteAdapter
                    recyclerFav.layoutManager = layoutManager
                }
            }
    }
}