package com.britomartis.android.britobudget

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    val TAG = "MyMessage"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        //declare the animation
        val ttp = AnimationUtils.loadAnimation(this, R.anim.ttp)
        val ttb = AnimationUtils.loadAnimation(this, R.anim.ttb)

        val headertitle = findViewById<TextView>(R.id.tite)
        val sideimage = findViewById<ImageView>(R.id.slide)

        //set the animation

        headertitle.startAnimation(ttp)
        sideimage.startAnimation(ttb)


        feedbackcard.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                data = Uri.parse("mailto:")
                type = "text/plain"
                putExtra(Intent.EXTRA_EMAIL, "teambritomartis@gmail.com")
                putExtra(Intent.EXTRA_SUBJECT, "FeedBack")
            }
            if (intent.resolveActivity(packageManager) != null) {
                intent.setPackage("com.google.android.gm")
                startActivity(intent)
            } else {
                Log.d(TAG, "No app available to send email.")
            }

        }

        chatbotcard.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }


}
