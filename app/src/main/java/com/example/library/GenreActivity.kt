package com.example.library

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.library.connection.CheckConnection
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.SignInAccount
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class GenreActivity : AppCompatActivity() {
    lateinit var btnHero:Button
    lateinit var btnProgramming:Button
    lateinit var btnPython:Button
    lateinit var btnHistory:Button
    lateinit var btnSports:Button
    lateinit var btnEconomics:Button
    lateinit var btnHorror:Button

    lateinit var txtSearch:TextView

    private lateinit var btnSignIn:SignInButton


    private lateinit var auth: FirebaseAuth


    private companion object{
        private const val RC_SIGN_IN=4926
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre)

        // Initialize Firebase Auth
        auth = Firebase.auth

        btnSignIn = findViewById(R.id.btnSignIn)
        btnHero =findViewById(R.id.btnHero)
        btnProgramming =findViewById(R.id.btnProgramming)
        btnPython =findViewById(R.id.btnPython)
        btnHistory =findViewById(R.id.btnHistory)
        btnSports =findViewById(R.id.btnSports)
        btnEconomics =findViewById(R.id.btnEconomics)
        btnHorror =findViewById(R.id.btnHorror)
        txtSearch = findViewById(R.id.txtSearch)

        if (CheckConnection().checkConnectivity(this)){
            buttonClick()


        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val client: GoogleSignInClient = GoogleSignIn.getClient(this, gso)
        btnSignIn.setOnClickListener {
            val signInIntent = client.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        }else{
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not Found")
            dialog.setCancelable(false)
            dialog.setPositiveButton("Open Settings"){text , listener ->
                //opening settings to make the connection available
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()

            }
            dialog.setNegativeButton("Exit"){text,listener ->
                //exiting the app
                ActivityCompat.finishAffinity(this)
            }
            dialog.create()
            dialog.show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
            }
        }
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
            Toast.makeText(this, "not logged in", Toast.LENGTH_SHORT).show()
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    updateUI(null)
                }
            }
    }


    private fun buttonClick(){

        txtSearch.setOnClickListener{
            val intent = Intent(this@GenreActivity,SearchActivity::class.java)
            startActivity(intent)
        }

        btnHero.setOnClickListener{
            val intent = Intent(this@GenreActivity, BooksActivity::class.java)
            intent.putExtra("key","hero")
            startActivity(intent)

        }
        btnProgramming.setOnClickListener{
            val intent = Intent(this@GenreActivity, BooksActivity::class.java)
            intent.putExtra("key","programming")
            startActivity(intent)

            }
        btnPython.setOnClickListener{
            val intent = Intent(this@GenreActivity, BooksActivity::class.java)
            intent.putExtra("key","python")
            startActivity(intent)

        }
        btnHistory.setOnClickListener{
            val intent = Intent(this@GenreActivity, BooksActivity::class.java)
            intent.putExtra("key","history")
            startActivity(intent)

        }
        btnSports.setOnClickListener{
            val intent = Intent(this@GenreActivity, BooksActivity::class.java)
            intent.putExtra("key","sports")
            startActivity(intent)

        }
        btnEconomics.setOnClickListener{
            val intent = Intent(this@GenreActivity, BooksActivity::class.java)
            intent.putExtra("key","economics")
            startActivity(intent)

        }
        btnHorror.setOnClickListener{
            val intent = Intent(this@GenreActivity, BooksActivity::class.java)
            intent.putExtra("key","horror")
            startActivity(intent)

        }

    }
}