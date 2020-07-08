package com.luisg.duckhunt

import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.DisplayMetrics
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_game.*


class GameActivity : AppCompatActivity() {

    private var counter = 0
    private var screenHeight = 0
    private var screenWidht = 0
    private var isGameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

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
            }
        }.start()
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
            setNegativeButton("Cancelar",
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                    finish()
                })
        }

        val dialog: AlertDialog? = builder?.create()
        dialog?.show()
    }

    private fun initScreen() {
        //1. Obtener el tamaño de la pantalla del dispositivo
        val metrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(metrics)
        screenHeight = metrics.heightPixels
        screenWidht = metrics.widthPixels
    }

    private fun events() {
        val nick = intent.extras?.getString("nick")
        txtNickName.text = nick

        imageDuck.setOnClickListener {
            if (!isGameOver) {
                counter++
                txtCounterDuck.text = counter.toString()
                imageDuck.setImageResource(R.drawable.duck_clicked)
                val handler = Handler().postDelayed(Runnable {
                    imageDuck.setImageResource(R.drawable.duck)
                    moveDuck()
                }, 500)
            }
        }
    }

    private fun moveDuck() {
        val min = 0
        val maxHeight = screenHeight - imageDuck.height
        val maxWidth = screenWidht - imageDuck.width

        //Generenado coordenadas aleatorias.
        val coordX = (min..maxWidth).random()
        val coordY = (min..maxHeight).random()

        //Aplicando las coordenadas en la image
        imageDuck.x = coordX.toFloat()
        imageDuck.y = coordY.toFloat()
    }
}

private fun Handler.postDelayed(runnable: Runnable): Any {
    TODO("Not yet implemented")
}
