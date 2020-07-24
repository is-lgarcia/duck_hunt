package com.luisg.duckhunt

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.luisg.duckhunt.model.RankingAdapter
import com.luisg.duckhunt.model.Users
import kotlinx.android.synthetic.main.activity_ranking.*


class RankingActivity : AppCompatActivity() {


    var userList = arrayListOf<Users>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)
        val db = FirebaseFirestore.getInstance()

        imageBack.setOnClickListener {
            onBackPressed()
        }

        val adaper = RankingAdapter(userList)
        val rvRanking = findViewById<RecyclerView>(R.id.rvRanking)
        rvRanking.setHasFixedSize(true)
        rvRanking.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvRanking.adapter = adaper

        db.collection("users")
            .orderBy("ducks", Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener{documents ->
                userList = ArrayList<Users>()

                for (document in documents){
                    val userItem = document.toObject(Users::class.java)
                    userList.add(userItem)
                }
                adaper.putUsers(userList)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error: $exception", Toast.LENGTH_LONG).show()
            }
    }
}