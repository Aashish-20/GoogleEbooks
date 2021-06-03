package com.example.library

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import org.w3c.dom.Document

class DescriptionActivity : AppCompatActivity() {

    lateinit var imgBookDesc:ImageView
    lateinit var txtBookTitleDesc:TextView
    lateinit var txtAuthorDesc:TextView
    lateinit var txtPriceDesc:TextView
    lateinit var txtBookDescription:TextView
    lateinit var btnAddToFav:Button
    lateinit var btnRemoveFav:Button
    lateinit var toolbar: Toolbar

    lateinit var edtNote:EditText
    lateinit var btnMarkNote:Button
    lateinit var txtNote:TextView

    var id:String?=""

    var link:String?=""
    var authorName:String?=""
    var listPrice: String?=""
    var title:String?=""

    var note:String?=""

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        auth = Firebase.auth

        toolbar = findViewById(R.id.toolbar)

        setupToolbar()
        supportActionBar?.title = "About Book"

        edtNote = findViewById(R.id.edtNote)
        btnMarkNote = findViewById(R.id.btnMarkNote)
        txtNote = findViewById(R.id.txtNote)

        imgBookDesc = findViewById(R.id.imgBookDesc)
        txtBookTitleDesc = findViewById(R.id.txtBookTitleDesc)
        txtAuthorDesc = findViewById(R.id.txtAuthorDesc)
        txtPriceDesc = findViewById(R.id.txtPriceDesc)
        txtBookDescription = findViewById(R.id.txtBookDescription)

        btnAddToFav = findViewById(R.id.btnAddToFav)
        btnRemoveFav = findViewById(R.id.btnRemoveFav)

        if (intent!=null){
            id = intent.getStringExtra("id")

            bookDescription(id)

            val db = FirebaseFirestore.getInstance()
            db.collection("users").whereEqualTo("id",id)
                .get()
                .addOnCompleteListener{
                    for (document in it.result!!){
                        note= document.data.getValue("note") as String
                    }
                    txtNote.text = note
                }

            btnMarkNote.setOnClickListener {
                note = edtNote.text.toString()
                saveFireStore(id.toString(),title.toString(),link.toString(),
                    authorName.toString(), listPrice.toString(),note.toString())
                val db = FirebaseFirestore.getInstance()
                db.collection("users").whereEqualTo("id",id)
                    .get()
                    .addOnCompleteListener{
                        for (document in it.result!!){
                           note= document.data.getValue("note") as String
                        }
                        txtNote.text = note
                    }
                edtNote.text.clear()
            }

            btnAddToFav.setOnClickListener {
                saveFireStore(id.toString(),title.toString(),link.toString(),
                    authorName.toString(), listPrice.toString(),note.toString())
            }
            btnRemoveFav.setOnClickListener {
                val db = FirebaseFirestore.getInstance()
                db.collection("users").whereEqualTo("id",id)
                    .get()
                    .addOnSuccessListener {documents->

                        for (document in documents){
                            db.collection("users").document(document.id).delete()
                        }
                        Toast.makeText(this, "removed successfully", Toast.LENGTH_SHORT).show()

                    }
                    .addOnFailureListener{
                        Toast.makeText(
                            this,
                            "please add the book first in favourites",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

            }


        }


    }

    private fun bookDescription(id: String?) {

            val queue = Volley.newRequestQueue(this)
            val url = "https://www.googleapis.com/books/v1/volumes?q=${id}"

            val jsonRequest = object : JsonObjectRequest(Request.Method.GET,url,null,
                Response.Listener {
                    //Responses are handled here

                    try {

                        val itemsArray = it.getJSONArray("items")

                        for (i in 0 until itemsArray.length()){

                            val bookObject = itemsArray.getJSONObject(i)

                            val volumeInfo = bookObject.optJSONObject("volumeInfo")

                            //for title
                            txtBookTitleDesc.text = volumeInfo.getString("title")
                            title=volumeInfo.getString("title")

                            //for image loading
                            val imageLinks = volumeInfo.optJSONObject("imageLinks")
                            var imageUrl:String?=""

                            if (imageLinks != null && imageLinks.has("thumbnail")){
                                imageUrl = imageLinks.getString("thumbnail")
                                link = "https:${imageUrl?.substring(5,imageUrl.length)}"
                                Picasso.get().load(link).error(R.drawable.ic_launcher_background).into(imgBookDesc)

                            }else{
                                link="https://cdn1.iconfinder.com/data/icons/get-me-home/512/square_blank_check_empty-128.png"
                                Picasso.get().load(link).error(R.drawable.ic_launcher_background).into(imgBookDesc)
                            }

                            //for displaying author name
                            val author = volumeInfo.optJSONArray("authors")

                            if (author != null){
                                if (author.length() >= 1){
                                    authorName ="By- " + author.getString(0)
                                    txtAuthorDesc.text = authorName
                                }
                            }else{
                                authorName="No Author"
                                txtAuthorDesc.text = authorName
                            }

                            //for displaying the book price
                            val saleInfo = bookObject.optJSONObject("saleInfo")
                            var amount: Double?

                            if (saleInfo != null && saleInfo.has("listPrice")){
                                amount = saleInfo.getJSONObject("listPrice").getDouble("amount")
                                listPrice = "$amount USD"
                                txtPriceDesc.text = listPrice
                            }else{
                                listPrice = "NOT_FOR_SALE"
                                txtPriceDesc.text = listPrice
                            }

                            //for description of the book
                            var description:String?
                            if (volumeInfo != null && volumeInfo.has("description")){
                                description = volumeInfo.getString("description")
                                txtBookDescription.text = description
                            }else{
                                description = "NO_DESCRIPTION_FOR_THIS_BOOK"
                                txtBookDescription.text = description
                            }

                        }


                    }catch (e:Exception){

                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    }

                }, Response.ErrorListener {
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

    fun saveFireStore(id:String,title:String,link:String,authorName:String,listPrice:String,note:String){

        val db = FirebaseFirestore.getInstance()
        val user:MutableMap<String,Any> = HashMap()
        user["id"]=id
        user["title"]=title
        user["link"]=link
        user["authorName"]=authorName
        user["listPrice"]=listPrice
        user["note"]=note
        db.collection("users")
            .add(user)
            .addOnSuccessListener {
                Toast.makeText(this, "saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favourite,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.btnFavourites){
            val intent = Intent(this,FavouritesActivity::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setupToolbar(){
        setSupportActionBar(toolbar)
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser == null) {
            Log.w("error", "not going to login")
            edtNote.visibility = View.GONE
            btnMarkNote.visibility = View.GONE
            txtNote.visibility = View.GONE
            Toast.makeText(this, "not logged in", Toast.LENGTH_SHORT).show()
        } else {
            edtNote.visibility = View.VISIBLE
            btnMarkNote.visibility = View.VISIBLE
            txtNote.visibility = View.VISIBLE
        }
    }


}
