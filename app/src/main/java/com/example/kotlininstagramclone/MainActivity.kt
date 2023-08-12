package com.example.kotlininstagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.kotlininstagramclone.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    private lateinit var auth:FirebaseAuth
    private lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)//Bunu menu olusturmak için tanımlıyoruz.
        auth=Firebase.auth
        db=Firebase.firestore

        //Burada kayıtlı bir kullanıcı varsa tekrar giriş yapmasını engellemek için oluşturuyoruz
        val currentUser=auth.currentUser
        if(currentUser!=null){
            var intent =Intent(this@MainActivity,PostActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun signUp_OnClick(view : View){
        val intent =Intent(this@MainActivity,SignUpActivity::class.java)
        startActivity(intent)
    }

    fun signIn_OnClick(view : View){
        //Kayıtlı bir kullanıcı giriş yapacak.

        if(binding.editTextEmail.text!!.isEmpty() ||  binding.editTextPassword.editText!!.text.isEmpty()){
            Toast.makeText(this@MainActivity, "Lütfen tüm boşlukları doldurunuz.", Toast.LENGTH_SHORT).show()
        }else{
            var email=binding.editTextEmail.text.toString()
            var password=binding.editTextPassword.editText!!.text.toString()

            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                var intent =Intent(this@MainActivity,PostActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener{
                Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }


}