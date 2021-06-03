package com.example.library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.library.adapter.BookActivityAdapter
import com.example.library.model.Book

class BooksActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var bookAdapter:BookActivityAdapter

    val bookList = arrayListOf<Book>()

    var genre: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books)

        recyclerView = findViewById(R.id.recyclerView)
        layoutManager = LinearLayoutManager(this)

        if (intent!=null){
            genre = intent.getStringExtra("key")
        }
        Toast.makeText(this, genre, Toast.LENGTH_SHORT).show()


        val queue = Volley.newRequestQueue(this)
        val url = "https://www.googleapis.com/books/v1/volumes?q=${"$genre"}"

        val jsonRequest = object : JsonObjectRequest(Request.Method.GET,url,null,
            Response.Listener {
                //Responses are handled here

                try {

                    val itemsArray = it.getJSONArray("items")

                    for (i in 0 until itemsArray.length()){

                        val bookObject = itemsArray.getJSONObject(i)

                        val volumeInfo = bookObject.getJSONObject("volumeInfo")

                        //for image loading
                        val imageLinks = volumeInfo.optJSONObject("imageLinks")
                        var imageUrl:String?=""
                        var link:String?=""
                        if (imageLinks != null && imageLinks.has("thumbnail")){
                            imageUrl = imageLinks.getString("thumbnail")
                            link = "https:${imageUrl?.substring(5,imageUrl.length)}"
                        }else{
                            link="https://cdn1.iconfinder.com/data/icons/get-me-home/512/square_blank_check_empty-128.png"
                        }

                        //for displaying author name
                        val author = volumeInfo.optJSONArray("authors")
                        var authorName:String?=""
                        if (author != null){
                            if (author.length() >= 1){
                                authorName ="By- " + author.getString(0)
                            }
                        }else{
                            authorName="No Author"
                        }

                        //for displaying the book price
                        val saleInfo = bookObject.optJSONObject("saleInfo")
                        var amount: Double?
                        var listPrice: String?
                        if (saleInfo != null && saleInfo.has("listPrice")){
                            amount = saleInfo.getJSONObject("listPrice").getDouble("amount")
                            listPrice = "$amount USD"
                        }else{
                            listPrice = "NOT_FOR_SALE"
                        }

                        //for book id
                        val id = bookObject.getString("id")

                        val bookDetails = Book(
                            link,
                            volumeInfo.getString("title"),
                            authorName.toString(),
                            listPrice,
                            id
                        )
                        bookList.add(bookDetails)
                        bookAdapter = BookActivityAdapter(this,bookList)
                        recyclerView.adapter = bookAdapter
                        recyclerView.layoutManager = layoutManager
                    }

                }catch (e:Exception){

                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }

            },Response.ErrorListener {
               //Errors are handled here

                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            })
                {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String,String>()
                        headers["Content-type"] = "application/json"
                        headers["Token"] = "AIzaSyCNSUKAql6LOa1J03sGohQBLU0tdBIQe2Q"
                        return headers
                    }
                }

        queue.add(jsonRequest)
    }

}