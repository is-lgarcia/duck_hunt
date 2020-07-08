package com.luisg.duckhunt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var nick: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_start.setOnClickListener {
            nick = edit_nick.text.toString()

            if (nick.isEmpty()){
                edit_nick.error = "Campo de usuario es obligatorio"
            } else{
                edit_nick.setText("")
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra("nick", nick)
                startActivity(intent)
            }
        }
    }
}