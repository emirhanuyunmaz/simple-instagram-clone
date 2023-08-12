package com.example.kotlininstagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlininstagramclone.databinding.ActivityPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PostActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPostBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var adapter :PostActivityRecyclerViewAdapter
    private lateinit var posts:ArrayList<Posts>
    private lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarPosts)
        auth=Firebase.auth
        db=Firebase.firestore
        getData()
        posts=ArrayList()
        adapter= PostActivityRecyclerViewAdapter(posts)
        binding.recyclerViewPosts.layoutManager=LinearLayoutManager(this@PostActivity)
        binding.recyclerViewPosts.adapter=adapter


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflate=menuInflater
        menuInflate.inflate(R.menu.post_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.menuSignOut){
            //Kullanıcı çıkış işlemi yapılacak.
            auth.signOut()
            var intent=Intent(this@PostActivity,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)


        }else if(item.itemId==R.id.menuAddPost){
            //burada kullanıcının yeni bir gönderi ekleme sayfasına gönderme işlemi yapıyoruz.
            var intent= Intent(this@PostActivity,AddPostActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getData(){
        db.collection("Posts").orderBy("time",Query.Direction.DESCENDING).addSnapshotListener(){ snapshot, e ->
            if(e!=null){
                println("bir hata ile karşılaşıldı"+e.message)
            }else{
                posts.clear()
                for(doc in snapshot!!){
                    var email=doc.getString("email")
                    var images=doc.getString("photo")
                    var comment=doc.getString("comment")
                    var post=Posts(email!!,images!!,comment!!)
                    posts.add(post)
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

}