package com.luisg.duckhunt

import android.content.DialogInterface
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.DisplayMetrics
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_game.*


class GameActivity : AppCompatActivity() {


    private var counter = 0
    private var screenHeight = 0
    private var screenWidth = 0
    private var isGameOver = false
    private lateinit var idUsers: String
    private lateinit var db: FirebaseFirestore
    var shooter: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        db = FirebaseFirestore.getInstance()

        events()
        initScreen()
        initCountDownTime()


    }

    private fun initCountDownTime() {
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                txtTime.text = "${millisUntilFinished / 1000}s"
            }

            override fun onFinish() {
                txtTime.text = "0s"
                isGameOver = true
                showDialogGameOver()
                saveResultFirestore()
            }
        }.start()
    }

    private fun saveResultFirestore() {
        db.collection("users").document(idUsers).update("ducks",counter)
    }

    private fun showDialogGameOver() {
        val builder: AlertDialog.Builder? = this.let {
            AlertDialog.Builder(it)
        }
        builder?.setMessage("Has conseguido cazar $counter patos")
            ?.setTitle("Game Over")
        builder?.apply {
            setPositiveButton("Reiniciar",
                DialogInterface.OnClickListener { dialog, id ->
                    counter = 0
                    txtCounterDuck.text = counter.toString()
                    isGameOver = false
                    initCountDownTime()
                    moveDuck()
                })
            setNegativeButton("Ver Ranking",
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                    showRanking()
                })
        }

        val dialog: AlertDialog? = builder?.create()
        dialog?.show()
    }

    private fun showRanking() {
        startActivity(Intent(this,RankingActivity::class.java))
    }


    private fun initScreen() {
        //1. Obtener el tamaño de la pantalla del dispositivo
        val metrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(metrics)
        screenHeight = metrics.heightPixels
        screenWidth = metrics.widthPixels
    }



    private fun events() {
        val nick = intent.extras?.getString("nick")
        idUsers = intent.extras?.getString("id")!!
        txtNickName.text = nick

        imageDuck.setOnClickListener {
            imageDuck.isClickable = false
            if (!isGameOver) {
                shooter = MediaPlayer.create(this, R.raw.duck_sound)
                shooter?.start()
                counter++
                txtCounterDuck.text = counter.toString()
                imageDuck.setImageResource(R.drawable.duck_clicked)
                val handler = Handler().postDelayed(Runnable {
                    imageDuck.setImageResource(R.drawable.duck)
                    moveDuck()
                    shooter?.release()
                    shooter = null
                }, 500)
            }
        }
    }

    private fun moveDuck() {
        val min = 0
        val maxHeight = screenHeight - imageDuck.height
        val maxWidth = screenWidth - imageDuck.width

        //Generenado coordenadas aleatorias.
        val coordX = (min..maxWidth).random()
        val coordY = (min..maxHeight).random()

        //Aplicando las coordenadas en la image
        imageDuck.x = coordX.toFloat()
        imageDuck.y = coordY.toFloat()
        imageDuck.isClickable = true
    }
}
