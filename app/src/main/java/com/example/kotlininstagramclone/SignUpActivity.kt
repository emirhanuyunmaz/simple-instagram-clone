package com.example.kotlininstagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.kotlininstagramclone.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySignUpBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var db:FirebaseFirestore
    private lateinit var userName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=Firebase.auth
        db=Firebase.firestore
        getUserName()



    }

    fun newSignUp_OnClick(view : View){
        //Yeni üye ile üye olma ve giriş yapma işlemleri
        if(binding.newEditTextEmail.text!!.isEmpty() || binding.newEditTextPassword.text!!.isEmpty() || binding.newEditTextUserName.text!!.isEmpty() || binding.newEditTextNameSurname.text!!.isEmpty()){
            println("booooşşşş")
        }else{
            //Yeni üye kaydı yapılacak

            var email=binding.newEditTextEmail.text.toString()
            var password=binding.newEditTextPassword.text.toString()

            var data= hashMapOf("userName" to binding.newEditTextUserName.text.toString(),
                                "userEmail" to binding.newEditTextEmail.text.toString(),
                                "nameSurname" to binding.newEditTextNameSurname.text.toString())
            db.collection("users").document().set(data).addOnSuccessListener {
                auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                    var intent= Intent(this@SignUpActivity,PostActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }.addOnFailureListener(){
                Toast.makeText(this@SignUpActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun getUserName(){
        db.collection("Users").addSnapshotListener(){ snapshot, e ->
            if(e!=null){
            }else{
                for(doc in snapshot!!){
                    if(auth.currentUser!!.email.equals(doc.getString("email").toString())){
                        userName=doc.getString("userName").toString()
                    }
                }
            }
        }
    }
}