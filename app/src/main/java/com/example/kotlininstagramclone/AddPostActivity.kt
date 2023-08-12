package com.example.kotlininstagramclone


import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kotlininstagramclone.databinding.ActivityAddPostBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso


class AddPostActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAddPostBinding
    private lateinit var photoUri : Uri
    private lateinit var pickMedia :ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var db : FirebaseFirestore
    private lateinit var auth:FirebaseAuth
    private lateinit var storage:FirebaseStorage
    private lateinit var storageReferences:StorageReference
    private lateinit var userName:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        register()
        db=Firebase.firestore
        auth=Firebase.auth
        storage=Firebase.storage
        storageReferences=storage.reference


    }

    fun addPost_OnClick(view:View){
        //Burada fotoğraf seçme ve yükleme işlemi yapılacaktır.

        var email=auth.currentUser!!.email
        var comment=binding.editTextPostComment.text.toString()
        var time=Timestamp.now().seconds.toString()

        storageReferences.child("images/"+time).putFile(photoUri).addOnSuccessListener(){
            it.storage.downloadUrl.addOnSuccessListener {
                //Burada kaydedilen verinin bağlantısını alıyoruz.
                var data= hashMapOf("email" to email,
                                    "comment" to comment,
                                    "photo" to it.toString(),
                                    "time" to time.toLong())
                db.collection("Posts").document().set(data).addOnSuccessListener {
                    Toast.makeText(this@AddPostActivity, "İşlem başarılı", Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener(){
            Toast.makeText(this@AddPostActivity, it.localizedMessage, Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    fun selectedPhoto_OnClick(view :View){
        //Burada resmi seçip gösterme işlemi yapılacaktır.
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun register(){
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                photoUri=uri
                println("Resim yolu::${photoUri}")
                Picasso.get().load(photoUri).into(binding.imageView)
            }
        }
    }

}