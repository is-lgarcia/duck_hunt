package com.luisg.duckhunt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.luisg.duckhunt.model.Users
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var nick: String = ""
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Init Firestore
        db = FirebaseFirestore.getInstance()


        btn_start.setOnClickListener {
            nick = edit_nick.text.toString()

            if (nick.isEmpty()){
                edit_nick.error = "Campo de usuario es obligatorio"
            } else{

                addNickAndStart()
            }
        }
    }

    private fun addNickAndStart() {

        db.collection("users").whereEqualTo("nick", nick)
            .get()
            .addOnSuccessListener(OnSuccessListener {
                if (it.size() > 0){
                    edit_nick.error = "El nick no está disponible."
                } else {
                    addToNickFirestore()
                }
            })
    }

    private fun addToNickFirestore() {

        db.collection("users").add(Users(nick,0))
            .addOnSuccessListener(OnSuccessListener {
                edit_nick.setText("")
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra("nick", nick)
                intent.putExtra("id",it.id)
                startActivity(intent)
            })


    }
}